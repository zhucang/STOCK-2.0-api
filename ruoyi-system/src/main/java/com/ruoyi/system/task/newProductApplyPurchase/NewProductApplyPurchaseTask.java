package com.ruoyi.system.task.newProductApplyPurchase;

import com.ruoyi.system.service.INewProductApplyPurchaseService;
import com.ruoyi.system.service.IUserApplyPurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 新股上市、新股申购定时任务
 */
@Component("NewProductApplyPurchaseTask")
public class NewProductApplyPurchaseTask {

    @Autowired
    private INewProductApplyPurchaseService newProductApplyPurchaseService;

    @Autowired
    private IUserApplyPurchaseOrderService userApplyPurchaseOrderService;

    /**
     * 新股新币上市定时任务
     */
    @Scheduled(cron = "10 0 * * * ?")
    public void newProductListing() {
        newProductApplyPurchaseService.newProductListingTask();
    }

    /**
     * 用户申购订单自动解锁定时任务
     */
    @Scheduled(cron = "30 1 * * * ?")
    public void userApplyPurchaseOrderAutoUnLockTask() {
        userApplyPurchaseOrderService.userApplyPurchaseOrderAutoUnLockTask();
    }

    /**
     * 新股新币开始申购定时任务
     */
    @Scheduled(cron = "10 0 * * * ?")
    public void newProductStartApplyPurchaseTask() {
        newProductApplyPurchaseService.newProductStartApplyPurchaseTask();
    }
}
