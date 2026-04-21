package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 官网菜单配置对象 web_menu_config
 * 
 * @author ruoyi
 * @date 2023-12-11
 */
public class WebMenuConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 多语言key */
    @Excel(name = "多语言key")
    private String langKey;

    /** 路由路径 */
    @Excel(name = "路由路径")
    private String routingPath;

    /** 是否展示 0：展示 1：不展示 */
    @Excel(name = "是否展示 0：展示 1：不展示")
    private Integer isVisible;

    /** 排序 */
    @Excel(name = "排序")
    private Long sort;

    /** 描述 */
    @Excel(name = "描述")
    private String desc;

    /** 是否跳转 0：是 1：不是 */
    @Excel(name = "是否跳转 0：是 1：不是")
    private Integer isJump;

    /** 跳转url */
    @Excel(name = "跳转url")
    private String jumpUrl;

    /** 官网版本 */
    private Integer websiteVersion;

    public Integer getWebsiteVersion() {
        return websiteVersion;
    }

    public void setWebsiteVersion(Integer websiteVersion) {
        this.websiteVersion = websiteVersion;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setLangKey(String langKey) 
    {
        this.langKey = langKey;
    }

    public String getLangKey() 
    {
        return langKey;
    }
    public void setRoutingPath(String routingPath) 
    {
        this.routingPath = routingPath;
    }

    public String getRoutingPath() 
    {
        return routingPath;
    }
    public void setIsVisible(Integer isVisible) 
    {
        this.isVisible = isVisible;
    }

    public Integer getIsVisible() 
    {
        return isVisible;
    }
    public void setSort(Long sort) 
    {
        this.sort = sort;
    }

    public Long getSort() 
    {
        return sort;
    }
    public void setDesc(String desc) 
    {
        this.desc = desc;
    }

    public String getDesc() 
    {
        return desc;
    }
    public void setIsJump(Integer isJump) 
    {
        this.isJump = isJump;
    }

    public Integer getIsJump() 
    {
        return isJump;
    }
    public void setJumpUrl(String jumpUrl) 
    {
        this.jumpUrl = jumpUrl;
    }

    public String getJumpUrl() 
    {
        return jumpUrl;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("langKey", getLangKey())
            .append("routingPath", getRoutingPath())
            .append("isVisible", getIsVisible())
            .append("sort", getSort())
            .append("desc", getDesc())
            .append("isJump", getIsJump())
            .append("jumpUrl", getJumpUrl())
            .toString();
    }
}
