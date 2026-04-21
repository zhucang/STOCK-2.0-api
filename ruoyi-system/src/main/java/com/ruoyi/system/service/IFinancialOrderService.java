package com.ruoyi.system.service;

import com.ruoyi.system.domain.FinancialOrder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 理财订单Service接口
 * 
 * @author ruoyi
 * @date 2023-11-26
 */
public interface IFinancialOrderService 
{
    /**
     * 查询理财订单
     * 
     * @param id 理财订单主键
     * @return 理财订单
     */
    public FinancialOrder selectFinancialOrderById(Long id);

    /**
     * 查询理财订单列表
     * 
     * @param financialOrder 理财订单
     * @return 理财订单集合
     */
    public List<FinancialOrder> selectFinancialOrderList(FinancialOrder financialOrder);

    /**
     * 填充其他信息
     */
    public void fillOtherInfo(List<FinancialOrder> financialOrders);

    /**
     * 新增理财订单
     * 
     * @param financialOrder 理财订单
     * @return 结果
     */
    public int insertFinancialOrder(FinancialOrder financialOrder);

    /**
     * 修改理财订单
     * 
     * @param financialOrder 理财订单
     * @return 结果
     */
    public int updateFinancialOrder(FinancialOrder financialOrder);

    /**
     * 批量删除理财订单
     * 
     * @param ids 需要删除的理财订单主键集合
     * @return 结果
     */
    public int deleteFinancialOrderByIds(Long[] ids);

    /**
     * 删除理财订单信息
     * 
     * @param id 理财订单主键
     * @return 结果
     */
    public int deleteFinancialOrderById(Long id);

    /**
     * 理财订单审核
     * @param financialOrderId 理财订单id
     * @param orderStatus 订单状态 1：通过 3：驳回
     * @return
     */
    public int updateFinancialOrderStatus(Long financialOrderId, Integer orderStatus);

    /**
     * 理财产品人工赎回
     * @param id 理财订单id
     * @return
     */
    public int manualRedemption(Long id);

    /**
     * 理财产品人工结算
     * @param id 理财订单id
     * @return
     */
    public int manualSettlement(Long id);

//        ------------------------------

    /**
     * 购买理财订单
     * @param financialProductId 理财产品id
     * @param buyPrice 购买金额
     * @return
     */
    public int addFinancialOrder(Long financialProductId, BigDecimal buyPrice);

    /**
     * 理财数据总统计
     */
    public Map<String, Object> financialOrderAnalysis(Long userId);

    /**
     * 用户赎回
     * @param financialOrderId 理财订单id
     * @return
     */
    public int redemption(Long financialOrderId);

    /**
     * 理财派息定时任务
     */
    void payInterestTask();

    /**
     * 理财派息定时任务
     */
    void doPayInterestTask(FinancialOrder financialOrder);

    /**
     * 理财派息定时任务异常修复
     */
    void exceptionPayInterestTask();
}
