package com.ruoyi.common.logDict;

import com.ruoyi.common.logDict.abstractDictMap.AbstractLogDictMap;

/**
 * 极速交易订单对象 fast_trade_order
 * 
 * @author ruoyi
 * @date 2023-11-02
 */
public class FastTradeOrderLogDict extends AbstractLogDictMap
{

    @Override
    public void init() {
        this.put("id","极速交易订单ID");
        this.put("userNo","用户编号");
        this.put("orderCode","订单编号");
        this.put("productType","产品类型");
        this.put("productType","1","股票");
        this.put("productType","2","加密货币");
        this.put("productType","3","期货");
        this.put("productType","4","外汇");
        this.put("productCode","产品代码");
        this.put("orderDirection","交易方向");
        this.put("orderDirection","0","涨");
        this.put("orderDirection","1","跌");
        this.put("orderPrice","下单金额");
        this.put("feeRate","手续费率");
        this.put("handingFee","手续费");
        this.put("fastTradeOrderOptionsId","下单选项ID");
        this.put("durationValue","订单时长");
        this.put("durationLabel","时长标签");
        this.put("durationLabel","0","秒");
        this.put("durationLabel","1","分钟");
        this.put("durationLabel","2","小时");
        this.put("winProfitRatio","赢收益比率");
        this.put("loseProfitRatio","输扣除比率");
        this.put("buyPrice","买入价格");
        this.put("currencyId","币种ID");
        this.put("currencyName","币种名称");
        this.put("loseMoneyMethod","输钱方式");
        this.put("loseMoneyMethod","0","全部扣除");
        this.put("loseMoneyMethod","1","按收益率扣除");
        this.put("orderControlFlag","订单单控状态");
        this.put("orderControlFlag","0","未控");
        this.put("orderControlFlag","1","控赢");
        this.put("orderControlFlag","2","控输");
        this.put("orderControlFlag","3","控平");
    }
}
