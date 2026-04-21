package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SiteBanner;

import java.util.List;

/**
 * banner横幅图片配置Mapper接口
 * 
 * @author ruoyi
 * @date 2023-11-04
 */
public interface SiteBannerMapper 
{
    /**
     * 查询banner横幅图片配置
     * 
     * @param id banner横幅图片配置主键
     * @return banner横幅图片配置
     */
    public SiteBanner selectSiteBannerById(Long id);

    /**
     * 查询banner横幅图片配置列表
     * 
     * @param siteBanner banner横幅图片配置
     * @return banner横幅图片配置集合
     */
    public List<SiteBanner> selectSiteBannerList(SiteBanner siteBanner);

    /**
     * 新增banner横幅图片配置
     * 
     * @param siteBanner banner横幅图片配置
     * @return 结果
     */
    public int insertSiteBanner(SiteBanner siteBanner);

    /**
     * 修改banner横幅图片配置
     * 
     * @param siteBanner banner横幅图片配置
     * @return 结果
     */
    public int updateSiteBanner(SiteBanner siteBanner);

    /**
     * 删除banner横幅图片配置
     * 
     * @param id banner横幅图片配置主键
     * @return 结果
     */
    public int deleteSiteBannerById(Long id);

    /**
     * 批量删除banner横幅图片配置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSiteBannerByIds(Long[] ids);
}
