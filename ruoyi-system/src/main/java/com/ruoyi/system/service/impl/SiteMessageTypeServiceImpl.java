package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.SiteMessageType;
import com.ruoyi.system.mapper.SiteMessageTypeMapper;
import com.ruoyi.system.service.ISiteMessageTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 站内信类型Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-04-12
 */
@Service
public class SiteMessageTypeServiceImpl implements ISiteMessageTypeService 
{
    @Resource
    private SiteMessageTypeMapper siteMessageTypeMapper;

    /**
     * 查询站内信类型
     * 
     * @param siteMessageTypeId 站内信类型主键
     * @return 站内信类型
     */
    @Override
    public SiteMessageType selectSiteMessageTypeBySiteMessageTypeId(Long siteMessageTypeId)
    {
        return siteMessageTypeMapper.selectSiteMessageTypeBySiteMessageTypeId(siteMessageTypeId);
    }

    /**
     * 查询站内信类型列表
     * 
     * @param siteMessageType 站内信类型
     * @return 站内信类型
     */
    @Override
    public List<SiteMessageType> selectSiteMessageTypeList(SiteMessageType siteMessageType)
    {
        return siteMessageTypeMapper.selectSiteMessageTypeList(siteMessageType);
    }

    /**
     * 新增站内信类型
     * 
     * @param siteMessageType 站内信类型
     * @return 结果
     */
    @Override
    public int insertSiteMessageType(SiteMessageType siteMessageType)
    {
        return siteMessageTypeMapper.insertSiteMessageType(siteMessageType);
    }

    /**
     * 修改站内信类型
     * 
     * @param siteMessageType 站内信类型
     * @return 结果
     */
    @Override
    public int updateSiteMessageType(SiteMessageType siteMessageType)
    {
        return siteMessageTypeMapper.updateSiteMessageType(siteMessageType);
    }

    /**
     * 批量删除站内信类型
     * 
     * @param siteMessageTypeIds 需要删除的站内信类型主键
     * @return 结果
     */
    @Override
    public int deleteSiteMessageTypeBySiteMessageTypeIds(Long[] siteMessageTypeIds)
    {
        return siteMessageTypeMapper.deleteSiteMessageTypeBySiteMessageTypeIds(siteMessageTypeIds);
    }

    /**
     * 删除站内信类型信息
     * 
     * @param siteMessageTypeId 站内信类型主键
     * @return 结果
     */
    @Override
    public int deleteSiteMessageTypeBySiteMessageTypeId(Long siteMessageTypeId)
    {
        return siteMessageTypeMapper.deleteSiteMessageTypeBySiteMessageTypeId(siteMessageTypeId);
    }
}
