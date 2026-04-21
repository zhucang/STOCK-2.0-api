package com.ruoyi.common.logDict;

import com.ruoyi.common.logDict.abstractDictMap.AbstractLogDictMap;

/**
 * 货币兑换汇率对象 currency_exchange_rate
 * 
 * @author ruoyi
 * @date 2023-11-25
 */
public class CurrencyExchangeRateLogDict extends AbstractLogDictMap
{

    @Override
    public void init() {
        this.put("id","货币兑换汇率配置ID");
        this.put("currencyIdFrom","转化者币种ID");
        this.put("currencyNameFrom","转化者币种名称");
        this.put("currencyIdTo","被转化者币种ID");
        this.put("currencyNameTo","被转化者币种名称");
        this.put("exchangeRate","汇率");
        this.put("oppositeExchangeRate","反汇率");
        this.put("feePercent","手续费率");
        this.put("isFixedExchangeRate","是否固定汇率");
        this.put("isFixedExchangeRate","0","固定汇率");
        this.put("isFixedExchangeRate","1","实时汇率");
    }
}
