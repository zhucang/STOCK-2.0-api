package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 贷款产品配置对象 loan_product
 * 
 * @author ruoyi
 * @date 2024-05-21
 */
public class LoanProduct extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 产品名称 */
    @Excel(name = "产品名称")
    private String productName;

    /** 产品名称多语言 */
    @Excel(name = "产品名称多语言")
    private LangMgr productNameLang;

    /** 产品图标 */
    @Excel(name = "产品图标")
    private String productImg;

    /** 最低金额 */
    @Excel(name = "最低金额")
    private BigDecimal minPrice;

    /** 最高金额 */
    @Excel(name = "最高金额")
    private BigDecimal maxPrice;

    /** 借贷天数 */
    @Excel(name = "借贷天数")
    private Integer loanDays;

    /** 免息天数 */
    @Excel(name = "免息天数")
    private Integer interestFreeDays;

    /** 贷款每日利息率 */
    @Excel(name = "贷款每日利息率")
    private BigDecimal loanDailyRate;

    /** 违约每日利息率 */
    @Excel(name = "违约每日利息率")
    private BigDecimal breakContractDailyRate;

    /** 最大贷款次数 */
    @Excel(name = "最大贷款次数")
    private Integer maxLoanCount;

    /** 利息结算方式 0：每日余额扣除利息 1：到期还款结清总利息 */
    @Excel(name = "利息结算方式 0：每日余额扣除利息 1：到期还款结清总利息")
    private Integer interestSettlementMethod;

    /** 还款方式 0：联系客服 1：提交还款订单 */
    @Excel(name = "还款方式 0：联系客服 1：提交还款订单")
    private Integer repaymentMethod;

    /** 状态：0：启用 1：禁用 */
    @Excel(name = "状态：0：启用 1：禁用")
    private Integer status;

    /** 排序 */
    @Excel(name = "排序")
    private Integer sort;

    /** 币种id */
    @Excel(name = "币种id")
    private Long currencyId;

    /** 币种名称 */
    @Excel(name = "币种名称")
    private String currencyName;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setProductName(String productName) 
    {
        this.productName = productName;
    }

    public String getProductName() 
    {
        return productName;
    }

    public LangMgr getProductNameLang() {
        return productNameLang;
    }

    public void setProductNameLang(LangMgr productNameLang) {
        this.productNameLang = productNameLang;
    }

    public void setProductImg(String productImg)
    {
        this.productImg = productImg;
    }

    public String getProductImg() 
    {
        return productImg;
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
    public void setLoanDailyRate(BigDecimal loanDailyRate) 
    {
        this.loanDailyRate = loanDailyRate;
    }

    public BigDecimal getLoanDailyRate() 
    {
        return loanDailyRate;
    }
    public void setBreakContractDailyRate(BigDecimal breakContractDailyRate) 
    {
        this.breakContractDailyRate = breakContractDailyRate;
    }

    public BigDecimal getBreakContractDailyRate() 
    {
        return breakContractDailyRate;
    }

    public Integer getMaxLoanCount() {
        return maxLoanCount;
    }

    public void setMaxLoanCount(Integer maxLoanCount) {
        this.maxLoanCount = maxLoanCount;
    }

    public Integer getInterestSettlementMethod() {
        return interestSettlementMethod;
    }

    public void setInterestSettlementMethod(Integer interestSettlementMethod) {
        this.interestSettlementMethod = interestSettlementMethod;
    }

    public Integer getRepaymentMethod() {
        return repaymentMethod;
    }

    public void setRepaymentMethod(Integer repaymentMethod) {
        this.repaymentMethod = repaymentMethod;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }

    public void setLoanDays(Integer loanDays) {
        this.loanDays = loanDays;
    }

    public void setInterestFreeDays(Integer interestFreeDays) {
        this.interestFreeDays = interestFreeDays;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
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

    public Integer getLoanDays() {
        return loanDays;
    }

    public Integer getInterestFreeDays() {
        return interestFreeDays;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("productName", getProductName())
            .append("productImg", getProductImg())
            .append("minPrice", getMinPrice())
            .append("maxPrice", getMaxPrice())
            .append("loanDays", getLoanDays())
            .append("interestFreeDays", getInterestFreeDays())
            .append("loanDailyRate", getLoanDailyRate())
            .append("breakContractDailyRate", getBreakContractDailyRate())
            .append("status", getStatus())
            .append("sort", getSort())
            .append("currencyId", getCurrencyId())
            .append("createTime", getCreateTime())
            .toString();
    }
}
