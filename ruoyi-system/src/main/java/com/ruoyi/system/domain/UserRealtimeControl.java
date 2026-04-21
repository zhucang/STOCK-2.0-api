package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 产品分时图数据(用户控制数据)对象 user_realtime_control
 * 
 * @author ruoyi
 * @date 2023-12-30
 */
public class UserRealtimeControl extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    /** 产品代码 */
    @Excel(name = "产品代码")
    private String productCode;

    /** 时间(HH:mm) */
    @Excel(name = "时间(HH:mm)")
    private String time;

    /** 成交量 */
    @Excel(name = "成交量")
    private BigDecimal volumes;

    /** 现价 */
    @Excel(name = "现价")
    private BigDecimal price;

    /** 涨跌幅 */
    @Excel(name = "涨跌幅")
    private BigDecimal rates;

    /** 手数 */
    @Excel(name = "手数")
    private BigDecimal amounts;

    /** 均价 */
    @Excel(name = "均价")
    private BigDecimal averagePrice;

    /** 类型：1：股票 2：加密货币 3：期货 4:外汇 */
    @Excel(name = "类型：1：股票 2：加密货币 3：期货 4:外汇")
    private Integer productType;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setProductCode(String productCode) 
    {
        this.productCode = productCode;
    }

    public String getProductCode() 
    {
        return productCode;
    }
    public void setTime(String time) 
    {
        this.time = time;
    }

    public String getTime() 
    {
        return time;
    }
    public void setVolumes(BigDecimal volumes) 
    {
        this.volumes = volumes;
    }

    public BigDecimal getVolumes() 
    {
        return volumes;
    }
    public void setPrice(BigDecimal price) 
    {
        this.price = price;
    }

    public BigDecimal getPrice() 
    {
        return price;
    }
    public void setRates(BigDecimal rates) 
    {
        this.rates = rates;
    }

    public BigDecimal getRates() 
    {
        return rates;
    }
    public void setAmounts(BigDecimal amounts) 
    {
        this.amounts = amounts;
    }

    public BigDecimal getAmounts() 
    {
        return amounts;
    }
    public void setAveragePrice(BigDecimal averagePrice) 
    {
        this.averagePrice = averagePrice;
    }

    public BigDecimal getAveragePrice() 
    {
        return averagePrice;
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
            .append("userId", getUserId())
            .append("productCode", getProductCode())
            .append("time", getTime())
            .append("volumes", getVolumes())
            .append("price", getPrice())
            .append("rates", getRates())
            .append("amounts", getAmounts())
            .append("averagePrice", getAveragePrice())
            .append("productType", getProductType())
            .append("createTime", getCreateTime())
            .toString();
    }
}
