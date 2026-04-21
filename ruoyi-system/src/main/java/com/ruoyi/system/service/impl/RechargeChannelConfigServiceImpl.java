package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.constant.CacheableKey;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.googleAuth.GoogleAuthenticatorUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.domain.LangMgr;
import com.ruoyi.system.domain.PlatformCurrency;
import com.ruoyi.system.domain.RechargeChannelConfig;
import com.ruoyi.system.mapper.RechargeChannelConfigMapper;
import com.ruoyi.system.service.IPlatformCurrencyService;
import com.ruoyi.system.service.IRechargeChannelConfigService;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * 充值通道配置Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-11-04
 */
@Service
public class RechargeChannelConfigServiceImpl implements IRechargeChannelConfigService 
{
    @Resource
    private RechargeChannelConfigMapper rechargeChannelConfigMapper;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private IPlatformCurrencyService platformCurrencyService;

    /**
     * 查询充值通道配置
     * 
     * @param id 充值通道配置主键
     * @return 充值通道配置
     */
    @Override
    @Cacheable(value = CacheableKey.RECHARGE_CHANNEL_CONFIG + CacheableKey.ENTITY,key = "#id")
    public RechargeChannelConfig selectRechargeChannelConfigById(Long id)
    {
        return rechargeChannelConfigMapper.selectRechargeChannelConfigById(id);
    }

    /**
     * 查询充值通道配置列表
     * 
     * @param rechargeChannelConfig 充值通道配置
     * @return 充值通道配置
     */
    @Override
    @Cacheable(value = CacheableKey.RECHARGE_CHANNEL_CONFIG + CacheableKey.LIST,key = "#rechargeChannelConfig.cacheableKey()")
    public List<RechargeChannelConfig> selectRechargeChannelConfigList(RechargeChannelConfig rechargeChannelConfig)
    {
        return rechargeChannelConfigMapper.selectRechargeChannelConfigList(rechargeChannelConfig);
    }

    /**
     * 新增充值通道配置
     * 
     * @param rechargeChannelConfig 充值通道配置
     * @return 结果
     */
    @Override
    @CacheEvict(value = CacheableKey.RECHARGE_CHANNEL_CONFIG + CacheableKey.LIST,allEntries = true)
    public int insertRechargeChannelConfig(RechargeChannelConfig rechargeChannelConfig)
    {
//        RechargeChannelConfig rechargeChannelConfigVo = rechargeChannelConfigMapper.selectRechargeChannelConfigByChannelName(rechargeChannelConfig.getChannelName());
//        if (rechargeChannelConfigVo != null){
//            throw new ServiceException("通道名称已存在");
//        }
        //如果通道类型不是客服
        if (!rechargeChannelConfig.getChannelType().equals(2)){
            //币种id
            Long currencyId = rechargeChannelConfig.getCurrencyId();
            //币种信息
            PlatformCurrency platformCurrency = platformCurrencyService.selectPlatformCurrencyById(currencyId);
            if (platformCurrency == null){
                throw new ServiceException("获取币种信息异常");
            }
            if (!platformCurrency.getStatus().equals(0)){
                throw new ServiceException("此币种已禁用");
            }
            //日志记录币种名称
            rechargeChannelConfig.setCurrencyName(platformCurrency.getCurrencyName());
        }
        //
        if (rechargeChannelConfig.getUserId() != null) {
            // 原始通道ID
            Long originalChannelId = rechargeChannelConfig.getOriginalChannelId();
            if (originalChannelId == null) {
                throw new ServiceException("用户定制通道必须绑定一个原始通道");
            } else {
                // 原始通道信息
                RechargeChannelConfig originalChannel = rechargeChannelConfigMapper.selectRechargeChannelConfigById(originalChannelId);
                if (originalChannel == null) {
                    throw new ServiceException("获取原始通道信息异常");
                }
                if (originalChannel.getUserId() != null) {
                    throw new ServiceException("所选择的原始通道异常");
                }
            }
        }

        //登录用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        //如果不是系统管理员，需要校验谷歌验证码
        if (!loginUser.getDeptId().equals(1L)){
            //谷歌验证器密钥
            String googleAuthSecurityKey = sysUserService.getGoogleAuthSecurityKey(loginUser.getUserId());
            if (StringUtils.isNotEmpty(googleAuthSecurityKey)){
                Object googleValidateCode = rechargeChannelConfig.getParams().get("googleValidateCode");
                if (googleValidateCode == null){
                    throw new ServiceException("请输入谷歌验证码");
                }
                // 根据密钥获取此刻的动态口令
                String realCode = GoogleAuthenticatorUtils.getTOTPCode(googleAuthSecurityKey);
                if (!realCode.equals(googleValidateCode.toString())){
                    throw new ServiceException("谷歌验证码错误");
                }
            }else {
                throw new ServiceException("请先开启谷歌验证器");
            }
        }
        int count = rechargeChannelConfigMapper.insertRechargeChannelConfig(rechargeChannelConfig);
        if (count <= 0){
            throw new ServiceException("系统繁忙");
        }
        return 1;
    }

    /**
     * 修改充值通道配置
     * 
     * @param rechargeChannelConfig 充值通道配置
     * @return 结果
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.RECHARGE_CHANNEL_CONFIG + CacheableKey.ENTITY,key = "#rechargeChannelConfig.id"),
            @CacheEvict(value = CacheableKey.RECHARGE_CHANNEL_CONFIG + CacheableKey.LIST,allEntries = true)})
    public int updateRechargeChannelConfig(RechargeChannelConfig rechargeChannelConfig)
    {
//        RechargeChannelConfig rechargeChannelConfigVo = rechargeChannelConfigMapper.selectRechargeChannelConfigByChannelName(rechargeChannelConfig.getChannelName());
//        if (rechargeChannelConfigVo != null && !rechargeChannelConfigVo.getId().equals(rechargeChannelConfig.getId())){
//            throw new ServiceException("通道名称已存在");
//        }
        //如果通道类型不是客服
        if (!rechargeChannelConfig.getChannelType().equals(2)){
            //币种id
            Long currencyId = rechargeChannelConfig.getCurrencyId();
            //币种信息
            PlatformCurrency platformCurrency = platformCurrencyService.selectPlatformCurrencyById(currencyId);
            if (platformCurrency == null){
                throw new ServiceException("获取币种信息异常");
            }
            if (!platformCurrency.getStatus().equals(0)){
                throw new ServiceException("此币种已禁用");
            }
            //日志记录币种名称
            rechargeChannelConfig.setCurrencyName(platformCurrency.getCurrencyName());
        }
        //登录用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        //如果不是系统管理员，需要校验谷歌验证码
        if (!loginUser.getDeptId().equals(1L)){
            //谷歌验证器密钥
            String googleAuthSecurityKey = sysUserService.getGoogleAuthSecurityKey(loginUser.getUserId());
            if (StringUtils.isNotEmpty(googleAuthSecurityKey)){
                Object googleValidateCode = rechargeChannelConfig.getParams().get("googleValidateCode");
                if (googleValidateCode == null){
                    throw new ServiceException("请输入谷歌验证码");
                }
                // 根据密钥获取此刻的动态口令
                String realCode = GoogleAuthenticatorUtils.getTOTPCode(googleAuthSecurityKey);
                if (!realCode.equals(googleValidateCode.toString())){
                    throw new ServiceException("谷歌验证码错误");
                }
            }else {
                throw new ServiceException("请先开启谷歌验证器");
            }
        }
        int count = rechargeChannelConfigMapper.updateRechargeChannelConfig(rechargeChannelConfig);
        if (count <= 0){
            throw new ServiceException("系统繁忙");
        }
        return 1;
    }

    /**
     * 修改提现通道名称多语言配置
     * @param rechargeChannelConfigId 提现通道id
     * @param channelNameLang 通道名称多语言
     * @return
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.RECHARGE_CHANNEL_CONFIG + CacheableKey.ENTITY,key = "#rechargeChannelConfigId"),
            @CacheEvict(value = CacheableKey.RECHARGE_CHANNEL_CONFIG + CacheableKey.LIST,allEntries = true)})
    public int updateChannelNameLang(Long rechargeChannelConfigId, LangMgr channelNameLang){
        RechargeChannelConfig rechargeChannelConfig = new RechargeChannelConfig();
        rechargeChannelConfig.setId(rechargeChannelConfigId);
        rechargeChannelConfig.setChannelNameLang(channelNameLang);
        return rechargeChannelConfigMapper.updateRechargeChannelConfig(rechargeChannelConfig);
    }

    /**
     * 批量删除充值通道配置
     * 
     * @param ids 需要删除的充值通道配置主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.RECHARGE_CHANNEL_CONFIG + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.RECHARGE_CHANNEL_CONFIG + CacheableKey.LIST,allEntries = true)})
    public int deleteRechargeChannelConfigByIds(Long[] ids)
    {
        RechargeChannelConfig search = new RechargeChannelConfig();
        search.getParams().put("ids", Arrays.asList(ids));
        List<RechargeChannelConfig> rechargeChannelConfigs = rechargeChannelConfigMapper.selectRechargeChannelConfigList(search);
        //日志记录充值通道配置信息
        HttpUtils.getRequestLogParams().put("JSONArray:rechargeChannelConfigs", JSONObject.toJSONString(rechargeChannelConfigs));
        return rechargeChannelConfigMapper.deleteRechargeChannelConfigByIds(ids);
    }

    /**
     * 删除充值通道配置信息
     * 
     * @param id 充值通道配置主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.RECHARGE_CHANNEL_CONFIG + CacheableKey.ENTITY,key = "#id"),
            @CacheEvict(value = CacheableKey.RECHARGE_CHANNEL_CONFIG + CacheableKey.LIST,allEntries = true)})
    public int deleteRechargeChannelConfigById(Long id)
    {
        return rechargeChannelConfigMapper.deleteRechargeChannelConfigById(id);
    }
}
