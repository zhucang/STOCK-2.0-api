package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.constant.CacheConstantKey;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.domain.LangMgr;
import com.ruoyi.system.domain.SiteMessage;
import com.ruoyi.system.domain.SiteMessageRead;
import com.ruoyi.system.mapper.SiteMessageMapper;
import com.ruoyi.system.mapper.SiteMessageReadMapper;
import com.ruoyi.system.service.ISiteMessageService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * 平台站内信发送记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-11-10
 */
@Service
public class SiteMessageServiceImpl implements ISiteMessageService 
{
    @Resource
    private SiteMessageMapper siteMessageMapper;

    @Resource
    private SiteMessageReadMapper siteMessageReadMapper;

    /**
     * 查询平台站内信发送记录
     * 
     * @param id 平台站内信发送记录主键
     * @return 平台站内信发送记录
     */
    @Override
    @Cacheable(value = CacheConstants.CONSTANT_CACHE+CacheConstantKey.SITE_MESSAGE,key = "#id")
    public SiteMessage selectSiteMessageById(Long id)
    {
        return siteMessageMapper.selectSiteMessageById(id);
    }

    /**
     * 查询平台站内信发送记录列表
     * 
     * @param siteMessage 平台站内信发送记录
     * @return 平台站内信发送记录
     */
    @Override
//    @Cacheable(value = CacheConstants.CONSTANT_CACHE+CacheConstantKey.SITE_MESSAGE,key = "#siteMessage.cacheableKey()")
    public List<SiteMessage> selectSiteMessageList(SiteMessage siteMessage)
    {
        return siteMessageMapper.selectSiteMessageList(siteMessage);
    }

    /**
     * 新增平台站内信发送记录
     * 
     * @param siteMessage 平台站内信发送记录
     * @return 结果
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheConstants.CONSTANT_CACHE + CacheConstantKey.SITE_MESSAGE + "notReadMessageCount",key = "#siteMessage.userId",condition = "#siteMessage.userId != null"),
            @CacheEvict(value = CacheConstants.CONSTANT_CACHE + CacheConstantKey.SITE_MESSAGE + "notReadMessageCount",allEntries = true,condition = "#siteMessage.userId == null")
    })
    public int insertSiteMessage(SiteMessage siteMessage)
    {
        siteMessage.setCreateTime(DateUtils.getNowDate());
        return siteMessageMapper.insertSiteMessage(siteMessage);
    }

    /**
     * 批量新增平台站内信发送记录
     *
     * @param siteMessages 平台站内信发送记录
     * @return 结果
     */
    @Override
    @CacheEvict(value = CacheConstants.CONSTANT_CACHE + CacheConstantKey.SITE_MESSAGE + "notReadMessageCount", allEntries = true)
    public int insertSiteMessages(List<SiteMessage> siteMessages) {
        return siteMessageMapper.insertSiteMessages(siteMessages);
    }

    /**
     * 修改平台站内信发送记录
     * 
     * @param siteMessage 平台站内信发送记录
     * @return 结果
     */
    @Override
    @CacheEvict(value = CacheConstants.CONSTANT_CACHE+CacheConstantKey.SITE_MESSAGE,key = "#siteMessage.id")
    public int updateSiteMessage(SiteMessage siteMessage)
    {
        return siteMessageMapper.updateSiteMessage(siteMessage);
    }

    /**
     * 修改消息标题多语言
     * @param siteMessageId 站内信id
     * @param msgTitleLang 消息标题语音包
     * @return
     */
    @Override
    @CacheEvict(value = CacheConstants.CONSTANT_CACHE+CacheConstantKey.SITE_MESSAGE,key = "#siteMessageId")
    public int updateMsgTitleLang(Long siteMessageId, LangMgr msgTitleLang){
        SiteMessage siteMessage = new SiteMessage();
        siteMessage.setId(siteMessageId);
        siteMessage.setMsgTitleLang(msgTitleLang);
        return siteMessageMapper.updateSiteMessage(siteMessage);
    }

    /**
     * 修改消息内容多语言
     * @param siteMessageId 站内信id
     * @param msgContentLang 消息内容语音包
     * @return
     */
    @Override
    @CacheEvict(value = CacheConstants.CONSTANT_CACHE+CacheConstantKey.SITE_MESSAGE,key = "#siteMessageId")
    public int updateMsgContentLang(Long siteMessageId, LangMgr msgContentLang){
        SiteMessage siteMessage = new SiteMessage();
        siteMessage.setId(siteMessageId);
        siteMessage.setMsgContentLang(msgContentLang);
        return siteMessageMapper.updateSiteMessage(siteMessage);
    }

    /**
     * 批量删除平台站内信发送记录
     * 
     * @param ids 需要删除的平台站内信发送记录主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheConstants.CONSTANT_CACHE+CacheConstantKey.SITE_MESSAGE,allEntries = true),
            @CacheEvict(value = CacheConstants.CONSTANT_CACHE + CacheConstantKey.SITE_MESSAGE + "notReadMessageCount",allEntries = true)})
    public int deleteSiteMessageByIds(Long[] ids)
    {
        SiteMessage search = new SiteMessage();
        search.getParams().put("ids", Arrays.asList(ids));
        List<SiteMessage> siteMessages = siteMessageMapper.selectSiteMessageList(search);
        //日志记录平台站内信信息
        HttpUtils.getRequestLogParams().put("JSONArray:siteMessages", JSONObject.toJSONString(siteMessages));
        return siteMessageMapper.deleteSiteMessageByIds(ids);
    }

    /**
     * 删除平台站内信发送记录信息
     * 
     * @param id 平台站内信发送记录主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheConstants.CONSTANT_CACHE+CacheConstantKey.SITE_MESSAGE,key = "#id"),
            @CacheEvict(value = CacheConstants.CONSTANT_CACHE + CacheConstantKey.SITE_MESSAGE + "notReadMessageCount",allEntries = true)})
    public int deleteSiteMessageById(Long id)
    {
        return siteMessageMapper.deleteSiteMessageById(id);
    }

    /**
     * 标记已读
     * @param siteMessageId 站内信id
     * @param userId 用户id
     * @return
     */
    @Override
    @CacheEvict(value = CacheConstants.CONSTANT_CACHE + CacheConstantKey.SITE_MESSAGE + "notReadMessageCount",key = "#userId",condition = "#userId != null")
    public int markReadFlag(Long siteMessageId,Long userId){
        SiteMessageRead siteMessageRead = new SiteMessageRead();
        siteMessageRead.setSiteMessageId(siteMessageId);
        siteMessageRead.setUserId(userId);
        //查询已读记录
        List<SiteMessageRead> siteMessageReads = siteMessageReadMapper.selectSiteMessageReadList(siteMessageRead);
        //如果还没有已读记录，则插入记录
        if (siteMessageReads.size() == 0){
            return siteMessageReadMapper.insertSiteMessageRead(siteMessageRead);
        }
        return 1;
    }

    /**
     * 未读消息数量
     * @param userId 用户id
     * @return
     */
    @Override
    @Cacheable(value = CacheConstants.CONSTANT_CACHE + CacheConstantKey.SITE_MESSAGE + "notReadMessageCount",key = "#userId")
    public int notReadMessageCount(Long userId){
        try{
            return siteMessageMapper.notReadMessageCount(userId);
        }catch (Exception e){
            return 0;
        }
    }
}
