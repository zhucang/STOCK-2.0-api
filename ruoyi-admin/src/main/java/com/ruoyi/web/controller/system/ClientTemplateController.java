package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.ClientTemplate;
import com.ruoyi.system.service.IClientTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 客户端模板管理Controller
 * 
 * @author ruoyi
 * @date 2024-01-08
 */
@RestController
@RequestMapping("/system/clientTemplate")
public class ClientTemplateController extends BaseController
{
    @Autowired
    private IClientTemplateService clientTemplateService;

    /**
     * 查询客户端模板管理列表
     */
    @GetMapping("/list")
    public TableDataInfo list(ClientTemplate clientTemplate)
    {
        startPage();
        List<ClientTemplate> list = clientTemplateService.selectClientTemplateList(clientTemplate);
        return getDataTable(list);
    }

    /**
     * 导出客户端模板管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:clientTemplate:export')")
    @Log(title = "导出客户端模板管理列表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ClientTemplate clientTemplate)
    {
        List<ClientTemplate> list = clientTemplateService.selectClientTemplateList(clientTemplate);
        ExcelUtil<ClientTemplate> util = new ExcelUtil<ClientTemplate>(ClientTemplate.class);
        util.exportExcel(response, list, "客户端模板管理数据");
    }

    /**
     * 获取客户端模板管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:clientTemplate:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(clientTemplateService.selectClientTemplateById(id));
    }

    /**
     * 新增客户端模板管理
     */
    @PreAuthorize("@ss.hasPermi('system:clientTemplate:add')")
    @Log(title = "新增客户端模板管理", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody ClientTemplate clientTemplate)
    {
        if (StringUtils.isEmpty(clientTemplate.getTemplateName())){
            throw new ServiceException("请输入客户端模板名称");
        }
        if (StringUtils.isEmpty(clientTemplate.getFileStoragePath())){
            throw new ServiceException("请输入客户端文件存放路径");
        }
        return toAjax(clientTemplateService.insertClientTemplate(clientTemplate));
    }

    /**
     * 修改客户端模板管理
     */
    @PreAuthorize("@ss.hasPermi('system:clientTemplate:edit')")
    @Log(title = "修改客户端模板管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody ClientTemplate clientTemplate)
    {
        if (clientTemplate.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (StringUtils.isEmpty(clientTemplate.getTemplateName())){
            throw new ServiceException("请输入客户端模板名称");
        }
        if (StringUtils.isEmpty(clientTemplate.getFileStoragePath())){
            throw new ServiceException("请输入客户端文件存放路径");
        }
        return toAjax(clientTemplateService.updateClientTemplate(clientTemplate));
    }

    /**
     * 删除客户端模板管理
     */
    @PreAuthorize("@ss.hasPermi('system:clientTemplate:remove')")
    @Log(title = "删除客户端模板管理", businessType = BusinessType.DELETE)
    @RepeatSubmit
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(clientTemplateService.deleteClientTemplateByIds(ids));
    }
}
