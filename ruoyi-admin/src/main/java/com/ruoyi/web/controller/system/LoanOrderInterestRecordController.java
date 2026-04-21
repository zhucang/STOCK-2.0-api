package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.LoanOrderInterestRecord;
import com.ruoyi.system.service.ILoanOrderInterestRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 贷款订单利息生成记录Controller
 * 
 * @author ruoyi
 * @date 2024-05-23
 */
@RestController
@RequestMapping("/system/loanOrderInterestRecord")
public class LoanOrderInterestRecordController extends BaseController
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
            throw new ServiceException("请选择需要查看利息明细的贷款订单");
        }
        startPage();
        startOrderBy("id desc");
        List<LoanOrderInterestRecord> list = loanOrderInterestRecordService.selectLoanOrderInterestRecordList(loanOrderInterestRecord);
        return getDataTable(list);
    }

    /**
     * 导出贷款订单利息生成记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:loanOrderInterestRecord:export')")
    @Log(title = "贷款订单利息生成记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LoanOrderInterestRecord loanOrderInterestRecord)
    {
        List<LoanOrderInterestRecord> list = loanOrderInterestRecordService.selectLoanOrderInterestRecordList(loanOrderInterestRecord);
        ExcelUtil<LoanOrderInterestRecord> util = new ExcelUtil<LoanOrderInterestRecord>(LoanOrderInterestRecord.class);
        util.exportExcel(response, list, "贷款订单利息生成记录数据");
    }

    /**
     * 获取贷款订单利息生成记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:loanOrderInterestRecord:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(loanOrderInterestRecordService.selectLoanOrderInterestRecordById(id));
    }

    /**
     * 新增贷款订单利息生成记录
     */
    @PreAuthorize("@ss.hasPermi('system:loanOrderInterestRecord:add')")
    @Log(title = "贷款订单利息生成记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LoanOrderInterestRecord loanOrderInterestRecord)
    {
        return toAjax(loanOrderInterestRecordService.insertLoanOrderInterestRecord(loanOrderInterestRecord));
    }

    /**
     * 修改贷款订单利息生成记录
     */
    @PreAuthorize("@ss.hasPermi('system:loanOrderInterestRecord:edit')")
    @Log(title = "贷款订单利息生成记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LoanOrderInterestRecord loanOrderInterestRecord)
    {
        return toAjax(loanOrderInterestRecordService.updateLoanOrderInterestRecord(loanOrderInterestRecord));
    }

    /**
     * 删除贷款订单利息生成记录
     */
    @PreAuthorize("@ss.hasPermi('system:loanOrderInterestRecord:remove')")
    @Log(title = "贷款订单利息生成记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(loanOrderInterestRecordService.deleteLoanOrderInterestRecordByIds(ids));
    }
}
