package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.RechargeRewardTier;
import com.ruoyi.system.service.IRechargeRewardTierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 充值奖励层级配置Controller
 * 
 * @author ruoyi
 * @date 2026-01-06
 */
@RestController
@RequestMapping("/system/rechargeRewardTier")
public class RechargeRewardTierController extends BaseController
{
    @Autowired
    private IRechargeRewardTierService rechargeRewardTierService;

    /**
     * 查询充值奖励层级配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:rechargeRewardTier:list')")
    @GetMapping("/list")
    public TableDataInfo list(RechargeRewardTier rechargeRewardTier)
    {
        startPage();
        List<RechargeRewardTier> list = rechargeRewardTierService.selectRechargeRewardTierList(rechargeRewardTier);
        return getDataTable(list);
    }

    /**
     * 导出充值奖励层级配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:rechargeRewardTier:export')")
    @Log(title = "充值奖励层级配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RechargeRewardTier rechargeRewardTier)
    {
        List<RechargeRewardTier> list = rechargeRewardTierService.selectRechargeRewardTierList(rechargeRewardTier);
        ExcelUtil<RechargeRewardTier> util = new ExcelUtil<RechargeRewardTier>(RechargeRewardTier.class);
        util.exportExcel(response, list, "充值奖励层级配置数据");
    }

    /**
     * 获取充值奖励层级配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:rechargeRewardTier:query')")
    @GetMapping(value = "/{rechargeRewardTierId}")
    public AjaxResult getInfo(@PathVariable("rechargeRewardTierId") Long rechargeRewardTierId)
    {
        return success(rechargeRewardTierService.selectRechargeRewardTierByRechargeRewardTierId(rechargeRewardTierId));
    }

    /**
     * 新增充值奖励层级配置
     */
    @PreAuthorize("@ss.hasPermi('system:rechargeRewardTier:add')")
    @Log(title = "充值奖励层级配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RechargeRewardTier rechargeRewardTier)
    {
        return toAjax(rechargeRewardTierService.insertRechargeRewardTier(rechargeRewardTier));
    }

    /**
     * 修改充值奖励层级配置
     */
    @PreAuthorize("@ss.hasPermi('system:rechargeRewardTier:edit')")
    @Log(title = "充值奖励层级配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RechargeRewardTier rechargeRewardTier)
    {
        return toAjax(rechargeRewardTierService.updateRechargeRewardTier(rechargeRewardTier));
    }

    /**
     * 删除充值奖励层级配置
     */
    @PreAuthorize("@ss.hasPermi('system:rechargeRewardTier:remove')")
    @Log(title = "充值奖励层级配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{rechargeRewardTierIds}")
    public AjaxResult remove(@PathVariable Long[] rechargeRewardTierIds)
    {
        return toAjax(rechargeRewardTierService.deleteRechargeRewardTierByRechargeRewardTierIds(rechargeRewardTierIds));
    }
}
