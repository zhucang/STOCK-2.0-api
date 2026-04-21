package com.ruoyi.system.service;

import com.ruoyi.system.domain.SiteMessageType;

import java.util.List;

/**
 * 站内信类型Service接口
 * 
 * @author ruoyi
 * @date 2026-04-12
 */
public interface ISiteMessageTypeService 
{
    /**
     * 查询站内信类型
     * 
     * @param siteMessageTypeId 站内信类型主键
     * @return 站内信类型
     */
    public SiteMessageType selectSiteMessageTypeBySiteMessageTypeId(Long siteMessageTypeId);

    /**
     * 查询站内信类型列表
     * 
     * @param siteMessageType 站内信类型
     * @return 站内信类型集合
     */
    public List<SiteMessageType> selectSiteMessageTypeList(SiteMessageType siteMessageType);

    /**
     * 新增站内信类型
     * 
     * @param siteMessageType 站内信类型
     * @return 结果
     */
    public int insertSiteMessageType(SiteMessageType siteMessageType);

    /**
     * 修改站内信类型
     * 
     * @param siteMessageType 站内信类型
     * @return 结果
     */
    public int updateSiteMessageType(SiteMessageType siteMessageType);

    /**
     * 批量删除站内信类型
     * 
     * @param siteMessageTypeIds 需要删除的站内信类型主键集合
     * @return 结果
     */
    public int deleteSiteMessageTypeBySiteMessageTypeIds(Long[] siteMessageTypeIds);

    /**
     * 删除站内信类型信息
     * 
     * @param siteMessageTypeId 站内信类型主键
     * @return 结果
     */
    public int deleteSiteMessageTypeBySiteMessageTypeId(Long siteMessageTypeId);
}
