package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 产品买入卖出手续费配置对象 order_fee_setting
 * 
 * @author ruoyi
 * @date 2023-11-27
 */
public class OrderFeeSetting extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** key */
    @Excel(name = "key")
    private String key;

    /** 手续费费率 */
    @Excel(name = "手续费费率")
    private BigDecimal feeRate;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setKey(String key) 
    {
        this.key = key;
    }

    public String getKey() 
    {
        return key;
    }
    public void setFeeRate(BigDecimal feeRate) 
    {
        this.feeRate = feeRate;
    }

    public BigDecimal getFeeRate() 
    {
        return feeRate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("key", getKey())
            .append("feeRate", getFeeRate())
            .append("remark", getRemark())
            .toString();
    }
}
