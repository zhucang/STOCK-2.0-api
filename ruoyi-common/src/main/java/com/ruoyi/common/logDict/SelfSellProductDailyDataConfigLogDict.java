package com.ruoyi.common.logDict;

import com.ruoyi.common.logDict.abstractDictMap.AbstractLogDictMap;

/**
 * 自营产品每日行情数据配置对象 self_sell_product_daily_data_config
 * 
 * @author ruoyi
 * @date 2023-11-27
 */
public class SelfSellProductDailyDataConfigLogDict extends AbstractLogDictMap
{

    @Override
    public void init() {
        this.put("id","自营产品每日行情数据配置ID");
        this.put("selfSellProductId","自营产品ID");
        this.put("productCode","产品代码");
        this.put("isDefault","是否默认");
        this.put("isDefault","0","是");
        this.put("isDefault","1","否");
        this.put("finallyChangeRate","设置的最终涨跌幅");
        this.put("finallyPrice","设置的最终价格");
        this.put("isTemp","是否根据其他支的产品模板复刻数据");
        this.put("isTemp","0","是");
        this.put("isTemp","1","否");
        this.put("tempProductCode","模板产品代码");
        this.put("productType","1","股票");
        this.put("productType","2","加密货币");
        this.put("productType","3","期货");
        this.put("productType","4","外汇");
    }
}
