package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 平台站内信已读记录对象 site_message_read
 * 
 * @author ruoyi
 * @date 2023-11-17
 */
public class SiteMessageRead extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 站内信id */
    @Excel(name = "站内信id")
    private Long siteMessageId;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setSiteMessageId(Long siteMessageId) 
    {
        this.siteMessageId = siteMessageId;
    }

    public Long getSiteMessageId() 
    {
        return siteMessageId;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("siteMessageId", getSiteMessageId())
            .append("userId", getUserId())
            .toString();
    }
}
