package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.entity.UserInfoDetailVo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户新股新币申购订单对象 user_apply_purchase_order
 * 
 * @author ruoyi
 * @date 2023-11-30
 */
public class UserApplyPurchaseOrder extends UserInfoDetailVo
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 订单编号 */
    @Excel(name = "订单编号")
    private String orderCode;

    /** 产品代码 */
    @Excel(name = "产品代码")
    private String productCode;

    /** 产品名称 */
    @Excel(name = "产品名称")
    private String productName;

    /** 产品名称-多语言 */
    @Excel(name = "产品名称-多语言")
    private LangMgr productNameLang;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    /** 申购时间 */
    @Excel(name = "申购时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date applyPurchaseTime;

    /** 申购金额 */
    @Excel(name = "申购金额")
    private BigDecimal applyPurchaseAmount;

    /** 申购数量 */
    @Excel(name = "申购数量")
    private Integer applyPurchaseQuantity;

    /** 申购单价 */
    @Excel(name = "申购单价")
    private BigDecimal applyPurchaseSingleAmount;

    /** 上市开始日期 */
    @Excel(name = "上市开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date listingStartDate;

    /** 中签率 */
    @Excel(name = "中签率")
    private BigDecimal winningRate;

    /** 中签金额 */
    @Excel(name = "中签金额")
    private BigDecimal winningAmount;

    /** 中签数量 */
    @Excel(name = "中签数量")
    private BigDecimal winningQuantity;

    /** 平仓价 */
    @Excel(name = "平仓价")
    private BigDecimal sellPrice;

    /** 平仓时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "平仓时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date sellTime;

    /** 盈亏 */
    @Excel(name = "盈亏")
    private BigDecimal profitAndLose;

    /** 新股新币申购配置的id */
    @Excel(name = "新股新币申购配置的id")
    private Long newProductApplyPurchaseId;

    /** 状态：0：申购中 1：已上市 */
    @Excel(name = "状态：0：申购中 1：已上市")
    private Integer status;

    /** 产品类型 1：股票 2：加密货币 */
    @Excel(name = "产品类型 1：股票 2：加密货币")
    private Integer productType;

    /** 是否锁仓 0：是 1：否 */
    @Excel(name = "是否锁仓 0：是 1：否")
    private Integer lockFlag;

    /** 锁仓解锁时间 */
    @Excel(name = "锁仓解锁时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date unLockTime;

    public Integer getLockFlag() {
        return lockFlag;
    }

    public void setLockFlag(Integer lockFlag) {
        this.lockFlag = lockFlag;
    }

    public Date getUnLockTime() {
        return unLockTime;
    }

    public void setUnLockTime(Date unLockTime) {
        this.unLockTime = unLockTime;
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

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setOrderCode(String orderCode) 
    {
        this.orderCode = orderCode;
    }

    public String getOrderCode() 
    {
        return orderCode;
    }
    public void setProductCode(String productCode) 
    {
        this.productCode = productCode;
    }

    public String getProductCode() 
    {
        return productCode;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setApplyPurchaseTime(Date applyPurchaseTime)
    {
        this.applyPurchaseTime = applyPurchaseTime;
    }

    public Date getApplyPurchaseTime()
    {
        return applyPurchaseTime;
    }
    public void setApplyPurchaseAmount(BigDecimal applyPurchaseAmount) 
    {
        this.applyPurchaseAmount = applyPurchaseAmount;
    }

    public BigDecimal getApplyPurchaseAmount() 
    {
        return applyPurchaseAmount;
    }
    public void setApplyPurchaseQuantity(Integer applyPurchaseQuantity) 
    {
        this.applyPurchaseQuantity = applyPurchaseQuantity;
    }

    public Integer getApplyPurchaseQuantity() 
    {
        return applyPurchaseQuantity;
    }
    public void setApplyPurchaseSingleAmount(BigDecimal applyPurchaseSingleAmount) 
    {
        this.applyPurchaseSingleAmount = applyPurchaseSingleAmount;
    }

    public BigDecimal getApplyPurchaseSingleAmount()
    {
        return applyPurchaseSingleAmount;
    }
    public void setListingStartDate(Date listingStartDate)
    {
        this.listingStartDate = listingStartDate;
    }

    public Date getListingStartDate()
    {
        return listingStartDate;
    }
    public void setWinningRate(BigDecimal winningRate) 
    {
        this.winningRate = winningRate;
    }

    public BigDecimal getWinningRate() 
    {
        return winningRate;
    }
    public void setWinningAmount(BigDecimal winningAmount) 
    {
        this.winningAmount = winningAmount;
    }

    public BigDecimal getWinningAmount() 
    {
        return winningAmount;
    }
    public void setWinningQuantity(BigDecimal winningQuantity) 
    {
        this.winningQuantity = winningQuantity;
    }

    public BigDecimal getWinningQuantity() 
    {
        return winningQuantity;
    }
    public void setSellPrice(BigDecimal sellPrice) 
    {
        this.sellPrice = sellPrice;
    }

    public BigDecimal getSellPrice() 
    {
        return sellPrice;
    }
    public void setSellTime(Date sellTime) 
    {
        this.sellTime = sellTime;
    }

    public Date getSellTime() 
    {
        return sellTime;
    }
    public void setProfitAndLose(BigDecimal profitAndLose) 
    {
        this.profitAndLose = profitAndLose;
    }

    public BigDecimal getProfitAndLose() 
    {
        return profitAndLose;
    }
    public void setNewProductApplyPurchaseId(Long newProductApplyPurchaseId) 
    {
        this.newProductApplyPurchaseId = newProductApplyPurchaseId;
    }

    public Long getNewProductApplyPurchaseId() 
    {
        return newProductApplyPurchaseId;
    }
    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
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
            .append("orderCode", getOrderCode())
            .append("productCode", getProductCode())
            .append("userId", getUserId())
            .append("applyPurchaseTime", getApplyPurchaseTime())
            .append("applyPurchaseAmount", getApplyPurchaseAmount())
            .append("applyPurchaseQuantity", getApplyPurchaseQuantity())
            .append("applyPurchaseSingleAmount", getApplyPurchaseSingleAmount())
            .append("listingStartDate", getListingStartDate())
            .append("winningRate", getWinningRate())
            .append("winningAmount", getWinningAmount())
            .append("winningQuantity", getWinningQuantity())
            .append("sellPrice", getSellPrice())
            .append("sellTime", getSellTime())
            .append("profitAndLose", getProfitAndLose())
            .append("newProductApplyPurchaseId", getNewProductApplyPurchaseId())
            .append("status", getStatus())
            .append("productType", getProductType())
            .toString();
    }
}
