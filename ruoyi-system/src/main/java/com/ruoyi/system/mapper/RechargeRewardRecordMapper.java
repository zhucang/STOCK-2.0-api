package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.RechargeRewardRecord;

import java.util.List;

/**
 * 充值奖励领取记录Mapper接口
 * 
 * @author ruoyi
 * @date 2026-01-06
 */
public interface RechargeRewardRecordMapper 
{
    /**
     * 查询充值奖励领取记录
     * 
     * @param rechargeRewardRecordId 充值奖励领取记录主键
     * @return 充值奖励领取记录
     */
    public RechargeRewardRecord selectRechargeRewardRecordByRechargeRewardRecordId(Long rechargeRewardRecordId);

    /**
     * 查询充值奖励领取记录列表
     * 
     * @param rechargeRewardRecord 充值奖励领取记录
     * @return 充值奖励领取记录集合
     */
    public List<RechargeRewardRecord> selectRechargeRewardRecordList(RechargeRewardRecord rechargeRewardRecord);

    /**
     * 新增充值奖励领取记录
     * 
     * @param rechargeRewardRecord 充值奖励领取记录
     * @return 结果
     */
    public int insertRechargeRewardRecord(RechargeRewardRecord rechargeRewardRecord);

    /**
     * 修改充值奖励领取记录
     * 
     * @param rechargeRewardRecord 充值奖励领取记录
     * @return 结果
     */
    public int updateRechargeRewardRecord(RechargeRewardRecord rechargeRewardRecord);

    /**
     * 删除充值奖励领取记录
     * 
     * @param rechargeRewardRecordId 充值奖励领取记录主键
     * @return 结果
     */
    public int deleteRechargeRewardRecordByRechargeRewardRecordId(Long rechargeRewardRecordId);

    /**
     * 批量删除充值奖励领取记录
     * 
     * @param rechargeRewardRecordIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRechargeRewardRecordByRechargeRewardRecordIds(Long[] rechargeRewardRecordIds);
}
