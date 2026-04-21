package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 自营产品对象 self_sell_product
 * 
 * @author ruoyi
 * @date 2023-11-27
 */
public class SelfSellProduct extends BaseEntity
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

    /** 产品产品描述 */
    @Excel(name = "产品描述")
    private String productDesc;

    /** 产品图标 */
    @Excel(name = "产品图标")
    private String productImg;

    /** 初始价格 */
    @Excel(name = "初始价格")
    private BigDecimal initialPrice;

    /** 状态0：启用 1：禁用 */
    @Excel(name = "状态0：启用 1：禁用")
    private Integer status;

    /** 产品类型 1：股票 2：加密货币 */
    @Excel(name = "产品类型 1：股票 2：加密货币")
    private Integer productType;

    /** 是否直接上市 0：是 1：否 */
    @Excel(name = "是否直接上市 0：是 1：否")
    private Integer isDirectListing;

    /** 关联产品id */
    @Excel(name = "关联产品id")
    private Long relateProductId;

    /** 关联产品排序 */
    @Excel(name = "关联产品排序")
    private Integer relateProductSort;

    /** 首页推荐标志 0：不添加首页推荐 1：添加首页推荐 */
    @Excel(name = "首页推荐标志 0：不添加首页推荐 1：添加首页推荐")
    private Integer homeRecommendProductFlag;

    /** 首页推荐排序 */
    @Excel(name = "首页推荐排序")
    private Integer homeRecommendProductSort;

    public Integer getHomeRecommendProductSort() {
        return homeRecommendProductSort;
    }

    public void setHomeRecommendProductSort(Integer homeRecommendProductSort) {
        this.homeRecommendProductSort = homeRecommendProductSort;
    }

    public Integer getHomeRecommendProductFlag() {
        return homeRecommendProductFlag;
    }

    public void setHomeRecommendProductFlag(Integer homeRecommendProductFlag) {
        this.homeRecommendProductFlag = homeRecommendProductFlag;
    }

    public Integer getRelateProductSort() {
        return relateProductSort;
    }

    public void setRelateProductSort(Integer relateProductSort) {
        this.relateProductSort = relateProductSort;
    }

    public BigDecimal getInitialPrice() {
        return initialPrice;
    }

    public void setInitialPrice(BigDecimal initialPrice) {
        this.initialPrice = initialPrice;
    }

    public Long getRelateProductId() {
        return relateProductId;
    }

    public void setRelateProductId(Long relateProductId) {
        this.relateProductId = relateProductId;
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

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }

    public LangMgr getProductNameLang() {
        return productNameLang;
    }

    public void setProductNameLang(LangMgr productNameLang) {
        this.productNameLang = productNameLang;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    public Integer getIsDirectListing() {
        return isDirectListing;
    }

    public void setIsDirectListing(Integer isDirectListing) {
        this.isDirectListing = isDirectListing;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("productName", getProductName())
            .append("productCode", getProductCode())
            .append("status", getStatus())
            .append("productType", getProductType())
            .toString();
    }
}
