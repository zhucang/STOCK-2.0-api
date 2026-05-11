package com.ruoyi.system.domain;

import java.math.BigDecimal;

/**
 * 跟单使用的持仓快照。
 * 用于屏蔽股票、加密货币、期货、外汇持仓对象差异。
 */
public class CopyTradePositionSnapshot {
    /** 产品类型：股票。 */
    public static final int PRODUCT_TYPE_STOCK = 1;

    /** 产品类型：加密货币。 */
    public static final int PRODUCT_TYPE_CRYPTOCURRENCY = 2;

    /** 产品类型：期货。 */
    public static final int PRODUCT_TYPE_FUTURES = 3;

    /** 产品类型：外汇。 */
    public static final int PRODUCT_TYPE_FOREX = 4;

    /** 产品类型，必须和 copy_trade_order.product_type、各产品交易设置保持一致。 */
    private Integer productType;

    /** 各产品持仓表的主键ID。 */
    private Long id;

    /** 持仓所属用户ID，交易员主单用于查交易员配置，跟单子单用于记录映射。 */
    private Long userId;

    /** 产品代码快照。 */
    private String productCode;

    /** 交易方向 0买涨 1买跌。 */
    private Integer orderDirection;

    /** 开仓价格快照。 */
    private BigDecimal buyOrderPrice;

    /** 平仓价格快照。 */
    private BigDecimal sellOrderPrice;

    /** 开仓总市值，用于按比例跟单计算保证金。 */
    private BigDecimal orderTotalPrice;

    /** 杠杆倍数，用于计算保证金。 */
    private Integer orderLever;

    /** 订单号快照。 */
    private String orderCode;

    /** 持仓状态 0持仓中 1已平仓。 */
    private Integer orderStatus;

    /** 止盈价，跟单子单继承主单设置。 */
    private BigDecimal stopProfitPrice;

    /** 止损价，跟单子单继承主单设置。 */
    private BigDecimal stopLossPrice;

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Integer getOrderDirection() {
        return orderDirection;
    }

    public void setOrderDirection(Integer orderDirection) {
        this.orderDirection = orderDirection;
    }

    public BigDecimal getBuyOrderPrice() {
        return buyOrderPrice;
    }

    public void setBuyOrderPrice(BigDecimal buyOrderPrice) {
        this.buyOrderPrice = buyOrderPrice;
    }

    public BigDecimal getSellOrderPrice() {
        return sellOrderPrice;
    }

    public void setSellOrderPrice(BigDecimal sellOrderPrice) {
        this.sellOrderPrice = sellOrderPrice;
    }

    public BigDecimal getOrderTotalPrice() {
        return orderTotalPrice;
    }

    public void setOrderTotalPrice(BigDecimal orderTotalPrice) {
        this.orderTotalPrice = orderTotalPrice;
    }

    public Integer getOrderLever() {
        return orderLever;
    }

    public void setOrderLever(Integer orderLever) {
        this.orderLever = orderLever;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public BigDecimal getStopProfitPrice() {
        return stopProfitPrice;
    }

    public void setStopProfitPrice(BigDecimal stopProfitPrice) {
        this.stopProfitPrice = stopProfitPrice;
    }

    public BigDecimal getStopLossPrice() {
        return stopLossPrice;
    }

    public void setStopLossPrice(BigDecimal stopLossPrice) {
        this.stopLossPrice = stopLossPrice;
    }
}
