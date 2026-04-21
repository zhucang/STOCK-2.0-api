package com.ruoyi.system.service;

import com.ruoyi.system.domain.UserFastTradeControl;

import java.util.List;

/**
 * 极速交易用户单控参数Service接口
 * 
 * @author ruoyi
 * @date 2023-12-20
 */
public interface IUserFastTradeControlService 
{
    /**
     * 查询极速交易用户单控参数
     * 
     * @param id 极速交易用户单控参数主键
     * @return 极速交易用户单控参数
     */
    public UserFastTradeControl selectUserFastTradeControlById(Long id);

    /**
     * 查询极速交易用户单控参数列表
     * 
     * @param userFastTradeControl 极速交易用户单控参数
     * @return 极速交易用户单控参数集合
     */
    public List<UserFastTradeControl> selectUserFastTradeControlList(UserFastTradeControl userFastTradeControl);

    /**
     * 新增极速交易用户单控参数
     * 
     * @param userFastTradeControl 极速交易用户单控参数
     * @return 结果
     */
    public int insertUserFastTradeControl(UserFastTradeControl userFastTradeControl);

    /**
     * 修改极速交易用户单控参数
     * 
     * @param userFastTradeControl 极速交易用户单控参数
     * @return 结果
     */
    public int updateUserFastTradeControl(UserFastTradeControl userFastTradeControl);

    /**
     * 批量删除极速交易用户单控参数
     * 
     * @param ids 需要删除的极速交易用户单控参数主键集合
     * @return 结果
     */
    public int deleteUserFastTradeControlByIds(Long[] ids);

    /**
     * 删除极速交易用户单控参数信息
     * 
     * @param id 极速交易用户单控参数主键
     * @return 结果
     */
    public int deleteUserFastTradeControlById(Long id);
}
