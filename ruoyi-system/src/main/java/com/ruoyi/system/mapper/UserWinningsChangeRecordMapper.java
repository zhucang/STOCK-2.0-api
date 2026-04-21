package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.UserWinningsChangeRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户彩金出入记录Mapper接口
 * 
 * @author ruoyi
 * @date 2024-04-04
 */
public interface UserWinningsChangeRecordMapper 
{
    /**
     * 查询用户彩金出入记录
     * 
     * @param id 用户彩金出入记录主键
     * @return 用户彩金出入记录
     */
    public UserWinningsChangeRecord selectUserWinningsChangeRecordById(Long id);

    /**
     * 查询用户彩金出入记录列表
     * 
     * @param userWinningsChangeRecord 用户彩金出入记录
     * @return 用户彩金出入记录集合
     */
    public List<UserWinningsChangeRecord> selectUserWinningsChangeRecordList(UserWinningsChangeRecord userWinningsChangeRecord);

    /**
     * 获取统计数据
     * @param userWinningsChangeRecord
     * @return
     */
    public List<UserWinningsChangeRecord> getStatisticalData(UserWinningsChangeRecord userWinningsChangeRecord);

    /**
     * 新增用户彩金出入记录
     * 
     * @param userWinningsChangeRecord 用户彩金出入记录
     * @return 结果
     */
    public int insertUserWinningsChangeRecord(UserWinningsChangeRecord userWinningsChangeRecord);

    /**
     * 修改用户彩金出入记录
     * 
     * @param userWinningsChangeRecord 用户彩金出入记录
     * @return 结果
     */
    public int updateUserWinningsChangeRecord(UserWinningsChangeRecord userWinningsChangeRecord);

    /**
     * 删除用户彩金出入记录
     * 
     * @param id 用户彩金出入记录主键
     * @return 结果
     */
    public int deleteUserWinningsChangeRecordById(Long id);

    /**
     * 批量删除用户彩金出入记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserWinningsChangeRecordByIds(Long[] ids);

    /**
     * 获取用户的各币种的赠送、回收彩金金额
     * @param userId 用户id
     * @param orderType 订单类型 0：彩金赠送（系统充值） 1：彩金赠送（福利彩金） 2：彩金回收 3:充值彩金 4：注册彩金
     * @return
     */
    List<UserWinningsChangeRecord> selectUserWinningsChangeAmountAllCurrencyByUserId(@Param("userId") Long userId,@Param("orderType") Integer orderType);
}
