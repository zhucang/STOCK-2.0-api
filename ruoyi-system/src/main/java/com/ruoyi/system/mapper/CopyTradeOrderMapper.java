package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.CopyTradeOrder;

import java.util.List;

/**
 * 跟单订单映射 Mapper 接口。
 * 负责主单与跟单单之间映射关系的查询和状态维护。
 */
public interface CopyTradeOrderMapper {
    /**
     * 查询跟单订单映射列表。
     *
     * @param copyTradeOrder 查询条件
     * @return 跟单订单映射列表
     */
    List<CopyTradeOrder> selectCopyTradeOrderList(CopyTradeOrder copyTradeOrder);

    /**
     * 查询某个主单下所有仍处于持仓中的跟单映射。
     *
     * @param productType 产品类型
     * @param leaderPositionId 主单持仓ID
     * @return 跟单映射列表
     */
    List<CopyTradeOrder> selectActiveOrdersByLeaderPositionId(Integer productType, Long leaderPositionId);

    /**
     * 统计某条跟单关系下当前仍处于持仓中的跟单单数量。
     *
     * @param relationId 跟单关系ID
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
}
