package com.ruoyi.system.mapper;


import com.ruoyi.system.domain.UserCommissionRecord;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 用户返佣记录Mapper接口
 * 
 * @author ruoyi
 * @date 2023-09-09
 */
public interface UserCommissionRecordMapper 
{
    /**
     * 查询用户返佣记录
     * 
     * @param id 用户返佣记录主键
     * @return 用户返佣记录
     */
    public UserCommissionRecord selectUserCommissionRecordById(Long id);

    /**
     * 查询用户返佣记录列表
     * 
     * @param userCommissionRecord 用户返佣记录
     * @return 用户返佣记录集合
     */
    public List<UserCommissionRecord> selectUserCommissionRecordList(UserCommissionRecord userCommissionRecord);

    /**
     * 新增用户返佣记录
     * 
     * @param userCommissionRecord 用户返佣记录
     * @return 结果
     */
    public int insertUserCommissionRecord(UserCommissionRecord userCommissionRecord);

    /**
     * 修改用户返佣记录
     * 
     * @param userCommissionRecord 用户返佣记录
     * @return 结果
     */
    public int updateUserCommissionRecord(UserCommissionRecord userCommissionRecord);

    /**
     * 删除用户返佣记录
     * 
     * @param id 用户返佣记录主键
     * @return 结果
     */
    public int deleteUserCommissionRecordById(Long id);

    /**
     * 批量删除用户返佣记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserCommissionRecordByIds(Long[] ids);

    /**
     * 获取用户各币种的返佣佣金
     * @param userIds 用户ids
     * @param supUserId 上级用户id
     * @param commissionType 返佣类型 0：充值
     * @return
     */
    List<UserCommissionRecord> selectUserCommissionAmountAllCurrency(@Param("list") List<Long> userIds,
                                                                     @Param("supUserId") Long supUserId,
                                                                     @Param("commissionType") Integer commissionType,
                                                                     @Param("startTime") Date startTime,
                                                                     @Param("endTime") Date endTime);
}
