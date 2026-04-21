package com.ruoyi.system.task.product.forex;

import com.ruoyi.system.service.IFastTradeOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 外汇极速交易定时任务
 */
@Component
public class ForexFastTradeOrderTask {

    @Autowired
    private IFastTradeOrderService fastTradeOrderService;

    /**
     * 外汇极速交易结算定时器
     */
    @Scheduled(cron = "* * * * * ?")
    public void forexFastTradeOrderSettleTask() {
        fastTradeOrderService.forexFastTradeOrderSettleTask();
    }

    /**
     * 外汇极速交易控制定时器
     */
    @Scheduled(cron = "* * * * * ?")
    public void forexFastTradeOrderControlTask() {
        fastTradeOrderService.forexFastTradeOrderControlTask();
    }
}
