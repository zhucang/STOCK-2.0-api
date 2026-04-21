package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.system.domain.FastTradeOrder;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 极速交易订单Mapper接口
 * 
 * @author ruoyi
 * @date 2023-11-02
 */
public interface FastTradeOrderMapper 
{
    /**
     * 查询极速交易订单
     * 
     * @param id 极速交易订单主键
     * @return 极速交易订单
     */
    public FastTradeOrder selectFastTradeOrderById(Long id);

    /**
     * 查询极速交易订单列表
     * 
     * @param fastTradeOrder 极速交易订单
     * @return 极速交易订单集合
     */
    public List<FastTradeOrder> selectFastTradeOrderList(FastTradeOrder fastTradeOrder);

    /**
     * 新增极速交易订单
     * 
     * @param fastTradeOrder 极速交易订单
     * @return 结果
     */
    public int insertFastTradeOrder(FastTradeOrder fastTradeOrder);

    /**
     * 修改极速交易订单
     * 
     * @param fastTradeOrder 极速交易订单
     * @return 结果
     */
    public int updateFastTradeOrder(FastTradeOrder fastTradeOrder);

    /**
     * 删除极速交易订单
     * 
     * @param id 极速交易订单主键
     * @return 结果
     */
    public int deleteFastTradeOrderById(Long id);

    /**
     * 批量删除极速交易订单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFastTradeOrderByIds(Long[] ids);

    /**
     * 获取即将结算的订单
     */
    public List<FastTradeOrder> getSettlementComingSoonOrder(@Param("productType") Integer productType,@Param("deliverTime")Date deliverTime);

    /**
     * 获取某时间至当前时间的订单数量
     * @param userId 用户id
     * @param time 开始时间
     * @return
     */
    Integer getOrderNumForPeriod(@Param("userId") Long userId, @Param("time") Date time);

    /**
     * 获取未结算的极速订单数量
     * @param baseEntity
     * @return
     */
    List<Long> getUserFastTradePendingSettleNum(BaseEntity baseEntity);
}
