package com.ruoyi.common.logDict;

import com.ruoyi.common.logDict.abstractDictMap.AbstractLogDictMap;

/**
 * 用户虚拟货币钱包地址对象 user_wallet_address
 * 
 * @author ruoyi
 * @date 2023-10-29
 */
public class UserUdunWalletAddressLogDict extends AbstractLogDictMap
{

    @Override
    public void init() {
        this.put("id","钱包信息ID");
        this.put("userNo","用户编号");
        this.put("mainCoinType","主币种编号");
        this.put("coinName","币种名称");
        this.put("walletAddress","钱包地址");
    }
}
