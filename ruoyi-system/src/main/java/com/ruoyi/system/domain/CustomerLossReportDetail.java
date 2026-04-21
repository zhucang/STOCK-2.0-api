package com.ruoyi.system.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ruoyi.system.domain.vo.BigDecimalSerializer2;

import java.math.BigDecimal;

public class CustomerLossReportDetail {

    /** 总充值 */
    @JsonSerialize(using = BigDecimalSerializer2.class)
    private BigDecimal rechargeAmount;

    /** 总充值笔数 */
    private Integer rechargeCount;

    /** 总在线支付充值 */
    @JsonSerialize(using = BigDecimalSerializer2.class)
    private BigDecimal onlineRechargeAmount;

    /** 总在线支付充值笔数 */
    private Integer onlineRechargeCount;

    /** 总提现 */
    @JsonSerialize(using = BigDecimalSerializer2.class)
    private BigDecimal withdrawAmount;

    /** 总提现笔数 */
    private Integer withdrawCount;

    /** 后台上分 */
    @JsonSerialize(using = BigDecimalSerializer2.class)
    private BigDecimal upPointAmount;

    /** 后台上分笔数 */
    private Integer upPointCount;

    /** 后台下分 */
    @JsonSerialize(using = BigDecimalSerializer2.class)
    private BigDecimal downPointAmount;

    /** 后台下分笔数 */
    private Integer downPointCount;

    /** 赠送彩金 */
    @JsonSerialize(using = BigDecimalSerializer2.class)
    private BigDecimal inWinningsAmount;

    /** 赠送彩金笔数 */
    private Integer inWinningsCount;

    /** 回收彩金 */
    @JsonSerialize(using = BigDecimalSerializer2.class)
    private BigDecimal outWinningsAmount;

    /** 回收彩金笔数 */
    private Integer outWinningsCount;

    /** 贷款金额 */
    @JsonSerialize(using = BigDecimalSerializer2.class)
    private BigDecimal loanAmount;

    /** 贷款笔数 */
    private Integer loanCount;

    /** 贷款金额免客损 */
    @JsonSerialize(using = BigDecimalSerializer2.class)
    private BigDecimal loanAmountNoStatistical;

    /** 贷款笔数免客损 */
    private Integer loanCountNoStatistical;

    /** 总客损 */
    @JsonSerialize(using = BigDecimalSerializer2.class)
    private BigDecimal customerLossAmount;

    /** 币种id */
    private Long currencyId;

    /** 货币名称 */
    private String currencyName;

    public BigDecimal getRechargeAmount() {
        return rechargeAmount;
    }

    public void setRechargeAmount(BigDecimal rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }

    public Integer getRechargeCount() {
        return rechargeCount;
    }

    public void setRechargeCount(Integer rechargeCount) {
        this.rechargeCount = rechargeCount;
    }

    public BigDecimal getOnlineRechargeAmount() {
        return onlineRechargeAmount;
    }

    public void setOnlineRechargeAmount(BigDecimal onlineRechargeAmount) {
        this.onlineRechargeAmount = onlineRechargeAmount;
    }

    public Integer getOnlineRechargeCount() {
        return onlineRechargeCount;
    }

    public void setOnlineRechargeCount(Integer onlineRechargeCount) {
        this.onlineRechargeCount = onlineRechargeCount;
    }

    public BigDecimal getWithdrawAmount() {
        return withdrawAmount;
    }

    public void setWithdrawAmount(BigDecimal withdrawAmount) {
        this.withdrawAmount = withdrawAmount;
    }

    public Integer getWithdrawCount() {
        return withdrawCount;
    }

    public void setWithdrawCount(Integer withdrawCount) {
        this.withdrawCount = withdrawCount;
    }

    public BigDecimal getUpPointAmount() {
        return upPointAmount;
    }

    public void setUpPointAmount(BigDecimal upPointAmount) {
        this.upPointAmount = upPointAmount;
    }

    public Integer getUpPointCount() {
        return upPointCount;
    }

    public void setUpPointCount(Integer upPointCount) {
        this.upPointCount = upPointCount;
    }

    public BigDecimal getDownPointAmount() {
        return downPointAmount;
    }

    public void setDownPointAmount(BigDecimal downPointAmount) {
        this.downPointAmount = downPointAmount;
    }

    public Integer getDownPointCount() {
        return downPointCount;
    }

    public void setDownPointCount(Integer downPointCount) {
        this.downPointCount = downPointCount;
    }

    public BigDecimal getInWinningsAmount() {
        return inWinningsAmount;
    }

    public void setInWinningsAmount(BigDecimal inWinningsAmount) {
        this.inWinningsAmount = inWinningsAmount;
    }

    public Integer getInWinningsCount() {
        return inWinningsCount;
    }

    public void setInWinningsCount(Integer inWinningsCount) {
        this.inWinningsCount = inWinningsCount;
    }

    public BigDecimal getOutWinningsAmount() {
        return outWinningsAmount;
    }

    public void setOutWinningsAmount(BigDecimal outWinningsAmount) {
        this.outWinningsAmount = outWinningsAmount;
    }

    public Integer getOutWinningsCount() {
        return outWinningsCount;
    }

    public void setOutWinningsCount(Integer outWinningsCount) {
        this.outWinningsCount = outWinningsCount;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Integer getLoanCount() {
        return loanCount;
    }

    public void setLoanCount(Integer loanCount) {
        this.loanCount = loanCount;
    }

    public BigDecimal getLoanAmountNoStatistical() {
        return loanAmountNoStatistical;
    }

    public void setLoanAmountNoStatistical(BigDecimal loanAmountNoStatistical) {
        this.loanAmountNoStatistical = loanAmountNoStatistical;
    }

    public Integer getLoanCountNoStatistical() {
        return loanCountNoStatistical;
    }

    public void setLoanCountNoStatistical(Integer loanCountNoStatistical) {
        this.loanCountNoStatistical = loanCountNoStatistical;
    }

    public BigDecimal getCustomerLossAmount() {
        return customerLossAmount;
    }

    public void setCustomerLossAmount(BigDecimal customerLossAmount) {
        this.customerLossAmount = customerLossAmount;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }
}
