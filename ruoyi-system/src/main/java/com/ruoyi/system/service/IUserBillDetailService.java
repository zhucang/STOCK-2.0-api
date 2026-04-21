package com.ruoyi.system.service;

import java.math.BigDecimal;
import java.util.List;
import com.ruoyi.system.domain.UserBillDetail;
import org.apache.ibatis.annotations.Param;

/**
 * 用户流水记录Service接口
 * 
 * @author ruoyi
 * @date 2023-10-29
 */
public interface IUserBillDetailService 
{
    /**
     * 查询用户流水记录
     * 
     * @param id 用户流水记录主键
     * @return 用户流水记录
     */
    public UserBillDetail selectUserBillDetailById(Long id);

    /**
     * 查询用户流水记录列表
     * 
     * @param userBillDetail 用户流水记录
     * @return 用户流水记录集合
     */
    public List<UserBillDetail> selectUserBillDetailList(UserBillDetail userBillDetail);

    /**
     * 新增用户流水记录
     * 
     * @param userBillDetail 用户流水记录
     * @return 结果
     */
    public int insertUserBillDetail(UserBillDetail userBillDetail);

    /**
     * 修改用户流水记录
     * 
     * @param userBillDetail 用户流水记录
     * @return 结果
     */
    public int updateUserBillDetail(UserBillDetail userBillDetail);

    /**
     * 批量删除用户流水记录
     * 
     * @param ids 需要删除的用户流水记录主键集合
     * @return 结果
     */
    public int deleteUserBillDetailByIds(Long[] ids);

    /**
     * 删除用户流水记录信息
     * 
     * @param id 用户流水记录主键
     * @return 结果
     */
    public int deleteUserBillDetailById(Long id);

    /**
     * 获取用户某币种的可回收彩金金额
     * @param userId 用户id
     * @param currencyId 币种id
     * @return
     */
    BigDecimal getTotalOutWinningsByUserId(Long userId,Long currencyId);

    /**
     * 获取用户某币种的可解冻金额
     * @param userId 用户id
     * @param currencyId 币种id
     * @return
     */
    BigDecimal getAllowUnfreezeAmountByUserId(Long userId,Long currencyId);
}
