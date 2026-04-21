package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 充值通道配置对象 recharge_channel_config
 * 
 * @author ruoyi
 * @date 2023-11-04
 */
public class RechargeChannelConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 通道名称 */
    @Excel(name = "通道名称")
    private String channelName;

    /** 通道名称多语言 */
    @Excel(name = "通道名称多语言")
    private LangMgr channelNameLang;

    /** 收款账号 */
    @Excel(name = "收款账号")
    private String channelAccount;

    /** 钱包收款码 */
    @Excel(name = "钱包收款码")
    private String walletReceiptCode;

    /** 通道图标 */
    @Excel(name = "通道图标")
    private String channelImg;

    /** 银行名称 */
    @Excel(name = "银行名称")
    private String bankName;

    /** 开户行 */
    @Excel(name = "开户行")
    private String accountOpenBank;

    /** 持有人 */
    @Excel(name = "持有人")
    private String holder;

    /** 绑定手机号 */
    @Excel(name = "绑定手机号")
    private String bankPhone;

    /** 银行国家 */
    @Excel(name = "银行国家")
    private String bankCountry;

    /** ABA代码 */
    @Excel(name = "ABA代码")
    private String abaCode;

    /** swift */
    @Excel(name = "swift")
    private String swift;

    /** 邮编号码 */
    @Excel(name = "邮编号码")
    private String postCode;

    /** 用户地址 */
    @Excel(name = "用户地址")
    private String userRealAddress;

    /** 账户类型 */
    @Excel(name = "账户类型")
    private String accountType;

    /** 最小入金 */
    @Excel(name = "最小入金")
    private BigDecimal channelMinLimit;

    /** 最大入金 */
    @Excel(name = "最大入金")
    private BigDecimal channelMaxLimit;

    /** 通道类型 0：加密货币 1：电汇 2：客服 */
    @Excel(name = "通道类型 0：加密货币 1：电汇 2：客服")
    private Integer channelType;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 原始通道ID */
    @Excel(name = "原始通道ID")
    private Long originalChannelId;

    /** 币种id */
    @Excel(name = "币种id")
    private Long currencyId;

    /** 币种名称 */
    @Excel(name = "币种名称")
    private String currencyName;

    /** 排序 */
    @Excel(name = "排序")
    private Integer sort;

    /** 快捷充值金额(使用/隔开) */
    @Excel(name = "快捷充值金额(使用/隔开)")
    private String quickAmounts;

    /** 客服跳转url */
    @Excel(name = "客服跳转url")
    private String customerUrl;

    /** 首次充值赠送金额 */
    @Excel(name = "首次充值赠送金额")
    private BigDecimal firstRechargeWinningsAmount;

    /** 首次充值赠送比率 */
    @Excel(name = "首次充值赠送比率")
    private BigDecimal firstRechargeWinningsRate;

    /** 首次充值赠送方式 0：按比例赠送 1：按固定金额赠送 */
    @Excel(name = "首次充值赠送方式 0：按比例赠送 1：按固定金额赠送")
    private Integer firstRechargeWinningsMethod;

    /** 日常充值赠送金额 */
    @Excel(name = "日常充值赠送金额")
    private BigDecimal dailyRechargeWinningsAmount;

    /** 日常充值赠送比率 */
    @Excel(name = "日常充值赠送比率")
    private BigDecimal dailyRechargeWinningsRate;

    /** 日常充值赠送方式 0：按比例赠送 1：按固定金额赠送 */
    @Excel(name = "日常充值赠送方式 0：按比例赠送 1：按固定金额赠送")
    private Integer dailyRechargeWinningsMethod;


    /** 状态 0：启用 1：禁用0 */
    @Excel(name = "状态 0：启用 1：禁用0")
    private Integer status;

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setChannelName(String channelName) 
    {
        this.channelName = channelName;
    }

    public String getChannelName() 
    {
        return channelName;
    }
    public void setChannelAccount(String channelAccount) 
    {
        this.channelAccount = channelAccount;
    }

    public String getChannelAccount() 
    {
        return channelAccount;
    }
    public void setWalletReceiptCode(String walletReceiptCode) 
    {
        this.walletReceiptCode = walletReceiptCode;
    }

    public String getWalletReceiptCode() 
    {
        return walletReceiptCode;
    }
    public void setChannelImg(String channelImg) 
    {
        this.channelImg = channelImg;
    }

    public String getChannelImg() 
    {
        return channelImg;
    }
    public void setBankName(String bankName) 
    {
        this.bankName = bankName;
    }

    public String getBankName() 
    {
        return bankName;
    }
    public void setAccountOpenBank(String accountOpenBank) 
    {
        this.accountOpenBank = accountOpenBank;
    }

    public String getAccountOpenBank() 
    {
        return accountOpenBank;
    }
    public void setHolder(String holder) 
    {
        this.holder = holder;
    }

    public String getHolder() 
    {
        return holder;
    }

    public String getBankCountry() {
        return bankCountry;
    }

    public void setBankCountry(String bankCountry) {
        this.bankCountry = bankCountry;
    }

    public String getAbaCode() {
        return abaCode;
    }

    public void setAbaCode(String abaCode) {
        this.abaCode = abaCode;
    }

    public String getSwift() {
        return swift;
    }

    public void setSwift(String swift) {
        this.swift = swift;
    }

    public void setChannelMinLimit(BigDecimal channelMinLimit)
    {
        this.channelMinLimit = channelMinLimit;
    }

    public String getBankPhone() {
        return bankPhone;
    }

    public void setBankPhone(String bankPhone) {
        this.bankPhone = bankPhone;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getUserRealAddress() {
        return userRealAddress;
    }

    public void setUserRealAddress(String userRealAddress) {
        this.userRealAddress = userRealAddress;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getChannelMinLimit()
    {
        return channelMinLimit;
    }
    public void setChannelMaxLimit(BigDecimal channelMaxLimit) 
    {
        this.channelMaxLimit = channelMaxLimit;
    }

    public BigDecimal getChannelMaxLimit() 
    {
        return channelMaxLimit;
    }
    public void setChannelType(Integer channelType) 
    {
        this.channelType = channelType;
    }

    public Integer getChannelType() 
    {
        return channelType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOriginalChannelId() {
        return originalChannelId;
    }

    public void setOriginalChannelId(Long originalChannelId) {
        this.originalChannelId = originalChannelId;
    }

    public void setCurrencyId(Long currencyId)
    {
        this.currencyId = currencyId;
    }

    public Long getCurrencyId() 
    {
        return currencyId;
    }
    public void setSort(Integer sort) 
    {
        this.sort = sort;
    }

    public Integer getSort() 
    {
        return sort;
    }
    public void setQuickAmounts(String quickAmounts) 
    {
        this.quickAmounts = quickAmounts;
    }

    public String getQuickAmounts() 
    {
        return quickAmounts;
    }
    public void setCustomerUrl(String customerUrl) 
    {
        this.customerUrl = customerUrl;
    }

    public String getCustomerUrl() 
    {
        return customerUrl;
    }
    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }

    public BigDecimal getFirstRechargeWinningsAmount() {
        return firstRechargeWinningsAmount;
    }

    public void setFirstRechargeWinningsAmount(BigDecimal firstRechargeWinningsAmount) {
        this.firstRechargeWinningsAmount = firstRechargeWinningsAmount;
    }

    public BigDecimal getFirstRechargeWinningsRate() {
        return firstRechargeWinningsRate;
    }

    public void setFirstRechargeWinningsRate(BigDecimal firstRechargeWinningsRate) {
        this.firstRechargeWinningsRate = firstRechargeWinningsRate;
    }

    public Integer getFirstRechargeWinningsMethod() {
        return firstRechargeWinningsMethod;
    }

    public void setFirstRechargeWinningsMethod(Integer firstRechargeWinningsMethod) {
        this.firstRechargeWinningsMethod = firstRechargeWinningsMethod;
    }

    public BigDecimal getDailyRechargeWinningsAmount() {
        return dailyRechargeWinningsAmount;
    }

    public void setDailyRechargeWinningsAmount(BigDecimal dailyRechargeWinningsAmount) {
        this.dailyRechargeWinningsAmount = dailyRechargeWinningsAmount;
    }

    public BigDecimal getDailyRechargeWinningsRate() {
        return dailyRechargeWinningsRate;
    }

    public void setDailyRechargeWinningsRate(BigDecimal dailyRechargeWinningsRate) {
        this.dailyRechargeWinningsRate = dailyRechargeWinningsRate;
    }

    public Integer getDailyRechargeWinningsMethod() {
        return dailyRechargeWinningsMethod;
    }

    public void setDailyRechargeWinningsMethod(Integer dailyRechargeWinningsMethod) {
        this.dailyRechargeWinningsMethod = dailyRechargeWinningsMethod;
    }

    public LangMgr getChannelNameLang() {
        return channelNameLang;
    }

    public void setChannelNameLang(LangMgr channelNameLang) {
        this.channelNameLang = channelNameLang;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("channelName", getChannelName())
            .append("channelAccount", getChannelAccount())
            .append("walletReceiptCode", getWalletReceiptCode())
            .append("channelImg", getChannelImg())
            .append("bankName", getBankName())
            .append("accountOpenBank", getAccountOpenBank())
            .append("holder", getHolder())
            .append("channelMinLimit", getChannelMinLimit())
            .append("channelMaxLimit", getChannelMaxLimit())
            .append("channelType", getChannelType())
            .append("currencyId", getCurrencyId())
            .append("sort", getSort())
            .append("quickAmounts", getQuickAmounts())
            .append("customerUrl", getCustomerUrl())
            .append("status", getStatus())
            .append("channelNameLang", getChannelNameLang())
            .toString();
    }
}
