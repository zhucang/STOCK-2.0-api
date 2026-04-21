package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;


/**
 * 用户返佣记录对象 user_commission_record
 * 
 * @author ruoyi
 * @date 2023-09-09
 */
public class UserCommissionRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 上级id */
    @Excel(name = "上级id")
    private Long superId;

    /** 下级id */
    @Excel(name = "下级id")
    private Long lowerId;

    /** 返佣等级 */
    @Excel(name = "返佣等级")
    private Integer commissionLevel;

    /** 返佣金额 */
    @Excel(name = "返佣金额")
    private BigDecimal commissionAmount;

    /** 返佣比例 */
    @Excel(name = "返佣比例")
    private BigDecimal commissionProfit;

    /** 返佣的来源订单号 */
    @Excel(name = "返佣的来源订单号")
    private String orderCodeSource;

    /** 返佣的流水订单号 */
    @Excel(name = "返佣的流水订单号")
    private String orderCodeCommission;

    /** 币种id */
    @Excel(name = "币种id")
    private Long currencyId;

    /** 返佣类型 0：充值 */
    @Excel(name = "返佣类型 0：充值")
    private Integer commissionType;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setSuperId(Long superId) 
    {
        this.superId = superId;
    }

    public Long getSuperId() 
    {
        return superId;
    }
    public void setLowerId(Long lowerId) 
    {
        this.lowerId = lowerId;
    }

    public Long getLowerId() 
    {
        return lowerId;
    }
    public void setCommissionLevel(Integer commissionLevel)
    {
        this.commissionLevel = commissionLevel;
    }

    public Integer getCommissionLevel()
    {
        return commissionLevel;
    }
    public void setCommissionAmount(BigDecimal commissionAmount)
    {
        this.commissionAmount = commissionAmount;
    }

    public BigDecimal getCommissionAmount()
    {
        return commissionAmount;
    }
    public void setCommissionProfit(BigDecimal commissionProfit) 
    {
        this.commissionProfit = commissionProfit;
    }

    public BigDecimal getCommissionProfit() 
    {
        return commissionProfit;
    }
    public void setOrderCodeSource(String orderCodeSource) 
    {
        this.orderCodeSource = orderCodeSource;
    }

    public String getOrderCodeSource() 
    {
        return orderCodeSource;
    }
    public void setOrderCodeCommission(String orderCodeCommission) 
    {
        this.orderCodeCommission = orderCodeCommission;
    }

    public String getOrderCodeCommission() 
    {
        return orderCodeCommission;
    }
    public void setCurrencyId(Long currencyId) 
    {
        this.currencyId = currencyId;
    }

    public Long getCurrencyId() 
    {
        return currencyId;
    }
    public void setCommissionType(Integer commissionType) 
    {
        this.commissionType = commissionType;
    }

    public Integer getCommissionType() 
    {
        return commissionType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("superId", getSuperId())
            .append("lowerId", getLowerId())
            .append("commissionLevel", getCommissionLevel())
            .append("commissionAmount", getCommissionAmount())
            .append("commissionProfit", getCommissionProfit())
            .append("orderCodeSource", getOrderCodeSource())
            .append("orderCodeCommission", getOrderCodeCommission())
            .append("createTime", getCreateTime())
            .append("currencyId", getCurrencyId())
            .append("commissionType", getCommissionType())
            .toString();
    }
}
