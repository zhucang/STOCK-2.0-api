package com.ruoyi.system.service;

import com.ruoyi.system.domain.FinancialProduct;
import com.ruoyi.system.domain.LangMgr;

import java.util.List;

/**
 * 理财产品配置Service接口
 * 
 * @author ruoyi
 * @date 2023-11-26
 */
public interface IFinancialProductService 
{
    /**
     * 查询理财产品配置
     * 
     * @param id 理财产品配置主键
     * @return 理财产品配置
     */
    public FinancialProduct selectFinancialProductById(Long id);

    /**
     * 查询理财产品配置列表
     * 
     * @param financialProduct 理财产品配置
     * @return 理财产品配置集合
     */
    public List<FinancialProduct> selectFinancialProductList(FinancialProduct financialProduct);

    /**
     * 新增理财产品配置
     * 
     * @param financialProduct 理财产品配置
     * @return 结果
     */
    public int insertFinancialProduct(FinancialProduct financialProduct);

    /**
     * 修改理财产品配置
     * 
     * @param financialProduct 理财产品配置
     * @return 结果
     */
    public int updateFinancialProduct(FinancialProduct financialProduct);

    /**
     * 修改理财名称多语言
     * @param financialProductId 币种配置id
     * @param financialNameLang 币种名称语言包
     * @return
     */
    public int updateFinancialNameLang(Long financialProductId, LangMgr financialNameLang);

    /**
     * 批量删除理财产品配置
     * 
     * @param ids 需要删除的理财产品配置主键集合
     * @return 结果
     */
    public int deleteFinancialProductByIds(Long[] ids);

    /**
     * 删除理财产品配置信息
     * 
     * @param id 理财产品配置主键
     * @return 结果
     */
    public int deleteFinancialProductById(Long id);
}
