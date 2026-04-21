package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.OrderFeeSetting;

import java.util.List;

/**
 * 产品买入卖出手续费配置Mapper接口
 * 
 * @author ruoyi
 * @date 2023-11-27
 */
public interface OrderFeeSettingMapper 
{
    /**
     * 查询产品买入卖出手续费配置
     * 
     * @param id 产品买入卖出手续费配置主键
     * @return 产品买入卖出手续费配置
     */
    public OrderFeeSetting selectOrderFeeSettingById(Long id);

    /**
     * 查询产品买入卖出手续费配置
     *
     * @param key 产品买入卖出手续费配置key
     * @return 产品买入卖出手续费配置
     */
    public OrderFeeSetting selectOrderFeeSettingByKey(String key);

    /**
     * 查询产品买入卖出手续费配置列表
     * 
     * @param orderFeeSetting 产品买入卖出手续费配置
     * @return 产品买入卖出手续费配置集合
     */
    public List<OrderFeeSetting> selectOrderFeeSettingList(OrderFeeSetting orderFeeSetting);

    /**
     * 新增产品买入卖出手续费配置
     * 
     * @param orderFeeSetting 产品买入卖出手续费配置
     * @return 结果
     */
    public int insertOrderFeeSetting(OrderFeeSetting orderFeeSetting);

    /**
     * 修改产品买入卖出手续费配置
     * 
     * @param orderFeeSetting 产品买入卖出手续费配置
     * @return 结果
     */
    public int updateOrderFeeSetting(OrderFeeSetting orderFeeSetting);

    /**
     * 删除产品买入卖出手续费配置
     * 
     * @param id 产品买入卖出手续费配置主键
     * @return 结果
     */
    public int deleteOrderFeeSettingById(Long id);

    /**
     * 批量删除产品买入卖出手续费配置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOrderFeeSettingByIds(Long[] ids);
}
