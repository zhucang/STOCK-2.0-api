package com.ruoyi.system.service;

import com.ruoyi.system.domain.EchartsDataVO;
import com.ruoyi.system.domain.LangMgr;
import com.ruoyi.system.domain.StockProduct;

import java.math.BigDecimal;
import java.util.List;

/**
 * 股票产品信息Service接口
 * 
 * @author ruoyi
 * @date 2023-11-01
 */
public interface IStockProductService 
{
    /**
     * 查询股票产品信息
     * 
     * @param id 股票产品信息主键
     * @return 股票产品信息
     */
    public StockProduct selectStockProductById(Long id);

    /**
     * 填充行情信息
     * @param product 产品信息
     */
    public void fillProductQuote(StockProduct product);

    /**
     * 查询股票产品信息
     *
     * @param productCode 代码
     * @return 股票产品信息
     */
    public StockProduct selectStockProductByCode(String productCode);

    /**
     * 查询股票产品信息列表
     * 
     * @param stockProduct 股票产品信息
     * @return 股票产品信息集合
     */
    public List<StockProduct> selectStockProductList(StockProduct stockProduct);

    /**
     * 填充产品行情信息
     * @param products
     * @return
     */
    public void fillProductQuote(List<StockProduct> products);

    /**
     * 填充产品自选标识
     * @param products 产品信息列表
     * @param userId 用户id
     */
    public void fillIsOption(List<StockProduct> products, Long userId);

    /**
     * 新增股票产品信息
     *
     * @param stockProduct 股票产品信息
     * @return 结果
     */
    public int insertStockProduct(StockProduct stockProduct);

    /**
     * 新增股票产品信息
     *
     * @param products 产品信息
     * @return 结果
     */
    public int addProducts(List<StockProduct> products);

    /**
     * 修改股票产品信息
     * 
     * @param stockProduct 股票产品信息
     * @return 结果
     */
    public int updateStockProduct(StockProduct stockProduct);

    /**
     * 修改股票名称多语言
     * @param productId 产品id
     * @param productNameLang 产品名称语言包
     * @return
     */
    public int updateProductNameLang(Long productId, LangMgr productNameLang);

    /**
     * 批量删除股票产品信息
     * 
     * @param ids 需要删除的股票产品信息主键集合
     * @return 结果
     */
    public int deleteStockProductByIds(Long[] ids);

    /**
     * 删除股票产品信息信息
     * 
     * @param id 股票产品信息主键
     * @return 结果
     */
    public int deleteStockProductById(Long id);

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
    public EchartsDataVO getKLine_Echarts(String code, Integer time, String timespan);
}
