package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.UserForexPosition;

import java.util.List;

/**
 * 用户外汇持仓Mapper接口
 * 
 * @author ruoyi
 * @date 2023-11-05
 */
public interface UserForexPositionMapper 
{
    /**
     * 查询用户外汇持仓
     * 
     * @param id 用户外汇持仓主键
     * @return 用户外汇持仓
     */
    public UserForexPosition selectUserForexPositionById(Long id);

    /**
     * 查询用户外汇持仓列表
     * 
     * @param userForexPosition 用户外汇持仓
     * @return 用户外汇持仓集合
     */
    public List<UserForexPosition> selectUserForexPositionList(UserForexPosition userForexPosition);

    /**
     * 新增用户外汇持仓
     * 
     * @param userForexPosition 用户外汇持仓
     * @return 结果
     */
    public int insertUserForexPosition(UserForexPosition userForexPosition);

    /**
     * 修改用户外汇持仓
     * 
     * @param userForexPosition 用户外汇持仓
     * @return 结果
     */
    public int updateUserForexPosition(UserForexPosition userForexPosition);

    /**
     * 删除用户外汇持仓
     * 
     * @param id 用户外汇持仓主键
     * @return 结果
     */
    public int deleteUserForexPositionById(Long id);

    /**
     * 批量删除用户外汇持仓
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserForexPositionByIds(Long[] ids);
}
