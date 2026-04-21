package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SiteMessageRead;

import java.util.List;

/**
 * 平台站内信已读记录Mapper接口
 * 
 * @author ruoyi
 * @date 2023-11-17
 */
public interface SiteMessageReadMapper 
{
    /**
     * 查询平台站内信已读记录
     * 
     * @param id 平台站内信已读记录主键
     * @return 平台站内信已读记录
     */
    public SiteMessageRead selectSiteMessageReadById(Long id);

    /**
     * 查询平台站内信已读记录列表
     * 
     * @param siteMessageRead 平台站内信已读记录
     * @return 平台站内信已读记录集合
     */
    public List<SiteMessageRead> selectSiteMessageReadList(SiteMessageRead siteMessageRead);

    /**
     * 新增平台站内信已读记录
     * 
     * @param siteMessageRead 平台站内信已读记录
     * @return 结果
     */
    public int insertSiteMessageRead(SiteMessageRead siteMessageRead);

    /**
     * 修改平台站内信已读记录
     * 
     * @param siteMessageRead 平台站内信已读记录
     * @return 结果
     */
    public int updateSiteMessageRead(SiteMessageRead siteMessageRead);

    /**
     * 删除平台站内信已读记录
     * 
     * @param id 平台站内信已读记录主键
     * @return 结果
     */
    public int deleteSiteMessageReadById(Long id);

    /**
     * 批量删除平台站内信已读记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSiteMessageReadByIds(Long[] ids);

}
