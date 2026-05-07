package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.CopyTradeSyncTask;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 跟单同步任务 Mapper 接口。
 */
public interface CopyTradeSyncTaskMapper {
    /** 查询同步任务列表。 */
    List<CopyTradeSyncTask> selectCopyTradeSyncTaskList(CopyTradeSyncTask copyTradeSyncTask);

    /** 查询单条同步任务。 */
    CopyTradeSyncTask selectCopyTradeSyncTaskById(Long id);

    /** 新增同步任务，唯一键冲突时忽略。 */
    int insertIgnoreCopyTradeSyncTask(CopyTradeSyncTask copyTradeSyncTask);

    /** 批量新增同步任务，唯一键冲突时忽略。 */
    int insertIgnoreCopyTradeSyncTaskBatch(@Param("list") List<CopyTradeSyncTask> list);

    /** 查询可执行任务。 */
    List<CopyTradeSyncTask> selectExecutableSyncTasks(@Param("limit") Integer limit);

    /** 抢占任务，避免多线程或多实例重复执行。 */
    int claimExecutableTask(@Param("id") Long id);

    /** 标记任务成功。 */
    int markTaskSuccess(@Param("id") Long id);

    /** 标记任务失败并安排下一次重试。 */
    int markTaskFailure(@Param("id") Long id, @Param("lastError") String lastError);

    /** 标记任务取消。 */
    int markTaskCanceled(@Param("id") Long id, @Param("lastError") String lastError);

    /** 批量删除同步任务。 */
    int deleteCopyTradeSyncTaskByIds(Long[] ids);
}
