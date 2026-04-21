package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.HomeRecommendProducts;

import java.util.List;

/**
 * 首页推荐产品Mapper接口
 * 
 * @author ruoyi
 * @date 2024-01-09
 */
public interface HomeRecommendProductsMapper 
{
    /**
     * 查询首页推荐产品
     * 
     * @param id 首页推荐产品主键
     * @return 首页推荐产品
     */
    public HomeRecommendProducts selectHomeRecommendProductsById(Long id);

    /**
     * 查询首页推荐产品列表
     * 
     * @param homeRecommendProducts 首页推荐产品
     * @return 首页推荐产品集合
     */
    public List<HomeRecommendProducts> selectHomeRecommendProductsList(HomeRecommendProducts homeRecommendProducts);

    /**
     * 新增首页推荐产品
     * 
     * @param homeRecommendProducts 首页推荐产品
     * @return 结果
     */
    public int insertHomeRecommendProducts(HomeRecommendProducts homeRecommendProducts);

    /**
     * 修改首页推荐产品
     * 
     * @param homeRecommendProducts 首页推荐产品
     * @return 结果
     */
    public int updateHomeRecommendProducts(HomeRecommendProducts homeRecommendProducts);

    /**
     * 修改首页推荐产品
     * @param homeRecommendProducts
     * @return
     */
    public int updateHomeRecommendProductByProductId(HomeRecommendProducts homeRecommendProducts);

    /**
     * 删除首页推荐产品
     * 
     * @param id 首页推荐产品主键
     * @return 结果
     */
    public int deleteHomeRecommendProductsById(Long id);

    /**
     * 批量删除首页推荐产品
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteHomeRecommendProductsByIds(Long[] ids);
}
