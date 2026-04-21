package com.ruoyi.system.service.impl;

import com.ruoyi.common.constant.CacheableKey;
import com.ruoyi.system.domain.WebsiteBanner;
import com.ruoyi.system.mapper.WebsiteBannerMapper;
import com.ruoyi.system.service.IWebsiteBannerService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 官网B图片Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-12-09
 */
@Service
public class WebsiteBannerServiceImpl implements IWebsiteBannerService 
{
    @Resource
    private WebsiteBannerMapper websiteBannerMapper;

    /**
     * 查询官网B图片
     * 
     * @param id 官网B图片主键
     * @return 官网B图片
     */
    @Override
//    @Cacheable(value = CacheableKey.WEBSITE_BANNER + CacheableKey.ENTITY,key = "#id")
    public WebsiteBanner selectWebsiteBannerById(Long id)
    {
        return websiteBannerMapper.selectWebsiteBannerById(id);
    }

    /**
     * 查询官网B图片列表
     * 
     * @param websiteBanner 官网B图片
     * @return 官网B图片
     */
    @Override
    @Cacheable(value = CacheableKey.WEBSITE_BANNER + CacheableKey.LIST,key = "#websiteBanner.cacheableKey()")
    public List<WebsiteBanner> selectWebsiteBannerList(WebsiteBanner websiteBanner)
    {
        return websiteBannerMapper.selectWebsiteBannerList(websiteBanner);
    }

    /**
     * 新增官网B图片
     * 
     * @param websiteBanner 官网B图片
     * @return 结果
     */
    @Override
    @CacheEvict(value = CacheableKey.WEBSITE_BANNER + CacheableKey.LIST,allEntries = true)
    public int insertWebsiteBanner(WebsiteBanner websiteBanner)
    {
        return websiteBannerMapper.insertWebsiteBanner(websiteBanner);
    }

    /**
     * 修改官网B图片
     * 
     * @param websiteBanner 官网B图片
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.WEBSITE_BANNER + CacheableKey.ENTITY,key = "#websiteBanner.id"),
            @CacheEvict(value = CacheableKey.WEBSITE_BANNER + CacheableKey.LIST,allEntries = true)})
    public int updateWebsiteBanner(WebsiteBanner websiteBanner)
    {
        websiteBanner.setBannerType(null);
        return websiteBannerMapper.updateWebsiteBanner(websiteBanner);
    }

    /**
     * 批量删除官网B图片
     * 
     * @param ids 需要删除的官网B图片主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.WEBSITE_BANNER + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.WEBSITE_BANNER + CacheableKey.LIST,allEntries = true)})
    public int deleteWebsiteBannerByIds(Long[] ids)
    {
        return websiteBannerMapper.deleteWebsiteBannerByIds(ids);
    }

    /**
     * 删除官网B图片信息
     * 
     * @param id 官网B图片主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.WEBSITE_BANNER + CacheableKey.ENTITY,key = "#id"),
            @CacheEvict(value = CacheableKey.WEBSITE_BANNER + CacheableKey.LIST,allEntries = true)})
    public int deleteWebsiteBannerById(Long id)
    {
        return websiteBannerMapper.deleteWebsiteBannerById(id);
    }
}
