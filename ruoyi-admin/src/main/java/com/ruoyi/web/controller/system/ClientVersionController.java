package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.ClientVersion;
import com.ruoyi.system.service.IClientVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 客户端版本管理Controller
 * 
 * @author ruoyi
 * @date 2024-01-08
 */
@RestController
@RequestMapping("/system/clientVersion")
public class ClientVersionController extends BaseController
{
    @Autowired
    private IClientVersionService clientVersionService;

    /**
     * 查询客户端版本管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:clientVersion:list')")
    @GetMapping("/list")
    public TableDataInfo list(ClientVersion clientVersion)
    {
        startPage();
        startOrderBy("client_template_id,id desc");
        List<ClientVersion> list = clientVersionService.selectClientVersionList(clientVersion);
        return getDataTable(list);
    }

    /**
     * 获取客户端版本管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:clientVersion:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(clientVersionService.selectClientVersionById(id));
    }

    /**
     * 新增客户端版本管理
     */
    @PreAuthorize("@ss.hasPermi('system:clientVersion:add')")
    @Log(title = "新增客户端版本管理", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody ClientVersion clientVersion)
    {
        if (clientVersion.getClientTemplateId() == null){
            throw new ServiceException("请选择客户端模板");
        }
        if (StringUtils.isEmpty(clientVersion.getCompressedPackageNameH5())){
            throw new ServiceException("请上传h5压缩包");
        }
//        if (StringUtils.isEmpty(clientVersion.getCompressedPackageNameApp())){
//            throw new ServiceException("请上传app压缩包");
//        }
        return toAjax(clientVersionService.insertClientVersion(clientVersion));
    }

    /**
     * 更新客户端版本版本
     */
    @PreAuthorize("@ss.hasPermi('system:clientVersion:updateVersion')")
    @Log(title = "更新客户端版本", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping(value = "updateVersion")
    public AjaxResult updateVersion(Long clientVersionId)
    {
        if (clientVersionId == null){
            throw new ServiceException("请选择需要更新的版本");
        }
        return toAjax(clientVersionService.updateVersion(clientVersionId));
    }

    /**
     * 修改客户端版本管理
     */
    @PreAuthorize("@ss.hasPermi('system:clientVersion:edit')")
    @Log(title = "修改客户端版本管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody ClientVersion clientVersion)
    {
        if (clientVersion.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (StringUtils.isEmpty(clientVersion.getCompressedPackageNameH5())){
            throw new ServiceException("请上传h5压缩包");
        }
//        if (StringUtils.isEmpty(clientVersion.getCompressedPackageNameApp())){
//            throw new ServiceException("请上传app压缩包");
//        }
        if (clientVersion.getClientTemplateId() == null){
            throw new ServiceException("请选择客户端模板");
        }
        if (StringUtils.isEmpty(clientVersion.getCompressedPackageNameH5())){
            throw new ServiceException("请上传h5压缩包");
        }
//        if (StringUtils.isEmpty(clientVersion.getCompressedPackageNameApp())){
//            throw new ServiceException("请上传app压缩包");
//        }
        return toAjax(clientVersionService.updateClientVersion(clientVersion));
    }

    /**
     * 删除客户端版本管理
     */
    @PreAuthorize("@ss.hasPermi('system:clientVersion:remove')")
    @Log(title = "删除客户端版本管理", businessType = BusinessType.DELETE)
    @RepeatSubmit
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(clientVersionService.deleteClientVersionByIds(ids));
    }
}
