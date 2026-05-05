package com.ruoyi.system.service.impl;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.CopyTradeOrder;
import com.ruoyi.system.domain.CopyTradeRelation;
import com.ruoyi.system.domain.CopyTradeTrader;
import com.ruoyi.system.domain.ScheduledTaskExceptionLog;
import com.ruoyi.system.domain.UserCryptocurrencyPosition;
import com.ruoyi.system.mapper.CopyTradeOrderMapper;
import com.ruoyi.system.mapper.ScheduledTaskExceptionLogMapper;
import com.ruoyi.system.service.ICopyTradeOrderService;
import com.ruoyi.system.service.ICopyTradeRelationService;
import com.ruoyi.system.service.ICopyTradeTraderService;
import com.ruoyi.system.service.IUserCryptocurrencyPositionService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 跟单订单映射服务实现类。
 * 负责跟单订单映射表的读写，以及交易员开平仓后的自动同步。
 */
@Service
public class CopyTradeOrderServiceImpl implements ICopyTradeOrderService {
    /** 跟单订单映射数据访问层。 */
    @Resource
    private CopyTradeOrderMapper copyTradeOrderMapper;

    /** 交易员服务。 */
    @Resource
    private ICopyTradeTraderService copyTradeTraderService;

    /** 跟单关系服务。 */
    @Resource
    private ICopyTradeRelationService copyTradeRelationService;

    /** 异常日志记录访问层。 */
    @Resource
    private ScheduledTaskExceptionLogMapper scheduledTaskExceptionLogMapper;

    /** 持仓服务，用于真正执行跟单开仓和平仓。 */
    @Lazy
    @Resource
    private IUserCryptocurrencyPositionService userCryptocurrencyPositionService;

    /** 自身代理对象，用于保证 REQUIRES_NEW 事务注解生效。 */
    @Lazy
    @Resource
    private ICopyTradeOrderService selfCopyTradeOrderService;

    /** 查询跟单订单映射列表。 */
    @Override
    public List<CopyTradeOrder> selectCopyTradeOrderList(CopyTradeOrder copyTradeOrder) {
        return copyTradeOrderMapper.selectCopyTradeOrderList(copyTradeOrder);
    }

    /** 查询单条跟单订单映射。 */
    @Override
    public CopyTradeOrder selectCopyTradeOrderById(Long id) {
        return copyTradeOrderMapper.selectCopyTradeOrderById(id);
    }

    /** 处理交易员开仓后的批量跟单。 */
    @Override
    public void handleLeaderOpenPosition(UserCryptocurrencyPosition leaderPosition) {
        // 没有主单ID就无法建立主从映射，直接忽略。
        if (leaderPosition == null || leaderPosition.getId() == null) {
            return;
        }
        // 只有被登记为交易员的用户开仓，才需要触发跟单。
        CopyTradeTrader trader = copyTradeTraderService.selectActiveCopyTradeTraderByUserId(leaderPosition.getUserId());
        if (trader == null) {
            return;
        }
        // 查询所有处于启用状态的跟单关系。
        List<CopyTradeRelation> relations = copyTradeRelationService.selectActiveRelationsByTraderUserId(trader.getUserId());
        for (CopyTradeRelation relation : relations) {
            try {
                // 达到最大同时持仓数时，不再继续为该用户复制新仓位。
                Integer activeOrderCount = countActiveOrderByRelationId(relation.getId());
                if (relation.getMaxOpenOrders() != null && relation.getMaxOpenOrders() > 0 && activeOrderCount >= relation.getMaxOpenOrders()) {
                    continue;
                }
                // 通过代理对象调用，确保新事务真正生效。
                selfCopyTradeOrderService.syncFollowerOpenPosition(relation, leaderPosition);
            } catch (Exception e) {
                // 某个跟单人失败不能影响其他跟单人，记录后继续。
                recordCopyTradeException("跟单开仓同步", "relationId:" + relation.getId() + ",leaderPositionId:" + leaderPosition.getId(), e);
            }
        }
    }

    /** 处理交易员平仓后的批量跟单平仓。 */
    @Override
    public void handleLeaderClosePosition(UserCryptocurrencyPosition leaderPosition) {
        // 没有主单ID时无法找到映射关系。
        if (leaderPosition == null || leaderPosition.getId() == null) {
            return;
        }
        // 只处理还处于持仓中的跟单映射。
        List<CopyTradeOrder> orders = selectActiveOrdersByLeaderPositionId(2, leaderPosition.getId());
        for (CopyTradeOrder order : orders) {
            try {
                // 通过代理对象调用，保证平仓同步在独立事务中执行。
                selfCopyTradeOrderService.syncFollowerClosePosition(order, leaderPosition);
            } catch (Exception e) {
                // 某条跟单单失败时记录异常，避免影响其他单。
                recordCopyTradeException("跟单平仓同步", "copyOrderId:" + order.getId() + ",leaderPositionId:" + leaderPosition.getId(), e);
            }
        }
    }

    /** 为单个跟单关系同步开仓。 */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void syncFollowerOpenPosition(CopyTradeRelation relation, UserCryptocurrencyPosition leaderPosition) {
        // 先为跟单用户创建实际持仓。
        UserCryptocurrencyPosition followerPosition = userCryptocurrencyPositionService.openCopyTradePosition(relation.getFollowerUserId(), leaderPosition, relation);
        // 再保存主仓位和跟单仓位之间的映射。
        CopyTradeOrder copyTradeOrder = new CopyTradeOrder();
        copyTradeOrder.setRelationId(relation.getId());
        copyTradeOrder.setTraderUserId(relation.getTraderUserId());
        // 当前版本先接入加密货币合约，因此这里固定写产品类型 2。
        // 后续接股票/期货/外汇时，只需要在各自入口写入对应类型即可复用这张映射表。
        copyTradeOrder.setProductType(2);
        copyTradeOrder.setLeaderPositionId(leaderPosition.getId());
        copyTradeOrder.setProductCode(leaderPosition.getProductCode());
        copyTradeOrder.setOrderDirection(leaderPosition.getOrderDirection());
        copyTradeOrder.setBuyOrderPrice(leaderPosition.getBuyOrderPrice());
        copyTradeOrder.setFollowerUserId(relation.getFollowerUserId());
        copyTradeOrder.setFollowerPositionId(followerPosition.getId());
        copyTradeOrder.setFollowMode(relation.getFollowMode());
        copyTradeOrder.setFollowAmount(relation.getFollowAmount());
        copyTradeOrder.setFollowRatio(relation.getFollowRatio());
        // 跟单保证金直接记录子仓位实际花费的保证金，后面做对账时不需要再反推。
        copyTradeOrder.setMarginAmount(calculateMarginAmount(followerPosition));
        copyTradeOrder.setOrderLever(followerPosition.getOrderLever());
        copyTradeOrder.setLeaderOrderCode(leaderPosition.getOrderCode());
        copyTradeOrder.setFollowerOrderCode(followerPosition.getOrderCode());
        copyTradeOrder.setStatus(0);
        copyTradeOrder.setRemark("自动跟单开仓");
        copyTradeOrder.setCreateTime(new Date());
        copyTradeOrder.setUpdateTime(new Date());
        if (insertCopyTradeOrder(copyTradeOrder) <= 0) {
            throw new ServiceException("保存跟单映射失败");
        }
        // 开仓成功后，刷新关系上的最近跟单时间。
        CopyTradeRelation update = new CopyTradeRelation();
        update.setId(relation.getId());
        update.setLastFollowTime(new Date());
        update.setUpdateTime(new Date());
        copyTradeRelationService.updateCopyTradeRelation(update);
    }

    /** 为单个跟单订单同步平仓。 */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void syncFollowerClosePosition(CopyTradeOrder order, UserCryptocurrencyPosition leaderPosition) {
        // 跟单平仓直接复用交易系统现有的强制平仓入口，并沿用主单卖出价。
        AjaxResult result = userCryptocurrencyPositionService.sell(order.getFollowerPositionId(), 0, leaderPosition.getSellOrderPrice());
        // 正常情况下接口会返回成功，这里额外兜底检查数据库状态。
        boolean closed = result.get(AjaxResult.CODE_TAG).equals(200);
        if (!closed) {
            UserCryptocurrencyPosition followerPosition = userCryptocurrencyPositionService.selectUserCryptocurrencyPositionById(order.getFollowerPositionId());
            closed = followerPosition != null && followerPosition.getOrderStatus() != null && followerPosition.getOrderStatus().equals(1);
        }
        if (closed) {
            // 平仓成功后关闭映射，避免重复平仓。
            CopyTradeOrder update = new CopyTradeOrder();
            update.setId(order.getId());
            update.setStatus(1);
            update.setRemark("交易员平仓后自动同步");
            update.setUpdateTime(new Date());
            updateCopyTradeOrder(update);
        }
    }

    /** 查询某个主单下当前持仓中的跟单映射。 */
    @Override
    public List<CopyTradeOrder> selectActiveOrdersByLeaderPositionId(Integer productType, Long leaderPositionId) {
        return copyTradeOrderMapper.selectActiveOrdersByLeaderPositionId(productType, leaderPositionId);
    }

    /** 统计某条关系下当前持仓中的跟单单数量。 */
    @Override
    public int countActiveOrderByRelationId(Long relationId) {
        return copyTradeOrderMapper.countActiveOrderByRelationId(relationId);
    }

    /** 新增跟单订单映射。 */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertCopyTradeOrder(CopyTradeOrder copyTradeOrder) {
        return copyTradeOrderMapper.insertCopyTradeOrder(copyTradeOrder);
    }

    /** 修改跟单订单映射。 */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateCopyTradeOrder(CopyTradeOrder copyTradeOrder) {
        return copyTradeOrderMapper.updateCopyTradeOrder(copyTradeOrder);
    }

    /** 批量删除跟单订单映射。 */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteCopyTradeOrderByIds(Long[] ids) {
        return copyTradeOrderMapper.deleteCopyTradeOrderByIds(ids);
    }

    /** 记录跟单同步过程中的异常日志，便于后续排查。 */
    private void recordCopyTradeException(String jobName, String relateInfo, Exception e) {
        ScheduledTaskExceptionLog scheduledTaskExceptionLog = new ScheduledTaskExceptionLog();
        scheduledTaskExceptionLog.setJobName(jobName);
        scheduledTaskExceptionLog.setExceptionInfo(e.getMessage());
        scheduledTaskExceptionLog.setExceptionInfoDetail(ExceptionUtil.stacktraceToString(e));
        scheduledTaskExceptionLog.setRelateInfo(relateInfo);
        scheduledTaskExceptionLog.setCreateTime(DateUtils.getNowDate());
        scheduledTaskExceptionLogMapper.insertScheduledTaskExceptionLog(scheduledTaskExceptionLog);
    }

    /** 计算跟单子仓位的保证金快照。 */
    private BigDecimal calculateMarginAmount(UserCryptocurrencyPosition followerPosition) {
        // 金额和杠杆都齐全时，保证金按实际总仓位金额 / 杠杆记录。
        if (followerPosition != null
                && followerPosition.getOrderTotalPrice() != null
                && followerPosition.getOrderLever() != null
                && followerPosition.getOrderLever() > 0) {
            return followerPosition.getOrderTotalPrice()
                    .divide(new BigDecimal(followerPosition.getOrderLever()), Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
        }
        return null;
    }
}
