package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 开关配置对象 switch_set
 * 
 * @author ruoyi
 * @date 2023-10-28
 */
public class SwitchSet extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 开关名称 */
    @Excel(name = "开关名称")
    private String switchName;

    /** 开关状态：0：开启 1：关闭 */
    @Excel(name = "开关状态：0：开启 1：关闭")
    private Integer status;

    /** 类型：0登陆注册开关  1：其他开关  2：交易方式开关 */
    @Excel(name = "类型：0登陆注册开关  1：其他开关  2：交易方式开关")
    private Integer type;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setSwitchName(String switchName) 
    {
        this.switchName = switchName;
    }

    public String getSwitchName() 
    {
        return switchName;
    }
    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }
    public void setType(Integer type) 
    {
        this.type = type;
    }

    public Integer getType() 
    {
        return type;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("switchName", getSwitchName())
            .append("status", getStatus())
            .append("type", getType())
            .toString();
    }
}
