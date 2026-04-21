package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SpotTradeOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 现货交易订单Mapper接口
 * 
 * @author ruoyi
 * @date 2023-11-05
 */
public interface SpotTradeOrderMapper 
{
    /**
     * 查询现货交易订单
     * 
     * @param id 现货交易订单主键
     * @return 现货交易订单
     */
    public SpotTradeOrder selectSpotTradeOrderById(Long id);

    /**
     * 查询现货交易订单列表
     * 
     * @param spotTradeOrder 现货交易订单
     * @return 现货交易订单集合
     */
    public List<SpotTradeOrder> selectSpotTradeOrderList(SpotTradeOrder spotTradeOrder);

    /**
     * 新增现货交易订单
     * 
     * @param spotTradeOrder 现货交易订单
     * @return 结果
     */
    public int insertSpotTradeOrder(SpotTradeOrder spotTradeOrder);

    /**
     * 批量新增现货交易
     *
     * @param spotTradeOrder 现货交易
     * @return 结果
     */
    public int insertSpotTradeOrders(@Param("list") List<SpotTradeOrder> spotTradeOrder);

    /**
     * 修改现货交易订单
     * 
     * @param spotTradeOrder 现货交易订单
     * @return 结果
     */
    public int updateSpotTradeOrder(SpotTradeOrder spotTradeOrder);

    /**
     * 删除现货交易订单
     * 
     * @param id 现货交易订单主键
     * @return 结果
     */
    public int deleteSpotTradeOrderById(Long id);

    /**
     * 批量删除现货交易订单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSpotTradeOrderByIds(Long[] ids);
}
