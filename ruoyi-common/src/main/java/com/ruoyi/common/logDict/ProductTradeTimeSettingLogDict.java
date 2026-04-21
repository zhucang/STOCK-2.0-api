package com.ruoyi.common.logDict;

import com.ruoyi.common.logDict.abstractDictMap.AbstractLogDictMap;

/**
 * 系统产品交易时间配置对象 product_trade_time_setting
 * 
 * @author ruoyi
 * @date 2023-11-27
 */
public class ProductTradeTimeSettingLogDict extends AbstractLogDictMap
{

    @Override
    public void init() {
//        this.put("id", "系统产品交易时间配置ID");
        this.put("day", "天(周一到周天，1~7)");
        this.put("transAmBegin", "早上交易开始时间");
        this.put("transAmEnd", "早上交易结束时间");
        this.put("transPmBegin", "下午交易开始时间");
        this.put("transPmEnd", "下午交易结束时间");
        this.put("id","产品类型");
        this.put("id","1","股票");
        this.put("id","2","加密货币");
        this.put("id","3","期货");
        this.put("id","4","外汇");
    }
}
