package com.ruoyi.system.service;


import com.ruoyi.common.core.domain.entity.UserInfo;
import com.ruoyi.system.domain.UserTeamLevelLine;

import java.util.List;
import java.util.Map;

/**
 * 用户团队关系网（用户代理线）Service接口
 * 
 * @author ruoyi
 * @date 2023-09-09
 */
public interface IUserTeamLevelLineService 
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
     * 修改用户团队关系网（用户代理线）
     * 
     * @param userTeamLevelLine 用户团队关系网（用户代理线）
     * @return 结果
     */
    public int updateUserTeamLevelLine(UserTeamLevelLine userTeamLevelLine);

    /**
     * 批量删除用户团队关系网（用户代理线）
     * 
     * @param ids 需要删除的用户团队关系网（用户代理线）主键集合
     * @return 结果
     */
    public int deleteUserTeamLevelLineByIds(Long[] ids);

    /**
     * 删除用户团队关系网（用户代理线）信息
     * 
     * @param id 用户团队关系网（用户代理线）主键
     * @return 结果
     */
    public int deleteUserTeamLevelLineById(Long id);

    /**
     * 更新团队等级关系
     * @param userId 用户id
     * @param supUserId 上级用户id
     * @param updateType 更新类型：0：用户新增更新  1：用户删除更新 2：变更代理线更新
     * @return
     */
    public int updateUserTeamLevelLine(Long userId,Long supUserId,int updateType);

    /**
     * 查询下级团队信息
     * @param userTeamLevelLine
     * @return
     */
    List<UserInfo> queryLowerTeamInfo(UserTeamLevelLine userTeamLevelLine);

    /**
     * 我的团队数据仪表板
     * @param userTeamLevelLine 团队等级关系
     * @return
     */
    Map<String,Object> myTeamDataDashboard(UserTeamLevelLine userTeamLevelLine);


}
