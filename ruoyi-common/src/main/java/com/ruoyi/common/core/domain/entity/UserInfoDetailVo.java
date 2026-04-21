package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.utils.cache.CacheUtil;

public class UserInfoDetailVo extends BaseEntity {

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    /** 用户编号 */
    @Excel(name = "用户编号")
    private Long userNo;

    /** 代理id */
    @Excel(name = "代理id")
    private Long agentId;

    /** 代理名称 */
    @Excel(name = "代理名称")
    private String agentName;

    /** 账号类型： 0：真实用户 1：游客 */
    @Excel(name = "账号类型： 0：真实用户 1：游客")
    private Integer accountType;

    /** 用户账号 */
    @Excel(name = "用户账号")
    private String userAccount;

    /** 手机号 */
    @Excel(name = "手机号")
    private String phone;

    /** 用户邮箱 */
    @Excel(name = "用户邮箱")
    private String email;

    /** 用户昵称 */
    @Excel(name = "用户昵称")
    private String nickName;

    /** 真实名称 */
    @Excel(name = "真实名称")
    private String realName;

    /** 用户备注 */
    @Excel(name = "用户备注")
    private String userRemark;

    /** 分页页数 */
    private Integer pageNum;

    /** 分页每页数量 */
    private Integer pageSize;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUserRemark() {
        return userRemark;
    }

    public void setUserRemark(String userRemark) {
        this.userRemark = userRemark;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserNo() {
        try{
            return CacheUtil.getOtherValueByKey("appShow_idAddValue", Long.class) + getUserId();
        }catch (Exception e){
            return getUserId();
        }
    }

    public void setUserNo(Long userNo) {
        this.userNo = userNo;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }
}
