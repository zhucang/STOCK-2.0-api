package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.domain.ticker.TickerInfo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 用户产品自选关联信息对象 user_product_option
 * 
 * @author ruoyi
 * @date 2023-11-01
 */
public class UserProductOption extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    /** 产品id */
    @Excel(name = "产品id")
    private Long productId;

    /** 产品代码 */
    @Excel(name = "产品代码")
    private String productCode;

    /** 产品产品描述 */
    @Excel(name = "产品描述")
    private String productDesc;

    /** 产品名称 */
    @Excel(name = "产品名称")
    private String productName;

    /** 产品名称多语言 */
    @Excel(name = "产品名称多语言")
    private LangMgr productNameLang;

    /** 产品类型（1：美股 2：加密货币 3：期货 4：外汇） */
    @Excel(name = "产品类型", readConverterExp = "1=：美股,2=：加密货币,3=：期货,4=：外汇")
    private Integer productType;

    /** 行情信息*/
    private TickerInfo tickerInfo;


    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public TickerInfo getTickerInfo() {
        return tickerInfo;
    }

    public void setTickerInfo(TickerInfo tickerInfo) {
        this.tickerInfo = tickerInfo;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
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

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setProductCode(String productCode) 
    {
        this.productCode = productCode;
    }

    public String getProductCode() 
    {
        return productCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("productCode", getProductCode())
            .append("createTime", getCreateTime())
            .toString();
    }
}
