package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.logDict.BankPropertyConfigLogDict;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.BankPropertyConfig;
import com.ruoyi.system.service.IBankPropertyConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 银行卡参数字段配置Controller
 * 
 * @author ruoyi
 * @date 2023-11-23
 * 日志已优化
 */
@RestController
@RequestMapping("/system/bankPropertyConfig")
public class BankPropertyConfigController extends BaseController
{
    @Autowired
    private IBankPropertyConfigService bankPropertyConfigService;

    /**
     * 查询银行卡参数字段配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:bankPropertyConfig:list')")
    @GetMapping("/list")
    public TableDataInfo list(BankPropertyConfig bankPropertyConfig)
    {
        startPage();
        startOrderBy("sort is null,sort");
        List<BankPropertyConfig> list = bankPropertyConfigService.selectBankPropertyConfigList(bankPropertyConfig);
        return getDataTable(list);
    }

    /**
     * 获取银行卡参数字段配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:bankPropertyConfig:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(bankPropertyConfigService.selectBankPropertyConfigById(id));
    }

    /**
     * 新增银行卡参数字段配置
     */
    @PreAuthorize("@ss.hasPermi('system:bankPropertyConfig:add')")
    @Log(title = "新增银行卡参数字段配置", businessType = BusinessType.INSERT,dict = BankPropertyConfigLogDict.class)
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody BankPropertyConfig bankPropertyConfig)
    {
        if (StringUtils.isEmpty(bankPropertyConfig.getPropertyName())){
            throw new ServiceException("请输入字段名称");
        }
        if (StringUtils.isEmpty(bankPropertyConfig.getPropertyDesc())){
            throw new ServiceException("请输入字段详情");
        }
        return toAjax(bankPropertyConfigService.insertBankPropertyConfig(bankPropertyConfig));
    }

    /**
     * 修改银行卡参数字段配置
     */
    @PreAuthorize("@ss.hasPermi('system:bankPropertyConfig:edit')")
    @Log(title = "修改银行卡参数字段配置", businessType = BusinessType.UPDATE,dict = BankPropertyConfigLogDict.class)
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody BankPropertyConfig bankPropertyConfig)
    {
        if (bankPropertyConfig.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (StringUtils.isEmpty(bankPropertyConfig.getPropertyName())){
            throw new ServiceException("请输入字段名称");
        }
        if (StringUtils.isEmpty(bankPropertyConfig.getPropertyDesc())){
            throw new ServiceException("请输入字段详情");
        }
        return toAjax(bankPropertyConfigService.updateBankPropertyConfig(bankPropertyConfig));
    }

    /**
     * 删除银行卡参数字段配置
     */
    @PreAuthorize("@ss.hasPermi('system:bankPropertyConfig:remove')")
    @Log(title = "删除银行卡参数字段配置", businessType = BusinessType.DELETE,dict = BankPropertyConfigLogDict.class)
    @RepeatSubmit
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(bankPropertyConfigService.deleteBankPropertyConfigByIds(ids));
    }
}
