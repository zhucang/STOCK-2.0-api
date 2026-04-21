package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 客户端版本管理对象 client_version
 * 
 * @author ruoyi
 * @date 2024-01-08
 */
public class ClientVersion extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 客户端模板id */
    @Excel(name = "客户端模板id")
    private Long clientTemplateId;

    /** 客户端模板名称 */
    @Excel(name = "客户端模板名称")
    private String templateName;

    /** h5压缩包名称 */
    @Excel(name = "h5压缩包名称")
    private String compressedPackageNameH5;

    /** app压缩包名称 */
    @Excel(name = "app压缩包名称")
    private String compressedPackageNameApp;

    /** 版本号 */
    @Excel(name = "版本号")
    private Long version;

    /** 文件存放路径 */
    @Excel(name = "文件存放路径")

    private String fileStoragePath;

    public String getFileStoragePath() {
        return fileStoragePath;
    }

    public void setFileStoragePath(String fileStoragePath) {
        this.fileStoragePath = fileStoragePath;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setClientTemplateId(Long clientTemplateId) 
    {
        this.clientTemplateId = clientTemplateId;
    }

    public Long getClientTemplateId() 
    {
        return clientTemplateId;
    }
    public void setCompressedPackageNameH5(String compressedPackageNameH5) 
    {
        this.compressedPackageNameH5 = compressedPackageNameH5;
    }

    public String getCompressedPackageNameH5() 
    {
        return compressedPackageNameH5;
    }
    public void setCompressedPackageNameApp(String compressedPackageNameApp) 
    {
        this.compressedPackageNameApp = compressedPackageNameApp;
    }

    public String getCompressedPackageNameApp() 
    {
        return compressedPackageNameApp;
    }
    public void setVersion(Long version) 
    {
        this.version = version;
    }

    public Long getVersion() 
    {
        return version;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("clientTemplateId", getClientTemplateId())
            .append("compressedPackageNameH5", getCompressedPackageNameH5())
            .append("compressedPackageNameApp", getCompressedPackageNameApp())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("version", getVersion())
            .toString();
    }
}
