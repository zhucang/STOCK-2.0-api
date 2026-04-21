//package com.ruoyi.system.task.product.stock;
//
//
//import com.ruoyi.system.mapper.SelfSellProductRealtimeMapper;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//
///**
// * 自营产品定时器
// */
//@Component
//public class SelfSellStockTask {
//
//    @Resource
//    private SelfSellProductRealtimeMapper selfSellProductRealtimeMapper;
//
//
//    /**
//     * 清空自营股票分时图数据
//     */
//    @Scheduled(cron = "0 0 0 * * ? ")
//    public void cleanProductRealTimeDataTask() {
//        //清空除了今天的所有自营分时图数据
//        selfSellProductRealtimeMapper.cleanProductRealTimeDataTaskWithoutToday(1);
//    }
//
//}
