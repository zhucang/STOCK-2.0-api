package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 其他值对象 other_value
 * 
 * @author ruoyi
 * @date 2024-03-27
 */
public class OtherValue extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** key */
    @Excel(name = "key")
    private String otherKey;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 值 */
    @Excel(name = "值")
    private String otherValue;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setOtherKey(String otherKey) 
    {
        this.otherKey = otherKey;
    }

    public String getOtherKey() 
    {
        return otherKey;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setOtherValue(String otherValue) 
    {
        this.otherValue = otherValue;
    }

    public String getOtherValue() 
    {
        return otherValue;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("otherKey", getOtherKey())
            .append("name", getName())
            .append("otherValue", getOtherValue())
            .append("remark", getRemark())
            .toString();
    }
}
