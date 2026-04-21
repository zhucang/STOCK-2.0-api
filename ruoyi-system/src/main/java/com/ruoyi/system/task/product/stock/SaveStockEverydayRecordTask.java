//package com.ruoyi.system.task.product.stock;
//
//import com.ruoyi.system.service.IStockEverydayRecordService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
///**
// * 保存股票每日数据
// */
//@Component
//public class SaveStockEverydayRecordTask {
//
//    @Autowired
//    private IStockEverydayRecordService stockEverydayRecordService;
//
//    /**
//     * 每日收盘时保存每日数据
//     */
//    @Scheduled(cron = "0 0 1 * * ?")
//    public void saveStockEverydayRecordTask() {
//        stockEverydayRecordService.saveStockEverydayRecordTask();
//    }
//
//}