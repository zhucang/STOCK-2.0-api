package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.UserAmount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户各币种余额Mapper接口
 * 
 * @author ruoyi
 * @date 2023-10-28
 */
public interface UserAmountMapper 
{
    /**
     * 查询用户各币种余额
     * 
     * @param id 用户各币种余额主键
     * @return 用户各币种余额
     */
    public UserAmount selectUserAmountById(Long id);

    /**
     * 查询用户各货币余额信息
     *
     * @param userId 用户id
     * @param currencyId 货币id
     * @return 用户各货币余额信息
     */
    public UserAmount getUserAmount(@Param("userId") Long userId, @Param("currencyId") Long currencyId);

    /**
     * 查询用户各币种余额列表
     * 
     * @param userAmount 用户各币种余额
     * @return 用户各币种余额集合
     */
    public List<UserAmount> selectUserAmountList(UserAmount userAmount);

    /**
     * 新增用户各币种余额
     * 
     * @param userAmount 用户各币种余额
     * @return 结果
     */
    public int insertUserAmount(UserAmount userAmount);

    /**
     * 修改用户各币种余额
     * 
     * @param userAmount 用户各币种余额
     * @return 结果
     */
    public int updateUserAmount(UserAmount userAmount);

    /**
     * 删除用户各币种余额
     * 
     * @param id 用户各币种余额主键
     * @return 结果
     */
    public int deleteUserAmountById(Long id);

    /**
     * 批量删除用户各币种余额
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserAmountByIds(Long[] ids);
}
