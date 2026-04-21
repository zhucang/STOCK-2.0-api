package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.ticker.TickerInfo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.math.BigDecimal;

/**
 * 期货产品信息对象 futures_product
 * 
 * @author ruoyi
 * @date 2023-11-01
 */
public class FuturesProduct extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 产品名称 */
    @Excel(name = "产品名称")
    private String productName;

    /** 产品名称-多语言 */
    @Excel(name = "产品名称-多语言")
    private LangMgr productNameLang;

    /** 产品代码 */
    @Excel(name = "产品代码")
    private String productCode;

    /** 产品类型（1：美股 2：加密货币 3:期货 4：外汇） */
    @Excel(name = "产品类型（1：美股 2：加密货币 3:期货 4：外汇）")
    private Integer productType;

    /** 产品图标*/
    @Excel(name = "产品图标")
    private String productImg;

    /** 是否锁定 0：否 1：是 */
    @Excel(name = "是否锁定 0：否 1：是")
    private Integer isLock;

    /** 是否显示 0：是 1：否 */
    @Excel(name = "是否显示 0：是 1：否")
    private Integer isShow;

    /** 是否自营币 0：否 1：是 */
    @Excel(name = "是否自营币 0：否 1：是")
    private Integer isSelfSell;

    /** 合约收益系数（1：N%） */
    @Excel(name = "合约收益系数（1：N%） ")
    private BigDecimal positionIncomeCoefficient;

    /** 排序 */
    @Excel(name = "排序")
    private Integer sort;

    /** 是否自选 0：否 1：是 */
    private Integer isOption;

    /** 行情信息*/
    private TickerInfo tickerInfo;

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public TickerInfo getTickerInfo() {
        return tickerInfo;
    }

    public void setTickerInfo(TickerInfo tickerInfo) {
        this.tickerInfo = tickerInfo;
    }

    public Integer getIsOption() {
        return isOption;
    }

    public void setIsOption(Integer isOption) {
        this.isOption = isOption;
    }
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
    public void setProductCode(String productCode) 
    {
        this.productCode = productCode;
    }

    public String getProductCode() 
    {
        return productCode;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    public void setIsLock(Integer isLock)
    {
        this.isLock = isLock;
    }

    public Integer getIsLock() 
    {
        return isLock;
    }
    public void setIsShow(Integer isShow) 
    {
        this.isShow = isShow;
    }

    public Integer getIsShow() 
    {
        return isShow;
    }
    public void setIsSelfSell(Integer isSelfSell) 
    {
        this.isSelfSell = isSelfSell;
    }

    public Integer getIsSelfSell() 
    {
        return isSelfSell;
    }

    public BigDecimal getPositionIncomeCoefficient() {
        return positionIncomeCoefficient;
    }

    public void setPositionIncomeCoefficient(BigDecimal positionIncomeCoefficient) {
        this.positionIncomeCoefficient = positionIncomeCoefficient;
    }

    public void setSort(Integer sort)
    {
        this.sort = sort;
    }

    public Integer getSort() 
    {
        return sort;
    }


    public LangMgr getProductNameLang() {
        return productNameLang;
    }

    public void setProductNameLang(LangMgr productNameLang) {
        this.productNameLang = productNameLang;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("productName", getProductName())
            .append("productCode", getProductCode())
            .append("isLock", getIsLock())
            .append("isShow", getIsShow())
            .append("createTime", getCreateTime())
            .append("isSelfSell", getIsSelfSell())
            .append("sort", getSort())
            .append("productNameLang", getProductNameLang())
            .toString();
    }
}
