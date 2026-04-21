package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.WebsiteBanner;

import java.util.List;

/**
 * 官网B图片Mapper接口
 * 
 * @author ruoyi
 * @date 2023-12-09
 */
public interface WebsiteBannerMapper 
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
     * 删除官网B图片
     * 
     * @param id 官网B图片主键
     * @return 结果
     */
    public int deleteWebsiteBannerById(Long id);

    /**
     * 批量删除官网B图片
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWebsiteBannerByIds(Long[] ids);
}
