package com.ruoyi.system.task.copytrade;

import com.ruoyi.system.service.ICopyTradeSyncTaskService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 跟单同步任务消费者。
 */
@Component
public class CopyTradeSyncTaskTask {
    @Resource
    private ICopyTradeSyncTaskService copyTradeSyncTaskService;

    /** 每5秒批量处理一次待执行的跟单同步任务。 */
    @Scheduled(cron = "*/5 * * * * ?")
    public void processPendingSyncTasks() {
        copyTradeSyncTaskService.processPendingSyncTasks();
    }
}
