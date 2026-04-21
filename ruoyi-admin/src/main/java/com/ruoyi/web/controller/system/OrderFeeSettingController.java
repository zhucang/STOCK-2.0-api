package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.OrderFeeSetting;
import com.ruoyi.system.service.IOrderFeeSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 产品买入卖出手续费配置Controller
 * 
 * @author ruoyi
 * @date 2023-11-27
 */
@RestController
@RequestMapping("/system/orderFeeSetting")
public class OrderFeeSettingController extends BaseController
{
    @Autowired
    private IOrderFeeSettingService orderFeeSettingService;

    /**
     * 查询产品买入卖出手续费配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:orderFeeSetting:list')")
    @GetMapping("/list")
    public TableDataInfo list(OrderFeeSetting orderFeeSetting)
    {
        startPage();
        List<OrderFeeSetting> list = orderFeeSettingService.selectOrderFeeSettingList(orderFeeSetting);
        return getDataTable(list);
    }

    /**
     * 获取产品买入卖出手续费配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:orderFeeSetting:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(orderFeeSettingService.selectOrderFeeSettingById(id));
    }

    /**
     * 修改产品买入卖出手续费配置
     */
    @PreAuthorize("@ss.hasPermi('system:orderFeeSetting:edit')")
    @Log(title = "修改产品买入卖出手续费配置", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody OrderFeeSetting orderFeeSetting)
    {
        if (orderFeeSetting.getId() == null){
            return AjaxResult.error("请选择需要修改配置的选项");
        }
        if (orderFeeSetting.getFeeRate() != null){
            if (orderFeeSetting.getFeeRate().compareTo(BigDecimal.ZERO) == -1){
                return AjaxResult.error("手续费率不允许小于0");
            }
        }
        return toAjax(orderFeeSettingService.updateOrderFeeSetting(orderFeeSetting));
    }
}
