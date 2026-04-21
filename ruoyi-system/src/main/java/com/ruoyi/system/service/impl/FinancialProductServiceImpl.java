package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.constant.CacheableKey;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.domain.FinancialProduct;
import com.ruoyi.system.domain.LangMgr;
import com.ruoyi.system.domain.PlatformCurrency;
import com.ruoyi.system.mapper.FinancialProductMapper;
import com.ruoyi.system.service.IFinancialProductService;
import com.ruoyi.system.service.IPlatformCurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * 理财产品配置Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-11-26
 */
@Service
public class FinancialProductServiceImpl implements IFinancialProductService 
{
    @Resource
    private FinancialProductMapper financialProductMapper;

    @Autowired
    private IPlatformCurrencyService platformCurrencyService;

    /**
     * 查询理财产品配置
     * 
     * @param id 理财产品配置主键
     * @return 理财产品配置
     */
    @Override
    @Cacheable(value = CacheableKey.FINANCIAL_PRODUCT + CacheableKey.ENTITY,key = "#id")
    public FinancialProduct selectFinancialProductById(Long id)
    {
        return financialProductMapper.selectFinancialProductById(id);
    }

    /**
     * 查询理财产品配置列表
     * 
     * @param financialProduct 理财产品配置
     * @return 理财产品配置
     */
    @Override
    @Cacheable(value = CacheableKey.FINANCIAL_PRODUCT + CacheableKey.LIST,key = "#financialProduct.cacheableKey()")
    public List<FinancialProduct> selectFinancialProductList(FinancialProduct financialProduct)
    {
        return financialProductMapper.selectFinancialProductList(financialProduct);
    }

    /**
     * 新增理财产品配置
     * 
     * @param financialProduct 理财产品配置
     * @return 结果
     */
    @Override
    @CacheEvict(value = CacheableKey.FINANCIAL_PRODUCT + CacheableKey.LIST,allEntries = true)
    public int insertFinancialProduct(FinancialProduct financialProduct)
    {
        //币种id
        Long currencyId = financialProduct.getCurrencyId();
        //币种信息
        PlatformCurrency platformCurrency = platformCurrencyService.selectPlatformCurrencyById(currencyId);
        if (platformCurrency == null){
            throw new ServiceException("获取币种信息异常");
        }
        if (!platformCurrency.getStatus().equals(0)){
            throw new ServiceException("此币种已禁用");
        }
        //日志记录币种名称
        financialProduct.setCurrencyName(platformCurrency.getCurrencyName());
        return financialProductMapper.insertFinancialProduct(financialProduct);
    }

    /**
     * 修改理财产品配置
     * 
     * @param financialProduct 理财产品配置
     * @return 结果
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.FINANCIAL_PRODUCT + CacheableKey.ENTITY,key = "#financialProduct.id"),
            @CacheEvict(value = CacheableKey.FINANCIAL_PRODUCT + CacheableKey.LIST,allEntries = true)})
    public int updateFinancialProduct(FinancialProduct financialProduct)
    {
        //币种id
        Long currencyId = financialProduct.getCurrencyId();
        //币种信息
        PlatformCurrency platformCurrency = platformCurrencyService.selectPlatformCurrencyById(currencyId);
        if (platformCurrency == null){
            throw new ServiceException("获取币种信息异常");
        }
        if (!platformCurrency.getStatus().equals(0)){
            throw new ServiceException("此币种已禁用");
        }
        //日志记录币种名称
        financialProduct.setCurrencyName(platformCurrency.getCurrencyName());
        return financialProductMapper.updateFinancialProduct(financialProduct);
    }

    /**
     * 修改理财名称多语言
     * @param financialProductId 币种配置id
     * @param financialNameLang 币种名称语言包
     * @return
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.FINANCIAL_PRODUCT + CacheableKey.ENTITY,key = "#financialProductId"),
            @CacheEvict(value = CacheableKey.FINANCIAL_PRODUCT + CacheableKey.LIST,allEntries = true)})
    public int updateFinancialNameLang(Long financialProductId, LangMgr financialNameLang){
        FinancialProduct financialProduct = new FinancialProduct();
        financialProduct.setId(financialProductId);
        financialProduct.setFinancialNameLang(financialNameLang);
        return financialProductMapper.updateFinancialProduct(financialProduct);
    }

    /**
     * 批量删除理财产品配置
     * 
     * @param ids 需要删除的理财产品配置主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.FINANCIAL_PRODUCT + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.FINANCIAL_PRODUCT + CacheableKey.LIST,allEntries = true)})
    public int deleteFinancialProductByIds(Long[] ids)
    {
        FinancialProduct search = new FinancialProduct();
        search.getParams().put("ids", Arrays.asList(ids));
        List<FinancialProduct> financialProducts = financialProductMapper.selectFinancialProductList(search);
        //日志记录理财产品信息
        HttpUtils.getRequestLogParams().put("JSONArray:financialProducts", JSONObject.toJSONString(financialProducts));
        return financialProductMapper.deleteFinancialProductByIds(ids);
    }

    /**
     * 删除理财产品配置信息
     * 
     * @param id 理财产品配置主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.FINANCIAL_PRODUCT + CacheableKey.ENTITY,key = "#id"),
            @CacheEvict(value = CacheableKey.FINANCIAL_PRODUCT + CacheableKey.LIST,allEntries = true)})
    public int deleteFinancialProductById(Long id)
    {
        return financialProductMapper.deleteFinancialProductById(id);
    }
}
