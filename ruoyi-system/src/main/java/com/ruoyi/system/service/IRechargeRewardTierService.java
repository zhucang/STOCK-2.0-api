package com.ruoyi.system.service;

import com.ruoyi.system.domain.RechargeRewardTier;

import java.util.List;

/**
 * 充值奖励层级配置Service接口
 * 
 * @author ruoyi
 * @date 2026-01-06
 */
public interface IRechargeRewardTierService 
{
    /**
     * 查询充值奖励层级配置
     * 
     * @param rechargeRewardTierId 充值奖励层级配置主键
     * @return 充值奖励层级配置
     */
    public RechargeRewardTier selectRechargeRewardTierByRechargeRewardTierId(Long rechargeRewardTierId);

    /**
     * 查询充值奖励层级配置列表
     * 
     * @param rechargeRewardTier 充值奖励层级配置
     * @return 充值奖励层级配置集合
     */
    public List<RechargeRewardTier> selectRechargeRewardTierList(RechargeRewardTier rechargeRewardTier);

    /**
     * 新增充值奖励层级配置
     * 
     * @param rechargeRewardTier 充值奖励层级配置
     * @return 结果
     */
    public int insertRechargeRewardTier(RechargeRewardTier rechargeRewardTier);

    /**
     * 修改充值奖励层级配置
     * 
     * @param rechargeRewardTier 充值奖励层级配置
     * @return 结果
     */
    public int updateRechargeRewardTier(RechargeRewardTier rechargeRewardTier);

    /**
     * 批量删除充值奖励层级配置
     * 
     * @param rechargeRewardTierIds 需要删除的充值奖励层级配置主键集合
     * @return 结果
     */
    public int deleteRechargeRewardTierByRechargeRewardTierIds(Long[] rechargeRewardTierIds);

    /**
     * 删除充值奖励层级配置信息
     * 
     * @param rechargeRewardTierId 充值奖励层级配置主键
     * @return 结果
     */
    public int deleteRechargeRewardTierByRechargeRewardTierId(Long rechargeRewardTierId);
}
