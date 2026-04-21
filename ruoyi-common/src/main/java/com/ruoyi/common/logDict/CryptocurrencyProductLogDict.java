package com.ruoyi.common.logDict;

import com.ruoyi.common.logDict.abstractDictMap.AbstractLogDictMap;

/**
 * 加密货币产品信息对象 cryptocurrency_product
 * 
 * @author ruoyi
 * @date 2023-11-01
 */
public class CryptocurrencyProductLogDict extends AbstractLogDictMap
{

    @Override
    public void init() {
        this.put("id","产品ID");
        this.put("productName","产品名称");
        this.put("productNameLang","多语言");
        this.put("productCode","产品代码");
        this.put("productType","产品类型");
        this.put("productType","1","股票");
        this.put("productType","2","加密货币");
        this.put("productType","3","期货");
        this.put("productType","4","外汇");
        this.put("productDesc","产品描述");
        this.put("productImg","产品图标");
        this.put("isLock","是否锁定");
        this.put("isLock","0","否");
        this.put("isLock","1","是");
        this.put("isShow","是否显示");
        this.put("isShow","0","是");
        this.put("isShow","1","否");
        this.put("sort","排序");
        this.put("standard","一个标准手对应数量");
    }
}
