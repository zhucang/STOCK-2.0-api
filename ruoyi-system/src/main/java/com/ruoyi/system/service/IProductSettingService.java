package com.ruoyi.system.service;

import com.ruoyi.system.domain.ProductSetting;

import java.util.List;

/**
 * 产品交易设置（产品风控）Service接口
 * 
 * @author ruoyi
 * @date 2023-12-01
 */
public interface IProductSettingService 
{
    /**
     * 查询产品交易设置（产品风控）
     * 
     * @param id 产品交易设置（产品风控）主键
     * @return 产品交易设置（产品风控）
     */
    public ProductSetting selectProductSettingById(Long id);

    /**
     * 查询产品交易设置（产品风控）
     *
     * @param productType 产品类型
     * @return 产品交易设置（产品风控）
     */
    public ProductSetting selectProductSettingByProductType(Integer productType);

    /**
     * 查询产品交易设置（产品风控）列表
     * 
     * @param productSetting 产品交易设置（产品风控）
     * @return 产品交易设置（产品风控）集合
     */
    public List<ProductSetting> selectProductSettingList(ProductSetting productSetting);

    /**
     * 新增产品交易设置（产品风控）
     * 
     * @param productSetting 产品交易设置（产品风控）
     * @return 结果
     */
    public int insertProductSetting(ProductSetting productSetting);

    /**
     * 修改产品交易设置（产品风控）
     * 
     * @param productSetting 产品交易设置（产品风控）
     * @return 结果
     */
    public int updateProductSetting(ProductSetting productSetting);

    /**
     * 批量删除产品交易设置（产品风控）
     * 
     * @param ids 需要删除的产品交易设置（产品风控）主键集合
     * @return 结果
     */
    public int deleteProductSettingByIds(Long[] ids);

    /**
     * 删除产品交易设置（产品风控）信息
     * 
     * @param id 产品交易设置（产品风控）主键
     * @return 结果
     */
    public int deleteProductSettingById(Long id);
}
