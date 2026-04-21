package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.ticker.TickerInfo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 首页推荐产品对象 home_recommend_products
 * 
 * @author ruoyi
 * @date 2024-01-09
 */
public class HomeRecommendProducts extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 产品id */
    @Excel(name = "产品id")
    private Long productId;

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

    /** 产品图标*/
    @Excel(name = "产品图标")
    private String productImg;

    /** 产品类型（1：美股 2：加密货币 3:期货 4：外汇） */
    @Excel(name = "产品类型", readConverterExp = "1=：美股,2=：加密货币,3=:期货,4=：外汇")
    private Integer productType;

    /** 是否展示 0：是 1：否 */
    @Excel(name = "是否展示 0：是 1：否")
    private Integer isVisible;

    /** 排序 */
    @Excel(name = "排序")
    private Integer sort;

    /** 行情信息*/
    private TickerInfo tickerInfo;

    public TickerInfo getTickerInfo() {
        return tickerInfo;
    }

    public void setTickerInfo(TickerInfo tickerInfo) {
        this.tickerInfo = tickerInfo;
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
    public void setProductId(Long productId) 
    {
        this.productId = productId;
    }

    public Long getProductId() 
    {
        return productId;
    }
    public void setProductType(Integer productType) 
    {
        this.productType = productType;
    }

    public Integer getProductType() 
    {
        return productType;
    }
    public void setIsVisible(Integer isVisible) 
    {
        this.isVisible = isVisible;
    }

    public Integer getIsVisible() 
    {
        return isVisible;
    }
    public void setSort(Integer sort) 
    {
        this.sort = sort;
    }

    public Integer getSort() 
    {
        return sort;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("productId", getProductId())
            .append("productType", getProductType())
            .append("isVisible", getIsVisible())
            .append("sort", getSort())
            .toString();
    }
}
