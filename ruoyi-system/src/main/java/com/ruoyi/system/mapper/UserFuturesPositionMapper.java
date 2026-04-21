package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.UserFuturesPosition;

import java.util.List;

/**
 * 用户期货持仓Mapper接口
 * 
 * @author ruoyi
 * @date 2023-11-05
 */
public interface UserFuturesPositionMapper 
{
    /**
     * 查询用户期货持仓
     * 
     * @param id 用户期货持仓主键
     * @return 用户期货持仓
     */
    public UserFuturesPosition selectUserFuturesPositionById(Long id);

    /**
     * 查询用户期货持仓列表
     * 
     * @param userFuturesPosition 用户期货持仓
     * @return 用户期货持仓集合
     */
    public List<UserFuturesPosition> selectUserFuturesPositionList(UserFuturesPosition userFuturesPosition);

    /**
     * 新增用户期货持仓
     * 
     * @param userFuturesPosition 用户期货持仓
     * @return 结果
     */
    public int insertUserFuturesPosition(UserFuturesPosition userFuturesPosition);

    /**
     * 修改用户期货持仓
     * 
     * @param userFuturesPosition 用户期货持仓
     * @return 结果
     */
    public int updateUserFuturesPosition(UserFuturesPosition userFuturesPosition);

    /**
     * 删除用户期货持仓
     * 
     * @param id 用户期货持仓主键
     * @return 结果
     */
    public int deleteUserFuturesPositionById(Long id);

    /**
     * 批量删除用户期货持仓
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserFuturesPositionByIds(Long[] ids);
}
