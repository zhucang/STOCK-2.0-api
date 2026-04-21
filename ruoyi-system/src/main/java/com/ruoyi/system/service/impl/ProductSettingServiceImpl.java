package com.ruoyi.system.service.impl;

import com.ruoyi.common.constant.CacheableKey;
import com.ruoyi.system.domain.ProductSetting;
import com.ruoyi.system.mapper.ProductSettingMapper;
import com.ruoyi.system.service.IProductSettingService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 产品交易设置（产品风控）Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-12-01
 */
@Service
public class ProductSettingServiceImpl implements IProductSettingService 
{
    @Resource
    private ProductSettingMapper productSettingMapper;

    /**
     * 查询产品交易设置（产品风控）
     * 
     * @param id 产品交易设置（产品风控）主键
     * @return 产品交易设置（产品风控）
     */
    @Override
//    @Cacheable(value = CacheableKey.PRODUCT_SETTING + CacheableKey.ENTITY,key = "#id")
    public ProductSetting selectProductSettingById(Long id)
    {
        return productSettingMapper.selectProductSettingById(id);
    }

    /**
     * 查询产品交易设置（产品风控）
     *
     * @param productType 产品类型
     * @return 产品交易设置（产品风控）
     */
    @Override
    @Cacheable(value = CacheableKey.PRODUCT_SETTING + CacheableKey.ENTITY,key = "'selectProductSettingByProductType'+#productType")
    public ProductSetting selectProductSettingByProductType(Integer productType){
        return productSettingMapper.selectProductSettingByProductType(productType);
    }

    /**
     * 查询产品交易设置（产品风控）列表
     * 
     * @param productSetting 产品交易设置（产品风控）
     * @return 产品交易设置（产品风控）
     */
    @Override
    @Cacheable(value = CacheableKey.PRODUCT_SETTING + CacheableKey.LIST,key = "#productSetting.cacheableKey()")
    public List<ProductSetting> selectProductSettingList(ProductSetting productSetting)
    {
        return productSettingMapper.selectProductSettingList(productSetting);
    }

    /**
     * 新增产品交易设置（产品风控）
     * 
     * @param productSetting 产品交易设置（产品风控）
     * @return 结果
     */
    @Override
    @CacheEvict(value = CacheableKey.PRODUCT_SETTING + CacheableKey.LIST,allEntries = true)
    public int insertProductSetting(ProductSetting productSetting)
    {
        return productSettingMapper.insertProductSetting(productSetting);
    }

    /**
     * 修改产品交易设置（产品风控）
     * 
     * @param productSetting 产品交易设置（产品风控）
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.PRODUCT_SETTING + CacheableKey.ENTITY,key = "#productSetting.id"),
            @CacheEvict(value = CacheableKey.PRODUCT_SETTING + CacheableKey.ENTITY,key = "'selectProductSettingByProductType'+#productSetting.productType"),
            @CacheEvict(value = CacheableKey.PRODUCT_SETTING + CacheableKey.LIST,allEntries = true)})
    public int updateProductSetting(ProductSetting productSetting)
    {
        return productSettingMapper.updateProductSetting(productSetting);
    }

    /**
     * 批量删除产品交易设置（产品风控）
     * 
     * @param ids 需要删除的产品交易设置（产品风控）主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.PRODUCT_SETTING + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.PRODUCT_SETTING + CacheableKey.LIST,allEntries = true)})
    public int deleteProductSettingByIds(Long[] ids)
    {
        return productSettingMapper.deleteProductSettingByIds(ids);
    }

    /**
     * 删除产品交易设置（产品风控）信息
     * 
     * @param id 产品交易设置（产品风控）主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.PRODUCT_SETTING + CacheableKey.ENTITY,key = "#id"),
//            @CacheEvict(value = CacheableKey.PRODUCT_SETTING + CacheableKey.ENTITY,key = "'selectProductSettingByProductType'+#productType"),
            @CacheEvict(value = CacheableKey.PRODUCT_SETTING + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.PRODUCT_SETTING + CacheableKey.LIST,allEntries = true)})
    public int deleteProductSettingById(Long id)
    {
        return productSettingMapper.deleteProductSettingById(id);
    }
}
