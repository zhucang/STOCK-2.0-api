package com.ruoyi.system.service.impl;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.CopyTradeOrder;
import com.ruoyi.system.domain.CopyTradePositionSnapshot;
import com.ruoyi.system.domain.CopyTradeRelation;
import com.ruoyi.system.domain.UserCryptocurrencyPosition;
import com.ruoyi.system.domain.UserForexPosition;
import com.ruoyi.system.domain.UserFuturesPosition;
import com.ruoyi.system.domain.UserStockPosition;
import com.ruoyi.system.mapper.CopyTradeOrderMapper;
import com.ruoyi.system.service.ICopyTradeOrderService;
import com.ruoyi.system.service.ICopyTradeRelationService;
import com.ruoyi.system.service.ICopyTradeSyncTaskService;
import com.ruoyi.system.service.IUserCryptocurrencyPositionService;
import com.ruoyi.system.service.IUserForexPositionService;
import com.ruoyi.system.service.IUserFuturesPositionService;
import com.ruoyi.system.service.IUserStockPositionService;
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
 * 负责跟单订单映射表的读写，以及同步任务执行阶段的跟随单开平仓。
 */
@Service
public class CopyTradeOrderServiceImpl implements ICopyTradeOrderService {
    /** 跟单订单映射数据访问层。 */
    @Resource
    private CopyTradeOrderMapper copyTradeOrderMapper;

    /** 跟单关系(跟单人员)服务。 */
    @Resource
    private ICopyTradeRelationService copyTradeRelationService;

    /** 跟单同步任务服务。 */
    @Lazy
    @Resource
    private ICopyTradeSyncTaskService copyTradeSyncTaskService;

    /** 股票持仓服务。 */
    @Lazy
    @Resource
    private IUserStockPositionService userStockPositionService;

    /** 加密货币持仓服务。 */
    @Lazy
    @Resource
    private IUserCryptocurrencyPositionService userCryptocurrencyPositionService;

    /** 期货持仓服务。 */
    @Lazy
    @Resource
    private IUserFuturesPositionService userFuturesPositionService;

    /** 外汇持仓服务。 */
    @Lazy
    @Resource
    private IUserForexPositionService userForexPositionService;

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

    /** 为单个跟单关系(跟单人员)同步开仓。 */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void syncFollowerOpenPosition(CopyTradeRelation relation, CopyTradePositionSnapshot leaderPosition) {
        // 最终幂等保护：同一跟单关系对同一主单只允许生成一条跟单映射，避免任务重试或并发消费重复开仓。
        CopyTradeOrder exists = selectOrderByRelationAndLeaderPosition(leaderPosition.getProductType(), relation.getId(), leaderPosition.getId());
        if (exists != null) {
            return;
        }
        // 先为跟单用户创建实际持仓。
        CopyTradePositionSnapshot followerPosition = openFollowerPosition(relation, leaderPosition);
        // 再保存主仓位和跟单仓位之间的映射。
        CopyTradeOrder copyTradeOrder = new CopyTradeOrder();
        copyTradeOrder.setRelationId(relation.getId());
        copyTradeOrder.setTraderUserId(relation.getTraderUserId());
        copyTradeOrder.setProductType(leaderPosition.getProductType());
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
        // 如果主单在跟单子单创建期间已经平仓，补一条平仓任务，避免慢任务导致跟随单遗留。
        CopyTradePositionSnapshot latestLeaderPosition = selectCopyTradePositionSnapshot(leaderPosition.getProductType(), leaderPosition.getId());
        if (latestLeaderPosition != null && latestLeaderPosition.getOrderStatus() != null && latestLeaderPosition.getOrderStatus().equals(1)) {
            copyTradeSyncTaskService.enqueueCloseSyncTask(copyTradeOrder, latestLeaderPosition);
        }
    }

    /** 为单个跟单订单同步平仓。 */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void syncFollowerClosePosition(CopyTradeOrder order, CopyTradePositionSnapshot leaderPosition) {
        // 跟单平仓直接复用交易系统现有的强制平仓入口，并沿用主单卖出价。
        AjaxResult result = closeFollowerPosition(order, leaderPosition);
        // 正常情况下接口会返回成功，这里额外兜底检查数据库状态。
        boolean closed = result.get(AjaxResult.CODE_TAG).equals(200);
        if (!closed) {
            CopyTradePositionSnapshot followerPosition = selectCopyTradePositionSnapshot(order.getProductType(), order.getFollowerPositionId());
            closed = followerPosition != null && followerPosition.getOrderStatus() != null && followerPosition.getOrderStatus().equals(1);
        }
        if (closed) {
            CopyTradePositionSnapshot followerPosition = selectCopyTradePositionSnapshot(order.getProductType(), order.getFollowerPositionId());
            // 平仓成功后关闭映射，避免重复平仓。
            CopyTradeOrder update = new CopyTradeOrder();
            update.setId(order.getId());
            update.setStatus(1);
            if (followerPosition != null) {
                update.setFollowerSellOrderPrice(followerPosition.getSellOrderPrice());
                update.setFollowerProfitAndLose(followerPosition.getProfitAndLose());
                update.setFollowerAllProfitAndLose(followerPosition.getAllProfitAndLose());
                update.setCloseTime(followerPosition.getSellOrderTime());
            }
            update.setCloseSource(0);
            update.setRemark("交易员平仓后自动同步");
            update.setUpdateTime(new Date());
            updateCopyTradeOrder(update);
        } else {
            throw new ServiceException("跟单平仓失败");
        }
    }

    /** 查询某个主单下当前持仓中的跟单映射。 */
    @Override
    public List<CopyTradeOrder> selectActiveOrdersByLeaderPositionId(Integer productType, Long leaderPositionId) {
        return copyTradeOrderMapper.selectActiveOrdersByLeaderPositionId(productType, leaderPositionId);
    }

    /** 查询某条关系对某个主单是否已有跟单映射。 */
    @Override
    public CopyTradeOrder selectOrderByRelationAndLeaderPosition(Integer productType, Long relationId, Long leaderPositionId) {
        return copyTradeOrderMapper.selectOrderByRelationAndLeaderPosition(productType, relationId, leaderPositionId);
    }

    /** 根据产品类型和持仓ID查询持仓快照。 */
    @Override
    public CopyTradePositionSnapshot selectCopyTradePositionSnapshot(Integer productType, Long positionId) {
        if (productType == null || positionId == null) {
            return null;
        }
        // 各产品持仓表不同，但跟单任务只需要一份统一快照。
        if (productType.equals(CopyTradePositionSnapshot.PRODUCT_TYPE_STOCK)) {
            return toCopyTradePositionSnapshot(productType, userStockPositionService.selectUserStockPositionById(positionId));
        } else if (productType.equals(CopyTradePositionSnapshot.PRODUCT_TYPE_CRYPTOCURRENCY)) {
            return toCopyTradePositionSnapshot(productType, userCryptocurrencyPositionService.selectUserCryptocurrencyPositionById(positionId));
        } else if (productType.equals(CopyTradePositionSnapshot.PRODUCT_TYPE_FUTURES)) {
            return toCopyTradePositionSnapshot(productType, userFuturesPositionService.selectUserFuturesPositionById(positionId));
        } else if (productType.equals(CopyTradePositionSnapshot.PRODUCT_TYPE_FOREX)) {
            return toCopyTradePositionSnapshot(productType, userForexPositionService.selectUserForexPositionById(positionId));
        }
        return null;
    }

    /** 跟随者自行平仓后关闭对应跟单映射。 */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void closeFollowerOrderByFollowerPosition(Integer productType, Long followerPositionId, Integer closeSource) {
        CopyTradeOrder order = copyTradeOrderMapper.selectActiveOrderByFollowerPosition(productType, followerPositionId);
        if (order == null) {
            return;
        }
        CopyTradePositionSnapshot followerPosition = selectCopyTradePositionSnapshot(productType, followerPositionId);
        if (followerPosition == null || followerPosition.getOrderStatus() == null || !followerPosition.getOrderStatus().equals(1)) {
            return;
        }
        CopyTradeOrder update = new CopyTradeOrder();
        update.setId(order.getId());
        update.setStatus(1);
        update.setCloseSource(closeSource);
        update.setCloseTime(followerPosition.getSellOrderTime());
        update.setFollowerSellOrderPrice(followerPosition.getSellOrderPrice());
        update.setFollowerProfitAndLose(followerPosition.getProfitAndLose());
        update.setFollowerAllProfitAndLose(followerPosition.getAllProfitAndLose());
        update.setRemark(closeSource != null && closeSource.equals(1) ? "跟随者手动平仓" : "系统强平跟单单");
        update.setUpdateTime(new Date());
        updateCopyTradeOrder(update);
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

    /** 计算跟单子仓位的保证金快照。 */
    private BigDecimal calculateMarginAmount(CopyTradePositionSnapshot followerPosition) {
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

    /** 按产品类型创建跟单子仓位。 */
    private CopyTradePositionSnapshot openFollowerPosition(CopyTradeRelation relation, CopyTradePositionSnapshot leaderPosition) {
        Integer productType = leaderPosition.getProductType();
        // 跟单子单开仓必须复用对应产品原交易服务，保证钱包、手续费、交易时间、产品限制等规则一致。
        if (productType.equals(CopyTradePositionSnapshot.PRODUCT_TYPE_STOCK)) {
            return toCopyTradePositionSnapshot(productType, userStockPositionService.openCopyTradePosition(relation.getFollowerUserId(), leaderPosition, relation));
        } else if (productType.equals(CopyTradePositionSnapshot.PRODUCT_TYPE_CRYPTOCURRENCY)) {
            return toCopyTradePositionSnapshot(productType, userCryptocurrencyPositionService.openCopyTradePosition(relation.getFollowerUserId(), leaderPosition, relation));
        } else if (productType.equals(CopyTradePositionSnapshot.PRODUCT_TYPE_FUTURES)) {
            return toCopyTradePositionSnapshot(productType, userFuturesPositionService.openCopyTradePosition(relation.getFollowerUserId(), leaderPosition, relation));
        } else if (productType.equals(CopyTradePositionSnapshot.PRODUCT_TYPE_FOREX)) {
            return toCopyTradePositionSnapshot(productType, userForexPositionService.openCopyTradePosition(relation.getFollowerUserId(), leaderPosition, relation));
        }
        throw new ServiceException("暂不支持该产品类型跟单");
    }

    /** 按产品类型平掉跟单子仓位。 */
    private AjaxResult closeFollowerPosition(CopyTradeOrder order, CopyTradePositionSnapshot leaderPosition) {
        Integer productType = order.getProductType();
        // 平仓也复用对应产品原强平入口，并沿用主单平仓价。
        if (productType.equals(CopyTradePositionSnapshot.PRODUCT_TYPE_STOCK)) {
            return userStockPositionService.sell(order.getFollowerPositionId(), 0, leaderPosition.getSellOrderPrice());
        } else if (productType.equals(CopyTradePositionSnapshot.PRODUCT_TYPE_CRYPTOCURRENCY)) {
            return userCryptocurrencyPositionService.sell(order.getFollowerPositionId(), 0, leaderPosition.getSellOrderPrice());
        } else if (productType.equals(CopyTradePositionSnapshot.PRODUCT_TYPE_FUTURES)) {
            return userFuturesPositionService.sell(order.getFollowerPositionId(), 0, leaderPosition.getSellOrderPrice());
        } else if (productType.equals(CopyTradePositionSnapshot.PRODUCT_TYPE_FOREX)) {
            return userForexPositionService.sell(order.getFollowerPositionId(), 0, leaderPosition.getSellOrderPrice());
        }
        throw new ServiceException("暂不支持该产品类型跟单平仓");
    }

    private CopyTradePositionSnapshot toCopyTradePositionSnapshot(Integer productType, UserStockPosition position) {
        if (position == null) {
            return null;
        }
        CopyTradePositionSnapshot copyTradePosition = new CopyTradePositionSnapshot();
        copyTradePosition.setProductType(productType);
        copyTradePosition.setId(position.getId());
        copyTradePosition.setUserId(position.getUserId());
        copyTradePosition.setProductCode(position.getProductCode());
        copyTradePosition.setOrderDirection(position.getOrderDirection());
        copyTradePosition.setBuyOrderPrice(position.getBuyOrderPrice());
        copyTradePosition.setSellOrderPrice(position.getSellOrderPrice());
        copyTradePosition.setSellOrderTime(position.getSellOrderTime());
        copyTradePosition.setProfitAndLose(position.getProfitAndLose());
        copyTradePosition.setAllProfitAndLose(position.getAllProfitAndLose());
        copyTradePosition.setOrderTotalPrice(position.getOrderTotalPrice());
        copyTradePosition.setOrderLever(position.getOrderLever());
        copyTradePosition.setOrderCode(position.getOrderCode());
        copyTradePosition.setOrderStatus(position.getOrderStatus());
        copyTradePosition.setStopProfitPrice(position.getStopProfitPrice());
        copyTradePosition.setStopLossPrice(position.getStopLossPrice());
        return copyTradePosition;
    }

    private CopyTradePositionSnapshot toCopyTradePositionSnapshot(Integer productType, UserCryptocurrencyPosition position) {
        if (position == null) {
            return null;
        }
        CopyTradePositionSnapshot copyTradePosition = new CopyTradePositionSnapshot();
        copyTradePosition.setProductType(productType);
        copyTradePosition.setId(position.getId());
        copyTradePosition.setUserId(position.getUserId());
        copyTradePosition.setProductCode(position.getProductCode());
        copyTradePosition.setOrderDirection(position.getOrderDirection());
        copyTradePosition.setBuyOrderPrice(position.getBuyOrderPrice());
        copyTradePosition.setSellOrderPrice(position.getSellOrderPrice());
        copyTradePosition.setSellOrderTime(position.getSellOrderTime());
        copyTradePosition.setProfitAndLose(position.getProfitAndLose());
        copyTradePosition.setAllProfitAndLose(position.getAllProfitAndLose());
        copyTradePosition.setOrderTotalPrice(position.getOrderTotalPrice());
        copyTradePosition.setOrderLever(position.getOrderLever());
        copyTradePosition.setOrderCode(position.getOrderCode());
        copyTradePosition.setOrderStatus(position.getOrderStatus());
        copyTradePosition.setStopProfitPrice(position.getStopProfitPrice());
        copyTradePosition.setStopLossPrice(position.getStopLossPrice());
        return copyTradePosition;
    }

    private CopyTradePositionSnapshot toCopyTradePositionSnapshot(Integer productType, UserFuturesPosition position) {
        if (position == null) {
            return null;
        }
        CopyTradePositionSnapshot copyTradePosition = new CopyTradePositionSnapshot();
        copyTradePosition.setProductType(productType);
        copyTradePosition.setId(position.getId());
        copyTradePosition.setUserId(position.getUserId());
        copyTradePosition.setProductCode(position.getProductCode());
        copyTradePosition.setOrderDirection(position.getOrderDirection());
        copyTradePosition.setBuyOrderPrice(position.getBuyOrderPrice());
        copyTradePosition.setSellOrderPrice(position.getSellOrderPrice());
        copyTradePosition.setSellOrderTime(position.getSellOrderTime());
        copyTradePosition.setProfitAndLose(position.getProfitAndLose());
        copyTradePosition.setAllProfitAndLose(position.getAllProfitAndLose());
        copyTradePosition.setOrderTotalPrice(position.getOrderTotalPrice());
        copyTradePosition.setOrderLever(position.getOrderLever());
        copyTradePosition.setOrderCode(position.getOrderCode());
        copyTradePosition.setOrderStatus(position.getOrderStatus());
        copyTradePosition.setStopProfitPrice(position.getStopProfitPrice());
        copyTradePosition.setStopLossPrice(position.getStopLossPrice());
        return copyTradePosition;
    }

    private CopyTradePositionSnapshot toCopyTradePositionSnapshot(Integer productType, UserForexPosition position) {
        if (position == null) {
            return null;
        }
        CopyTradePositionSnapshot copyTradePosition = new CopyTradePositionSnapshot();
        copyTradePosition.setProductType(productType);
        copyTradePosition.setId(position.getId());
        copyTradePosition.setUserId(position.getUserId());
        copyTradePosition.setProductCode(position.getProductCode());
        copyTradePosition.setOrderDirection(position.getOrderDirection());
        copyTradePosition.setBuyOrderPrice(position.getBuyOrderPrice());
        copyTradePosition.setSellOrderPrice(position.getSellOrderPrice());
        copyTradePosition.setSellOrderTime(position.getSellOrderTime());
        copyTradePosition.setProfitAndLose(position.getProfitAndLose());
        copyTradePosition.setAllProfitAndLose(position.getAllProfitAndLose());
        copyTradePosition.setOrderTotalPrice(position.getOrderTotalPrice());
        copyTradePosition.setOrderLever(position.getOrderLever());
        copyTradePosition.setOrderCode(position.getOrderCode());
        copyTradePosition.setOrderStatus(position.getOrderStatus());
        copyTradePosition.setStopProfitPrice(position.getStopProfitPrice());
        copyTradePosition.setStopLossPrice(position.getStopLossPrice());
        return copyTradePosition;
    }
}
