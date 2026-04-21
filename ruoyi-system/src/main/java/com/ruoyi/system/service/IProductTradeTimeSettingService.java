package com.ruoyi.system.service;

import com.ruoyi.system.domain.ProductTradeTimeSetting;

import java.util.List;

/**
 * 系统产品交易时间配置Service接口
 * 
 * @author ruoyi
 * @date 2023-11-27
 */
public interface IProductTradeTimeSettingService 
{
    /**
     * 查询系统产品交易时间配置
     * 
     * @param id 系统产品交易时间配置主键
     * @return 系统产品交易时间配置
     */
    public ProductTradeTimeSetting selectProductTradeTimeSettingById(Long id);

    /**
     * 查询系统产品交易时间配置
     *
     * @param day 天
     * @param productType 产品类型
     * @return 系统产品交易时间配置
     */
    public ProductTradeTimeSetting selectProductTradeTimeSettingByDayAndProductType(Integer day,Integer productType);

    /**
     * 查询系统产品交易时间配置列表
     * 
     * @param productTradeTimeSetting 系统产品交易时间配置
     * @return 系统产品交易时间配置集合
     */
    public List<ProductTradeTimeSetting> selectProductTradeTimeSettingList(ProductTradeTimeSetting productTradeTimeSetting);

    /**
     * 新增系统产品交易时间配置
     * 
     * @param productTradeTimeSetting 系统产品交易时间配置
     * @return 结果
     */
    public int insertProductTradeTimeSetting(ProductTradeTimeSetting productTradeTimeSetting);

    /**
     * 修改系统产品交易时间配置
     * 
     * @param productTradeTimeSetting 系统产品交易时间配置
     * @return 结果
     */
    public int updateProductTradeTimeSetting(ProductTradeTimeSetting productTradeTimeSetting);

    /**
     * 批量删除系统产品交易时间配置
     * 
     * @param ids 需要删除的系统产品交易时间配置主键集合
     * @return 结果
     */
    public int deleteProductTradeTimeSettingByIds(Long[] ids);

    /**
     * 删除系统产品交易时间配置信息
     * 
     * @param id 系统产品交易时间配置主键
     * @return 结果
     */
    public int deleteProductTradeTimeSettingById(Long id);
}
