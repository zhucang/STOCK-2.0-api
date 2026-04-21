package com.ruoyi.system.service;

import com.ruoyi.system.domain.UserPointChangeRecord;

import java.util.List;

/**
 * 用户上分下分记录Service接口
 * 
 * @author ruoyi
 * @date 2024-03-30
 */
public interface IUserPointChangeRecordService 
{
    /**
     * 查询用户上分下分记录
     * 
     * @param id 用户上分下分记录主键
     * @return 用户上分下分记录
     */
    public UserPointChangeRecord selectUserPointChangeRecordById(Long id);

    /**
     * 查询用户上分下分记录列表
     * 
     * @param userPointChangeRecord 用户上分下分记录
     * @return 用户上分下分记录集合
     */
    public List<UserPointChangeRecord> selectUserPointChangeRecordList(UserPointChangeRecord userPointChangeRecord);

    /**
     * 获取统计数据
     * @param userPointChangeRecord
     * @return
     */
    public List<UserPointChangeRecord> getStatisticalData(UserPointChangeRecord userPointChangeRecord);

    /**
     * 填充其他信息
     * @param userPointChangeRecords 用户上分下分记录列表
     */
    public void fillOtherInfo(List<UserPointChangeRecord> userPointChangeRecords);

    /**
     * 新增用户上分下分记录
     * 
     * @param userPointChangeRecord 用户上分下分记录
     * @return 结果
     */
    public int insertUserPointChangeRecord(UserPointChangeRecord userPointChangeRecord);

    /**
     * 修改用户上分下分记录
     * 
     * @param userPointChangeRecord 用户上分下分记录
     * @return 结果
     */
    public int updateUserPointChangeRecord(UserPointChangeRecord userPointChangeRecord);

    /**
     * 批量删除用户上分下分记录
     * 
     * @param ids 需要删除的用户上分下分记录主键集合
     * @return 结果
     */
    public int deleteUserPointChangeRecordByIds(Long[] ids);

    /**
     * 删除用户上分下分记录信息
     * 
     * @param id 用户上分下分记录主键
     * @return 结果
     */
    public int deleteUserPointChangeRecordById(Long id);

    /**
     * 获取用户的各币种的上下分金额
     * @param userId 用户id
     * @param orderType 订单类型 0：上分 1：下分
     * @return
     */
    List<UserPointChangeRecord> selectUserPointChangeAmountAllCurrencyByUserId(Long userId,Integer orderType);
}
