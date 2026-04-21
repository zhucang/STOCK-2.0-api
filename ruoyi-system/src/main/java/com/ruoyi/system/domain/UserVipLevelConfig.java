package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;


/**
 * 用户vip等级升级配置对象 user_vip_level_config
 * 
 * @author ruoyi
 * @date 2023-10-09
 */
public class UserVipLevelConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** vip等级 */
    @Excel(name = "vip等级")
    private Integer vipLevel;

    /** 最小充值金额 */
    @Excel(name = "最小充值金额")
    private BigDecimal rechargeAmountMin;

    /** 最大充值金额 */
    @Excel(name = "最大充值金额")
    private BigDecimal rechargeAmountMax;

    /** 充值赠送比率 */
    @Excel(name = "充值赠送比率")
    private BigDecimal rechargeBonusRatio;

    /** 充值赠送金额 */
    @Excel(name = "充值赠送金额")
    private BigDecimal rechargeBonusAmount;

    /** 充值赠送方式 0：按比例赠送 1：按固定金额赠送 */
    @Excel(name = "充值赠送方式 0：按比例赠送 1：按固定金额赠送")
    private Integer bonusMethod;

    /** 在N天内所有品种允许交易总单数M(N) */
    @Excel(name = "在N天内所有品种允许交易总单数M(N)")
    private Integer day;

    /** 在N天内所有品种允许交易总单数M(M) */
    @Excel(name = "在N天内所有品种允许交易总单数M(M)")
    private Integer orderNum;

    /** 状态： 0：启用 1：禁用 */
    @Excel(name = "状态： 0：启用 1：禁用")
    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setVipLevel(Integer vipLevel)
    {
        this.vipLevel = vipLevel;
    }

    public Integer getVipLevel()
    {
        return vipLevel;
    }
    public void setRechargeAmountMin(BigDecimal rechargeAmountMin) 
    {
        this.rechargeAmountMin = rechargeAmountMin;
    }

    public BigDecimal getRechargeAmountMin() 
    {
        return rechargeAmountMin;
    }
    public void setRechargeAmountMax(BigDecimal rechargeAmountMax) 
    {
        this.rechargeAmountMax = rechargeAmountMax;
    }

    public BigDecimal getRechargeAmountMax() 
    {
        return rechargeAmountMax;
    }
    public void setRechargeBonusRatio(BigDecimal rechargeBonusRatio) 
    {
        this.rechargeBonusRatio = rechargeBonusRatio;
    }

    public BigDecimal getRechargeBonusRatio() 
    {
        return rechargeBonusRatio;
    }
    public void setDay(Integer day)
    {
        this.day = day;
    }

    public BigDecimal getRechargeBonusAmount() {
        return rechargeBonusAmount;
    }

    public void setRechargeBonusAmount(BigDecimal rechargeBonusAmount) {
        this.rechargeBonusAmount = rechargeBonusAmount;
    }

    public Integer getBonusMethod() {
        return bonusMethod;
    }

    public void setBonusMethod(Integer bonusMethod) {
        this.bonusMethod = bonusMethod;
    }

    public Integer getDay()
    {
        return day;
    }
    public void setOrderNum(Integer orderNum)
    {
        this.orderNum = orderNum;
    }

    public Integer getOrderNum()
    {
        return orderNum;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("vipLevel", getVipLevel())
            .append("rechargeAmountMin", getRechargeAmountMin())
            .append("rechargeAmountMax", getRechargeAmountMax())
            .append("rechargeBonusRatio", getRechargeBonusRatio())
            .append("day", getDay())
            .append("orderNum", getOrderNum())
            .toString();
    }
}
