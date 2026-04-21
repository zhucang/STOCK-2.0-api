package com.ruoyi.web.controller.api;

import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.ProductSetting;
import com.ruoyi.system.service.IProductSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 产品交易设置（产品风控）Controller
 * 
 * @author ruoyi
 * @date 2023-12-01
 */
@RestController
@RequestMapping("/api/productSetting")
public class ApiProductSettingController extends BaseController
{
    @Autowired
    private IProductSettingService productSettingService;

    /**
     * 查询产品交易设置（产品风控）列表
     */
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
     * 查询产品交易设置（产品风控）列表
     */
    @GetMapping("/detail")
    public AjaxResult detail(Integer productType)
    {
        if (productType == null){
            return AjaxResult.error(HintConstants.PARAM_NULL,"请选择产品类型");
        }
        return AjaxResult.success(productSettingService.selectProductSettingByProductType(productType));
    }
}
