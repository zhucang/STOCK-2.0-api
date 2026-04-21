package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 极速交易用户单控参数对象 user_fast_trade_control
 * 
 * @author ruoyi
 * @date 2023-12-20
 */
public class UserFastTradeControl extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    /** 控制类型：0：全部未控 1：全部控赢 2：全部控输 3：注额概率控（全部） 4：注额概率控（买涨） 5：注额概率控（买跌） 6：账户余额概率控（全部）
7：账户余额固定控（全部） 8：交易方向控（买多赢买空输） 9：交易方向控（买多输买空赢）
 */
    @Excel(name = "控制类型：0：全部未控 1：全部控赢 2：全部控输 3：注额概率控", readConverterExp = "全=部")
    private Integer controlType;

    /** 最小界限注额 */
    @Excel(name = "最小界限注额")
    private BigDecimal minLimitTradeAmount;

    /** 最大界限注额 */
    @Excel(name = "最大界限注额")
    private BigDecimal maxLimitTradeAmount;

    /** 注额界限范围之内赢率 */
    @Excel(name = "注额界限范围之内赢率")
    private BigDecimal winPercentWithinTradeAmount;

    /** 注额界限范围之外赢率 */
    @Excel(name = "注额界限范围之外赢率")
    private BigDecimal winPercentOutsideTradeAmount;

    /** 最小界限用户余额 */
    @Excel(name = "最小界限用户余额")
    private BigDecimal minLimitUserBalance;

    /** 最大界限用户余额 */
    @Excel(name = "最大界限用户余额")
    private BigDecimal maxLimitUserBalance;

    /** 用户余额界限范围内赢率 */
    @Excel(name = "用户余额界限范围内赢率")
    private BigDecimal winPercentWithinUserBalance;

    /** 用户余额界限范围之外赢率 */
    @Excel(name = "用户余额界限范围之外赢率")
    private BigDecimal winPercentOutsideUserBalance;

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
    public void setControlType(Integer controlType) 
    {
        this.controlType = controlType;
    }

    public Integer getControlType() 
    {
        return controlType;
    }
    public void setMinLimitTradeAmount(BigDecimal minLimitTradeAmount) 
    {
        this.minLimitTradeAmount = minLimitTradeAmount;
    }

    public BigDecimal getMinLimitTradeAmount() 
    {
        return minLimitTradeAmount;
    }
    public void setMaxLimitTradeAmount(BigDecimal maxLimitTradeAmount) 
    {
        this.maxLimitTradeAmount = maxLimitTradeAmount;
    }

    public BigDecimal getMaxLimitTradeAmount() 
    {
        return maxLimitTradeAmount;
    }
    public void setWinPercentWithinTradeAmount(BigDecimal winPercentWithinTradeAmount) 
    {
        this.winPercentWithinTradeAmount = winPercentWithinTradeAmount;
    }

    public BigDecimal getWinPercentWithinTradeAmount() 
    {
        return winPercentWithinTradeAmount;
    }
    public void setWinPercentOutsideTradeAmount(BigDecimal winPercentOutsideTradeAmount) 
    {
        this.winPercentOutsideTradeAmount = winPercentOutsideTradeAmount;
    }

    public BigDecimal getWinPercentOutsideTradeAmount() 
    {
        return winPercentOutsideTradeAmount;
    }
    public void setMinLimitUserBalance(BigDecimal minLimitUserBalance) 
    {
        this.minLimitUserBalance = minLimitUserBalance;
    }

    public BigDecimal getMinLimitUserBalance() 
    {
        return minLimitUserBalance;
    }
    public void setMaxLimitUserBalance(BigDecimal maxLimitUserBalance) 
    {
        this.maxLimitUserBalance = maxLimitUserBalance;
    }

    public BigDecimal getMaxLimitUserBalance() 
    {
        return maxLimitUserBalance;
    }
    public void setWinPercentWithinUserBalance(BigDecimal winPercentWithinUserBalance) 
    {
        this.winPercentWithinUserBalance = winPercentWithinUserBalance;
    }

    public BigDecimal getWinPercentWithinUserBalance() 
    {
        return winPercentWithinUserBalance;
    }
    public void setWinPercentOutsideUserBalance(BigDecimal winPercentOutsideUserBalance) 
    {
        this.winPercentOutsideUserBalance = winPercentOutsideUserBalance;
    }

    public BigDecimal getWinPercentOutsideUserBalance() 
    {
        return winPercentOutsideUserBalance;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("controlType", getControlType())
            .append("minLimitTradeAmount", getMinLimitTradeAmount())
            .append("maxLimitTradeAmount", getMaxLimitTradeAmount())
            .append("winPercentWithinTradeAmount", getWinPercentWithinTradeAmount())
            .append("winPercentOutsideTradeAmount", getWinPercentOutsideTradeAmount())
            .append("minLimitUserBalance", getMinLimitUserBalance())
            .append("maxLimitUserBalance", getMaxLimitUserBalance())
            .append("winPercentWithinUserBalance", getWinPercentWithinUserBalance())
            .append("winPercentOutsideUserBalance", getWinPercentOutsideUserBalance())
            .toString();
    }
}
