package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 官网B图片对象 website_banner
 * 
 * @author ruoyi
 * @date 2023-12-09
 */
public class WebsiteBanner extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 横幅图片 */
    @Excel(name = "横幅图片")
    private String bannerImg;

    /** 类型 */
    @Excel(name = "类型")
    private Integer bannerType;

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
    public void setBannerType(Integer bannerType) 
    {
        this.bannerType = bannerType;
    }

    public Integer getBannerType() 
    {
        return bannerType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("bannerImg", getBannerImg())
            .append("remark", getRemark())
            .append("bannerType", getBannerType())
            .toString();
    }
}
