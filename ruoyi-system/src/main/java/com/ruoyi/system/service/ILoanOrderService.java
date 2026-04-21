package com.ruoyi.system.service;

import com.ruoyi.system.domain.LoanOrder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 贷款订单Service接口
 * 
 * @author ruoyi
 * @date 2024-05-22
 */
public interface ILoanOrderService 
{
    /**
     * 查询贷款订单
     * 
     * @param id 贷款订单主键
     * @return 贷款订单
     */
    public LoanOrder selectLoanOrderById(Long id);

    /**
     * 查询贷款订单列表
     * 
     * @param loanOrder 贷款订单
     * @return 贷款订单集合
     */
    public List<LoanOrder> selectLoanOrderList(LoanOrder loanOrder);

    /**
     * 获取统计数据
     * @param loanOrder
     * @return
     */
    public List<LoanOrder> getStatisticalData(LoanOrder loanOrder);

    /**
     * 填充其他信息
     * @param loanOrders 贷款订单
     */
    public void fillOtherInfo(List<LoanOrder> loanOrders);

    /**
     * 新增贷款订单
     * 
     * @param loanOrder 贷款订单
     * @return 结果
     */
    public int insertLoanOrder(LoanOrder loanOrder);

    /**
     * 修改贷款订单
     * 
     * @param loanOrder 贷款订单
     * @return 结果
     */
    public int updateLoanOrder(LoanOrder loanOrder);

    /**
     * 批量删除贷款订单
     * 
     * @param ids 需要删除的贷款订单主键集合
     * @return 结果
     */
    public int deleteLoanOrderByIds(Long[] ids);

    /**
     * 删除贷款订单信息
     * 
     * @param id 贷款订单主键
     * @return 结果
     */
    public int deleteLoanOrderById(Long id);

    /**
     * 贷款订单审核
     * @param loanOrderId 贷款订单id
     * @param orderStatus 状态：1:通过 3：驳回
     * @param loanMsg 驳回信息
     * @param remark 备注
     * @return
     */
    public int updateLoanOrderStatus(Long loanOrderId, Integer orderStatus, BigDecimal realLoanAmount, BigDecimal loanDailyRate, String loanMsg, String remark);

    /**
     * 修改贷款订单是否免客损状态
     * @param loanOrderId 贷款订单id
     * @param statisticalReport 是否统计报表 0：是 1：否
     * @return
     */
    public int updateStatisticalReport(Long loanOrderId, Integer statisticalReport);

    /**
     * 获取用户的各币种的贷款金额
     * @param userId 用户id
     * @return
     */
    public List<LoanOrder> selectUserLoanAmountAllCurrencyByUserId(Long userId);




    /**
     * 用户贷款
     * @param loanOrder
     * @return
     */
    public int addLoanOrder(LoanOrder loanOrder);

    /**
     * 用户贷款信息面板
     * @return
     */
    public Map<String, Object> userLoanPanelData();

    /**
     * 贷款订单后台人工结算
     * @param loanOrderId 贷款订单id
     * @param settlementType 结算类型 0：从余额扣除 1：直接结算
     * @return
     */
    public int loanRepayment(Long loanOrderId,Integer settlementType);

    /**
     * 贷款收取利息定时器
     * @return
     */
    void chargeInterestTask();

    /**
     * 贷款收取利息定时器
     * @return
     */
    void doChargeInterestTask(LoanOrder loanOrder);
}
