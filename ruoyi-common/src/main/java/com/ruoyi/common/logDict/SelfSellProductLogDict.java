package com.ruoyi.common.logDict;

import com.ruoyi.common.logDict.abstractDictMap.AbstractLogDictMap;

/**
 * 自营产品对象 self_sell_product
 * 
 * @author ruoyi
 * @date 2023-11-27
 */
public class SelfSellProductLogDict extends AbstractLogDictMap
{

    @Override
    public void init() {
        this.put("id","客服配置ID");
        this.put("productName","产品名称");
        this.put("productCode","产品代码");
        this.put("initialPrice","初始价格");
        this.put("productType","产品类型");
        this.put("productType","1","股票");
        this.put("productType","2","加密货币");
        this.put("productType","3","期货");
        this.put("productType","4","外汇");
        this.put("status","状态");
        this.put("status","0","启用");
        this.put("status","1","禁用");
        this.put("relateProductId","关联产品ID");
    }
}
