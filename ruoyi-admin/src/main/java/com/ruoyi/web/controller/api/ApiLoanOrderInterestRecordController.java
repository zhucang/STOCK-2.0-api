package com.ruoyi.web.controller.api;

import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.exception.LangException;
import com.ruoyi.system.domain.LoanOrderInterestRecord;
import com.ruoyi.system.service.ILoanOrderInterestRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 贷款订单利息生成记录Controller
 * 
 * @author ruoyi
 * @date 2024-05-23
 */
@RestController
@RequestMapping("/api/loanOrderInterestRecord")
public class ApiLoanOrderInterestRecordController extends BaseController
{
    @Autowired
    private ILoanOrderInterestRecordService loanOrderInterestRecordService;

    /**
     * 查询贷款订单利息生成记录列表
     */
    @GetMapping("/list")
    public TableDataInfo list(LoanOrderInterestRecord loanOrderInterestRecord)
    {
        if (loanOrderInterestRecord.getLoanOrderId() == null){
            throw new LangException(HintConstants.PARAM_NULL,"请选择需要查看利息明细的贷款订单");
        }
        startPage();
        startOrderBy("id desc");
        List<LoanOrderInterestRecord> list = loanOrderInterestRecordService.selectLoanOrderInterestRecordList(loanOrderInterestRecord);
        return getDataTable(list);
    }
}
