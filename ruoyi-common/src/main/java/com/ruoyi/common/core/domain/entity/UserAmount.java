package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 用户各币种余额对象 user_amount
 * 
 * @author ruoyi
 * @date 2023-10-28
 */
public class UserAmount extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 余额 */
    @Excel(name = "余额")
    private BigDecimal amount;

    /** 币种id */
    @Excel(name = "币种id")
    private Long currencyId;

    /** 币种名称 */
    @Excel(name = "币种名称")
    private String currencyName;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    /** 冻结金额 */
    @Excel(name = "冻结金额")
    private BigDecimal frozenAmount;

    /** 灵活投资资金 */
    @Excel(name = "灵活投资资金")
    private BigDecimal flexibleInvestmentFunds;

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
    public void setAmount(BigDecimal amount) 
    {
        this.amount = amount;
    }

    public BigDecimal getAmount() 
    {
        return amount;
    }
    public void setCurrencyId(Long currencyId) 
    {
        this.currencyId = currencyId;
    }

    public Long getCurrencyId() 
    {
        return currencyId;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setFrozenAmount(BigDecimal frozenAmount) 
    {
        this.frozenAmount = frozenAmount;
    }

    public BigDecimal getFrozenAmount() 
    {
        return frozenAmount;
    }

    public BigDecimal getFlexibleInvestmentFunds() {
        return flexibleInvestmentFunds;
    }

    public void setFlexibleInvestmentFunds(BigDecimal flexibleInvestmentFunds) {
        this.flexibleInvestmentFunds = flexibleInvestmentFunds;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("amount", getAmount())
            .append("currencyId", getCurrencyId())
            .append("userId", getUserId())
            .append("frozenAmount", getFrozenAmount())
            .toString();
    }
}
