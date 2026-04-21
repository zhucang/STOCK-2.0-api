package com.ruoyi.common.logDict;

import com.ruoyi.common.logDict.abstractDictMap.AbstractLogDictMap;

/**
 * 贷款产品配置对象 loan_product
 * 
 * @author ruoyi
 * @date 2024-05-21
 */
public class LoanProductLogDict extends AbstractLogDictMap
{

    @Override
    public void init() {
        this.put("id","贷款产品ID");
        this.put("productName","产品名称");
        this.put("productImg","产品图标");
        this.put("minPrice","最低金额");
        this.put("maxPrice","最高金额");
        this.put("loanDays","借贷天数");
        this.put("interestFreeDays","免息天数");
        this.put("loanDailyRate","贷款每日利息率");
        this.put("breakContractDailyRate","违约每日利息率");
        this.put("maxLoanCount","最大贷款次数");
        this.put("interestSettlementMethod","利息结算方式");
        this.put("interestSettlementMethod","0","每日余额扣除利息");
        this.put("interestSettlementMethod","1","到期还款结清总利息");
        this.put("status","状态");
        this.put("status","0","启用");
        this.put("status","1","禁用");
        this.put("sort","排序");
        this.put("currencyId","币种ID");
        this.put("currencyName","币种名称");
    }
}
