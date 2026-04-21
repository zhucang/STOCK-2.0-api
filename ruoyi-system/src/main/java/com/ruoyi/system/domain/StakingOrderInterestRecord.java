package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 质押订单派息记录对象 staking_order_interest_record
 * 
 * @author ruoyi
 * @date 2025-07-20
 */
public class StakingOrderInterestRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 质押订单派息记录ID */
    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 质押订单ID */
    @Excel(name = "质押订单ID")
    private Long stakingOrderId;

    /** 质押池金额 */
    @Excel(name = "质押池金额")
    private BigDecimal stakingPoolAmount;

    /** 日收益率 */
    @Excel(name = "日收益率")
    private BigDecimal dailyIncomeRate;

    /** 派息时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "派息时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date payTime;

    /** 派息金额 */
    @Excel(name = "派息金额")
    private BigDecimal payAmount;

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
    public void setStakingOrderId(Long stakingOrderId) 
    {
        this.stakingOrderId = stakingOrderId;
    }

    public Long getStakingOrderId() 
    {
        return stakingOrderId;
    }

    public BigDecimal getStakingPoolAmount() {
        return stakingPoolAmount;
    }

    public void setStakingPoolAmount(BigDecimal stakingPoolAmount) {
        this.stakingPoolAmount = stakingPoolAmount;
    }

    public void setDailyIncomeRate(BigDecimal dailyIncomeRate)
    {
        this.dailyIncomeRate = dailyIncomeRate;
    }

    public BigDecimal getDailyIncomeRate() 
    {
        return dailyIncomeRate;
    }
    public void setPayTime(Date payTime) 
    {
        this.payTime = payTime;
    }

    public Date getPayTime() 
    {
        return payTime;
    }
    public void setPayAmount(BigDecimal payAmount) 
    {
        this.payAmount = payAmount;
    }

    public BigDecimal getPayAmount() 
    {
        return payAmount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("stakingOrderId", getStakingOrderId())
            .append("dailyIncomeRate", getDailyIncomeRate())
            .append("payTime", getPayTime())
            .append("payAmount", getPayAmount())
            .toString();
    }
}
