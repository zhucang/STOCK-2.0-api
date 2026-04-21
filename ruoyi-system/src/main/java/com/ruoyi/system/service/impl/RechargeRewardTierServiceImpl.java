package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.RechargeRewardTier;
import com.ruoyi.system.mapper.RechargeRewardTierMapper;
import com.ruoyi.system.service.IRechargeRewardTierService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 充值奖励层级配置Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-01-06
 */
@Service
public class RechargeRewardTierServiceImpl implements IRechargeRewardTierService 
{
    @Resource
    private RechargeRewardTierMapper rechargeRewardTierMapper;

    /**
     * 查询充值奖励层级配置
     * 
     * @param rechargeRewardTierId 充值奖励层级配置主键
     * @return 充值奖励层级配置
     */
    @Override
    public RechargeRewardTier selectRechargeRewardTierByRechargeRewardTierId(Long rechargeRewardTierId)
    {
        return rechargeRewardTierMapper.selectRechargeRewardTierByRechargeRewardTierId(rechargeRewardTierId);
    }

    /**
     * 查询充值奖励层级配置列表
     * 
     * @param rechargeRewardTier 充值奖励层级配置
     * @return 充值奖励层级配置
     */
    @Override
    public List<RechargeRewardTier> selectRechargeRewardTierList(RechargeRewardTier rechargeRewardTier)
    {
        return rechargeRewardTierMapper.selectRechargeRewardTierList(rechargeRewardTier);
    }

    /**
     * 新增充值奖励层级配置
     * 
     * @param rechargeRewardTier 充值奖励层级配置
     * @return 结果
     */
    @Override
    public int insertRechargeRewardTier(RechargeRewardTier rechargeRewardTier)
    {
        rechargeRewardTier.setCreateTime(DateUtils.getNowDate());
        return rechargeRewardTierMapper.insertRechargeRewardTier(rechargeRewardTier);
    }

    /**
     * 修改充值奖励层级配置
     * 
     * @param rechargeRewardTier 充值奖励层级配置
     * @return 结果
     */
    @Override
    public int updateRechargeRewardTier(RechargeRewardTier rechargeRewardTier)
    {
        rechargeRewardTier.setUpdateTime(DateUtils.getNowDate());
        return rechargeRewardTierMapper.updateRechargeRewardTier(rechargeRewardTier);
    }

    /**
     * 批量删除充值奖励层级配置
     * 
     * @param rechargeRewardTierIds 需要删除的充值奖励层级配置主键
     * @return 结果
     */
    @Override
    public int deleteRechargeRewardTierByRechargeRewardTierIds(Long[] rechargeRewardTierIds)
    {
        return rechargeRewardTierMapper.deleteRechargeRewardTierByRechargeRewardTierIds(rechargeRewardTierIds);
    }

    /**
     * 删除充值奖励层级配置信息
     * 
     * @param rechargeRewardTierId 充值奖励层级配置主键
     * @return 结果
     */
    @Override
    public int deleteRechargeRewardTierByRechargeRewardTierId(Long rechargeRewardTierId)
    {
        return rechargeRewardTierMapper.deleteRechargeRewardTierByRechargeRewardTierId(rechargeRewardTierId);
    }
}
