package com.ruoyi.system.task.staking;

import com.ruoyi.system.service.IStakingOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 质押任定时任务
 */
@Component
public class StakingTask {

    @Autowired
    private IStakingOrderService stakingOrderService;

    /**
     * 质押派息定时任务（每日凌晨一点固定派息）
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void payInterestTask(){
        stakingOrderService.payInterestTask();
    }
}
