package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SiteMessage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 平台站内信发送记录Mapper接口
 * 
 * @author ruoyi
 * @date 2023-11-10
 */
public interface SiteMessageMapper 
{
    /**
     * 查询平台站内信发送记录
     * 
     * @param id 平台站内信发送记录主键
     * @return 平台站内信发送记录
     */
    public SiteMessage selectSiteMessageById(Long id);

    /**
     * 查询平台站内信发送记录列表
     *
     * @param siteMessage 平台站内信发送记录
     * @return 平台站内信发送记录集合
     */
    public List<SiteMessage> selectSiteMessageList(SiteMessage siteMessage);

    /**
     * 新增平台站内信发送记录
     * 
     * @param siteMessage 平台站内信发送记录
     * @return 结果
     */
    public int insertSiteMessage(SiteMessage siteMessage);

    /**
     * 批量新增平台站内信发送记录
     *
     * @param siteMessages 平台站内信发送记录
     * @return 结果
     */
    public int insertSiteMessages(@Param("siteMessages") List<SiteMessage> siteMessages);

    /**
     * 修改平台站内信发送记录
     * 
     * @param siteMessage 平台站内信发送记录
     * @return 结果
     */
    public int updateSiteMessage(SiteMessage siteMessage);

    /**
     * 删除平台站内信发送记录
     * 
     * @param id 平台站内信发送记录主键
     * @return 结果
     */
    public int deleteSiteMessageById(Long id);

    /**
     * 批量删除平台站内信发送记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSiteMessageByIds(Long[] ids);

    /**
     * 未读消息数量
     */
    public int notReadMessageCount(Long userId);
}
