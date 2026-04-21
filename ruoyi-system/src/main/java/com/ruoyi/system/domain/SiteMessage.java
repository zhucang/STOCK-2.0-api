package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.entity.UserInfoDetailVo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.HashMap;

/**
 * 平台站内信发送记录对象 site_message
 * 
 * @author ruoyi
 * @date 2023-11-10
 */
public class SiteMessage extends UserInfoDetailVo
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    /** 信息标题 */
    @Excel(name = "信息标题")
    private String msgTitle;

    /** 信息标题多语言 */
    @Excel(name = "信息标题多语言")
    private LangMgr msgTitleLang;

    /** 信息内容 */
    @Excel(name = "信息内容")
    private String msgContent;

    /** 信息内容多语言 */
    @Excel(name = "信息内容多语言")
    private LangMgr msgContentLang;

    /** 是否显示 0：是 1：否 */
    @Excel(name = "是否显示 0：是 1：否")
    private Integer isVisible;

    /** 类型 ：0:用户通知 1：系统公告 */
    @Excel(name = "类型 ：0:用户通知 1：系统公告")
    private Integer isPrivate;

    /** 站内信类型ID */
    @Excel(name = "站内信类型ID")
    private Integer siteMessageTypeId;

    /** 是否已读 0：未读 1：已读 */
    @Excel(name = "是否已读 0：未读 1：已读")
    private Integer readFlag;


    public Integer getReadFlag() {
        return readFlag;
    }

    public void setReadFlag(Integer readFlag) {
        this.readFlag = readFlag;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setMsgTitle(String msgTitle) 
    {
        this.msgTitle = msgTitle;
    }

    public String getMsgTitle() 
    {
        return msgTitle;
    }
    public void setMsgContent(String msgContent) 
    {
        this.msgContent = msgContent;
    }

    public String getMsgContent() 
    {
        return msgContent;
    }
    public void setIsVisible(Integer isVisible) 
    {
        this.isVisible = isVisible;
    }

    public Integer getIsVisible() 
    {
        return isVisible;
    }
    public void setIsPrivate(Integer isPrivate) 
    {
        this.isPrivate = isPrivate;
    }

    public Integer getIsPrivate() 
    {
        return isPrivate;
    }

    public Integer getSiteMessageTypeId() {
        return siteMessageTypeId;
    }

    public void setSiteMessageTypeId(Integer siteMessageTypeId) {
        this.siteMessageTypeId = siteMessageTypeId;
    }

    public LangMgr getMsgTitleLang() {
        if (msgTitleLang == null){
            msgTitleLang = new LangMgr();
        }
        return msgTitleLang;
    }

    public void setMsgTitleLang(LangMgr msgTitleLang) {
        this.msgTitleLang = msgTitleLang;
    }

    public LangMgr getMsgContentLang() {
        if (msgContentLang == null){
            msgContentLang = new LangMgr();
        }
        return msgContentLang;
    }

    public void setMsgContentLang(LangMgr msgContentLang) {
        this.msgContentLang = msgContentLang;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("msgTitle", getMsgTitle())
            .append("msgContent", getMsgContent())
            .append("createTime", getCreateTime())
            .append("isVisible", getIsVisible())
            .append("isPrivate", getIsPrivate())
            .append("msgTitleLang", getMsgTitleLang())
            .append("msgContentLang", getMsgContentLang())
            .toString();
    }

    public String cacheableKey() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId",getUserId());
        map.put("isVisible",getIsVisible());
        map.put("isPrivate",getIsPrivate());
        map.put("pageSize",getPageSize());
        map.put("pageNum",getPageNum());
        return map.toString();
    }
}
