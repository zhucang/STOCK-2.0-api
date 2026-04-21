package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.logDict.CustomerServiceLogDict;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.MimeTypeUtils;
import com.ruoyi.system.domain.CustomerService;
import com.ruoyi.system.service.ICustomerServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 客服配置Controller
 * 
 * @author ruoyi
 * @date 2023-11-04
 * 日志已优化
 */
@RestController
@RequestMapping("/system/customerService")
public class CustomerServiceController extends BaseController
{
    @Autowired
    private ICustomerServiceService customerServiceService;

    /**
     * 查询客服配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:customerService:list')")
    @GetMapping("/list")
    public TableDataInfo list(CustomerService customerService)
    {
        startPage();
        List<CustomerService> list = customerServiceService.selectCustomerServiceList(customerService);
        return getDataTable(list);
    }

    /**
     * 获取客服配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:customerService:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(customerServiceService.selectCustomerServiceById(id));
    }

    /**
     * 新增客服配置
     */
    @PreAuthorize("@ss.hasPermi('system:customerService:add')")
    @Log(title = "新增客服配置", businessType = BusinessType.INSERT,dict = CustomerServiceLogDict.class)
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody CustomerService customerService)
    {
        if (customerService.getCustomerServiceName() == null){
            throw new ServiceException("请输入客服热线名称");
        }
        if (customerService.getCustomerServiceLine() == null){
            throw new ServiceException("请输入客服热线链接");
        }
        return toAjax(customerServiceService.insertCustomerService(customerService));
    }

    /**
     * 修改客服配置
     */
    @PreAuthorize("@ss.hasPermi('system:customerService:edit')")
    @Log(title = "修改客服配置", businessType = BusinessType.UPDATE,dict = CustomerServiceLogDict.class)
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody CustomerService customerService)
    {
        if (customerService.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (customerService.getCustomerServiceName() == null){
            throw new ServiceException("请输入客服热线名称");
        }
        if (customerService.getCustomerServiceLine() == null){
            throw new ServiceException("请输入客服热线链接");
        }
        return toAjax(customerServiceService.updateCustomerService(customerService));
    }

    /**
     * 修改客服名称多语言配置
     */
    @PreAuthorize("@ss.hasPermi('system:customerService:edit')")
    @Log(title = "修改客服名称多语言", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping(value = "updateCustomerServiceNameLang")
    public AjaxResult updateCustomerServiceNameLang(@RequestBody CustomerService customerService)
    {
        if (customerService.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (customerService.getCustomerServiceNameLang().getZh() == null){
            throw new ServiceException("请输入客服热线名称");
        }
        return toAjax(customerServiceService.updateCustomerServiceNameLang(customerService.getId(),customerService.getCustomerServiceNameLang()));
    }

    /**
     * 删除客服配置
     */
    @PreAuthorize("@ss.hasPermi('system:customerService:remove')")
    @Log(title = "删除客服配置", businessType = BusinessType.DELETE,dict = CustomerServiceLogDict.class)
    @RepeatSubmit
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(customerServiceService.deleteCustomerServiceByIds(ids));
    }

    /**
     * 上传自定义客服文件
     */
    @PostMapping("/uploadCustomCustomerServiceFile")
    public AjaxResult uploadCustomCustomerServiceFile(MultipartFile file)
    {
        try {
            //文件名称
            String fileName = FileUploadUtils.upload("C:/web-vue/customerService", file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION,1);
            //访问路径
            fileName = "/customerService/" + fileName;
            return AjaxResult.success().put("fileName", fileName);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }
}
