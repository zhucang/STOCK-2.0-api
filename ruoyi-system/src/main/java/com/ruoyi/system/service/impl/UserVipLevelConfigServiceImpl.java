package com.ruoyi.system.service.impl;


import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.UserVipLevelConfig;
import com.ruoyi.system.mapper.UserVipLevelConfigMapper;
import com.ruoyi.system.service.IUserVipLevelConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;


/**
 * 用户vip等级升级配置Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-10-09
 */
@Service
public class UserVipLevelConfigServiceImpl implements IUserVipLevelConfigService
{
    @Resource
    private UserVipLevelConfigMapper userVipLevelConfigMapper;

    /**
     * 查询用户vip等级升级配置
     * 
     * @param id 用户vip等级升级配置主键
     * @return 用户vip等级升级配置
     */
    @Override
    public UserVipLevelConfig selectUserVipLevelConfigById(Long id)
    {
        return userVipLevelConfigMapper.selectUserVipLevelConfigById(id);
    }

    /**
     * 查询用户vip等级升级配置
     *
     * @param vipLevel vip等级
     * @return 用户vip等级升级配置
     */
    @Override
    public UserVipLevelConfig selectUserVipLevelConfigByVipLevel(Integer vipLevel){
        return userVipLevelConfigMapper.selectUserVipLevelConfigByVipLevel(vipLevel);
    }

    /**
     * 查询用户vip等级升级配置
     *
     * @param rechargeAmount 充值金额
     * @return 用户vip等级升级配置
     */
    public UserVipLevelConfig selectUserVipLevelConfigByRechargeAmount(BigDecimal rechargeAmount){
        return userVipLevelConfigMapper.selectUserVipLevelConfigByRechargeAmount(rechargeAmount);
    }

    /**
     * 查询用户vip等级升级配置列表
     * 
     * @param userVipLevelConfig 用户vip等级升级配置
     * @return 用户vip等级升级配置
     */
    @Override
    public List<UserVipLevelConfig> selectUserVipLevelConfigList(UserVipLevelConfig userVipLevelConfig)
    {
        return userVipLevelConfigMapper.selectUserVipLevelConfigList(userVipLevelConfig);
    }

    /**
     * 新增用户vip等级升级配置
     * 
     * @param userVipLevelConfig 用户vip等级升级配置
     * @return 结果
     */
    @Override
    public AjaxResult insertUserVipLevelConfig(UserVipLevelConfig userVipLevelConfig)
    {
        //vip等级
        Integer vipLevel = userVipLevelConfig.getVipLevel();
        UserVipLevelConfig userVipLevelConfigVo = userVipLevelConfigMapper.selectUserVipLevelConfigByVipLevel(vipLevel);
        if (userVipLevelConfigVo != null){
            return AjaxResult.error("该会员等级已存在");
        }
        int count = userVipLevelConfigMapper.insertUserVipLevelConfig(userVipLevelConfig);
        if (count <= 0){
            return AjaxResult.error("系统繁忙");
        }
        return AjaxResult.success();
    }

    /**
     * 修改用户vip等级升级配置
     * 
     * @param userVipLevelConfig 用户vip等级升级配置
     * @return 结果
     */
    @Override
    public AjaxResult updateUserVipLevelConfig(UserVipLevelConfig userVipLevelConfig)
    {
        //vip等级
        Integer vipLevel = userVipLevelConfig.getVipLevel();
        UserVipLevelConfig userVipLevelConfigVo = userVipLevelConfigMapper.selectUserVipLevelConfigByVipLevel(vipLevel);
        if (userVipLevelConfigVo != null && !userVipLevelConfigVo.getId().equals(userVipLevelConfig.getId())){
            return AjaxResult.error("该会员等级已存在");
        }
        int count = userVipLevelConfigMapper.updateUserVipLevelConfig(userVipLevelConfig);
        if (count <= 0){
            return AjaxResult.error("系统繁忙");
        }
        return AjaxResult.success();
    }

    /**
     * 批量删除用户vip等级升级配置
     * 
     * @param ids 需要删除的用户vip等级升级配置主键
     * @return 结果
     */
    @Override
    public int deleteUserVipLevelConfigByIds(Long[] ids)
    {
        return userVipLevelConfigMapper.deleteUserVipLevelConfigByIds(ids);
    }

    /**
     * 删除用户vip等级升级配置信息
     * 
     * @param id 用户vip等级升级配置主键
     * @return 结果
     */
    @Override
    public int deleteUserVipLevelConfigById(Long id)
    {
        return userVipLevelConfigMapper.deleteUserVipLevelConfigById(id);
    }
}
