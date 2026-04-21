package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 充值奖励层级配置对象 recharge_reward_tier
 * 
 * @author ruoyi
 * @date 2026-01-06
 */
public class RechargeRewardTier extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 充值奖励层级配置ID */
    private Long rechargeRewardTierId;

    /** 档位金额范围最小值 */
    @Excel(name = "档位金额范围最小值")
    private BigDecimal tierAmountMin;

    /** 档位金额范围最大值 */
    @Excel(name = "档位金额范围最大值")
    private BigDecimal tierAmountMax;

    /** 奖励百分比（%） */
    @Excel(name = "奖励百分比", readConverterExp = "%=")
    private BigDecimal rewardRate;

    /** 状态 0：禁用 1：启用 */
    @Excel(name = "状态 0：禁用 1：启用")
    private Long status;

    /** 币种ID */
    @Excel(name = "币种ID")
    private Long currencyId;

    /** 删除标志 0：未删除 1：已删除 */
    private Long delFlag;

    public void setRechargeRewardTierId(Long rechargeRewardTierId) 
    {
        this.rechargeRewardTierId = rechargeRewardTierId;
    }

    public Long getRechargeRewardTierId() 
    {
        return rechargeRewardTierId;
    }

    public BigDecimal getTierAmountMin() {
        return tierAmountMin;
    }

    public void setTierAmountMin(BigDecimal tierAmountMin) {
        this.tierAmountMin = tierAmountMin;
    }

    public BigDecimal getTierAmountMax() {
        return tierAmountMax;
    }

    public void setTierAmountMax(BigDecimal tierAmountMax) {
        this.tierAmountMax = tierAmountMax;
    }

    public void setRewardRate(BigDecimal rewardRate)
    {
        this.rewardRate = rewardRate;
    }

    public BigDecimal getRewardRate() 
    {
        return rewardRate;
    }

    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public void setDelFlag(Long delFlag)
    {
        this.delFlag = delFlag;
    }

    public Long getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("rechargeRewardTierId", getRechargeRewardTierId())
            .append("tierAmount", getTierAmountMin())
            .append("rewardRate", getRewardRate())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
