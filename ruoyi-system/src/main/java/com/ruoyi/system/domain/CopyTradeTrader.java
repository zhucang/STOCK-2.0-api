package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.entity.UserInfoDetailVo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 跟单交易员对象 copy_trade_trader
 * 用于描述一个可以被用户跟随下单的“带单员”。
 */
public class CopyTradeTrader extends UserInfoDetailVo {
    private static final long serialVersionUID = 1L;

    /** 主键ID。 */
    private Long id;

    /** 交易员对应的用户ID。 */
    @Excel(name = "交易员用户ID")
    private Long userId;

    /** 前端展示给用户看的交易员标题。 */
    @Excel(name = "交易员标题")
    private String traderTitle;

    /** 交易员标题多语言。 */
    private LangMgr traderTitleLang;

    /** 交易员简介，用于展示交易风格或说明。 */
    @Excel(name = "交易员简介")
    private String traderDesc;

    /** 交易员简介多语言。 */
    private LangMgr traderDescLang;

    /** 状态 0启用 1停用。 */
    @Excel(name = "状态 0启用 1停用")
    private Integer status;

    /** 允许跟随者设置的最小跟单金额。 */
    @Excel(name = "最小跟单金额")
    private BigDecimal minFollowAmount;

    /** 允许跟随者设置的最大跟单金额。 */
    @Excel(name = "最大跟单金额")
    private BigDecimal maxFollowAmount;

    /** 限制可以跟随该交易员的最大人数。 */
    @Excel(name = "最大跟单人数")
    private Integer maxFollowerCount;

    /** 默认跟单模式 0固定金额 1按比例。 */
    @Excel(name = "默认跟单模式 0固定金额 1按比例")
    private Integer defaultFollowMode;

    /** 默认跟单比例，在按比例模式下使用。 */
    @Excel(name = "默认跟单比例")
    private BigDecimal defaultFollowRatio;

    /** APP展示的月收益率范围最低值，由后台直接配置。 */
    @Excel(name = "月收益率最低")
    private BigDecimal monthlyProfitMinRate;

    /** APP展示的月收益率范围最高值，由后台直接配置。 */
    @Excel(name = "月收益率最高")
    private BigDecimal monthlyProfitMaxRate;

    /** APP展示的年收益率范围最低值，由后台直接配置。 */
    @Excel(name = "年收益率最低")
    private BigDecimal annualProfitMinRate;

    /** APP展示的年收益率范围最高值，由后台直接配置。 */
    @Excel(name = "年收益率最高")
    private BigDecimal annualProfitMaxRate;

    /** 列表排序值，越小越靠前。 */
    @Excel(name = "排序")
    private Integer sort;

    /** 交易员头像，用于前端展示。 */
    private String avatar;

    /** 当前用户是否已跟随 0否 1是，仅用于APP列表展示。 */
    private Integer isFollow;

    /** 获取主键ID。 */
    public Long getId() {
        return id;
    }

    /** 设置主键ID。 */
    public void setId(Long id) {
        this.id = id;
    }

    /** 获取交易员用户ID。 */
    @Override
    public Long getUserId() {
        return userId;
    }

    /** 设置交易员用户ID。 */
    @Override
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /** 获取交易员标题。 */
    public String getTraderTitle() {
        return traderTitle;
    }

    /** 设置交易员标题。 */
    public void setTraderTitle(String traderTitle) {
        this.traderTitle = traderTitle;
    }

    /** 获取交易员标题多语言。 */
    public LangMgr getTraderTitleLang() {
        if (traderTitleLang == null){
            traderTitleLang = new LangMgr();
        }
        return traderTitleLang;
    }

    /** 设置交易员标题多语言。 */
    public void setTraderTitleLang(LangMgr traderTitleLang) {
        this.traderTitleLang = traderTitleLang;
    }

    /** 获取交易员简介。 */
    public String getTraderDesc() {
        return traderDesc;
    }

    /** 设置交易员简介。 */
    public void setTraderDesc(String traderDesc) {
        this.traderDesc = traderDesc;
    }

    /** 获取交易员简介多语言。 */
    public LangMgr getTraderDescLang() {
        if (traderDescLang == null){
            traderDescLang = new LangMgr();
        }
        return traderDescLang;
    }

    /** 设置交易员简介多语言。 */
    public void setTraderDescLang(LangMgr traderDescLang) {
        this.traderDescLang = traderDescLang;
    }

    /** 获取交易员状态。 */
    public Integer getStatus() {
        return status;
    }

    /** 设置交易员状态。 */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /** 获取最小跟单金额。 */
    public BigDecimal getMinFollowAmount() {
        return minFollowAmount;
    }

    /** 设置最小跟单金额。 */
    public void setMinFollowAmount(BigDecimal minFollowAmount) {
        this.minFollowAmount = minFollowAmount;
    }

    /** 获取最大跟单金额。 */
    public BigDecimal getMaxFollowAmount() {
        return maxFollowAmount;
    }

    /** 设置最大跟单金额。 */
    public void setMaxFollowAmount(BigDecimal maxFollowAmount) {
        this.maxFollowAmount = maxFollowAmount;
    }

    /** 获取最大跟单人数。 */
    public Integer getMaxFollowerCount() {
        return maxFollowerCount;
    }

    /** 设置最大跟单人数。 */
    public void setMaxFollowerCount(Integer maxFollowerCount) {
        this.maxFollowerCount = maxFollowerCount;
    }

    /** 获取默认跟单模式。 */
    public Integer getDefaultFollowMode() {
        return defaultFollowMode;
    }

    /** 设置默认跟单模式。 */
    public void setDefaultFollowMode(Integer defaultFollowMode) {
        this.defaultFollowMode = defaultFollowMode;
    }

    /** 获取默认跟单比例。 */
    public BigDecimal getDefaultFollowRatio() {
        return defaultFollowRatio;
    }

    /** 设置默认跟单比例。 */
    public void setDefaultFollowRatio(BigDecimal defaultFollowRatio) {
        this.defaultFollowRatio = defaultFollowRatio;
    }

    /** 获取月收益率范围最低值。 */
    public BigDecimal getMonthlyProfitMinRate() {
        return monthlyProfitMinRate;
    }

    /** 设置月收益率范围最低值。 */
    public void setMonthlyProfitMinRate(BigDecimal monthlyProfitMinRate) {
        this.monthlyProfitMinRate = monthlyProfitMinRate;
    }

    /** 获取月收益率范围最高值。 */
    public BigDecimal getMonthlyProfitMaxRate() {
        return monthlyProfitMaxRate;
    }

    /** 设置月收益率范围最高值。 */
    public void setMonthlyProfitMaxRate(BigDecimal monthlyProfitMaxRate) {
        this.monthlyProfitMaxRate = monthlyProfitMaxRate;
    }

    /** 获取年收益率范围最低值。 */
    public BigDecimal getAnnualProfitMinRate() {
        return annualProfitMinRate;
    }

    /** 设置年收益率范围最低值。 */
    public void setAnnualProfitMinRate(BigDecimal annualProfitMinRate) {
        this.annualProfitMinRate = annualProfitMinRate;
    }

    /** 获取年收益率范围最高值。 */
    public BigDecimal getAnnualProfitMaxRate() {
        return annualProfitMaxRate;
    }

    /** 设置年收益率范围最高值。 */
    public void setAnnualProfitMaxRate(BigDecimal annualProfitMaxRate) {
        this.annualProfitMaxRate = annualProfitMaxRate;
    }

    /** 获取排序值。 */
    public Integer getSort() {
        return sort;
    }

    /** 设置排序值。 */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /** 获取头像地址。 */
    public String getAvatar() {
        return avatar;
    }

    /** 设置头像地址。 */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    /** 获取当前用户是否已跟随。 */
    public Integer getIsFollow() {
        return isFollow;
    }

    /** 设置当前用户是否已跟随。 */
    public void setIsFollow(Integer isFollow) {
        this.isFollow = isFollow;
    }

    /** 输出对象调试信息。 */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("userId", getUserId())
                .append("traderTitle", getTraderTitle())
                .append("traderTitleLang", getTraderTitleLang())
                .append("traderDesc", getTraderDesc())
                .append("traderDescLang", getTraderDescLang())
                .append("status", getStatus())
                .append("minFollowAmount", getMinFollowAmount())
                .append("maxFollowAmount", getMaxFollowAmount())
                .append("maxFollowerCount", getMaxFollowerCount())
                .append("defaultFollowMode", getDefaultFollowMode())
                .append("defaultFollowRatio", getDefaultFollowRatio())
                .append("monthlyProfitMinRate", getMonthlyProfitMinRate())
                .append("monthlyProfitMaxRate", getMonthlyProfitMaxRate())
                .append("annualProfitMinRate", getAnnualProfitMinRate())
                .append("annualProfitMaxRate", getAnnualProfitMaxRate())
                .append("sort", getSort())
                .append("isFollow", getIsFollow())
                .toString();
    }
}
