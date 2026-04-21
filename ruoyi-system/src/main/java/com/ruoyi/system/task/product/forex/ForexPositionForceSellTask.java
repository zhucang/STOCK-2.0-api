package com.ruoyi.system.task.product.forex;


import com.ruoyi.system.service.IUserForexPositionService;
import com.ruoyi.system.service.IUserFuturesPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 外汇强制平仓定时任务
 */
@Component
public class ForexPositionForceSellTask {

    @Autowired
    private IUserForexPositionService userForexPositionService;

    /**
     * 外汇爆仓定时任务
     */
    @Scheduled(cron = "* * * * * ?")
    public void forexPositionForceSellTask() {
        userForexPositionService.forexPositionForceSellTask();
    }

}
