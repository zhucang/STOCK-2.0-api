package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.logDict.SelfSellProductDailyDataConfigLogDict;
import com.ruoyi.system.domain.SelfSellProductDailyDataConfig;
import com.ruoyi.system.service.ISelfSellProductDailyDataConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 自营产品每日行情数据配置Controller
 * 
 * @author ruoyi
 * @date 2023-11-27
 */
@RestController
@RequestMapping("/system/selfSellProductDailyDataConfig")
public class SelfSellProductDailyDataConfigController extends BaseController
{
    @Autowired
    private ISelfSellProductDailyDataConfigService selfSellProductDailyDataConfigService;

    /**
     * 查询自营产品每日行情数据配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:selfSellProductDailyDataConfig:list')")
    @GetMapping("/list")
    public TableDataInfo list(SelfSellProductDailyDataConfig selfSellProductDailyDataConfig)
    {
        startPage();
        List<SelfSellProductDailyDataConfig> list = selfSellProductDailyDataConfigService.selectSelfSellProductDailyDataConfigList(selfSellProductDailyDataConfig);
        return getDataTable(list);
    }

    /**
     * 获取自营产品每日行情数据配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:selfSellProductDailyDataConfig:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(selfSellProductDailyDataConfigService.selectSelfSellProductDailyDataConfigById(id));
    }

    /**
     * 新增自营产品每日行情数据配置
     */
    @PreAuthorize("@ss.hasPermi('system:selfSellProductDailyDataConfig:add')")
    @Log(title = "新增自营产品每日行情数据配置", businessType = BusinessType.INSERT,dict = SelfSellProductDailyDataConfigLogDict.class)
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody SelfSellProductDailyDataConfig selfSellProductDailyDataConfig)
    {
        if (selfSellProductDailyDataConfig.getSelfSellProductId() == null){
            throw new ServiceException("请选择需要生成行情的产品");
        }
        if (selfSellProductDailyDataConfig.getFinallyChangeRate() == null){
            throw new ServiceException("请输入最终涨跌幅");
        }
        if (selfSellProductDailyDataConfig.getFinallyPrice() == null){
            throw new ServiceException("请输入最终价格");
        }
        if (selfSellProductDailyDataConfig.getIsDefault() == null){
            throw new ServiceException("请选择模板是否默认");
        }
        return selfSellProductDailyDataConfigService.insertSelfSellProductDailyDataConfig(selfSellProductDailyDataConfig);
    }

    /**
     * 修改自营产品每日行情数据配置
     */
    @PreAuthorize("@ss.hasPermi('system:selfSellProductDailyDataConfig:edit')")
    @Log(title = "修改自营产品每日行情数据配置", businessType = BusinessType.UPDATE,dict = SelfSellProductDailyDataConfigLogDict.class)
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody SelfSellProductDailyDataConfig selfSellProductDailyDataConfig)
    {
        if (selfSellProductDailyDataConfig.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (selfSellProductDailyDataConfig.getProductCode() == null){
            throw new ServiceException("请选择需要生成行情的产品");
        }
        if (selfSellProductDailyDataConfig.getFinallyChangeRate() == null){
            throw new ServiceException("请输入最终涨跌幅");
        }
        if (selfSellProductDailyDataConfig.getFinallyPrice() == null){
            throw new ServiceException("请输入最终价格");
        }
        if (selfSellProductDailyDataConfig.getIsDefault() == null){
            throw new ServiceException("请选择模板是否默认");
        }
        return selfSellProductDailyDataConfigService.updateSelfSellProductDailyDataConfig(selfSellProductDailyDataConfig);
    }

    /**
     * 删除自营产品每日行情数据配置
     */
    @PreAuthorize("@ss.hasPermi('system:selfSellProductDailyDataConfig:remove')")
    @Log(title = "删除自营产品每日行情数据配置", businessType = BusinessType.DELETE,dict = SelfSellProductDailyDataConfigLogDict.class)
    @RepeatSubmit
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(selfSellProductDailyDataConfigService.deleteSelfSellProductDailyDataConfigByIds(ids));
    }

    /**
     * 重新生成自营产品行情模板数据
     */
    @PreAuthorize("@ss.hasPermi('system:selfSellProductDailyDataConfig:regenerateRealtimeTempData')")
    @Log(title = "重新生成自营产品行情模板数据", businessType = BusinessType.UPDATE,dict = SelfSellProductDailyDataConfigLogDict.class)
    @RepeatSubmit
    @PostMapping("/regenerateRealtimeTempData")
    public AjaxResult regenerateRealtimeTempData(Long id)
    {
        return toAjax(selfSellProductDailyDataConfigService.regenerateRealtimeTempData(id));
    }
}
