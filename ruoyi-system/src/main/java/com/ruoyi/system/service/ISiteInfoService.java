package com.ruoyi.system.service;

import com.ruoyi.system.domain.SiteInfo;

/**
 * 平台基本信息Service接口
 * 
 * @author ruoyi
 * @date 2023-11-13
 */
public interface ISiteInfoService 
{
    /**
     * 查询平台基本信息
     * 
     * @param id 平台基本信息主键
     * @return 平台基本信息
     */
    public SiteInfo selectSiteInfoById(Long id);

//    /**
//     * 查询平台基本信息列表
//     *
//     * @param siteInfo 平台基本信息
//     * @return 平台基本信息集合
//     */
//    public List<SiteInfo> selectSiteInfoList(SiteInfo siteInfo);
//
//    /**
//     * 新增平台基本信息
//     *
//     * @param siteInfo 平台基本信息
//     * @return 结果
//     */
//    public int insertSiteInfo(SiteInfo siteInfo);

    /**
     * 修改平台基本信息
     * 
     * @param siteInfo 平台基本信息
     * @return 结果
     */
    public int updateSiteInfo(SiteInfo siteInfo);

//    /**
//     * 批量删除平台基本信息
//     *
//     * @param ids 需要删除的平台基本信息主键集合
//     * @return 结果
//     */
//    public int deleteSiteInfoByIds(Long[] ids);
//
//    /**
//     * 删除平台基本信息信息
//     *
//     * @param id 平台基本信息主键
//     * @return 结果
//     */
//    public int deleteSiteInfoById(Long id);
}
