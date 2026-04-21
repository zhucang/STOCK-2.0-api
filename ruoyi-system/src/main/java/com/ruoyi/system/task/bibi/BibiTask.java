package com.ruoyi.system.task.bibi;

import com.ruoyi.system.service.IBibiTradeOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 优化*
 * 币币交易订单定时任务
 */

@Component
public class BibiTask {

    @Autowired
    private IBibiTradeOrderService bibiTradeOrderService;

    /**
     * 委托订单自动通过定时任务
     */
    @Scheduled(cron = "* * * * * ?")
    public void bibiOrderAutoDealTask(){
        bibiTradeOrderService.bibiOrderAutoDealTask();
    }


}
