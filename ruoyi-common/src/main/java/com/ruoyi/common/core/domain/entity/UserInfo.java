package com.ruoyi.common.core.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.utils.cache.CacheUtil;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 用户信息对象 user_info
 * 
 * @author ruoyi
 * @date 2023-10-28
 */
public class UserInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 用户编号*/
    private Long userNo;

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

    /** dappId */
    @Excel(name = "dappId")
    private String dappId;

    /** 登陆密码 */
    @Excel(name = "登陆密码")
    private String userPwd;

    /** 提现密码 */
    @Excel(name = "提现密码")
    private String withPwd;

    /** 代理id */
    @Excel(name = "代理id")
    private Long agentId;

    /** 代理名称 */
    @Excel(name = "代理名称")
    private String agentName;

    /** 代理昵称 */
    @Excel(name = "代理昵称")
    private String agentNickName;

    /** 代理线 */
    @Excel(name = "代理线")
    private String agentLine;

    /** 邀请码 */
    @Excel(name = "邀请码")
    private String inviteCode;

    /** 账号类型： 0：真实用户 1：游客 */
    @Excel(name = "账号类型： 0：真实用户 1：游客")
    private Integer accountType;

    /** 用户头像 */
    @Excel(name = "用户头像")
    private String avatar;

    /** 是否锁定 0：正常 1：锁定 */
    @Excel(name = "是否锁定 0：正常 1：锁定")
    private Integer isLock;

    /** 注册时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "注册时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date regTime;

    /** 注册ip */
    @Excel(name = "注册ip")
    private String regIp;

    /** 注册地址 */
    @Excel(name = "注册地址")
    private String regAddress;

    /** 最后登录ip */
    @Excel(name = "最后登录ip")
    private String lastLoginIp;

    /** 最后登录时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "最后登录时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastLoginTime;

    /** 身份证号码 */
    @Excel(name = "身份证号码")
    private String idCard;

    /** 身份证图片（正面） */
    @Excel(name = "身份证图片", readConverterExp = "正=面")
    private String img1Key;

    /** 身份证图片（背面） */
    @Excel(name = "身份证图片", readConverterExp = "背=面")
    private String img2Key;

    /** 手持身份证图片 */
    @Excel(name = "手持身份证图片")
    private String img3Key;

    /** 是否已实名 0：未实名 1：审核中 2：审核通过 3：审核驳回 */
    @Excel(name = "是否已实名 0：未实名 1：审核中 2：审核通过 3：审核驳回")
    private Integer isActive;

    /** 初级实名认证状态：  0：未实名 1：审核中 2：审核通过 3：审核驳回 */
    private Integer authStatusJunior;

    /** 高级级实名认证状态：  0：未实名 1：审核中 2：审核通过 3：审核驳回 */
    private Integer authStatusSenior;

    /** 实名审核方式 0：身份证 1：驾驶证 2：护照 */
    @Excel(name = "实名审核方式 0：身份证 1：驾驶证 2：护照")
    private Integer authMethod;

    /** 实名审核反馈信息 */
    @Excel(name = "实名审核反馈信息")
    private String authMsg;

    /** 0：正常 1：控制 */
    @Excel(name = "0：正常 1：控制")
    private Integer isControl;

    /** 是否允许价格跌为负数 0：不允许 1：允许 */
    @Excel(name = "是否允许价格跌为负数 0：不允许 1：允许")
    private Integer allowFallIntoNegative;

    /** 单控启动延时时间（单位：秒） */
    @Excel(name = "单控启动延时时间", readConverterExp = "单=位：秒")
    private Integer startDelayTime;

    /** 单控溢出数值 */
    @Excel(name = "单控溢出数值")
    private BigDecimal overflowAmount;

    /** 上级用户id */
    @Excel(name = "上级用户id")
    private Long supUserId;

    /** 免登陆信息记录 */
    @Excel(name = "免登陆信息记录")
    private String noLoginInfo;

    /** 状态 0：正常 1：禁用 */
    @Excel(name = "状态 0：正常 1：禁用")
    private Integer status;

    /** 打码量 */
    @Excel(name = "打码量")
    private BigDecimal needOrderAmount;

    /** vip等级 */
    @Excel(name = "vip等级")
    private Integer vipLevel;

    /** 信用分 */
    @Excel(name = "信用分")
    private Integer creditScore;

    /** 是否能提现：0：是 1：否 */
    @Excel(name = "是否能提现：0：是 1：否")
    private Integer isCanWithdraw;

    /** 极速交易赢收益率 */
    @Excel(name = "极速交易赢收益率")
    private BigDecimal fastTradeWinProfitRatio;

    /** 极速交易输收益率 */
    @Excel(name = "极速交易输收益率")
    private BigDecimal fastTradeLoseProfitRatio;

    /** 是否设置为代理员 0：是 1：否 */
    @Excel(name = "是否设置为代理员 0：是 1：否")
    private Integer isAgent;

    /** 删除标志 0：未删除 1：已删除 */
    @Excel(name = "删除标志 0：未删除 1：已删除")
    private Integer isDel;

    /** 用户钱包余额信息 */
    private List<UserAmount> userAmounts;

    /** 用户上级线 */
    private String supUserLine;

    /**
     * 当前是否在线 0:离线 1:在线
     */
    private Integer currentOnline;

    /**
     * 用户充值金额
     */
    private BigDecimal rechargeAmount;

    /**
     * 用户总余额（折合USDT）
     */
    private BigDecimal userAmount;

    /** 关联账号ID */
    @Excel(name = "关联账号ID")
    private Long relateUserId;

    public Long getRelateUserId() {
        return relateUserId;
    }

    public void setRelateUserId(Long relateUserId) {
        this.relateUserId = relateUserId;
    }

    public Integer getAuthStatusJunior() {
        return authStatusJunior;
    }

    public void setAuthStatusJunior(Integer authStatusJunior) {
        this.authStatusJunior = authStatusJunior;
    }

    public Integer getAuthStatusSenior() {
        return authStatusSenior;
    }

    public void setAuthStatusSenior(Integer authStatusSenior) {
        this.authStatusSenior = authStatusSenior;
    }

    public BigDecimal getRechargeAmount() {
        return rechargeAmount;
    }

    public void setRechargeAmount(BigDecimal rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }

    public BigDecimal getUserAmount() {
        return userAmount;
    }

    public void setUserAmount(BigDecimal userAmount) {
        this.userAmount = userAmount;
    }

    public String getAgentLine() {
        return agentLine;
    }

    public void setAgentLine(String agentLine) {
        this.agentLine = agentLine;
    }

    public Integer getCurrentOnline() {
        return currentOnline;
    }

    public void setCurrentOnline(Integer currentOnline) {
        this.currentOnline = currentOnline;
    }

    public String getSupUserLine() {
        return supUserLine;
    }

    public void setSupUserLine(String supUserLine) {
        this.supUserLine = supUserLine;
    }

    public List<UserAmount> getUserAmounts() {
        return userAmounts;
    }

    public void setUserAmounts(List<UserAmount> userAmounts) {
        this.userAmounts = userAmounts;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setUserAccount(String userAccount) 
    {
        this.userAccount = userAccount;
    }

    public String getUserAccount() 
    {
        return userAccount;
    }
    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }
    public void setEmail(String email) 
    {
        this.email = email;
    }

    public String getEmail() 
    {
        return email;
    }
    public void setNickName(String nickName) 
    {
        this.nickName = nickName;
    }

    public String getNickName() 
    {
        return nickName;
    }
    public void setRealName(String realName) 
    {
        this.realName = realName;
    }

    public String getRealName() 
    {
        return realName;
    }

    public String getDappId() {
        return dappId;
    }

    public void setDappId(String dappId) {
        this.dappId = dappId;
    }

    public void setUserPwd(String userPwd)
    {
        this.userPwd = userPwd;
    }

    public String getUserPwd() 
    {
        return userPwd;
    }
    public void setWithPwd(String withPwd) 
    {
        this.withPwd = withPwd;
    }

    public String getWithPwd() 
    {
        return withPwd;
    }
    public void setAgentId(Long agentId) 
    {
        this.agentId = agentId;
    }

    public Long getAgentId() 
    {
        return agentId;
    }
    public void setAgentName(String agentName) 
    {
        this.agentName = agentName;
    }

    public String getAgentName() 
    {
        return agentName;
    }

    public String getAgentNickName() {
        return agentNickName;
    }

    public void setAgentNickName(String agentNickName) {
        this.agentNickName = agentNickName;
    }

    public void setInviteCode(String inviteCode)
    {
        this.inviteCode = inviteCode;
    }

    public String getInviteCode() 
    {
        return inviteCode;
    }
    public void setAccountType(Integer accountType) 
    {
        this.accountType = accountType;
    }

    public Integer getAccountType() 
    {
        return accountType;
    }
    public void setAvatar(String avatar) 
    {
        this.avatar = avatar;
    }

    public String getAvatar() 
    {
        return avatar;
    }
    public void setIsLock(Integer isLock) 
    {
        this.isLock = isLock;
    }

    public Integer getIsLock() 
    {
        return isLock;
    }
    public void setRegTime(Date regTime) 
    {
        this.regTime = regTime;
    }

    public Date getRegTime() 
    {
        return regTime;
    }
    public void setRegIp(String regIp) 
    {
        this.regIp = regIp;
    }

    public String getRegIp() 
    {
        return regIp;
    }
    public void setRegAddress(String regAddress) 
    {
        this.regAddress = regAddress;
    }

    public String getRegAddress() 
    {
        return regAddress;
    }
    public void setLastLoginIp(String lastLoginIp) 
    {
        this.lastLoginIp = lastLoginIp;
    }

    public String getLastLoginIp() 
    {
        return lastLoginIp;
    }
    public void setLastLoginTime(Date lastLoginTime) 
    {
        this.lastLoginTime = lastLoginTime;
    }

    public Date getLastLoginTime() 
    {
        return lastLoginTime;
    }
    public void setIdCard(String idCard) 
    {
        this.idCard = idCard;
    }

    public String getIdCard() 
    {
        return idCard;
    }
    public void setImg1Key(String img1Key) 
    {
        this.img1Key = img1Key;
    }

    public String getImg1Key() 
    {
        return img1Key;
    }
    public void setImg2Key(String img2Key) 
    {
        this.img2Key = img2Key;
    }

    public String getImg2Key() 
    {
        return img2Key;
    }
    public void setImg3Key(String img3Key) 
    {
        this.img3Key = img3Key;
    }

    public String getImg3Key() 
    {
        return img3Key;
    }
    public void setIsActive(Integer isActive) 
    {
        this.isActive = isActive;
    }

    public Integer getIsActive()
    {
        return isActive;
    }

    public Integer getAuthMethod() {
        return authMethod;
    }

    public void setAuthMethod(Integer authMethod) {
        this.authMethod = authMethod;
    }

    public void setAuthMsg(String authMsg)
    {
        this.authMsg = authMsg;
    }

    public String getAuthMsg() 
    {
        return authMsg;
    }
    public void setIsControl(Integer isControl) 
    {
        this.isControl = isControl;
    }

    public Integer getIsControl() 
    {
        return isControl;
    }
    public void setAllowFallIntoNegative(Integer allowFallIntoNegative) 
    {
        this.allowFallIntoNegative = allowFallIntoNegative;
    }

    public Integer getAllowFallIntoNegative() 
    {
        return allowFallIntoNegative;
    }
    public void setStartDelayTime(Integer startDelayTime) 
    {
        this.startDelayTime = startDelayTime;
    }

    public Integer getStartDelayTime() 
    {
        return startDelayTime;
    }
    public void setOverflowAmount(BigDecimal overflowAmount) 
    {
        this.overflowAmount = overflowAmount;
    }

    public BigDecimal getOverflowAmount() 
    {
        return overflowAmount;
    }
    public void setSupUserId(Long supUserId) 
    {
        this.supUserId = supUserId;
    }

    public Long getSupUserId() 
    {
        return supUserId;
    }
    public void setNoLoginInfo(String noLoginInfo) 
    {
        this.noLoginInfo = noLoginInfo;
    }

    public String getNoLoginInfo() 
    {
        return noLoginInfo;
    }
    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }
    public void setNeedOrderAmount(BigDecimal needOrderAmount) 
    {
        this.needOrderAmount = needOrderAmount;
    }

    public BigDecimal getNeedOrderAmount() 
    {
        return needOrderAmount;
    }
    public void setVipLevel(Integer vipLevel) 
    {
        this.vipLevel = vipLevel;
    }

    public Integer getVipLevel() 
    {
        return vipLevel;
    }
    public void setCreditScore(Integer creditScore) 
    {
        this.creditScore = creditScore;
    }

    public Integer getCreditScore() 
    {
        return creditScore;
    }
    public void setIsCanWithdraw(Integer isCanWithdraw) 
    {
        this.isCanWithdraw = isCanWithdraw;
    }

    public Integer getIsCanWithdraw() 
    {
        return isCanWithdraw;
    }
    public void setFastTradeWinProfitRatio(BigDecimal fastTradeWinProfitRatio) 
    {
        this.fastTradeWinProfitRatio = fastTradeWinProfitRatio;
    }

    public BigDecimal getFastTradeWinProfitRatio() 
    {
        return fastTradeWinProfitRatio;
    }
    public void setFastTradeLoseProfitRatio(BigDecimal fastTradeLoseProfitRatio) 
    {
        this.fastTradeLoseProfitRatio = fastTradeLoseProfitRatio;
    }

    public BigDecimal getFastTradeLoseProfitRatio() 
    {
        return fastTradeLoseProfitRatio;
    }
    public void setIsAgent(Integer isAgent) 
    {
        this.isAgent = isAgent;
    }

    public Integer getIsAgent() 
    {
        return isAgent;
    }

    public Long getUserNo() {
        try{
            return CacheUtil.getOtherValueByKey("appShow_idAddValue", Long.class) + getId();
        }catch (Exception e){
            return getId();
        }
    }

    public void setUserNo(Long userNo) {
        this.userNo = userNo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userAccount", getUserAccount())
            .append("phone", getPhone())
            .append("email", getEmail())
            .append("nickName", getNickName())
            .append("realName", getRealName())
            .append("userPwd", getUserPwd())
            .append("withPwd", getWithPwd())
            .append("agentId", getAgentId())
            .append("agentName", getAgentName())
            .append("inviteCode", getInviteCode())
            .append("accountType", getAccountType())
            .append("avatar", getAvatar())
            .append("isLock", getIsLock())
            .append("regTime", getRegTime())
            .append("regIp", getRegIp())
            .append("regAddress", getRegAddress())
            .append("lastLoginIp", getLastLoginIp())
            .append("lastLoginTime", getLastLoginTime())
            .append("idCard", getIdCard())
            .append("img1Key", getImg1Key())
            .append("img2Key", getImg2Key())
            .append("img3Key", getImg3Key())
            .append("isActive", getIsActive())
            .append("authMsg", getAuthMsg())
            .append("isControl", getIsControl())
            .append("allowFallIntoNegative", getAllowFallIntoNegative())
            .append("startDelayTime", getStartDelayTime())
            .append("overflowAmount", getOverflowAmount())
            .append("supUserId", getSupUserId())
            .append("noLoginInfo", getNoLoginInfo())
            .append("status", getStatus())
            .append("needOrderAmount", getNeedOrderAmount())
            .append("vipLevel", getVipLevel())
            .append("creditScore", getCreditScore())
            .append("isCanWithdraw", getIsCanWithdraw())
            .append("fastTradeWinProfitRatio", getFastTradeWinProfitRatio())
            .append("fastTradeLoseProfitRatio", getFastTradeLoseProfitRatio())
            .append("isAgent", getIsAgent())
            .toString();
    }
}
