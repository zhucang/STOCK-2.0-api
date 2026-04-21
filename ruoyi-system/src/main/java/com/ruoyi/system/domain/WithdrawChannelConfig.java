package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 提现通道配置对象 withdraw_channel_config
 * 
 * @author ruoyi
 * @date 2023-11-04
 */
public class WithdrawChannelConfig extends BaseEntity
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

    /** 提现最小金额 */
    @Excel(name = "提现最小金额")
    private BigDecimal withdrawMinAmount;

    /** 提现最大金额 */
    @Excel(name = "提现最大金额")
    private BigDecimal withdrawMaxAmount;

    /** 平台交易币种id */
    @Excel(name = "平台交易币种id")
    private Long currencyId;

    /** 币种名称 */
    @Excel(name = "币种名称")
    private String currencyName;

    /** 到账币种id */
    @Excel(name = "到账币种id")
    private Long arrivalCurrencyId;

    /** 到账币种名称 */
    @Excel(name = "到账币种名称")
    private String arrivalCurrencyName;

    /** 通道图片 */
    @Excel(name = "通道图片")
    private String channelImg;

    /** 0：百分比手续费  1：固定手续费 */
    @Excel(name = "0：百分比手续费  1：固定手续费")
    private Integer handingFeeMethod;

    /** 固定手续费金额 */
    @Excel(name = "固定手续费金额")
    private BigDecimal handingFeeFixedAmount;

    /** 百分比手续费率 */
    @Excel(name = "百分比手续费率")
    private BigDecimal handingFeeRate;

    /** 每日提现开始时间 */
    @Excel(name = "每日提现开始时间")
    private String withdrawStartTime;

    /** 每日提现结束时间 */
    @Excel(name = "每日提现结束时间")
    private String withdrawEndTime;

    /** 每日可提现次数 */
    @Excel(name = "每日可提现次数")
    private Integer withdrawCount;

    /** 0:启用  1：禁用 */
    @Excel(name = "0:启用  1：禁用")
    private Integer status;

    /** 排序 */
    @Excel(name = "排序")
    private Integer sort;

    /** 提现类型 0：银行卡 1：虚拟钱包 2：客服 */
    @Excel(name = "提现类型 0：银行卡 1：虚拟钱包 2：客服")
    private Integer withdrawType;

    /** 钱包地址类型 */
    @Excel(name = "钱包地址类型")
    private String walletAddressType;

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getWalletAddressType() {
        return walletAddressType;
    }

    public void setWalletAddressType(String walletAddressType) {
        this.walletAddressType = walletAddressType;
    }
    public Integer getWithdrawType() {
        return withdrawType;
    }

    public void setWithdrawType(Integer withdrawType) {
        this.withdrawType = withdrawType;
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
    public void setWithdrawMinAmount(BigDecimal withdrawMinAmount) 
    {
        this.withdrawMinAmount = withdrawMinAmount;
    }

    public BigDecimal getWithdrawMinAmount() 
    {
        return withdrawMinAmount;
    }
    public void setWithdrawMaxAmount(BigDecimal withdrawMaxAmount) 
    {
        this.withdrawMaxAmount = withdrawMaxAmount;
    }

    public BigDecimal getWithdrawMaxAmount() 
    {
        return withdrawMaxAmount;
    }
    public void setCurrencyId(Long currencyId) 
    {
        this.currencyId = currencyId;
    }

    public Long getArrivalCurrencyId() {
        return arrivalCurrencyId;
    }

    public void setArrivalCurrencyId(Long arrivalCurrencyId) {
        this.arrivalCurrencyId = arrivalCurrencyId;
    }

    public String getArrivalCurrencyName() {
        return arrivalCurrencyName;
    }

    public void setArrivalCurrencyName(String arrivalCurrencyName) {
        this.arrivalCurrencyName = arrivalCurrencyName;
    }

    public Long getCurrencyId()
    {
        return currencyId;
    }
    public void setChannelImg(String channelImg) 
    {
        this.channelImg = channelImg;
    }

    public String getChannelImg() 
    {
        return channelImg;
    }
    public void setHandingFeeMethod(Integer handingFeeMethod) 
    {
        this.handingFeeMethod = handingFeeMethod;
    }

    public Integer getHandingFeeMethod() 
    {
        return handingFeeMethod;
    }
    public void setHandingFeeFixedAmount(BigDecimal handingFeeFixedAmount) 
    {
        this.handingFeeFixedAmount = handingFeeFixedAmount;
    }

    public BigDecimal getHandingFeeFixedAmount() 
    {
        return handingFeeFixedAmount;
    }
    public void setHandingFeeRate(BigDecimal handingFeeRate) 
    {
        this.handingFeeRate = handingFeeRate;
    }

    public BigDecimal getHandingFeeRate() 
    {
        return handingFeeRate;
    }
    public void setWithdrawStartTime(String withdrawStartTime) 
    {
        this.withdrawStartTime = withdrawStartTime;
    }

    public String getWithdrawStartTime() 
    {
        return withdrawStartTime;
    }
    public void setWithdrawEndTime(String withdrawEndTime) 
    {
        this.withdrawEndTime = withdrawEndTime;
    }

    public String getWithdrawEndTime() 
    {
        return withdrawEndTime;
    }
    public void setWithdrawCount(Integer withdrawCount)
    {
        this.withdrawCount = withdrawCount;
    }

    public Integer getWithdrawCount()
    {
        return withdrawCount;
    }
    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }
    public void setSort(Integer sort)
    {
        this.sort = sort;
    }

    public Integer getSort()
    {
        return sort;
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
            .append("withdrawMinAmount", getWithdrawMinAmount())
            .append("withdrawMaxAmount", getWithdrawMaxAmount())
            .append("currencyId", getCurrencyId())
            .append("channelImg", getChannelImg())
            .append("handingFeeMethod", getHandingFeeMethod())
            .append("handingFeeFixedAmount", getHandingFeeFixedAmount())
            .append("handingFeeRate", getHandingFeeRate())
            .append("withdrawStartTime", getWithdrawStartTime())
            .append("withdrawEndTime", getWithdrawEndTime())
            .append("withdrawCount", getWithdrawCount())
            .append("status", getStatus())
            .append("sort", getSort())
            .append("channelNameLang", getChannelNameLang())
            .toString();
    }
}
