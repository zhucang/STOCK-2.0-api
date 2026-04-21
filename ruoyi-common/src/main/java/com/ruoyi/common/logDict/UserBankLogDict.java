package com.ruoyi.common.logDict;

import com.ruoyi.common.logDict.abstractDictMap.AbstractLogDictMap;

/**
 * 用户银行卡对象 user_bank
 * 
 * @author ruoyi
 * @date 2023-11-23
 */
public class UserBankLogDict extends AbstractLogDictMap
{

    @Override
    public void init() {
        this.put("id","银行卡信息ID");
        this.put("userNo","用户编号");
        this.put("bankName","银行名称");
        this.put("bankNo","银行卡号");
        this.put("bankOpenAddress","开户行");
        this.put("holder","开户人姓名");
        this.put("bankPhone","绑定手机号");
        this.put("bankCountry","银行国家");
        this.put("abaCode","ABA代码");
        this.put("swift","SWIFT");
        this.put("postCode","邮编号码");
        this.put("userRealAddress","用户地址");
        this.put("accountType","账户类型");
    }
}
