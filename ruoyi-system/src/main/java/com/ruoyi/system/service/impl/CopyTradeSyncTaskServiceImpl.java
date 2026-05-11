package com.ruoyi.system.service.impl;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.ruoyi.system.domain.CopyTradeOrder;
import com.ruoyi.system.domain.CopyTradePositionSnapshot;
import com.ruoyi.system.domain.CopyTradeRelation;
import com.ruoyi.system.domain.CopyTradeSyncTask;
import com.ruoyi.system.domain.CopyTradeTrader;
import com.ruoyi.system.domain.ScheduledTaskExceptionLog;
import com.ruoyi.system.mapper.CopyTradeSyncTaskMapper;
import com.ruoyi.system.mapper.ScheduledTaskExceptionLogMapper;
import com.ruoyi.system.service.ICopyTradeOrderService;
import com.ruoyi.system.service.ICopyTradeRelationService;
import com.ruoyi.system.service.ICopyTradeSyncTaskService;
import com.ruoyi.system.service.ICopyTradeTraderService;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 跟单同步任务服务实现类。
 */
@Service
public class CopyTradeSyncTaskServiceImpl implements ICopyTradeSyncTaskService {
    /** 同步任务类型：开仓。 */
    private static final int SYNC_TYPE_OPEN = 0;

    /** 同步任务类型：平仓。 */
    private static final int SYNC_TYPE_CLOSE = 1;

    /** 同步任务状态：待执行。 */
    private static final int TASK_STATUS_PENDING = 0;

    /** 单次消费任务数量，控制后台批处理压力。 */
    private static final int COPY_TRADE_TASK_BATCH_SIZE = 20;

    /** 默认最大重试次数。 */
    private static final int COPY_TRADE_TASK_MAX_RETRY_COUNT = 5;

    @Resource
    private CopyTradeSyncTaskMapper copyTradeSyncTaskMapper;

    @Resource
    private ICopyTradeRelationService copyTradeRelationService;

    @Resource
    private ICopyTradeTraderService copyTradeTraderService;

    @Lazy
    @Resource
    private ICopyTradeOrderService copyTradeOrderService;

    @Resource
    private ScheduledTaskExceptionLogMapper scheduledTaskExceptionLogMapper;

    /** 查询同步任务列表。 */
    @Override
    public List<CopyTradeSyncTask> selectCopyTradeSyncTaskList(CopyTradeSyncTask copyTradeSyncTask) {
        return copyTradeSyncTaskMapper.selectCopyTradeSyncTaskList(copyTradeSyncTask);
    }

    /** 查询单条同步任务。 */
    @Override
    public CopyTradeSyncTask selectCopyTradeSyncTaskById(Long id) {
        return copyTradeSyncTaskMapper.selectCopyTradeSyncTaskById(id);
    }

    /**
     * 交易员开仓后生成跟单开仓同步任务。
     * 产品交易服务只负责通知产品类型和主单ID，任务服务负责筛选交易员、跟单关系并落库任务。
     */
    @Override
    public void enqueueLeaderOpenSyncTasks(Integer productType, Long leaderPositionId) {
        CopyTradePositionSnapshot leaderPosition = copyTradeOrderService.selectCopyTradePositionSnapshot(productType, leaderPositionId);
        if (leaderPosition == null || leaderPosition.getId() == null || leaderPosition.getProductType() == null) {
            return;
        }
        CopyTradeTrader trader = copyTradeTraderService.selectActiveCopyTradeTraderByUserId(leaderPosition.getUserId());
        if (trader == null) {
            return;
        }
        List<CopyTradeRelation> relations = copyTradeRelationService.selectActiveRelationsByTraderUserId(trader.getUserId());
        List<CopyTradeRelation> executableRelations = new ArrayList<>();
        for (CopyTradeRelation relation : relations) {
            Integer activeOrderCount = copyTradeOrderService.countActiveOrderByRelationId(relation.getId());
            if (relation.getMaxOpenOrders() != null && relation.getMaxOpenOrders() > 0 && activeOrderCount >= relation.getMaxOpenOrders()) {
                continue;
            }
            executableRelations.add(relation);
        }
        enqueueOpenSyncTasks(executableRelations, leaderPosition);
    }

    /**
     * 交易员平仓后生成跟单平仓同步任务。
     * 平仓必须按 copy_trade_order 历史映射查已有跟随单，不依赖跟单关系当前状态。
     */
    @Override
    public void enqueueLeaderCloseSyncTasks(Integer productType, Long leaderPositionId) {
        CopyTradePositionSnapshot leaderPosition = copyTradeOrderService.selectCopyTradePositionSnapshot(productType, leaderPositionId);
        if (leaderPosition == null || leaderPosition.getId() == null || leaderPosition.getProductType() == null) {
            return;
        }
        List<CopyTradeOrder> orders = copyTradeOrderService.selectActiveOrdersByLeaderPositionId(leaderPosition.getProductType(), leaderPosition.getId());
        enqueueCloseSyncTasks(orders, leaderPosition);
    }

    /** 根据跟单关系(跟单人员)批量生成开仓同步任务。 */
    @Override
    public void enqueueOpenSyncTasks(List<CopyTradeRelation> relations, CopyTradePositionSnapshot leaderPosition) {
        List<CopyTradeSyncTask> tasks = new ArrayList<>();
        for (CopyTradeRelation relation : relations) {
            tasks.add(buildOpenSyncTask(relation, leaderPosition));
        }
        insertSyncTaskBatch(tasks);
    }

    /** 根据跟单订单批量生成平仓同步任务。 */
    @Override
    public void enqueueCloseSyncTasks(List<CopyTradeOrder> orders, CopyTradePositionSnapshot leaderPosition) {
        List<CopyTradeSyncTask> tasks = new ArrayList<>();
        for (CopyTradeOrder order : orders) {
            tasks.add(buildCloseSyncTask(order, leaderPosition));
        }
        insertSyncTaskBatch(tasks);
    }

    /** 生成单条平仓同步任务。 */
    @Override
    public void enqueueCloseSyncTask(CopyTradeOrder order, CopyTradePositionSnapshot leaderPosition) {
        copyTradeSyncTaskMapper.insertIgnoreCopyTradeSyncTask(buildCloseSyncTask(order, leaderPosition));
    }

    /** 批量处理待执行的跟单同步任务。 */
    @Override
    public void processPendingSyncTasks() {
        List<CopyTradeSyncTask> tasks = copyTradeSyncTaskMapper.selectExecutableSyncTasks(COPY_TRADE_TASK_BATCH_SIZE);
        for (CopyTradeSyncTask task : tasks) {
            if (copyTradeSyncTaskMapper.claimExecutableTask(task.getId()) <= 0) {
                continue;
            }
            try {
                boolean completed = processSingleSyncTask(task);
                if (completed) {
                    copyTradeSyncTaskMapper.markTaskSuccess(task.getId());
                }
            } catch (Exception e) {
                copyTradeSyncTaskMapper.markTaskFailure(task.getId(), abbreviateError(e));
                recordCopyTradeException("跟单同步任务执行", "taskId:" + task.getId(), e);
            }
        }
    }

    /** 批量删除同步任务。 */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteCopyTradeSyncTaskByIds(Long[] ids) {
        return copyTradeSyncTaskMapper.deleteCopyTradeSyncTaskByIds(ids);
    }

    /** 构建跟单开仓任务。 */
    private CopyTradeSyncTask buildOpenSyncTask(CopyTradeRelation relation, CopyTradePositionSnapshot copyTradePositionSnapshot) {
        CopyTradeSyncTask task = new CopyTradeSyncTask();
        task.setSyncType(SYNC_TYPE_OPEN);
        // 不同产品持仓表独立自增，主单ID可能相同；taskKey 必须包含产品类型，避免 insert ignore 误判重复任务。
        task.setTaskKey(buildTaskKey(SYNC_TYPE_OPEN, copyTradePositionSnapshot.getProductType(), relation.getId(), copyTradePositionSnapshot.getId()));
        task.setRelationId(relation.getId());
        task.setTraderUserId(relation.getTraderUserId());
        task.setFollowerUserId(relation.getFollowerUserId());
        task.setLeaderPositionId(copyTradePositionSnapshot.getId());
        task.setProductType(copyTradePositionSnapshot.getProductType());
        task.setStatus(TASK_STATUS_PENDING);
        task.setRetryCount(0);
        task.setMaxRetryCount(COPY_TRADE_TASK_MAX_RETRY_COUNT);
        task.setCreateTime(new Date());
        task.setUpdateTime(new Date());
        return task;
    }

    /** 构建跟单平仓任务。 */
    private CopyTradeSyncTask buildCloseSyncTask(CopyTradeOrder order, CopyTradePositionSnapshot copyTradePositionSnapshot) {
        CopyTradeSyncTask task = new CopyTradeSyncTask();
        task.setSyncType(SYNC_TYPE_CLOSE);
        // 平仓任务也带上产品类型，保证任务幂等键和排查口径与开仓任务一致。
        task.setTaskKey(buildTaskKey(SYNC_TYPE_CLOSE, order.getProductType() == null ? copyTradePositionSnapshot.getProductType() : order.getProductType(), order.getId(), copyTradePositionSnapshot.getId()));
        task.setCopyTradeOrderId(order.getId());
        task.setTraderUserId(order.getTraderUserId());
        task.setFollowerUserId(order.getFollowerUserId());
        task.setLeaderPositionId(copyTradePositionSnapshot.getId());
        task.setProductType(order.getProductType() == null ? copyTradePositionSnapshot.getProductType() : order.getProductType());
        task.setStatus(TASK_STATUS_PENDING);
        task.setRetryCount(0);
        task.setMaxRetryCount(COPY_TRADE_TASK_MAX_RETRY_COUNT);
        task.setCreateTime(new Date());
        task.setUpdateTime(new Date());
        return task;
    }

    /** 构建同步任务幂等键。 */
    private String buildTaskKey(Integer syncType, Integer productType, Long businessId, Long leaderPositionId) {
        return syncType + ":" + productType + ":" + businessId + ":" + leaderPositionId;
    }

    /** 批量插入同步任务。 */
    private void insertSyncTaskBatch(List<CopyTradeSyncTask> tasks) {
        if (tasks == null || tasks.size() == 0) {
            return;
        }
        copyTradeSyncTaskMapper.insertIgnoreCopyTradeSyncTaskBatch(tasks);
    }

    /** 执行单条同步任务。 */
    private boolean processSingleSyncTask(CopyTradeSyncTask task) {
        if (SYNC_TYPE_OPEN == task.getSyncType()) {
            return processOpenSyncTask(task);
        } else if (SYNC_TYPE_CLOSE == task.getSyncType()) {
            return processCloseSyncTask(task);
        } else {
            cancelTask(task.getId(), "不支持的跟单同步类型");
            return false;
        }
    }

    /** 执行开仓同步任务。 */
    private boolean processOpenSyncTask(CopyTradeSyncTask task) {
        CopyTradeRelation relation = copyTradeRelationService.selectCopyTradeRelationById(task.getRelationId());
        if (relation == null || relation.getStatus() == null || !relation.getStatus().equals(0)) {
            cancelTask(task.getId(), "跟单关系(跟单人员)不存在或已停止");
            return false;
        }
        Integer activeOrderCount = copyTradeOrderService.countActiveOrderByRelationId(relation.getId());
        if (relation.getMaxOpenOrders() != null && relation.getMaxOpenOrders() > 0 && activeOrderCount >= relation.getMaxOpenOrders()) {
            cancelTask(task.getId(), "已达到最大同时持仓数");
            return false;
        }
        CopyTradePositionSnapshot leaderPosition = copyTradeOrderService.selectCopyTradePositionSnapshot(task.getProductType(), task.getLeaderPositionId());
        if (leaderPosition == null) {
            cancelTask(task.getId(), "交易员主单不存在");
            return false;
        }
        if (leaderPosition.getOrderStatus() != null && leaderPosition.getOrderStatus().equals(1)) {
            cancelTask(task.getId(), "交易员主单已平仓");
            return false;
        }
        copyTradeOrderService.syncFollowerOpenPosition(relation, leaderPosition);
        return true;
    }

    /** 执行平仓同步任务。 */
    private boolean processCloseSyncTask(CopyTradeSyncTask task) {
        CopyTradeOrder order = copyTradeOrderService.selectCopyTradeOrderById(task.getCopyTradeOrderId());
        if (order == null || order.getStatus() == null || !order.getStatus().equals(0)) {
            cancelTask(task.getId(), "跟单订单不存在或已结束");
            return false;
        }
        CopyTradePositionSnapshot leaderPosition = copyTradeOrderService.selectCopyTradePositionSnapshot(task.getProductType(), task.getLeaderPositionId());
        if (leaderPosition == null) {
            cancelTask(task.getId(), "交易员主单不存在");
            return false;
        }
        copyTradeOrderService.syncFollowerClosePosition(order, leaderPosition);
        return true;
    }

    /** 取消无需继续重试的同步任务。 */
    private void cancelTask(Long taskId, String reason) {
        copyTradeSyncTaskMapper.markTaskCanceled(taskId, reason);
    }

    /** 记录跟单同步过程中的异常日志，便于后续排查。 */
    private void recordCopyTradeException(String jobName, String relateInfo, Exception e) {
        ScheduledTaskExceptionLog scheduledTaskExceptionLog = new ScheduledTaskExceptionLog();
        scheduledTaskExceptionLog.setJobName(jobName);
        scheduledTaskExceptionLog.setExceptionInfo(e.getMessage());
        scheduledTaskExceptionLog.setExceptionInfoDetail(ExceptionUtil.stacktraceToString(e));
        scheduledTaskExceptionLog.setRelateInfo(relateInfo);
        scheduledTaskExceptionLog.setCreateTime(DateUtils.getNowDate());
        scheduledTaskExceptionLogMapper.insertScheduledTaskExceptionLog(scheduledTaskExceptionLog);
    }

    /** 缩短错误信息，避免超过数据库字段长度。 */
    private String abbreviateError(Exception e) {
        String message = e.getMessage();
        if (message == null || message.length() == 0) {
            message = e.getClass().getSimpleName();
        }
        return message.length() > 1000 ? message.substring(0, 1000) : message;
    }
}
