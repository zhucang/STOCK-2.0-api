package com.ruoyi.common.logDict;

import com.ruoyi.common.logDict.abstractDictMap.AbstractLogDictMap;

/**
 * 贷款订单对象 loan_order
 * 
 * @author ruoyi
 * @date 2024-05-22
 */
public class LoanOrderLogDict extends AbstractLogDictMap
{

    @Override
    public void init() {
        this.put("id","贷款订单ID");
        this.put("orderCode","订单号");
        this.put("loanProductId","贷款产品ID");
        this.put("productName","产品名称");
        this.put("orderPrice","订单金额");
        this.put("loanDays","借贷天数");
        this.put("interestFreeDays","免息天数");
        this.put("loanDailyRate","贷款每日利息率");
        this.put("breakContractDailyRate","违约每日利息率");
        this.put("loanStartTime","贷款开始时间");
        this.put("loanEndTime","贷款结束时间");
        this.put("alreadyLoanDays","已贷款天数");
        this.put("needPayInterest","需还利息");
        this.put("interestSettlementMethod","利息结算方式");
        this.put("interestSettlementMethod","0","每日余额扣除利息");
        this.put("interestSettlementMethod","1","到期还款结清总利息");
        this.put("orderStatus","订单状态");
        this.put("orderStatus","0","待审核");
        this.put("orderStatus","1","进行中");
        this.put("orderStatus","2","已完成");
        this.put("orderStatus","3","已驳回");
        this.put("currencyId","币种ID");
        this.put("currencyName","币种名称");
        this.put("userInfoCountry","国家");
        this.put("immediateFamilyMemberName","直系亲属姓名");
        this.put("immediateFamilyMemberPhoneNumber","直系亲属电话号码");
        this.put("residentialAddress","居住地址");
        this.put("idType","证件类型");
        this.put("idType","0","身份证");
        this.put("idType","1","驾驶证");
        this.put("idType","2","护照");
        this.put("idNumber","证件号码");
        this.put("img1Key","证件图片（正面）");
        this.put("img2Key","证件图片（背面）");
        this.put("img3Key","证件图片（手持）");
        this.put("img4Key","银行流水清单");
    }
}
