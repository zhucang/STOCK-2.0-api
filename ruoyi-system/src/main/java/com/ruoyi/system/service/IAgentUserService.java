package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.SysUser;

import java.util.List;

public interface IAgentUserService {

    /**
     * 获取代理用户信息列表
     * @param user
     * @return
     */
    List<SysUser> selectAgentUserList(SysUser user);

    /**
     * 填充其他信息
     * @param users 用户信息列表
     */
    public void fillOtherInfo(List<SysUser> users);

    /**
     * 构建代理树
     * @param users
     */
    public List<SysUser> buildAgentTree(List<SysUser> users);

    /**
     * 获取代理用户信息详情
     * @param id 代理id
     * @return
     */
    SysUser selectAgentUserById(Long id);

    /**
     * 新增代理用户信息
     * @param sysUser
     * @return
     */
    public int insertAgentUser(SysUser sysUser);

    /**
     * 修改代理用户信息
     * @param sysUser
     * @return
     */
    public int updateAgentUser(SysUser sysUser);

    /**
     * 批量删除代理用户信息
     *
     * @param agentUserIds 需要删除的代理用户ID
     * @return 结果
     */
    public int deleteUserByIds(Long[] agentUserIds);

    /**
     * 变更代理的上级
     * @param supUserId 上级用户id
     * @param userId 用户id
     * @return
     */
    public int changeSuperior(Long supUserId, Long userId);
}
