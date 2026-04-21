package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.entity.UserInfoDetailVo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 质押订单对象 staking_order
 * 
 * @author ruoyi
 * @date 2025-07-17
 */
public class StakingOrder extends UserInfoDetailVo
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 订单号 */
    @Excel(name = "订单号")
    private String orderCode;

    /** 质押产品id */
    @Excel(name = "质押产品id")
    private Long stakingProductId;

    /** 质押名称 */
    @Excel(name = "质押名称")
    private String stakingName;

    /** 质押名称多语言 */
    @Excel(name = "质押名称多语言")
    private LangMgr stakingNameLang;

    /** 质押天数 */
    @Excel(name = "质押天数")
    private Integer stakingTime;

    /** 质押初始金额 */
    @Excel(name = "质押初始金额")
    private BigDecimal buyPrice;

    /** 质押池金额 */
    @Excel(name = "质押池金额")
    private BigDecimal stakingPoolAmount;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    /** 质押开始日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "质押开始日期")
    private Date startDate;

    /** 质押结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "质押结束日期")
    private Date endDate;

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

    /** 订单状态：0：待审核  1：进行中 2：已完成 3：已驳回 4：已赎回 */
    @Excel(name = "订单状态：0：待审核  1：进行中 2：已完成 3：已驳回 4：已赎回")
    private Integer orderStatus;

    /** 货币id */
    @Excel(name = "货币id")
    private Long currencyId;

    /** 币种名称 */
    @Excel(name = "币种名称")
    private String currencyName;

    /** 浮动日收益最低 */
    @Excel(name = "浮动日收益最低")
    private BigDecimal floatDailyIncomeMinRate;

    /** 浮动日收益最高 */
    @Excel(name = "浮动日收益最高")
    private BigDecimal floatDailyIncomeMaxRate;

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
    public void setOrderCode(String orderCode) 
    {
        this.orderCode = orderCode;
    }

    public String getOrderCode() 
    {
        return orderCode;
    }
    public void setStakingProductId(Long stakingProductId) 
    {
        this.stakingProductId = stakingProductId;
    }

    public Long getStakingProductId() 
    {
        return stakingProductId;
    }

    public String getStakingName() {
        return stakingName;
    }

    public void setStakingName(String stakingName) {
        this.stakingName = stakingName;
    }

    public LangMgr getStakingNameLang() {
        return stakingNameLang;
    }

    public void setStakingNameLang(LangMgr stakingNameLang) {
        this.stakingNameLang = stakingNameLang;
    }

    public void setStakingTime(Integer stakingTime)
    {
        this.stakingTime = stakingTime;
    }

    public Integer getStakingTime() 
    {
        return stakingTime;
    }
    public void setBuyPrice(BigDecimal buyPrice) 
    {
        this.buyPrice = buyPrice;
    }

    public BigDecimal getBuyPrice() 
    {
        return buyPrice;
    }
    public void setStakingPoolAmount(BigDecimal stakingPoolAmount) 
    {
        this.stakingPoolAmount = stakingPoolAmount;
    }

    public BigDecimal getStakingPoolAmount() 
    {
        return stakingPoolAmount;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    public Date getStartDate()
    {
        return startDate;
    }
    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

    public Date getEndDate()
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
    public void setOrderStatus(Integer orderStatus) 
    {
        this.orderStatus = orderStatus;
    }

    public Integer getOrderStatus() 
    {
        return orderStatus;
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

    public BigDecimal getFloatDailyIncomeMaxRate() 
    {
        return floatDailyIncomeMaxRate;
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
            .append("orderCode", getOrderCode())
            .append("stakingProductId", getStakingProductId())
            .append("stakingTime", getStakingTime())
            .append("buyPrice", getBuyPrice())
            .append("stakingPoolAmount", getStakingPoolAmount())
            .append("userId", getUserId())
            .append("startDate", getStartDate())
            .append("endDate", getEndDate())
            .append("lastInterestTime", getLastInterestTime())
            .append("alreadyInterestCount", getAlreadyInterestCount())
            .append("breakContractRate", getBreakContractRate())
            .append("dailyIncomeRate", getDailyIncomeRate())
            .append("createTime", getCreateTime())
            .append("orderStatus", getOrderStatus())
            .append("currencyId", getCurrencyId())
            .append("floatDailyIncomeMinRate", getFloatDailyIncomeMinRate())
            .append("floatDailyIncomeMaxRate", getFloatDailyIncomeMaxRate())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("sqlVersion", getSqlVersion())
            .toString();
    }
}
