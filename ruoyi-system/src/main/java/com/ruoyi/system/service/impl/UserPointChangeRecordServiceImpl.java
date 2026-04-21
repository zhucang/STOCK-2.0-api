package com.ruoyi.system.service.impl;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.AgentTeamLevelLine;
import com.ruoyi.system.domain.UserPointChangeRecord;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.mapper.UserPointChangeRecordMapper;
import com.ruoyi.system.service.IAgentTeamLevelLineService;
import com.ruoyi.system.service.IUserPointChangeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户上分下分记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-03-30
 */
@Service
public class UserPointChangeRecordServiceImpl implements IUserPointChangeRecordService 
{
    @Resource
    private UserPointChangeRecordMapper userPointChangeRecordMapper;

    @Autowired
    private IAgentTeamLevelLineService agentTeamLevelLineService;

    @Resource
    private SysUserMapper sysUserMapper;

    /**
     * 查询用户上分下分记录
     * 
     * @param id 用户上分下分记录主键
     * @return 用户上分下分记录
     */
    @Override
    public UserPointChangeRecord selectUserPointChangeRecordById(Long id)
    {
        return userPointChangeRecordMapper.selectUserPointChangeRecordById(id);
    }

    /**
     * 查询用户上分下分记录列表
     * 
     * @param userPointChangeRecord 用户上分下分记录
     * @return 用户上分下分记录
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u", isUserInfo = true)
    public List<UserPointChangeRecord> selectUserPointChangeRecordList(UserPointChangeRecord userPointChangeRecord)
    {
        return userPointChangeRecordMapper.selectUserPointChangeRecordList(userPointChangeRecord);
    }

    /**
     * 获取统计数据
     * @param userPointChangeRecord
     * @return
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u", isUserInfo = true)
    public List<UserPointChangeRecord> getStatisticalData(UserPointChangeRecord userPointChangeRecord){
        return userPointChangeRecordMapper.getStatisticalData(userPointChangeRecord);
    }

    /**
     * 填充其他信息
     * @param userPointChangeRecords 用户上分下分记录列表
     */
    @Override
    public void fillOtherInfo(List<UserPointChangeRecord> userPointChangeRecords){
        fillAgentLine(userPointChangeRecords);
    }

    /**
     * 填充代理线
     * @param userPointChangeRecords 用户上分下分记录列表
     */

    public void fillAgentLine(List<UserPointChangeRecord> userPointChangeRecords){
        //用户的代理集合
        List<Long> agentIds = userPointChangeRecords.stream().map(UserPointChangeRecord::getAgentId).distinct().collect(Collectors.toList());
        //取这些代理各自的最高级别代理
        List<AgentTeamLevelLine> agentTeamLevelLines = agentTeamLevelLineService.selectMaxLevelAgentTeamLevelLineByUserIds(agentIds);
        //上级团队信息map
        Map<Long, AgentTeamLevelLine> agentTeamMap = agentTeamLevelLines.stream().collect(Collectors.toMap(a->a.getUserId(), a->a));
        //获取这些代理的信息
        agentIds.addAll(agentTeamLevelLines.stream().map(AgentTeamLevelLine::getSupUserId).distinct().collect(Collectors.toList()));
        SysUser sysUser = new SysUser();
        sysUser.getParams().put("userIds",agentIds);
        sysUser.getParams().put("agentData",1);
        Map<Long, SysUser> agentUsersMap = sysUserMapper.selectUserList(sysUser).stream().collect(Collectors.toMap(a -> a.getUserId(), a -> a));
        //遍历
        for (int i = 0; i < userPointChangeRecords.size(); i++) {
            //用户充值订单信息
            UserPointChangeRecord userPointChangeRecord = userPointChangeRecords.get(i);
            //代理id
            Long agentId = userPointChangeRecord.getAgentId();
            //代理线
            if (agentUsersMap.get(agentId) == null){
                //如果代理信息不存在
                continue;
            }
            String agentLine = agentUsersMap.get(agentId).getUserName();
            //代理的上级信息
            AgentTeamLevelLine line = agentTeamMap.get(agentId);
            if (line != null){
                SysUser agentUser = agentUsersMap.get(line.getSupUserId());
                if (agentUser != null){
                    agentLine = agentUser.getUserName() + "——" + agentLine;
                }
            }
            userPointChangeRecord.setAgentName(agentLine);
        }
    }

    /**
     * 新增用户上分下分记录
     * 
     * @param userPointChangeRecord 用户上分下分记录
     * @return 结果
     */
    @Override
    public int insertUserPointChangeRecord(UserPointChangeRecord userPointChangeRecord)
    {
        userPointChangeRecord.setCreateTime(DateUtils.getNowDate());
        return userPointChangeRecordMapper.insertUserPointChangeRecord(userPointChangeRecord);
    }

    /**
     * 修改用户上分下分记录
     * 
     * @param userPointChangeRecord 用户上分下分记录
     * @return 结果
     */
    @Override
    public int updateUserPointChangeRecord(UserPointChangeRecord userPointChangeRecord)
    {
        return userPointChangeRecordMapper.updateUserPointChangeRecord(userPointChangeRecord);
    }

    /**
     * 批量删除用户上分下分记录
     * 
     * @param ids 需要删除的用户上分下分记录主键
     * @return 结果
     */
    @Override
    public int deleteUserPointChangeRecordByIds(Long[] ids)
    {
        return userPointChangeRecordMapper.deleteUserPointChangeRecordByIds(ids);
    }

    /**
     * 删除用户上分下分记录信息
     * 
     * @param id 用户上分下分记录主键
     * @return 结果
     */
    @Override
    public int deleteUserPointChangeRecordById(Long id)
    {
        return userPointChangeRecordMapper.deleteUserPointChangeRecordById(id);
    }

    /**
     * 获取用户的各币种的上下分金额
     * @param userId 用户id
     * @param orderType 订单类型 0：上分 1：下分
     * @return
     */
    @Override
    public List<UserPointChangeRecord> selectUserPointChangeAmountAllCurrencyByUserId(Long userId,Integer orderType){
        return userPointChangeRecordMapper.selectUserPointChangeAmountAllCurrencyByUserId(userId,orderType);
    }
}
