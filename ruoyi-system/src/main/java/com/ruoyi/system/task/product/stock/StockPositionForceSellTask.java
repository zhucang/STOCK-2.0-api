package com.ruoyi.system.task.product.stock;


import com.ruoyi.system.service.IUserStockPositionService;
import com.ruoyi.system.utils.BuyAndSellUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 股票强制平仓定时任务
 */
@Component
public class StockPositionForceSellTask {

    @Autowired
    private IUserStockPositionService userStockPositionService;

    /**
     * 股票爆仓定时任务
     */
    @Scheduled(cron = "* * 9-16 ? * MON-FRI")
    public void stockPositionForceSellTask() {
        //是否在交易时间内
        boolean transTime = BuyAndSellUtils.isTransTime("9:30", "16:00");
        if (transTime == false){
            return;
        }
        userStockPositionService.stockPositionForceSellTask();
    }

}
