package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 系统产品交易时间配置对象 product_trade_time_setting
 * 
 * @author ruoyi
 * @date 2023-11-27
 */
public class ProductTradeTimeSetting extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 天(周一到周天，1~7) */
    @Excel(name = "天(周一到周天，1~7)")
    private Integer day;

    /** 早上交易开始时间 */
    @Excel(name = "早上交易开始时间")
    private String transAmBegin;

    /** 早上交易结束时间 */
    @Excel(name = "早上交易结束时间")
    private String transAmEnd;

    /** 下午交易开始时间 */
    @Excel(name = "下午交易开始时间")
    private String transPmBegin;

    /** 下午交易结束时间 */
    @Excel(name = "下午交易结束时间")
    private String transPmEnd;

    /** 产品类型：1美股，2加密货币 3外汇 4期货 */
    @Excel(name = "产品类型：1美股，2加密货币 3外汇 4期货")
    private Integer productType;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setDay(Integer day) 
    {
        this.day = day;
    }

    public Integer getDay() 
    {
        return day;
    }
    public void setTransAmBegin(String transAmBegin) 
    {
        this.transAmBegin = transAmBegin;
    }

    public String getTransAmBegin() 
    {
        return transAmBegin;
    }
    public void setTransAmEnd(String transAmEnd) 
    {
        this.transAmEnd = transAmEnd;
    }

    public String getTransAmEnd() 
    {
        return transAmEnd;
    }
    public void setTransPmBegin(String transPmBegin) 
    {
        this.transPmBegin = transPmBegin;
    }

    public String getTransPmBegin() 
    {
        return transPmBegin;
    }
    public void setTransPmEnd(String transPmEnd) 
    {
        this.transPmEnd = transPmEnd;
    }

    public String getTransPmEnd() 
    {
        return transPmEnd;
    }
    public void setProductType(Integer productType) 
    {
        this.productType = productType;
    }

    public Integer getProductType() 
    {
        return productType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("day", getDay())
            .append("transAmBegin", getTransAmBegin())
            .append("transAmEnd", getTransAmEnd())
            .append("transPmBegin", getTransPmBegin())
            .append("transPmEnd", getTransPmEnd())
            .append("productType", getProductType())
            .toString();
    }
}
