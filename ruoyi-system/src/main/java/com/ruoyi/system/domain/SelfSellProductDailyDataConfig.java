package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 自营产品每日行情数据配置对象 self_sell_product_daily_data_config
 * 
 * @author ruoyi
 * @date 2023-11-27
 */
public class SelfSellProductDailyDataConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 自营产品id */
    @Excel(name = "自营产品id")
    private Long selfSellProductId;

    /** 产品代码 */
    @Excel(name = "产品代码")
    private String productCode;

    /** 是否默认：0：是 1：否 */
    @Excel(name = "是否默认：0：是 1：否")
    private Integer isDefault;

    /** 设置的最终涨跌幅 */
    @Excel(name = "设置的最终涨跌幅")
    private BigDecimal finallyChangeRate;

    /** 设置的最终价格 */
    @Excel(name = "设置的最终价格")
    private BigDecimal finallyPrice;

    /** 是否根据其他支的产品模板复刻数据0：是 1：否 */
    @Excel(name = "是否根据其他支的产品模板复刻数据0：是 1：否")
    private Integer isTemp;

    /** 模板产品代码 */
    @Excel(name = "模板产品代码")
    private String tempProductCode;

    /** 产品类型 1：股票 2：加密货币 */
    @Excel(name = "产品类型 1：股票 2：加密货币")
    private Integer productType;


    public Long getSelfSellProductId() {
        return selfSellProductId;
    }

    public void setSelfSellProductId(Long selfSellProductId) {
        this.selfSellProductId = selfSellProductId;
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
    public void setIsDefault(Integer isDefault) 
    {
        this.isDefault = isDefault;
    }

    public Integer getIsDefault() 
    {
        return isDefault;
    }
    public void setFinallyChangeRate(BigDecimal finallyChangeRate) 
    {
        this.finallyChangeRate = finallyChangeRate;
    }

    public BigDecimal getFinallyChangeRate() 
    {
        return finallyChangeRate;
    }
    public void setFinallyPrice(BigDecimal finallyPrice) 
    {
        this.finallyPrice = finallyPrice;
    }

    public BigDecimal getFinallyPrice() 
    {
        return finallyPrice;
    }
    public void setIsTemp(Integer isTemp) 
    {
        this.isTemp = isTemp;
    }

    public Integer getIsTemp() 
    {
        return isTemp;
    }
    public void setTempProductCode(String tempProductCode) 
    {
        this.tempProductCode = tempProductCode;
    }

    public String getTempProductCode() 
    {
        return tempProductCode;
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
            .append("productCode", getProductCode())
            .append("isDefault", getIsDefault())
            .append("finallyChangeRate", getFinallyChangeRate())
            .append("finallyPrice", getFinallyPrice())
            .append("isTemp", getIsTemp())
            .append("tempProductCode", getTempProductCode())
            .append("productType", getProductType())
            .toString();
    }
}
