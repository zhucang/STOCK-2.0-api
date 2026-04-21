package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.CryptocurrencyProduct;
import com.ruoyi.system.domain.LangMgr;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 加密货币产品信息Mapper接口
 * 
 * @author ruoyi
 * @date 2023-11-01
 */
public interface CryptocurrencyProductMapper 
{
    /**
     * 查询加密货币产品信息
     * 
     * @param id 加密货币产品信息主键
     * @return 加密货币产品信息
     */
    public CryptocurrencyProduct selectCryptocurrencyProductById(Long id);

    /**
     * 查询加密货币产品信息
     *
     * @param productCode 产品代码
     * @return 加密货币产品信息
     */
    public CryptocurrencyProduct selectCryptocurrencyProductByCode(String productCode);

    /**
     * 查询加密货币产品信息
     *
     * @param productName 产品名称
     * @return 加密货币产品信息
     */
    public CryptocurrencyProduct selectCryptocurrencyProductByName(String productName);

    /**
     * 查询加密货币产品信息列表
     * 
     * @param cryptocurrencyProduct 加密货币产品信息
     * @return 加密货币产品信息集合
     */
    public List<CryptocurrencyProduct> selectCryptocurrencyProductList(CryptocurrencyProduct cryptocurrencyProduct);

    /**
     * 新增加密货币产品信息
     * 
     * @param cryptocurrencyProduct 加密货币产品信息
     * @return 结果
     */
    public int insertCryptocurrencyProduct(CryptocurrencyProduct cryptocurrencyProduct);

    /**
     * 批量新增加密货币产品信息
     *
     * @param products 加密货币产品信息列表
     * @return 结果
     */
    public int insertCryptocurrencyProducts(@Param("products") List<CryptocurrencyProduct> products);

    /**
     * 修改加密货币产品信息
     * 
     * @param cryptocurrencyProduct 加密货币产品信息
     * @return 结果
     */
    public int updateCryptocurrencyProduct(CryptocurrencyProduct cryptocurrencyProduct);

    /**
     * 删除加密货币产品信息
     * 
     * @param id 加密货币产品信息主键
     * @return 结果
     */
    public int deleteCryptocurrencyProductById(Long id);

    /**
     * 批量删除加密货币产品信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCryptocurrencyProductByIds(Long[] ids);

    /**
     * 修改产品锁定状态
     * @param ids
     * @param status 是否锁定 0：否 1：是
     * @return
     */
    int updateLock(@Param("ids") List<Long> ids, @Param("status") Integer status);

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
