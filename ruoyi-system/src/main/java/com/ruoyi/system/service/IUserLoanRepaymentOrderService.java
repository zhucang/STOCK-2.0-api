package com.ruoyi.system.service;

import com.ruoyi.system.domain.UserLoanRepaymentOrder;

import java.util.List;

/**
 * 用户贷款还款订单Service接口
 * 
 * @author ruoyi
 * @date 2024-05-28
 */
public interface IUserLoanRepaymentOrderService 
{
    /**
     * 查询用户贷款还款订单
     * 
     * @param id 用户贷款还款订单主键
     * @return 用户贷款还款订单
     */
    public UserLoanRepaymentOrder selectUserLoanRepaymentOrderById(Long id);

    /**
     * 查询用户贷款还款订单列表
     * 
     * @param userLoanRepaymentOrder 用户贷款还款订单
     * @return 用户贷款还款订单集合
     */
    public List<UserLoanRepaymentOrder> selectUserLoanRepaymentOrderList(UserLoanRepaymentOrder userLoanRepaymentOrder);

    /**
     * 获取统计数据
     * @param userLoanRepaymentOrder
     * @return
     */
    public List<UserLoanRepaymentOrder> getStatisticalData(UserLoanRepaymentOrder userLoanRepaymentOrder);

    /**
     * 填充其他信息
     * @param userLoanRepaymentOrders 用户贷款还款订单
     */
    public void fillOtherInfo(List<UserLoanRepaymentOrder> userLoanRepaymentOrders);

    /**
     * 新增用户贷款还款订单
     * 
     * @param userLoanRepaymentOrder 用户贷款还款订单
     * @return 结果
     */
    public int insertUserLoanRepaymentOrder(UserLoanRepaymentOrder userLoanRepaymentOrder);

    /**
     * 修改用户贷款还款订单
     * 
     * @param userLoanRepaymentOrder 用户贷款还款订单
     * @return 结果
     */
    public int updateUserLoanRepaymentOrder(UserLoanRepaymentOrder userLoanRepaymentOrder);

    /**
     * 批量删除用户贷款还款订单
     * 
     * @param ids 需要删除的用户贷款还款订单主键集合
     * @return 结果
     */
    public int deleteUserLoanRepaymentOrderByIds(Long[] ids);

    /**
     * 删除用户贷款还款订单信息
     * 
     * @param id 用户贷款还款订单主键
     * @return 结果
     */
    public int deleteUserLoanRepaymentOrderById(Long id);

    /**
     * 贷款还款订单审核
     * @param loanRepaymentOrderId 贷款还款订单id
     * @param orderStatus 订单状态
     * @param message 返回信息
     * @param remark 备注
     * @return
     */
    public int updateLoanRepaymentOrderStatus(Long loanRepaymentOrderId,Integer orderStatus,String message,String remark);


    /**
     * 用户贷款还款
     * @param userLoanRepaymentOrder
     * @return
     */
    public int userLoanRepayment(UserLoanRepaymentOrder userLoanRepaymentOrder);
}
