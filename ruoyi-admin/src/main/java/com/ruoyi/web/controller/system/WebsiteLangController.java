package com.ruoyi.web.controller.system;

import com.github.pagehelper.util.StringUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.WebsiteLang;
import com.ruoyi.system.service.IWebsiteLangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 官网多语言包Controller
 * 
 * @author ruoyi
 * @date 2023-12-09
 */
@RestController
@RequestMapping("/system/websiteLang")
public class WebsiteLangController extends BaseController
{
    @Autowired
    private IWebsiteLangService websiteLangService;

    /**
     * 查询官网多语言包列表
     */
    @PreAuthorize("@ss.hasPermi('system:websiteLang:list')")
    @GetMapping("/list")
    public TableDataInfo list(WebsiteLang websiteLang)
    {
        startPage();
        startOrderBy("id desc");
        List<WebsiteLang> list = websiteLangService.selectWebsiteLangList(websiteLang);
        return getDataTable(list);
    }

    /**
     * 导出官网多语言包列表
     */
    @Log(title = "官网多语言包", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WebsiteLang websiteLang)
    {
        List<WebsiteLang> list = websiteLangService.selectWebsiteLangList(websiteLang);
        ExcelUtil<WebsiteLang> util = new ExcelUtil<WebsiteLang>(WebsiteLang.class);
        util.exportExcel(response, list, "官网多语言包数据");
    }

    /**
     * 导入语言包
     */
    @PreAuthorize("@ss.hasPermi('system:websiteLang:import')")
    @PostMapping("/import")
    @RepeatSubmit
    public AjaxResult importData(@RequestParam("file") MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<WebsiteLang> util = new ExcelUtil<WebsiteLang>(WebsiteLang.class);
        List<WebsiteLang> list = util.importExcel(file.getInputStream());
        long count = list.stream().filter(a -> StringUtils.isEmpty(a.getLangKey()) || StringUtils.isEmpty(a.getZh())).count();
        if (count > 0){
            return AjaxResult.error("key和中文不允许为空");
        }
        String message = websiteLangService.importWebsiteLang(list, updateSupport);
        return AjaxResult.success(message);
    }

    /**
     * 获取官网多语言包详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:websiteLang:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(websiteLangService.selectWebsiteLangById(id));
    }

    /**
     * 新增官网多语言包
     */
    @PreAuthorize("@ss.hasPermi('system:websiteLang:add')")
    @Log(title = "官网多语言包", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WebsiteLang websiteLang)
    {
        if (StringUtils.isEmpty(websiteLang.getLangKey())){
            return AjaxResult.error("请输入多语言key");
        }
        if (StringUtils.isEmpty(websiteLang.getZh())){
            return AjaxResult.error("请输入中文内容");
        }
        return toAjax(websiteLangService.insertWebsiteLang(websiteLang));
    }

    /**
     * 修改官网多语言包
     */
    @PreAuthorize("@ss.hasPermi('system:websiteLang:edit')")
    @Log(title = "官网多语言包", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WebsiteLang websiteLang)
    {
        if (websiteLang.getId() == null){
            return AjaxResult.error("请选择需要修改的选项");
        }
        if (StringUtils.isEmpty(websiteLang.getLangKey())){
            return AjaxResult.error("请输入多语言key");
        }
        if (StringUtils.isEmpty(websiteLang.getZh())){
            return AjaxResult.error("请输入中文内容");
        }
        return toAjax(websiteLangService.updateWebsiteLang(websiteLang));
    }


    /**
     * 批量替换官网多语言
     */
    @PreAuthorize("@ss.hasPermi('system:websiteLang:edit')")
    @Log(title = "批量替换官网多语言", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping("/batchReplaceLangValue")
    public AjaxResult batchReplaceLangValue(String from, String to)
    {
        if (StringUtil.isEmpty(from)){
            return AjaxResult.error("请输入需要替换的内容");
        }
        if (StringUtil.isEmpty(to)){
            return AjaxResult.error("请输入需要被替换的内容");
        }
        return toAjax(websiteLangService.batchReplaceLangValue(from,to));
    }

    /**
     * 删除官网多语言包
     */
    @PreAuthorize("@ss.hasPermi('system:websiteLang:remove')")
    @Log(title = "官网多语言包", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(websiteLangService.deleteWebsiteLangByIds(ids));
    }
}
