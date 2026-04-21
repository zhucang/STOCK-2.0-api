package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.StakingOrderInterestRecord;
import com.ruoyi.system.mapper.StakingOrderInterestRecordMapper;
import com.ruoyi.system.service.IStakingOrderInterestRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 质押订单派息记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-07-20
 */
@Service
public class StakingOrderInterestRecordServiceImpl implements IStakingOrderInterestRecordService 
{
    @Resource
    private StakingOrderInterestRecordMapper stakingOrderInterestRecordMapper;

    /**
     * 查询质押订单派息记录
     * 
     * @param id 质押订单派息记录主键
     * @return 质押订单派息记录
     */
    @Override
    public StakingOrderInterestRecord selectStakingOrderInterestRecordById(Long id)
    {
        return stakingOrderInterestRecordMapper.selectStakingOrderInterestRecordById(id);
    }

    /**
     * 查询质押订单派息记录列表
     * 
     * @param stakingOrderInterestRecord 质押订单派息记录
     * @return 质押订单派息记录
     */
    @Override
    public List<StakingOrderInterestRecord> selectStakingOrderInterestRecordList(StakingOrderInterestRecord stakingOrderInterestRecord)
    {
        return stakingOrderInterestRecordMapper.selectStakingOrderInterestRecordList(stakingOrderInterestRecord);
    }

    /**
     * 新增质押订单派息记录
     * 
     * @param stakingOrderInterestRecord 质押订单派息记录
     * @return 结果
     */
    @Override
    public int insertStakingOrderInterestRecord(StakingOrderInterestRecord stakingOrderInterestRecord)
    {
        return stakingOrderInterestRecordMapper.insertStakingOrderInterestRecord(stakingOrderInterestRecord);
    }

    /**
     * 修改质押订单派息记录
     * 
     * @param stakingOrderInterestRecord 质押订单派息记录
     * @return 结果
     */
    @Override
    public int updateStakingOrderInterestRecord(StakingOrderInterestRecord stakingOrderInterestRecord)
    {
        return stakingOrderInterestRecordMapper.updateStakingOrderInterestRecord(stakingOrderInterestRecord);
    }

    /**
     * 批量删除质押订单派息记录
     * 
     * @param ids 需要删除的质押订单派息记录主键
     * @return 结果
     */
    @Override
    public int deleteStakingOrderInterestRecordByIds(Long[] ids)
    {
        return stakingOrderInterestRecordMapper.deleteStakingOrderInterestRecordByIds(ids);
    }

    /**
     * 删除质押订单派息记录信息
     * 
     * @param id 质押订单派息记录主键
     * @return 结果
     */
    @Override
    public int deleteStakingOrderInterestRecordById(Long id)
    {
        return stakingOrderInterestRecordMapper.deleteStakingOrderInterestRecordById(id);
    }
}
