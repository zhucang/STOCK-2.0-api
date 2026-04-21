package com.ruoyi.common.logDict;

import com.ruoyi.common.logDict.abstractDictMap.AbstractLogDictMap;

/**
 * 理财订单对象 financial_order
 * 
 * @author ruoyi
 * @date 2023-11-26
 */
public class FinancialOrderLogDict extends AbstractLogDictMap
{

    @Override
    public void init() {
        this.put("id","理财订单ID");
        this.put("userNo","用户编号");
        this.put("orderCode","理财订单号");
        this.put("financialProductId","理财产品ID");
        this.put("financialName","理财产品名称");
        this.put("financialTime","理财天数");
        this.put("buyPrice","理财购买金额");
        this.put("startDate","理财开始日期");
        this.put("endDate","理财结束日期");
        this.put("breakContractRate","违约金比率");
        this.put("breakContractAmount","提前赎回违约金");
        this.put("dailyIncomeRate","日收益率");
        this.put("floatDailyIncomeMinRate","浮动日收益最低");
        this.put("floatDailyIncomeMaxRate","浮动日收益最高");
        this.put("orderStatus","订单状态");
        this.put("orderStatus","0","待审核");
        this.put("orderStatus","1","进行中");
        this.put("orderStatus","2","已完成");
        this.put("orderStatus","3","已驳回");
        this.put("orderStatus","4","已赎回");
        this.put("currencyId","币种ID");
        this.put("currencyName","币种名称");
        this.put("settleMethod","结算方式");
        this.put("alreadyInterestCount","已经派息次数");
        this.put("settleMethod","0","日结");
        this.put("settleMethod","1","到期结");
    }
}
