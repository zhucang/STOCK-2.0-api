package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.domain.entity.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户信息Mapper接口
 * 
 * @author ruoyi
 * @date 2023-10-28
 */
public interface UserInfoMapper 
{
    /**
     * 查询用户信息
     * 
     * @param id 用户信息主键
     * @return 用户信息
     */
    public UserInfo selectUserInfoById(Long id);

    /**
     * 查询用户信息
     *
     * @param userAccount 用户账号
     * @return 用户信息
     */
    public UserInfo selectUserInfoByUserAccount(String userAccount);

    /**
     * 查询用户信息
     *
     * @param loginAccount 登录账号
     * @return 用户信息
     */
    UserInfo userLogin(String loginAccount);

    /**
     * 查询用户信息
     *
     * @param email 邮箱
     * @return 用户信息
     */
    public UserInfo selectUserInfoByEmail(String email);

    /**
     * 查询用户信息
     *
     * @param phone 邮箱
     * @return 用户信息
     */
    public UserInfo selectUserInfoByPhone(String phone);

    /**
     * 查询用户信息
     *
     * @param inviteCode 邀请码
     * @return 用户信息
     */
    public UserInfo selectUserInfoByInviteCode(String inviteCode);

    /**
     * 查询用户信息列表
     * 
     * @param userInfo 用户信息
     * @return 用户信息集合
     */
    public List<UserInfo> selectUserInfoList(UserInfo userInfo);

    /**
     * 新增用户信息
     * 
     * @param userInfo 用户信息
     * @return 结果
     */
    public int insertUserInfo(UserInfo userInfo);

    /**
     * 修改用户信息
     * 
     * @param userInfo 用户信息
     * @return 结果
     */
    public int updateUserInfo(UserInfo userInfo);

    /**
     * 删除用户信息
     * 
     * @param id 用户信息主键
     * @return 结果
     */
    public int deleteUserInfoById(Long id);

    /**
     * 批量删除用户信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserInfoByIds(Long[] ids);

    /**
     * 替换上级用户id
     * @param supUserIdBefore
     * @param supUserIdAfter
     * @return
     */
    int replaceSupUserId(@Param("supUserIdBefore")Long supUserIdBefore, @Param("supUserIdAfter") Long supUserIdAfter);

    /**
     * 替换代理id与代理名称
     * @param agentIdBefore
     * @param agentIdAfter
     * @param agentName
     * @return
     */
    int replaceAgentIdAndAgentName(@Param("agentIdBefore")Long agentIdBefore,
                                   @Param("agentIdAfter") Long agentIdAfter,
                                   @Param("agentName")String agentName,
                                   @Param("agentNickName")String agentNickName,
                                   @Param("userIds")List<Long> userIds);

    /**
     * 实名认证待审核数量
     * @param baseEntity
     * @return
     */
    List<Long> getRealNameAuthPendingReviewNum(BaseEntity baseEntity);

    /**
     * 查询用户登陆密码
     * @param userId 用户id
     * @return
     */
    String selectUserPwdByUserId(Long userId);

    /**
     * 查询用户提现密码
     * @param userId 用户id
     * @return
     */
    String selectUserWithPwdByUserId(Long userId);

    /**
     * 更新用户的实名认证状态
     * @param userIds 用户ids
     * @param authLevel 0：初级 1：高级
     * @param authStatus 实名认证状态  0：未实名 1：审核中 2：审核通过 3：审核驳回
     * @return
     */
    public int updateUserAuthStatus(@Param("userIds")List<Long> userIds,
                                    @Param("authLevel")Integer authLevel,
                                    @Param("authStatus")Integer authStatus);
}
