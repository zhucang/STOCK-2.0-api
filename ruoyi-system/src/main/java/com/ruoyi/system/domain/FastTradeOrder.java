package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.entity.UserInfoDetailVo;
import com.ruoyi.system.domain.vo.BigDecimalSerializer2;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 极速交易订单对象 fast_trade_order
 * 
 * @author ruoyi
 * @date 2023-11-02
 */
public class FastTradeOrder extends UserInfoDetailVo
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 产品类型 （1：股票 2：加密货币 3：期货 4：外汇） */
    @Excel(name = "产品类型 ", readConverterExp = "1=：股票,2=：加密货币,3=：期货,4=：外汇")
    private Integer productType;

    /** 订单编号 */
    @Excel(name = "订单编号")
    private String orderCode;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    /** 现价 */
    private BigDecimal nowPrice;

    /** 产品代码 */
    @Excel(name = "产品代码")
    private String productCode;

    /** 产品名称 */
    @Excel(name = "产品名称")
    private String productName;

    /** 产品产品描述 */
    @Excel(name = "产品描述")
    private String productDesc;

    /** 交易方向0：涨 1：跌 */
    @Excel(name = "交易方向 0：涨 1：跌")
    private Integer orderDirection;

    /** 下单金额 */
    @Excel(name = "下单金额")
    private BigDecimal orderPrice;

    /** 手续费 */
    @Excel(name = "手续费")
    private BigDecimal handingFee;

    /** 下单时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "下单时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date orderTime;

    /** 极速交易选项id */
    @Excel(name = "极速交易选项id")
    private Long fastTradeOrderOptionsId;

    /** 订单时长 */
    @Excel(name = "订单时长")
    private Integer durationValue;

    /** 时长标签（0:秒 1：分钟 2：小时 3:天） */
    @Excel(name = "时长标签", readConverterExp = "0=:秒,1=：分钟,2=：小时 3:天")
    private Integer durationLabel;

    /** 赢收益比率 */
    @Excel(name = "赢收益比率")
    private BigDecimal winProfitRatio;

    /** 输扣除比率 */
    @Excel(name = "输扣除比率")
    private BigDecimal loseProfitRatio;

    /** 买入价格 */
    @Excel(name = "买入价格")
    @JsonSerialize(using = BigDecimalSerializer2.class)
    private BigDecimal buyPrice;

    /** 结算价格 */
    @Excel(name = "结算价格")
    @JsonSerialize(using = BigDecimalSerializer2.class)
    private BigDecimal sellPrice;

    /** 订单状态 0：待结算 1：已结算 */
    @Excel(name = "订单状态 0：待结算 1：已结算")
    private Integer orderStatus;

    /** 结算时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "结算时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date deliverTime;

    /** 订单收益 */
    @Excel(name = "订单收益")
    private BigDecimal orderProfit;

    /** 货币id */
    @Excel(name = "货币id")
    private Long currencyId;

    /** 输钱方式：0:全部扣除 1：按收益率扣除 */
    @Excel(name = "输钱方式：0:全部扣除 1：按收益率扣除")
    private Integer loseMoneyMethod;

    /** 订单单控状态：0：未控  1：赢  2：输 3：平 */
    @Excel(name = "订单单控状态：0：未控  1：赢  2：输 3：平")
    private Integer orderControlFlag;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public Integer getProductType() {
        return productType;
    }

    public BigDecimal getHandingFee() {
        return handingFee;
    }

    public void setHandingFee(BigDecimal handingFee) {
        this.handingFee = handingFee;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
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

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public void setProductCode(String productCode)
    {
        this.productCode = productCode;
    }

    public String getProductCode() 
    {
        return productCode;
    }
    public void setOrderDirection(Integer orderDirection) 
    {
        this.orderDirection = orderDirection;
    }

    public Integer getOrderDirection() 
    {
        return orderDirection;
    }
    public void setOrderPrice(BigDecimal orderPrice) 
    {
        this.orderPrice = orderPrice;
    }

    public BigDecimal getOrderPrice() 
    {
        return orderPrice;
    }
    public void setOrderTime(Date orderTime) 
    {
        this.orderTime = orderTime;
    }

    public Date getOrderTime() 
    {
        return orderTime;
    }
    public void setFastTradeOrderOptionsId(Long fastTradeOrderOptionsId) 
    {
        this.fastTradeOrderOptionsId = fastTradeOrderOptionsId;
    }

    public Long getFastTradeOrderOptionsId() 
    {
        return fastTradeOrderOptionsId;
    }
    public void setDurationValue(Integer durationValue) 
    {
        this.durationValue = durationValue;
    }

    public Integer getDurationValue() 
    {
        return durationValue;
    }
    public void setDurationLabel(Integer durationLabel) 
    {
        this.durationLabel = durationLabel;
    }

    public Integer getDurationLabel() 
    {
        return durationLabel;
    }


    public BigDecimal getWinProfitRatio() {
        return winProfitRatio;
    }

    public void setWinProfitRatio(BigDecimal winProfitRatio) {
        this.winProfitRatio = winProfitRatio;
    }

    public BigDecimal getLoseProfitRatio() {
        return loseProfitRatio;
    }

    public void setLoseProfitRatio(BigDecimal loseProfitRatio) {
        this.loseProfitRatio = loseProfitRatio;
    }

    public void setBuyPrice(BigDecimal buyPrice)
    {
        this.buyPrice = buyPrice;
    }

    public BigDecimal getBuyPrice() 
    {
        return buyPrice;
    }
    public void setSellPrice(BigDecimal sellPrice) 
    {
        this.sellPrice = sellPrice;
    }

    public BigDecimal getSellPrice() 
    {
        return sellPrice;
    }
    public void setOrderStatus(Integer orderStatus) 
    {
        this.orderStatus = orderStatus;
    }

    public Integer getOrderStatus() 
    {
        return orderStatus;
    }
    public void setDeliverTime(Date deliverTime) 
    {
        this.deliverTime = deliverTime;
    }

    public Date getDeliverTime() 
    {
        return deliverTime;
    }
    public void setOrderProfit(BigDecimal orderProfit) 
    {
        this.orderProfit = orderProfit;
    }

    public BigDecimal getOrderProfit() 
    {
        return orderProfit;
    }
    public void setCurrencyId(Long currencyId) 
    {
        this.currencyId = currencyId;
    }

    public Long getCurrencyId() 
    {
        return currencyId;
    }
    public void setLoseMoneyMethod(Integer loseMoneyMethod) 
    {
        this.loseMoneyMethod = loseMoneyMethod;
    }

    public Integer getLoseMoneyMethod() 
    {
        return loseMoneyMethod;
    }
    public void setOrderControlFlag(Integer orderControlFlag) 
    {
        this.orderControlFlag = orderControlFlag;
    }

    public Integer getOrderControlFlag() 
    {
        return orderControlFlag;
    }

    public BigDecimal getNowPrice() {
        return nowPrice;
    }

    public void setNowPrice(BigDecimal nowPrice) {
        this.nowPrice = nowPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("productType", getProductType())
            .append("orderCode", getOrderCode())
            .append("userId", getUserId())
            .append("productCode", getProductCode())
            .append("orderDirection", getOrderDirection())
            .append("orderPrice", getOrderPrice())
            .append("orderTime", getOrderTime())
            .append("fastTradeOrderOptionsId", getFastTradeOrderOptionsId())
            .append("durationValue", getDurationValue())
            .append("durationLabel", getDurationLabel())
            .append("winProfitRatio", getWinProfitRatio())
            .append("loseProfitRatio", getLoseProfitRatio())
            .append("buyPrice", getBuyPrice())
            .append("sellPrice", getSellPrice())
            .append("orderStatus", getOrderStatus())
            .append("deliverTime", getDeliverTime())
            .append("orderProfit", getOrderProfit())
            .append("currencyId", getCurrencyId())
            .append("loseMoneyMethod", getLoseMoneyMethod())
            .append("orderControlFlag", getOrderControlFlag())
            .toString();
    }
}
