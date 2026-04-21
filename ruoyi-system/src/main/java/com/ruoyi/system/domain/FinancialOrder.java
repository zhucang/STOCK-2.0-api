package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.entity.UserInfoDetailVo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 理财订单对象 financial_order
 * 
 * @author ruoyi
 * @date 2023-11-26
 */
public class FinancialOrder extends UserInfoDetailVo
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 订单号 */
    @Excel(name = "订单号")
    private String orderCode;

    /** 理财名称 */
    @Excel(name = "理财名称")
    private String financialName;

    /** 理财名称多语言 */
    @Excel(name = "理财名称多语言")
    private LangMgr financialNameLang;

    /** 理财图标 */
    @Excel(name = "理财图标")
    private String financialImg;

    /** 理财产品id */
    @Excel(name = "理财产品id")
    private Long financialProductId;

    /** 理财天数 */
    @Excel(name = "理财天数")
    private Integer financialTime;

    /** 理财购买金额 */
    @Excel(name = "理财购买金额")
    private BigDecimal buyPrice;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    /** 理财开始日期 */
    @Excel(name = "理财开始日期")
    private String startDate;

    /** 理财结束日期 */
    @Excel(name = "理财结束日期")
    private String endDate;

    /** 最后派息时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "最后派息时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastInterestTime;

    /** 已经派息次数 */
    @Excel(name = "已经派息次数")
    private Integer alreadyInterestCount;

    /** 违约金比率 */
    @Excel(name = "违约金比率")
    private BigDecimal breakContractRate;

    /** 日收益率 */
    @Excel(name = "日收益率")
    private BigDecimal dailyIncomeRate;

    /** 浮动日收益最低 */
    @Excel(name = "浮动日收益最低")
    private BigDecimal floatDailyIncomeMinRate;

    /** 浮动日收益最高 */
    @Excel(name = "浮动日收益最高")
    private BigDecimal floatDailyIncomeMaxRate;

    /** 订单状态：0：待审核  1：进行中 2：已完成 3：已驳回 */
    @Excel(name = "订单状态：0：待审核  1：进行中 2：已完成 3：已驳回 4:已赎回")
    private Integer orderStatus;

    /** 是否自动续约 0：是 1：否 */
    @Excel(name = "是否自动续约 0：是 1：否")
    private Integer autoRenew;

    /** 货币id */
    @Excel(name = "货币id")
    private Long currencyId;

    /** 币种名称 */
    @Excel(name = "币种名称")
    private String currencyName;

    /** 结算方式 0：日结 1：到期结 */
    @Excel(name = "结算方式 0：日结 1：到期结")
    private Integer settleMethod;

    public Integer getSettleMethod() {
        return settleMethod;
    }

    public void setSettleMethod(Integer settleMethod) {
        this.settleMethod = settleMethod;
    }

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setFinancialProductId(Long financialProductId) 
    {
        this.financialProductId = financialProductId;
    }

    public Long getFinancialProductId() 
    {
        return financialProductId;
    }
    public void setFinancialTime(Integer financialTime) 
    {
        this.financialTime = financialTime;
    }

    public Integer getFinancialTime() 
    {
        return financialTime;
    }
    public void setBuyPrice(BigDecimal buyPrice) 
    {
        this.buyPrice = buyPrice;
    }

    public BigDecimal getBuyPrice() 
    {
        return buyPrice;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setStartDate(String startDate) 
    {
        this.startDate = startDate;
    }

    public String getStartDate() 
    {
        return startDate;
    }
    public void setEndDate(String endDate) 
    {
        this.endDate = endDate;
    }

    public String getEndDate() 
    {
        return endDate;
    }
    public void setLastInterestTime(Date lastInterestTime) 
    {
        this.lastInterestTime = lastInterestTime;
    }

    public Date getLastInterestTime() 
    {
        return lastInterestTime;
    }
    public void setAlreadyInterestCount(Integer alreadyInterestCount) 
    {
        this.alreadyInterestCount = alreadyInterestCount;
    }

    public Integer getAlreadyInterestCount() 
    {
        return alreadyInterestCount;
    }
    public void setBreakContractRate(BigDecimal breakContractRate) 
    {
        this.breakContractRate = breakContractRate;
    }

    public BigDecimal getBreakContractRate() 
    {
        return breakContractRate;
    }
    public void setDailyIncomeRate(BigDecimal dailyIncomeRate) 
    {
        this.dailyIncomeRate = dailyIncomeRate;
    }

    public BigDecimal getDailyIncomeRate() 
    {
        return dailyIncomeRate;
    }
    public void setFloatDailyIncomeMinRate(BigDecimal floatDailyIncomeMinRate) 
    {
        this.floatDailyIncomeMinRate = floatDailyIncomeMinRate;
    }

    public BigDecimal getFloatDailyIncomeMinRate() 
    {
        return floatDailyIncomeMinRate;
    }
    public void setFloatDailyIncomeMaxRate(BigDecimal floatDailyIncomeMaxRate) 
    {
        this.floatDailyIncomeMaxRate = floatDailyIncomeMaxRate;
    }

    public String getFinancialImg() {
        return financialImg;
    }

    public void setFinancialImg(String financialImg) {
        this.financialImg = financialImg;
    }

    public BigDecimal getFloatDailyIncomeMaxRate()
    {
        return floatDailyIncomeMaxRate;
    }
    public void setOrderStatus(Integer orderStatus) 
    {
        this.orderStatus = orderStatus;
    }

    public Integer getOrderStatus() 
    {
        return orderStatus;
    }
    public void setAutoRenew(Integer autoRenew) 
    {
        this.autoRenew = autoRenew;
    }

    public Integer getAutoRenew() 
    {
        return autoRenew;
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

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getFinancialName() {
        return financialName;
    }

    public void setFinancialName(String financialName) {
        this.financialName = financialName;
    }

    public LangMgr getFinancialNameLang() {
        return financialNameLang;
    }

    public void setFinancialNameLang(LangMgr financialNameLang) {
        this.financialNameLang = financialNameLang;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("financialProductId", getFinancialProductId())
            .append("financialTime", getFinancialTime())
            .append("buyPrice", getBuyPrice())
            .append("userId", getUserId())
            .append("startDate", getStartDate())
            .append("endDate", getEndDate())
            .append("lastInterestTime", getLastInterestTime())
            .append("alreadyInterestCount", getAlreadyInterestCount())
            .append("breakContractRate", getBreakContractRate())
            .append("dailyIncomeRate", getDailyIncomeRate())
            .append("floatDailyIncomeMinRate", getFloatDailyIncomeMinRate())
            .append("floatDailyIncomeMaxRate", getFloatDailyIncomeMaxRate())
            .append("orderStatus", getOrderStatus())
            .append("autoRenew", getAutoRenew())
            .append("currencyId", getCurrencyId())
            .append("sqlVersion", getSqlVersion())
            .toString();
    }
}
