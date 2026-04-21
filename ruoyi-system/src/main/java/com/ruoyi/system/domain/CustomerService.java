package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 客服配置对象 customer_service
 * 
 * @author ruoyi
 * @date 2023-11-04
 */
public class CustomerService extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 客服名称 */
    @Excel(name = "客服名称")
    private String customerServiceName;

    /** 客服名称多语言 */
    @Excel(name = "客服名称多语言")
    private LangMgr customerServiceNameLang;

    /** 客服链接 */
    @Excel(name = "客服链接")
    private String customerServiceLine;

    /** 客服图标 */
    @Excel(name = "客服图标")
    private String customerServiceImg;

    /** 状态：0：启用 1：禁用 */
    @Excel(name = "状态：0：启用 1：禁用")
    private Integer status;

    /** 是否跳转：0：跳转  1：不跳转 */
    @Excel(name = "是否跳转：0：跳转  1：不跳转")
    private Integer isJump;

    /** 客服类型 0：网页客服 1：在线客服 2：Telegram 3：Whatsapp */
    @Excel(name = "客服类型 0：网页客服 1：在线客服 2：Telegram 3：Whatsapp")
    private Integer webOrOnline;


    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setCustomerServiceName(String customerServiceName) 
    {
        this.customerServiceName = customerServiceName;
    }

    public String getCustomerServiceName() 
    {
        return customerServiceName;
    }
    public void setCustomerServiceLine(String customerServiceLine) 
    {
        this.customerServiceLine = customerServiceLine;
    }

    public String getCustomerServiceLine() 
    {
        return customerServiceLine;
    }

    public String getCustomerServiceImg() {
        return customerServiceImg;
    }

    public void setCustomerServiceImg(String customerServiceImg) {
        this.customerServiceImg = customerServiceImg;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }
    public void setIsJump(Integer isJump) 
    {
        this.isJump = isJump;
    }

    public Integer getIsJump() 
    {
        return isJump;
    }
    public void setWebOrOnline(Integer webOrOnline) 
    {
        this.webOrOnline = webOrOnline;
    }

    public Integer getWebOrOnline() 
    {
        return webOrOnline;
    }

    public LangMgr getCustomerServiceNameLang() {
        return customerServiceNameLang;
    }

    public void setCustomerServiceNameLang(LangMgr customerServiceNameLang) {
        this.customerServiceNameLang = customerServiceNameLang;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("customerServiceName", getCustomerServiceName())
            .append("customerServiceLine", getCustomerServiceLine())
            .append("status", getStatus())
            .append("isJump", getIsJump())
            .append("webOrOnline", getWebOrOnline())
            .append("customerServiceNameLang", getCustomerServiceNameLang())
            .toString();
    }
}
