package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.logDict.PlatformCurrencyLogDict;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.PlatformCurrency;
import com.ruoyi.system.service.IPlatformCurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 平台交易币种配置信息Controller
 * 
 * @author ruoyi
 * @date 2023-10-28
 */
@RestController
@RequestMapping("/system/platformCurrency")
public class PlatformCurrencyController extends BaseController
{
    @Autowired
    private IPlatformCurrencyService platformCurrencyService;

    /**
     * 查询平台交易币种配置信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:platformCurrency:list')")
    @GetMapping("/list")
    public TableDataInfo list(PlatformCurrency platformCurrency)
    {
        startPage();
        startOrderBy("sort is null,sort");
        List<PlatformCurrency> list = platformCurrencyService.selectPlatformCurrencyList(platformCurrency);
        return getDataTable(list);
    }

    /**
     * 查询平台交易币种配置信息列表(无权限)
     */
    @GetMapping("/listNoPreAuthorize")
    public TableDataInfo listNoPreAuthorize(PlatformCurrency platformCurrency)
    {
        startPage();
        platformCurrency.setStatus(0);
        startOrderBy("sort is null,sort");
        List<PlatformCurrency> list = platformCurrencyService.selectPlatformCurrencyList(platformCurrency);
        return getDataTable(list);
    }

    /**
     * 获取平台交易币种配置信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:platformCurrency:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(platformCurrencyService.selectPlatformCurrencyById(id));
    }

    /**
     * 新增平台交易币种配置信息
     */
    @PreAuthorize("@ss.hasPermi('system:platformCurrency:add')")
    @Log(title = "新增平台交易币种配置信息", businessType = BusinessType.INSERT,dict = PlatformCurrencyLogDict.class)
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody PlatformCurrency platformCurrency)
    {
        if (StringUtils.isEmpty(platformCurrency.getCurrencyName())){
            throw new ServiceException("请输入币种名称");
        }
        if (StringUtils.isEmpty(platformCurrency.getCurrencyDesc())){
            throw new ServiceException("请输入币种描述");
        }
        if (platformCurrency.getCurrencyType() == null){
            throw new ServiceException("请选择币种类型");
        }
        if (platformCurrency.getFixedExchangeRate() != null && platformCurrency.getFixedExchangeRate().compareTo(BigDecimal.ZERO) < 0){
            throw new ServiceException("固定汇率不允许小于0");
        }
        if (platformCurrency.getRealTimeExchangeRateFlag() == null){
            throw new ServiceException("请选择汇率模式");
        }
        if (!platformCurrency.getRealTimeExchangeRateFlag().equals(0) && !platformCurrency.getRealTimeExchangeRateFlag().equals(1)){
            throw new ServiceException("汇率模式错误");
        }
        if (platformCurrency.getRealTimeExchangeRateFlag().equals(1) && StringUtils.isEmpty(platformCurrency.getRealTimeExchangeRateProduct())){
            throw new ServiceException("请选择实时汇率品种");
        }
        return toAjax(platformCurrencyService.insertPlatformCurrency(platformCurrency));
    }

    /**
     * 修改平台交易币种配置信息
     */
    @PreAuthorize("@ss.hasPermi('system:platformCurrency:edit')")
    @Log(title = "修改平台交易币种配置信息", businessType = BusinessType.UPDATE,dict = PlatformCurrencyLogDict.class)
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody PlatformCurrency platformCurrency)
    {
        if (platformCurrency.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (StringUtils.isEmpty(platformCurrency.getCurrencyDesc())){
            throw new ServiceException("请输入币种描述");
        }
        if (StringUtils.isEmpty(platformCurrency.getCurrencyName())){
            throw new ServiceException("请输入币种名称");
        }
        if (platformCurrency.getCurrencyType() == null){
            throw new ServiceException("请选择币种类型");
        }
        if (platformCurrency.getFixedExchangeRate() != null && platformCurrency.getFixedExchangeRate().compareTo(BigDecimal.ZERO) < 0){
            throw new ServiceException("固定汇率不允许小于0");
        }
        if (platformCurrency.getRealTimeExchangeRateFlag() == null){
            throw new ServiceException("请选择汇率模式");
        }
        if (!platformCurrency.getRealTimeExchangeRateFlag().equals(0) && !platformCurrency.getRealTimeExchangeRateFlag().equals(1)){
            throw new ServiceException("汇率模式错误");
        }
        if (platformCurrency.getRealTimeExchangeRateFlag().equals(1) && StringUtils.isEmpty(platformCurrency.getRealTimeExchangeRateProduct())){
            throw new ServiceException("请选择实时汇率品种");
        }
        return toAjax(platformCurrencyService.updatePlatformCurrency(platformCurrency));
    }

    /**
     * 修改币种名称多语言
     */
    @PreAuthorize("@ss.hasPermi('system:platformCurrency:edit')")
    @Log(title = "修改币种名称多语言", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping(value = "updateCurrencyNameLang")
    public AjaxResult updateCurrencyNameLang(@RequestBody PlatformCurrency platformCurrency)
    {
        if (platformCurrency.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (StringUtils.isEmpty(platformCurrency.getCurrencyNameLang().getZh())){
            throw new ServiceException("请输入币种名称");
        }
        return toAjax(platformCurrencyService.updateCurrencyNameLang(platformCurrency.getId(),platformCurrency.getCurrencyNameLang()));
    }

    /**
     * 修改币种名称多语言
     */
    @PreAuthorize("@ss.hasPermi('system:platformCurrency:edit')")
    @Log(title = "修改币种名称多语言", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping(value = "updateCurrencyDescLang")
    public AjaxResult updateCurrencyDescLang(@RequestBody PlatformCurrency platformCurrency)
    {
        if (platformCurrency.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (StringUtils.isEmpty(platformCurrency.getCurrencyDescLang().getZh())){
            throw new ServiceException("请输入币种描述");
        }
        return toAjax(platformCurrencyService.updateCurrencyDescLang(platformCurrency.getId(),platformCurrency.getCurrencyDescLang()));
    }

    /**
     * 删除平台交易币种配置信息
     */
    @PreAuthorize("@ss.hasPermi('system:platformCurrency:remove')")
    @Log(title = "删除平台交易币种配置信息", businessType = BusinessType.DELETE,dict = PlatformCurrencyLogDict.class)
    @RepeatSubmit
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
//        return toAjax(platformCurrencyService.deletePlatformCurrencyByIds(ids));
        throw new ServiceException("请联系开发人员");
    }
}
