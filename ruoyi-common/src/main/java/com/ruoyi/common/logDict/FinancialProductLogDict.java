package com.ruoyi.common.logDict;

import com.ruoyi.common.logDict.abstractDictMap.AbstractLogDictMap;

/**
 * 理财产品配置对象 financial_product
 * 
 * @author ruoyi
 * @date 2023-11-26
 */
public class FinancialProductLogDict extends AbstractLogDictMap
{

    @Override
    public void init() {
        this.put("id","理财产品ID");
        this.put("financialName","理财产品名称");
        this.put("financialImg","理财图标");
        this.put("minPrice","最低购买");
        this.put("maxPrice","最高购买");
        this.put("financialTime","理财天数");
        this.put("floatDailyIncomeMinRate","浮动日收益最低");
        this.put("floatDailyIncomeMaxRate","浮动日收益最高");
        this.put("fixedIncomeRate","固定收益率");
        this.put("status","状态");
        this.put("status","0","启用");
        this.put("status","1","禁用");
        this.put("sort","排序");
        this.put("breakContractRate","违约金比率");
        this.put("userAmountLimit","用户余额限制");
        this.put("autoApprove","是否自动审核");
        this.put("autoApprove","0","是");
        this.put("autoApprove","1","否");
        this.put("currencyId","币种ID");
        this.put("currencyName","币种名称");
        this.put("vipLevelLimit","vip等级最小限制");
        this.put("settleMethod","结算方式");
        this.put("settleMethod","0","日结");
        this.put("settleMethod","1","到期结");
    }
}
