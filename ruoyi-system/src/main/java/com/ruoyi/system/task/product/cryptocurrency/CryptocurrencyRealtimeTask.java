package com.ruoyi.system.task.product.cryptocurrency;

import com.ruoyi.system.mapper.OtherValueMapper;
import com.ruoyi.system.service.ICryptocurrencyEverydayRecordService;
import com.ruoyi.system.service.ISelfSellProductRealTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 加密货币分时图数据
 */
@Component
public class CryptocurrencyRealtimeTask {

    @Autowired
    private ISelfSellProductRealTimeService selfSellProductRealTimeService;

    @Autowired
    private ICryptocurrencyEverydayRecordService cryptocurrencyEverydayRecordService;

    /**
     * 生成自营产品分时图数据
     */
    @Scheduled(cron = "20 0 0 * * ? ")
    public void generateAllProductRealTimeData() {
        //生成新数据之前记录行情每日记录
        cryptocurrencyEverydayRecordService.saveCryptocurrencyEverydayRecordTask();
        //生成新的自营产品分时图数据
        selfSellProductRealTimeService.generateAllProductRealTimeData(2);
    }
}
