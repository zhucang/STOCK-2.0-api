package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.LangMgr;
import com.ruoyi.system.domain.StockProduct;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 股票产品信息Mapper接口
 * 
 * @author ruoyi
 * @date 2023-10-30
 */
public interface StockProductMapper 
{
    /**
     * 查询股票产品信息
     * 
     * @param id 股票产品信息主键
     * @return 股票产品信息
     */
    public StockProduct selectStockProductById(Long id);

    /**
     * 查询股票产品信息
     *
     * @param productCode 代码
     * @return 股票产品信息
     */
    public StockProduct selectStockProductByCode(String productCode);

    /**
     * 查询股票产品信息
     *
     * @param productName 名称
     * @return 股票产品信息
     */
    public StockProduct selectStockProductByName(String productName);

    /**
     * 查询股票产品信息列表
     * 
     * @param stockProduct 股票产品信息
     * @return 股票产品信息集合
     */
    public List<StockProduct> selectStockProductList(StockProduct stockProduct);

    /**
     * 新增股票产品信息
     * 
     * @param stockProduct 股票产品信息
     * @return 结果
     */
    public int insertStockProduct(StockProduct stockProduct);

    /**
     * 批量新增股票产品信息
     *
     * @param stockProducts 股票产品信息列表
     * @return 结果
     */
    public int insertStockProducts(@Param("stockProducts") List<StockProduct> stockProducts);

    /**
     * 修改股票产品信息
     * 
     * @param stockProduct 股票产品信息
     * @return 结果
     */
    public int updateStockProduct(StockProduct stockProduct);

    /**
     * 删除股票产品信息
     * 
     * @param id 股票产品信息主键
     * @return 结果
     */
    public int deleteStockProductById(Long id);

    /**
     * 批量删除股票产品信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStockProductByIds(Long[] ids);

    /**
     * 修改产品锁定状态
     * @param ids
     * @param status 是否锁定 0：否 1：是
     * @return
     */
    int updateLock(@Param("ids") List<Long> ids,@Param("status") Integer status);

    /**
     * 修改产品显示状态
     * @param ids
     * @param status 是否显示 0：是 1：否
     * @return
     */
    int updateShow(@Param("ids") List<Long> ids,@Param("status") Integer status);

    /**
     * 修改产品显示状态
     * @param ids
     * @param positionIncomeCoefficient 合约收益系数
     * @return
     */
    public int batchUpdatePositionIncomeCoefficient(@Param("ids") List<Long> ids, @Param("positionIncomeCoefficient") BigDecimal positionIncomeCoefficient);

    /**
     * 更新产品名称多语言
     * @param lang
     * @return
     */
    int updateProductNameLang(LangMgr lang);
}
