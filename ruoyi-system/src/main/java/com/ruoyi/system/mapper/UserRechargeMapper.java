package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.system.domain.UserRecharge;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 用户充值订单Mapper接口
 * 
 * @author ruoyi
 * @date 2023-10-30
 */
public interface UserRechargeMapper 
{
    /**
     * 查询用户充值订单
     * 
     * @param id 用户充值订单主键
     * @return 用户充值订单
     */
    public UserRecharge selectUserRechargeById(Long id);

    /**
     * 查询用户充值订单列表
     * 
     * @param userRecharge 用户充值订单
     * @return 用户充值订单集合
     */
    public List<UserRecharge> selectUserRechargeList(UserRecharge userRecharge);

    /**
     * 获取统计数据
     * @param userRecharge
     * @return
     */
    public List<UserRecharge> getStatisticalData(UserRecharge userRecharge);

    /**
     * 查询用户充值订单列表（含彩金赠送记录、上分记录)
     *
     * @param userRecharge 用户充值订单
     * @return 用户充值订单集合
     */
    public List<UserRecharge> selectUserRechargeListWithOthers(UserRecharge userRecharge);

    /**
     * 新增用户充值订单
     * 
     * @param userRecharge 用户充值订单
     * @return 结果
     */
    public int insertUserRecharge(UserRecharge userRecharge);

    /**
     * 修改用户充值订单
     * 
     * @param userRecharge 用户充值订单
     * @return 结果
     */
    public int updateUserRecharge(UserRecharge userRecharge);

    /**
     * 删除用户充值订单
     * 
     * @param id 用户充值订单主键
     * @return 结果
     */
    public int deleteUserRechargeById(Long id);

    /**
     * 批量删除用户充值订单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserRechargeByIds(Long[] ids);

    /**
     * 获取用户的各币种的充值金额
     * @param userIds 用户ids
     * @return
     */
    List<UserRecharge> selectUserRechargeAmountAllCurrency(@Param("userIds") List<Long> userIds,
                                                           @Param("startTime") Date startTime,
                                                           @Param("endTime") Date endTime);

    /**
     * 获取用户的各币种的充值金额
     * @param userId 用户id
     * @return
     */
    List<UserRecharge> selectUserRechargeAmountAllCurrencyByUserId(Long userId);

    /**
     * 团队今日充值人数
     * @param userIds 用户ids
     * @return
     */
    Integer getRechargePersonNumToday(@Param("list") List<Long> userIds);

    /**
     * 团队首冲人数
     * @param userIds 用户ids
     * @return
     */
    Integer getFirstRechargePersonNum(@Param("list") List<Long> userIds);

    /**
     * 充值订单待审核数量
     * @param baseEntity
     * @param userId
     * @return
     */
    List<Long> getUserRechargePendingReviewNum(@Param("baseEntity") BaseEntity baseEntity, @Param("userId")Long userId);
}
