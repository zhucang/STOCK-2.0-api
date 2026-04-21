package com.ruoyi.system.service.impl;

import com.ruoyi.common.constant.CacheableKey;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.LangMgr;
import com.ruoyi.system.domain.SiteBanner;
import com.ruoyi.system.mapper.SiteBannerMapper;
import com.ruoyi.system.service.ISiteBannerService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * banner横幅图片配置Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-11-04
 */
@Service
public class SiteBannerServiceImpl implements ISiteBannerService 
{
    @Resource
    private SiteBannerMapper siteBannerMapper;

    /**
     * 查询banner横幅图片配置
     * 
     * @param id banner横幅图片配置主键
     * @return banner横幅图片配置
     */
    @Override
//    @Cacheable(value = CacheableKey.SITE_BANNER + CacheableKey.ENTITY,key = "#id")
    public SiteBanner selectSiteBannerById(Long id)
    {
        return siteBannerMapper.selectSiteBannerById(id);
    }

    /**
     * 查询banner横幅图片配置列表
     * 
     * @param siteBanner banner横幅图片配置
     * @return banner横幅图片配置
     */
    @Override
    @Cacheable(value = CacheableKey.SITE_BANNER + CacheableKey.LIST,key = "#siteBanner.cacheableKey()")
    public List<SiteBanner> selectSiteBannerList(SiteBanner siteBanner)
    {
        return siteBannerMapper.selectSiteBannerList(siteBanner);
    }

    /**
     * 新增banner横幅图片配置
     * 
     * @param siteBanner banner横幅图片配置
     * @return 结果
     */
    @Override
    @CacheEvict(value = CacheableKey.SITE_BANNER + CacheableKey.LIST,allEntries = true)
    public int insertSiteBanner(SiteBanner siteBanner)
    {
        siteBanner.setCreateTime(DateUtils.getNowDate());
        return siteBannerMapper.insertSiteBanner(siteBanner);
    }

    /**
     * 修改banner横幅图片配置
     * 
     * @param siteBanner banner横幅图片配置
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.SITE_BANNER + CacheableKey.ENTITY,key = "#siteBanner.id"),
            @CacheEvict(value = CacheableKey.SITE_BANNER + CacheableKey.LIST,allEntries = true)})
    public int updateSiteBanner(SiteBanner siteBanner)
    {
        siteBanner.getParams().put("showTime","update");
        return siteBannerMapper.updateSiteBanner(siteBanner);
    }

    /**
     * 修改banner横幅图片多语言配置
     * @param siteBannerId bannerId
     * @param bannerImgLang 横幅图片语言包
     * @return
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.SITE_BANNER + CacheableKey.ENTITY,key = "#siteBannerId"),
            @CacheEvict(value = CacheableKey.SITE_BANNER + CacheableKey.LIST,allEntries = true)})
    public int updateBannerImgLang(Long siteBannerId, LangMgr bannerImgLang){
        SiteBanner siteBanner = new SiteBanner();
        siteBanner.setId(siteBannerId);
        siteBanner.setBannerImgLang(bannerImgLang);
        return siteBannerMapper.updateSiteBanner(siteBanner);
    }

    /**
     * 修改banner横幅标题多语言
     * @param siteBannerId bannerId
     * @param bannerTitleLang 横幅标题语言包
     * @return
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.SITE_BANNER + CacheableKey.ENTITY,key = "#siteBannerId"),
            @CacheEvict(value = CacheableKey.SITE_BANNER + CacheableKey.LIST,allEntries = true)})
    public int updateBannerTitleLang(Long siteBannerId, LangMgr bannerTitleLang){
        SiteBanner siteBanner = new SiteBanner();
        siteBanner.setId(siteBannerId);
        siteBanner.setBannerTitleLang(bannerTitleLang);
        return siteBannerMapper.updateSiteBanner(siteBanner);
    }

    /**
     * 修改banner横幅内容多语言
     * @param siteBannerId bannerId
     * @param bannerContentLang 横幅内容语言包
     * @return
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.SITE_BANNER + CacheableKey.ENTITY,key = "#siteBannerId"),
            @CacheEvict(value = CacheableKey.SITE_BANNER + CacheableKey.LIST,allEntries = true)})
    public int updateBannerContentLang(Long siteBannerId, LangMgr bannerContentLang){
        SiteBanner siteBanner = new SiteBanner();
        siteBanner.setId(siteBannerId);
        siteBanner.setBannerContentLang(bannerContentLang);
        return siteBannerMapper.updateSiteBanner(siteBanner);
    }

    /**
     * 批量删除banner横幅图片配置
     * 
     * @param ids 需要删除的banner横幅图片配置主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.SITE_BANNER + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.SITE_BANNER + CacheableKey.LIST,allEntries = true)})
    public int deleteSiteBannerByIds(Long[] ids)
    {
        return siteBannerMapper.deleteSiteBannerByIds(ids);
    }

    /**
     * 删除banner横幅图片配置信息
     * 
     * @param id banner横幅图片配置主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.SITE_BANNER + CacheableKey.ENTITY,key = "#id"),
            @CacheEvict(value = CacheableKey.SITE_BANNER + CacheableKey.LIST,allEntries = true)})
    public int deleteSiteBannerById(Long id)
    {
        return siteBannerMapper.deleteSiteBannerById(id);
    }
}
