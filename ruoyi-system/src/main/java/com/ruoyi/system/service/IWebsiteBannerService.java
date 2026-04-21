package com.ruoyi.system.service;

import com.ruoyi.system.domain.WebsiteBanner;

import java.util.List;

/**
 * 官网B图片Service接口
 * 
 * @author ruoyi
 * @date 2023-12-09
 */
public interface IWebsiteBannerService 
{
    /**
     * 查询官网B图片
     * 
     * @param id 官网B图片主键
     * @return 官网B图片
     */
    public WebsiteBanner selectWebsiteBannerById(Long id);

    /**
     * 查询官网B图片列表
     * 
     * @param websiteBanner 官网B图片
     * @return 官网B图片集合
     */
    public List<WebsiteBanner> selectWebsiteBannerList(WebsiteBanner websiteBanner);

    /**
     * 新增官网B图片
     * 
     * @param websiteBanner 官网B图片
     * @return 结果
     */
    public int insertWebsiteBanner(WebsiteBanner websiteBanner);

    /**
     * 修改官网B图片
     * 
     * @param websiteBanner 官网B图片
     * @return 结果
     */
    public int updateWebsiteBanner(WebsiteBanner websiteBanner);

    /**
     * 批量删除官网B图片
     * 
     * @param ids 需要删除的官网B图片主键集合
     * @return 结果
     */
    public int deleteWebsiteBannerByIds(Long[] ids);

    /**
     * 删除官网B图片信息
     * 
     * @param id 官网B图片主键
     * @return 结果
     */
    public int deleteWebsiteBannerById(Long id);
}
