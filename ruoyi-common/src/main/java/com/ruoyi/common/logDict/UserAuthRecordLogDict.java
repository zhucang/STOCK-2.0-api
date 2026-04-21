package com.ruoyi.common.logDict;

import com.ruoyi.common.logDict.abstractDictMap.AbstractLogDictMap;

/**
 * 用户实名认证信息对象 user_auth_record
 * 
 * @author ruoyi
 * @date 2024-04-05
 */
public class UserAuthRecordLogDict extends AbstractLogDictMap
{

    @Override
    public void init() {
        this.put("id","实名认证记录ID");
        this.put("userNo","用户编号");
        this.put("idNumber","证件号码");
        this.put("realName","真实姓名");
        this.put("img1Key","证件图片（正面）");
        this.put("img2Key","证件图片（背面）");
        this.put("img3Key","证件图片（手持）");
        this.put("authStatus","实名状态");
        this.put("authStatus","0","审核中");
        this.put("authStatus","1","审核通过");
        this.put("authStatus","2","审核驳回");
        this.put("authMethod","证件类型");
        this.put("authMethod","0","身份证");
        this.put("authMethod","1","驾驶证");
        this.put("authMethod","2","护照");
        this.put("authLevel","实名等级");
        this.put("authLevel","0","初级认证");
        this.put("authLevel","1","高级认证");
    }
}
