package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 极速交易下单选项对象 fast_trade_order_options
 * 
 * @author ruoyi
 * @date 2023-11-02
 */
public class FastTradeOrderOptions extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** ids */
    private List<Long> ids;

    /** 产品名称 */
    @Excel(name = "产品名称")
    private String productName;

    /** 产品代码 */
    @Excel(name = "产品代码")
    private String productCode;

    /** 产品类型（1：美股 2：加密货币 3:期货 4：外汇） */
    @Excel(name = "产品类型", readConverterExp = "1=：美股,2=：加密货币,3=:期货,4=：外汇")
    private Integer productType;

    /** 时长 */
    @Excel(name = "时长")
    private Integer durationValue;

    /** 时长标签（0:秒 1：分钟 2：小时 3:天） */
    @Excel(name = "时长标签", readConverterExp = "0=:秒,1=：分钟,2=：小时 3:天")
    private Integer durationLabel;

    /** 状态: 0启用 1停用 */
    @Excel(name = "状态: 0启用 1停用")
    private Integer status;

    /** 涨赢收益率 */
    @Excel(name = "涨赢收益率")
    private BigDecimal upWinProfitRatio;

    /** 涨输扣除率 */
    @Excel(name = "涨输扣除率")
    private BigDecimal upLoseProfitRatio;

    /** 涨波动率 */
    @Excel(name = "涨波动率")
    private BigDecimal upFluctuationRatio;

    /** 跌赢收益率 */
    @Excel(name = "跌赢收益率")
    private BigDecimal downWinProfitRatio;

    /** 跌输扣除率 */
    @Excel(name = "跌输扣除率")
    private BigDecimal downLoseProfitRatio;

    /** 跌波动率 */
    @Excel(name = "跌波动率")
    private BigDecimal downFluctuationRatio;

    /** 最小买入金额 */
    @Excel(name = "最小买入金额")
    private BigDecimal minBuyAmount;

    /** 最大买入金额 */
    @Excel(name = "最大买入金额")
    private BigDecimal maxBuyAmount;

    /** 用户金额大于此刻度可下单 */
    @Excel(name = "用户金额大于此刻度可下单")
    private BigDecimal minUserAmount;

    /** 收益率方式 0:固定   1:波动 */
    @Excel(name = "收益率方式 0:固定   1:波动")
    private Integer profitRatioMethod;

    /** 输钱方式：0:全部扣除 1：按收益率扣除 */
    @Excel(name = "输钱方式：0:全部扣除 1：按收益率扣除")
    private Integer loseMoneyMethod;

    /** vip等级限制 */
    private String vipLevelLimit;

    /** 排序 */
    private Integer sort;

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getVipLevelLimit() {
        return vipLevelLimit;
    }

    public void setVipLevelLimit(String vipLevelLimit) {
        this.vipLevelLimit = vipLevelLimit;
    }

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public String getProductName() 
    {
        return productName;
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
    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }

    public void setUpFluctuationRatio(BigDecimal upFluctuationRatio) 
    {
        this.upFluctuationRatio = upFluctuationRatio;
    }

    public BigDecimal getUpFluctuationRatio() 
    {
        return upFluctuationRatio;
    }

    public BigDecimal getUpWinProfitRatio() {
        return upWinProfitRatio;
    }

    public void setUpWinProfitRatio(BigDecimal upWinProfitRatio) {
        this.upWinProfitRatio = upWinProfitRatio;
    }

    public BigDecimal getUpLoseProfitRatio() {
        return upLoseProfitRatio;
    }

    public void setUpLoseProfitRatio(BigDecimal upLoseProfitRatio) {
        this.upLoseProfitRatio = upLoseProfitRatio;
    }

    public BigDecimal getDownWinProfitRatio() {
        return downWinProfitRatio;
    }

    public void setDownWinProfitRatio(BigDecimal downWinProfitRatio) {
        this.downWinProfitRatio = downWinProfitRatio;
    }

    public BigDecimal getDownLoseProfitRatio() {
        return downLoseProfitRatio;
    }

    public void setDownLoseProfitRatio(BigDecimal downLoseProfitRatio) {
        this.downLoseProfitRatio = downLoseProfitRatio;
    }

    public void setDownFluctuationRatio(BigDecimal downFluctuationRatio)
    {
        this.downFluctuationRatio = downFluctuationRatio;
    }

    public BigDecimal getDownFluctuationRatio() 
    {
        return downFluctuationRatio;
    }
    public void setMinBuyAmount(BigDecimal minBuyAmount) 
    {
        this.minBuyAmount = minBuyAmount;
    }

    public BigDecimal getMinBuyAmount() 
    {
        return minBuyAmount;
    }
    public void setMaxBuyAmount(BigDecimal maxBuyAmount) 
    {
        this.maxBuyAmount = maxBuyAmount;
    }

    public BigDecimal getMaxBuyAmount() 
    {
        return maxBuyAmount;
    }
    public void setMinUserAmount(BigDecimal minUserAmount) 
    {
        this.minUserAmount = minUserAmount;
    }

    public BigDecimal getMinUserAmount() 
    {
        return minUserAmount;
    }
    public void setProfitRatioMethod(Integer profitRatioMethod) 
    {
        this.profitRatioMethod = profitRatioMethod;
    }

    public Integer getProfitRatioMethod() 
    {
        return profitRatioMethod;
    }
    public void setLoseMoneyMethod(Integer loseMoneyMethod) 
    {
        this.loseMoneyMethod = loseMoneyMethod;
    }

    public Integer getLoseMoneyMethod() 
    {
        return loseMoneyMethod;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("productName", getProductName())
            .append("productCode", getProductCode())
            .append("productType", getProductType())
            .append("durationValue", getDurationValue())
            .append("durationLabel", getDurationLabel())
            .append("status", getStatus())
            .append("upWinProfitRatio", getUpWinProfitRatio())
            .append("upLoseProfitRatio", getUpLoseProfitRatio())
            .append("upFluctuationRatio", getUpFluctuationRatio())
            .append("downWinProfitRatio", getDownWinProfitRatio())
            .append("downLoseProfitRatio", getDownLoseProfitRatio())
            .append("downFluctuationRatio", getDownFluctuationRatio())
            .append("minBuyAmount", getMinBuyAmount())
            .append("maxBuyAmount", getMaxBuyAmount())
            .append("minUserAmount", getMinUserAmount())
            .append("profitRatioMethod", getProfitRatioMethod())
            .append("loseMoneyMethod", getLoseMoneyMethod())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }

    /**
     * cacheable缓存Key
     */
    @Override
    public String cacheableKey() {
        Map<String, Object> map = new HashMap<>();
        map.put("productCode",getProductCode());
        map.put("productType",getProductType());
        map.put("durationValue",getDurationValue());
        map.put("status",getStatus());
        map.put("pageSize",getPageNum());
        map.put("pageNum",getPageSize());
        return map.toString();
    }
}
