package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 货币兑换汇率对象 currency_exchange_rate
 * 
 * @author ruoyi
 * @date 2023-11-25
 */
public class CurrencyExchangeRate extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 转化者货币id */
    @Excel(name = "转化者货币id")
    private Long currencyIdFrom;

    /** 转化者币种名称 */
    @Excel(name = "转化者币种名称")
    private String currencyNameFrom;

    /** 转化者币种描述 */
    @Excel(name = "转化者币种描述")
    private String currencyDescFrom;

    /** 被转化者货币id */
    @Excel(name = "被转化者货币id")
    private Long currencyIdTo;

    /** 被转化者币种名称 */
    @Excel(name = "被转化者币种名称")
    private String currencyNameTo;

    /** 被转化者币种描述 */
    @Excel(name = "被转化者币种描述")
    private String currencyDescTo;

    /** 汇率 */
    @Excel(name = "汇率")
    private BigDecimal exchangeRate;

    /** 反汇率 */
    @Excel(name = "反汇率")
    private BigDecimal oppositeExchangeRate;

    /** 手续费率 */
    @Excel(name = "手续费率")
    private BigDecimal feePercent;

    /** 是否固定汇率 0:固定汇率   1:实时汇率 */
    @Excel(name = "是否固定汇率 0:固定汇率   1:实时汇率")
    private Integer isFixedExchangeRate;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setCurrencyIdFrom(Long currencyIdFrom) 
    {
        this.currencyIdFrom = currencyIdFrom;
    }

    public Long getCurrencyIdFrom()
    {
        return currencyIdFrom;
    }

    public String getCurrencyNameFrom() {
        return currencyNameFrom;
    }

    public void setCurrencyNameFrom(String currencyNameFrom) {
        this.currencyNameFrom = currencyNameFrom;
    }

    public String getCurrencyDescFrom() {
        return currencyDescFrom;
    }

    public void setCurrencyDescFrom(String currencyDescFrom) {
        this.currencyDescFrom = currencyDescFrom;
    }

    public void setCurrencyIdTo(Long currencyIdTo)
    {
        this.currencyIdTo = currencyIdTo;
    }

    public Long getCurrencyIdTo() 
    {
        return currencyIdTo;
    }

    public String getCurrencyNameTo() {
        return currencyNameTo;
    }

    public void setCurrencyNameTo(String currencyNameTo) {
        this.currencyNameTo = currencyNameTo;
    }

    public String getCurrencyDescTo() {
        return currencyDescTo;
    }

    public void setCurrencyDescTo(String currencyDescTo) {
        this.currencyDescTo = currencyDescTo;
    }

    public void setExchangeRate(BigDecimal exchangeRate)
    {
        this.exchangeRate = exchangeRate;
    }

    public BigDecimal getExchangeRate() 
    {
        return exchangeRate;
    }
    public void setOppositeExchangeRate(BigDecimal oppositeExchangeRate) 
    {
        this.oppositeExchangeRate = oppositeExchangeRate;
    }

    public BigDecimal getOppositeExchangeRate() 
    {
        return oppositeExchangeRate;
    }
    public void setFeePercent(BigDecimal feePercent) 
    {
        this.feePercent = feePercent;
    }

    public BigDecimal getFeePercent() 
    {
        return feePercent;
    }
    public void setIsFixedExchangeRate(Integer isFixedExchangeRate) 
    {
        this.isFixedExchangeRate = isFixedExchangeRate;
    }

    public Integer getIsFixedExchangeRate() 
    {
        return isFixedExchangeRate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("currencyIdFrom", getCurrencyIdFrom())
            .append("currencyIdTo", getCurrencyIdTo())
            .append("exchangeRate", getExchangeRate())
            .append("oppositeExchangeRate", getOppositeExchangeRate())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("feePercent", getFeePercent())
            .append("isFixedExchangeRate", getIsFixedExchangeRate())
            .toString();
    }
}
