package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.constant.CacheableKey;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.domain.LangMgr;
import com.ruoyi.system.domain.LoanProduct;
import com.ruoyi.system.domain.PlatformCurrency;
import com.ruoyi.system.mapper.LoanProductMapper;
import com.ruoyi.system.service.ILoanProductService;
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
 * 贷款产品配置Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-05-21
 */
@Service
public class LoanProductServiceImpl implements ILoanProductService 
{
    @Resource
    private LoanProductMapper loanProductMapper;

    @Autowired
    private IPlatformCurrencyService platformCurrencyService;

    /**
     * 查询贷款产品配置
     * 
     * @param id 贷款产品配置主键
     * @return 贷款产品配置
     */
    @Override
    @Cacheable(value = CacheableKey.LOAN_PRODUCT + CacheableKey.ENTITY,key = "#id")
    public LoanProduct selectLoanProductById(Long id)
    {
        return loanProductMapper.selectLoanProductById(id);
    }

    /**
     * 查询贷款产品配置列表
     * 
     * @param loanProduct 贷款产品配置
     * @return 贷款产品配置
     */
    @Override
    @Cacheable(value = CacheableKey.LOAN_PRODUCT + CacheableKey.LIST,key = "#loanProduct.cacheableKey()")
    public List<LoanProduct> selectLoanProductList(LoanProduct loanProduct)
    {
        return loanProductMapper.selectLoanProductList(loanProduct);
    }

    /**
     * 新增贷款产品配置
     * 
     * @param loanProduct 贷款产品配置
     * @return 结果
     */
    @Override
    @CacheEvict(value = CacheableKey.LOAN_PRODUCT + CacheableKey.LIST,allEntries = true)
    public int insertLoanProduct(LoanProduct loanProduct)
    {
        //币种信息
        PlatformCurrency platformCurrency = platformCurrencyService.selectPlatformCurrencyById(loanProduct.getCurrencyId());
        if (platformCurrency == null){
            throw new ServiceException("获取币种信息异常");
        }
        if (!platformCurrency.getStatus().equals(0)){
            throw new ServiceException("此币种已禁用");
        }
        //日志记录币种名称
        HttpUtils.getRequestLogParams().put("currencyName",platformCurrency.getCurrencyName());
        loanProduct.setCreateTime(DateUtils.getNowDate());
        return loanProductMapper.insertLoanProduct(loanProduct);
    }

    /**
     * 修改贷款产品配置
     * 
     * @param loanProduct 贷款产品配置
     * @return 结果
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.LOAN_PRODUCT + CacheableKey.ENTITY,key = "#loanProduct.id"),
            @CacheEvict(value = CacheableKey.LOAN_PRODUCT + CacheableKey.LIST,allEntries = true)})
    public int updateLoanProduct(LoanProduct loanProduct)
    {
        //币种信息
        PlatformCurrency platformCurrency = platformCurrencyService.selectPlatformCurrencyById(loanProduct.getCurrencyId());
        if (platformCurrency == null){
            throw new ServiceException("获取币种信息异常");
        }
        if (!platformCurrency.getStatus().equals(0)){
            throw new ServiceException("此币种已禁用");
        }
        //日志记录币种名称
        HttpUtils.getRequestLogParams().put("currencyName",platformCurrency.getCurrencyName());
//        loanProduct.setUpdateBy(SecurityUtils.getUsername());
//        loanProduct.setUpdateTime(new Date());
        return loanProductMapper.updateLoanProduct(loanProduct);
    }

    /**
     * 修改贷款产品多语言
     * @param loanProductId 贷款产品id
     * @param productNameLang 产品名称多语言
     * @return
     */
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.LOAN_PRODUCT + CacheableKey.ENTITY,key = "#loanProductId.id"),
            @CacheEvict(value = CacheableKey.LOAN_PRODUCT + CacheableKey.LIST,allEntries = true)})
    public int updateProductNameLang(Long loanProductId, LangMgr productNameLang){
        LoanProduct loanProduct = new LoanProduct();
        loanProduct.setId(loanProductId);
        loanProduct.setProductNameLang(productNameLang);
        return loanProductMapper.updateLoanProduct(loanProduct);
    }

    /**
     * 批量删除贷款产品配置
     * 
     * @param ids 需要删除的贷款产品配置主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.LOAN_PRODUCT + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.LOAN_PRODUCT + CacheableKey.LIST,allEntries = true)})
    public int deleteLoanProductByIds(Long[] ids)
    {
        LoanProduct search = new LoanProduct();
        search.getParams().put("ids", Arrays.asList(ids));
        List<LoanProduct> loanProducts = loanProductMapper.selectLoanProductList(search);
        //日志记录贷款产品配置信息
        HttpUtils.getRequestLogParams().put("JSONArray:loanProducts", JSONObject.toJSONString(loanProducts));
        return loanProductMapper.deleteLoanProductByIds(ids);
    }

    /**
     * 删除贷款产品配置信息
     * 
     * @param id 贷款产品配置主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.LOAN_PRODUCT + CacheableKey.ENTITY,key = "#id"),
            @CacheEvict(value = CacheableKey.LOAN_PRODUCT + CacheableKey.LIST,allEntries = true)})
    public int deleteLoanProductById(Long id)
    {
        return loanProductMapper.deleteLoanProductById(id);
    }
}
