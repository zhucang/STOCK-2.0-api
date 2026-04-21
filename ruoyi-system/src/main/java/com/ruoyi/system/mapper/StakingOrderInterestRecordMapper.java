package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.StakingOrderInterestRecord;

/**
 * 质押订单派息记录Mapper接口
 * 
 * @author ruoyi
 * @date 2025-07-20
 */
public interface StakingOrderInterestRecordMapper 
{
    /**
     * 查询质押订单派息记录
     * 
     * @param id 质押订单派息记录主键
     * @return 质押订单派息记录
     */
    public StakingOrderInterestRecord selectStakingOrderInterestRecordById(Long id);

    /**
     * 查询质押订单派息记录列表
     * 
     * @param stakingOrderInterestRecord 质押订单派息记录
     * @return 质押订单派息记录集合
     */
    public List<StakingOrderInterestRecord> selectStakingOrderInterestRecordList(StakingOrderInterestRecord stakingOrderInterestRecord);

    /**
     * 新增质押订单派息记录
     * 
     * @param stakingOrderInterestRecord 质押订单派息记录
     * @return 结果
     */
    public int insertStakingOrderInterestRecord(StakingOrderInterestRecord stakingOrderInterestRecord);

    /**
     * 修改质押订单派息记录
     * 
     * @param stakingOrderInterestRecord 质押订单派息记录
     * @return 结果
     */
    public int updateStakingOrderInterestRecord(StakingOrderInterestRecord stakingOrderInterestRecord);

    /**
     * 删除质押订单派息记录
     * 
     * @param id 质押订单派息记录主键
     * @return 结果
     */
    public int deleteStakingOrderInterestRecordById(Long id);

    /**
     * 批量删除质押订单派息记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStakingOrderInterestRecordByIds(Long[] ids);
}
