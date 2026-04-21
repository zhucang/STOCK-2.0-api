package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.UserRebateRate;

import java.util.List;

/**
 * 用户返佣比率配置Service接口
 * 
 * @author ruoyi
 * @date 2023-11-05
 */
public interface IUserRebateRateService 
{
    /**
     * 查询用户返佣比率配置
     * 
     * @param id 用户返佣比率配置主键
     * @return 用户返佣比率配置
     */
    public UserRebateRate selectUserRebateRateById(Long id);

    /**
     * 查询用户返佣比率配置列表
     * 
     * @param userRebateRate 用户返佣比率配置
     * @return 用户返佣比率配置集合
     */
    public List<UserRebateRate> selectUserRebateRateList(UserRebateRate userRebateRate);

    /**
     * 新增用户返佣比率配置
     * 
     * @param userRebateRate 用户返佣比率配置
     * @return 结果
     */
    public AjaxResult insertUserRebateRate(UserRebateRate userRebateRate);

    /**
     * 修改用户返佣比率配置
     * 
     * @param userRebateRate 用户返佣比率配置
     * @return 结果
     */
    public AjaxResult updateUserRebateRate(UserRebateRate userRebateRate);

    /**
     * 批量删除用户返佣比率配置
     * 
     * @param ids 需要删除的用户返佣比率配置主键集合
     * @return 结果
     */
    public int deleteUserRebateRateByIds(Long[] ids);

    /**
     * 删除用户返佣比率配置信息
     * 
     * @param id 用户返佣比率配置主键
     * @return 结果
     */
    public int deleteUserRebateRateById(Long id);
}
