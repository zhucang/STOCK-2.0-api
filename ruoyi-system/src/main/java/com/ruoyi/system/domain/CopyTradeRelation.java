package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 跟单关系(跟单人员)对象 copy_trade_relation
 * 表示某个用户正在跟随某个交易员，以及跟随的参数配置。
 */
public class CopyTradeRelation extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键ID。 */
    private Long id;

    /** 交易员配置主键ID。 */
    @Excel(name = "交易员ID")
    private Long traderId;

    /** 交易员用户ID。 */
    @Excel(name = "交易员用户ID")
    private Long traderUserId;

    /** 交易员账号，仅用于展示。 */
    private String traderUserAccount;

    /** 交易员昵称，仅用于展示。 */
    private String traderNickName;

    /** 跟单用户ID。 */
    @Excel(name = "跟单用户ID")
    private Long followerUserId;

    /** 跟单用户账号，仅用于展示。 */
    private String followerUserAccount;

    /** 跟单用户昵称，仅用于展示。 */
    private String followerNickName;

    /** 跟单模式 0固定金额 1按比例。 */
    @Excel(name = "跟单模式 0固定金额 1按比例")
    private Integer followMode;

    /** 固定金额模式下每次跟单的金额。 */
    @Excel(name = "固定跟单金额")
    private BigDecimal followAmount;

    /** 比例模式下，按交易员保证金乘以该比例跟单。 */
    @Excel(name = "跟单比例")
    private BigDecimal followRatio;

    /** 允许该关系同时持有的最大跟单仓位数。 */
    @Excel(name = "最大同时持仓数")
    private Integer maxOpenOrders;

    /** 状态 0跟单中 1已停止。 */
    @Excel(name = "状态 0跟单中 1已停止")
    private Integer status;

    /** 删除标志 0未删除 1已删除。 */
    private Integer delFlag;

    /** 删除时间。 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deleteTime;

    /** 最近一次成功跟单的时间。 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastFollowTime;

    /** 当前这条关系下还在持仓中的跟单单数。 */
    private Integer currentOpenOrders;

    /** 交易员详细信息，仅用于前端展示。 */
    private CopyTradeTrader traderInfo;

    /** 获取主键ID。 */
    public Long getId() {
        return id;
    }

    /** 设置主键ID。 */
    public void setId(Long id) {
        this.id = id;
    }

    /** 获取交易员主键ID。 */
    public Long getTraderId() {
        return traderId;
    }

    /** 设置交易员主键ID。 */
    public void setTraderId(Long traderId) {
        this.traderId = traderId;
    }

    /** 获取交易员用户ID。 */
    public Long getTraderUserId() {
        return traderUserId;
    }

    /** 设置交易员用户ID。 */
    public void setTraderUserId(Long traderUserId) {
        this.traderUserId = traderUserId;
    }

    /** 获取交易员账号。 */
    public String getTraderUserAccount() {
        return traderUserAccount;
    }

    /** 设置交易员账号。 */
    public void setTraderUserAccount(String traderUserAccount) {
        this.traderUserAccount = traderUserAccount;
    }

    /** 获取交易员昵称。 */
    public String getTraderNickName() {
        return traderNickName;
    }

    /** 设置交易员昵称。 */
    public void setTraderNickName(String traderNickName) {
        this.traderNickName = traderNickName;
    }

    /** 获取跟单用户ID。 */
    public Long getFollowerUserId() {
        return followerUserId;
    }

    /** 设置跟单用户ID。 */
    public void setFollowerUserId(Long followerUserId) {
        this.followerUserId = followerUserId;
    }

    /** 获取跟单用户账号。 */
    public String getFollowerUserAccount() {
        return followerUserAccount;
    }

    /** 设置跟单用户账号。 */
    public void setFollowerUserAccount(String followerUserAccount) {
        this.followerUserAccount = followerUserAccount;
    }

    /** 获取跟单用户昵称。 */
    public String getFollowerNickName() {
        return followerNickName;
    }

    /** 设置跟单用户昵称。 */
    public void setFollowerNickName(String followerNickName) {
        this.followerNickName = followerNickName;
    }

    /** 获取跟单模式。 */
    public Integer getFollowMode() {
        return followMode;
    }

    /** 设置跟单模式。 */
    public void setFollowMode(Integer followMode) {
        this.followMode = followMode;
    }

    /** 获取固定跟单金额。 */
    public BigDecimal getFollowAmount() {
        return followAmount;
    }

    /** 设置固定跟单金额。 */
    public void setFollowAmount(BigDecimal followAmount) {
        this.followAmount = followAmount;
    }

    /** 获取跟单比例。 */
    public BigDecimal getFollowRatio() {
        return followRatio;
    }

    /** 设置跟单比例。 */
    public void setFollowRatio(BigDecimal followRatio) {
        this.followRatio = followRatio;
    }

    /** 获取最大同时持仓数。 */
    public Integer getMaxOpenOrders() {
        return maxOpenOrders;
    }

    /** 设置最大同时持仓数。 */
    public void setMaxOpenOrders(Integer maxOpenOrders) {
        this.maxOpenOrders = maxOpenOrders;
    }

    /** 获取状态。 */
    public Integer getStatus() {
        return status;
    }

    /** 设置状态。 */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /** 获取删除标志。 */
    public Integer getDelFlag() {
        return delFlag;
    }

    /** 设置删除标志。 */
    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    /** 获取删除时间。 */
    public Date getDeleteTime() {
        return deleteTime;
    }

    /** 设置删除时间。 */
    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }

    /** 获取最近跟单时间。 */
    public Date getLastFollowTime() {
        return lastFollowTime;
    }

    /** 设置最近跟单时间。 */
    public void setLastFollowTime(Date lastFollowTime) {
        this.lastFollowTime = lastFollowTime;
    }

    /** 获取当前持仓中的跟单单数。 */
    public Integer getCurrentOpenOrders() {
        return currentOpenOrders;
    }

    /** 设置当前持仓中的跟单单数。 */
    public void setCurrentOpenOrders(Integer currentOpenOrders) {
        this.currentOpenOrders = currentOpenOrders;
    }

    /** 获取交易员详细信息。 */
    public CopyTradeTrader getTraderInfo() {
        return traderInfo;
    }

    /** 设置交易员详细信息。 */
    public void setTraderInfo(CopyTradeTrader traderInfo) {
        this.traderInfo = traderInfo;
    }

    /** 输出对象调试信息。 */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("traderId", getTraderId())
                .append("traderUserId", getTraderUserId())
                .append("followerUserId", getFollowerUserId())
                .append("followMode", getFollowMode())
                .append("followAmount", getFollowAmount())
                .append("followRatio", getFollowRatio())
                .append("maxOpenOrders", getMaxOpenOrders())
                .append("status", getStatus())
                .append("delFlag", getDelFlag())
                .append("deleteTime", getDeleteTime())
                .append("lastFollowTime", getLastFollowTime())
                .append("currentOpenOrders", getCurrentOpenOrders())
                .append("traderInfo", getTraderInfo())
                .toString();
    }
}
