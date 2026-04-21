package com.ruoyi.system.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * VIP经验值对象 vip_experience_value
 * 
 * @author ruoyi
 * @date 2024-10-24
 */
public class VipExperienceValue extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 关联订单号 */
    @Excel(name = "关联订单号")
    private String relateOrderCode;

    /** 本次经验值 */
    @Excel(name = "本次经验值")
    private BigDecimal experienceValue;

    /** 经验值更新前 */
    @Excel(name = "经验值更新前")
    private BigDecimal experienceValueBefore;

    /** 经验值更新后 */
    @Excel(name = "经验值更新后")
    private BigDecimal experienceValueAfter;

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
    public void setRelateOrderCode(String relateOrderCode) 
    {
        this.relateOrderCode = relateOrderCode;
    }

    public String getRelateOrderCode() 
    {
        return relateOrderCode;
    }

    public BigDecimal getExperienceValue() {
        return experienceValue;
    }

    public void setExperienceValue(BigDecimal experienceValue) {
        this.experienceValue = experienceValue;
    }

    public void setExperienceValueBefore(BigDecimal experienceValueBefore)
    {
        this.experienceValueBefore = experienceValueBefore;
    }

    public BigDecimal getExperienceValueBefore() 
    {
        return experienceValueBefore;
    }
    public void setExperienceValueAfter(BigDecimal experienceValueAfter) 
    {
        this.experienceValueAfter = experienceValueAfter;
    }

    public BigDecimal getExperienceValueAfter() 
    {
        return experienceValueAfter;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("relateOrderCode", getRelateOrderCode())
            .append("experienceValueBefore", getExperienceValueBefore())
            .append("experienceValueAfter", getExperienceValueAfter())
            .append("createTime", getCreateTime())
            .toString();
    }
}
