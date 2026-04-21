package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 国家语言对象 language
 * 
 * @author ruoyi
 * @date 2023-11-12
 */
public class Language extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 语言 */
    @Excel(name = "语言")
    private String language;

    /** 语种简称 */
    @Excel(name = "语种简称")
    private String abbreviations;

    /** 国旗图标地址 */
    @Excel(name = "国旗图标地址")
    private String nationalFlag;

    /** 多语言值 */
    @Excel(name = "多语言值")
    private String value;

    /** 状态：0：启用 1：禁用 */
    @Excel(name = "状态：0：启用 1：禁用")
    private Integer status;

    /** 排序 */
    @Excel(name = "排序")
    private Integer sort;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setLanguage(String language) 
    {
        this.language = language;
    }

    public String getLanguage() 
    {
        return language;
    }
    public void setAbbreviations(String abbreviations) 
    {
        this.abbreviations = abbreviations;
    }

    public String getAbbreviations() 
    {
        return abbreviations;
    }
    public void setNationalFlag(String nationalFlag) 
    {
        this.nationalFlag = nationalFlag;
    }

    public String getNationalFlag() 
    {
        return nationalFlag;
    }
    public void setValue(String value) 
    {
        this.value = value;
    }

    public String getValue() 
    {
        return value;
    }
    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }
    public void setSort(Integer sort) 
    {
        this.sort = sort;
    }

    public Integer getSort() 
    {
        return sort;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("language", getLanguage())
            .append("abbreviations", getAbbreviations())
            .append("nationalFlag", getNationalFlag())
            .append("value", getValue())
            .append("status", getStatus())
            .append("sort", getSort())
            .toString();
    }
}
