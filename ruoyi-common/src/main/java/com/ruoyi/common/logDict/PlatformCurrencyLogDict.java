package com.ruoyi.common.logDict;

import com.ruoyi.common.logDict.abstractDictMap.AbstractLogDictMap;

/**
 * 平台交易币种配置信息对象 platform_currency
 * 
 * @author ruoyi
 * @date 2023-10-28
 */
public class PlatformCurrencyLogDict extends AbstractLogDictMap
{

    @Override
    public void init() {
        this.put("id","币种ID");
        this.put("currencyName","币种名称");
        this.put("currencyDesc","币种描述");
        this.put("currencyType","币种类型");
        this.put("currencyType","0","法币");
        this.put("currencyType","1","虚拟币");
        this.put("balanceConvertMinLimit","资金互转金额最小限制");
        this.put("balanceConvertMaxLimit","资金互转金额最大限制");
        this.put("status","状态");
        this.put("status","0","启用");
        this.put("status","1","禁用");
        this.put("sort","排序");
        this.put("quoteCurrency","行情报价币种");
        this.put("isPlatformCurrency","是否两融资产");
        this.put("isPlatformCurrency","0","是");
        this.put("isPlatformCurrency","1","否");
        this.put("useInFastTrade","是否用于极速交易");
        this.put("useInFastTrade","0","是");
        this.put("useInFastTrade","1","否");
        this.put("walletAddressType","钱包地址类型");
    }
}
