package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.constant.CacheableKey;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.domain.LangMgr;
import com.ruoyi.system.domain.PlatformCurrency;
import com.ruoyi.system.domain.RechargeChannelConfig;
import com.ruoyi.system.domain.WithdrawChannelConfig;
import com.ruoyi.system.mapper.WithdrawChannelConfigMapper;
import com.ruoyi.system.service.IPlatformCurrencyService;
import com.ruoyi.system.service.IWithdrawChannelConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * 提现通道配置Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-11-04
 */
@Service
public class WithdrawChannelConfigServiceImpl implements IWithdrawChannelConfigService 
{
    @Resource
    private WithdrawChannelConfigMapper withdrawChannelConfigMapper;

    @Autowired
    private IPlatformCurrencyService platformCurrencyService;

    /**
     * 查询提现通道配置
     * 
     * @param id 提现通道配置主键
     * @return 提现通道配置
     */
    @Override
    @Cacheable(value = CacheableKey.WITHDRAW_CHANNEL_CONFIG + CacheableKey.ENTITY,key = "#id")
    public WithdrawChannelConfig selectWithdrawChannelConfigById(Long id)
    {
        return withdrawChannelConfigMapper.selectWithdrawChannelConfigById(id);
    }

    /**
     * 查询提现通道配置列表
     * 
     * @param withdrawChannelConfig 提现通道配置
     * @return 提现通道配置
     */
    @Override
    @Cacheable(value = CacheableKey.WITHDRAW_CHANNEL_CONFIG + CacheableKey.LIST,key = "#withdrawChannelConfig.cacheableKey()")
    public List<WithdrawChannelConfig> selectWithdrawChannelConfigList(WithdrawChannelConfig withdrawChannelConfig)
    {
        return withdrawChannelConfigMapper.selectWithdrawChannelConfigList(withdrawChannelConfig);
    }

    /**
     * 新增提现通道配置
     * 
     * @param withdrawChannelConfig 提现通道配置
     * @return 结果
     */
    @Override
    @CacheEvict(value = CacheableKey.WITHDRAW_CHANNEL_CONFIG + CacheableKey.LIST,allEntries = true)
    public int insertWithdrawChannelConfig(WithdrawChannelConfig withdrawChannelConfig)
    {
        //到账币种id
        Long currencyId = withdrawChannelConfig.getArrivalCurrencyId();
        //币种信息
        PlatformCurrency platformCurrency = platformCurrencyService.selectPlatformCurrencyById(currencyId);
        if (platformCurrency == null){
            throw new ServiceException("获取币种信息异常");
        }
        //如果币种有分多种钱包
        if (StringUtils.isNotEmpty(platformCurrency.getWalletAddressType())){
            //钱包地址类型
            String walletAddressType = withdrawChannelConfig.getWalletAddressType();
            if (StringUtils.isEmpty(walletAddressType)){
                throw new ServiceException("请选择钱包地址类型");
            }
            if (!Arrays.asList(platformCurrency.getWalletAddressType().split("/")).contains(walletAddressType)){
                throw new ServiceException("钱包地址类型错误");
            }
        }else {
            withdrawChannelConfig.setWalletAddressType(null);
        }
        int count = withdrawChannelConfigMapper.insertWithdrawChannelConfig(withdrawChannelConfig);
        if (count <= 0){
            throw new ServiceException("系统繁忙");
        }
        return 1;
    }

    /**
     * 修改提现通道配置
     * 
     * @param withdrawChannelConfig 提现通道配置
     * @return 结果
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.WITHDRAW_CHANNEL_CONFIG + CacheableKey.ENTITY,key = "#withdrawChannelConfig.id"),
            @CacheEvict(value = CacheableKey.WITHDRAW_CHANNEL_CONFIG + CacheableKey.LIST,allEntries = true)})
    public int updateWithdrawChannelConfig(WithdrawChannelConfig withdrawChannelConfig)
    {
        //到账币种id
        Long currencyId = withdrawChannelConfig.getArrivalCurrencyId();
        //币种信息
        PlatformCurrency platformCurrency = platformCurrencyService.selectPlatformCurrencyById(currencyId);
        if (platformCurrency == null){
            throw new ServiceException("获取币种信息异常");
        }
        //如果币种有分多种钱包
        if (StringUtils.isNotEmpty(platformCurrency.getWalletAddressType())){
            //钱包地址类型
            String walletAddressType = withdrawChannelConfig.getWalletAddressType();
            if (StringUtils.isEmpty(walletAddressType)){
                throw new ServiceException("请选择钱包地址类型");
            }
            if (!Arrays.asList(platformCurrency.getWalletAddressType().split("/")).contains(walletAddressType)){
                throw new ServiceException("钱包地址类型错误");
            }
        }else {
            withdrawChannelConfig.setWalletAddressType("");
        }
        int count = withdrawChannelConfigMapper.updateWithdrawChannelConfig(withdrawChannelConfig);
        if (count <= 0){
            throw new ServiceException("系统繁忙");
        }
        return 1;
    }

    /**
     * 修改提现通道名称多语言配置
     * @param withdrawChannelConfigId 提现通道id
     * @param channelNameLang 通道名称多语言
     * @return
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.WITHDRAW_CHANNEL_CONFIG + CacheableKey.ENTITY,key = "#withdrawChannelConfigId"),
            @CacheEvict(value = CacheableKey.WITHDRAW_CHANNEL_CONFIG + CacheableKey.LIST,allEntries = true)})
    public int updateChannelNameLang(Long withdrawChannelConfigId, LangMgr channelNameLang){
        WithdrawChannelConfig withdrawChannelConfig = new WithdrawChannelConfig();
        withdrawChannelConfig.setId(withdrawChannelConfigId);
        withdrawChannelConfig.setChannelNameLang(channelNameLang);
        return withdrawChannelConfigMapper.updateWithdrawChannelConfig(withdrawChannelConfig);
    }

    /**
     * 批量删除提现通道配置
     * 
     * @param ids 需要删除的提现通道配置主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.WITHDRAW_CHANNEL_CONFIG + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.WITHDRAW_CHANNEL_CONFIG + CacheableKey.LIST,allEntries = true)})
    public int deleteWithdrawChannelConfigByIds(Long[] ids)
    {
        WithdrawChannelConfig search = new WithdrawChannelConfig();
        search.getParams().put("ids", Arrays.asList(ids));
        List<WithdrawChannelConfig> withdrawChannelConfigs = withdrawChannelConfigMapper.selectWithdrawChannelConfigList(search);
        //日志记录体现通道配置信息
        HttpUtils.getRequestLogParams().put("JSONArray:withdrawChannelConfigs", JSONObject.toJSONString(withdrawChannelConfigs));
        return withdrawChannelConfigMapper.deleteWithdrawChannelConfigByIds(ids);
    }

    /**
     * 删除提现通道配置信息
     * 
     * @param id 提现通道配置主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.WITHDRAW_CHANNEL_CONFIG + CacheableKey.ENTITY,key = "#id"),
            @CacheEvict(value = CacheableKey.WITHDRAW_CHANNEL_CONFIG + CacheableKey.LIST,allEntries = true)})
    public int deleteWithdrawChannelConfigById(Long id)
    {
        return withdrawChannelConfigMapper.deleteWithdrawChannelConfigById(id);
    }
}
