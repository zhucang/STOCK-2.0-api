package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.FuturesProduct;
import com.ruoyi.system.domain.LangMgr;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 期货产品信息Mapper接口
 * 
 * @author ruoyi
 * @date 2023-11-01
 */
public interface FuturesProductMapper 
{
    /**
     * 查询期货产品信息
     * 
     * @param id 期货产品信息主键
     * @return 期货产品信息
     */
    public FuturesProduct selectFuturesProductById(Long id);

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
     * 新增期货产品信息
     * 
     * @param futuresProduct 期货产品信息
     * @return 结果
     */
    public int insertFuturesProduct(FuturesProduct futuresProduct);

    /**
     * 修改期货产品信息
     * 
     * @param futuresProduct 期货产品信息
     * @return 结果
     */
    public int updateFuturesProduct(FuturesProduct futuresProduct);

    /**
     * 删除期货产品信息
     * 
     * @param id 期货产品信息主键
     * @return 结果
     */
    public int deleteFuturesProductById(Long id);

    /**
     * 批量删除期货产品信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFuturesProductByIds(Long[] ids);


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
