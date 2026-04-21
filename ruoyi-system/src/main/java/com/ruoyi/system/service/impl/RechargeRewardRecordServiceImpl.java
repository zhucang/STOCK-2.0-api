package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.RechargeRewardRecord;
import com.ruoyi.system.mapper.RechargeRewardRecordMapper;
import com.ruoyi.system.service.IRechargeRewardRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 充值奖励领取记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-01-06
 */
@Service
public class RechargeRewardRecordServiceImpl implements IRechargeRewardRecordService 
{
    @Resource
    private RechargeRewardRecordMapper rechargeRewardRecordMapper;

    /**
     * 查询充值奖励领取记录
     * 
     * @param rechargeRewardRecordId 充值奖励领取记录主键
     * @return 充值奖励领取记录
     */
    @Override
    public RechargeRewardRecord selectRechargeRewardRecordByRechargeRewardRecordId(Long rechargeRewardRecordId)
    {
        return rechargeRewardRecordMapper.selectRechargeRewardRecordByRechargeRewardRecordId(rechargeRewardRecordId);
    }

    /**
     * 查询充值奖励领取记录列表
     * 
     * @param rechargeRewardRecord 充值奖励领取记录
     * @return 充值奖励领取记录
     */
    @Override
    public List<RechargeRewardRecord> selectRechargeRewardRecordList(RechargeRewardRecord rechargeRewardRecord)
    {
        return rechargeRewardRecordMapper.selectRechargeRewardRecordList(rechargeRewardRecord);
    }

    /**
     * 新增充值奖励领取记录
     * 
     * @param rechargeRewardRecord 充值奖励领取记录
     * @return 结果
     */
    @Override
    public int insertRechargeRewardRecord(RechargeRewardRecord rechargeRewardRecord)
    {
        rechargeRewardRecord.setCreateTime(DateUtils.getNowDate());
        return rechargeRewardRecordMapper.insertRechargeRewardRecord(rechargeRewardRecord);
    }

    /**
     * 修改充值奖励领取记录
     * 
     * @param rechargeRewardRecord 充值奖励领取记录
     * @return 结果
     */
    @Override
    public int updateRechargeRewardRecord(RechargeRewardRecord rechargeRewardRecord)
    {
        rechargeRewardRecord.setUpdateTime(DateUtils.getNowDate());
        return rechargeRewardRecordMapper.updateRechargeRewardRecord(rechargeRewardRecord);
    }

    /**
     * 批量删除充值奖励领取记录
     * 
     * @param rechargeRewardRecordIds 需要删除的充值奖励领取记录主键
     * @return 结果
     */
    @Override
    public int deleteRechargeRewardRecordByRechargeRewardRecordIds(Long[] rechargeRewardRecordIds)
    {
        return rechargeRewardRecordMapper.deleteRechargeRewardRecordByRechargeRewardRecordIds(rechargeRewardRecordIds);
    }

    /**
     * 删除充值奖励领取记录信息
     * 
     * @param rechargeRewardRecordId 充值奖励领取记录主键
     * @return 结果
     */
    @Override
    public int deleteRechargeRewardRecordByRechargeRewardRecordId(Long rechargeRewardRecordId)
    {
        return rechargeRewardRecordMapper.deleteRechargeRewardRecordByRechargeRewardRecordId(rechargeRewardRecordId);
    }
}
