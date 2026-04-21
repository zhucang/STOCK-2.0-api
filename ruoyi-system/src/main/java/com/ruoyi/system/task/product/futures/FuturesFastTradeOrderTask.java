package com.ruoyi.system.task.product.futures;

import com.ruoyi.system.service.IFastTradeOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 期货极速交易定时任务
 */
@Component
public class FuturesFastTradeOrderTask {

    @Autowired
    private IFastTradeOrderService fastTradeOrderService;

    /**
     * 期货极速交易结算定时器
     */
    @Scheduled(cron = "* * * * * ?")
    public void futuresFastTradeOrderSettleTask() {
        fastTradeOrderService.futuresFastTradeOrderSettleTask();
    }

    /**
     * 期货极速交易控制定时器
     */
    @Scheduled(cron = "* * * * * ?")
    public void futuresFastTradeOrderControlTask() {
        fastTradeOrderService.futuresFastTradeOrderControlTask();
    }
}
