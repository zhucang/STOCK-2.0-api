package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 新股新币申购初始配置对象 new_product_apply_purchase
 * 
 * @author ruoyi
 * @date 2023-11-30
 */
public class NewProductApplyPurchase extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 自营产品id */
    @Excel(name = "自营产品id")
    private Long selfSellProductId;

    /** 产品名称 */
    @Excel(name = "产品名称")
    private String productName;

    /** 产品名称-多语言 */
    @Excel(name = "产品名称-多语言")
    private LangMgr productNameLang;

    /** 产品代码 */
    @Excel(name = "产品代码")
    private String productCode;

    /** 上市价格 */
    @Excel(name = "上市价格")
    private BigDecimal listingPrice;

    /** 上市数量 */
    @Excel(name = "上市数量")
    private Integer listingQuantity;

    /** 上市当天涨幅率 */
    @Excel(name = "上市当天涨幅率")
    private BigDecimal listingDayIncreaseRate;

    /** 用户余额最低要求 */
    @Excel(name = "用户余额最低要求")
    private BigDecimal userAmountLimit;

    /** 交易日期 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "交易日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date tradeDate;

    /** 状态0：启用 1：禁用 */
    @Excel(name = "状态0：启用 1：禁用")
    private Integer status;

    /** 排序 */
    @Excel(name = "排序")
    private Integer sort;

    /** 申购开始日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "申购开始日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date applyPurchaseStartDate;

    /** 申购结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "申购结束日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date applyPurchaseEndDate;

    /** 剩余数量 */
    @Excel(name = "剩余数量")
    private Integer remainingQuantity;

    /** 上市开始日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "上市开始日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date listingStartDate;

    /** 上市结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "上市结束日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date listingEndDate;

    /** 上市后每天涨幅率 */
    @Excel(name = "上市后每天涨幅率")
    private BigDecimal listedDayIncreaseRate;

    /** 单笔申购数量最低 */
    @Excel(name = "单笔申购数量最低")
    private BigDecimal applyPurchaseMin;

    /** 单笔申购数量最高 */
    @Excel(name = "单笔申购数量最高")
    private BigDecimal applyPurchaseMax;

    /** 锁仓期限 */
    @Excel(name = "锁仓期限")
    private Integer lockupPeriod;

    /** 上市状态：0：预申购 1：正在申购 2：已上市 */
    @Excel(name = "上市状态：0：预申购 1：正在申购 2：已上市")
    private Integer listingStatus;

    /** 股票类型 1：股票 2：加密货币 */
    @Excel(name = "产品类型 1：股票 2：加密货币")
    private Integer productType;

    /** 币种id */
    private Long currencyId;

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public LangMgr getProductNameLang() {
        return productNameLang;
    }

    public void setProductNameLang(LangMgr productNameLang) {
        this.productNameLang = productNameLang;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setSelfSellProductId(Long selfSellProductId) 
    {
        this.selfSellProductId = selfSellProductId;
    }

    public Long getSelfSellProductId() 
    {
        return selfSellProductId;
    }
    public void setListingPrice(BigDecimal listingPrice) 
    {
        this.listingPrice = listingPrice;
    }

    public BigDecimal getListingPrice() 
    {
        return listingPrice;
    }
    public void setListingQuantity(Integer listingQuantity) 
    {
        this.listingQuantity = listingQuantity;
    }

    public Integer getListingQuantity() 
    {
        return listingQuantity;
    }
    public void setListingDayIncreaseRate(BigDecimal listingDayIncreaseRate) 
    {
        this.listingDayIncreaseRate = listingDayIncreaseRate;
    }

    public BigDecimal getListingDayIncreaseRate() 
    {
        return listingDayIncreaseRate;
    }
    public void setUserAmountLimit(BigDecimal userAmountLimit) 
    {
        this.userAmountLimit = userAmountLimit;
    }

    public BigDecimal getUserAmountLimit() 
    {
        return userAmountLimit;
    }
    public void setTradeDate(Date tradeDate) 
    {
        this.tradeDate = tradeDate;
    }

    public Date getTradeDate() 
    {
        return tradeDate;
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
    public void setApplyPurchaseStartDate(Date applyPurchaseStartDate) 
    {
        this.applyPurchaseStartDate = applyPurchaseStartDate;
    }

    public Date getApplyPurchaseStartDate() 
    {
        return applyPurchaseStartDate;
    }
    public void setApplyPurchaseEndDate(Date applyPurchaseEndDate) 
    {
        this.applyPurchaseEndDate = applyPurchaseEndDate;
    }

    public Date getApplyPurchaseEndDate() 
    {
        return applyPurchaseEndDate;
    }

    public Integer getRemainingQuantity() {
        return remainingQuantity;
    }

    public void setRemainingQuantity(Integer remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }

    public void setListingStartDate(Date listingStartDate)
    {
        this.listingStartDate = listingStartDate;
    }

    public Date getListingStartDate() 
    {
        return listingStartDate;
    }
    public void setListingEndDate(Date listingEndDate) 
    {
        this.listingEndDate = listingEndDate;
    }

    public Date getListingEndDate() 
    {
        return listingEndDate;
    }
    public void setListedDayIncreaseRate(BigDecimal listedDayIncreaseRate) 
    {
        this.listedDayIncreaseRate = listedDayIncreaseRate;
    }

    public BigDecimal getListedDayIncreaseRate() 
    {
        return listedDayIncreaseRate;
    }
    public void setApplyPurchaseMin(BigDecimal applyPurchaseMin) 
    {
        this.applyPurchaseMin = applyPurchaseMin;
    }

    public BigDecimal getApplyPurchaseMin() 
    {
        return applyPurchaseMin;
    }
    public void setApplyPurchaseMax(BigDecimal applyPurchaseMax) 
    {
        this.applyPurchaseMax = applyPurchaseMax;
    }

    public BigDecimal getApplyPurchaseMax() 
    {
        return applyPurchaseMax;
    }
    public void setLockupPeriod(Integer lockupPeriod) 
    {
        this.lockupPeriod = lockupPeriod;
    }

    public Integer getLockupPeriod() 
    {
        return lockupPeriod;
    }
    public void setListingStatus(Integer listingStatus) 
    {
        this.listingStatus = listingStatus;
    }

    public Integer getListingStatus() 
    {
        return listingStatus;
    }
    public void setProductType(Integer productType) 
    {
        this.productType = productType;
    }

    public Integer getProductType() 
    {
        return productType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("selfSellProductId", getSelfSellProductId())
            .append("listingPrice", getListingPrice())
            .append("listingQuantity", getListingQuantity())
            .append("listingDayIncreaseRate", getListingDayIncreaseRate())
            .append("userAmountLimit", getUserAmountLimit())
            .append("tradeDate", getTradeDate())
            .append("status", getStatus())
            .append("sort", getSort())
            .append("applyPurchaseStartDate", getApplyPurchaseStartDate())
            .append("applyPurchaseEndDate", getApplyPurchaseEndDate())
            .append("remainingQuantity", getRemainingQuantity())
            .append("listingStartDate", getListingStartDate())
            .append("listingEndDate", getListingEndDate())
            .append("listedDayIncreaseRate", getListedDayIncreaseRate())
            .append("applyPurchaseMin", getApplyPurchaseMin())
            .append("applyPurchaseMax", getApplyPurchaseMax())
            .append("lockupPeriod", getLockupPeriod())
            .append("listingStatus", getListingStatus())
            .append("productType", getProductType())
            .toString();
    }
}
