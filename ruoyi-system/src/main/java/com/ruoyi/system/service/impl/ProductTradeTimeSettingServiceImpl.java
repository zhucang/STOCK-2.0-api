package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.ProductTradeTimeSetting;
import com.ruoyi.system.mapper.ProductTradeTimeSettingMapper;
import com.ruoyi.system.service.IProductTradeTimeSettingService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统产品交易时间配置Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-11-27
 */
@Service
public class ProductTradeTimeSettingServiceImpl implements IProductTradeTimeSettingService 
{
    @Resource
    private ProductTradeTimeSettingMapper productTradeTimeSettingMapper;

    /**
     * 查询系统产品交易时间配置
     * 
     * @param id 系统产品交易时间配置主键
     * @return 系统产品交易时间配置
     */
    @Override
    public ProductTradeTimeSetting selectProductTradeTimeSettingById(Long id)
    {
        return productTradeTimeSettingMapper.selectProductTradeTimeSettingById(id);
    }

    /**
     * 查询系统产品交易时间配置
     *
     * @param day 天
     * @param productType 产品类型
     * @return 系统产品交易时间配置
     */
    @Override
    public ProductTradeTimeSetting selectProductTradeTimeSettingByDayAndProductType(Integer day,Integer productType){
        return productTradeTimeSettingMapper.selectProductTradeTimeSettingByDayAndProductType(day,productType);
    }

    /**
     * 查询系统产品交易时间配置列表
     * 
     * @param productTradeTimeSetting 系统产品交易时间配置
     * @return 系统产品交易时间配置
     */
    @Override
    public List<ProductTradeTimeSetting> selectProductTradeTimeSettingList(ProductTradeTimeSetting productTradeTimeSetting)
    {
        return productTradeTimeSettingMapper.selectProductTradeTimeSettingList(productTradeTimeSetting);
    }

    /**
     * 新增系统产品交易时间配置
     * 
     * @param productTradeTimeSetting 系统产品交易时间配置
     * @return 结果
     */
    @Override
    public int insertProductTradeTimeSetting(ProductTradeTimeSetting productTradeTimeSetting)
    {
        return productTradeTimeSettingMapper.insertProductTradeTimeSetting(productTradeTimeSetting);
    }

    /**
     * 修改系统产品交易时间配置
     * 
     * @param productTradeTimeSetting 系统产品交易时间配置
     * @return 结果
     */
    @Override
    public int updateProductTradeTimeSetting(ProductTradeTimeSetting productTradeTimeSetting)
    {
        return productTradeTimeSettingMapper.updateProductTradeTimeSetting(productTradeTimeSetting);
    }

    /**
     * 批量删除系统产品交易时间配置
     * 
     * @param ids 需要删除的系统产品交易时间配置主键
     * @return 结果
     */
    @Override
    public int deleteProductTradeTimeSettingByIds(Long[] ids)
    {
        return productTradeTimeSettingMapper.deleteProductTradeTimeSettingByIds(ids);
    }

    /**
     * 删除系统产品交易时间配置信息
     * 
     * @param id 系统产品交易时间配置主键
     * @return 结果
     */
    @Override
    public int deleteProductTradeTimeSettingById(Long id)
    {
        return productTradeTimeSettingMapper.deleteProductTradeTimeSettingById(id);
    }
}
