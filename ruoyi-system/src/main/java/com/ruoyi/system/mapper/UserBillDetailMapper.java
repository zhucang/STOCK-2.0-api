package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.UserBillDetail;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 用户流水记录Mapper接口
 * 
 * @author ruoyi
 * @date 2023-10-29
 */
public interface UserBillDetailMapper 
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
     * 删除用户流水记录
     * 
     * @param id 用户流水记录主键
     * @return 结果
     */
    public int deleteUserBillDetailById(Long id);

    /**
     * 批量删除用户流水记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserBillDetailByIds(Long[] ids);

    /**
     * 获取用户某币种的可回收彩金金额
     * @param userId 用户id
     * @param currencyId 币种id
     * @return
     */
    BigDecimal getTotalOutWinningsByUserId(@Param("userId") Long userId, @Param("currencyId")  Long currencyId);

    /**
     * 获取用户某币种的可解冻金额
     * @param userId 用户id
     * @param currencyId 币种id
     * @return
     */
    BigDecimal getAllowUnfreezeAmountByUserId(@Param("userId") Long userId, @Param("currencyId")  Long currencyId);

    /**
     * 获取用户存入未满24小时的灵活投资资金
     * @return
     */
    @MapKey("user_id")
    public Map<Long, Map<String, Object>> getUserTransferredLessThan24HoursFlexibleInvestmentFunds();
}
