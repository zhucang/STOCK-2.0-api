package com.ruoyi.common.logDict;

import com.ruoyi.common.logDict.abstractDictMap.AbstractLogDictMap;

/**
 * 平台基本信息对象 site_info
 * 
 * @author ruoyi
 * @date 2023-11-13
 */
public class SiteInfoLogDict extends AbstractLogDictMap
{

    @Override
    public void init() {
        this.put("id","平台基本信息配置ID");
        this.put("siteName","平台名称");
        this.put("siteLogo","平台logo");
        this.put("appLogo","app logo");
        this.put("iosDownloadUrl","ios下载链接");
        this.put("androidDownloadUrl","安卓下载链接");
        this.put("androidApkDownloadUrl","安卓apk下载链接");
        this.put("appUrl","分享链接");
        this.put("websiteUrl","官网链接");
    }
}
