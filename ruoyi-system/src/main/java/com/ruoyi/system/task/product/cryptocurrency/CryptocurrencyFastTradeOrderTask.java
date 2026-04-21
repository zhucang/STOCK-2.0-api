package com.ruoyi.system.task.product.cryptocurrency;

import com.ruoyi.system.service.IFastTradeOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 加密货币极速交易定时任务
 */
@Component
public class CryptocurrencyFastTradeOrderTask {

    @Autowired
    private IFastTradeOrderService fastTradeOrderService;

    /**
     * 加密货币极速交易结算定时器
     */
    @Scheduled(cron = "* * * * * ?")
    public void cryptocurrencyFastTradeOrderSettleTask() {
        fastTradeOrderService.cryptocurrencyFastTradeOrderSettleTask();
    }

    /**
     * 加密货币极速交易控制定时器
     */
    @Scheduled(cron = "* * * * * ?")
    public void cryptocurrencyFastTradeOrderControlTask() {
        fastTradeOrderService.cryptocurrencyFastTradeOrderControlTask();
    }
}
