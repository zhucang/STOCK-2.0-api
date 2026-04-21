package com.ruoyi.system.service.impl;

import com.ruoyi.common.constant.CacheableKey;
import com.ruoyi.system.domain.OrderFeeSetting;
import com.ruoyi.system.mapper.OrderFeeSettingMapper;
import com.ruoyi.system.service.IOrderFeeSettingService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 产品买入卖出手续费配置Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-11-27
 */
@Service
public class OrderFeeSettingServiceImpl implements IOrderFeeSettingService 
{
    @Resource
    private OrderFeeSettingMapper orderFeeSettingMapper;

    /**
     * 查询产品买入卖出手续费配置
     * 
     * @param id 产品买入卖出手续费配置主键
     * @return 产品买入卖出手续费配置
     */
    @Override
//    @Cacheable(value = CacheableKey.ORDER_FEE_SETTING + CacheableKey.ENTITY,key = "#id")
    public OrderFeeSetting selectOrderFeeSettingById(Long id)
    {
        return orderFeeSettingMapper.selectOrderFeeSettingById(id);
    }

    /**
     * 查询产品买入卖出手续费配置
     *
     * @param key 产品买入卖出手续费配置key
     * @return 产品买入卖出手续费配置
     */
    @Cacheable(value = CacheableKey.ORDER_FEE_SETTING + CacheableKey.ENTITY,key = "'selectOrderFeeSettingByKey'+#key")
    public OrderFeeSetting selectOrderFeeSettingByKey(String key){
        return orderFeeSettingMapper.selectOrderFeeSettingByKey(key);
    }

    /**
     * 查询产品买入卖出手续费配置列表
     * 
     * @param orderFeeSetting 产品买入卖出手续费配置
     * @return 产品买入卖出手续费配置
     */
    @Override
    @Cacheable(value = CacheableKey.ORDER_FEE_SETTING + CacheableKey.LIST,key = "#orderFeeSetting.cacheableKey()")
    public List<OrderFeeSetting> selectOrderFeeSettingList(OrderFeeSetting orderFeeSetting)
    {
        return orderFeeSettingMapper.selectOrderFeeSettingList(orderFeeSetting);
    }

    /**
     * 新增产品买入卖出手续费配置
     * 
     * @param orderFeeSetting 产品买入卖出手续费配置
     * @return 结果
     */
    @Override
    @CacheEvict(value = CacheableKey.ORDER_FEE_SETTING + CacheableKey.LIST,allEntries = true)
    public int insertOrderFeeSetting(OrderFeeSetting orderFeeSetting)
    {
        return orderFeeSettingMapper.insertOrderFeeSetting(orderFeeSetting);
    }

    /**
     * 修改产品买入卖出手续费配置
     * 
     * @param orderFeeSetting 产品买入卖出手续费配置
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.ORDER_FEE_SETTING + CacheableKey.ENTITY,key = "#orderFeeSetting.id"),
            @CacheEvict(value = CacheableKey.ORDER_FEE_SETTING + CacheableKey.ENTITY,key = "'selectOrderFeeSettingByKey'+#orderFeeSetting.key"),
            @CacheEvict(value = CacheableKey.ORDER_FEE_SETTING + CacheableKey.LIST,allEntries = true)})
    public int updateOrderFeeSetting(OrderFeeSetting orderFeeSetting)
    {
        return orderFeeSettingMapper.updateOrderFeeSetting(orderFeeSetting);
    }

    /**
     * 批量删除产品买入卖出手续费配置
     * 
     * @param ids 需要删除的产品买入卖出手续费配置主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.ORDER_FEE_SETTING + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.ORDER_FEE_SETTING + CacheableKey.LIST,allEntries = true)})
    public int deleteOrderFeeSettingByIds(Long[] ids)
    {
        return orderFeeSettingMapper.deleteOrderFeeSettingByIds(ids);
    }

    /**
     * 删除产品买入卖出手续费配置信息
     * 
     * @param id 产品买入卖出手续费配置主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.ORDER_FEE_SETTING + CacheableKey.ENTITY,key = "#id"),
//            @CacheEvict(value = CacheableKey.ORDER_FEE_SETTING + CacheableKey.ENTITY,key = "'selectOrderFeeSettingByKey'+#key"),
            @CacheEvict(value = CacheableKey.ORDER_FEE_SETTING + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.ORDER_FEE_SETTING + CacheableKey.LIST,allEntries = true)})
    public int deleteOrderFeeSettingById(Long id)
    {
        return orderFeeSettingMapper.deleteOrderFeeSettingById(id);
    }
}
