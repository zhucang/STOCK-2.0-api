package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.UserCryptocurrencyPosition;

import java.util.List;

/**
 * 用户加密货币持仓Mapper接口
 * 
 * @author ruoyi
 * @date 2023-11-05
 */
public interface UserCryptocurrencyPositionMapper 
{
    /**
     * 查询用户加密货币持仓
     * 
     * @param id 用户加密货币持仓主键
     * @return 用户加密货币持仓
     */
    public UserCryptocurrencyPosition selectUserCryptocurrencyPositionById(Long id);

    /**
     * 查询用户加密货币持仓列表
     * 
     * @param userCryptocurrencyPosition 用户加密货币持仓
     * @return 用户加密货币持仓集合
     */
    public List<UserCryptocurrencyPosition> selectUserCryptocurrencyPositionList(UserCryptocurrencyPosition userCryptocurrencyPosition);

    /**
     * 新增用户加密货币持仓
     * 
     * @param userCryptocurrencyPosition 用户加密货币持仓
     * @return 结果
     */
    public int insertUserCryptocurrencyPosition(UserCryptocurrencyPosition userCryptocurrencyPosition);

    /**
     * 修改用户加密货币持仓
     * 
     * @param userCryptocurrencyPosition 用户加密货币持仓
     * @return 结果
     */
    public int updateUserCryptocurrencyPosition(UserCryptocurrencyPosition userCryptocurrencyPosition);

    /**
     * 删除用户加密货币持仓
     * 
     * @param id 用户加密货币持仓主键
     * @return 结果
     */
    public int deleteUserCryptocurrencyPositionById(Long id);

    /**
     * 批量删除用户加密货币持仓
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserCryptocurrencyPositionByIds(Long[] ids);
}
