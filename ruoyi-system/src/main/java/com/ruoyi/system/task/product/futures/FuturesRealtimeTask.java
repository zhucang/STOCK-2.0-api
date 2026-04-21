//package com.ruoyi.system.task.product.futures;
//
//import com.ruoyi.system.service.IRealtimeService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
///**
// * 期货分时图数据
// */
//@Component
//public class FuturesRealtimeTask {
//
//    @Autowired
//    private IRealtimeService realtimeService;
//
//    /**
//     * 保存期货分时图数据
//     */
//    @Scheduled(cron = "0 * * * * ?")
//    public void saveFuturesRealTimeDataTask(){
//        realtimeService.saveFuturesRealTimeDataTask();
//    }
//
//    /**
//     * 清空期货分时图数据
//     */
//    @Scheduled(cron = "0 0 0 * * ? ")
//    public void cleanProductRealTimeDataTask() {
//        realtimeService.cleanProductRealTimeDataTask(3);
//    }
//}
