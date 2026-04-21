package com.ruoyi.system.task.product.stock;

import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.system.service.ICryptocurrencyEverydayRecordService;
import com.ruoyi.system.service.ISelfSellProductRealTimeService;
import com.ruoyi.system.service.IStockEverydayRecordService;
import com.ruoyi.system.utils.BuyAndSellUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 股票分时图数据
 */
@Component
public class StockRealtimeTask {


    @Autowired
    private ISelfSellProductRealTimeService selfSellProductRealTimeService;

    @Autowired
    private IStockEverydayRecordService stockEverydayRecordService;


    /**
     * 生成自营产品分时图数据
     */
    @Scheduled(cron = "0 0 9 ? * MON-FRI")
    public void generateAllProductRealTimeData() {
        //生成新数据之前记录行情每日记录
        stockEverydayRecordService.saveStockEverydayRecordTask();
        //生成新的自营产品分时图数据
        selfSellProductRealTimeService.generateAllProductRealTimeData(1);
    }


}
