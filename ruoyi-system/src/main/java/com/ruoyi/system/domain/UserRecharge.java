package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.entity.UserInfoDetailVo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户充值订单对象 user_recharge
 * 
 * @author ruoyi
 * @date 2023-10-30
 */
public class UserRecharge extends UserInfoDetailVo
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
    private BigDecimal rechargeAmount;

    /** 状态：0：审核中 1:通过 2：驳回 */
    @Excel(name = "状态：0：审核中 1:通过 2：驳回 3：待审核")
    private Integer orderStatus;

    /** 支付时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "支付时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date payTime;

    /** 操作人名称 */
    @Excel(name = "操作人名称")
    private String operatorName;

    /** 充值通道名称 */
    @Excel(name = "充值通道名称")
    private String payChannelName;

    /** 充值通道名称多语言 */
    @Excel(name = "充值通道名称多语言")
    private LangMgr payChannelNameLang;

    /** 支付通道id */
    @Excel(name = "支付通道id")
    private Long payChannelId;

    /** 币种id */
    @Excel(name = "币种id")
    private Long currencyId;

    /** 币种名称 */
    private String currencyName;

    /** 支付凭证 */
    @Excel(name = "支付凭证")
    private String rechargeImg;

    /** 返回信息 */
    @Excel(name = "返回信息")
    private String rechargeMsg;

    /** 余额变更前 */
    @Excel(name = "余额变更前")
    private BigDecimal userAmountBefore;

    /** 余额变更后 */
    @Excel(name = "余额变更后")
    private BigDecimal userAmountAfter;

    /** 充值方式： 0：线下充值 1：在线支付 */
    @Excel(name = "充值方式： 0：线下充值 1：在线支付")
    private Integer rechargeMethod;

    public Integer getRechargeMethod() {
        return rechargeMethod;
    }

    public void setRechargeMethod(Integer rechargeMethod) {
        this.rechargeMethod = rechargeMethod;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
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

    public String getRechargeMsg() {
        return rechargeMsg;
    }

    public void setRechargeMsg(String rechargeMsg) {
        this.rechargeMsg = rechargeMsg;
    }

    public String getOrderCode()
    {
        return orderCode;
    }
    public void setRechargeAmount(BigDecimal rechargeAmount) 
    {
        this.rechargeAmount = rechargeAmount;
    }

    public BigDecimal getRechargeAmount() 
    {
        return rechargeAmount;
    }
    public void setOrderStatus(Integer orderStatus) 
    {
        this.orderStatus = orderStatus;
    }

    public Integer getOrderStatus()
    {
        return orderStatus;
    }
    public void setPayTime(Date payTime) 
    {
        this.payTime = payTime;
    }

    public Date getPayTime() 
    {
        return payTime;
    }
    public void setPayChannelName(String payChannelName) 
    {
        this.payChannelName = payChannelName;
    }

    public String getPayChannelName() 
    {
        return payChannelName;
    }

    public LangMgr getPayChannelNameLang() {
        return payChannelNameLang;
    }

    public void setPayChannelNameLang(LangMgr payChannelNameLang) {
        this.payChannelNameLang = payChannelNameLang;
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
            .append("rechargeAmount", getRechargeAmount())
            .append("orderStatus", getOrderStatus())
            .append("createTime", getCreateTime())
            .append("payTime", getPayTime())
            .append("payChannelName", getPayChannelName())
            .append("payChannelId", getPayChannelId())
            .append("currencyId", getCurrencyId())
            .append("rechargeImg", getRechargeImg())
            .append("userAmountBefore", getUserAmountBefore())
            .append("userAmountAfter", getUserAmountAfter())
            .toString();
    }
}
