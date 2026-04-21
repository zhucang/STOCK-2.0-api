package com.ruoyi.common.logDict;

import com.ruoyi.common.logDict.abstractDictMap.AbstractLogDictMap;

/**
 * 产品交易设置（产品风控）对象 product_setting
 * 
 * @author ruoyi
 * @date 2023-12-01
 */
public class ProductSettingLogDict extends AbstractLogDictMap
{

    @Override
    public void init() {
        this.put("id", "产品交易配置ID");
        this.put("dutyFee", "印花税率");
        this.put("stayFee", "留仓费率");
        this.put("stayMaxDays", "最大留仓天数");
        this.put("buyMinAmt", "最小购买金额");
        this.put("buyMinNum", "最小购买数量");
        this.put("buyMaxNum","最大购买数量");
        this.put("buyMaxAmtPercent","最大买入比例");
        this.put("forceStopPercent","强制平仓比例");
        this.put("siteLever","快捷交易杠杆倍数");
        this.put("handNum","快捷交易手数");
        this.put("buySameTimes","N分钟内同一只股票不得下单M次（N）");
        this.put("buySameNums","N分钟内同一只股票不得下单M次（M）");
        this.put("buyNumTimes","N分钟内交易手数不能超过M次（N）");
        this.put("buyNumLots","N分钟内交易手数不能超过M次（M）");
        this.put("cantSellTimes","至少持有时间（分钟）");
        this.put("handCorrespondNum","一手对应的数量");
        this.put("liquidationMethod","爆仓模式");
        this.put("liquidationMethod","1","保证金+账户余额亏完");
        this.put("liquidationMethod","2","保证金亏完");
        this.put("liquidationMethod","3","全亏损");
        this.put("productType","产品类型");
        this.put("productType","1","股票");
        this.put("productType","2","加密货币");
        this.put("productType","3","期货");
        this.put("productType","4","外汇");
    }
}
