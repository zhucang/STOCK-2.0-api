package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.CopyTradeOrder;

import java.util.List;

/**
 * 跟单订单映射 Mapper 接口。
 * 负责主单与跟单单之间映射关系的查询和状态维护。
 */
public interface CopyTradeOrderMapper {
    /**
     * 根据主键查询跟单订单映射。
     *
     * @param id 跟单订单映射主键
     * @return 跟单订单映射
     */
    CopyTradeOrder selectCopyTradeOrderById(Long id);

    /**
     * 查询跟单订单映射列表。
     *
     * @param copyTradeOrder 查询条件
     * @return 跟单订单映射列表
     */
    List<CopyTradeOrder> selectCopyTradeOrderList(CopyTradeOrder copyTradeOrder);

    /**
     * 查询某个主单下所有仍处于持仓中的跟单映射。
     * 平仓同步必须以 copy_trade_order 映射为准，不依赖跟单关系是否仍启用。
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
     * 查询某个跟单子仓位对应的持仓中映射。
     *
     * @param productType 产品类型
     * @param followerPositionId 跟单子仓位ID
     * @return 跟单映射
     */
    CopyTradeOrder selectActiveOrderByFollowerPosition(Integer productType, Long followerPositionId);

    /**
     * 统计某条跟单关系(跟单人员)下当前仍处于持仓中的跟单单数量。
     *
     * @param relationId 跟单关系(跟单人员)ID
     * @return 当前持仓中的跟单单数量
     */
    int countActiveOrderByRelationId(Long relationId);

    /**
     * 新增主单和跟单单映射。
     *
     * @param copyTradeOrder 跟单订单映射信息
     * @return 影响行数
     */
    int insertCopyTradeOrder(CopyTradeOrder copyTradeOrder);

    /**
     * 修改跟单订单映射状态或备注。
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
