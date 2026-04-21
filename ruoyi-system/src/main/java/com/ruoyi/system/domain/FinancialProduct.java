package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 理财产品配置对象 financial_product
 * 
 * @author ruoyi
 * @date 2023-11-26
 */
public class FinancialProduct extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 理财名称 */
    @Excel(name = "理财名称")
    private String financialName;

    /** 理财名称多语言 */
    @Excel(name = "理财名称多语言")
    private LangMgr financialNameLang;

    /** 理财图标 */
    @Excel(name = "理财图标")
    private String financialImg;

    /** 最低购买 */
    @Excel(name = "最低购买")
    private BigDecimal minPrice;

    /** 最高购买 */
    @Excel(name = "最高购买")
    private BigDecimal maxPrice;

    /** 理财天数 */
    @Excel(name = "理财天数")
    private Integer financialTime;

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
    private Integer sort;

    /** 违约金比率 */
    @Excel(name = "违约金比率")
    private BigDecimal breakContractRate;

    /** 用户余额最低要求 */
    @Excel(name = "用户余额最低要求")
    private BigDecimal userAmountLimit;

    /** 是否自动续约 0：是 1：否 */
    @Excel(name = "是否自动续约 0：是 1：否")
    private Integer renewVisible;

    /** 是否自动审核 0：是 1：否 */
    @Excel(name = "是否自动审核 0：是 1：否")
    private Integer autoApprove;

    /** 币种id */
    @Excel(name = "币种id")
    private Long currencyId;

    /** 币种名称 */
    @Excel(name = "币种名称")
    private String currencyName;

    /** 用户限购数量 */
    @Excel(name = "用户限购数量")
    private Integer userBuyLimit;

    /** vip等级最小限制 */
    @Excel(name = "vip等级最小限制")
    private Integer vipLevelLimit;

    /** 开放数量 */
    @Excel(name = "开放数量")
    private BigDecimal openQuantity;

    /** 剩余数量 */
    @Excel(name = "剩余数量")
    private BigDecimal remainingQuantity;

    /** 结算方式 0：日结 1：到期结 */
    @Excel(name = "结算方式 0：日结 1：到期结")
    private Integer settleMethod;

    public Integer getSettleMethod() {
        return settleMethod;
    }

    public void setSettleMethod(Integer settleMethod) {
        this.settleMethod = settleMethod;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setFinancialName(String financialName) 
    {
        this.financialName = financialName;
    }

    public String getFinancialName() 
    {
        return financialName;
    }
    public void setFinancialImg(String financialImg) 
    {
        this.financialImg = financialImg;
    }

    public String getFinancialImg() 
    {
        return financialImg;
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
    public void setFinancialTime(Integer financialTime) 
    {
        this.financialTime = financialTime;
    }

    public Integer getFinancialTime() 
    {
        return financialTime;
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
    public void setSort(Integer sort) 
    {
        this.sort = sort;
    }

    public Integer getSort() 
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
    public void setRenewVisible(Integer renewVisible) 
    {
        this.renewVisible = renewVisible;
    }

    public Integer getRenewVisible() 
    {
        return renewVisible;
    }
    public void setAutoApprove(Integer autoApprove) 
    {
        this.autoApprove = autoApprove;
    }

    public Integer getAutoApprove() 
    {
        return autoApprove;
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

    public Integer getUserBuyLimit() {
        return userBuyLimit;
    }

    public void setUserBuyLimit(Integer userBuyLimit) {
        this.userBuyLimit = userBuyLimit;
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

    public LangMgr getFinancialNameLang() {
        return financialNameLang;
    }

    public void setFinancialNameLang(LangMgr financialNameLang) {
        this.financialNameLang = financialNameLang;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("financialName", getFinancialName())
            .append("financialImg", getFinancialImg())
            .append("minPrice", getMinPrice())
            .append("maxPrice", getMaxPrice())
            .append("financialTime", getFinancialTime())
            .append("floatDailyIncomeMinRate", getFloatDailyIncomeMinRate())
            .append("floatDailyIncomeMaxRate", getFloatDailyIncomeMaxRate())
            .append("fixedIncomeRate", getFixedIncomeRate())
            .append("status", getStatus())
            .append("sort", getSort())
            .append("breakContractRate", getBreakContractRate())
            .append("userAmountLimit", getUserAmountLimit())
            .append("renewVisible", getRenewVisible())
            .append("autoApprove", getAutoApprove())
            .append("currencyId", getCurrencyId())
            .append("financialNameLang", getFinancialNameLang())
            .append("vipLevelLimit", getVipLevelLimit())
            .toString();
    }
}
