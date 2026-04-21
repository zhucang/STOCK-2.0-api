package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.UserDmAmountChangeRecord;

import java.util.List;

/**
 * 用户打码量变更记录Mapper接口
 * 
 * @author ruoyi
 * @date 2024-05-24
 */
public interface UserDmAmountChangeRecordMapper 
{
    /**
     * 查询用户打码量变更记录
     * 
     * @param id 用户打码量变更记录主键
     * @return 用户打码量变更记录
     */
    public UserDmAmountChangeRecord selectUserDmAmountChangeRecordById(Long id);

    /**
     * 查询用户打码量变更记录列表
     * 
     * @param userDmAmountChangeRecord 用户打码量变更记录
     * @return 用户打码量变更记录集合
     */
    public List<UserDmAmountChangeRecord> selectUserDmAmountChangeRecordList(UserDmAmountChangeRecord userDmAmountChangeRecord);

    /**
     * 新增用户打码量变更记录
     * 
     * @param userDmAmountChangeRecord 用户打码量变更记录
     * @return 结果
     */
    public int insertUserDmAmountChangeRecord(UserDmAmountChangeRecord userDmAmountChangeRecord);

    /**
     * 修改用户打码量变更记录
     * 
     * @param userDmAmountChangeRecord 用户打码量变更记录
     * @return 结果
     */
    public int updateUserDmAmountChangeRecord(UserDmAmountChangeRecord userDmAmountChangeRecord);

    /**
     * 删除用户打码量变更记录
     * 
     * @param id 用户打码量变更记录主键
     * @return 结果
     */
    public int deleteUserDmAmountChangeRecordById(Long id);

    /**
     * 批量删除用户打码量变更记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserDmAmountChangeRecordByIds(Long[] ids);
}
