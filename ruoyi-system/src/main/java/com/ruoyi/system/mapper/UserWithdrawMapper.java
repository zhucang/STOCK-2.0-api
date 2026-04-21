package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.system.domain.UserWithdraw;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 提现记录Mapper接口
 * 
 * @author ruoyi
 * @date 2023-10-30
 */
public interface UserWithdrawMapper 
{
    /**
     * 查询提现记录
     * 
     * @param id 提现记录主键
     * @return 提现记录
     */
    public UserWithdraw selectUserWithdrawById(Long id);

    /**
     * 查询提现记录列表
     * 
     * @param userWithdraw 提现记录
     * @return 提现记录集合
     */
    public List<UserWithdraw> selectUserWithdrawList(UserWithdraw userWithdraw);

    /**
     * 获取统计数据
     * @param userWithdraw
     * @return
     */
    public List<UserWithdraw> getStatisticalData(UserWithdraw userWithdraw);

    /**
     * 查询提现记录列表
     *
     * @param userWithdraw 提现记录
     * @return 提现记录集合
     */
    public List<UserWithdraw> selectUserWithdrawListWithOthers(UserWithdraw userWithdraw);

    /**
     * 新增提现记录
     * 
     * @param userWithdraw 提现记录
     * @return 结果
     */
    public int insertUserWithdraw(UserWithdraw userWithdraw);

    /**
     * 修改提现记录
     * 
     * @param userWithdraw 提现记录
     * @return 结果
     */
    public int updateUserWithdraw(UserWithdraw userWithdraw);

    /**
     * 删除提现记录
     * 
     * @param id 提现记录主键
     * @return 结果
     */
    public int deleteUserWithdrawById(Long id);

    /**
     * 批量删除提现记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserWithdrawByIds(Long[] ids);

    /**
     * 提现订单待审核数量
     * @param baseEntity
     * @return
     */
    List<Long> getUserWithdrawPendingReviewNum(BaseEntity baseEntity);

    /**
     * 提现订单待审核数量
     * @param userId
     * @return
     */
    int getUserWithdrawPendingReviewNumByUserId(Long userId);

    /**
     * 获取用户持仓单数量
     * @param userId
     * @return
     */
    int getUserHoldingPositionNumByUserId(Long userId);

    /**
     * 根据用户id查询用户今日提现次数
     * @param userId 用户id
     * @param date 时间
     * @param currencyId 币种id
     * @return
     */
    int getWithCountTodayByUserId(@Param("userId") Long userId, @Param("today") Date date, @Param("currencyId")Long currencyId);

    /**
     * 获取用户的各币种的提现金额
     * @param userId 用户id
     * @return
     */
    public List<UserWithdraw> selectUserWithdrawAmountAllCurrency(Long userId);


}
