package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.domain.AgentTeamLevelLine;
import com.ruoyi.system.mapper.AgentTeamLevelLineMapper;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.mapper.UserInfoMapper;
import com.ruoyi.system.service.IAgentTeamLevelLineService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 代理团队关系网（代理代理线）Service业务层处理
 *
 * @author ruoyi
 * @date 2023-11-19
 */
@Service
public class AgentTeamLevelLineServiceImpl implements IAgentTeamLevelLineService
{
    @Resource
    private AgentTeamLevelLineMapper agentTeamLevelLineMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    /**
     * 查询代理团队关系网（代理代理线）
     *
     * @param id 代理团队关系网（代理代理线）主键
     * @return 代理团队关系网（代理代理线）
     */
    @Override
    public AgentTeamLevelLine selectAgentTeamLevelLineById(Long id)
    {
        return agentTeamLevelLineMapper.selectAgentTeamLevelLineById(id);
    }

    /**
     * 查询代理团队关系网（代理代理线）列表
     *
     * @param agentTeamLevelLine 代理团队关系网（代理代理线）
     * @return 代理团队关系网（代理代理线）
     */
    @Override
    public List<AgentTeamLevelLine> selectAgentTeamLevelLineList(AgentTeamLevelLine agentTeamLevelLine)
    {
        return agentTeamLevelLineMapper.selectAgentTeamLevelLineList(agentTeamLevelLine);
    }

    /**
     * 新增代理团队关系网（代理代理线）
     *
     * @param agentTeamLevelLine 代理团队关系网（代理代理线）
     * @return 结果
     */
    @Override
    public int insertAgentTeamLevelLine(AgentTeamLevelLine agentTeamLevelLine)
    {
        agentTeamLevelLine.setCreateTime(DateUtils.getNowDate());
        return agentTeamLevelLineMapper.insertAgentTeamLevelLine(agentTeamLevelLine);
    }

    /**
     * 修改代理团队关系网（代理代理线）
     *
     * @param agentTeamLevelLine 代理团队关系网（代理代理线）
     * @return 结果
     */
    @Override
    public int updateAgentTeamLevelLine(AgentTeamLevelLine agentTeamLevelLine)
    {
        return agentTeamLevelLineMapper.updateAgentTeamLevelLine(agentTeamLevelLine);
    }

    /**
     * 批量删除代理团队关系网（代理代理线）
     *
     * @param ids 需要删除的代理团队关系网（代理代理线）主键
     * @return 结果
     */
    @Override
    public int deleteAgentTeamLevelLineByIds(Long[] ids)
    {
        return agentTeamLevelLineMapper.deleteAgentTeamLevelLineByIds(ids);
    }

    /**
     * 删除代理团队关系网（代理代理线）信息
     *
     * @param id 代理团队关系网（代理代理线）主键
     * @return 结果
     */
    @Override
    public int deleteAgentTeamLevelLineById(Long id)
    {
        return agentTeamLevelLineMapper.deleteAgentTeamLevelLineById(id);
    }

    /**
     * 更新团队等级关系
     * @param userId 用户id
     * @param supUserId 上级用户id
     * @param updateType 更新类型：0：用户新增更新  1：用户删除更新 2：变更代理线更新
     * @return
     */
    public int updateAgentTeamLevelLine(Long userId,Long supUserId,int updateType){
        int count = 0;
        if (updateType == 0){
            count = updateAgentTeamLevelLineByInsertUser(userId,supUserId);
        }else if (updateType == 1){
            count = updateAgentTeamLevelLineByDeleteUser(userId,supUserId);
        }else if (updateType == 2){
            count = updateAgentTeamLevelLineByChangeSuperior(userId,supUserId);
        }
        validate();
        return count;
    }

    /**
     * 新增用户更新团队等级关系
     * @param userId 用户id
     * @param supUserId 上级用户id
     * @return
     */
    int updateAgentTeamLevelLineByInsertUser(Long userId,Long supUserId){
        //上级信息
        SysUser supUser = sysUserMapper.selectUserById(supUserId);
        if (supUser == null || "2".equals(supUser.getDelFlag())){
            throw new ServiceException("上级不存在");
        }
        //日志记录上级账号
        HttpUtils.getRequestLogParams().put("上级账号", supUser.getUserName());
        //即将新增的代理线
        List<AgentTeamLevelLine> newAgentTeamLevelLineList = new ArrayList<>();
        //插入用户与上级的团队等级关系
        AgentTeamLevelLine AgentTeamLevelLine = new AgentTeamLevelLine();
        AgentTeamLevelLine.setUserId(userId);
        AgentTeamLevelLine.setSupUserId(supUserId);
        AgentTeamLevelLine.setTeamLevel(1);
        AgentTeamLevelLine.setCreateTime(new Date());
        newAgentTeamLevelLineList.add(AgentTeamLevelLine);
        //获取上级用户的所有上级线
        List<AgentTeamLevelLine> AgentTeamLevelLines = agentTeamLevelLineMapper.getSupTeamLine(supUserId,null,0);
        //插入用户与上级用户的所有上级的团队等级关系
        for (int i = 0; i < AgentTeamLevelLines.size(); i++) {
            AgentTeamLevelLine AgentTeamLevelLineVo = AgentTeamLevelLines.get(i);
            AgentTeamLevelLineVo.setUserId(userId);
            AgentTeamLevelLineVo.setTeamLevel(AgentTeamLevelLineVo.getTeamLevel()+1);
            AgentTeamLevelLineVo.setCreateTime(new Date());
            newAgentTeamLevelLineList.add(AgentTeamLevelLineVo);
        }
        int count = agentTeamLevelLineMapper.insertAgentTeamLevelLines(newAgentTeamLevelLineList);
        if (count != newAgentTeamLevelLineList.size()){
            return 0;
        }
        return 1;
    }

    /**
     * 删除用户更新团队等级关系
     * @param userId 用户id
     * @param supUserId 上级用户id
     * @return
     */
    int updateAgentTeamLevelLineByDeleteUser(Long userId,Long supUserId){
        //如果有上级
        if (supUserId != null){
            //清空其supUserId;
            SysUser sysUser = sysUserMapper.selectUserById(userId);
            sysUser.getParams().put("cleanSupUserId",0);
            int updateUser = sysUserMapper.updateUser(sysUser);
            if (updateUser <= 0){
                throw new ServiceException("系统繁忙");
            }
            //上级用户
            SysUser supUser = sysUserMapper.selectUserById(supUserId);
            //变更其管辖的app用户的agentId为自己的上级
            userInfoMapper.replaceAgentIdAndAgentName(userId,supUserId,supUser.getUserName(),supUser.getNickName(),null);
        }else {
            //清空其管辖的app用户的agentId
            userInfoMapper.replaceAgentIdAndAgentName(userId,null,null,null,null);
        }

        //所有下级团队关系信息
        List<AgentTeamLevelLine> lowerTeamLine = agentTeamLevelLineMapper.getLowerTeamLine(userId, null, 0);
        //直推下级人数
        long directLowersCount = lowerTeamLine.stream().filter(a -> a.getTeamLevel().equals(1)).count();
        //更新用户信息表的上级用户id
        //删除用户后其下级的上级用户id变更为此用户的上级用户id
        int updateSupUserId = sysUserMapper.updateSupUserId(userId, supUserId);
        if (directLowersCount != updateSupUserId){
            throw new RuntimeException("系统繁忙");
        }
        //所有上级团队关系信息
        List<AgentTeamLevelLine> supTeamLine = agentTeamLevelLineMapper.getSupTeamLine(userId, null, 0);
        //更新团队关系网
        //清空用户的团队关系网
        int cleanAgentTeamLevelLineByUserId = agentTeamLevelLineMapper.cleanAgentTeamLevelLineByUserId(userId);
        //此用户涉及到的关系网数量
        int teamLineInvolvedCount = lowerTeamLine.size() + supTeamLine.size();
        if (teamLineInvolvedCount != cleanAgentTeamLevelLineByUserId){
            throw new RuntimeException("系统繁忙");
        }
        //下级团队ids
        List<Long> lowerTeamUserIds = lowerTeamLine.stream().map(AgentTeamLevelLine::getUserId).collect(Collectors.toList());
        //上级团队ids
        List<Long> supTeamUserIds = supTeamLine.stream().map(AgentTeamLevelLine::getSupUserId).collect(Collectors.toList());
        //如果上下都有团队
        if (lowerTeamUserIds.size() != 0 && supTeamUserIds.size() != 0){
            int connectUpperAndLowerTeam = agentTeamLevelLineMapper.connectUpperAndLowerTeam(lowerTeamUserIds, supTeamUserIds);
            if (connectUpperAndLowerTeam != lowerTeamUserIds.size()*supTeamUserIds.size()){
                throw new RuntimeException("系统繁忙");
            }
        }
        return 1;
    }

//    /**
//     * 变更上级更新团队等级关系
//     * @param userId 用户id
//     * @param supUserId 上级用户id
//     * @return
//     */
//    int updateAgentTeamLevelLineByChangeSuperior(Long userId,Long supUserId){
//        if (userId.equals(supUserId)){
//            throw new ServiceException("即将设置的新上级是当前用户自己");
//        }
//        //上级信息
//        SysUser supUser = sysUserMapper.selectUserById(supUserId);
//        if (supUser == null || "2".equals(supUser.getDelFlag())){
//            throw new ServiceException("上级不存在");
//        }
//        //获取上级用户的所有上级团队关系信息
//        List<AgentTeamLevelLine> supTeamLineBySupUser = agentTeamLevelLineMapper.getSupTeamLine(supUserId, null, 0);
//        //验证新的上级是否是当前用户的下级
//        if (supTeamLineBySupUser.stream().filter(a->a.getSupUserId().equals(userId)).count() > 0){
//            throw new ServiceException("即将设置的新上级是当前用户的下级！");
//        }
//        //更新当前用户的上级用户id
//        SysUser sysUser = sysUserMapper.selectUserById(userId);
//        sysUser.setSupUserId(supUserId);
//        int updateUser = sysUserMapper.updateUser(sysUser);
//        if (updateUser <= 0){
//            throw new ServiceException("系统繁忙");
//        }
//        //获取当前用户的所有下级团队关系信息
//        List<AgentTeamLevelLine> lowerTeamLine = agentTeamLevelLineMapper.getLowerTeamLine(userId, null, 0);
//        //获取当前用户的所有上级团队关系信息
//        List<AgentTeamLevelLine> supTeamLine = agentTeamLevelLineMapper.getSupTeamLine(userId, null, 0);
//        //清空当前用户的团队关系网
//        int cleanAgentTeamLevelLineByUserId = agentTeamLevelLineMapper.cleanSupAgentTeamLevelLineByUserId(userId);
//        //此用户涉及到的关系网数量
//        int teamLineInvolvedCount = supTeamLine.size();
//        if (teamLineInvolvedCount != cleanAgentTeamLevelLineByUserId){
//            throw new ServiceException("系统繁忙");
//        }
//        //删除下级
//        for (int i = 0; i < lowerTeamLine.size(); i++) {
//            AgentTeamLevelLine agentTeamLevelLine = lowerTeamLine.get(i);
//            List<AgentTeamLevelLine> line = agentTeamLevelLineMapper.getSupTeamLine(agentTeamLevelLine.getUserId(), null, 0);
//            Integer teamLevel = agentTeamLevelLine.getTeamLevel();
//            List<Long> collect = line.stream().filter(a -> a.getTeamLevel() > teamLevel).map(AgentTeamLevelLine::getId).collect(Collectors.toList());
//            int deleteAgentTeamLevelLineByIds = agentTeamLevelLineMapper.deleteAgentTeamLevelLineByIds(collect.toArray(new Long[collect.size()]));
//            if (deleteAgentTeamLevelLineByIds != collect.size()){
//                throw new ServiceException("系统繁忙");
//            }
//        }
//        //加入上级用户本身
//        AgentTeamLevelLine supUserTeamLevelLine = new AgentTeamLevelLine();
//        supUserTeamLevelLine.setSupUserId(supUserId);
//        supUserTeamLevelLine.setTeamLevel(0);
//        supTeamLineBySupUser.add(supUserTeamLevelLine);
////        166,165,164       163    162
//        //当前时间
//        Date nowDateTime = new Date();
//        List<AgentTeamLevelLine> agentTeamLevelLines = new ArrayList<>();
//        //遍历
//        for (int i = 0; i < supTeamLineBySupUser.size(); i++) {
//            //上级信息
//            AgentTeamLevelLine sup = supTeamLineBySupUser.get(i);
//            //获取新上级与原下级的团队关系信息
//            for (int j = 0; j < lowerTeamLine.size(); j++) {
//                //下级信息
//                AgentTeamLevelLine low = lowerTeamLine.get(j);
//                //new
//                AgentTeamLevelLine vo = new AgentTeamLevelLine();
//                vo.setUserId(low.getUserId());
//                vo.setSupUserId(sup.getSupUserId());
//                vo.setTeamLevel(low.getTeamLevel()+sup.getTeamLevel()+1);
//                vo.setCreateTime(nowDateTime);
//                agentTeamLevelLines.add(vo);
//            }
//            //获取新上级当前用户的团队关系信息
//            AgentTeamLevelLine vo = new AgentTeamLevelLine();
//            vo.setUserId(userId);
//            vo.setSupUserId(sup.getSupUserId());
//            vo.setTeamLevel(sup.getTeamLevel()+1);
//            vo.setCreateTime(nowDateTime);
//            agentTeamLevelLines.add(vo);
//        }
////        加入当前用户与原下级的团队关系信息
////        agentTeamLevelLines.addAll(lowerTeamLine);
//        int insertAgentTeamLevelLines = agentTeamLevelLineMapper.insertAgentTeamLevelLines(agentTeamLevelLines);
//        if (insertAgentTeamLevelLines != agentTeamLevelLines.size()){
//            throw new ServiceException("系统繁忙");
//        }
//        return 1;
//    }

    /**
     * 变更上级更新团队等级关系
     * @param userId 用户id
     * @param supUserId 上级用户id
     * @return
     */
    int updateAgentTeamLevelLineByChangeSuperior(Long userId,Long supUserId){
        //验证即将设置的新上级是当前用户自己
        if (userId.equals(supUserId)){
            throw new ServiceException("即将设置的新上级是当前用户自己");
        }
        //自身信息
        SysUser user = sysUserMapper.selectUserById(userId);
        if (user == null || "2".equals(user.getDelFlag())){
            throw new ServiceException("获取用户信息异常");
        }
        //日志记录用户id
        HttpUtils.getRequestLogParams().put("用户ID", userId);
        //日志记录用户账号
        HttpUtils.getRequestLogParams().put("用户账号", user.getUserName());
        //上级信息
        SysUser supUser = sysUserMapper.selectUserById(supUserId);
        if (supUser == null || "2".equals(supUser.getDelFlag())){
            throw new ServiceException("上级不存在");
        }
        //日志记录上级id
        HttpUtils.getRequestLogParams().put("上级ID", supUserId);
        //日志记录上级账号
        HttpUtils.getRequestLogParams().put("上级账号", supUser.getUserName());
        //获取上级用户的所有上级团队关系信息
        List<AgentTeamLevelLine> supTeamLineBySupUser = agentTeamLevelLineMapper.getSupTeamLine(supUserId, null, 0);
        //验证新的上级是否是当前用户的下级
        if (supTeamLineBySupUser.stream().filter(a->a.getSupUserId().equals(userId)).count() > 0){
            throw new ServiceException("即将设置的新上级是当前用户的下级！");
        }
        //更新当前用户的上级用户id
        SysUser sysUser = sysUserMapper.selectUserById(userId);
        sysUser.setSupUserId(supUserId);
        int updateUser = sysUserMapper.updateUser(sysUser);
        if (updateUser <= 0){
            throw new ServiceException("系统繁忙");
        }
        //获取当前用户的所有下级团队关系信息
        List<AgentTeamLevelLine> lowerTeamLine = agentTeamLevelLineMapper.getLowerTeamLine(userId, null, 0);
        //获取当前用户的所有上级团队关系信息
        List<AgentTeamLevelLine> supTeamLine = agentTeamLevelLineMapper.getSupTeamLine(userId, null, 0);
        //需要删除的团队关系信息
        List<Long> deleteIds = new ArrayList<>();
        //清理当前用户的所有上级团队关系信息
        deleteIds.addAll(supTeamLine.stream().map(AgentTeamLevelLine::getId).collect(Collectors.toList()));
        //清理下级与当前用户旧上级的团队关系信息
        for (int i = 0; i < lowerTeamLine.size(); i++) {
            //此下级与当前用户的团队等级差
            Integer teamLevel = lowerTeamLine.get(i).getTeamLevel();
            //清理团队等级大于与当前用户的团队等级差的数据（下级与当前用户旧上级的团队关系信息）
            deleteIds.addAll(agentTeamLevelLineMapper.getSupTeamLine(lowerTeamLine.get(i).getUserId(), null, 0).stream().filter(a -> a.getTeamLevel() > teamLevel).map(AgentTeamLevelLine::getId).collect(Collectors.toList()));
        }
        //需要清理的数据数量
        int deleteCount = deleteIds.size();
        if (deleteCount > 0 ){
            //清理需要清理的数据
            int deleteAgentTeamLevelLineByIds = agentTeamLevelLineMapper.deleteAgentTeamLevelLineByIds(deleteIds.toArray(new Long[deleteCount]));
            if (deleteAgentTeamLevelLineByIds != deleteCount){
                throw new ServiceException("系统繁忙");
            }
        }

        //加入上级用户本身
        AgentTeamLevelLine supUserTeamLevelLine = new AgentTeamLevelLine();
        supUserTeamLevelLine.setSupUserId(supUserId);
        supUserTeamLevelLine.setTeamLevel(0);
        supTeamLineBySupUser.add(supUserTeamLevelLine);
        //当前时间
        Date nowDateTime = new Date();
        List<AgentTeamLevelLine> agentTeamLevelLines = new ArrayList<>();
        //遍历
        for (int i = 0; i < supTeamLineBySupUser.size(); i++) {
            //上级信息
            AgentTeamLevelLine sup = supTeamLineBySupUser.get(i);
            //获取新上级与原下级的团队关系信息
            for (int j = 0; j < lowerTeamLine.size(); j++) {
                //下级信息
                AgentTeamLevelLine low = lowerTeamLine.get(j);
                //new
                AgentTeamLevelLine vo = new AgentTeamLevelLine();
                vo.setUserId(low.getUserId());
                vo.setSupUserId(sup.getSupUserId());
                vo.setTeamLevel(low.getTeamLevel()+sup.getTeamLevel()+1);
                vo.setCreateTime(nowDateTime);
                agentTeamLevelLines.add(vo);
            }
            //获取新上级当前用户的团队关系信息
            AgentTeamLevelLine vo = new AgentTeamLevelLine();
            vo.setUserId(userId);
            vo.setSupUserId(sup.getSupUserId());
            vo.setTeamLevel(sup.getTeamLevel()+1);
            vo.setCreateTime(nowDateTime);
            agentTeamLevelLines.add(vo);
        }
        //插入新团队关系信息
        int insertAgentTeamLevelLines = agentTeamLevelLineMapper.insertAgentTeamLevelLines(agentTeamLevelLines);
        if (insertAgentTeamLevelLines != agentTeamLevelLines.size()){
            throw new ServiceException("系统繁忙");
        }
        return 1;
    }

    public void validate(){
        SysUser sysUser = new SysUser();
        sysUser.getParams().put("agentData",1);
        List<SysUser> sysUsers = sysUserMapper.selectUserList(sysUser);
        //用户信息map
        Map<Long, SysUser> userMap = sysUsers.stream().collect(Collectors.toMap(a -> a.getUserId(), a -> a));
        for (int i = 0; i < sysUsers.size(); i++) {
            //用户id
            Long userId = sysUsers.get(i).getUserId();
            //上级用户id
            Long supUserId = sysUsers.get(i).getSupUserId();
            //获取该用户的所有上级团队关系信息
            List<AgentTeamLevelLine> supTeamLine = agentTeamLevelLineMapper.getSupTeamLine(userId, null, 0);
            for (int j = 0; j < supTeamLine.size(); j++) {
                //上级用户id
                Long supUserIdValidate = supTeamLine.get(j).getSupUserId();
                if (!supUserId.equals(supUserIdValidate)){
                    throw new ServiceException("系统繁忙");
                }
                supUserId = userMap.get(supUserId).getSupUserId();
            }
        }
    }

    /**
     * 获取上级团队线
     * @param userId 用户id
     * @param queryLevel 查询等级
     * @return queryType 0:获取某一等级直接的所有 1:只获取某一等级
     */
    public List<AgentTeamLevelLine> getSupTeamLine(Long userId,Integer queryLevel,Integer queryType){
        return agentTeamLevelLineMapper.getSupTeamLine(userId,queryLevel,queryType);
    }

    /**
     * 获取下级团队线
     * @param supUserId 上级用户id
     * @param queryLevel 查询等级
     * @return queryType 0:获取某一等级直接的所有 1:只获取某一等级
     */
    public List<AgentTeamLevelLine> getLowerTeamLine(Long supUserId, Integer queryLevel,Integer queryType){
        return agentTeamLevelLineMapper.getLowerTeamLine(supUserId,queryLevel,queryType);
    }

    /**
     * 根据下级ids获取其最高级别代理团队关系信息
     * @param userIds 代理ids
     * @return
     */
    public List<AgentTeamLevelLine> selectMaxLevelAgentTeamLevelLineByUserIds(List<Long> userIds){
        return agentTeamLevelLineMapper.selectMaxLevelAgentTeamLevelLineByUserIds(userIds);
    }

}
