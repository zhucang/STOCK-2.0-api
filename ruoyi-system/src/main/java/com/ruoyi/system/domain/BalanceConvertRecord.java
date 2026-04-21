package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.entity.UserInfoDetailVo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 资金互转记录对象 balance_convert_record
 * 
 * @author ruoyi
 * @date 2023-12-01
 */
public class BalanceConvertRecord extends UserInfoDetailVo
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    /** 转化金额 */
    @Excel(name = "转化金额")
    private BigDecimal transAmt;

    /** 换算金额 */
    @Excel(name = "换算金额")
    private BigDecimal convertedAmount;

    /** 汇率 */
    @Excel(name = "汇率")
    private BigDecimal exchangeRate;

    /** 手续费金额 */
    @Excel(name = "手续费金额")
    private BigDecimal handingFee;

    /** 转化from币种id */
    @Excel(name = "转化from币种id")
    private Long currencyFromId;

    /** 转化to币种id */
    @Excel(name = "转化to币种id")
    private Long currencyToId;

    /** 转化from币种名称 */
    @Excel(name = "转化from币种名称")
    private String currencyFromName;

    /** 转化to币种名称 */
    @Excel(name = "转化to币种名称")
    private String currencyToName;

    public BigDecimal getHandingFee() {
        return handingFee;
    }

    public void setHandingFee(BigDecimal handingFee) {
        this.handingFee = handingFee;
    }

    public String getCurrencyFromName() {
        return currencyFromName;
    }

    public void setCurrencyFromName(String currencyFromName) {
        this.currencyFromName = currencyFromName;
    }

    public String getCurrencyToName() {
        return currencyToName;
    }

    public void setCurrencyToName(String currencyToName) {
        this.currencyToName = currencyToName;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setTransAmt(BigDecimal transAmt) 
    {
        this.transAmt = transAmt;
    }

    public BigDecimal getTransAmt() 
    {
        return transAmt;
    }
    public void setConvertedAmount(BigDecimal convertedAmount) 
    {
        this.convertedAmount = convertedAmount;
    }

    public BigDecimal getConvertedAmount() 
    {
        return convertedAmount;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public void setCurrencyFromId(Long currencyFromId)
    {
        this.currencyFromId = currencyFromId;
    }

    public Long getCurrencyFromId() 
    {
        return currencyFromId;
    }
    public void setCurrencyToId(Long currencyToId) 
    {
        this.currencyToId = currencyToId;
    }

    public Long getCurrencyToId() 
    {
        return currencyToId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("transAmt", getTransAmt())
            .append("convertedAmount", getConvertedAmount())
            .append("createTime", getCreateTime())
            .append("currencyFromId", getCurrencyFromId())
            .append("currencyToId", getCurrencyToId())
            .toString();
    }
}
