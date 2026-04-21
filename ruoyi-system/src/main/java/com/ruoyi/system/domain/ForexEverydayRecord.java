package com.ruoyi.system.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 外汇日涨幅记录对象 forex_everyday_record
 * 
 * @author ruoyi
 * @date 2023-11-02
 */
public class ForexEverydayRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 产品代码 */
    @Excel(name = "产品代码")
    private String productCode;

    /** 现价 */
    @Excel(name = "现价")
    private BigDecimal nowPrice;

    /** 涨跌幅 */
    @Excel(name = "涨跌幅")
    private BigDecimal changeRate;

    /** 开盘价 */
    @Excel(name = "开盘价")
    private BigDecimal openPrice;

    /** 收盘价 */
    @Excel(name = "收盘价")
    private BigDecimal closePrice;

    /** 交易总金额 */
    @Excel(name = "交易总金额")
    private BigDecimal businessAmount;

    /** 交易总量 */
    @Excel(name = "交易总量")
    private BigDecimal businessVolume;

    /** 最高价 */
    @Excel(name = "最高价")
    private BigDecimal maxPrice;

    /** 最低价 */
    @Excel(name = "最低价")
    private BigDecimal minPrice;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setProductCode(String productCode) 
    {
        this.productCode = productCode;
    }

    public String getProductCode() 
    {
        return productCode;
    }
    public void setNowPrice(BigDecimal nowPrice) 
    {
        this.nowPrice = nowPrice;
    }

    public BigDecimal getNowPrice() 
    {
        return nowPrice;
    }
    public void setChangeRate(BigDecimal changeRate) 
    {
        this.changeRate = changeRate;
    }

    public BigDecimal getChangeRate() 
    {
        return changeRate;
    }
    public void setOpenPrice(BigDecimal openPrice) 
    {
        this.openPrice = openPrice;
    }

    public BigDecimal getOpenPrice() 
    {
        return openPrice;
    }
    public void setClosePrice(BigDecimal closePrice) 
    {
        this.closePrice = closePrice;
    }

    public BigDecimal getClosePrice() 
    {
        return closePrice;
    }
    public void setBusinessAmount(BigDecimal businessAmount) 
    {
        this.businessAmount = businessAmount;
    }

    public BigDecimal getBusinessAmount() 
    {
        return businessAmount;
    }
    public void setBusinessVolume(BigDecimal businessVolume) 
    {
        this.businessVolume = businessVolume;
    }

    public BigDecimal getBusinessVolume() 
    {
        return businessVolume;
    }
    public void setMaxPrice(BigDecimal maxPrice) 
    {
        this.maxPrice = maxPrice;
    }

    public BigDecimal getMaxPrice() 
    {
        return maxPrice;
    }
    public void setMinPrice(BigDecimal minPrice) 
    {
        this.minPrice = minPrice;
    }

    public BigDecimal getMinPrice() 
    {
        return minPrice;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("productCode", getProductCode())
            .append("nowPrice", getNowPrice())
            .append("changeRate", getChangeRate())
            .append("openPrice", getOpenPrice())
            .append("closePrice", getClosePrice())
            .append("businessAmount", getBusinessAmount())
            .append("businessVolume", getBusinessVolume())
            .append("maxPrice", getMaxPrice())
            .append("minPrice", getMinPrice())
            .append("createTime", getCreateTime())
            .toString();
    }
}
