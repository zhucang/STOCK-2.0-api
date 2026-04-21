package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.UserApplyPurchaseOrder;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户新股新币申购订单Service接口
 * 
 * @author ruoyi
 * @date 2023-11-30
 */
public interface IUserApplyPurchaseOrderService 
{
    /**
     * 查询用户新股新币申购订单
     * 
     * @param id 用户新股新币申购订单主键
     * @return 用户新股新币申购订单
     */
    public UserApplyPurchaseOrder selectUserApplyPurchaseOrderById(Long id);

    /**
     * 查询用户新股新币申购订单列表
     * 
     * @param userApplyPurchaseOrder 用户新股新币申购订单
     * @return 用户新股新币申购订单集合
     */
    public List<UserApplyPurchaseOrder> selectUserApplyPurchaseOrderList(UserApplyPurchaseOrder userApplyPurchaseOrder);

    /**
     * 新增用户新股新币申购订单
     * 
     * @param userApplyPurchaseOrder 用户新股新币申购订单
     * @return 结果
     */
    public int insertUserApplyPurchaseOrder(UserApplyPurchaseOrder userApplyPurchaseOrder);

    /**
     * 修改用户新股新币申购订单
     * 
     * @param userApplyPurchaseOrder 用户新股新币申购订单
     * @return 结果
     */
    public int updateUserApplyPurchaseOrder(UserApplyPurchaseOrder userApplyPurchaseOrder);

    /**
     * 批量删除用户新股新币申购订单
     * 
     * @param ids 需要删除的用户新股新币申购订单主键集合
     * @return 结果
     */
    public int deleteUserApplyPurchaseOrderByIds(Long[] ids);

    /**
     * 删除用户新股新币申购订单信息
     * 
     * @param id 用户新股新币申购订单主键
     * @return 结果
     */
    public int deleteUserApplyPurchaseOrderById(Long id);

    /**
     * 用户申购
     * @param userApplyPurchaseOrder
     * @return
     */
    public int addUserApplyPurchaseOrder(UserApplyPurchaseOrder userApplyPurchaseOrder);

    /**
     * 设置中签率
     * @param id 用户申购订单id
     * @param winningRate 中签率
     * @param userApplyPurchaseOrder 用户申购订单
     * @return
     */
    AjaxResult setWinningRate(Long id, BigDecimal winningRate, UserApplyPurchaseOrder userApplyPurchaseOrder);

    /**
     * 解锁用户申购订单锁仓
     * @param id 用户申购订单id
     * @return
     */
    public int unLockOrder(Long id,UserApplyPurchaseOrder userApplyPurchaseOrder);

    /**
     * 用户申购订单自动解锁定时任务
     */
    public void userApplyPurchaseOrderAutoUnLockTask();
}
