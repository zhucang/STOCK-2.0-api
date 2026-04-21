package com.ruoyi.system.service;


import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.UserVipLevelConfig;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户vip等级升级配置Service接口
 * 
 * @author ruoyi
 * @date 2023-10-09
 */
public interface IUserVipLevelConfigService 
{
    /**
     * 查询用户vip等级升级配置
     * 
     * @param id 用户vip等级升级配置主键
     * @return 用户vip等级升级配置
     */
    public UserVipLevelConfig selectUserVipLevelConfigById(Long id);

    /**
     * 查询用户vip等级升级配置
     *
     * @param vipLevel vip等级
     * @return 用户vip等级升级配置
     */
    public UserVipLevelConfig selectUserVipLevelConfigByVipLevel(Integer vipLevel);

    /**
     * 查询用户vip等级升级配置
     *
     * @param rechargeAmount 充值金额
     * @return 用户vip等级升级配置
     */
    public UserVipLevelConfig selectUserVipLevelConfigByRechargeAmount(BigDecimal rechargeAmount);

    /**
     * 查询用户vip等级升级配置列表
     * 
     * @param userVipLevelConfig 用户vip等级升级配置
     * @return 用户vip等级升级配置集合
     */
    public List<UserVipLevelConfig> selectUserVipLevelConfigList(UserVipLevelConfig userVipLevelConfig);

    /**
     * 新增用户vip等级升级配置
     * 
     * @param userVipLevelConfig 用户vip等级升级配置
     * @return 结果
     */
    public AjaxResult insertUserVipLevelConfig(UserVipLevelConfig userVipLevelConfig);

    /**
     * 修改用户vip等级升级配置
     * 
     * @param userVipLevelConfig 用户vip等级升级配置
     * @return 结果
     */
    public AjaxResult updateUserVipLevelConfig(UserVipLevelConfig userVipLevelConfig);

    /**
     * 批量删除用户vip等级升级配置
     * 
     * @param ids 需要删除的用户vip等级升级配置主键集合
     * @return 结果
     */
    public int deleteUserVipLevelConfigByIds(Long[] ids);

    /**
     * 删除用户vip等级升级配置信息
     * 
     * @param id 用户vip等级升级配置主键
     * @return 结果
     */
    public int deleteUserVipLevelConfigById(Long id);
}
