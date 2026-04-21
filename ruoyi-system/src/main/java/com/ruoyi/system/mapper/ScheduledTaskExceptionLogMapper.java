package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.ScheduledTaskExceptionLog;

import java.util.List;

/**
 * 定时任务异常日志Mapper接口
 * 
 * @author ruoyi
 * @date 2023-12-10
 * // TODO: 3/13/2024 cache待优化
 */
public interface ScheduledTaskExceptionLogMapper 
{
    /**
     * 查询定时任务异常日志
     * 
     * @param id 定时任务异常日志主键
     * @return 定时任务异常日志
     */
    public ScheduledTaskExceptionLog selectScheduledTaskExceptionLogById(Long id);

    /**
     * 查询定时任务异常日志列表
     * 
     * @param scheduledTaskExceptionLog 定时任务异常日志
     * @return 定时任务异常日志集合
     */
    public List<ScheduledTaskExceptionLog> selectScheduledTaskExceptionLogList(ScheduledTaskExceptionLog scheduledTaskExceptionLog);

    /**
     * 新增定时任务异常日志
     * 
     * @param scheduledTaskExceptionLog 定时任务异常日志
     * @return 结果
     */
    public int insertScheduledTaskExceptionLog(ScheduledTaskExceptionLog scheduledTaskExceptionLog);

    /**
     * 修改定时任务异常日志
     * 
     * @param scheduledTaskExceptionLog 定时任务异常日志
     * @return 结果
     */
    public int updateScheduledTaskExceptionLog(ScheduledTaskExceptionLog scheduledTaskExceptionLog);

    /**
     * 删除定时任务异常日志
     * 
     * @param id 定时任务异常日志主键
     * @return 结果
     */
    public int deleteScheduledTaskExceptionLogById(Long id);

    /**
     * 批量删除定时任务异常日志
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteScheduledTaskExceptionLogByIds(Long[] ids);
}
