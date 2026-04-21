package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.UserDmAmountChangeRecord;
import com.ruoyi.system.mapper.UserDmAmountChangeRecordMapper;
import com.ruoyi.system.service.IUserDmAmountChangeRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户打码量变更记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-05-24
 */
@Service
public class UserDmAmountChangeRecordServiceImpl implements IUserDmAmountChangeRecordService 
{
    @Resource
    private UserDmAmountChangeRecordMapper userDmAmountChangeRecordMapper;

    /**
     * 查询用户打码量变更记录
     * 
     * @param id 用户打码量变更记录主键
     * @return 用户打码量变更记录
     */
    @Override
    public UserDmAmountChangeRecord selectUserDmAmountChangeRecordById(Long id)
    {
        return userDmAmountChangeRecordMapper.selectUserDmAmountChangeRecordById(id);
    }

    /**
     * 查询用户打码量变更记录列表
     * 
     * @param userDmAmountChangeRecord 用户打码量变更记录
     * @return 用户打码量变更记录
     */
    @Override
    public List<UserDmAmountChangeRecord> selectUserDmAmountChangeRecordList(UserDmAmountChangeRecord userDmAmountChangeRecord)
    {
        return userDmAmountChangeRecordMapper.selectUserDmAmountChangeRecordList(userDmAmountChangeRecord);
    }

    /**
     * 新增用户打码量变更记录
     * 
     * @param userDmAmountChangeRecord 用户打码量变更记录
     * @return 结果
     */
    @Override
    public int insertUserDmAmountChangeRecord(UserDmAmountChangeRecord userDmAmountChangeRecord)
    {
        userDmAmountChangeRecord.setCreateTime(DateUtils.getNowDate());
        return userDmAmountChangeRecordMapper.insertUserDmAmountChangeRecord(userDmAmountChangeRecord);
    }

    /**
     * 修改用户打码量变更记录
     * 
     * @param userDmAmountChangeRecord 用户打码量变更记录
     * @return 结果
     */
    @Override
    public int updateUserDmAmountChangeRecord(UserDmAmountChangeRecord userDmAmountChangeRecord)
    {
        return userDmAmountChangeRecordMapper.updateUserDmAmountChangeRecord(userDmAmountChangeRecord);
    }

    /**
     * 批量删除用户打码量变更记录
     * 
     * @param ids 需要删除的用户打码量变更记录主键
     * @return 结果
     */
    @Override
    public int deleteUserDmAmountChangeRecordByIds(Long[] ids)
    {
        return userDmAmountChangeRecordMapper.deleteUserDmAmountChangeRecordByIds(ids);
    }

    /**
     * 删除用户打码量变更记录信息
     * 
     * @param id 用户打码量变更记录主键
     * @return 结果
     */
    @Override
    public int deleteUserDmAmountChangeRecordById(Long id)
    {
        return userDmAmountChangeRecordMapper.deleteUserDmAmountChangeRecordById(id);
    }
}
