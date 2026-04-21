package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.entity.UserInfoDetailVo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 币币交易订单对象 bibi_trade_order
 * 
 * @author ruoyi
 * @date 2024-06-24
 */
public class BibiTradeOrder extends UserInfoDetailVo
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 订单编号 */
    @Excel(name = "订单编号")
    private String orderCode;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    /** 产品代码 */
    @Excel(name = "产品代码")
    private String productCode;

    /** 产品名称 */
    @Excel(name = "产品代码")
    private String productName;

    /** 产品类型 （1：股票 2：加密货币 3：期货 4：外汇） */
    @Excel(name = "产品类型 ", readConverterExp = "1=：股票,2=：加密货币,3=：期货,4=：外汇")
    private Integer productType;

    /** 订单金额 */
    @Excel(name = "订单金额")
    private BigDecimal orderAmount;

    /** 成交额 */
    @Excel(name = "成交额")
    private BigDecimal turnoverAmount;

    /** 成交量 */
    @Excel(name = "成交量")
    private BigDecimal orderVolume;

    /** 手续费 */
    @Excel(name = "手续费")
    private BigDecimal handingFee;

    /** 产品价格 */
    @Excel(name = "产品价格")
    private BigDecimal productPrice;

    /** 订单类型 0：买入 1：卖出 */
    @Excel(name = "订单类型 0：买入 1：卖出")
    private Integer orderType;

    /** 订单方式 0：限价委托 1：市价委托 */
    @Excel(name = "订单方式 0：限价委托 1：市价委托")
    private Integer orderMethod;

    /** 订单状态 0：委托中 1：成交 2：撤单 3：冻结 */
    @Excel(name = "订单状态 0：委托中 1：成交 2：撤单 3：冻结")
    private Integer orderStatus;

    /** 币种id */
    @Excel(name = "币种id")
    private Long currencyId;

    /** 币种名称 */
    @Excel(name = "币种名称")
    private String currencyName;

    /** sql版本 */
    @Excel(name = "sql版本")
    private Long sqlVersion;

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setOrderCode(String orderCode) 
    {
        this.orderCode = orderCode;
    }

    public String getOrderCode() 
    {
        return orderCode;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductType(Integer productType)
    {
        this.productType = productType;
    }

    public Integer getProductType() 
    {
        return productType;
    }
    public void setOrderAmount(BigDecimal orderAmount) 
    {
        this.orderAmount = orderAmount;
    }

    public BigDecimal getOrderAmount() 
    {
        return orderAmount;
    }
    public void setTurnoverAmount(BigDecimal turnoverAmount) 
    {
        this.turnoverAmount = turnoverAmount;
    }

    public BigDecimal getTurnoverAmount() 
    {
        return turnoverAmount;
    }
    public void setOrderVolume(BigDecimal orderVolume) 
    {
        this.orderVolume = orderVolume;
    }

    public BigDecimal getOrderVolume() 
    {
        return orderVolume;
    }
    public void setHandingFee(BigDecimal handingFee) 
    {
        this.handingFee = handingFee;
    }

    public BigDecimal getHandingFee() 
    {
        return handingFee;
    }
    public void setProductPrice(BigDecimal productPrice) 
    {
        this.productPrice = productPrice;
    }

    public BigDecimal getProductPrice() 
    {
        return productPrice;
    }
    public void setOrderType(Integer orderType) 
    {
        this.orderType = orderType;
    }

    public Integer getOrderType() 
    {
        return orderType;
    }
    public void setOrderMethod(Integer orderMethod) 
    {
        this.orderMethod = orderMethod;
    }

    public Integer getOrderMethod() 
    {
        return orderMethod;
    }
    public void setOrderStatus(Integer orderStatus) 
    {
        this.orderStatus = orderStatus;
    }

    public Integer getOrderStatus() 
    {
        return orderStatus;
    }
    public void setSqlVersion(Long sqlVersion) 
    {
        this.sqlVersion = sqlVersion;
    }

    public Long getSqlVersion() 
    {
        return sqlVersion;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("orderCode", getOrderCode())
            .append("userId", getUserId())
            .append("productCode", getProductCode())
            .append("productType", getProductType())
            .append("orderAmount", getOrderAmount())
            .append("turnoverAmount", getTurnoverAmount())
            .append("orderVolume", getOrderVolume())
            .append("handingFee", getHandingFee())
            .append("productPrice", getProductPrice())
            .append("orderType", getOrderType())
            .append("orderMethod", getOrderMethod())
            .append("orderStatus", getOrderStatus())
            .append("createTime", getCreateTime())
            .append("remark", getRemark())
            .append("sqlVersion", getSqlVersion())
            .toString();
    }
}
