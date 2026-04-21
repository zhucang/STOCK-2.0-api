package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.logDict.ProductTradeTimeSettingLogDict;
import com.ruoyi.system.domain.ProductTradeTimeSetting;
import com.ruoyi.system.service.IProductTradeTimeSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统产品交易时间配置Controller
 * 
 * @author ruoyi
 * @date 2023-11-27
 * 日志优化已完成
 */
@RestController
@RequestMapping("/system/productTradeTimeSetting")
public class ProductTradeTimeSettingController extends BaseController
{
    @Autowired
    private IProductTradeTimeSettingService productTradeTimeSettingService;

    /**
     * 查询系统产品交易时间配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:productTradeTimeSetting:list')")
    @GetMapping("/list")
    public TableDataInfo list(ProductTradeTimeSetting productTradeTimeSetting)
    {
        startPage();
        List<ProductTradeTimeSetting> list = productTradeTimeSettingService.selectProductTradeTimeSettingList(productTradeTimeSetting);
        return getDataTable(list);
    }

    /**
     * 获取系统产品交易时间配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:productTradeTimeSetting:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(productTradeTimeSettingService.selectProductTradeTimeSettingById(id));
    }

    /**
     * 修改系统产品交易时间配置
     */
    @PreAuthorize("@ss.hasPermi('system:productTradeTimeSetting:edit')")
    @Log(title = "修改系统产品交易时间配置", businessType = BusinessType.UPDATE,dict = ProductTradeTimeSettingLogDict.class)
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody ProductTradeTimeSetting productTradeTimeSetting)
    {
        if (productTradeTimeSetting.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        productTradeTimeSetting.setProductType(null);
        productTradeTimeSetting.setDay(null);
        return toAjax(productTradeTimeSettingService.updateProductTradeTimeSetting(productTradeTimeSetting));
    }
}
