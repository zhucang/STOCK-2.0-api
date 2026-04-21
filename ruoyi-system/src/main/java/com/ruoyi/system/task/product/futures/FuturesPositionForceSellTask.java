package com.ruoyi.system.task.product.futures;


import com.ruoyi.system.service.IUserFuturesPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 期货强制平仓定时任务
 */
@Component
public class FuturesPositionForceSellTask {

    @Autowired
    private IUserFuturesPositionService userFuturesPositionService;

    /**
     * 期货爆仓定时任务
     */
    @Scheduled(cron = "* * * * * ?")
    public void futuresPositionForceSellTask() {
        userFuturesPositionService.futuresPositionForceSellTask();
    }

}
