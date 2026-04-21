package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 银行卡参数字段配置对象 bank_property_config
 * 
 * @author ruoyi
 * @date 2023-11-23
 */
public class BankPropertyConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 字段名称 */
    @Excel(name = "字段名称")
    private String propertyName;

    /** 字段描述 */
    @Excel(name = "字段描述")
    private String propertyDesc;

    /** 多语言key */
    @Excel(name = "多语言key")
    private String langKey;

    /** 是否展示 0：展示 1：不展示 */
    @Excel(name = "是否展示 0：展示 1：不展示")
    private Integer isVisible;

    /** 是否必填 0：是 1：否 */
    @Excel(name = "是否必填 0：是 1：否")
    private Integer isRequire;

    /** 排序 */
    @Excel(name = "排序")
    private Integer sort;

    /** 输入类型 */
    @Excel(name = "输入类型")
    private String inputType;

    /** 配置类型 0：用户银行卡 1：平台收款银行卡 */
    @Excel(name = "配置类型 0：用户银行卡 1：平台收款银行卡")
    private Integer configType;

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public Integer getConfigType() {
        return configType;
    }

    public void setConfigType(Integer configType) {
        this.configType = configType;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setPropertyName(String propertyName) 
    {
        this.propertyName = propertyName;
    }

    public String getPropertyName() 
    {
        return propertyName;
    }
    public void setPropertyDesc(String propertyDesc) 
    {
        this.propertyDesc = propertyDesc;
    }

    public String getPropertyDesc() 
    {
        return propertyDesc;
    }
    public void setIsVisible(Integer isVisible) 
    {
        this.isVisible = isVisible;
    }

    public Integer getIsVisible() 
    {
        return isVisible;
    }
    public void setIsRequire(Integer isRequire) 
    {
        this.isRequire = isRequire;
    }

    public Integer getIsRequire() 
    {
        return isRequire;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("propertyName", getPropertyName())
            .append("propertyDesc", getPropertyDesc())
            .append("isVisible", getIsVisible())
            .append("isRequire", getIsRequire())
            .toString();
    }
}
