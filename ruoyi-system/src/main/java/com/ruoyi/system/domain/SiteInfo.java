package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 平台基本信息对象 site_info
 * 
 * @author ruoyi
 * @date 2023-11-13
 */
public class SiteInfo extends BaseEntity
{

    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 平台名称 */
    @Excel(name = "平台名称")
    private String siteName;

    /** 平台logo */
    @Excel(name = "平台logo")
    private String siteLogo;

    /** app logo */
    @Excel(name = "app logo")
    private String appLogo;

    /** ios下载链接 */
    @Excel(name = "ios下载链接")
    private String iosDownloadUrl;

    /** 安卓下载链接 */
    @Excel(name = "安卓下载链接")
    private String androidDownloadUrl;

    /** 安卓apk下载链接 */
    @Excel(name = "安卓apk下载链接")
    private String androidApkDownloadUrl;

    /** 分享链接 */
    @Excel(name = "分享链接")
    private String appUrl;

    /** 官网链接 */
    @Excel(name = "官网链接")
    private String websiteUrl;

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getAppLogo() {
        return appLogo;
    }

    public void setAppLogo(String appLogo) {
        this.appLogo = appLogo;
    }

    public Long getId()
    {
        return id;
    }
    public void setSiteName(String siteName) 
    {
        this.siteName = siteName;
    }

    public String getSiteName() 
    {
        return siteName;
    }
    public void setSiteLogo(String siteLogo) 
    {
        this.siteLogo = siteLogo;
    }

    public String getSiteLogo() 
    {
        return siteLogo;
    }
    public void setIosDownloadUrl(String iosDownloadUrl) 
    {
        this.iosDownloadUrl = iosDownloadUrl;
    }

    public String getIosDownloadUrl() 
    {
        return iosDownloadUrl;
    }
    public void setAndroidDownloadUrl(String androidDownloadUrl) 
    {
        this.androidDownloadUrl = androidDownloadUrl;
    }

    public String getAndroidApkDownloadUrl() {
        return androidApkDownloadUrl;
    }

    public void setAndroidApkDownloadUrl(String androidApkDownloadUrl) {
        this.androidApkDownloadUrl = androidApkDownloadUrl;
    }

    public String getAndroidDownloadUrl()
    {
        return androidDownloadUrl;
    }
    public void setAppUrl(String appUrl) 
    {
        this.appUrl = appUrl;
    }

    public String getAppUrl() 
    {
        return appUrl;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("siteName", getSiteName())
            .append("siteLogo", getSiteLogo())
            .append("iosDownloadUrl", getIosDownloadUrl())
            .append("androidDownloadUrl", getAndroidDownloadUrl())
            .append("appUrl", getAppUrl())
            .toString();
    }
}
