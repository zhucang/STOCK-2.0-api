package com.ruoyi.web.controller.api;

import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.exception.LangException;
import com.ruoyi.system.domain.StakingOrderInterestRecord;
import com.ruoyi.system.service.IStakingOrderInterestRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 质押订单派息记录Controller
 * 
 * @author ruoyi
 * @date 2025-07-20
 */
@RestController
@RequestMapping("/api/stakingOrderInterestRecord")
public class ApiStakingOrderInterestRecordController extends BaseController
{
    @Autowired
    private IStakingOrderInterestRecordService stakingOrderInterestRecordService;

    /**
     * 查询质押订单派息记录列表
     */
    @GetMapping("/list")
    public TableDataInfo list(StakingOrderInterestRecord stakingOrderInterestRecord)
    {
        startPage();
        if (stakingOrderInterestRecord.getStakingOrderId() == null){
            throw new LangException(HintConstants.SYSTEM_BUSY, "请选择相应的质押订单");
        }
        List<StakingOrderInterestRecord> list = stakingOrderInterestRecordService.selectStakingOrderInterestRecordList(stakingOrderInterestRecord);
        return getDataTable(list);
    }
}
