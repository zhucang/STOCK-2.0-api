package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.SysUser;

import java.util.List;

/**
 * 代理信息
 */
public interface AgentUserMapper {

    /**
     * 根据条件分页查询用户列表
     *
     * @param sysUser 用户信息
     * @return 用户信息集合信息
     */
    public List<SysUser> selectUserList(SysUser sysUser);

}
