package com.ruoyi.system.service.impl;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.exception.LangException;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.CopyTradeOrder;
import com.ruoyi.system.domain.CopyTradeRelation;
import com.ruoyi.system.domain.CopyTradeTrader;
import com.ruoyi.system.domain.ScheduledTaskExceptionLog;
import com.ruoyi.system.domain.UserCryptocurrencyPosition;
import com.ruoyi.system.mapper.CopyTradeOrderMapper;
import com.ruoyi.system.mapper.CopyTradeRelationMapper;
import com.ruoyi.system.mapper.CopyTradeTraderMapper;
import com.ruoyi.system.mapper.ScheduledTaskExceptionLogMapper;
import com.ruoyi.system.service.ICopyTradeService;
import com.ruoyi.system.service.IUserCryptocurrencyPositionService;
import com.ruoyi.system.utils.UserApiKeyUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 跟单业务实现类。
 * 负责跟单关系管理，以及交易员开平仓后的自动同步。
 */
@Service
public class CopyTradeServiceImpl implements ICopyTradeService {
    /** 交易员数据访问层。 */
    @Resource
    private CopyTradeTraderMapper copyTradeTraderMapper;

    /** 跟单关系数据访问层。 */
    @Resource
    private CopyTradeRelationMapper copyTradeRelationMapper;

    /** 跟单订单映射数据访问层。 */
    @Resource
    private CopyTradeOrderMapper copyTradeOrderMapper;

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
    private ICopyTradeService selfCopyTradeService;

    /** 查询交易员列表。 */
    @Override
    public List<CopyTradeTrader> selectCopyTradeTraderList(CopyTradeTrader copyTradeTrader) {
        return copyTradeTraderMapper.selectCopyTradeTraderList(copyTradeTrader);
    }

    /** 查询单个交易员。 */
    @Override
    public CopyTradeTrader selectCopyTradeTraderById(Long id) {
        return copyTradeTraderMapper.selectCopyTradeTraderById(id);
    }

    /** 新增交易员配置。 */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertCopyTradeTrader(CopyTradeTrader copyTradeTrader) {
        // 新增时统一补齐创建和更新时间。
        copyTradeTrader.setCreateTime(new Date());
        copyTradeTrader.setUpdateTime(new Date());
        return copyTradeTraderMapper.insertCopyTradeTrader(copyTradeTrader);
    }

    /** 修改交易员配置。 */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateCopyTradeTrader(CopyTradeTrader copyTradeTrader) {
        // 修改时刷新更新时间，便于审计。
        copyTradeTrader.setUpdateTime(new Date());
        return copyTradeTraderMapper.updateCopyTradeTrader(copyTradeTrader);
    }

    /** 批量删除交易员配置。 */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteCopyTradeTraderByIds(Long[] ids) {
        return copyTradeTraderMapper.deleteCopyTradeTraderByIds(ids);
    }

    /** 查询跟单关系列表。 */
    @Override
    public List<CopyTradeRelation> selectCopyTradeRelationList(CopyTradeRelation copyTradeRelation) {
        return copyTradeRelationMapper.selectCopyTradeRelationList(copyTradeRelation);
    }

    /** 查询单条跟单关系。 */
    @Override
    public CopyTradeRelation selectCopyTradeRelationById(Long id) {
        return copyTradeRelationMapper.selectCopyTradeRelationById(id);
    }

    /** 批量删除跟单关系。 */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteCopyTradeRelationByIds(Long[] ids) {
        return copyTradeRelationMapper.deleteCopyTradeRelationByIds(ids);
    }

    /** 创建或恢复跟单关系。 */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult followTrader(CopyTradeRelation copyTradeRelation) {
        // 当前登录用户就是跟单人。
        Long followerUserId = UserApiKeyUtils.getUserId();
        // 必须明确指定要跟随哪个交易员。
        if (copyTradeRelation.getTraderId() == null) {
            throw new LangException("hint_copyTradeSelectTrader", "请选择要跟随的交易员");
        }
        // 加载交易员配置并校验其可用状态。
        CopyTradeTrader trader = copyTradeTraderMapper.selectCopyTradeTraderById(copyTradeRelation.getTraderId());
        if (trader == null || trader.getStatus() == null || !trader.getStatus().equals(0)) {
            throw new LangException("hint_copyTradeTraderNotAvailable", "交易员不存在或已停用");
        }
        // 禁止用户自己跟随自己，避免出现自复制逻辑。
        if (trader.getUserId().equals(followerUserId)) {
            throw new LangException("hint_copyTradeCanNotFollowSelf", "不能跟随自己");
        }
        // 先查询是否存在历史关系，支持“停止后恢复”。
        CopyTradeRelation exists = copyTradeRelationMapper.selectRelationByTraderAndFollower(trader.getUserId(), followerUserId);
        if (exists != null && exists.getStatus() != null && exists.getStatus().equals(0)) {
            throw new LangException("hint_copyTradeAlreadyFollowing", "您已经在跟随该交易员");
        }
        // 如果交易员设置了最大人数，需要先校验名额。
        if (trader.getMaxFollowerCount() != null && trader.getMaxFollowerCount() > 0) {
            int currentFollowerCount = copyTradeRelationMapper.countActiveFollowerByTraderUserId(trader.getUserId());
            if (currentFollowerCount >= trader.getMaxFollowerCount()) {
                throw new LangException("hint_copyTradeFollowerLimitReached", "该交易员跟单名额已满");
            }
        }
        // 没有传模式时，自动使用交易员默认模式。
        Integer followMode = copyTradeRelation.getFollowMode();
        if (followMode == null) {
            followMode = trader.getDefaultFollowMode() == null ? 1 : trader.getDefaultFollowMode();
            copyTradeRelation.setFollowMode(followMode);
        }
        // 目前只支持固定金额和按比例两种模式。
        if (!followMode.equals(0) && !followMode.equals(1)) {
            throw new LangException("hint_copyTradeModeError", "跟单模式错误");
        }
        if (followMode.equals(0)) {
            // 固定金额模式必须传入有效金额。
            if (copyTradeRelation.getFollowAmount() == null || copyTradeRelation.getFollowAmount().compareTo(BigDecimal.ZERO) <= 0) {
                throw new LangException("hint_copyTradeInputFollowAmount", "请输入固定跟单金额");
            }
            // 固定金额不能小于交易员要求的门槛。
            if (trader.getMinFollowAmount() != null && copyTradeRelation.getFollowAmount().compareTo(trader.getMinFollowAmount()) < 0) {
                throw new LangException("hint_copyTradeAmountLtMin", "跟单金额不能低于交易员要求的最小金额");
            }
            // 固定金额也不能高于交易员要求的上限。
            if (trader.getMaxFollowAmount() != null && trader.getMaxFollowAmount().compareTo(BigDecimal.ZERO) > 0
                    && copyTradeRelation.getFollowAmount().compareTo(trader.getMaxFollowAmount()) > 0) {
                throw new LangException("hint_copyTradeAmountGtMax", "跟单金额不能高于交易员要求的最大金额");
            }
        } else {
            // 比例模式如果没传比例，则回退到交易员默认比例。
            if (copyTradeRelation.getFollowRatio() == null || copyTradeRelation.getFollowRatio().compareTo(BigDecimal.ZERO) <= 0) {
                copyTradeRelation.setFollowRatio(trader.getDefaultFollowRatio() == null ? BigDecimal.ONE : trader.getDefaultFollowRatio());
            }
        }
        // 不传最大持仓数时，给一个保守默认值。
        if (copyTradeRelation.getMaxOpenOrders() == null || copyTradeRelation.getMaxOpenOrders() <= 0) {
            copyTradeRelation.setMaxOpenOrders(10);
        }
        // 系统内部字段由后端统一写入，避免前端伪造。
        copyTradeRelation.setTraderUserId(trader.getUserId());
        copyTradeRelation.setFollowerUserId(followerUserId);
        copyTradeRelation.setStatus(0);
        copyTradeRelation.setLastFollowTime(new Date());
        copyTradeRelation.setUpdateTime(new Date());
        int count;
        if (exists != null) {
            // 有历史关系时直接复用原记录，避免唯一索引冲突。
            copyTradeRelation.setId(exists.getId());
            count = copyTradeRelationMapper.updateCopyTradeRelation(copyTradeRelation);
        } else {
            // 首次创建时补齐创建时间。
            copyTradeRelation.setCreateTime(new Date());
            count = copyTradeRelationMapper.insertCopyTradeRelation(copyTradeRelation);
        }
        if (count <= 0) {
            throw new ServiceException("创建跟单关系失败");
        }
        return AjaxResult.success().put("relationId", copyTradeRelation.getId() == null ? exists.getId() : copyTradeRelation.getId());
    }

    /** 停止跟单。 */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult unfollowTrader(Long relationId) {
        // 停止跟单必须先拿到关系主键。
        if (relationId == null) {
            throw new LangException("hint_copyTradeSelectRelation", "请选择要停止的跟单关系");
        }
        Long followerUserId = UserApiKeyUtils.getUserId();
        // 校验关系是否存在。
        CopyTradeRelation relation = copyTradeRelationMapper.selectCopyTradeRelationById(relationId);
        if (relation == null) {
            throw new LangException("hint_copyTradeRelationNotExists", "跟单关系不存在");
        }
        // 只能操作自己的跟单关系。
        if (!relation.getFollowerUserId().equals(followerUserId)) {
            throw new ServiceException("无权操作该跟单关系");
        }
        // 这里只是停止后续跟单，不主动平掉历史已开的仓位。
        CopyTradeRelation update = new CopyTradeRelation();
        update.setId(relationId);
        update.setStatus(1);
        update.setUpdateTime(new Date());
        int count = copyTradeRelationMapper.updateCopyTradeRelation(update);
        if (count <= 0) {
            throw new ServiceException("停止跟单失败");
        }
        return AjaxResult.success();
    }

    /** 处理交易员开仓后的批量跟单。 */
    @Override
    public void handleLeaderOpenPosition(UserCryptocurrencyPosition leaderPosition) {
        // 没有主单ID就无法建立主从映射，直接忽略。
        if (leaderPosition == null || leaderPosition.getId() == null) {
            return;
        }
        // 只有被登记为交易员的用户开仓，才需要触发跟单。
        CopyTradeTrader trader = copyTradeTraderMapper.selectActiveCopyTradeTraderByUserId(leaderPosition.getUserId());
        if (trader == null) {
            return;
        }
        // 查询所有处于启用状态的跟单关系。
        List<CopyTradeRelation> relations = copyTradeRelationMapper.selectActiveRelationsByTraderUserId(trader.getUserId());
        for (CopyTradeRelation relation : relations) {
            try {
                // 达到最大同时持仓数时，不再继续为该用户复制新仓位。
                Integer activeOrderCount = copyTradeOrderMapper.countActiveOrderByRelationId(relation.getId());
                if (relation.getMaxOpenOrders() != null && relation.getMaxOpenOrders() > 0 && activeOrderCount >= relation.getMaxOpenOrders()) {
                    continue;
                }
                // 通过代理对象调用，确保新事务真正生效。
                selfCopyTradeService.syncFollowerOpenPosition(relation, leaderPosition);
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
        List<CopyTradeOrder> orders = copyTradeOrderMapper.selectActiveOrdersByLeaderPositionId(2, leaderPosition.getId());
        for (CopyTradeOrder order : orders) {
            try {
                // 通过代理对象调用，保证平仓同步在独立事务中执行。
                selfCopyTradeService.syncFollowerClosePosition(order, leaderPosition);
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
        if (copyTradeOrderMapper.insertCopyTradeOrder(copyTradeOrder) <= 0) {
            throw new ServiceException("保存跟单映射失败");
        }
        // 开仓成功后，刷新关系上的最近跟单时间。
        CopyTradeRelation update = new CopyTradeRelation();
        update.setId(relation.getId());
        update.setLastFollowTime(new Date());
        update.setUpdateTime(new Date());
        copyTradeRelationMapper.updateCopyTradeRelation(update);
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
            copyTradeOrderMapper.updateCopyTradeOrder(update);
        }
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
