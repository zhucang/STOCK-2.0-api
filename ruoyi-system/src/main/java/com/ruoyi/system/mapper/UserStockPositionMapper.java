package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.UserStockPosition;

import java.util.List;

/**
 * 用户股票持仓Mapper接口
 * 
 * @author ruoyi
 * @date 2023-11-05
 */
public interface UserStockPositionMapper 
{
    /**
     * 查询用户股票持仓
     * 
     * @param id 用户股票持仓主键
     * @return 用户股票持仓
     */
    public UserStockPosition selectUserStockPositionById(Long id);

    /**
     * 查询用户股票持仓列表
     * 
     * @param userStockPosition 用户股票持仓
     * @return 用户股票持仓集合
     */
    public List<UserStockPosition> selectUserStockPositionList(UserStockPosition userStockPosition);

    /**
     * 查询用户所有产品持仓列表
     *
     * @param userStockPosition 用户股票持仓
     * @return 用户股票持仓集合
     */
    public List<UserStockPosition> selectUserAllPositionList(UserStockPosition userStockPosition);

    /**
     * 新增用户股票持仓
     * 
     * @param userStockPosition 用户股票持仓
     * @return 结果
     */
    public int insertUserStockPosition(UserStockPosition userStockPosition);

    /**
     * 修改用户股票持仓
     * 
     * @param userStockPosition 用户股票持仓
     * @return 结果
     */
    public int updateUserStockPosition(UserStockPosition userStockPosition);

    /**
     * 删除用户股票持仓
     * 
     * @param id 用户股票持仓主键
     * @return 结果
     */
    public int deleteUserStockPositionById(Long id);

    /**
     * 批量删除用户股票持仓
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserStockPositionByIds(Long[] ids);
}
