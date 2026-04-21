package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.constant.CacheableKey;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.domain.LangMgr;
import com.ruoyi.system.domain.PlatformCurrency;
import com.ruoyi.system.domain.StakingProduct;
import com.ruoyi.system.mapper.StakingProductMapper;
import com.ruoyi.system.service.IPlatformCurrencyService;
import com.ruoyi.system.service.IStakingProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * 质押产品配置Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-07-17
 */
@Service
public class StakingProductServiceImpl implements IStakingProductService 
{
    @Resource
    private StakingProductMapper stakingProductMapper;

    @Autowired
    private IPlatformCurrencyService platformCurrencyService;

    /**
     * 查询质押产品配置
     * 
     * @param id 质押产品配置主键
     * @return 质押产品配置
     */
    @Override
    @Cacheable(value = CacheableKey.STAKING_PRODUCT + CacheableKey.ENTITY,key = "#id")
    public StakingProduct selectStakingProductById(Long id)
    {
        return stakingProductMapper.selectStakingProductById(id);
    }

    /**
     * 查询质押产品配置列表
     * 
     * @param stakingProduct 质押产品配置
     * @return 质押产品配置
     */
    @Override
    @Cacheable(value = CacheableKey.STAKING_PRODUCT + CacheableKey.LIST,key = "#stakingProduct.cacheableKey()")
    public List<StakingProduct> selectStakingProductList(StakingProduct stakingProduct)
    {
        return stakingProductMapper.selectStakingProductList(stakingProduct);
    }

    /**
     * 新增质押产品配置
     * 
     * @param stakingProduct 质押产品配置
     * @return 结果
     */
    @Override
    @CacheEvict(value = CacheableKey.STAKING_PRODUCT + CacheableKey.LIST,allEntries = true)
    public int insertStakingProduct(StakingProduct stakingProduct)
    {
        //币种id
        Long currencyId = stakingProduct.getCurrencyId();
        //币种信息
        PlatformCurrency platformCurrency = platformCurrencyService.selectPlatformCurrencyById(currencyId);
        if (platformCurrency == null){
            throw new ServiceException("获取币种信息异常");
        }
        if (!platformCurrency.getStatus().equals(0)){
            throw new ServiceException("此币种已禁用");
        }
        //日志记录币种名称
        stakingProduct.setCurrencyName(platformCurrency.getCurrencyName());
        return stakingProductMapper.insertStakingProduct(stakingProduct);
    }

    /**
     * 修改质押产品配置
     * 
     * @param stakingProduct 质押产品配置
     * @return 结果
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.STAKING_PRODUCT + CacheableKey.ENTITY,key = "#stakingProduct.id"),
            @CacheEvict(value = CacheableKey.STAKING_PRODUCT + CacheableKey.LIST,allEntries = true)})
    public int updateStakingProduct(StakingProduct stakingProduct)
    {
        //币种id
        Long currencyId = stakingProduct.getCurrencyId();
        //币种信息
        PlatformCurrency platformCurrency = platformCurrencyService.selectPlatformCurrencyById(currencyId);
        if (platformCurrency == null){
            throw new ServiceException("获取币种信息异常");
        }
        if (!platformCurrency.getStatus().equals(0)){
            throw new ServiceException("此币种已禁用");
        }
        if (stakingProduct.getStakingTime() == null){
            stakingProduct.getParams().put("stakingTimeNull",0);
        }
        //日志记录币种名称
        stakingProduct.setCurrencyName(platformCurrency.getCurrencyName());
        return stakingProductMapper.updateStakingProduct(stakingProduct);
    }

    /**
     * 修改质押名称多语言
     * @param stakingProductId 币种配置id
     * @param stakingNameLang 质押名称语言包
     * @return
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.STAKING_PRODUCT + CacheableKey.ENTITY,key = "#stakingProductId"),
            @CacheEvict(value = CacheableKey.STAKING_PRODUCT + CacheableKey.LIST,allEntries = true)})
    public int updateStakingNameLang(Long stakingProductId, LangMgr stakingNameLang){
        StakingProduct stakingProduct = new StakingProduct();
        stakingProduct.setId(stakingProductId);
        stakingProduct.setStakingNameLang(stakingNameLang);
        return stakingProductMapper.updateStakingProduct(stakingProduct);
    }

    /**
     * 批量删除质押产品配置
     * 
     * @param ids 需要删除的质押产品配置主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.STAKING_PRODUCT + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.STAKING_PRODUCT + CacheableKey.LIST,allEntries = true)})
    public int deleteStakingProductByIds(Long[] ids)
    {
        StakingProduct search = new StakingProduct();
        search.getParams().put("ids", Arrays.asList(ids));
        List<StakingProduct> list = stakingProductMapper.selectStakingProductList(search);
        //日志记录质押产品信息
        HttpUtils.getRequestLogParams().put("JSONArray:stakingProduct", JSONObject.toJSONString(list));
        return stakingProductMapper.deleteStakingProductByIds(ids);
    }

    /**
     * 删除质押产品配置信息
     * 
     * @param id 质押产品配置主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.STAKING_PRODUCT + CacheableKey.ENTITY,key = "#id"),
            @CacheEvict(value = CacheableKey.STAKING_PRODUCT + CacheableKey.LIST,allEntries = true)})
    public int deleteStakingProductById(Long id)
    {
        return stakingProductMapper.deleteStakingProductById(id);
    }
}
