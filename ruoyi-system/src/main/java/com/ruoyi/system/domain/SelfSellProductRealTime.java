package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 自营产品分时图数据对象 self_sell_product_real_time
 * 
 * @author ruoyi
 * @date 2024-08-28
 */
public class SelfSellProductRealTime extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 产品代码 */
    @Excel(name = "产品代码")
    private String productCode;

    /** 参考价格 */
    @Excel(name = "参考价格")
    private BigDecimal referencePrice;

    /** 参考涨跌幅 */
    @Excel(name = "参考涨跌幅")
    private BigDecimal referenceChangeRate;

    /** 开盘价格 */
    @Excel(name = "开盘价格")
    private BigDecimal openPrice;

    /** 收盘价格 */
    @Excel(name = "收盘价格")
    private BigDecimal closePrice;

    /** 最高价格 */
    @Excel(name = "最高价格")
    private BigDecimal highPrice;

    /** 最低价格 */
    @Excel(name = "最低价格")
    private BigDecimal lowPrice;

    /** 平均价格 */
    @Excel(name = "平均价格")
    private BigDecimal averagePrice;

    /** 成交量 */
    @Excel(name = "成交量")
    private BigDecimal volumes;

    /** 成交额 */
    @Excel(name = "成交额")
    private BigDecimal amount;

    /** 类型：1：股票 2：加密货币 3：期货 4:外汇 */
    @Excel(name = "类型：1：股票 2：加密货币 3：期货 4:外汇")
    private Integer productType;

    /** 具体时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "具体时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date specificTime;

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
    public void setReferencePrice(BigDecimal referencePrice) 
    {
        this.referencePrice = referencePrice;
    }

    public BigDecimal getReferencePrice() 
    {
        return referencePrice;
    }
    public void setReferenceChangeRate(BigDecimal referenceChangeRate) 
    {
        this.referenceChangeRate = referenceChangeRate;
    }

    public BigDecimal getReferenceChangeRate() 
    {
        return referenceChangeRate;
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
    public void setHighPrice(BigDecimal highPrice) 
    {
        this.highPrice = highPrice;
    }

    public BigDecimal getHighPrice() 
    {
        return highPrice;
    }
    public void setLowPrice(BigDecimal lowPrice) 
    {
        this.lowPrice = lowPrice;
    }

    public BigDecimal getLowPrice() 
    {
        return lowPrice;
    }
    public void setAveragePrice(BigDecimal averagePrice) 
    {
        this.averagePrice = averagePrice;
    }

    public BigDecimal getAveragePrice() 
    {
        return averagePrice;
    }
    public void setVolumes(BigDecimal volumes) 
    {
        this.volumes = volumes;
    }

    public BigDecimal getVolumes() 
    {
        return volumes;
    }
    public void setAmount(BigDecimal amount) 
    {
        this.amount = amount;
    }

    public BigDecimal getAmount() 
    {
        return amount;
    }
    public void setProductType(Integer productType) 
    {
        this.productType = productType;
    }

    public Integer getProductType() 
    {
        return productType;
    }
    public void setSpecificTime(Date specificTime) 
    {
        this.specificTime = specificTime;
    }

    public Date getSpecificTime() 
    {
        return specificTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("productCode", getProductCode())
            .append("referencePrice", getReferencePrice())
            .append("referenceChangeRate", getReferenceChangeRate())
            .append("openPrice", getOpenPrice())
            .append("closePrice", getClosePrice())
            .append("highPrice", getHighPrice())
            .append("lowPrice", getLowPrice())
            .append("averagePrice", getAveragePrice())
            .append("volumes", getVolumes())
            .append("amount", getAmount())
            .append("productType", getProductType())
            .append("specificTime", getSpecificTime())
            .append("createTime", getCreateTime())
            .toString();
    }
}
