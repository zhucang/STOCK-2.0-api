package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.UserFastTradeControl;

import java.util.List;

/**
 * 极速交易用户单控参数Mapper接口
 * 
 * @author ruoyi
 * @date 2023-12-20
 */
public interface UserFastTradeControlMapper 
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
     * 删除极速交易用户单控参数
     * 
     * @param id 极速交易用户单控参数主键
     * @return 结果
     */
    public int deleteUserFastTradeControlById(Long id);

    /**
     * 批量删除极速交易用户单控参数
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserFastTradeControlByIds(Long[] ids);
}
