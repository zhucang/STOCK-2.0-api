package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 跟单同步任务对象 copy_trade_sync_task
 * 用于把交易员开平仓后的跟单同步持久化，项目重启后可继续补执行。
 */
public class CopyTradeSyncTask extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键ID。 */
    private Long id;

    /** 同步类型 0开仓 1平仓。 */
    @Excel(name = "同步类型 0开仓 1平仓")
    private Integer syncType;

    /** 任务唯一键，用于防止重复入库。 */
    @Excel(name = "任务唯一键")
    private String taskKey;

    /** 跟单关系ID，开仓任务使用。 */
    @Excel(name = "跟单关系ID")
    private Long relationId;

    /** 跟单订单映射ID，平仓任务使用。 */
    @Excel(name = "跟单订单ID")
    private Long copyTradeOrderId;

    /** 交易员用户ID。 */
    @Excel(name = "交易员用户ID")
    private Long traderUserId;

    /** 跟单用户ID。 */
    @Excel(name = "跟单用户ID")
    private Long followerUserId;

    /** 主单持仓ID。 */
    @Excel(name = "主单持仓ID")
    private Long leaderPositionId;

    /** 产品类型 1股票 2加密货币 3期货 4外汇。 */
    @Excel(name = "产品类型")
    private Integer productType;

    /** 状态 0待执行 1执行中 2成功 3失败 4取消。 */
    @Excel(name = "状态 0待执行 1执行中 2成功 3失败 4取消")
    private Integer status;

    /** 已重试次数。 */
    @Excel(name = "已重试次数")
    private Integer retryCount;

    /** 最大重试次数。 */
    @Excel(name = "最大重试次数")
    private Integer maxRetryCount;

    /** 下次允许执行时间。 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date nextRetryTime;

    /** 最近一次错误信息。 */
    @Excel(name = "最近错误")
    private String lastError;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSyncType() {
        return syncType;
    }

    public void setSyncType(Integer syncType) {
        this.syncType = syncType;
    }

    public String getTaskKey() {
        return taskKey;
    }

    public void setTaskKey(String taskKey) {
        this.taskKey = taskKey;
    }

    public Long getRelationId() {
        return relationId;
    }

    public void setRelationId(Long relationId) {
        this.relationId = relationId;
    }

    public Long getCopyTradeOrderId() {
        return copyTradeOrderId;
    }

    public void setCopyTradeOrderId(Long copyTradeOrderId) {
        this.copyTradeOrderId = copyTradeOrderId;
    }

    public Long getTraderUserId() {
        return traderUserId;
    }

    public void setTraderUserId(Long traderUserId) {
        this.traderUserId = traderUserId;
    }

    public Long getFollowerUserId() {
        return followerUserId;
    }

    public void setFollowerUserId(Long followerUserId) {
        this.followerUserId = followerUserId;
    }

    public Long getLeaderPositionId() {
        return leaderPositionId;
    }

    public void setLeaderPositionId(Long leaderPositionId) {
        this.leaderPositionId = leaderPositionId;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public Integer getMaxRetryCount() {
        return maxRetryCount;
    }

    public void setMaxRetryCount(Integer maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
    }

    public Date getNextRetryTime() {
        return nextRetryTime;
    }

    public void setNextRetryTime(Date nextRetryTime) {
        this.nextRetryTime = nextRetryTime;
    }

    public String getLastError() {
        return lastError;
    }

    public void setLastError(String lastError) {
        this.lastError = lastError;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("syncType", getSyncType())
                .append("taskKey", getTaskKey())
                .append("relationId", getRelationId())
                .append("copyTradeOrderId", getCopyTradeOrderId())
                .append("traderUserId", getTraderUserId())
                .append("followerUserId", getFollowerUserId())
                .append("leaderPositionId", getLeaderPositionId())
                .append("productType", getProductType())
                .append("status", getStatus())
                .append("retryCount", getRetryCount())
                .append("maxRetryCount", getMaxRetryCount())
                .append("nextRetryTime", getNextRetryTime())
                .append("lastError", getLastError())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
