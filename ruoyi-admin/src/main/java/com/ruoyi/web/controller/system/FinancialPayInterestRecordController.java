package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.FinancialPayInterestRecord;
import com.ruoyi.system.service.IFinancialPayInterestRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 理财产品派息记录Controller
 * 
 * @author ruoyi
 * @date 2023-12-10
 */
@RestController
@RequestMapping("/system/financialPayInterestRecord")
public class FinancialPayInterestRecordController extends BaseController
{
    @Autowired
    private IFinancialPayInterestRecordService financialPayInterestRecordService;

    /**
     * 查询理财产品派息记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:financialPayInterestRecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(FinancialPayInterestRecord financialPayInterestRecord)
    {
        startPage();
        List<FinancialPayInterestRecord> list = financialPayInterestRecordService.selectFinancialPayInterestRecordList(financialPayInterestRecord);
        return getDataTable(list);
    }

//    /**
//     * 导出理财产品派息记录列表
//     */
//    @PreAuthorize("@ss.hasPermi('system:financialPayInterestRecord:export')")
//    @Log(title = "理财产品派息记录", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, FinancialPayInterestRecord financialPayInterestRecord)
//    {
//        List<FinancialPayInterestRecord> list = financialPayInterestRecordService.selectFinancialPayInterestRecordList(financialPayInterestRecord);
//        ExcelUtil<FinancialPayInterestRecord> util = new ExcelUtil<FinancialPayInterestRecord>(FinancialPayInterestRecord.class);
//        util.exportExcel(response, list, "理财产品派息记录数据");
//    }
//
//    /**
//     * 获取理财产品派息记录详细信息
//     */
//    @PreAuthorize("@ss.hasPermi('system:financialPayInterestRecord:query')")
//    @GetMapping(value = "/{id}")
//    public AjaxResult getInfo(@PathVariable("id") Long id)
//    {
//        return success(financialPayInterestRecordService.selectFinancialPayInterestRecordById(id));
//    }
//
//    /**
//     * 新增理财产品派息记录
//     */
//    @PreAuthorize("@ss.hasPermi('system:financialPayInterestRecord:add')")
//    @Log(title = "理财产品派息记录", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody FinancialPayInterestRecord financialPayInterestRecord)
//    {
//        return toAjax(financialPayInterestRecordService.insertFinancialPayInterestRecord(financialPayInterestRecord));
//    }
//
//    /**
//     * 修改理财产品派息记录
//     */
//    @PreAuthorize("@ss.hasPermi('system:financialPayInterestRecord:edit')")
//    @Log(title = "理财产品派息记录", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody FinancialPayInterestRecord financialPayInterestRecord)
//    {
//        return toAjax(financialPayInterestRecordService.updateFinancialPayInterestRecord(financialPayInterestRecord));
//    }
//
//    /**
//     * 删除理财产品派息记录
//     */
//    @PreAuthorize("@ss.hasPermi('system:financialPayInterestRecord:remove')")
//    @Log(title = "理财产品派息记录", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        return toAjax(financialPayInterestRecordService.deleteFinancialPayInterestRecordByIds(ids));
//    }
}
