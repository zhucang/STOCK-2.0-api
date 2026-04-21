package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.entity.UserInfoDetailVo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 用户币币资产对象 user_bibi_assets
 * 
 * @author ruoyi
 * @date 2024-06-24
 */
public class UserBibiAssets extends UserInfoDetailVo
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    /** 产品代码 */
    @Excel(name = "产品代码")
    private String productCode;

    /** 产品名称 */
    @Excel(name = "产品代码")
    private String productName;

    /** 产品类型：1：美股 2：加密货币 3:期货 4：外汇 */
    @Excel(name = "产品类型：1：美股 2：加密货币 3:期货 4：外汇")
    private Integer productType;

    /** 持有数量 */
    @Excel(name = "持有数量")
    private BigDecimal bibiAmount;

    /** 冻结数量 */
    @Excel(name = "冻结数量")
    private BigDecimal bibiFrozenAmount;

    /** 买入总金额 */
    @Excel(name = "买入总金额")
    private BigDecimal buyAmountAll;

    /** 卖出总金额 */
    @Excel(name = "卖出总金额")
    private BigDecimal sellAmountAll;

    /** 买入与卖出金额的差值 */
    @Excel(name = "买入与卖出金额的差值")
    private BigDecimal buyAndSellAmountDifference;

    /** sql版本 */
    @Excel(name = "sql版本")
    private Long sqlVersion;

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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductType(Integer productType)
    {
        this.productType = productType;
    }

    public Integer getProductType() 
    {
        return productType;
    }
    public void setBibiAmount(BigDecimal bibiAmount) 
    {
        this.bibiAmount = bibiAmount;
    }

    public BigDecimal getBibiAmount() 
    {
        return bibiAmount;
    }

    public BigDecimal getBibiFrozenAmount() {
        return bibiFrozenAmount;
    }

    public void setBibiFrozenAmount(BigDecimal bibiFrozenAmount) {
        this.bibiFrozenAmount = bibiFrozenAmount;
    }

    public void setBuyAmountAll(BigDecimal buyAmountAll)
    {
        this.buyAmountAll = buyAmountAll;
    }

    public BigDecimal getBuyAmountAll() 
    {
        return buyAmountAll;
    }
    public void setSellAmountAll(BigDecimal sellAmountAll) 
    {
        this.sellAmountAll = sellAmountAll;
    }

    public BigDecimal getSellAmountAll() 
    {
        return sellAmountAll;
    }
    public void setBuyAndSellAmountDifference(BigDecimal buyAndSellAmountDifference) 
    {
        this.buyAndSellAmountDifference = buyAndSellAmountDifference;
    }

    public BigDecimal getBuyAndSellAmountDifference() 
    {
        return buyAndSellAmountDifference;
    }
    public void setSqlVersion(Long sqlVersion) 
    {
        this.sqlVersion = sqlVersion;
    }

    public Long getSqlVersion() 
    {
        return sqlVersion;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("productCode", getProductCode())
            .append("productType", getProductType())
            .append("bibiAmount", getBibiAmount())
            .append("buyAmountAll", getBuyAmountAll())
            .append("sellAmountAll", getSellAmountAll())
            .append("buyAndSellAmountDifference", getBuyAndSellAmountDifference())
            .append("createTime", getCreateTime())
            .append("sqlVersion", getSqlVersion())
            .toString();
    }
}
