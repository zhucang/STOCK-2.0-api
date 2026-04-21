package com.ruoyi.web.controller.system;

import com.github.pagehelper.util.StringUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.LangMgr;
import com.ruoyi.system.service.ILangMgrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 多语言配置包Controller
 * 
 * @author ruoyi
 * @date 2023-11-07
 */
@RestController
@RequestMapping("/system/langMgr")
public class LangMgrController extends BaseController
{
    @Autowired
    private ILangMgrService langMgrService;

    /**
     * 查询多语言配置包列表
     */
    @PreAuthorize("@ss.hasPermi('system:langMgr:list')")
    @GetMapping("/list")
    public TableDataInfo list(LangMgr langMgr)
    {
        startPage();
        startOrderBy("id desc");
        List<LangMgr> list = langMgrService.selectLangMgrList(langMgr);
        return getDataTable(list);
    }

    /**
     * 导出多语言配置包列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, LangMgr langMgr)
    {
        List<LangMgr> list = langMgrService.selectLangMgrList(langMgr);
        ExcelUtil<LangMgr> util = new ExcelUtil<LangMgr>(LangMgr.class);
        util.exportExcel(response, list, "多语言配置包数据");
    }

    /**
     * 导入语言包
     */
    @PreAuthorize("@ss.hasPermi('system:langMgr:import')")
    @Log(title = "导入语言包", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping("/import")
    public AjaxResult importData(@RequestParam("file") MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<LangMgr> util = new ExcelUtil<LangMgr>(LangMgr.class);
        List<LangMgr> list = util.importExcel(file.getInputStream());
        long count = list.stream().filter(a -> StringUtils.isEmpty(a.getLangKey()) || StringUtils.isEmpty(a.getZh())).count();
        if (count > 0){
            throw new ServiceException("key和中文不允许为空");
        }
        String message = langMgrService.importLangMgr(list, updateSupport);
        return AjaxResult.success(message);
    }

    /**
     * 获取多语言配置包详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:langMgr:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(langMgrService.selectLangMgrById(id));
    }

    /**
     * 新增多语言配置包
     */
    @PreAuthorize("@ss.hasPermi('system:langMgr:add')")
    @Log(title = "新增多语言配置", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody LangMgr langMgr)
    {
        if (StringUtils.isEmpty(langMgr.getLangKey())){
            throw new ServiceException("请输入多语言key");
        }
        if (StringUtils.isEmpty(langMgr.getZh())){
            throw new ServiceException("请输入中文内容");
        }
        return toAjax(langMgrService.insertLangMgr(langMgr));
    }

    /**
     * 修改多语言配置包
     */
    @PreAuthorize("@ss.hasPermi('system:langMgr:edit')")
    @Log(title = "修改多语言配置", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody LangMgr langMgr)
    {
        if (langMgr.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (StringUtils.isEmpty(langMgr.getLangKey())){
            throw new ServiceException("请输入多语言key");
        }
        if (StringUtils.isEmpty(langMgr.getZh())){
            throw new ServiceException("请输入中文内容");
        }
        return toAjax(langMgrService.updateLangMgr(langMgr));
    }

    /**
     * 批量替换多语言
     */
    @PreAuthorize("@ss.hasPermi('system:langMgr:edit')")
    @Log(title = "批量替换多语言", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping("/batchReplaceLangValue")
    public AjaxResult batchReplaceLangValue(String from, String to)
    {
        if (StringUtil.isEmpty(from)){
            throw new ServiceException("请输入需要替换的内容");
        }
        if (StringUtil.isEmpty(to)){
            throw new ServiceException("请输入需要被替换的内容");
        }
        return toAjax(langMgrService.batchReplaceLangValue(from,to));
    }

    /**
     * 删除多语言配置包
     */
    @PreAuthorize("@ss.hasPermi('system:langMgr:remove')")
    @Log(title = "删除多语言配置", businessType = BusinessType.DELETE)
    @RepeatSubmit
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(langMgrService.deleteLangMgrByIds(ids));
    }
}
