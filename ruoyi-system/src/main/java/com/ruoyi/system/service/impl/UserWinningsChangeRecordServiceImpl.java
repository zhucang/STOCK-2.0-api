package com.ruoyi.system.service.impl;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.UserWinningsChangeRecord;
import com.ruoyi.system.mapper.UserWinningsChangeRecordMapper;
import com.ruoyi.system.service.IUserWinningsChangeRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户彩金出入记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-04-04
 */
@Service
public class UserWinningsChangeRecordServiceImpl implements IUserWinningsChangeRecordService 
{
    @Resource
    private UserWinningsChangeRecordMapper userWinningsChangeRecordMapper;

    /**
     * 查询用户彩金出入记录
     * 
     * @param id 用户彩金出入记录主键
     * @return 用户彩金出入记录
     */
    @Override
    public UserWinningsChangeRecord selectUserWinningsChangeRecordById(Long id)
    {
        return userWinningsChangeRecordMapper.selectUserWinningsChangeRecordById(id);
    }

    /**
     * 查询用户彩金出入记录列表
     * 
     * @param userWinningsChangeRecord 用户彩金出入记录
     * @return 用户彩金出入记录
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u", isUserInfo = true)
    public List<UserWinningsChangeRecord> selectUserWinningsChangeRecordList(UserWinningsChangeRecord userWinningsChangeRecord)
    {
        return userWinningsChangeRecordMapper.selectUserWinningsChangeRecordList(userWinningsChangeRecord);
    }

    /**
     * 获取统计数据
     * @param userWinningsChangeRecord
     * @return
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u", isUserInfo = true)
    public List<UserWinningsChangeRecord> getStatisticalData(UserWinningsChangeRecord userWinningsChangeRecord){
        return userWinningsChangeRecordMapper.getStatisticalData(userWinningsChangeRecord);
    }

    /**
     * 新增用户彩金出入记录
     * 
     * @param userWinningsChangeRecord 用户彩金出入记录
     * @return 结果
     */
    @Override
    public int insertUserWinningsChangeRecord(UserWinningsChangeRecord userWinningsChangeRecord)
    {
        userWinningsChangeRecord.setCreateTime(DateUtils.getNowDate());
        return userWinningsChangeRecordMapper.insertUserWinningsChangeRecord(userWinningsChangeRecord);
    }

    /**
     * 修改用户彩金出入记录
     * 
     * @param userWinningsChangeRecord 用户彩金出入记录
     * @return 结果
     */
    @Override
    public int updateUserWinningsChangeRecord(UserWinningsChangeRecord userWinningsChangeRecord)
    {
        return userWinningsChangeRecordMapper.updateUserWinningsChangeRecord(userWinningsChangeRecord);
    }

    /**
     * 批量删除用户彩金出入记录
     * 
     * @param ids 需要删除的用户彩金出入记录主键
     * @return 结果
     */
    @Override
    public int deleteUserWinningsChangeRecordByIds(Long[] ids)
    {
        return userWinningsChangeRecordMapper.deleteUserWinningsChangeRecordByIds(ids);
    }

    /**
     * 删除用户彩金出入记录信息
     * 
     * @param id 用户彩金出入记录主键
     * @return 结果
     */
    @Override
    public int deleteUserWinningsChangeRecordById(Long id)
    {
        return userWinningsChangeRecordMapper.deleteUserWinningsChangeRecordById(id);
    }

    /**
     * 获取用户的各币种的赠送、回收彩金金额
     * @param userId 用户id
     * @param orderType 订单类型 0：彩金赠送（系统充值） 1：彩金赠送（福利彩金） 2：彩金回收 3:充值彩金 4：注册彩金
     * @return
     */
    @Override
    public List<UserWinningsChangeRecord> selectUserWinningsChangeAmountAllCurrencyByUserId(Long userId, Integer orderType){
        return userWinningsChangeRecordMapper.selectUserWinningsChangeAmountAllCurrencyByUserId(userId,orderType);
    }
}
