package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.BibiTradeOrder;

import java.util.List;

/**
 * 币币交易订单Mapper接口
 * 
 * @author ruoyi
 * @date 2024-06-24
 */
public interface BibiTradeOrderMapper 
{
    /**
     * 查询币币交易订单
     * 
     * @param id 币币交易订单主键
     * @return 币币交易订单
     */
    public BibiTradeOrder selectBibiTradeOrderById(Long id);

    /**
     * 查询币币交易订单列表
     * 
     * @param bibiTradeOrder 币币交易订单
     * @return 币币交易订单集合
     */
    public List<BibiTradeOrder> selectBibiTradeOrderList(BibiTradeOrder bibiTradeOrder);

    /**
     * 新增币币交易订单
     * 
     * @param bibiTradeOrder 币币交易订单
     * @return 结果
     */
    public int insertBibiTradeOrder(BibiTradeOrder bibiTradeOrder);

    /**
     * 批量新增币币交易订单
     *
     * @param bibiTradeOrders 币币交易订单列表
     * @return 结果
     */
    public int insertBibiTradeOrders(List<BibiTradeOrder> bibiTradeOrders);

    /**
     * 修改币币交易订单
     * 
     * @param bibiTradeOrder 币币交易订单
     * @return 结果
     */
    public int updateBibiTradeOrder(BibiTradeOrder bibiTradeOrder);

    /**
     * 删除币币交易订单
     * 
     * @param id 币币交易订单主键
     * @return 结果
     */
    public int deleteBibiTradeOrderById(Long id);

    /**
     * 批量删除币币交易订单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBibiTradeOrderByIds(Long[] ids);
}
