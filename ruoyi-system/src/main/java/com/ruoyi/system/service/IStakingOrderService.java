package com.ruoyi.system.service;

import com.ruoyi.system.domain.StakingOrder;

import java.math.BigDecimal;
import java.util.List;

/**
 * 质押订单Service接口
 * 
 * @author ruoyi
 * @date 2025-07-17
 */
public interface IStakingOrderService 
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
     * 批量删除质押订单
     * 
     * @param ids 需要删除的质押订单主键集合
     * @return 结果
     */
    public int deleteStakingOrderByIds(Long[] ids);

    /**
     * 删除质押订单信息
     * 
     * @param id 质押订单主键
     * @return 结果
     */
    public int deleteStakingOrderById(Long id);

    /**
     * 质押订单审核
     * @param stakingOrderId 质押订单id
     * @param orderStatus 订单状态 1：通过 3：驳回
     * @return
     */
    public int updateStakingOrderStatus(Long stakingOrderId, Integer orderStatus);

    /**
     * 用户质押代币
     * @param stakingProductId 质押产品配置ID
     * @param buyPrice 质押金额
     * @return
     */
    public int addStakingOrderOrder(Long stakingProductId, BigDecimal buyPrice);

    /**
     * 用户赎回质押金
     * @param stakingOrderId 质押订单ID
     * @return
     */
    public int redemption(Long stakingOrderId);

    /**
     * 押金派息定时任务
     */
    void payInterestTask();

    /**
     * 质押派息定时任务
     */
    void doPayInterestTask(StakingOrder stakingOrder);
}
