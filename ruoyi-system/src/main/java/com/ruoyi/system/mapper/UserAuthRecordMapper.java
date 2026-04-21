package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.UserAuthRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户实名认证信息Mapper接口
 * 
 * @author ruoyi
 * @date 2024-04-05
 */
public interface UserAuthRecordMapper 
{
    /**
     * 查询用户实名认证信息
     * 
     * @param id 用户实名认证信息主键
     * @return 用户实名认证信息
     */
    public UserAuthRecord selectUserAuthRecordById(Long id);

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
     * 删除用户实名认证信息
     * 
     * @param id 用户实名认证信息主键
     * @return 结果
     */
    public int deleteUserAuthRecordById(Long id);

    /**
     * 批量删除用户实名认证信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserAuthRecordByIds(Long[] ids);

    /**
     * 获取最近一条用户实名认证记录
     * @param userId 用户id
     * @param authLevel 认证等级
     * @return
     */
    public UserAuthRecord selectLastOne(@Param("userId") Long userId,
                                        @Param("authLevel") Integer authLevel);
}
