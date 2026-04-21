package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.logDict.ProductSettingLogDict;
import com.ruoyi.system.domain.ProductSetting;
import com.ruoyi.system.service.IProductSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 产品交易设置（产品风控）Controller
 * 
 * @author ruoyi
 * @date 2023-12-01
 * 日志优化完成
 */
@RestController
@RequestMapping("/system/productSetting")
public class ProductSettingController extends BaseController
{
    @Autowired
    private IProductSettingService productSettingService;

    /**
     * 查询产品交易设置（产品风控）列表
     */
    @PreAuthorize("@ss.hasPermi('system:productSetting:list')")
    @GetMapping("/list")
    public TableDataInfo list(ProductSetting productSetting)
    {
        if (productSetting.getProductType() == null){
            return getDataTable(new ArrayList<>());
        }
        startPage();
        List<ProductSetting> list = productSettingService.selectProductSettingList(productSetting);
        return getDataTable(list);
    }

    /**
     * 修改产品交易设置（产品风控）
     */
    @PreAuthorize("@ss.hasPermi('system:productSetting:edit')")
    @Log(title = "修改产品交易设置（产品风控）", businessType = BusinessType.UPDATE,dict = ProductSettingLogDict.class)
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody ProductSetting productSetting)
    {
        if (productSetting.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        return toAjax(productSettingService.updateProductSetting(productSetting));
    }
}
