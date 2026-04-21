package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.ticker.TickerInfo;
import com.ruoyi.system.domain.FastTradeOrder;

import java.util.List;
import java.util.Map;

/**
 * 极速交易订单Service接口
 * 
 * @author ruoyi
 * @date 2023-11-02
 */
public interface IFastTradeOrderService 
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
     * 填充其他信息
     * @param fastTradeOrders 极速交易订单列表
     */
    public void fillOtherInfo(List<FastTradeOrder> fastTradeOrders);

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
     * 批量删除极速交易订单
     * 
     * @param ids 需要删除的极速交易订单主键集合
     * @return 结果
     */
    public int deleteFastTradeOrderByIds(Long[] ids);

    /**
     * 删除极速交易订单信息
     * 
     * @param id 极速交易订单主键
     * @return 结果
     */
    public int deleteFastTradeOrderById(Long id);

    /**
     * 极速交易订单单控
     * @param fastTradeOrderId 极速交易订单id
     * @param orderControlFlag 订单单控状态：0：未控  1：赢  2：输 3：平
     * @return
     */
    public int fastOrderControl(Long fastTradeOrderId,Integer orderControlFlag);

    /**
     * 极速交易订单单控
     * @param fastTradeOrderIds 极速交易订单ids
     * @param orderControlFlag 订单单控状态：0：未控  1：赢  2：输 3：平
     * @return
     */
    public int batchFastOrderControl(List<Long> fastTradeOrderIds, Integer orderControlFlag);

    /**
     * 用户极速交易下单
     * @param fastTradeOrder
     * @return
     */
    public FastTradeOrder addFastTradeOrder(FastTradeOrder fastTradeOrder);

    /**
     * 股票极速交易订单结算任务
     */
    void stockFastTradeOrderSettleTask();

    /**
     * 加密货币极速交易订单结算任务
     */
    void cryptocurrencyFastTradeOrderSettleTask();

    /**
     * 期货极速交易订单结算任务
     */
    void futuresFastTradeOrderSettleTask();

    /**
     * 外汇极速交易订单结算任务
     */
    void forexFastTradeOrderSettleTask();

    /**
     * 极速交易订单结算方法
     * @param fastTradeOrder 订单信息
     * @param tickerInfoMap 行情map
     */
    void fastTradeOrderSettle(FastTradeOrder fastTradeOrder, Map<String, TickerInfo> tickerInfoMap);

    /**
     * 股票极速交易控制定时器
     */
    void stockFastTradeOrderControlTask();

    /**
     * 加密货币极速交易控制定时器
     */
    void cryptocurrencyFastTradeOrderControlTask();

    /**
     * 期货极速交易控制定时器
     */
    void futuresFastTradeOrderControlTask();

    /**
     * 外汇极速交易控制定时器
     */
    void forexFastTradeOrderControlTask();
}
