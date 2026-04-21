package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.entity.UserInfoDetailVo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户合约交易订单对象 user_product_position
 * 
 * @author ruoyi
 * @date 2025-06-25
 */
public class UserProductPosition extends UserInfoDetailVo
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 订单号 */
    @Excel(name = "订单号")
    private String orderCode;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    /** 产品代码 */
    @Excel(name = "产品代码")
    private String productCode;

    /** 产品类型 1：股票 2：加密货币 3：期货 4：外汇 */
    @Excel(name = "产品类型 1：股票 2：加密货币 3：期货 4：外汇")
    private Integer productType;

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

    /** 当前价格 */
    @Excel(name = "当前价格")
    private BigDecimal nowPrice;

    /** 下单方向 0：买涨 1：买跌 */
    @Excel(name = "下单方向 0：买涨 1：买跌")
    private Integer orderDirection;

    /** 订单买入花费 */
    @Excel(name = "订单买入花费")
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

    /** 是否锁仓 0：否 1：是 */
    @Excel(name = "是否锁仓 0：否 1：是")
    private Integer isLock;

    /** 锁仓原因 */
    @Excel(name = "锁仓原因")
    private String lockMsg;

    /** 币种id */
    @Excel(name = "币种id")
    private Long currencyId;

    /** 止盈线 */
    @Excel(name = "止盈线")
    private BigDecimal stopProfitPrice;

    /** 止损线 */
    @Excel(name = "止损线")
    private BigDecimal stopLossPrice;

    /** 订单状态：0：持仓中 1：已平仓 */
    @Excel(name = "订单状态：0：持仓中 1：已平仓")
    private Integer orderStatus;

    /** sql版本 */
    @Excel(name = "sql版本")
    private Long sqlVersion;

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
    public void setProductType(Integer productType) 
    {
        this.productType = productType;
    }

    public Integer getProductType() 
    {
        return productType;
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

    public BigDecimal getNowPrice() {
        return nowPrice;
    }

    public void setNowPrice(BigDecimal nowPrice) {
        this.nowPrice = nowPrice;
    }

    public void setOrderDirection(Integer orderDirection)
    {
        this.orderDirection = orderDirection;
    }

    public Integer getOrderDirection() 
    {
        return orderDirection;
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
    public void setIsLock(Integer isLock) 
    {
        this.isLock = isLock;
    }

    public Integer getIsLock() 
    {
        return isLock;
    }
    public void setLockMsg(String lockMsg) 
    {
        this.lockMsg = lockMsg;
    }

    public String getLockMsg() 
    {
        return lockMsg;
    }
    public void setCurrencyId(Long currencyId) 
    {
        this.currencyId = currencyId;
    }

    public Long getCurrencyId() 
    {
        return currencyId;
    }
    public void setStopProfitPrice(BigDecimal stopProfitPrice) 
    {
        this.stopProfitPrice = stopProfitPrice;
    }

    public BigDecimal getStopProfitPrice() 
    {
        return stopProfitPrice;
    }
    public void setStopLossPrice(BigDecimal stopLossPrice) 
    {
        this.stopLossPrice = stopLossPrice;
    }

    public BigDecimal getStopLossPrice() 
    {
        return stopLossPrice;
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
            .append("buyOrderTime", getBuyOrderTime())
            .append("buyOrderPrice", getBuyOrderPrice())
            .append("sellOrderTime", getSellOrderTime())
            .append("sellOrderPrice", getSellOrderPrice())
            .append("orderDirection", getOrderDirection())
            .append("orderTotalPrice", getOrderTotalPrice())
            .append("orderFee", getOrderFee())
            .append("profitAndLose", getProfitAndLose())
            .append("allProfitAndLose", getAllProfitAndLose())
            .append("isLock", getIsLock())
            .append("lockMsg", getLockMsg())
            .append("currencyId", getCurrencyId())
            .append("stopProfitPrice", getStopProfitPrice())
            .append("stopLossPrice", getStopLossPrice())
            .append("orderStatus", getOrderStatus())
            .append("sqlVersion", getSqlVersion())
            .toString();
    }
}
