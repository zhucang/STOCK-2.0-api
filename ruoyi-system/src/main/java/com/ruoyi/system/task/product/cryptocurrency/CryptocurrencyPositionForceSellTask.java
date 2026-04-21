package com.ruoyi.system.task.product.cryptocurrency;


import com.ruoyi.system.service.IUserCryptocurrencyPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 加密货币强制平仓定时任务
 */
@Component
public class CryptocurrencyPositionForceSellTask {

    @Autowired
    private IUserCryptocurrencyPositionService userCryptocurrencyPositionService;

    /**
     * 加密货币爆仓定时任务
     */
    @Scheduled(cron = "* * * * * ?")
    public void cryptocurrencyPositionForceSellTask() {
        userCryptocurrencyPositionService.cryptocurrencyPositionForceSellTask();
    }

}
