package com.ruoyi.system.task.product.forex;


import com.ruoyi.system.service.IUserForexPositionService;
import com.ruoyi.system.service.IUserFuturesPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 外汇止盈止损定时任务
 */
@Component
public class ForexStopProfitAndLossTask {

    @Autowired
    private IUserForexPositionService userForexPositionService;

    /**
     * 外汇止盈止损定时任务
     */
    @Scheduled(cron = "* * * * * ?")
    public void forexStopProfitAndLossTask() {
        userForexPositionService.forexStopProfitAndLossTask();
    }
}
