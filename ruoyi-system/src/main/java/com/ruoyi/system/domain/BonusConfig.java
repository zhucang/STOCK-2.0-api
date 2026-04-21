package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 赠送彩金配置对象 bonus_config
 * 
 * @author ruoyi
 * @date 2023-11-29
 */
public class BonusConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** 赠送金额 */
    @Excel(name = "赠送金额")
    private BigDecimal bonusAmount;

    /** 币种id */
    @Excel(name = "币种id")
    private Long currencyId;

    /** 赠送类型 0：注册彩金 */
    @Excel(name = "赠送类型 0：注册彩金")
    private Integer bonusType;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setStartTime(Date startTime) 
    {
        this.startTime = startTime;
    }

    public Date getStartTime() 
    {
        return startTime;
    }
    public void setEndTime(Date endTime) 
    {
        this.endTime = endTime;
    }

    public Date getEndTime() 
    {
        return endTime;
    }
    public void setBonusAmount(BigDecimal bonusAmount) 
    {
        this.bonusAmount = bonusAmount;
    }

    public BigDecimal getBonusAmount() 
    {
        return bonusAmount;
    }
    public void setCurrencyId(Long currencyId) 
    {
        this.currencyId = currencyId;
    }

    public Long getCurrencyId() 
    {
        return currencyId;
    }
    public void setBonusType(Integer bonusType) 
    {
        this.bonusType = bonusType;
    }

    public Integer getBonusType() 
    {
        return bonusType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("bonusAmount", getBonusAmount())
            .append("currencyId", getCurrencyId())
            .append("bonusType", getBonusType())
            .toString();
    }
}
