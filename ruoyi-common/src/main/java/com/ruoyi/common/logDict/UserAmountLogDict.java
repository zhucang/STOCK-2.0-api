package com.ruoyi.common.logDict;

import com.ruoyi.common.logDict.abstractDictMap.AbstractLogDictMap;

/**
 * 用户各币种余额对象 user_amount
 * 
 * @author ruoyi
 * @date 2023-10-28
 */
public class UserAmountLogDict extends AbstractLogDictMap
{

    @Override
    public void init() {
        this.put("id", "用户钱包余额信息ID");
        this.put("amount", "余额");
        this.put("frozenAmount", "冻结金额");
        this.put("currencyId", "币种ID");
        this.put("currencyName", "币种名称");
    }
}
