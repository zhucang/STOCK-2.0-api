package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 第三方充值通道配置对象 third_party_recharge_channel
 * 
 * @author ruoyi
 * @date 2023-11-23
 */
public class ThirdPartyRechargeChannel extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 通道名称 */
    @Excel(name = "通道名称")
    private String channelName;

    /** 通道名称多语言 */
    @Excel(name = "通道名称多语言")
    private LangMgr channelNameLang;

    /** 通道图标 */
    @Excel(name = "通道图标")
    private String channelImg;

    /** 跳转url */
    @Excel(name = "跳转url")
    private String jumpUrl;

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
    public void setChannelName(String channelName) 
    {
        this.channelName = channelName;
    }

    public String getChannelName() 
    {
        return channelName;
    }
    public void setChannelImg(String channelImg) 
    {
        this.channelImg = channelImg;
    }

    public String getChannelImg() 
    {
        return channelImg;
    }
    public void setJumpUrl(String jumpUrl) 
    {
        this.jumpUrl = jumpUrl;
    }

    public String getJumpUrl() 
    {
        return jumpUrl;
    }
    public void setSort(Integer sort) 
    {
        this.sort = sort;
    }

    public Integer getSort() 
    {
        return sort;
    }

    public LangMgr getChannelNameLang() {
        return channelNameLang;
    }

    public void setChannelNameLang(LangMgr channelNameLang) {
        this.channelNameLang = channelNameLang;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("channelName", getChannelName())
            .append("channelImg", getChannelImg())
            .append("jumpUrl", getJumpUrl())
            .append("sort", getSort())
            .append("channelNameLang", getChannelNameLang())
            .toString();
    }
}
