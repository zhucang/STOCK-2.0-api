package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.WebMenuConfig;
import com.ruoyi.system.service.IWebMenuConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 官网菜单配置Controller
 * 
 * @author ruoyi
 * @date 2023-12-11
 */
@RestController
@RequestMapping("/system/webMenuConfig")
public class WebMenuConfigController extends BaseController
{
    @Autowired
    private IWebMenuConfigService webMenuConfigService;

    /**
     * 查询官网菜单配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:webMenuConfig:list')")
    @GetMapping("/list")
    public TableDataInfo list(WebMenuConfig webMenuConfig)
    {
        startPage();
        startOrderBy("sort is null,sort");
        List<WebMenuConfig> list = webMenuConfigService.selectWebMenuConfigList(webMenuConfig);
        return getDataTable(list);
    }

    /**
     * 导出官网菜单配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:webMenuConfig:export')")
    @Log(title = "官网菜单配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WebMenuConfig webMenuConfig)
    {
        List<WebMenuConfig> list = webMenuConfigService.selectWebMenuConfigList(webMenuConfig);
        ExcelUtil<WebMenuConfig> util = new ExcelUtil<WebMenuConfig>(WebMenuConfig.class);
        util.exportExcel(response, list, "官网菜单配置数据");
    }

    /**
     * 获取官网菜单配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:webMenuConfig:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(webMenuConfigService.selectWebMenuConfigById(id));
    }

    /**
     * 新增官网菜单配置
     */
    @PreAuthorize("@ss.hasPermi('system:webMenuConfig:add')")
    @Log(title = "官网菜单配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WebMenuConfig webMenuConfig)
    {
        return toAjax(webMenuConfigService.insertWebMenuConfig(webMenuConfig));
    }

    /**
     * 修改官网菜单配置
     */
    @PreAuthorize("@ss.hasPermi('system:webMenuConfig:edit')")
    @Log(title = "官网菜单配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WebMenuConfig webMenuConfig)
    {
        return toAjax(webMenuConfigService.updateWebMenuConfig(webMenuConfig));
    }

    /**
     * 删除官网菜单配置
     */
    @PreAuthorize("@ss.hasPermi('system:webMenuConfig:remove')")
    @Log(title = "官网菜单配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(webMenuConfigService.deleteWebMenuConfigByIds(ids));
    }
}
