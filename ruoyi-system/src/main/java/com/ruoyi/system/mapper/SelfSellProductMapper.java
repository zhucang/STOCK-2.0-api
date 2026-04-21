package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SelfSellProduct;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 自营产品Mapper接口
 * 
 * @author ruoyi
 * @date 2023-11-27
 */
public interface SelfSellProductMapper 
{
    /**
     * 查询自营产品
     * 
     * @param id 自营产品主键
     * @return 自营产品
     */
    public SelfSellProduct selectSelfSellProductById(Long id);

    /**
     * 查询自营产品
     *
     * @param productCode 产品代码
     * @param productType 产品类型
     * @return 自营产品
     */
    public SelfSellProduct selectSelfSellProductByProductCodeAndProductType(@Param("productCode") String productCode,
                                                                            @Param("productType") Integer productType);

    /**
     * 查询自营产品列表
     * 
     * @param selfSellProduct 自营产品
     * @return 自营产品集合
     */
    public List<SelfSellProduct> selectSelfSellProductList(SelfSellProduct selfSellProduct);

    /**
     * 新增自营产品
     * 
     * @param selfSellProduct 自营产品
     * @return 结果
     */
    public int insertSelfSellProduct(SelfSellProduct selfSellProduct);

    /**
     * 修改自营产品
     * 
     * @param selfSellProduct 自营产品
     * @return 结果
     */
    public int updateSelfSellProduct(SelfSellProduct selfSellProduct);

    /**
     * 删除自营产品
     * 
     * @param id 自营产品主键
     * @return 结果
     */
    public int deleteSelfSellProductById(Long id);

    /**
     * 批量删除自营产品
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSelfSellProductByIds(Long[] ids);
}
