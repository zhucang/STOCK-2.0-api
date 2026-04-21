package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 质押产品配置对象 staking_product
 * 
 * @author ruoyi
 * @date 2025-07-17
 */
public class StakingProduct extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 质押名称 */
    @Excel(name = "质押名称")
    private String stakingName;

    /** 质押名称多语言 */
    @Excel(name = "质押名称多语言")
    private LangMgr stakingNameLang;

    /** 质押图标 */
    @Excel(name = "质押图标")
    private String stakingImg;

    /** 最低购买 */
    @Excel(name = "最低购买")
    private BigDecimal minPrice;

    /** 最高购买 */
    @Excel(name = "最高购买")
    private BigDecimal maxPrice;

    /** 质押天数 */
    @Excel(name = "质押天数")
    private Integer stakingTime;

    /** 浮动日收益最低 */
    @Excel(name = "浮动日收益最低")
    private BigDecimal floatDailyIncomeMinRate;

    /** 浮动日收益最高 */
    @Excel(name = "浮动日收益最高")
    private BigDecimal floatDailyIncomeMaxRate;

    /** 固定收益率 */
    @Excel(name = "固定收益率")
    private BigDecimal fixedIncomeRate;

    /** 状态：0：启用 1：禁用 */
    @Excel(name = "状态：0：启用 1：禁用")
    private Integer status;

    /** 排序 */
    @Excel(name = "排序")
    private Long sort;

    /** 违约金比率 */
    @Excel(name = "违约金比率")
    private BigDecimal breakContractRate;

    /** 用户余额最低要求 */
    @Excel(name = "用户余额最低要求")
    private BigDecimal userAmountLimit;

    /** 是否自动审核 0：是 1：否 */
    @Excel(name = "是否自动审核 0：是 1：否")
    private Integer autoApprove;

    /** 币种id */
    @Excel(name = "币种id")
    private Long currencyId;

    /** 币种名称 */
    @Excel(name = "币种名称")
    private String currencyName;

    /** vip等级最小限制 */
    @Excel(name = "vip等级最小限制")
    private Integer vipLevelLimit;

    /** 开放数量 */
    @Excel(name = "开放数量")
    private BigDecimal openQuantity;

    /** 剩余数量 */
    @Excel(name = "剩余数量")
    private BigDecimal remainingQuantity;

    /** sql版本 */
    @Excel(name = "sql版本")
    private Long sqlVersion;

    /** 删除标志 0：未删除 1：已删除 */
    @Excel(name = "删除标志 0：未删除 1：已删除")
    private Integer isDel;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setStakingName(String stakingName) 
    {
        this.stakingName = stakingName;
    }

    public String getStakingName() 
    {
        return stakingName;
    }

    public LangMgr getStakingNameLang() {
        return stakingNameLang;
    }

    public void setStakingNameLang(LangMgr stakingNameLang) {
        this.stakingNameLang = stakingNameLang;
    }

    public void setStakingImg(String stakingImg)
    {
        this.stakingImg = stakingImg;
    }

    public String getStakingImg() 
    {
        return stakingImg;
    }
    public void setMinPrice(BigDecimal minPrice) 
    {
        this.minPrice = minPrice;
    }

    public BigDecimal getMinPrice() 
    {
        return minPrice;
    }
    public void setMaxPrice(BigDecimal maxPrice) 
    {
        this.maxPrice = maxPrice;
    }

    public BigDecimal getMaxPrice() 
    {
        return maxPrice;
    }
    public void setStakingTime(Integer stakingTime) 
    {
        this.stakingTime = stakingTime;
    }

    public Integer getStakingTime() 
    {
        return stakingTime;
    }
    public void setFloatDailyIncomeMinRate(BigDecimal floatDailyIncomeMinRate) 
    {
        this.floatDailyIncomeMinRate = floatDailyIncomeMinRate;
    }

    public BigDecimal getFloatDailyIncomeMinRate() 
    {
        return floatDailyIncomeMinRate;
    }
    public void setFloatDailyIncomeMaxRate(BigDecimal floatDailyIncomeMaxRate) 
    {
        this.floatDailyIncomeMaxRate = floatDailyIncomeMaxRate;
    }

    public BigDecimal getFloatDailyIncomeMaxRate() 
    {
        return floatDailyIncomeMaxRate;
    }
    public void setFixedIncomeRate(BigDecimal fixedIncomeRate) 
    {
        this.fixedIncomeRate = fixedIncomeRate;
    }

    public BigDecimal getFixedIncomeRate() 
    {
        return fixedIncomeRate;
    }
    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }
    public void setSort(Long sort) 
    {
        this.sort = sort;
    }

    public Long getSort() 
    {
        return sort;
    }
    public void setBreakContractRate(BigDecimal breakContractRate) 
    {
        this.breakContractRate = breakContractRate;
    }

    public BigDecimal getBreakContractRate() 
    {
        return breakContractRate;
    }
    public void setUserAmountLimit(BigDecimal userAmountLimit) 
    {
        this.userAmountLimit = userAmountLimit;
    }

    public BigDecimal getUserAmountLimit() 
    {
        return userAmountLimit;
    }

    public Integer getAutoApprove() {
        return autoApprove;
    }

    public void setAutoApprove(Integer autoApprove) {
        this.autoApprove = autoApprove;
    }

    public void setCurrencyId(Long currencyId)
    {
        this.currencyId = currencyId;
    }

    public Long getCurrencyId() 
    {
        return currencyId;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public void setVipLevelLimit(Integer vipLevelLimit)
    {
        this.vipLevelLimit = vipLevelLimit;
    }

    public Integer getVipLevelLimit() 
    {
        return vipLevelLimit;
    }

    public BigDecimal getOpenQuantity() {
        return openQuantity;
    }

    public void setOpenQuantity(BigDecimal openQuantity) {
        this.openQuantity = openQuantity;
    }

    public BigDecimal getRemainingQuantity() {
        return remainingQuantity;
    }

    public void setRemainingQuantity(BigDecimal remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }

    public void setSqlVersion(Long sqlVersion)
    {
        this.sqlVersion = sqlVersion;
    }

    public Long getSqlVersion() 
    {
        return sqlVersion;
    }
    public void setIsDel(Integer isDel) 
    {
        this.isDel = isDel;
    }

    public Integer getIsDel() 
    {
        return isDel;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("stakingName", getStakingName())
            .append("stakingImg", getStakingImg())
            .append("minPrice", getMinPrice())
            .append("maxPrice", getMaxPrice())
            .append("stakingTime", getStakingTime())
            .append("floatDailyIncomeMinRate", getFloatDailyIncomeMinRate())
            .append("floatDailyIncomeMaxRate", getFloatDailyIncomeMaxRate())
            .append("fixedIncomeRate", getFixedIncomeRate())
            .append("status", getStatus())
            .append("sort", getSort())
            .append("breakContractRate", getBreakContractRate())
            .append("userAmountLimit", getUserAmountLimit())
            .append("currencyId", getCurrencyId())
            .append("vipLevelLimit", getVipLevelLimit())
            .append("sqlVersion", getSqlVersion())
            .append("isDel", getIsDel())
            .toString();
    }
}
