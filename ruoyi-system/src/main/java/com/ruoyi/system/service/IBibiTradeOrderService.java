package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.ticker.TickerInfo;
import com.ruoyi.system.domain.BibiTradeOrder;

import java.util.List;
import java.util.Map;

/**
 * 币币交易订单Service接口
 * 
 * @author ruoyi
 * @date 2024-06-24
 */
public interface IBibiTradeOrderService 
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
     * 修改币币交易订单
     * 
     * @param bibiTradeOrder 币币交易订单
     * @return 结果
     */
    public int updateBibiTradeOrder(BibiTradeOrder bibiTradeOrder);

    /**
     * 批量删除币币交易订单
     * 
     * @param ids 需要删除的币币交易订单主键集合
     * @return 结果
     */
    public int deleteBibiTradeOrderByIds(Long[] ids);

    /**
     * 删除币币交易订单信息
     * 
     * @param id 币币交易订单主键
     * @return 结果
     */
    public int deleteBibiTradeOrderById(Long id);

    /**
     * 人工正常平仓
     * @param bibiTradeOrder
     * @return
     */
    public int manualSell(BibiTradeOrder bibiTradeOrder);




    /**
     * 用户币币交易买入
     * @param bibiTradeOrder
     * @return
     */
    public int buy(BibiTradeOrder bibiTradeOrder);

    /**
     * 用户币币交易卖出
     * @param bibiTradeOrder
     * @return
     */
    public int sell(BibiTradeOrder bibiTradeOrder);

    /**
     * 撤销委托
     * @param bibiTradeOrderId
     * @return
     */
    public int cancel(Long bibiTradeOrderId);

    /**
     * 委托订单自动通过定时任务
     */
    public void bibiOrderAutoDealTask();

    /**
     * 委托订单自动通过定时任务
     */
    public void doBibiOrderAutoDealTask(BibiTradeOrder bibiTradeOrder,Map<String,TickerInfo> tickerInfoMap);
}
