package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 新闻数据对象 news
 * 
 * @author ruoyi
 * @date 2023-12-04
 */
public class News extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 新闻主键id */
    private Long id;

    /** 新闻标题 */
    @Excel(name = "新闻标题")
    private String title;

    /** 新闻内容 */
    @Excel(name = "新闻内容")
    private String content;

    /** 描述 */
    @Excel(name = "描述")
    private String description;

    /** 浏览量 */
    @Excel(name = "浏览量")
    private Integer views;

    /** 状态：0、启用，1、停用 */
    @Excel(name = "状态：0、启用，1、停用")
    private Integer status;

    /** 显示时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "显示时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date showTime;

    /** 语言id */
    @Excel(name = "语言id")
    private Long languageId;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }
    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }
    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }
    public void setViews(Integer views) 
    {
        this.views = views;
    }

    public Integer getViews()
    {
        if (views == null){
            return RandomUtils.nextInt(10000,100000);
        }
        return views;
    }
    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }
    public void setShowTime(Date showTime) 
    {
        this.showTime = showTime;
    }

    public Date getShowTime() 
    {
        return showTime;
    }
    public void setLanguageId(Long languageId) 
    {
        this.languageId = languageId;
    }

    public Long getLanguageId() 
    {
        return languageId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("title", getTitle())
            .append("content", getContent())
            .append("description", getDescription())
            .append("views", getViews())
            .append("status", getStatus())
            .append("showTime", getShowTime())
            .append("createTime", getCreateTime())
            .append("languageId", getLanguageId())
            .toString();
    }
}
