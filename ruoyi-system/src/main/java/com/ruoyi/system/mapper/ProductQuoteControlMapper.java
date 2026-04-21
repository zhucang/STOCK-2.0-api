package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.ProductQuoteControl;

import java.util.List;

/**
 * 产品短线行情控制Mapper接口
 * 
 * @author ruoyi
 * @date 2024-01-11
 */
public interface ProductQuoteControlMapper 
{
    /**
     * 查询产品短线行情控制
     * 
     * @param id 产品短线行情控制主键
     * @return 产品短线行情控制
     */
    public ProductQuoteControl selectProductQuoteControlById(Long id);

    /**
     * 查询产品短线行情控制列表
     * 
     * @param productQuoteControl 产品短线行情控制
     * @return 产品短线行情控制集合
     */
    public List<ProductQuoteControl> selectProductQuoteControlList(ProductQuoteControl productQuoteControl);

    /**
     * 新增产品短线行情控制
     * 
     * @param productQuoteControl 产品短线行情控制
     * @return 结果
     */
    public int insertProductQuoteControl(ProductQuoteControl productQuoteControl);

    /**
     * 修改产品短线行情控制
     * 
     * @param productQuoteControl 产品短线行情控制
     * @return 结果
     */
    public int updateProductQuoteControl(ProductQuoteControl productQuoteControl);

    /**
     * 删除产品短线行情控制
     * 
     * @param id 产品短线行情控制主键
     * @return 结果
     */
    public int deleteProductQuoteControlById(Long id);

    /**
     * 批量删除产品短线行情控制
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteProductQuoteControlByIds(Long[] ids);
}
