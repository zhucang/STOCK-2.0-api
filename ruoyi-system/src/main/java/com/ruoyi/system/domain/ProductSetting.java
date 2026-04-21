package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 产品交易设置（产品风控）对象 product_setting
 * 
 * @author ruoyi
 * @date 2023-12-01
 */
public class ProductSetting extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 印花税率 */
    @Excel(name = "印花税率")
    private BigDecimal dutyFee;

    /** 留仓费率 */
    @Excel(name = "留仓费率")
    private BigDecimal stayFee;

    /** 最大留仓天数 */
    @Excel(name = "最大留仓天数")
    private Integer stayMaxDays;

    /** 最小购买金额 */
    @Excel(name = "最小购买金额")
    private BigDecimal buyMinAmt;

    /** 最小购买数量 */
    @Excel(name = "最小购买数量")
    private BigDecimal buyMinNum;

    /** 最大购买数量 */
    @Excel(name = "最大购买数量")
    private BigDecimal buyMaxNum;

    /** 最大买入比例 */
    @Excel(name = "最大买入比例")
    private BigDecimal buyMaxAmtPercent;

    /** 强制平仓比例 */
    @Excel(name = "强制平仓比例")
    private BigDecimal forceStopPercent;

    /** 快捷交易杠杆倍数,多个用/分割 */
    @Excel(name = "快捷交易杠杆倍数,多个用/分割")
    private String siteLever;

    /** 快捷交易手数 */
    @Excel(name = "快捷交易手数")
    private String handNum;

    /** N分钟内同一只股票不得下单M次（N） */
    @Excel(name = "N分钟内同一只股票不得下单M次", readConverterExp = "N=")
    private Integer buySameTimes;

    /** N分钟内同一只股票不得下单M次（M） */
    @Excel(name = "N分钟内同一只股票不得下单M次", readConverterExp = "M=")
    private Integer buySameNums;

    /** N分钟内交易手数不能超过M次（N） */
    @Excel(name = "N分钟内交易手数不能超过M次", readConverterExp = "N=")
    private Integer buyNumTimes;

    /** N分钟内交易手数不能超过M次（M） */
    @Excel(name = "N分钟内交易手数不能超过M次", readConverterExp = "M=")
    private Integer buyNumLots;

    /** 至少持有时间（分钟） */
    @Excel(name = "至少持有时间", readConverterExp = "分=钟")
    private Integer cantSellTimes;

    /** 一手对应的数量 */
    @Excel(name = "一手对应的数量")
    private Integer handCorrespondNum;

    /** 爆仓模式 1:保证金+账户余额亏完 2:保证金亏完 3:全亏损 */
    @Excel(name = "爆仓模式 1:保证金+账户余额亏完 2:保证金亏完 3:全亏损")
    private Integer liquidationMethod;

    /** 产品类型：1：股票 2：加密货币 3：期货 4：外汇 */
    @Excel(name = "产品类型：1：股票 2：加密货币 3：期货 4：外汇")
    private Integer productType;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setDutyFee(BigDecimal dutyFee) 
    {
        this.dutyFee = dutyFee;
    }

    public BigDecimal getDutyFee() 
    {
        return dutyFee;
    }
    public void setStayFee(BigDecimal stayFee) 
    {
        this.stayFee = stayFee;
    }

    public BigDecimal getStayFee() 
    {
        return stayFee;
    }
    public void setStayMaxDays(Integer stayMaxDays) 
    {
        this.stayMaxDays = stayMaxDays;
    }

    public Integer getStayMaxDays() 
    {
        return stayMaxDays;
    }
    public void setBuyMinAmt(BigDecimal buyMinAmt) 
    {
        this.buyMinAmt = buyMinAmt;
    }

    public BigDecimal getBuyMinAmt() 
    {
        return buyMinAmt;
    }
    public void setBuyMinNum(BigDecimal buyMinNum)
    {
        this.buyMinNum = buyMinNum;
    }

    public BigDecimal getBuyMinNum()
    {
        return buyMinNum;
    }
    public void setBuyMaxNum(BigDecimal buyMaxNum)
    {
        this.buyMaxNum = buyMaxNum;
    }

    public BigDecimal getBuyMaxNum()
    {
        return buyMaxNum;
    }
    public void setBuyMaxAmtPercent(BigDecimal buyMaxAmtPercent) 
    {
        this.buyMaxAmtPercent = buyMaxAmtPercent;
    }

    public BigDecimal getBuyMaxAmtPercent() 
    {
        return buyMaxAmtPercent;
    }
    public void setForceStopPercent(BigDecimal forceStopPercent) 
    {
        this.forceStopPercent = forceStopPercent;
    }

    public BigDecimal getForceStopPercent() 
    {
        return forceStopPercent;
    }
    public void setSiteLever(String siteLever) 
    {
        this.siteLever = siteLever;
    }

    public String getSiteLever() 
    {
        return siteLever;
    }
    public void setHandNum(String handNum) 
    {
        this.handNum = handNum;
    }

    public String getHandNum() 
    {
        return handNum;
    }
    public void setBuySameTimes(Integer buySameTimes) 
    {
        this.buySameTimes = buySameTimes;
    }

    public Integer getBuySameTimes() 
    {
        return buySameTimes;
    }
    public void setBuySameNums(Integer buySameNums) 
    {
        this.buySameNums = buySameNums;
    }

    public Integer getBuySameNums() 
    {
        return buySameNums;
    }
    public void setBuyNumTimes(Integer buyNumTimes) 
    {
        this.buyNumTimes = buyNumTimes;
    }

    public Integer getBuyNumTimes() 
    {
        return buyNumTimes;
    }
    public void setBuyNumLots(Integer buyNumLots) 
    {
        this.buyNumLots = buyNumLots;
    }

    public Integer getBuyNumLots() 
    {
        return buyNumLots;
    }
    public void setCantSellTimes(Integer cantSellTimes) 
    {
        this.cantSellTimes = cantSellTimes;
    }

    public Integer getCantSellTimes() 
    {
        return cantSellTimes;
    }
    public void setHandCorrespondNum(Integer handCorrespondNum) 
    {
        this.handCorrespondNum = handCorrespondNum;
    }

    public Integer getHandCorrespondNum() 
    {
        return handCorrespondNum;
    }
    public void setLiquidationMethod(Integer liquidationMethod) 
    {
        this.liquidationMethod = liquidationMethod;
    }

    public Integer getLiquidationMethod() 
    {
        return liquidationMethod;
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
            .append("dutyFee", getDutyFee())
            .append("stayFee", getStayFee())
            .append("stayMaxDays", getStayMaxDays())
            .append("buyMinAmt", getBuyMinAmt())
            .append("buyMinNum", getBuyMinNum())
            .append("buyMaxNum", getBuyMaxNum())
            .append("buyMaxAmtPercent", getBuyMaxAmtPercent())
            .append("forceStopPercent", getForceStopPercent())
            .append("siteLever", getSiteLever())
            .append("handNum", getHandNum())
            .append("buySameTimes", getBuySameTimes())
            .append("buySameNums", getBuySameNums())
            .append("buyNumTimes", getBuyNumTimes())
            .append("buyNumLots", getBuyNumLots())
            .append("cantSellTimes", getCantSellTimes())
            .append("handCorrespondNum", getHandCorrespondNum())
            .append("liquidationMethod", getLiquidationMethod())
            .append("productType", getProductType())
            .toString();
    }
}
