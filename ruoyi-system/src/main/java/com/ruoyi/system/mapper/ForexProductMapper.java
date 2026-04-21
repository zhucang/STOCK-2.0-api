package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.ForexProduct;
import com.ruoyi.system.domain.LangMgr;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 外汇产品信息Mapper接口
 * 
 * @author ruoyi
 * @date 2023-11-01
 */
public interface ForexProductMapper 
{
    /**
     * 查询外汇产品信息
     * 
     * @param id 外汇产品信息主键
     * @return 外汇产品信息
     */
    public ForexProduct selectForexProductById(Long id);

    /**
     * 查询外汇产品信息
     *
     * @param productCode 产品代码
     * @return 外汇产品信息
     */
    public ForexProduct selectForexProductByCode(String productCode);

    /**
     * 查询外汇产品信息
     *
     * @param productName 产品名称
     * @return 外汇产品信息
     */
    public ForexProduct selectForexProductByName(String productName);

    /**
     * 查询外汇产品信息列表
     * 
     * @param forexProduct 外汇产品信息
     * @return 外汇产品信息集合
     */
    public List<ForexProduct> selectForexProductList(ForexProduct forexProduct);

    /**
     * 新增外汇产品信息
     * 
     * @param forexProduct 外汇产品信息
     * @return 结果
     */
    public int insertForexProduct(ForexProduct forexProduct);

    /**
     * 批量新增外汇产品信息
     *
     * @param products 外汇产品信息列表
     * @return 结果
     */
    public int insertForexProducts(@Param("products") List<ForexProduct> products);

    /**
     * 修改外汇产品信息
     * 
     * @param forexProduct 外汇产品信息
     * @return 结果
     */
    public int updateForexProduct(ForexProduct forexProduct);

    /**
     * 删除外汇产品信息
     * 
     * @param id 外汇产品信息主键
     * @return 结果
     */
    public int deleteForexProductById(Long id);

    /**
     * 批量删除外汇产品信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteForexProductByIds(Long[] ids);

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
