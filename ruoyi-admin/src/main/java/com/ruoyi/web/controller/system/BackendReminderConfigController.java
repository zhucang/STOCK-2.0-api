package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.BackendReminderConfig;
import com.ruoyi.system.service.IBackendReminderConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台提醒配置Controller
 * 
 * @author ruoyi
 * @date 2024-07-07
 */
@RestController
@RequestMapping("/system/backendReminderConfig")
public class BackendReminderConfigController extends BaseController
{
    @Autowired
    private IBackendReminderConfigService backendReminderConfigService;

    /**
     * 查询后台提醒配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:backendReminderConfig:list')")
    @GetMapping("/list")
    public TableDataInfo list(BackendReminderConfig backendReminderConfig)
    {
        startPage();
        List<BackendReminderConfig> list = backendReminderConfigService.selectBackendReminderConfigList(backendReminderConfig);
        return getDataTable(list);
    }

    /**
     * 获取后台提醒配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:backendReminderConfig:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(backendReminderConfigService.selectBackendReminderConfigById(id));
    }

    /**
     * 修改后台提醒配置
     */
    @PreAuthorize("@ss.hasPermi('system:backendReminderConfig:edit')")
    @Log(title = "后台提醒配置", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody BackendReminderConfig backendReminderConfig)
    {
        return toAjax(backendReminderConfigService.updateBackendReminderConfig(backendReminderConfig));
    }
}
