package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.entity.UserInfoDetailVo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 提现记录对象 user_withdraw
 * 
 * @author ruoyi
 * @date 2023-10-30
 */
public class UserWithdraw extends UserInfoDetailVo
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

    /** 提现金额 */
    @Excel(name = "提现金额")
    private BigDecimal withdrawAmount;

    /** 到账金额 */
    @Excel(name = "到账金额")
    private BigDecimal receivedAmount;

    /** 申请提现时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "申请提现时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date applyTime;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date transTime;

    /** 操作人名称 */
    @Excel(name = "操作人名称")
    private String operatorName;

    /** 收款账户信息 */
    @Excel(name = "收款账户信息")
    private String receiptAccountInfo;


    /** 钱包收款码 */
    @Excel(name = "钱包收款码")
    private String walletReceiptQrCode;

    /** 提现状态：0：审核中 1：审核通过 2：审核驳回 3：取消提现 */
    @Excel(name = "提现状态：0：审核中 1：审核通过 2：审核驳回 3：取消提现")
    private Integer withdrawStatus;

    /** 提现手续费 */
    @Excel(name = "提现手续费")
    private BigDecimal withdrawFee;

    /** 返回信息 */
    @Excel(name = "返回信息")
    private String withdrawMsg;

    /** 币种id */
    @Excel(name = "币种id")
    private Long currencyId;

    /** 币种名称 */
    @Excel(name = "币种名称")
    private String currencyName;

    /** 提现类型 0：银行卡 1：虚拟钱包 */
    @Excel(name = "提现类型 0：银行卡 1：虚拟钱包")
    private Integer withdrawType;

    /** 余额变更前 */
    @Excel(name = "余额变更前")
    private BigDecimal userAmtBefore;

    /** 余额变更后 */
    @Excel(name = "余额变更后")
    private BigDecimal userAmtAfter;


    /** 是否统计报表 0：是 1：否 */
    @Excel(name = "是否统计报表 0：是 1：否")
    private Integer statisticalReport;


    public Integer getStatisticalReport() {
        return statisticalReport;
    }

    public void setStatisticalReport(Integer statisticalReport) {
        this.statisticalReport = statisticalReport;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getWalletReceiptQrCode() {
        return walletReceiptQrCode;
    }

    public void setWalletReceiptQrCode(String walletReceiptQrCode) {
        this.walletReceiptQrCode = walletReceiptQrCode;
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

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public void setWithdrawAmount(BigDecimal withdrawAmount)
    {
        this.withdrawAmount = withdrawAmount;
    }

    public BigDecimal getReceivedAmount() {
        return receivedAmount;
    }

    public void setReceivedAmount(BigDecimal receivedAmount) {
        this.receivedAmount = receivedAmount;
    }

    public BigDecimal getWithdrawAmount()
    {
        return withdrawAmount;
    }
    public void setApplyTime(Date applyTime) 
    {
        this.applyTime = applyTime;
    }

    public Date getApplyTime() 
    {
        return applyTime;
    }
    public void setTransTime(Date transTime) 
    {
        this.transTime = transTime;
    }

    public Date getTransTime() 
    {
        return transTime;
    }
    public void setReceiptAccountInfo(String receiptAccountInfo) 
    {
        this.receiptAccountInfo = receiptAccountInfo;
    }

    public String getReceiptAccountInfo() 
    {
        return receiptAccountInfo;
    }
    public void setWithdrawStatus(Integer withdrawStatus) 
    {
        this.withdrawStatus = withdrawStatus;
    }

    public Integer getWithdrawStatus() 
    {
        return withdrawStatus;
    }
    public void setWithdrawFee(BigDecimal withdrawFee) 
    {
        this.withdrawFee = withdrawFee;
    }

    public BigDecimal getWithdrawFee() 
    {
        return withdrawFee;
    }
    public void setWithdrawMsg(String withdrawMsg) 
    {
        this.withdrawMsg = withdrawMsg;
    }

    public String getWithdrawMsg() 
    {
        return withdrawMsg;
    }
    public void setCurrencyId(Long currencyId) 
    {
        this.currencyId = currencyId;
    }

    public Long getCurrencyId() 
    {
        return currencyId;
    }
    public void setWithdrawType(Integer withdrawType) 
    {
        this.withdrawType = withdrawType;
    }

    public Integer getWithdrawType() 
    {
        return withdrawType;
    }
    public void setUserAmtBefore(BigDecimal userAmtBefore) 
    {
        this.userAmtBefore = userAmtBefore;
    }

    public BigDecimal getUserAmtBefore() 
    {
        return userAmtBefore;
    }
    public void setUserAmtAfter(BigDecimal userAmtAfter) 
    {
        this.userAmtAfter = userAmtAfter;
    }

    public BigDecimal getUserAmtAfter() 
    {
        return userAmtAfter;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("withdrawAmount", getWithdrawAmount())
            .append("applyTime", getApplyTime())
            .append("transTime", getTransTime())
            .append("receiptAccountInfo", getReceiptAccountInfo())
            .append("withdrawStatus", getWithdrawStatus())
            .append("withdrawFee", getWithdrawFee())
            .append("withdrawMsg", getWithdrawMsg())
            .append("currencyId", getCurrencyId())
            .append("withdrawType", getWithdrawType())
            .append("userAmtBefore", getUserAmtBefore())
            .append("userAmtAfter", getUserAmtAfter())
            .toString();
    }
}
