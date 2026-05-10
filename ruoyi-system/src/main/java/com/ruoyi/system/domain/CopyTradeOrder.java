package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 跟单订单映射对象 copy_trade_order
 * 记录交易员主仓位与跟单用户子仓位之间的一对一映射关系。
 */
public class CopyTradeOrder extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键ID。 */
    private Long id;

    /** 跟单关系(跟单人员)ID。 */
    @Excel(name = "跟单关系(跟单人员)ID")
    private Long relationId;

    /** 交易员用户ID。 */
    @Excel(name = "交易员用户ID")
    private Long traderUserId;

    /** 产品类型 1股票 2加密货币 3期货 4外汇。 */
    @Excel(name = "产品类型 1股票 2加密货币 3期货 4外汇")
    private Integer productType;

    /** 主单持仓ID。 */
    @Excel(name = "主单持仓ID")
    private Long leaderPositionId;

    /** 主单产品代码快照。 */
    @Excel(name = "主单产品代码")
    private String productCode;

    /** 主单开仓方向快照 0买涨 1买跌。 */
    @Excel(name = "主单开仓方向 0买涨 1买跌")
    private Integer orderDirection;

    /** 主单开仓价格快照。 */
    @Excel(name = "主单开仓价格")
    private BigDecimal buyOrderPrice;

    /** 跟单用户ID。 */
    @Excel(name = "跟单用户ID")
    private Long followerUserId;

    /** 跟单持仓ID。 */
    @Excel(name = "跟单持仓ID")
    private Long followerPositionId;

    /** 跟单模式快照 0固定金额 1按比例。 */
    @Excel(name = "跟单模式 0固定金额 1按比例")
    private Integer followMode;

    /** 固定金额模式下本次跟单金额快照。 */
    @Excel(name = "固定跟单金额快照")
    private BigDecimal followAmount;

    /** 比例模式下本次跟单比例快照。 */
    @Excel(name = "跟单比例快照")
    private BigDecimal followRatio;

    /** 跟单实际使用保证金快照。 */
    @Excel(name = "跟单保证金快照")
    private BigDecimal marginAmount;

    /** 跟单实际使用杠杆快照。 */
    @Excel(name = "跟单杠杆快照")
    private Integer orderLever;

    /** 主单订单号快照。 */
    @Excel(name = "主单订单号")
    private String leaderOrderCode;

    /** 跟单单订单号快照。 */
    @Excel(name = "跟单订单号")
    private String followerOrderCode;

    /** 状态 0持仓中 1已结束。 */
    @Excel(name = "状态 0持仓中 1已结束")
    private Integer status;

    /** 补充备注，用于记录同步来源或处理结果。 */
    @Excel(name = "备注")
    private String remark;

    /** 获取主键ID。 */
    public Long getId() {
        return id;
    }

    /** 设置主键ID。 */
    public void setId(Long id) {
        this.id = id;
    }

    /** 获取跟单关系(跟单人员)ID。 */
    public Long getRelationId() {
        return relationId;
    }

    /** 设置跟单关系(跟单人员)ID。 */
    public void setRelationId(Long relationId) {
        this.relationId = relationId;
    }

    /** 获取交易员用户ID。 */
    public Long getTraderUserId() {
        return traderUserId;
    }

    /** 设置交易员用户ID。 */
    public void setTraderUserId(Long traderUserId) {
        this.traderUserId = traderUserId;
    }

    /** 获取产品类型。 */
    public Integer getProductType() {
        return productType;
    }

    /** 设置产品类型。 */
    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    /** 获取主单持仓ID。 */
    public Long getLeaderPositionId() {
        return leaderPositionId;
    }

    /** 设置主单持仓ID。 */
    public void setLeaderPositionId(Long leaderPositionId) {
        this.leaderPositionId = leaderPositionId;
    }

    /** 获取主单产品代码快照。 */
    public String getProductCode() {
        return productCode;
    }

    /** 设置主单产品代码快照。 */
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    /** 获取主单开仓方向快照。 */
    public Integer getOrderDirection() {
        return orderDirection;
    }

    /** 设置主单开仓方向快照。 */
    public void setOrderDirection(Integer orderDirection) {
        this.orderDirection = orderDirection;
    }

    /** 获取主单开仓价格快照。 */
    public BigDecimal getBuyOrderPrice() {
        return buyOrderPrice;
    }

    /** 设置主单开仓价格快照。 */
    public void setBuyOrderPrice(BigDecimal buyOrderPrice) {
        this.buyOrderPrice = buyOrderPrice;
    }

    /** 获取跟单用户ID。 */
    public Long getFollowerUserId() {
        return followerUserId;
    }

    /** 设置跟单用户ID。 */
    public void setFollowerUserId(Long followerUserId) {
        this.followerUserId = followerUserId;
    }

    /** 获取跟单持仓ID。 */
    public Long getFollowerPositionId() {
        return followerPositionId;
    }

    /** 设置跟单持仓ID。 */
    public void setFollowerPositionId(Long followerPositionId) {
        this.followerPositionId = followerPositionId;
    }

    /** 获取跟单模式快照。 */
    public Integer getFollowMode() {
        return followMode;
    }

    /** 设置跟单模式快照。 */
    public void setFollowMode(Integer followMode) {
        this.followMode = followMode;
    }

    /** 获取固定跟单金额快照。 */
    public BigDecimal getFollowAmount() {
        return followAmount;
    }

    /** 设置固定跟单金额快照。 */
    public void setFollowAmount(BigDecimal followAmount) {
        this.followAmount = followAmount;
    }

    /** 获取跟单比例快照。 */
    public BigDecimal getFollowRatio() {
        return followRatio;
    }

    /** 设置跟单比例快照。 */
    public void setFollowRatio(BigDecimal followRatio) {
        this.followRatio = followRatio;
    }

    /** 获取跟单保证金快照。 */
    public BigDecimal getMarginAmount() {
        return marginAmount;
    }

    /** 设置跟单保证金快照。 */
    public void setMarginAmount(BigDecimal marginAmount) {
        this.marginAmount = marginAmount;
    }

    /** 获取跟单杠杆快照。 */
    public Integer getOrderLever() {
        return orderLever;
    }

    /** 设置跟单杠杆快照。 */
    public void setOrderLever(Integer orderLever) {
        this.orderLever = orderLever;
    }

    /** 获取主单订单号快照。 */
    public String getLeaderOrderCode() {
        return leaderOrderCode;
    }

    /** 设置主单订单号快照。 */
    public void setLeaderOrderCode(String leaderOrderCode) {
        this.leaderOrderCode = leaderOrderCode;
    }

    /** 获取跟单单订单号快照。 */
    public String getFollowerOrderCode() {
        return followerOrderCode;
    }

    /** 设置跟单单订单号快照。 */
    public void setFollowerOrderCode(String followerOrderCode) {
        this.followerOrderCode = followerOrderCode;
    }

    /** 获取状态。 */
    public Integer getStatus() {
        return status;
    }

    /** 设置状态。 */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /** 获取备注。 */
    public String getRemark() {
        return remark;
    }

    /** 设置备注。 */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /** 输出对象调试信息。 */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("relationId", getRelationId())
                .append("traderUserId", getTraderUserId())
                .append("productType", getProductType())
                .append("leaderPositionId", getLeaderPositionId())
                .append("productCode", getProductCode())
                .append("orderDirection", getOrderDirection())
                .append("buyOrderPrice", getBuyOrderPrice())
                .append("followerUserId", getFollowerUserId())
                .append("followerPositionId", getFollowerPositionId())
                .append("followMode", getFollowMode())
                .append("followAmount", getFollowAmount())
                .append("followRatio", getFollowRatio())
                .append("marginAmount", getMarginAmount())
                .append("orderLever", getOrderLever())
                .append("leaderOrderCode", getLeaderOrderCode())
                .append("followerOrderCode", getFollowerOrderCode())
                .append("status", getStatus())
                .append("remark", getRemark())
                .toString();
    }
}
