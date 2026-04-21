package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 平台交易币种配置信息对象 platform_currency
 * 
 * @author ruoyi
 * @date 2023-10-28
 */
public class PlatformCurrency extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 币种名称 */
    @Excel(name = "币种名称")
    private String currencyName;

    /** 币种名称多语言 */
    @Excel(name = "币种名称多语言")
    private LangMgr currencyNameLang;

    /** 币种描述 */
    @Excel(name = "币种描述")
    private String currencyDesc;

    /** 币种描述多语言 */
    @Excel(name = "币种描述多语言")
    private LangMgr currencyDescLang;

    /** 币种图片 */
    @Excel(name = "币种图片")
    private String currencyImg;

    /** 币种类型 0：法币 1：虚拟币 */
    @Excel(name = "币种类型 0：法币 1：虚拟币")
    private Integer currencyType;

    /** 资金互转金额最小限制 */
    @Excel(name = "资金互转金额最小限制")
    private BigDecimal balanceConvertMinLimit;

    /** 资金互转金额最大限制 */
    @Excel(name = "资金互转金额最大限制")
    private BigDecimal balanceConvertMaxLimit;

    /** 资金兑换手续费率 */
    @Excel(name = "资金兑换手续费率")
    private BigDecimal exchangeHandlingFeeRate;

    /** 实时汇率标志 0：固定汇率 1：实时汇率 */
    @Excel(name = "实时汇率标志 0：固定汇率 1：实时汇率")
    private Integer realTimeExchangeRateFlag;

    /** 固定汇率 */
    @Excel(name = "固定汇率")
    private BigDecimal fixedExchangeRate;

    /** 实时汇率品种 */
    @Excel(name = "实时汇率品种")
    private String realTimeExchangeRateProduct;

    /** 状态：0：启用 1：禁用 */
    @Excel(name = "状态：0：启用 1：禁用")
    private Integer status;

    /** 排序 */
    @Excel(name = "排序")
    private Integer sort;

    /** 是否两融资产 0：是   1：不是 */
    @Excel(name = "是否两融资产 0：是   1：不是")
    private Integer isPlatformCurrency;

    /** 是否用于极速交易 0：是 1：否 */
    @Excel(name = "是否用于极速交易 0：是 1：否")
    private String useInFastTrade;

    /** 钱包地址类型 */
    @Excel(name = "钱包地址类型")
    private String walletAddressType;

    /** 保留小数位数 */
    @Excel(name = "保留小数位数")
    private Integer savaScale;

    public Integer getSavaScale() {
        return savaScale;
    }

    public void setSavaScale(Integer savaScale) {
        this.savaScale = savaScale;
    }

    public String getWalletAddressType() {
        return walletAddressType;
    }

    public void setWalletAddressType(String walletAddressType) {
        this.walletAddressType = walletAddressType;
    }

    public Integer getIsPlatformCurrency() {
        return isPlatformCurrency;
    }

    public void setIsPlatformCurrency(Integer isPlatformCurrency) {
        this.isPlatformCurrency = isPlatformCurrency;
    }

    public String getCurrencyDesc() {
        return currencyDesc;
    }

    public void setCurrencyDesc(String currencyDesc) {
        this.currencyDesc = currencyDesc;
    }

    public LangMgr getCurrencyDescLang() {
        if (currencyDescLang == null){
            return new LangMgr();
        }
        return currencyDescLang;
    }

    public String getUseInFastTrade() {
        return useInFastTrade;
    }

    public void setUseInFastTrade(String useInFastTrade) {
        this.useInFastTrade = useInFastTrade;
    }

    public void setCurrencyDescLang(LangMgr currencyDescLang) {
        this.currencyDescLang = currencyDescLang;
    }

    public LangMgr getCurrencyNameLang() {
        if (currencyNameLang == null){
            return new LangMgr();
        }
        return currencyNameLang;
    }

    public void setCurrencyNameLang(LangMgr currencyNameLang) {
        this.currencyNameLang = currencyNameLang;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setCurrencyName(String currencyName) 
    {
        this.currencyName = currencyName;
    }

    public String getCurrencyName() 
    {
        return currencyName;
    }

    public String getCurrencyImg() {
        return currencyImg;
    }

    public void setCurrencyImg(String currencyImg) {
        this.currencyImg = currencyImg;
    }

    public Integer getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(Integer currencyType) {
        this.currencyType = currencyType;
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

    public BigDecimal getBalanceConvertMinLimit() {
        return balanceConvertMinLimit;
    }

    public void setBalanceConvertMinLimit(BigDecimal balanceConvertMinLimit) {
        this.balanceConvertMinLimit = balanceConvertMinLimit;
    }

    public BigDecimal getBalanceConvertMaxLimit() {
        return balanceConvertMaxLimit;
    }

    public void setBalanceConvertMaxLimit(BigDecimal balanceConvertMaxLimit) {
        this.balanceConvertMaxLimit = balanceConvertMaxLimit;
    }

    public BigDecimal getExchangeHandlingFeeRate() {
        return exchangeHandlingFeeRate;
    }

    public void setExchangeHandlingFeeRate(BigDecimal exchangeHandlingFeeRate) {
        this.exchangeHandlingFeeRate = exchangeHandlingFeeRate;
    }

    public Integer getRealTimeExchangeRateFlag() {
        return realTimeExchangeRateFlag;
    }

    public void setRealTimeExchangeRateFlag(Integer realTimeExchangeRateFlag) {
        this.realTimeExchangeRateFlag = realTimeExchangeRateFlag;
    }

    public BigDecimal getFixedExchangeRate() {
        return fixedExchangeRate;
    }

    public void setFixedExchangeRate(BigDecimal fixedExchangeRate) {
        this.fixedExchangeRate = fixedExchangeRate;
    }

    public String getRealTimeExchangeRateProduct() {
        return realTimeExchangeRateProduct;
    }

    public void setRealTimeExchangeRateProduct(String realTimeExchangeRateProduct) {
        this.realTimeExchangeRateProduct = realTimeExchangeRateProduct;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("currencyName", getCurrencyName())
            .append("currencyType", getCurrencyType())
            .append("status", getStatus())
            .append("sort", getSort())
            .toString();
    }

}
