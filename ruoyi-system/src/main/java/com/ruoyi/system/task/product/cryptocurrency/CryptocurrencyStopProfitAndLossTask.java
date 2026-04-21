package com.ruoyi.system.task.product.cryptocurrency;


import com.ruoyi.system.service.IUserCryptocurrencyPositionService;
import com.ruoyi.system.service.IUserProductPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 加密货币止盈止损定时任务
 */
@Component
public class CryptocurrencyStopProfitAndLossTask {

    @Autowired
    private IUserCryptocurrencyPositionService userCryptocurrencyPositionService;

    @Autowired
    private IUserProductPositionService userProductPositionService;

    /**
     * 加密货币止盈止损定时任务
     */
    @Scheduled(cron = "* * * * * ?")
    public void cryptocurrencyStopProfitAndLossTask() {
        userCryptocurrencyPositionService.cryptocurrencyStopProfitAndLossTask();
    }

    /**
     * 加密货币止盈止损定时任务
     */
    @Scheduled(cron = "* * * * * ?")
    public void cryptocurrencyStopProfitAndLossTask2() {
        userProductPositionService.stopProfitAndLossTask();
    }
}
