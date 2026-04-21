package com.ruoyi.system.service;

import com.ruoyi.system.domain.UserAuthRecord;

import java.util.List;

/**
 * 用户实名认证信息Service接口
 * 
 * @author ruoyi
 * @date 2024-04-05
 */
public interface IUserAuthRecordService 
{
    /**
     * 查询用户实名认证信息
     * 
     * @param id 用户实名认证信息主键
     * @return 用户实名认证信息
     */
    public UserAuthRecord selectUserAuthRecordById(Long id);

    /**
     * 获取最近一条用户实名认证记录
     * @param userId 用户id
     * @param authLevel 认证等级
     * @return
     */
    public UserAuthRecord selectLastOne(Long userId, Integer authLevel);

    /**
     * 查询用户实名认证信息列表
     * 
     * @param userAuthRecord 用户实名认证信息
     * @return 用户实名认证信息集合
     */
    public List<UserAuthRecord> selectUserAuthRecordList(UserAuthRecord userAuthRecord);

    /**
     * 新增用户实名认证信息
     * 
     * @param userAuthRecord 用户实名认证信息
     * @return 结果
     */
    public int insertUserAuthRecord(UserAuthRecord userAuthRecord);

    /**
     * 修改用户实名认证信息
     * 
     * @param userAuthRecord 用户实名认证信息
     * @return 结果
     */
    public int updateUserAuthRecord(UserAuthRecord userAuthRecord);

    /**
     * 批量删除用户实名认证信息
     * 
     * @param ids 需要删除的用户实名认证信息主键集合
     * @return 结果
     */
    public int deleteUserAuthRecordByIds(Long[] ids);

    /**
     * 删除用户实名认证信息信息
     * 
     * @param id 用户实名认证信息主键
     * @return 结果
     */
    public int deleteUserAuthRecordById(Long id);

    /**
     * 用户实名认证审核
     * @param userAuthRecord 用户实名认证信息信息
     * @return
     */
    public int userAuthReview(UserAuthRecord userAuthRecord);


//    --------------------------------------------------------------------

    /**
     * 用户实名认证申请
     * @param userAuthRecord 用户实名认证信息信息
     * @return
     */
    public int userAuthApply(UserAuthRecord userAuthRecord);
}
