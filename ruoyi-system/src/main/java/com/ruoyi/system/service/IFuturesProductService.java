package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.FuturesProduct;
import com.ruoyi.system.domain.LangMgr;

import java.math.BigDecimal;
import java.util.List;

/**
 * 期货产品信息Service接口
 * 
 * @author ruoyi
 * @date 2023-11-01
 */
public interface IFuturesProductService 
{
    /**
     * 查询期货产品信息
     * 
     * @param id 期货产品信息主键
     * @return 期货产品信息
     */
    public FuturesProduct selectFuturesProductById(Long id);

    /**
     * 填充行情信息
     * @param product 产品信息
     */
    public void fillProductQuote(FuturesProduct product);

    /**
     * 查询期货产品信息
     *
     * @param productCode 产品代码
     * @return 期货产品信息
     */
    public FuturesProduct selectFuturesProductByCode(String productCode);

    /**
     * 查询期货产品信息列表
     * 
     * @param futuresProduct 期货产品信息
     * @return 期货产品信息集合
     */
    public List<FuturesProduct> selectFuturesProductList(FuturesProduct futuresProduct);

    /**
     * 填充行情信息
     * @param products 产品信息列表
     */
    public void fillProductQuote(List<FuturesProduct> products);

    /**
     * 填充产品自选标识
     * @param products 产品信息列表
     * @param userId 用户id
     */
    public void fillIsOption(List<FuturesProduct> products, Long userId);

    /**
     * 修改期货产品信息
     * 
     * @param futuresProduct 期货产品信息
     * @return 结果
     */
    public int updateFuturesProduct(FuturesProduct futuresProduct);

    /**
     * 修改期货名称多语言
     * @param productId 产品id
     * @param productNameLang 产品名称语言包
     * @return
     */
    public int updateProductNameLang(Long productId, LangMgr productNameLang);

    /**
     * 批量删除期货产品信息
     * 
     * @param ids 需要删除的期货产品信息主键集合
     * @return 结果
     */
    public int deleteFuturesProductByIds(Long[] ids);

    /**
     * 删除期货产品信息信息
     * 
     * @param id 期货产品信息主键
     * @return 结果
     */
    public int deleteFuturesProductById(Long id);

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
