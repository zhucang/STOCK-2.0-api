package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.system.domain.StakingOrder;

import java.util.List;

/**
 * 质押订单Mapper接口
 * 
 * @author ruoyi
 * @date 2025-07-17
 */
public interface StakingOrderMapper 
{
    /**
     * 查询质押订单
     * 
     * @param id 质押订单主键
     * @return 质押订单
     */
    public StakingOrder selectStakingOrderById(Long id);

    /**
     * 查询质押订单列表
     * 
     * @param stakingOrder 质押订单
     * @return 质押订单集合
     */
    public List<StakingOrder> selectStakingOrderList(StakingOrder stakingOrder);

    /**
     * 新增质押订单
     * 
     * @param stakingOrder 质押订单
     * @return 结果
     */
    public int insertStakingOrder(StakingOrder stakingOrder);

    /**
     * 修改质押订单
     * 
     * @param stakingOrder 质押订单
     * @return 结果
     */
    public int updateStakingOrder(StakingOrder stakingOrder);

    /**
     * 删除质押订单
     * 
     * @param id 质押订单主键
     * @return 结果
     */
    public int deleteStakingOrderById(Long id);

    /**
     * 批量删除质押订单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStakingOrderByIds(Long[] ids);

    /**
     * 质押订单待审核数量
     * @param baseEntity
     * @return
     */
    List<Long> getUserStakingPendingReviewNum(BaseEntity baseEntity);
}
