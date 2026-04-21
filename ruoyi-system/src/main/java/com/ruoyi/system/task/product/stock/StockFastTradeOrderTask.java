package com.ruoyi.system.task.product.stock;

import com.ruoyi.system.service.IFastTradeOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 股票极速交易定时任务
 */
@Component
public class StockFastTradeOrderTask {

    @Autowired
    private IFastTradeOrderService fastTradeOrderService;

    /**
     * 股票极速交易结算定时器
     */
    @Scheduled(cron = "* * * * * ?")
    public void stockFastTradeOrderSettleTask() {
        fastTradeOrderService.stockFastTradeOrderSettleTask();
    }

    /**
     * 股票极速交易控制定时器
     */
    @Scheduled(cron = "* * * * * ?")
    public void stockFastTradeOrderControlTask() {
        fastTradeOrderService.stockFastTradeOrderControlTask();
    }


}
