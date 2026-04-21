package com.ruoyi.common.logDict;

import com.ruoyi.common.logDict.abstractDictMap.AbstractLogDictMap;

/**
 * 极速交易下单选项对象 fast_trade_order_options
 * 
 * @author ruoyi
 * @date 2023-11-02
 */
public class FastTradeOrderOptionsLogDict extends AbstractLogDictMap
{

    @Override
    public void init() {
        this.put("id","极速交易下单选项ID");
        this.put("ids","极速交易下单选项ID列表");
        this.put("productCode","产品代码");
        this.put("productType","产品类型");
        this.put("productType","1","股票");
        this.put("productType","2","加密货币");
        this.put("productType","3","期货");
        this.put("productType","4","外汇");
        this.put("durationValue","时长");
        this.put("durationLabel","时长标签");
        this.put("durationLabel","0","秒");
        this.put("durationLabel","1","分钟");
        this.put("durationLabel","2","小时");
        this.put("status","状态");
        this.put("status","0","启用");
        this.put("status","1","停用");
        this.put("upWinProfitRatio","涨赢收益率");
        this.put("upLoseProfitRatio","涨输扣除率");
        this.put("upFluctuationRatio","涨波动率");
        this.put("downWinProfitRatio","跌赢收益率");
        this.put("downLoseProfitRatio","跌输扣除率");
        this.put("downFluctuationRatio","跌波动率");
        this.put("minBuyAmount","最小买入金额");
        this.put("maxBuyAmount","最大买入金额");
        this.put("minUserAmount","用户余额最小限制");
        this.put("profitRatioMethod","收益率方式");
        this.put("profitRatioMethod","0","固定");
        this.put("profitRatioMethod","1","波动");
        this.put("loseMoneyMethod","输钱方式");
        this.put("loseMoneyMethod","0","全部扣除");
        this.put("loseMoneyMethod","1","按收益率扣除");
        this.put("vipLevelLimit","vip等级限制");
        this.put("sort","排序");
    }
}
