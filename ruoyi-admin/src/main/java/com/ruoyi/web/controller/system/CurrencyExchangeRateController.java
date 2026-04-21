package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.logDict.CurrencyExchangeRateLogDict;
import com.ruoyi.system.domain.CurrencyExchangeRate;
import com.ruoyi.system.service.ICurrencyExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 货币兑换汇率Controller
 * 
 * @author ruoyi
 * @date 2023-11-25
 * 已优化日志
 */
@RestController
@RequestMapping("/system/currencyExchangeRate")
public class CurrencyExchangeRateController extends BaseController
{
    @Autowired
    private ICurrencyExchangeRateService currencyExchangeRateService;

    /**
     * 查询货币兑换汇率列表
     */
    @PreAuthorize("@ss.hasPermi('system:currencyExchangeRate:list')")
    @GetMapping("/list")
    public TableDataInfo list(CurrencyExchangeRate currencyExchangeRate)
    {
        startPage();
        List<CurrencyExchangeRate> list = currencyExchangeRateService.selectCurrencyExchangeRateList(currencyExchangeRate);
        return getDataTable(list);
    }

    /**
     * 获取货币兑换汇率详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:currencyExchangeRate:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(currencyExchangeRateService.selectCurrencyExchangeRateById(id));
    }

    /**
     * 新增货币兑换汇率
     */
    @PreAuthorize("@ss.hasPermi('system:currencyExchangeRate:add')")
    @Log(title = "新增货币兑换汇率", businessType = BusinessType.INSERT,dict = CurrencyExchangeRateLogDict.class)
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody CurrencyExchangeRate currencyExchangeRate)
    {
        if (currencyExchangeRate.getCurrencyIdFrom() == null){
            throw new ServiceException("请选择转化币种");
        }
        if (currencyExchangeRate.getCurrencyIdTo() == null){
            throw new ServiceException("请选择被转化币种");
        }
        if (currencyExchangeRate.getCurrencyIdFrom().equals(currencyExchangeRate.getCurrencyIdTo())){
            throw new ServiceException("无须配置自身汇率");
        }
        if (currencyExchangeRate.getExchangeRate() == null){
            throw new ServiceException("请输入转化汇率");
        }
        if (currencyExchangeRate.getExchangeRate().compareTo(BigDecimal.ZERO) == -1){
            throw new ServiceException("转化汇率不允许小于0");
        }
        if (currencyExchangeRate.getFeePercent() == null){
            throw new ServiceException("请输入兑换手续费率");
        }
        if (currencyExchangeRate.getFeePercent().compareTo(BigDecimal.ZERO) == -1){
            throw new ServiceException("手续费率不允许小于0");
        }
        return toAjax(currencyExchangeRateService.insertCurrencyExchangeRate(currencyExchangeRate));
    }

    /**
     * 修改货币兑换汇率
     */
    @PreAuthorize("@ss.hasPermi('system:currencyExchangeRate:edit')")
    @Log(title = "修改货币兑换汇率", businessType = BusinessType.UPDATE,dict = CurrencyExchangeRateLogDict.class)
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody CurrencyExchangeRate currencyExchangeRate)
    {
        if (currencyExchangeRate.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (currencyExchangeRate.getCurrencyIdFrom() == null){
            throw new ServiceException("请选择转化币种");
        }
        if (currencyExchangeRate.getCurrencyIdTo() == null){
            throw new ServiceException("请选择被转化币种");
        }
        if (currencyExchangeRate.getCurrencyIdFrom().equals(currencyExchangeRate.getCurrencyIdTo())){
            throw new ServiceException("无须配置自身汇率");
        }
        if (currencyExchangeRate.getExchangeRate() == null){
            throw new ServiceException("请输入转化汇率");
        }
        if (currencyExchangeRate.getExchangeRate().compareTo(BigDecimal.ZERO) == -1){
            throw new ServiceException("转化汇率不允许小于0");
        }
        if (currencyExchangeRate.getFeePercent() == null){
            throw new ServiceException("请输入兑换手续费率");
        }
        if (currencyExchangeRate.getFeePercent().compareTo(BigDecimal.ZERO) == -1){
            throw new ServiceException("手续费率不允许小于0");
        }
        return toAjax(currencyExchangeRateService.updateCurrencyExchangeRate(currencyExchangeRate));
    }

    /**
     * 删除货币兑换汇率
     */
    @PreAuthorize("@ss.hasPermi('system:currencyExchangeRate:remove')")
    @Log(title = "删除货币兑换汇率", businessType = BusinessType.DELETE,dict = CurrencyExchangeRateLogDict.class)
    @RepeatSubmit
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(currencyExchangeRateService.deleteCurrencyExchangeRateByIds(ids));
    }
}
