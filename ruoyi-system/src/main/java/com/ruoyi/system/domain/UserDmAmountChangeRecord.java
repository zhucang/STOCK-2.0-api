package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 用户打码量变更记录对象 user_dm_amount_change_record
 * 
 * @author ruoyi
 * @date 2024-05-24
 */
public class UserDmAmountChangeRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    /** 订单金额 */
    @Excel(name = "订单金额")
    private BigDecimal orderAmount;

    /** 打码倍数 */
    @Excel(name = "打码倍数")
    private BigDecimal dmMultiples;

    /** 打码量 */
    @Excel(name = "打码量")
    private BigDecimal dmAmount;

    /** 打码量变更前 */
    @Excel(name = "打码量变更前")
    private BigDecimal dmAmountBefore;

    /** 打码量变更后 */
    @Excel(name = "打码量变更后")
    private BigDecimal dmAmountAfter;

    /** 变更类型 0：后台上分 1：用户充值 2：后台赠送彩金 3：后台修改用户打码量 4:用户贷款*/
    @Excel(name = "变更类型 0：后台上分 1：用户充值 2：后台赠送彩金 3：后台修改用户打码量 4:用户贷款")
    private Integer orderType;

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
    public void setOrderAmount(BigDecimal orderAmount) 
    {
        this.orderAmount = orderAmount;
    }

    public BigDecimal getOrderAmount() 
    {
        return orderAmount;
    }
    public void setDmMultiples(BigDecimal dmMultiples) 
    {
        this.dmMultiples = dmMultiples;
    }

    public BigDecimal getDmMultiples() 
    {
        return dmMultiples;
    }
    public void setDmAmount(BigDecimal dmAmount) 
    {
        this.dmAmount = dmAmount;
    }

    public BigDecimal getDmAmount() 
    {
        return dmAmount;
    }
    public void setDmAmountBefore(BigDecimal dmAmountBefore) 
    {
        this.dmAmountBefore = dmAmountBefore;
    }

    public BigDecimal getDmAmountBefore() 
    {
        return dmAmountBefore;
    }
    public void setDmAmountAfter(BigDecimal dmAmountAfter) 
    {
        this.dmAmountAfter = dmAmountAfter;
    }

    public BigDecimal getDmAmountAfter() 
    {
        return dmAmountAfter;
    }
    public void setOrderType(Integer orderType) 
    {
        this.orderType = orderType;
    }

    public Integer getOrderType() 
    {
        return orderType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("orderAmount", getOrderAmount())
            .append("dmMultiples", getDmMultiples())
            .append("dmAmount", getDmAmount())
            .append("dmAmountBefore", getDmAmountBefore())
            .append("dmAmountAfter", getDmAmountAfter())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("orderType", getOrderType())
            .toString();
    }
}
