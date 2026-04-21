package com.ruoyi.system.service;

import com.ruoyi.system.domain.LangMgr;
import com.ruoyi.system.domain.SiteMessage;

import java.util.List;

/**
 * 平台站内信发送记录Service接口
 * 
 * @author ruoyi
 * @date 2023-11-10
 */
public interface ISiteMessageService 
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
    public int insertSiteMessages(List<SiteMessage> siteMessages);

    /**
     * 修改平台站内信发送记录
     * 
     * @param siteMessage 平台站内信发送记录
     * @return 结果
     */
    public int updateSiteMessage(SiteMessage siteMessage);

    /**
     * 修改消息标题多语言
     * @param siteMessageId 站内信id
     * @param msgTitleLang 消息标题语音包
     * @return
     */
    public int updateMsgTitleLang(Long siteMessageId, LangMgr msgTitleLang);

    /**
     * 修改消息内容多语言
     * @param siteMessageId 站内信id
     * @param msgContentLang 消息内容语音包
     * @return
     */
    public int updateMsgContentLang(Long siteMessageId, LangMgr msgContentLang);

    /**
     * 批量删除平台站内信发送记录
     * 
     * @param ids 需要删除的平台站内信发送记录主键集合
     * @return 结果
     */
    public int deleteSiteMessageByIds(Long[] ids);

    /**
     * 删除平台站内信发送记录信息
     * 
     * @param id 平台站内信发送记录主键
     * @return 结果
     */
    public int deleteSiteMessageById(Long id);

    /**
     * 标记已读
     * @param siteMessageId 站内信id
     * @param userId 用户id
     * @return
     */
    public int markReadFlag(Long siteMessageId,Long userId);

    /**
     * 未读消息数量
     * @param userId 用户id
     * @return
     */
    public int notReadMessageCount(Long userId);
}
