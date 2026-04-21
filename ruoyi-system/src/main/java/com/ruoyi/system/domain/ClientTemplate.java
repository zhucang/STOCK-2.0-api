package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 客户端模板管理对象 client_template
 * 
 * @author ruoyi
 * @date 2024-01-08
 */
public class ClientTemplate extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 客户端模板名称 */
    @Excel(name = "客户端模板名称")
    private String templateName;

    /** 文件存放路径 */
    @Excel(name = "文件存放路径")
    private String fileStoragePath;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setTemplateName(String templateName) 
    {
        this.templateName = templateName;
    }

    public String getTemplateName() 
    {
        return templateName;
    }
    public void setFileStoragePath(String fileStoragePath) 
    {
        this.fileStoragePath = fileStoragePath;
    }

    public String getFileStoragePath() 
    {
        return fileStoragePath;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("templateName", getTemplateName())
            .append("fileStoragePath", getFileStoragePath())
            .toString();
    }
}
