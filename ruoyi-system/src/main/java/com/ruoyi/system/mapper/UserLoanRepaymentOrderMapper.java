package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.system.domain.UserLoanRepaymentOrder;

import java.util.List;

/**
 * 用户贷款还款订单Mapper接口
 * 
 * @author ruoyi
 * @date 2024-05-28
 */
public interface UserLoanRepaymentOrderMapper 
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
     * 删除用户贷款还款订单
     * 
     * @param id 用户贷款还款订单主键
     * @return 结果
     */
    public int deleteUserLoanRepaymentOrderById(Long id);

    /**
     * 批量删除用户贷款还款订单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserLoanRepaymentOrderByIds(Long[] ids);

    /**
     * 贷款还款订单待审核数量
     * @param baseEntity
     * @return
     */
    List<Long> getUserLoanRepaymentPendingReviewNum(BaseEntity baseEntity);
}
