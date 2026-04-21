package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.UserTransferMoneyRecord;

import java.util.List;

/**
 * 用户转账记录Mapper接口
 * 
 * @author ruoyi
 * @date 2025-05-14
 */
public interface UserTransferMoneyRecordMapper 
{
    /**
     * 查询用户转账记录
     * 
     * @param userTransferMoneyRecordId 用户转账记录主键
     * @return 用户转账记录
     */
    public UserTransferMoneyRecord selectUserTransferMoneyRecordByUserTransferMoneyRecordId(Long userTransferMoneyRecordId);

    /**
     * 查询用户转账记录列表
     * 
     * @param userTransferMoneyRecord 用户转账记录
     * @return 用户转账记录集合
     */
    public List<UserTransferMoneyRecord> selectUserTransferMoneyRecordList(UserTransferMoneyRecord userTransferMoneyRecord);

    /**
     * 新增用户转账记录
     * 
     * @param userTransferMoneyRecord 用户转账记录
     * @return 结果
     */
    public int insertUserTransferMoneyRecord(UserTransferMoneyRecord userTransferMoneyRecord);

    /**
     * 修改用户转账记录
     * 
     * @param userTransferMoneyRecord 用户转账记录
     * @return 结果
     */
    public int updateUserTransferMoneyRecord(UserTransferMoneyRecord userTransferMoneyRecord);

    /**
     * 删除用户转账记录
     * 
     * @param userTransferMoneyRecordId 用户转账记录主键
     * @return 结果
     */
    public int deleteUserTransferMoneyRecordByUserTransferMoneyRecordId(Long userTransferMoneyRecordId);

    /**
     * 批量删除用户转账记录
     * 
     * @param userTransferMoneyRecordIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserTransferMoneyRecordByUserTransferMoneyRecordIds(Long[] userTransferMoneyRecordIds);
}
