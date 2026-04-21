package com.ruoyi.system.task.financial;


import com.ruoyi.system.service.IFinancialOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 优化*
 * 理财每日派息
 */

@Component
public class FinancialTask {


    @Autowired
    private IFinancialOrderService financialOrderService;

    /**
     * 理财派息定时任务（每日凌晨一点固定派息）
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void payInterestTask(){
        financialOrderService.payInterestTask();
    }

    /**
     * 理财派息定时任务异常修复
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void exceptionPayInterestTask(){
        financialOrderService.exceptionPayInterestTask();
    }



}
