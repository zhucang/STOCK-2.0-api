package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 充值奖励领取记录对象 recharge_reward_record
 * 
 * @author ruoyi
 * @date 2026-01-06
 */
public class RechargeRewardRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 充值奖励领取记录 */
    private Long rechargeRewardRecordId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 充值订单ID */
    @Excel(name = "充值订单ID")
    private Long userRechargeId;

    /** 充值奖励层级ID */
    @Excel(name = "充值奖励层级ID")
    private Long rewardTierId;

    /** 充值金额 */
    @Excel(name = "充值金额")
    private BigDecimal rechargeAmount;

    /** 奖励比例（%） */
    @Excel(name = "奖励比例", readConverterExp = "%=")
    private BigDecimal rewardRate;

    /** 奖励金额 */
    @Excel(name = "奖励金额")
    private BigDecimal rewardAmount;

    public void setRechargeRewardRecordId(Long rechargeRewardRecordId) 
    {
        this.rechargeRewardRecordId = rechargeRewardRecordId;
    }

    public Long getRechargeRewardRecordId() 
    {
        return rechargeRewardRecordId;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setUserRechargeId(Long userRechargeId) 
    {
        this.userRechargeId = userRechargeId;
    }

    public Long getUserRechargeId() 
    {
        return userRechargeId;
    }

    public void setRewardTierId(Long rewardTierId) 
    {
        this.rewardTierId = rewardTierId;
    }

    public Long getRewardTierId() 
    {
        return rewardTierId;
    }

    public void setRechargeAmount(BigDecimal rechargeAmount) 
    {
        this.rechargeAmount = rechargeAmount;
    }

    public BigDecimal getRechargeAmount() 
    {
        return rechargeAmount;
    }

    public void setRewardRate(BigDecimal rewardRate) 
    {
        this.rewardRate = rewardRate;
    }

    public BigDecimal getRewardRate() 
    {
        return rewardRate;
    }

    public void setRewardAmount(BigDecimal rewardAmount) 
    {
        this.rewardAmount = rewardAmount;
    }

    public BigDecimal getRewardAmount() 
    {
        return rewardAmount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("rechargeRewardRecordId", getRechargeRewardRecordId())
            .append("userId", getUserId())
            .append("userRechargeId", getUserRechargeId())
            .append("rewardTierId", getRewardTierId())
            .append("rechargeAmount", getRechargeAmount())
            .append("rewardRate", getRewardRate())
            .append("rewardAmount", getRewardAmount())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
