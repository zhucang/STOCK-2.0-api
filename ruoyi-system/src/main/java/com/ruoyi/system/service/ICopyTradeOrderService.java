package com.ruoyi.system.service;

import com.ruoyi.system.domain.CopyTradeOrder;
import com.ruoyi.system.domain.CopyTradeRelation;
import com.ruoyi.system.domain.UserCryptocurrencyPosition;

import java.util.List;

/**
 * 跟单订单映射服务接口。
 * 负责主单与跟单单映射表的查询、维护以及同步过程中的状态读取。
 */
public interface ICopyTradeOrderService {
    /**
     * 查询跟单订单映射列表。
     *
     * @param copyTradeOrder 跟单订单映射筛选条件
     * @return 跟单订单映射列表
     */
    List<CopyTradeOrder> selectCopyTradeOrderList(CopyTradeOrder copyTradeOrder);

    /**
     * 根据主键查询跟单订单映射。
     *
     * @param id 跟单订单映射主键
     * @return 跟单订单映射详情
     */
    CopyTradeOrder selectCopyTradeOrderById(Long id);

    /**
     * 交易员开仓后触发批量跟单开仓。
     * 开仓只扫描当前启用中的跟单关系(跟单人员)。
     *
     * @param leaderPosition 交易员主仓位
     */
    void handleLeaderOpenPosition(UserCryptocurrencyPosition leaderPosition);

    /**
     * 交易员平仓后触发批量跟单平仓。
     * 平仓只扫描 copy_trade_order 中仍持仓的历史映射，不依赖跟单关系状态。
     *
     * @param leaderPosition 交易员主仓位
     */
    void handleLeaderClosePosition(UserCryptocurrencyPosition leaderPosition);

    /**
     * 为单个跟单关系(跟单人员)同步开仓。
     *
     * @param relation 跟单关系(跟单人员)
     * @param leaderPosition 交易员主仓位
     */
    void syncFollowerOpenPosition(CopyTradeRelation relation, UserCryptocurrencyPosition leaderPosition);

    /**
     * 为单个跟单订单同步平仓。
     *
     * @param order 跟单订单映射
     * @param leaderPosition 交易员主仓位
     */
    void syncFollowerClosePosition(CopyTradeOrder order, UserCryptocurrencyPosition leaderPosition);

    /**
     * 查询某个主单下所有仍处于持仓中的跟单映射。
     * 用于主单平仓同步，不应关联或过滤 copy_trade_relation 状态。
     *
     * @param productType 产品类型
     * @param leaderPositionId 主单持仓ID
     * @return 跟单映射列表
     */
    List<CopyTradeOrder> selectActiveOrdersByLeaderPositionId(Integer productType, Long leaderPositionId);

    /**
     * 查询某条跟单关系对某个主单是否已经生成过跟单映射。
     *
     * @param productType 产品类型
     * @param relationId 跟单关系(跟单人员)ID
     * @param leaderPositionId 主单持仓ID
     * @return 跟单映射
     */
    CopyTradeOrder selectOrderByRelationAndLeaderPosition(Integer productType, Long relationId, Long leaderPositionId);

    /**
     * 统计某条跟单关系(跟单人员)下当前持仓中的跟单单数量。
     *
     * @param relationId 跟单关系(跟单人员)ID
     * @return 当前持仓中的跟单单数量
     */
    int countActiveOrderByRelationId(Long relationId);

    /**
     * 新增跟单订单映射。
     *
     * @param copyTradeOrder 跟单订单映射信息
     * @return 影响行数
     */
    int insertCopyTradeOrder(CopyTradeOrder copyTradeOrder);

    /**
     * 修改跟单订单映射。
     *
     * @param copyTradeOrder 跟单订单映射信息
     * @return 影响行数
     */
    int updateCopyTradeOrder(CopyTradeOrder copyTradeOrder);

    /**
     * 批量删除跟单订单映射。
     *
     * @param ids 主键数组
     * @return 影响行数
     */
    int deleteCopyTradeOrderByIds(Long[] ids);
}
