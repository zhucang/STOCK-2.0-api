package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.AgentTeamLevelLine;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 代理团队关系网（代理代理线）Mapper接口
 * 
 * @author ruoyi
 * @date 2023-11-19
 */
public interface AgentTeamLevelLineMapper 
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
     * 批量新增代理团队关系网（代理代理线）
     *
     * @param agentTeamLevelLines 代理团队关系网List（用户代理线）
     * @return 结果
     */
    public int insertAgentTeamLevelLines(@Param("list") List<AgentTeamLevelLine> agentTeamLevelLines);

    /**
     * 修改代理团队关系网（代理代理线）
     * 
     * @param agentTeamLevelLine 代理团队关系网（代理代理线）
     * @return 结果
     */
    public int updateAgentTeamLevelLine(AgentTeamLevelLine agentTeamLevelLine);

    /**
     * 删除代理团队关系网（代理代理线）
     * 
     * @param id 代理团队关系网（代理代理线）主键
     * @return 结果
     */
    public int deleteAgentTeamLevelLineById(Long id);

    /**
     * 批量删除代理团队关系网（代理代理线）
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAgentTeamLevelLineByIds(Long[] ids);

    /**
     * 获取上级团队线
     * @param userId 用户id
     * @param queryLevel 查询等级
     * @return queryType 0:获取某一等级直接的所有 1:只获取某一等级
     */
    public List<AgentTeamLevelLine> getSupTeamLine(@Param("userId") Long userId, @Param("queryLevel") Integer queryLevel, @Param("queryType")Integer queryType);

    /**
     * 获取下级团队线
     * @param supUserId 上级用户id
     * @param queryLevel 查询等级
     * @return queryType 0:获取某一等级直接的所有 1:只获取某一等级
     */
    public List<AgentTeamLevelLine> getLowerTeamLine(@Param("supUserId") Long supUserId, @Param("queryLevel") Integer queryLevel, @Param("queryType")Integer queryType);

    /**
     * 清空某用户的团队关系网
     * @param userId 用户id
     * @return
     */
    int cleanAgentTeamLevelLineByUserId(Long userId);

    int cleanSupAgentTeamLevelLineByUserId(Long userId);

    /**
     * 衔接上下级团队
     * @param lowerTeamUserIds 下级团队ids
     * @param supTeamUserIds 上级团队ids
     * @return
     */
    int connectUpperAndLowerTeam(@Param("lowerTeamUserIds") List<Long> lowerTeamUserIds,@Param("supTeamUserIds") List<Long> supTeamUserIds);

    /**
     * 根据下级ids获取其最高级别代理团队关系信息
     * @param userIds 代理ids
     * @return
     */
    public List<AgentTeamLevelLine> selectMaxLevelAgentTeamLevelLineByUserIds(@Param("userIds") List<Long> userIds);
}
