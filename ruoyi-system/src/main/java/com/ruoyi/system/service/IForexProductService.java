package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.ForexProduct;
import com.ruoyi.system.domain.LangMgr;

import java.math.BigDecimal;
import java.util.List;

/**
 * 外汇产品信息Service接口
 * 
 * @author ruoyi
 * @date 2023-11-01
 */
public interface IForexProductService 
{
    /**
     * 查询外汇产品信息
     * 
     * @param id 外汇产品信息主键
     * @return 外汇产品信息
     */
    public ForexProduct selectForexProductById(Long id);

    /**
     * 填充行情信息
     * @param product 产品信息
     */
    public void fillProductQuote(ForexProduct product);

    /**
     * 查询外汇产品信息
     *
     * @param productCode 产品代码
     * @return 外汇产品信息
     */
    public ForexProduct selectForexProductByCode(String productCode);

    /**
     * 查询外汇产品信息列表
     * 
     * @param forexProduct 外汇产品信息
     * @return 外汇产品信息集合
     */
    public List<ForexProduct> selectForexProductList(ForexProduct forexProduct);

    /**
     * 填充行情信息
     * @param products 产品信息列表
     */
    public void fillProductQuote(List<ForexProduct> products);

    /**
     * 填充产品自选标识
     * @param products 产品信息列表
     * @param userId 用户id
     */
    public void fillIsOption(List<ForexProduct> products, Long userId);

    /**
     * 新增外汇产品信息
     *
     * @param products 产品信息
     * @return 结果
     */
    public int addProducts(List<ForexProduct> products);

    /**
     * 修改外汇产品信息
     * 
     * @param forexProduct 外汇产品信息
     * @return 结果
     */
    public int updateForexProduct(ForexProduct forexProduct);

    /**
     * 修改外汇名称多语言
     * @param productId 产品id
     * @param productNameLang 产品名称语言包
     * @return
     */
    public int updateProductNameLang(Long productId, LangMgr productNameLang);

    /**
     * 批量删除外汇产品信息
     * 
     * @param ids 需要删除的外汇产品信息主键集合
     * @return 结果
     */
    public int deleteForexProductByIds(Long[] ids);

    /**
     * 删除外汇产品信息信息
     * 
     * @param id 外汇产品信息主键
     * @return 结果
     */
    public int deleteForexProductById(Long id);

    /**
     * 修改产品锁定状态
     * @param ids
     * @param status 是否锁定 0：否 1：是
     * @return
     */
    public int updateLock(List<Long> ids, Integer status);

    /**
     * 修改产品显示状态
     * @param ids
     * @param status 是否显示 0：是 1：否
     * @return
     */
    public int updateShow(List<Long> ids, Integer status);

    /**
     * 批量修改产品合约收益系数
     * @param ids
     * @param positionIncomeCoefficient
     * @return
     */
    public int batchUpdatePositionIncomeCoefficient(List<Long> ids, BigDecimal positionIncomeCoefficient);

    /**
     * 获取K线
     * @param code 产品代码
     * @param time 时间跨度
     * @param timespan 时间类型 (minute，day，hour，week。。。。)
     * @return
     */
    public AjaxResult getKLine_Echarts(String code,Integer time,String timespan);
}
