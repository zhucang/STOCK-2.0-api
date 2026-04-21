package com.ruoyi.system.service;

import com.ruoyi.system.domain.LangMgr;
import com.ruoyi.system.domain.SelfSellProduct;

import java.util.List;

/**
 * 自营产品Service接口
 * 
 * @author ruoyi
 * @date 2023-11-27
 */
public interface ISelfSellProductService 
{
    /**
     * 查询自营产品
     * 
     * @param id 自营产品主键
     * @return 自营产品
     */
    public SelfSellProduct selectSelfSellProductById(Long id);

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
     * 修改自营产品多语言配置
     * @param selfSellProductId 自营产品id
     * @param productNameLangLang 产品名称多语言
     * @return
     */

    public int updateProductNameLangLang(Long selfSellProductId, LangMgr productNameLangLang);

    /**
     * 批量删除自营产品
     * 
     * @param ids 需要删除的自营产品主键集合
     * @return 结果
     */
    public int deleteSelfSellProductByIds(Long[] ids);

    /**
     * 删除自营产品信息
     * 
     * @param id 自营产品主键
     * @return 结果
     */
    public int deleteSelfSellProductById(Long id);
}
