package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 用户返佣比率配置对象 user_rebate_rate
 * 
 * @author ruoyi
 * @date 2023-11-05
 */
public class UserRebateRate extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 返佣名字 */
    @Excel(name = "返佣名字")
    private String rebateName;

    /** 返佣比率 */
    @Excel(name = "返佣比率")
    private BigDecimal rebateRate;

    /** 返佣类型：0：充值返佣 1:理财返佣 2：极速下单返佣 */
    @Excel(name = "返佣类型：0：充值返佣 1:理财返佣 2：极速下单返佣")
    private Integer rebateType;

    /** 返佣等级 */
    @Excel(name = "返佣等级")
    private Integer rebateLevel;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setRebateName(String rebateName) 
    {
        this.rebateName = rebateName;
    }

    public String getRebateName() 
    {
        return rebateName;
    }
    public void setRebateRate(BigDecimal rebateRate) 
    {
        this.rebateRate = rebateRate;
    }

    public BigDecimal getRebateRate() 
    {
        return rebateRate;
    }
    public void setRebateType(Integer rebateType) 
    {
        this.rebateType = rebateType;
    }

    public Integer getRebateType() 
    {
        return rebateType;
    }
    public void setRebateLevel(Integer rebateLevel) 
    {
        this.rebateLevel = rebateLevel;
    }

    public Integer getRebateLevel() 
    {
        return rebateLevel;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("rebateName", getRebateName())
            .append("rebateRate", getRebateRate())
            .append("rebateType", getRebateType())
            .append("rebateLevel", getRebateLevel())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
