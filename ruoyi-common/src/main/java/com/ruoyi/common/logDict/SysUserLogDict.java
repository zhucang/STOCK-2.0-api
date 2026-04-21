package com.ruoyi.common.logDict;

import com.ruoyi.common.logDict.abstractDictMap.AbstractLogDictMap;

/**
 * 用户对象 sys_user
 * 
 * @author ruoyi
 */
public class SysUserLogDict extends AbstractLogDictMap
{

    @Override
    public void init() {
        this.put("userId","用户ID");
        this.put("deptId","部门编号");
        this.put("userName","用户账号");
        this.put("nickName","用户昵称");
        this.put("email","用户邮箱");
        this.put("phonenumber","手机号码");
        this.put("password","登录密码");
        this.put("status","帐号状态");
        this.put("status","0","正常");
        this.put("status","1","停用");
        this.put("deptName","部门名称");
        this.put("inviteCode","邀请码");
        this.put("supUserId","上级ID");
        this.put("googleAuthSecurityKey","谷歌验证器密钥");
        this.put("supUserId","上级用户ID");
    }
}
