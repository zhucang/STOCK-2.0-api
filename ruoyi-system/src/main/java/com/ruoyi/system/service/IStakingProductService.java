package com.ruoyi.system.service;

import com.ruoyi.system.domain.LangMgr;
import com.ruoyi.system.domain.StakingProduct;

import java.util.List;

/**
 * 质押产品配置Service接口
 * 
 * @author ruoyi
 * @date 2025-07-17
 */
public interface IStakingProductService 
{
    /**
     * 查询质押产品配置
     * 
     * @param id 质押产品配置主键
     * @return 质押产品配置
     */
    public StakingProduct selectStakingProductById(Long id);

    /**
     * 查询质押产品配置列表
     * 
     * @param stakingProduct 质押产品配置
     * @return 质押产品配置集合
     */
    public List<StakingProduct> selectStakingProductList(StakingProduct stakingProduct);

    /**
     * 新增质押产品配置
     * 
     * @param stakingProduct 质押产品配置
     * @return 结果
     */
    public int insertStakingProduct(StakingProduct stakingProduct);

    /**
     * 修改质押产品配置
     * 
     * @param stakingProduct 质押产品配置
     * @return 结果
     */
    public int updateStakingProduct(StakingProduct stakingProduct);

    /**
     * 修改质押名称多语言
     * @param stakingProductId 币种配置id
     * @param stakingNameLang 质押名称语言包
     * @return
     */
    public int updateStakingNameLang(Long stakingProductId, LangMgr stakingNameLang);

    /**
     * 批量删除质押产品配置
     * 
     * @param ids 需要删除的质押产品配置主键集合
     * @return 结果
     */
    public int deleteStakingProductByIds(Long[] ids);

    /**
     * 删除质押产品配置信息
     * 
     * @param id 质押产品配置主键
     * @return 结果
     */
    public int deleteStakingProductById(Long id);
}
