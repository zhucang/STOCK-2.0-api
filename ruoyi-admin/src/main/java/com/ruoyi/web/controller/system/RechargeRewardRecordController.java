package com.ruoyi.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.RechargeRewardRecord;
import com.ruoyi.system.service.IRechargeRewardRecordService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 充值奖励领取记录Controller
 * 
 * @author ruoyi
 * @date 2026-01-06
 */
@RestController
@RequestMapping("/system/rechargeRewardRecord")
public class RechargeRewardRecordController extends BaseController
{
    @Autowired
    private IRechargeRewardRecordService rechargeRewardRecordService;

    /**
     * 查询充值奖励领取记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:rechargeRewardRecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(RechargeRewardRecord rechargeRewardRecord)
    {
        startPage();
        List<RechargeRewardRecord> list = rechargeRewardRecordService.selectRechargeRewardRecordList(rechargeRewardRecord);
        return getDataTable(list);
    }

    /**
     * 导出充值奖励领取记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:rechargeRewardRecord:export')")
    @Log(title = "充值奖励领取记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RechargeRewardRecord rechargeRewardRecord)
    {
        List<RechargeRewardRecord> list = rechargeRewardRecordService.selectRechargeRewardRecordList(rechargeRewardRecord);
        ExcelUtil<RechargeRewardRecord> util = new ExcelUtil<RechargeRewardRecord>(RechargeRewardRecord.class);
        util.exportExcel(response, list, "充值奖励领取记录数据");
    }

    /**
     * 获取充值奖励领取记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:rechargeRewardRecord:query')")
    @GetMapping(value = "/{rechargeRewardRecordId}")
    public AjaxResult getInfo(@PathVariable("rechargeRewardRecordId") Long rechargeRewardRecordId)
    {
        return success(rechargeRewardRecordService.selectRechargeRewardRecordByRechargeRewardRecordId(rechargeRewardRecordId));
    }

    /**
     * 新增充值奖励领取记录
     */
    @PreAuthorize("@ss.hasPermi('system:rechargeRewardRecord:add')")
    @Log(title = "充值奖励领取记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RechargeRewardRecord rechargeRewardRecord)
    {
        return toAjax(rechargeRewardRecordService.insertRechargeRewardRecord(rechargeRewardRecord));
    }

    /**
     * 修改充值奖励领取记录
     */
    @PreAuthorize("@ss.hasPermi('system:rechargeRewardRecord:edit')")
    @Log(title = "充值奖励领取记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RechargeRewardRecord rechargeRewardRecord)
    {
        return toAjax(rechargeRewardRecordService.updateRechargeRewardRecord(rechargeRewardRecord));
    }

    /**
     * 删除充值奖励领取记录
     */
    @PreAuthorize("@ss.hasPermi('system:rechargeRewardRecord:remove')")
    @Log(title = "充值奖励领取记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{rechargeRewardRecordIds}")
    public AjaxResult remove(@PathVariable Long[] rechargeRewardRecordIds)
    {
        return toAjax(rechargeRewardRecordService.deleteRechargeRewardRecordByRechargeRewardRecordIds(rechargeRewardRecordIds));
    }
}
