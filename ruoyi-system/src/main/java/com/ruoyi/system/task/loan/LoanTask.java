package com.ruoyi.system.task.loan;

import com.ruoyi.system.service.ILoanOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 贷款定时任务
 */
@Component
public class LoanTask {

    @Autowired
    private ILoanOrderService loanOrderService;

    /**
     * 结算利息
     */
    /**
     * 贷款收取利息定时器（每日0点固定派息）
     */
    @Scheduled(cron = "10 0 0 * * ?")
    public void chargeInterestTask(){
        loanOrderService.chargeInterestTask();
    }
}
