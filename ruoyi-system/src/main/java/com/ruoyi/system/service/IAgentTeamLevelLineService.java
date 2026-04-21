package com.ruoyi.system.service;

import com.ruoyi.system.domain.AgentTeamLevelLine;

import java.util.List;

/**
 * 代理团队关系网（代理代理线）Service接口
 * 
 * @author ruoyi
 * @date 2023-11-19
 */
public interface IAgentTeamLevelLineService 
{
    /**
     * 查询代理团队关系网（代理代理线）
     * 
     * @param id 代理团队关系网（代理代理线）主键
     * @return 代理团队关系网（代理代理线）
     */
    public AgentTeamLevelLine selectAgentTeamLevelLineById(Long id);

    /**
     * 查询代理团队关系网（代理代理线）列表
     * 
     * @param agentTeamLevelLine 代理团队关系网（代理代理线）
     * @return 代理团队关系网（代理代理线）集合
     */
    public List<AgentTeamLevelLine> selectAgentTeamLevelLineList(AgentTeamLevelLine agentTeamLevelLine);

    /**
     * 新增代理团队关系网（代理代理线）
     * 
     * @param agentTeamLevelLine 代理团队关系网（代理代理线）
     * @return 结果
     */
    public int insertAgentTeamLevelLine(AgentTeamLevelLine agentTeamLevelLine);

    /**
     * 修改代理团队关系网（代理代理线）
     * 
     * @param agentTeamLevelLine 代理团队关系网（代理代理线）
     * @return 结果
     */
    public int updateAgentTeamLevelLine(AgentTeamLevelLine agentTeamLevelLine);

    /**
     * 批量删除代理团队关系网（代理代理线）
     * 
     * @param ids 需要删除的代理团队关系网（代理代理线）主键集合
     * @return 结果
     */
    public int deleteAgentTeamLevelLineByIds(Long[] ids);

    /**
     * 删除代理团队关系网（代理代理线）信息
     * 
     * @param id 代理团队关系网（代理代理线）主键
     * @return 结果
     */
    public int deleteAgentTeamLevelLineById(Long id);

    /**
     * 更新团队等级关系
     * @param userId 用户id
     * @param supUserId 上级用户id
     * @param updateType 更新类型：0：用户新增更新  1：用户删除更新 2：变更代理线更新
     * @return
     */
    public int updateAgentTeamLevelLine(Long userId,Long supUserId,int updateType);

    /**
     * 获取上级团队线
     * @param userId 用户id
     * @param queryLevel 查询等级
     * @return queryType 0:获取某一等级直接的所有 1:只获取某一等级
     */
    public List<AgentTeamLevelLine> getSupTeamLine(Long userId,Integer queryLevel,Integer queryType);

    /**
     * 获取下级团队线
     * @param supUserId 上级用户id
     * @param queryLevel 查询等级
     * @return queryType 0:获取某一等级直接的所有 1:只获取某一等级
     */
    public List<AgentTeamLevelLine> getLowerTeamLine(Long supUserId, Integer queryLevel,Integer queryType);

    /**
     * 根据下级ids获取其最高级别代理团队关系信息
     * @param userIds 代理ids
     * @return
     */
    public List<AgentTeamLevelLine> selectMaxLevelAgentTeamLevelLineByUserIds(List<Long> userIds);

}
