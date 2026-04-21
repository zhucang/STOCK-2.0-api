package com.ruoyi.common.logDict;

import com.ruoyi.common.logDict.abstractDictMap.AbstractLogDictMap;

/**
 * 用户信息对象 user_info
 * 
 * @author ruoyi
 * @date 2023-10-28
 */
public class UserInfoLogDict extends AbstractLogDictMap
{

    @Override
    public void init() {
        this.put("id", "ID");
        this.put("userNo", "用户编号");
        this.put("userAccount", "用户账号");
        this.put("phone", "手机号");
        this.put("email", "用户邮箱");
        this.put("nickName", "用户昵称");
        this.put("realName", "真实名称");
        this.put("userPwd", "登陆密码");
        this.put("withPwd", "提现密码");
        this.put("agentId", "代理ID");
        this.put("agentName", "代理名称");
        this.put("accountType", "账号类型");
        this.put("accountType", "0", "真实用户");
        this.put("accountType", "1", "游客");
        this.put("avatar", "用户头像");
        this.put("isLock", "锁定状态");
        this.put("isLock", "0", "正常");
        this.put("isLock", "1", "锁定");
        this.put("regIp", "注册IP");
        this.put("regAddress", "注册地址");
        this.put("lastLoginIp", "最后登录IP");
        this.put("supUserId", "上级用户ID");
        this.put("status", "用户状态");
        this.put("status", "0","正常");
        this.put("status", "1","禁用");
        this.put("needOrderAmount","打码量");
        this.put("vipLevel","vip等级");
        this.put("creditScore","信用分");
        this.put("isCanWithdraw","是否能提现");
        this.put("isCanWithdraw","0","是");
        this.put("isCanWithdraw","1","否");
        this.put("fastTradeWinProfitRatio","极速交易赢收益率");
        this.put("fastTradeLoseProfitRatio","极速交易输收益率");
        this.put("isAgent","是否设置为代理员");
        this.put("isAgent","0","是");
        this.put("isAgent","1","否");
        this.put("regType","注册类型");
        this.put("regType","0","手机注册");
        this.put("regType","1","邮箱注册");
    }
}
