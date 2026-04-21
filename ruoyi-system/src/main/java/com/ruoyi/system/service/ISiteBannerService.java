package com.ruoyi.system.service;

import com.ruoyi.system.domain.LangMgr;
import com.ruoyi.system.domain.SiteBanner;

import java.util.List;

/**
 * banner横幅图片配置Service接口
 * 
 * @author ruoyi
 * @date 2023-11-04
 */
public interface ISiteBannerService 
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
     * 修改banner横幅图片多语言配置
     * @param siteBannerId bannerId
     * @param bannerImgLang 横幅图片语言包
     * @return
     */
    public int updateBannerImgLang(Long siteBannerId, LangMgr bannerImgLang);

    /**
     * 修改banner横幅标题多语言
     * @param siteBannerId bannerId
     * @param bannerTitleLang 横幅标题语言包
     * @return
     */
    public int updateBannerTitleLang(Long siteBannerId, LangMgr bannerTitleLang);

    /**
     * 修改banner横幅内容多语言
     * @param siteBannerId bannerId
     * @param bannerContentLang 横幅内容语言包
     * @return
     */
    public int updateBannerContentLang(Long siteBannerId, LangMgr bannerContentLang);

    /**
     * 批量删除banner横幅图片配置
     *
     * @param ids 需要删除的banner横幅图片配置主键集合
     * @return 结果
     */
    public int deleteSiteBannerByIds(Long[] ids);

    /**
     * 删除banner横幅图片配置信息
     * 
     * @param id banner横幅图片配置主键
     * @return 结果
     */
    public int deleteSiteBannerById(Long id);
}
