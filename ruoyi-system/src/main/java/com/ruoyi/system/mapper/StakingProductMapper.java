package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.StakingProduct;

import java.util.List;

/**
 * 质押产品配置Mapper接口
 * 
 * @author ruoyi
 * @date 2025-07-17
 */
public interface StakingProductMapper 
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
     * 删除质押产品配置
     * 
     * @param id 质押产品配置主键
     * @return 结果
     */
    public int deleteStakingProductById(Long id);

    /**
     * 批量删除质押产品配置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStakingProductByIds(Long[] ids);
}
