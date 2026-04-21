package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.entity.UserInfoDetailVo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 用户贷款还款订单对象 user_loan_repayment_order
 * 
 * @author ruoyi
 * @date 2024-05-28
 */
public class UserLoanRepaymentOrder extends UserInfoDetailVo
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 贷款订单id */
    @Excel(name = "贷款订单id")
    private Long loanOrderId;

    /** 贷款订单号 */
    @Excel(name = "贷款订单号")
    private String loanOrderCode;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    /** 订单号 */
    @Excel(name = "订单号")
    private String orderCode;

    /** 还款金额 */
    @Excel(name = "还款金额")
    private BigDecimal orderAmount;

    /** 状态：0：审核中 1:通过 2：驳回 3：待审核 */
    @Excel(name = "状态：0：审核中 1:通过 2：驳回 3：待审核")
    private Integer orderStatus;

    /** 操作人名称 */
    @Excel(name = "操作人名称")
    private String operatorName;

    /** 充值通道名称 */
    @Excel(name = "充值通道名称")
    private String payChannelName;

    /** 支付通道id */
    @Excel(name = "支付通道id")
    private Long payChannelId;

    /** 币种id */
    @Excel(name = "币种id")
    private Long currencyId;

    /** 币种名称 */
    @Excel(name = "币种名称")
    private String currencyName;

    /** 支付凭证 */
    @Excel(name = "支付凭证")
    private String rechargeImg;

    /** 返回信息 */
    @Excel(name = "返回信息")
    private String rechargeMsg;

    /** 充值方式： 0：线下充值 1：在线支付 */
    @Excel(name = "充值方式： 0：线下充值 1：在线支付")
    private Integer rechargeMethod;

    /** sql版本 */
    @Excel(name = "sql版本")
    private Long sqlVersion;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setLoanOrderId(Long loanOrderId) 
    {
        this.loanOrderId = loanOrderId;
    }

    public Long getLoanOrderId() 
    {
        return loanOrderId;
    }

    public String getLoanOrderCode() {
        return loanOrderCode;
    }

    public void setLoanOrderCode(String loanOrderCode) {
        this.loanOrderCode = loanOrderCode;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
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
    public void setOrderStatus(Integer orderStatus) 
    {
        this.orderStatus = orderStatus;
    }

    public Integer getOrderStatus() 
    {
        return orderStatus;
    }
    public void setOperatorName(String operatorName) 
    {
        this.operatorName = operatorName;
    }

    public String getOperatorName() 
    {
        return operatorName;
    }
    public void setPayChannelName(String payChannelName) 
    {
        this.payChannelName = payChannelName;
    }

    public String getPayChannelName() 
    {
        return payChannelName;
    }
    public void setPayChannelId(Long payChannelId) 
    {
        this.payChannelId = payChannelId;
    }

    public Long getPayChannelId() 
    {
        return payChannelId;
    }
    public void setCurrencyId(Long currencyId) 
    {
        this.currencyId = currencyId;
    }

    public Long getCurrencyId() 
    {
        return currencyId;
    }
    public void setRechargeImg(String rechargeImg) 
    {
        this.rechargeImg = rechargeImg;
    }

    public String getRechargeImg() 
    {
        return rechargeImg;
    }
    public void setRechargeMsg(String rechargeMsg) 
    {
        this.rechargeMsg = rechargeMsg;
    }

    public String getRechargeMsg() 
    {
        return rechargeMsg;
    }
    public void setRechargeMethod(Integer rechargeMethod) 
    {
        this.rechargeMethod = rechargeMethod;
    }

    public Integer getRechargeMethod() 
    {
        return rechargeMethod;
    }
    public void setSqlVersion(Long sqlVersion) 
    {
        this.sqlVersion = sqlVersion;
    }

    public Long getSqlVersion() 
    {
        return sqlVersion;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("loanOrderId", getLoanOrderId())
            .append("userId", getUserId())
            .append("orderCode", getOrderCode())
            .append("orderAmount", getOrderAmount())
            .append("orderStatus", getOrderStatus())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("operatorName", getOperatorName())
            .append("payChannelName", getPayChannelName())
            .append("payChannelId", getPayChannelId())
            .append("currencyId", getCurrencyId())
            .append("rechargeImg", getRechargeImg())
            .append("rechargeMsg", getRechargeMsg())
            .append("rechargeMethod", getRechargeMethod())
            .append("remark", getRemark())
            .append("sqlVersion", getSqlVersion())
            .toString();
    }
}
