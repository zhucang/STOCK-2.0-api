package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 贷款订单利息生成记录对象 loan_order_interest_record
 * 
 * @author ruoyi
 * @date 2024-05-23
 */
public class LoanOrderInterestRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 贷款订单id */
    private Long loanOrderId;

    /** 订单金额 */
    @Excel(name = "订单金额")
    private BigDecimal orderPrice;

    /** 是否结算 0：是 1：否 */
    @Excel(name = "是否结算 0：是 1：否")
    private Integer alreadySettleFlag;

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
    public void setOrderPrice(BigDecimal orderPrice) 
    {
        this.orderPrice = orderPrice;
    }

    public BigDecimal getOrderPrice() 
    {
        return orderPrice;
    }
    public void setAlreadySettleFlag(Integer alreadySettleFlag) 
    {
        this.alreadySettleFlag = alreadySettleFlag;
    }

    public Integer getAlreadySettleFlag() 
    {
        return alreadySettleFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("loanOrderId", getLoanOrderId())
            .append("orderPrice", getOrderPrice())
            .append("createTime", getCreateTime())
            .append("alreadySettleFlag", getAlreadySettleFlag())
            .toString();
    }
}
