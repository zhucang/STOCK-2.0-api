package com.ruoyi.common.logDict;

import com.ruoyi.common.logDict.abstractDictMap.AbstractLogDictMap;

/**
 * 用户贷款还款订单对象 user_loan_repayment_order
 * 
 * @author ruoyi
 * @date 2024-05-28
 */
public class UserLoanRepaymentOrderLogDict extends AbstractLogDictMap
{

    @Override
    public void init() {
        this.put("id","用户还款订单ID");
        this.put("loanOrderId","用户贷款订单ID");
        this.put("loanOrderCode","用户贷款订单号");
        this.put("loanOrderPrice","贷款本金");
        this.put("needPayInterest","需还利息");
        this.put("userNo","用户编号");
        this.put("orderCode","用户还款订单号");
        this.put("currencyId","币种ID");
        this.put("currencyName","币种名称");
        this.put("rechargeAmount","还款金额");
        this.put("payChannelName","充值通道名称");
        this.put("payChannelId","充值通道ID");
        this.put("rechargeImg","支付凭证");
        this.put("rechargeMethod","充值方式");
        this.put("rechargeMethod","0","线下充值");
        this.put("rechargeMethod","1","在线支付");
        this.put("orderStatus","状态");
        this.put("orderStatus","0","审核中");
        this.put("orderStatus","1","通过");
        this.put("orderStatus","2","驳回");
        this.put("orderStatus","3","待审核");
        this.put("rechargeMsg","返回信息");
        this.put("operatorName","操作人名称");
    }
}
