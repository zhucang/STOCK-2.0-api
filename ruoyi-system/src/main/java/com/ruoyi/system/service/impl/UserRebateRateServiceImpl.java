package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.UserRebateRate;
import com.ruoyi.system.mapper.UserRebateRateMapper;
import com.ruoyi.system.service.IUserRebateRateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 用户返佣比率配置Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-11-05
 */
@Service
public class UserRebateRateServiceImpl implements IUserRebateRateService 
{
    @Resource
    private UserRebateRateMapper userRebateRateMapper;

    /**
     * 查询用户返佣比率配置
     * 
     * @param id 用户返佣比率配置主键
     * @return 用户返佣比率配置
     */
    @Override
    public UserRebateRate selectUserRebateRateById(Long id)
    {
        return userRebateRateMapper.selectUserRebateRateById(id);
    }

    /**
     * 查询用户返佣比率配置列表
     * 
     * @param userRebateRate 用户返佣比率配置
     * @return 用户返佣比率配置
     */
    @Override
    public List<UserRebateRate> selectUserRebateRateList(UserRebateRate userRebateRate)
    {
        return userRebateRateMapper.selectUserRebateRateList(userRebateRate);
    }

    /**
     * 新增用户返佣比率配置
     * 
     * @param userRebateRate 用户返佣比率配置
     * @return 结果
     */
    @Override
    public AjaxResult insertUserRebateRate(UserRebateRate userRebateRate)
    {
        UserRebateRate userRebateRateVo = new UserRebateRate();
        userRebateRateVo.setRebateType(userRebateRate.getRebateType());
        userRebateRateVo.setRebateLevel(userRebateRate.getRebateLevel());
        userRebateRateVo = userRebateRateMapper.selectUserRebateRate(userRebateRateVo);
        if (userRebateRateVo != null){
            return AjaxResult.error("该返佣类型已经有此等级的返佣配置");
        }
        userRebateRate.setCreateTime(DateUtils.getNowDate());
        int count = userRebateRateMapper.insertUserRebateRate(userRebateRate);
        if (count <= 0){
            return AjaxResult.error("系统繁忙");
        }
        return AjaxResult.success();
    }

    /**
     * 修改用户返佣比率配置
     * 
     * @param userRebateRate 用户返佣比率配置
     * @return 结果
     */
    @Override
    public AjaxResult updateUserRebateRate(UserRebateRate userRebateRate)
    {
        UserRebateRate userRebateRateVo = new UserRebateRate();
        userRebateRateVo.setRebateType(userRebateRate.getRebateType());
        userRebateRateVo.setRebateLevel(userRebateRate.getRebateLevel());
        userRebateRateVo = userRebateRateMapper.selectUserRebateRate(userRebateRateVo);
        if (userRebateRateVo != null){
            if (!userRebateRateVo.getId().equals(userRebateRate.getId())){
                return AjaxResult.error("该返佣类型已经有此等级的返佣配置");
            }
        }
        userRebateRate.setUpdateTime(new Date());
        int count = userRebateRateMapper.updateUserRebateRate(userRebateRate);
        if (count <= 0){
            return AjaxResult.error("系统繁忙");
        }
        return AjaxResult.success();
    }

    /**
     * 批量删除用户返佣比率配置
     * 
     * @param ids 需要删除的用户返佣比率配置主键
     * @return 结果
     */
    @Override
    public int deleteUserRebateRateByIds(Long[] ids)
    {
        return userRebateRateMapper.deleteUserRebateRateByIds(ids);
    }

    /**
     * 删除用户返佣比率配置信息
     * 
     * @param id 用户返佣比率配置主键
     * @return 结果
     */
    @Override
    public int deleteUserRebateRateById(Long id)
    {
        return userRebateRateMapper.deleteUserRebateRateById(id);
    }
}
