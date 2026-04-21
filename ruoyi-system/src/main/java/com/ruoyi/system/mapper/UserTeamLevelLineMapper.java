package com.ruoyi.system.mapper;


import com.ruoyi.system.domain.UserTeamLevelLine;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户团队关系网（用户代理线）Mapper接口
 * 
 * @author ruoyi
 * @date 2023-09-09
 */
public interface UserTeamLevelLineMapper 
{
    /**
     * 查询用户团队关系网（用户代理线）
     * 
     * @param id 用户团队关系网（用户代理线）主键
     * @return 用户团队关系网（用户代理线）
     */
    public UserTeamLevelLine selectUserTeamLevelLineById(Long id);

    /**
     * 查询用户团队关系网（用户代理线）列表
     * 
     * @param userTeamLevelLine 用户团队关系网（用户代理线）
     * @return 用户团队关系网（用户代理线）集合
     */
    public List<UserTeamLevelLine> selectUserTeamLevelLineList(UserTeamLevelLine userTeamLevelLine);

    /**
     * 新增用户团队关系网（用户代理线）
     * 
     * @param userTeamLevelLine 用户团队关系网（用户代理线）
     * @return 结果
     */
    public int insertUserTeamLevelLine(UserTeamLevelLine userTeamLevelLine);

    /**
     * 批量新增用户团队关系网（用户代理线）
     *
     * @param userTeamLevelLines 用户团队关系网List（用户代理线）
     * @return 结果
     */
    public int insertUserTeamLevelLines(@Param("list") List<UserTeamLevelLine> userTeamLevelLines);

    /**
     * 修改用户团队关系网（用户代理线）
     * 
     * @param userTeamLevelLine 用户团队关系网（用户代理线）
     * @return 结果
     */
    public int updateUserTeamLevelLine(UserTeamLevelLine userTeamLevelLine);

    /**
     * 删除用户团队关系网（用户代理线）
     * 
     * @param id 用户团队关系网（用户代理线）主键
     * @return 结果
     */
    public int deleteUserTeamLevelLineById(Long id);

    /**
     * 批量删除用户团队关系网（用户代理线）
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserTeamLevelLineByIds(Long[] ids);


    /**
     * 获取上级团队线
     * @param userId 用户id
     * @param queryLevel 查询等级
     * @return queryType 0:获取某一等级直接的所有 1:只获取某一等级
     */
    public List<UserTeamLevelLine> getSupTeamLine(@Param("userId") Long userId,@Param("queryLevel") Integer queryLevel,@Param("queryType")Integer queryType);

    /**
     * 获取下级团队线
     * @param supUserId 上级用户id
     * @param queryLevel 查询等级
     * @return queryType 0:获取某一等级直接的所有 1:只获取某一等级
     */
    public List<UserTeamLevelLine> getLowerTeamLine(@Param("supUserId") Long supUserId,@Param("queryLevel") Integer queryLevel,@Param("queryType")Integer queryType);

    /**
     * 清空某用户的团队关系网
     * @param userId 用户id
     * @return
     */
    int cleanUserTeamLevelLineByUserId(Long userId);

    /**
     * 衔接上下级团队
     * @param lowerTeamUserIds 下级团队ids
     * @param supTeamUserIds 上级团队ids
     * @return
     */
    int connectUpperAndLowerTeam(@Param("lowerTeamUserIds") List<Long> lowerTeamUserIds,@Param("supTeamUserIds") List<Long> supTeamUserIds);
}
