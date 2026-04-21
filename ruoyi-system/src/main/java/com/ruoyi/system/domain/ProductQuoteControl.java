package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 产品短线行情控制对象 product_quote_control
 * 
 * @author ruoyi
 * @date 2024-01-11
 */
public class ProductQuoteControl extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 产品代码 */
    @Excel(name = "产品代码")
    private String productCode;

    /** 产品类型 1：股票 2：加密货币 3：期货 4：外汇 */
    @Excel(name = "产品类型 1：股票 2：加密货币 3：期货 4：外汇")
    private Integer productType;

    /** 预设点位 */
    @Excel(name = "预设点位")
    private BigDecimal presetPrice;

    /** 启动时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "启动时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date presetStartTime;

    /** 预设时长 */
    @Excel(name = "预设时长")
    private Integer presetDuration;

    /** 回归时长 */
    @Excel(name = "回归时长")
    private Integer returnDuration;

    /** 到达预设点位时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "到达预设点位时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date arrivePresetPriceTime;

    /** 预设结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "预设结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date presetEndTime;

    /** 启动点位 */
    @Excel(name = "启动点位")
    private BigDecimal startPrice;

    /** 启动延时 */
    @Excel(name = "启动延时")
    private Integer startDelayTime;

    /** 状态：0：已预设 1：已启动 */
    @Excel(name = "状态：0：已预设 1：已启动")
    private Integer status;

    /** 上行波动点位 */
    private BigDecimal upwardFluctuation;

    /** 下行波动点位 */
    private BigDecimal downWardFluctuation;

    public Integer getStartDelayTime() {
        return startDelayTime;
    }

    public void setStartDelayTime(Integer startDelayTime) {
        this.startDelayTime = startDelayTime;
    }

    public BigDecimal getUpwardFluctuation() {
        return upwardFluctuation;
    }

    public void setUpwardFluctuation(BigDecimal upwardFluctuation) {
        this.upwardFluctuation = upwardFluctuation;
    }

    public BigDecimal getDownWardFluctuation() {
        return downWardFluctuation;
    }

    public void setDownWardFluctuation(BigDecimal downWardFluctuation) {
        this.downWardFluctuation = downWardFluctuation;
    }

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
    public void setProductType(Integer productType) 
    {
        this.productType = productType;
    }

    public Integer getProductType() 
    {
        return productType;
    }
    public void setPresetPrice(BigDecimal presetPrice) 
    {
        this.presetPrice = presetPrice;
    }

    public BigDecimal getPresetPrice() 
    {
        return presetPrice;
    }
    public void setPresetStartTime(Date presetStartTime) 
    {
        this.presetStartTime = presetStartTime;
    }

    public Date getPresetStartTime() 
    {
        return presetStartTime;
    }
    public void setPresetDuration(Integer presetDuration) 
    {
        this.presetDuration = presetDuration;
    }

    public Integer getPresetDuration() 
    {
        return presetDuration;
    }
    public void setReturnDuration(Integer returnDuration) 
    {
        this.returnDuration = returnDuration;
    }

    public Integer getReturnDuration() 
    {
        return returnDuration;
    }
    public void setArrivePresetPriceTime(Date arrivePresetPriceTime) 
    {
        this.arrivePresetPriceTime = arrivePresetPriceTime;
    }

    public Date getArrivePresetPriceTime() 
    {
        return arrivePresetPriceTime;
    }
    public void setPresetEndTime(Date presetEndTime) 
    {
        this.presetEndTime = presetEndTime;
    }

    public Date getPresetEndTime() 
    {
        return presetEndTime;
    }
    public void setStartPrice(BigDecimal startPrice) 
    {
        this.startPrice = startPrice;
    }

    public BigDecimal getStartPrice() 
    {
        return startPrice;
    }
    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("productCode", getProductCode())
            .append("productType", getProductType())
            .append("presetPrice", getPresetPrice())
            .append("presetStartTime", getPresetStartTime())
            .append("presetDuration", getPresetDuration())
            .append("returnDuration", getReturnDuration())
            .append("arrivePresetPriceTime", getArrivePresetPriceTime())
            .append("presetEndTime", getPresetEndTime())
            .append("startPrice", getStartPrice())
            .append("status", getStatus())
            .toString();
    }
}
