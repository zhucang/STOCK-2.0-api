package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.entity.UserInfoDetailVo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 现货交易订单对象 spot_trade_order
 * 
 * @author ruoyi
 * @date 2023-11-05
 */
public class SpotTradeOrder extends UserInfoDetailVo
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 产品类型：1：美股 2：加密货币 3:期货 4：外汇 */
    @Excel(name = "产品类型：1：美股 2：加密货币 3:期货 4：外汇")
    private Integer productType;

    /** 订单编号 */
    @Excel(name = "订单编号")
    private String orderCode;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    /** 产品代码 */
    @Excel(name = "产品代码")
    private String productCode;

    /** 产品产品描述 */
    @Excel(name = "产品描述")
    private String productDesc;

    /** 产品名称 */
    @Excel(name = "产品名称")
    private String productName;

    /** 现价 */
    @Excel(name = "现价")
    private BigDecimal nowPrice;

    /** 购买时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "购买时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date buyOrderTime;

    /** 购买价格 */
    @Excel(name = "购买价格")
    private BigDecimal buyOrderPrice;

    /** 卖出时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "卖出时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date sellOrderTime;

    /** 卖出价格 */
    @Excel(name = "卖出价格")
    private BigDecimal sellOrderPrice;

    /** 订单数量 */
    @Excel(name = "订单数量")
    private BigDecimal orderNum;

    /** 订单总金额 */
    @Excel(name = "订单总金额")
    private BigDecimal orderTotalPrice;

    /** 手续费 */
    @Excel(name = "手续费")
    private BigDecimal orderFee;

    /** 浮动盈亏 */
    @Excel(name = "浮动盈亏")
    private BigDecimal profitAndLose;

    /** 总盈亏 */
    @Excel(name = "总盈亏")
    private BigDecimal allProfitAndLose;

    /** 币种id */
    @Excel(name = "币种id")
    private Long currencyId;

    /** 状态0：未卖出 1：已卖出 */
    @Excel(name = "状态0：未卖出 1：已卖出")
    private Integer orderStatus;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setProductType(Integer productType) 
    {
        this.productType = productType;
    }

    public Integer getProductType() 
    {
        return productType;
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
    public void setProductName(String productName) 
    {
        this.productName = productName;
    }

    public String getProductName() 
    {
        return productName;
    }
    public void setBuyOrderTime(Date buyOrderTime) 
    {
        this.buyOrderTime = buyOrderTime;
    }

    public Date getBuyOrderTime() 
    {
        return buyOrderTime;
    }
    public void setBuyOrderPrice(BigDecimal buyOrderPrice) 
    {
        this.buyOrderPrice = buyOrderPrice;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public BigDecimal getBuyOrderPrice()
    {
        return buyOrderPrice;
    }
    public void setSellOrderTime(Date sellOrderTime) 
    {
        this.sellOrderTime = sellOrderTime;
    }

    public Date getSellOrderTime() 
    {
        return sellOrderTime;
    }
    public void setSellOrderPrice(BigDecimal sellOrderPrice) 
    {
        this.sellOrderPrice = sellOrderPrice;
    }

    public BigDecimal getSellOrderPrice() 
    {
        return sellOrderPrice;
    }
    public void setOrderNum(BigDecimal orderNum) 
    {
        this.orderNum = orderNum;
    }

    public BigDecimal getOrderNum() 
    {
        return orderNum;
    }
    public void setOrderTotalPrice(BigDecimal orderTotalPrice) 
    {
        this.orderTotalPrice = orderTotalPrice;
    }

    public BigDecimal getOrderTotalPrice() 
    {
        return orderTotalPrice;
    }
    public void setOrderFee(BigDecimal orderFee) 
    {
        this.orderFee = orderFee;
    }

    public BigDecimal getOrderFee() 
    {
        return orderFee;
    }
    public void setProfitAndLose(BigDecimal profitAndLose) 
    {
        this.profitAndLose = profitAndLose;
    }

    public BigDecimal getProfitAndLose() 
    {
        return profitAndLose;
    }
    public void setAllProfitAndLose(BigDecimal allProfitAndLose) 
    {
        this.allProfitAndLose = allProfitAndLose;
    }

    public BigDecimal getAllProfitAndLose() 
    {
        return allProfitAndLose;
    }
    public void setCurrencyId(Long currencyId) 
    {
        this.currencyId = currencyId;
    }

    public Long getCurrencyId() 
    {
        return currencyId;
    }


    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public BigDecimal getNowPrice() {
        return nowPrice;
    }

    public void setNowPrice(BigDecimal nowPrice) {
        this.nowPrice = nowPrice;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("productType", getProductType())
            .append("orderCode", getOrderCode())
            .append("userId", getUserId())
            .append("productCode", getProductCode())
            .append("productName", getProductName())
            .append("buyOrderTime", getBuyOrderTime())
            .append("buyOrderPrice", getBuyOrderPrice())
            .append("sellOrderTime", getSellOrderTime())
            .append("sellOrderPrice", getSellOrderPrice())
            .append("orderNum", getOrderNum())
            .append("orderTotalPrice", getOrderTotalPrice())
            .append("orderFee", getOrderFee())
            .append("profitAndLose", getProfitAndLose())
            .append("allProfitAndLose", getAllProfitAndLose())
            .append("currencyId", getCurrencyId())
            .append("status", getOrderStatus())
            .toString();
    }
}
