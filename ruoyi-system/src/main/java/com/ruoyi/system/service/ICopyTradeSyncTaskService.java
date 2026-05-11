package com.ruoyi.system.service;

import com.ruoyi.system.domain.CopyTradeOrder;
import com.ruoyi.system.domain.CopyTradePositionSnapshot;
import com.ruoyi.system.domain.CopyTradeRelation;
import com.ruoyi.system.domain.CopyTradeSyncTask;

import java.util.List;

/**
 * 跟单同步任务服务接口。
 */
public interface ICopyTradeSyncTaskService {
    /** 查询同步任务列表。 */
    List<CopyTradeSyncTask> selectCopyTradeSyncTaskList(CopyTradeSyncTask copyTradeSyncTask);

    /** 查询单条同步任务。 */
    CopyTradeSyncTask selectCopyTradeSyncTaskById(Long id);

    /**
     * 交易员开仓后生成跟单开仓同步任务。
     *
     * @param productType 产品类型
     * @param leaderPositionId 交易员主单持仓ID
     */
    void enqueueLeaderOpenSyncTasks(Integer productType, Long leaderPositionId);

    /**
     * 交易员平仓后生成跟单平仓同步任务。
     *
     * @param productType 产品类型
     * @param leaderPositionId 交易员主单持仓ID
     */
    void enqueueLeaderCloseSyncTasks(Integer productType, Long leaderPositionId);

    /** 根据跟单关系(跟单人员)批量生成开仓同步任务。 */
    void enqueueOpenSyncTasks(List<CopyTradeRelation> relations, CopyTradePositionSnapshot leaderPosition);

    /** 根据跟单订单批量生成平仓同步任务。 */
    void enqueueCloseSyncTasks(List<CopyTradeOrder> orders, CopyTradePositionSnapshot leaderPosition);

    /** 生成单条平仓同步任务。 */
    void enqueueCloseSyncTask(CopyTradeOrder order, CopyTradePositionSnapshot leaderPosition);

    /** 批量处理待执行的跟单同步任务。 */
    void processPendingSyncTasks();

    /** 批量删除同步任务。 */
    int deleteCopyTradeSyncTaskByIds(Long[] ids);
}
