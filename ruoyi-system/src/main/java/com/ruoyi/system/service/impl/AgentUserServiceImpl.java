package com.ruoyi.system.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.github.pagehelper.PageHelper;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.AgentTeamLevelLine;
import com.ruoyi.system.mapper.AgentUserMapper;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.service.IAgentTeamLevelLineService;
import com.ruoyi.system.service.IAgentUserService;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AgentUserServiceImpl implements IAgentUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private IAgentTeamLevelLineService agentTeamLevelLineService;

    @Resource
    private AgentUserMapper agentUserMapper;

    /**
     * 获取代理用户信息列表
     * @param user
     * @return
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectAgentUserList(SysUser user){
        return agentUserMapper.selectUserList(user);
    }

    /**
     * 填充其他信息
     * @param users 用户信息列表
     */
    @Override
    public void fillOtherInfo(List<SysUser> users){
        fillSupUserLine(users);
        fillRoleName(users);
    }

    /**
     * 填充上级用户线
     * @param users 用户信息列表
     */
    public void fillSupUserLine(List<SysUser> users){
        for (int i = 0; i < users.size(); i++) {
            //获取用户上级线
            PageHelper.orderBy("team_level");
            AgentTeamLevelLine agentTeamLevelLine = new AgentTeamLevelLine();
            agentTeamLevelLine.setUserId(users.get(i).getUserId());
            List<AgentTeamLevelLine> agentTeamLevelLines = agentTeamLevelLineService.selectAgentTeamLevelLineList(agentTeamLevelLine);
            String supUserLine = users.get(i).getUserName();
            if (agentTeamLevelLines.size() > 0){
                List<Long> supUserIds = agentTeamLevelLines.stream().map(AgentTeamLevelLine::getSupUserId).collect(Collectors.toList());
                SysUser search = new SysUser();
                search.getParams().put("userIds",supUserIds);
                search.getParams().put("agentData",1);
                Map<Long, SysUser> userMap = sysUserMapper.selectUserList(search).stream().collect(Collectors.toMap(a -> a.getUserId(), a -> a));
                for (int j = 0; j < agentTeamLevelLines.size(); j++) {
                    //用户id
                    Long userId = agentTeamLevelLines.get(j).getSupUserId();
                    String userAccount = userMap.get(userId).getUserName();
                    supUserLine = userAccount + ">>" + supUserLine;
                }
            }
            users.get(i).setSupUserLine(supUserLine);
        }
    }

    /**
     * 构建代理树
     * @param users
     */
    @Override
    public List<SysUser> buildAgentTree(List<SysUser> users){
        //所有团队关系
        List<AgentTeamLevelLine> agentTeamLevelLines = agentTeamLevelLineService.selectAgentTeamLevelLineList(new AgentTeamLevelLine());
        //非顶点的用户ids
        List<Long> notTopUserIds = agentTeamLevelLines.stream().map(AgentTeamLevelLine::getUserId).collect(Collectors.toList());
        //顶点的用户
        List<SysUser> topUser = users.stream().filter(a -> !notTopUserIds.contains(a.getUserId())).collect(Collectors.toList());
        //获取子节点
        getAgentTreeChildList(agentTeamLevelLines,topUser,users);
        return topUser;
    }

    /**获取代理数子节点
     *
     * @param agentTeamLevelLines 团队关系
     * @param usersFather 父节点
     * @param usersAll 所有用户信息
     */
    public void getAgentTreeChildList(List<AgentTeamLevelLine> agentTeamLevelLines,List<SysUser> usersFather,List<SysUser> usersAll){
        //遍历获取子节点
        for (int i = 0; i < usersFather.size(); i++) {
            SysUser sysUser = usersFather.get(i);
            //节点父id
            Long parentId = sysUser.getUserId();
            //下级用户ids
            List<Long> lowerUserIds = agentTeamLevelLines.stream().filter(a -> a.getSupUserId().equals(parentId) && a.getTeamLevel().equals(1)).map(AgentTeamLevelLine::getUserId).collect(Collectors.toList());
            //如果有下级用户
            if (lowerUserIds.size() > 0){
                //下级用户
                List<SysUser> lowerUsers = usersAll.stream().filter(a -> lowerUserIds.contains(a.getUserId())).collect(Collectors.toList());
                //塞入children
                sysUser.getParams().put("children",lowerUsers);
                //递归
                getAgentTreeChildList(agentTeamLevelLines,lowerUsers,usersAll);
            }else {
                //塞入空children
                sysUser.getParams().put("children",new ArrayList<SysUser>());
            }
        }
    }

    /**
     * 填充角色名称
     * @param users 用户信息列表
     */
    public void fillRoleName(List<SysUser> users){
        for (int i = 0; i < users.size(); i++) {
            //角色名称
            String roleName = users.get(i).getRoles().stream().map(SysRole::getRoleName).collect(Collectors.joining());
            users.get(i).getParams().put("roleName",roleName);
        }
    }

    /**
     * 获取代理用户信息详情
     * @param id 代理id
     * @return
     */
    @Override
    public SysUser selectAgentUserById(Long id){
        SysUser sysUser = sysUserMapper.selectUserById(id);
        sysUser.setRoleIds(ArrayUtil.toArray(sysUser.getRoles().stream().map(SysRole::getRoleId).collect(Collectors.toList()), Long.class));
        return sysUser;
    }

    /**
     * 新增代理用户信息
     * @param sysUser
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertAgentUser(SysUser sysUser){
        return sysUserService.insertUser(sysUser);
    }

    /**
     * 修改代理用户信息
     * @param sysUser
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateAgentUser(SysUser sysUser){
        return sysUserService.updateUser(sysUser);
    }

    /**
     * 批量删除代理用户信息
     *
     * @param agentUserIds 需要删除的代理用户ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteUserByIds(Long[] agentUserIds){
        return sysUserService.deleteUserByIds(agentUserIds);
    }

    /**
     * 变更代理的上级
     * @param supUserId 上级用户id
     * @param userId 用户id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int changeSuperior(Long supUserId, Long userId){
        return agentTeamLevelLineService.updateAgentTeamLevelLine(userId, supUserId, 2);
    }
}
