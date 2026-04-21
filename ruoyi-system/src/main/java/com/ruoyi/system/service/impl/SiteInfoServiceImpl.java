package com.ruoyi.system.service.impl;

import com.ruoyi.common.constant.CacheableKey;
import com.ruoyi.system.domain.SiteInfo;
import com.ruoyi.system.mapper.SiteInfoMapper;
import com.ruoyi.system.service.ISiteInfoService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 平台基本信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-11-13
 */
@Service
public class SiteInfoServiceImpl implements ISiteInfoService 
{
    @Resource
    private SiteInfoMapper siteInfoMapper;

    /**
     * 查询平台基本信息
     * 
     * @param id 平台基本信息主键
     * @return 平台基本信息
     */
    @Override
    @Cacheable(value = CacheableKey.SITE_INFO,key = "#id")
    public SiteInfo selectSiteInfoById(Long id)
    {
        return siteInfoMapper.selectSiteInfoById(id);
    }

//    /**
//     * 查询平台基本信息列表
//     *
//     * @param siteInfo 平台基本信息
//     * @return 平台基本信息
//     */
//    @Override
//    public List<SiteInfo> selectSiteInfoList(SiteInfo siteInfo)
//    {
//        return siteInfoMapper.selectSiteInfoList(siteInfo);
//    }
//
//    /**
//     * 新增平台基本信息
//     *
//     * @param siteInfo 平台基本信息
//     * @return 结果
//     */
//    @Override
//    public int insertSiteInfo(SiteInfo siteInfo)
//    {
//        return siteInfoMapper.insertSiteInfo(siteInfo);
//    }

    /**
     * 修改平台基本信息
     * 
     * @param siteInfo 平台基本信息
     * @return 结果
     */
    @Override
    @CacheEvict(value = CacheableKey.SITE_INFO,key = "#siteInfo.id")
    public int updateSiteInfo(SiteInfo siteInfo)
    {
        return siteInfoMapper.updateSiteInfo(siteInfo);
    }

//    /**
//     * 批量删除平台基本信息
//     *
//     * @param ids 需要删除的平台基本信息主键
//     * @return 结果
//     */
//    @Override
//    public int deleteSiteInfoByIds(Long[] ids)
//    {
//        return siteInfoMapper.deleteSiteInfoByIds(ids);
//    }
//
//    /**
//     * 删除平台基本信息信息
//     *
//     * @param id 平台基本信息主键
//     * @return 结果
//     */
//    @Override
//    public int deleteSiteInfoById(Long id)
//    {
//        return siteInfoMapper.deleteSiteInfoById(id);
//    }
}
