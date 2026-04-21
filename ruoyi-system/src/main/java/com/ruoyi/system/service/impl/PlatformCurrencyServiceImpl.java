package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.constant.CacheableKey;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.domain.LangMgr;
import com.ruoyi.system.domain.PlatformCurrency;
import com.ruoyi.system.mapper.PlatformCurrencyMapper;
import com.ruoyi.system.service.IPlatformCurrencyService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * 平台交易币种配置信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-10-28
 */
@Service
public class PlatformCurrencyServiceImpl implements IPlatformCurrencyService 
{
    @Resource
    private PlatformCurrencyMapper platformCurrencyMapper;

    /**
     * 查询平台交易币种配置信息
     * 
     * @param id 平台交易币种配置信息主键
     * @return 平台交易币种配置信息
     */
    @Override
    @Cacheable(value = CacheableKey.PLATFORM_CURRENCY + CacheableKey.ENTITY,key = "#id")
    public PlatformCurrency selectPlatformCurrencyById(Long id)
    {
        return platformCurrencyMapper.selectPlatformCurrencyById(id);
    }

    /**
     * 查询平台交易币种配置信息列表
     * 
     * @param platformCurrency 平台交易币种配置信息
     * @return 平台交易币种配置信息
     */
    @Override
    @Cacheable(value = CacheableKey.PLATFORM_CURRENCY + CacheableKey.LIST,key = "#platformCurrency.cacheableKey()")
    public List<PlatformCurrency> selectPlatformCurrencyList(PlatformCurrency platformCurrency)
    {
        return platformCurrencyMapper.selectPlatformCurrencyList(platformCurrency);
    }

    /**
     * 新增平台交易币种配置信息
     * 
     * @param platformCurrency 平台交易币种配置信息
     * @return 结果
     */
    @Override
    @CacheEvict(value = CacheableKey.PLATFORM_CURRENCY + CacheableKey.LIST,allEntries = true)
    public int insertPlatformCurrency(PlatformCurrency platformCurrency)
    {
        PlatformCurrency platformCurrencyVo = platformCurrencyMapper.selectPlatformCurrencyByName(platformCurrency.getCurrencyName());
        if (platformCurrencyVo != null){
            throw new ServiceException("已存在币种"+platformCurrency.getCurrencyName());
        }
        //如果是法币
        if (platformCurrency.getCurrencyType().equals(0)){
            if (platformCurrency.getRealTimeExchangeRateFlag().equals(1) && !platformCurrency.getRealTimeExchangeRateProduct().startsWith("C:")){
                throw new ServiceException("请选择外汇品种");
            }
        }else if (platformCurrency.getCurrencyType().equals(1)){
            //如果是加密货币
            if (platformCurrency.getRealTimeExchangeRateFlag().equals(1) && !platformCurrency.getRealTimeExchangeRateProduct().startsWith("X:")){
                throw new ServiceException("请选择加密货币品种");
            }
        }else {
            throw new ServiceException("币种类型错误");
        }
        int count = platformCurrencyMapper.insertPlatformCurrency(platformCurrency);
        if (count <= 0) {
            throw new ServiceException("系统繁忙");
        }
        return 1;
    }

    /**
     * 修改平台交易币种配置信息
     * 
     * @param platformCurrency 平台交易币种配置信息
     * @return 结果
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.PLATFORM_CURRENCY + CacheableKey.ENTITY,key = "#platformCurrency.id"),
            @CacheEvict(value = CacheableKey.PLATFORM_CURRENCY + CacheableKey.LIST,allEntries = true),
            @CacheEvict(value = CacheableKey.WITHDRAW_CHANNEL_CONFIG + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.WITHDRAW_CHANNEL_CONFIG + CacheableKey.LIST,allEntries = true),
            @CacheEvict(value = CacheableKey.RECHARGE_CHANNEL_CONFIG + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.RECHARGE_CHANNEL_CONFIG + CacheableKey.LIST,allEntries = true)
    })
    public int updatePlatformCurrency(PlatformCurrency platformCurrency)
    {
        PlatformCurrency platformCurrencyVo = platformCurrencyMapper.selectPlatformCurrencyByName(platformCurrency.getCurrencyName());
        if (platformCurrencyVo != null){
            if (!platformCurrencyVo.getId().equals(platformCurrency.getId())){
                throw new RuntimeException("已存在币种"+platformCurrency.getCurrencyName());
            }
        }
        //如果是法币
        if (platformCurrency.getCurrencyType().equals(0)){
            if (platformCurrency.getRealTimeExchangeRateFlag().equals(1) && !platformCurrency.getRealTimeExchangeRateProduct().startsWith("C:")){
                throw new ServiceException("请选择外汇品种");
            }
        }else if (platformCurrency.getCurrencyType().equals(1)){
            //如果是加密货币
            if (platformCurrency.getRealTimeExchangeRateFlag().equals(1) && !platformCurrency.getRealTimeExchangeRateProduct().startsWith("X:")){
                throw new ServiceException("请选择加密货币品种");
            }
        }else {
            throw new ServiceException("币种类型错误");
        }
        int count = platformCurrencyMapper.updatePlatformCurrency(platformCurrency);
        if (count <= 0){
            throw new RuntimeException("系统繁忙");
        }
        return 1;
    }

    /**
     * 修改币种名称多语言
     * @param platformCurrencyId 币种配置id
     * @param currencyNameLang 币种名称语言包
     * @return
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.PLATFORM_CURRENCY + CacheableKey.ENTITY,key = "#platformCurrencyId"),
            @CacheEvict(value = CacheableKey.PLATFORM_CURRENCY + CacheableKey.LIST,allEntries = true),
            @CacheEvict(value = CacheableKey.WITHDRAW_CHANNEL_CONFIG + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.WITHDRAW_CHANNEL_CONFIG + CacheableKey.LIST,allEntries = true),
            @CacheEvict(value = CacheableKey.RECHARGE_CHANNEL_CONFIG + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.RECHARGE_CHANNEL_CONFIG + CacheableKey.LIST,allEntries = true)
    })
    public int updateCurrencyNameLang(Long platformCurrencyId, LangMgr currencyNameLang){
        PlatformCurrency platformCurrency = new PlatformCurrency();
        platformCurrency.setId(platformCurrencyId);
        platformCurrency.setCurrencyNameLang(currencyNameLang);
        return platformCurrencyMapper.updatePlatformCurrency(platformCurrency);
    }

    /**
     * 修改币种描述多语言
     * @param platformCurrencyId 币种配置id
     * @param currencyDescLang 币种描述语言包
     * @return
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.PLATFORM_CURRENCY + CacheableKey.ENTITY,key = "#platformCurrencyId"),
            @CacheEvict(value = CacheableKey.PLATFORM_CURRENCY + CacheableKey.LIST,allEntries = true),
            @CacheEvict(value = CacheableKey.WITHDRAW_CHANNEL_CONFIG + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.WITHDRAW_CHANNEL_CONFIG + CacheableKey.LIST,allEntries = true),
            @CacheEvict(value = CacheableKey.RECHARGE_CHANNEL_CONFIG + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.RECHARGE_CHANNEL_CONFIG + CacheableKey.LIST,allEntries = true)
    })
    public int updateCurrencyDescLang(Long platformCurrencyId, LangMgr currencyDescLang){
        PlatformCurrency platformCurrency = new PlatformCurrency();
        platformCurrency.setId(platformCurrencyId);
        platformCurrency.setCurrencyDescLang(currencyDescLang);
        return platformCurrencyMapper.updatePlatformCurrency(platformCurrency);
    }

    /**
     * 批量删除平台交易币种配置信息
     * 
     * @param ids 需要删除的平台交易币种配置信息主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.PLATFORM_CURRENCY + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.PLATFORM_CURRENCY + CacheableKey.LIST,allEntries = true),
            @CacheEvict(value = CacheableKey.WITHDRAW_CHANNEL_CONFIG + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.WITHDRAW_CHANNEL_CONFIG + CacheableKey.LIST,allEntries = true),
            @CacheEvict(value = CacheableKey.RECHARGE_CHANNEL_CONFIG + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.RECHARGE_CHANNEL_CONFIG + CacheableKey.LIST,allEntries = true)
    })
    public int deletePlatformCurrencyByIds(Long[] ids)
    {
        PlatformCurrency search = new PlatformCurrency();
        search.getParams().put("ids", Arrays.asList(ids));
        List<PlatformCurrency> platformCurrencies = platformCurrencyMapper.selectPlatformCurrencyList(search);
        //日志记录其他值配置信息
        HttpUtils.getRequestLogParams().put("JSONArray:platformCurrencies", JSONObject.toJSONString(platformCurrencies));
        return platformCurrencyMapper.deletePlatformCurrencyByIds(ids);
    }

    /**
     * 删除平台交易币种配置信息信息
     * 
     * @param id 平台交易币种配置信息主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.PLATFORM_CURRENCY + CacheableKey.ENTITY,key = "#id"),
            @CacheEvict(value = CacheableKey.PLATFORM_CURRENCY + CacheableKey.LIST,allEntries = true),
            @CacheEvict(value = CacheableKey.WITHDRAW_CHANNEL_CONFIG + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.WITHDRAW_CHANNEL_CONFIG + CacheableKey.LIST,allEntries = true),
            @CacheEvict(value = CacheableKey.RECHARGE_CHANNEL_CONFIG + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.RECHARGE_CHANNEL_CONFIG + CacheableKey.LIST,allEntries = true)
    })
    public int deletePlatformCurrencyById(Long id)
    {
        return platformCurrencyMapper.deletePlatformCurrencyById(id);
    }
}
