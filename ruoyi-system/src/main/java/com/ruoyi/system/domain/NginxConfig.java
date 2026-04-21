package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * nginx配置转发对象 nginx_config
 * 
 * @author ruoyi
 * @date 2024-04-23
 */
public class NginxConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 服务器名称 */
    @Excel(name = "服务器名称")
    private String serverName;

    /** 配置类型 0：后台 1：h5 2：描述文件 3：官网 4：加速线路  */
    @Excel(name = "配置类型 0：后台 1：h5 2：描述文件 3：官网 4：加速线路 ")
    private Integer configType;

    /** 配置内容 */
    @Excel(name = "配置内容")
    private String configContent;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setServerName(String serverName) 
    {
        this.serverName = serverName;
    }

    public String getServerName() 
    {
        return serverName;
    }
    public void setConfigType(Integer configType) 
    {
        this.configType = configType;
    }

    public Integer getConfigType() 
    {
        return configType;
    }
    public void setConfigContent(String configContent) 
    {
        this.configContent = configContent;
    }

    public String getConfigContent() 
    {
        return configContent;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("serverName", getServerName())
            .append("configType", getConfigType())
            .append("configContent", getConfigContent())
            .toString();
    }
}
