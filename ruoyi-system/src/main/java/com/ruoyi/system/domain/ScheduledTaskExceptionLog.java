package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 定时任务异常日志对象 scheduled_task_exception_log
 * 
 * @author ruoyi
 * @date 2023-12-10
 */
public class ScheduledTaskExceptionLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 任务日志ID */
    private Long id;

    /** 任务名称 */
    @Excel(name = "任务名称")
    private String jobName;

    /** 异常信息 */
    @Excel(name = "异常信息")
    private String exceptionInfo;

    /** 是否修复 0：未修复 1：已修复  */
    @Excel(name = "是否修复 0：未修复 1：已修复 ")
    private Integer isFixed;

    /** 异常详情 */
    @Excel(name = "异常详情")
    private String exceptionInfoDetail;

    /** 类型：1：理财派发利息任务 2：股票止盈止损触发定时任务 3：加密货币止盈止损触发定时任务 4：期货止盈止损触发定时任务 5:新股新币上市定时器 6:新股新币开始申购定时器 7:留仓费收取定时任务 8:股票留仓到期强制平仓任务 9:同步节假日开关定时任务 10:每日16点收盘时保存每日数据定时任务  11:删除分时图数据定时任务 12：外汇止盈止损定时任务 13:股票强制平仓定时任务 14：加密货币强制平仓定时任务 15：期货强制平仓定时任务 16：外汇强制平仓定时任务 17：贷款收取利息定时任务 */
    @Excel(name = "类型：1：理财派发利息任务 2：股票止盈止损触发定时任务 3：加密货币止盈止损触发定时任务 4：期货止盈止损触发定时任务 5:新股新币上市定时器 6:新股新币开始申购定时器 7:留仓费收取定时任务 8:股票留仓到期强制平仓任务 9:同步节假日开关定时任务 10:每日16点收盘时保存每日数据定时任务  11:删除分时图数据定时任务 12：外汇止盈止损定时任务 13:股票强制平仓定时任务 14：加密货币强制平仓定时任务 15：期货强制平仓定时任务 16：外汇强制平仓定时任务 17：贷款收取利息定时任务 18：币币交易委托订单自动通过定时任务")
    private Integer type;

    /** 执行次数 */
    @Excel(name = "执行次数")
    private Integer executeCount;

    /** 相关信息 （id或订单号等） */
    @Excel(name = "相关信息 ", readConverterExp = "i=d或订单号等")
    private String relateInfo;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setJobName(String jobName) 
    {
        this.jobName = jobName;
    }

    public String getJobName() 
    {
        return jobName;
    }
    public void setExceptionInfo(String exceptionInfo) 
    {
        this.exceptionInfo = exceptionInfo;
    }

    public String getExceptionInfo() 
    {
        return exceptionInfo;
    }
    public void setIsFixed(Integer isFixed) 
    {
        this.isFixed = isFixed;
    }

    public Integer getIsFixed() 
    {
        return isFixed;
    }
    public void setExceptionInfoDetail(String exceptionInfoDetail) 
    {
        this.exceptionInfoDetail = exceptionInfoDetail;
    }

    public String getExceptionInfoDetail() 
    {
        return exceptionInfoDetail;
    }
    public void setType(Integer type) 
    {
        this.type = type;
    }

    public Integer getType() 
    {
        return type;
    }
    public void setExecuteCount(Integer executeCount) 
    {
        this.executeCount = executeCount;
    }

    public Integer getExecuteCount() 
    {
        return executeCount;
    }
    public void setRelateInfo(String relateInfo) 
    {
        this.relateInfo = relateInfo;
    }

    public String getRelateInfo() 
    {
        return relateInfo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("jobName", getJobName())
            .append("exceptionInfo", getExceptionInfo())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("isFixed", getIsFixed())
            .append("exceptionInfoDetail", getExceptionInfoDetail())
            .append("type", getType())
            .append("executeCount", getExecuteCount())
            .append("relateInfo", getRelateInfo())
            .toString();
    }
}
