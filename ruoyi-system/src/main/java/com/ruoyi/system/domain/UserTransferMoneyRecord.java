package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 用户转账记录对象 user_transfer_money_record
 * 
 * @author ruoyi
 * @date 2025-05-14
 */
public class UserTransferMoneyRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 用户转账记录ID */
    private Long userTransferMoneyRecordId;

    /** 转出者ID */
    @Excel(name = "转账用户ID")
    private Long userIdFrom;

    /** 转出者账号 */
    @Excel(name = "转账用户账号")
    private String userAccountFrom;

    /** 转入者ID */
    @Excel(name = "收款用户ID")
    private Long userIdTo;

    /** 转入者账号 */
    @Excel(name = "收款用户账号")
    private String userAccountTo;

    /** 转账金额 */
    @Excel(name = "转账金额")
    private BigDecimal transferAmount;

    /** 币种ID */
    @Excel(name = "币种ID")
    private Long currencyId;

    /** 币种名称 */
    @Excel(name = "币种名称")
    private String currencyName;

    public void setUserTransferMoneyRecordId(Long userTransferMoneyRecordId) 
    {
        this.userTransferMoneyRecordId = userTransferMoneyRecordId;
    }

    public Long getUserTransferMoneyRecordId() 
    {
        return userTransferMoneyRecordId;
    }
    public void setUserIdFrom(Long userIdFrom) 
    {
        this.userIdFrom = userIdFrom;
    }

    public String getUserAccountFrom() {
        return userAccountFrom;
    }

    public void setUserAccountFrom(String userAccountFrom) {
        this.userAccountFrom = userAccountFrom;
    }

    public Long getUserIdFrom()
    {
        return userIdFrom;
    }
    public void setUserIdTo(Long userIdTo) 
    {
        this.userIdTo = userIdTo;
    }

    public Long getUserIdTo() 
    {
        return userIdTo;
    }

    public String getUserAccountTo() {
        return userAccountTo;
    }

    public void setUserAccountTo(String userAccountTo) {
        this.userAccountTo = userAccountTo;
    }

    public void setTransferAmount(BigDecimal transferAmount)
    {
        this.transferAmount = transferAmount;
    }

    public BigDecimal getTransferAmount() 
    {
        return transferAmount;
    }
    public void setCurrencyId(Long currencyId) 
    {
        this.currencyId = currencyId;
    }

    public Long getCurrencyId() 
    {
        return currencyId;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userTransferMoneyRecordId", getUserTransferMoneyRecordId())
            .append("userIdFrom", getUserIdFrom())
            .append("userIdTo", getUserIdTo())
            .append("transferAmount", getTransferAmount())
            .append("currencyId", getCurrencyId())
            .append("createTime", getCreateTime())
            .append("remark", getRemark())
            .toString();
    }
}
