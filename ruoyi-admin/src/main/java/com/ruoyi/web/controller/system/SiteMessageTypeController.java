package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SiteMessageType;
import com.ruoyi.system.service.ISiteMessageTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 站内信类型Controller
 * 
 * @author ruoyi
 * @date 2026-04-12
 */
@RestController
@RequestMapping("/system/siteMessageType")
public class SiteMessageTypeController extends BaseController
{
    @Autowired
    private ISiteMessageTypeService siteMessageTypeService;

    /**
     * 查询站内信类型列表
     */
    @PreAuthorize("@ss.hasPermi('system:siteMessageType:list')")
    @GetMapping("/list")
    public TableDataInfo list(SiteMessageType siteMessageType)
    {
        startPage();
        List<SiteMessageType> list = siteMessageTypeService.selectSiteMessageTypeList(siteMessageType);
        return getDataTable(list);
    }

    /**
     * 查询站内信类型列表
     */
    @GetMapping("/listNoPreAuthorize")
    public TableDataInfo listNoPreAuthorize(SiteMessageType siteMessageType)
    {
        startPage();
        List<SiteMessageType> list = siteMessageTypeService.selectSiteMessageTypeList(siteMessageType);
        return getDataTable(list);
    }

    /**
     * 导出站内信类型列表
     */
    @PreAuthorize("@ss.hasPermi('system:siteMessageType:export')")
    @Log(title = "站内信类型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SiteMessageType siteMessageType)
    {
        List<SiteMessageType> list = siteMessageTypeService.selectSiteMessageTypeList(siteMessageType);
        ExcelUtil<SiteMessageType> util = new ExcelUtil<SiteMessageType>(SiteMessageType.class);
        util.exportExcel(response, list, "站内信类型数据");
    }

    /**
     * 获取站内信类型详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:siteMessageType:query')")
    @GetMapping(value = "/{siteMessageTypeId}")
    public AjaxResult getInfo(@PathVariable("siteMessageTypeId") Long siteMessageTypeId)
    {
        return success(siteMessageTypeService.selectSiteMessageTypeBySiteMessageTypeId(siteMessageTypeId));
    }

    /**
     * 新增站内信类型
     */
    @PreAuthorize("@ss.hasPermi('system:siteMessageType:add')")
    @Log(title = "站内信类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SiteMessageType siteMessageType)
    {
        return toAjax(siteMessageTypeService.insertSiteMessageType(siteMessageType));
    }

    /**
     * 修改站内信类型
     */
    @PreAuthorize("@ss.hasPermi('system:siteMessageType:edit')")
    @Log(title = "站内信类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SiteMessageType siteMessageType)
    {
        return toAjax(siteMessageTypeService.updateSiteMessageType(siteMessageType));
    }

    /**
     * 删除站内信类型
     */
    @PreAuthorize("@ss.hasPermi('system:siteMessageType:remove')")
    @Log(title = "站内信类型", businessType = BusinessType.DELETE)
	@DeleteMapping("/{siteMessageTypeIds}")
    public AjaxResult remove(@PathVariable Long[] siteMessageTypeIds)
    {
        return toAjax(siteMessageTypeService.deleteSiteMessageTypeBySiteMessageTypeIds(siteMessageTypeIds));
    }
}
