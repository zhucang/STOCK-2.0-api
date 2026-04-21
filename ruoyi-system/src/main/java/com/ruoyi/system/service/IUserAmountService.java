package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.UserAmount;
import com.ruoyi.system.domain.PlatformCurrency;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户各币种余额Service接口
 * 
 * @author ruoyi
 * @date 2023-10-28
 */
public interface IUserAmountService 
{
    /**
     * 查询用户各币种余额
     * 
     * @param id 用户各币种余额主键
     * @return 用户各币种余额
     */
    public UserAmount selectUserAmountById(Long id);

    /**
     * 查询用户各币种余额列表
     * 
     * @param userAmount 用户各币种余额
     * @return 用户各币种余额集合
     */
    public List<UserAmount> selectUserAmountList(UserAmount userAmount);

    /**
     * 查询用户各货币余额信息列表
     *
     * @param userId 用户id
     * @return 用户各货币余额信息集合
     */
    public List<UserAmount> selectUserAmountListByUserId(Long userId,List<PlatformCurrency> platformCurrencies);

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
     * 批量删除用户各币种余额
     * 
     * @param ids 需要删除的用户各币种余额主键集合
     * @return 结果
     */
    public int deleteUserAmountByIds(Long[] ids);

    /**
     * 删除用户各币种余额信息
     * 
     * @param id 用户各币种余额主键
     * @return 结果
     */
    public int deleteUserAmountById(Long id);

    /**
     * 获取用户某币种余额
     * @param userId 用户id
     * @param currencyId 货币id
     */
    UserAmount getUserAmount(Long userId, Long currencyId);

    /**
     * 转入/转出灵活投资资金
     * @param transferType 转移类型 0：转入 1：转出
     * @param transferAmount 转移金额
     * @param currencyId 币种
     * @return
     */
    public int transferFlexibleInvestmentFunds(Integer transferType, BigDecimal transferAmount, Long currencyId);
}
