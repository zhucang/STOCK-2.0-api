package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * banner横幅图片配置对象 site_banner
 * 
 * @author ruoyi
 * @date 2023-11-04
 */
public class SiteBanner extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 横幅图片 */
    @Excel(name = "横幅图片")
    private String bannerImg;

    /** 横幅图片多语言 */
    @Excel(name = "横幅图片多语言")
    private LangMgr bannerImgLang;

    /** 横幅标题 */
    @Excel(name = "横幅标题")
    private String bannerTitle;

    /** 横幅标题多语言 */
    @Excel(name = "横幅标题多语言")
    private LangMgr bannerTitleLang;

    /** 横幅内容 */
    @Excel(name = "横幅内容")
    private String bannerContent;

    /** 横幅内容多语言 */
    @Excel(name = "横幅内容多语言")
    private LangMgr bannerContentLang;

    /** 排序 */
    @Excel(name = "排序")
    private Integer sort;

    /** 展示时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "展示时间")
    private Date showTime;

    public Date getShowTime() {
        return showTime;
    }

    public void setShowTime(Date showTime) {
        this.showTime = showTime;
    }

    public String getBannerTitle() {
        return bannerTitle;
    }

    public void setBannerTitle(String bannerTitle) {
        this.bannerTitle = bannerTitle;
    }

    public LangMgr getBannerTitleLang() {
        if (bannerTitleLang == null){
            return new LangMgr();
        }
        return bannerTitleLang;
    }

    public void setBannerTitleLang(LangMgr bannerTitleLang) {
        this.bannerTitleLang = bannerTitleLang;
    }

    public String getBannerContent() {
        return bannerContent;
    }

    public void setBannerContent(String bannerContent) {
        this.bannerContent = bannerContent;
    }

    public LangMgr getBannerContentLang() {
        if (bannerContentLang == null){
            return new LangMgr();
        }
        return bannerContentLang;
    }

    public void setBannerContentLang(LangMgr bannerContentLang) {
        this.bannerContentLang = bannerContentLang;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setBannerImg(String bannerImg) 
    {
        this.bannerImg = bannerImg;
    }

    public String getBannerImg() 
    {
        return bannerImg;
    }
    public void setSort(Integer sort) 
    {
        this.sort = sort;
    }

    public Integer getSort() 
    {
        return sort;
    }

    public LangMgr getBannerImgLang() {
        if (bannerImgLang == null){
            return new LangMgr();
        }
        return bannerImgLang;
    }

    public void setBannerImgLang(LangMgr bannerImgLang) {
        this.bannerImgLang = bannerImgLang;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("bannerImg", getBannerImg())
            .append("sort", getSort())
            .append("createTime", getCreateTime())
            .append("bannerImgLang", getBannerImgLang())
            .toString();
    }
}
