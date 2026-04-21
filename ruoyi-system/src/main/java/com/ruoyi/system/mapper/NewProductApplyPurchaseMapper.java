package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.NewProductApplyPurchase;

import java.util.Date;
import java.util.List;

/**
 * 新股新币申购初始配置Mapper接口
 * 
 * @author ruoyi
 * @date 2023-11-30
 */
public interface NewProductApplyPurchaseMapper 
{
    /**
     * 查询新股新币申购初始配置
     * 
     * @param id 新股新币申购初始配置主键
     * @return 新股新币申购初始配置
     */
    public NewProductApplyPurchase selectNewProductApplyPurchaseById(Long id);

    /**
     * 查询新股新币申购初始配置列表
     * 
     * @param newProductApplyPurchase 新股新币申购初始配置
     * @return 新股新币申购初始配置集合
     */
    public List<NewProductApplyPurchase> selectNewProductApplyPurchaseList(NewProductApplyPurchase newProductApplyPurchase);

    /**
     * 新增新股新币申购初始配置
     * 
     * @param newProductApplyPurchase 新股新币申购初始配置
     * @return 结果
     */
    public int insertNewProductApplyPurchase(NewProductApplyPurchase newProductApplyPurchase);

    /**
     * 修改新股新币申购初始配置
     * 
     * @param newProductApplyPurchase 新股新币申购初始配置
     * @return 结果
     */
    public int updateNewProductApplyPurchase(NewProductApplyPurchase newProductApplyPurchase);

    /**
     * 删除新股新币申购初始配置
     * 
     * @param id 新股新币申购初始配置主键
     * @return 结果
     */
    public int deleteNewProductApplyPurchaseById(Long id);

    /**
     * 批量删除新股新币申购初始配置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteNewProductApplyPurchaseByIds(Long[] ids);

    /**
     * 获取今日上市的股票新币列表
     * @return
     */
    public List<NewProductApplyPurchase> selectListingNewProduct(Date nowDate);

    /**
     * 新股新币开始申购操作
     */
    public int startApplyPurchaseNewProduct(Date nowDate);
}
