package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.UserApplyPurchaseOrder;

import java.util.List;

/**
 * 用户新股新币申购订单Mapper接口
 * 
 * @author ruoyi
 * @date 2023-11-30
 */
public interface UserApplyPurchaseOrderMapper 
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
     * 删除用户新股新币申购订单
     * 
     * @param id 用户新股新币申购订单主键
     * @return 结果
     */
    public int deleteUserApplyPurchaseOrderById(Long id);

    /**
     * 批量删除用户新股新币申购订单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserApplyPurchaseOrderByIds(Long[] ids);
}
