package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.StakingOrderInterestRecord;
import com.ruoyi.system.service.IStakingOrderInterestRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 质押订单派息记录Controller
 * 
 * @author ruoyi
 * @date 2025-07-20
 */
@RestController
@RequestMapping("/system/stakingOrderInterestRecord")
public class StakingOrderInterestRecordController extends BaseController
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
            throw new ServiceException("请选择相应的质押订单");
        }
        List<StakingOrderInterestRecord> list = stakingOrderInterestRecordService.selectStakingOrderInterestRecordList(stakingOrderInterestRecord);
        return getDataTable(list);
    }

    /**
     * 导出质押订单派息记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:stakingOrderInterestRecord:export')")
    @Log(title = "质押订单派息记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StakingOrderInterestRecord stakingOrderInterestRecord)
    {
        List<StakingOrderInterestRecord> list = stakingOrderInterestRecordService.selectStakingOrderInterestRecordList(stakingOrderInterestRecord);
        ExcelUtil<StakingOrderInterestRecord> util = new ExcelUtil<StakingOrderInterestRecord>(StakingOrderInterestRecord.class);
        util.exportExcel(response, list, "质押订单派息记录数据");
    }

    /**
     * 获取质押订单派息记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:stakingOrderInterestRecord:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(stakingOrderInterestRecordService.selectStakingOrderInterestRecordById(id));
    }

    /**
     * 新增质押订单派息记录
     */
    @PreAuthorize("@ss.hasPermi('system:stakingOrderInterestRecord:add')")
    @Log(title = "质押订单派息记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StakingOrderInterestRecord stakingOrderInterestRecord)
    {
        return toAjax(stakingOrderInterestRecordService.insertStakingOrderInterestRecord(stakingOrderInterestRecord));
    }

    /**
     * 修改质押订单派息记录
     */
    @PreAuthorize("@ss.hasPermi('system:stakingOrderInterestRecord:edit')")
    @Log(title = "质押订单派息记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StakingOrderInterestRecord stakingOrderInterestRecord)
    {
        return toAjax(stakingOrderInterestRecordService.updateStakingOrderInterestRecord(stakingOrderInterestRecord));
    }

    /**
     * 删除质押订单派息记录
     */
    @PreAuthorize("@ss.hasPermi('system:stakingOrderInterestRecord:remove')")
    @Log(title = "质押订单派息记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(stakingOrderInterestRecordService.deleteStakingOrderInterestRecordByIds(ids));
    }
}
