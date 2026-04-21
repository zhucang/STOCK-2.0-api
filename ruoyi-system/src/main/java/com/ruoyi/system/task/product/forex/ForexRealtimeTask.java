//package com.ruoyi.system.task.product.forex;
//
//import com.ruoyi.system.service.IRealtimeService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
///**
// * 外汇分时图数据
// */
//@Component
//public class ForexRealtimeTask {
//
//    @Autowired
//    private IRealtimeService realtimeService;
//
//    /**
//     * 保存外汇分时图数据
//     */
//    @Scheduled(cron = "0 * * * * ?")
//    public void saveForexRealTimeDataTask(){
//        realtimeService.saveForexRealTimeDataTask();
//    }
//
//    /**
//     * 清空外汇分时图数据
//     */
//    @Scheduled(cron = "0 0 0 * * ? ")
//    public void cleanProductRealTimeDataTask() {
//        realtimeService.cleanProductRealTimeDataTask(4);
//    }
//}
