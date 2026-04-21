package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 理财产品派息记录对象 financial_pay_interest_record
 * 
 * @author ruoyi
 * @date 2023-12-10
 */
public class FinancialPayInterestRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    /** 理财订单id */
    @Excel(name = "理财订单id")
    private Long financialOrderId;

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

    /** 是否结算 0：是 1：否 */
    @Excel(name = "是否结算 0：是 1：否")
    private Integer alreadySettleFlag;

    public Integer getAlreadySettleFlag() {
        return alreadySettleFlag;
    }

    public void setAlreadySettleFlag(Integer alreadySettleFlag) {
        this.alreadySettleFlag = alreadySettleFlag;
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
    public void setFinancialOrderId(Long financialOrderId) 
    {
        this.financialOrderId = financialOrderId;
    }

    public Long getFinancialOrderId() 
    {
        return financialOrderId;
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
            .append("financialOrderId", getFinancialOrderId())
            .append("dailyIncomeRate", getDailyIncomeRate())
            .append("payTime", getPayTime())
            .append("payAmount", getPayAmount())
            .toString();
    }
}
