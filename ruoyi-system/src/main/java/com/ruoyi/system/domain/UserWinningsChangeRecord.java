package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.entity.UserInfoDetailVo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 用户彩金出入记录对象 user_winnings_change_record
 * 
 * @author ruoyi
 * @date 2024-04-04
 */
public class UserWinningsChangeRecord extends UserInfoDetailVo
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    /** 订单号 */
    @Excel(name = "订单号")
    private String orderCode;

    /** 充值金额 */
    @Excel(name = "充值金额")
    private BigDecimal orderAmount;

    /** 订单类型： 0：彩金赠送（系统充值） 1：彩金赠送（福利彩金） 2：彩金回收 3:充值彩金 4：注册彩金 */
    @Excel(name = "订单类型： 0：彩金赠送（系统充值） 1：彩金赠送（福利彩金） 2：彩金回收 3:充值彩金 4：注册彩金 5：首充彩金")
    private Integer orderType;

    /** 操作人名称 */
    @Excel(name = "操作人名称")
    private String operatorName;

    /** 币种id */
    @Excel(name = "币种id")
    private Long currencyId;

    /** 币种名称 */
    @Excel(name = "币种名称")
    private String currencyName;

    /** 余额变更前 */
    @Excel(name = "余额变更前")
    private BigDecimal userAmountBefore;

    /** 余额变更后 */
    @Excel(name = "余额变更后")
    private BigDecimal userAmountAfter;

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
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setOrderCode(String orderCode) 
    {
        this.orderCode = orderCode;
    }

    public String getOrderCode() 
    {
        return orderCode;
    }
    public void setOrderAmount(BigDecimal orderAmount) 
    {
        this.orderAmount = orderAmount;
    }

    public BigDecimal getOrderAmount() 
    {
        return orderAmount;
    }
    public void setOrderType(Integer orderType) 
    {
        this.orderType = orderType;
    }

    public Integer getOrderType() 
    {
        return orderType;
    }
    public void setOperatorName(String operatorName) 
    {
        this.operatorName = operatorName;
    }

    public String getOperatorName() 
    {
        return operatorName;
    }
    public void setCurrencyId(Long currencyId) 
    {
        this.currencyId = currencyId;
    }

    public Long getCurrencyId() 
    {
        return currencyId;
    }
    public void setUserAmountBefore(BigDecimal userAmountBefore) 
    {
        this.userAmountBefore = userAmountBefore;
    }

    public BigDecimal getUserAmountBefore() 
    {
        return userAmountBefore;
    }
    public void setUserAmountAfter(BigDecimal userAmountAfter) 
    {
        this.userAmountAfter = userAmountAfter;
    }

    public BigDecimal getUserAmountAfter() 
    {
        return userAmountAfter;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("orderCode", getOrderCode())
            .append("orderAmount", getOrderAmount())
            .append("orderType", getOrderType())
            .append("createTime", getCreateTime())
            .append("operatorName", getOperatorName())
            .append("currencyId", getCurrencyId())
            .append("userAmountBefore", getUserAmountBefore())
            .append("userAmountAfter", getUserAmountAfter())
            .append("remark", getRemark())
            .toString();
    }
}
