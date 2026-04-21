package com.ruoyi.web.controller.system;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.logDict.CryptocurrencyProductLogDict;
import com.ruoyi.system.domain.FuturesProduct;
import com.ruoyi.system.service.IFuturesProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 期货产品信息Controller
 * 
 * @author ruoyi
 * @date 2023-11-01
 * 已优化日志
 */
@RestController
@RequestMapping("/system/futuresProduct")
public class FuturesProductController extends BaseController
{
    @Autowired
    private IFuturesProductService futuresProductService;

    /**
     * 查询期货产品信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:futuresProduct:list')")
    @GetMapping("/list")
    public TableDataInfo list(FuturesProduct futuresProduct)
    {
        startPage();
        startOrderBy("sort is null,sort");
        List<FuturesProduct> list = futuresProductService.selectFuturesProductList(futuresProduct);
        PageHelper.clearPage();
        //填充行情信息
        futuresProductService.fillProductQuote(list);
        return getDataTable(list);
    }

    /**
     * 查询期货产品信息列表
     */
    @GetMapping("/listNoPreAuthorize")
    public TableDataInfo listNoPreAuthorize(FuturesProduct futuresProduct)
    {
        startPage();
        startOrderBy("sort is null,sort");
        List<FuturesProduct> list = futuresProductService.selectFuturesProductList(futuresProduct);
        return getDataTable(list);
    }

    /**
     * 获取期货产品信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:futuresProduct:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(futuresProductService.selectFuturesProductById(id));
    }

//    /**
//     * 新增期货产品信息
//     */
//    @PreAuthorize("@ss.hasPermi('system:futuresProduct:add')")
//    @Log(title = "期货产品信息", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody FuturesProduct futuresProduct)
//    {
//        return toAjax(futuresProductService.insertFuturesProduct(futuresProduct));
//    }

    /**
     * 修改期货产品信息
     */
    @PreAuthorize("@ss.hasPermi('system:futuresProduct:edit')")
    @Log(title = "修改期货产品信息", businessType = BusinessType.UPDATE,dict = CryptocurrencyProductLogDict.class,
            saveParamNames = {"id","productName","productCode","isLock","isLock","sort","standard","productDesc","productImg"})
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody FuturesProduct futuresProduct)
    {
        return toAjax(futuresProductService.updateFuturesProduct(futuresProduct));
    }

    /**
     * 修改期货名称多语言
     */
    @PreAuthorize("@ss.hasPermi('system:futuresProduct:edit')")
    @Log(title = "修改期货名称多语言", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping(value = "updateProductNameLang")
    public AjaxResult updateProductNameLang(@RequestBody FuturesProduct futuresProduct)
    {
        if (futuresProduct.getId() == null){
            return AjaxResult.error("请选择需要修改的选项");
        }
        if (StringUtils.isEmpty(futuresProduct.getProductNameLang().getZh())){
            return AjaxResult.error("请输入产品名称");
        }
        return toAjax(futuresProductService.updateProductNameLang(futuresProduct.getId(),futuresProduct.getProductNameLang()));
    }

    /**
     * 删除期货产品信息
     */
    @PreAuthorize("@ss.hasPermi('system:futuresProduct:remove')")
    @Log(title = "删除期货产品信息", businessType = BusinessType.DELETE,dict = CryptocurrencyProductLogDict.class,
            saveParamNames = {"id","productCode","futuresProducts"})
    @RepeatSubmit
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(futuresProductService.deleteFuturesProductByIds(ids));
    }

    /**
     * 修改产品锁定状态
     */
    @PreAuthorize("@ss.hasPermi('system:futuresProduct:updateLock')")
    @Log(title = "修改产品锁定状态", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping(value = "updateLock")
    public AjaxResult updateLock(@RequestParam("ids")List<Long> ids, Integer status) {
        return toAjax(futuresProductService.updateLock(ids,status));
    }

    /**
     * 修改产品显示状态
     */
    @PreAuthorize("@ss.hasPermi('system:futuresProduct:updateShow')")
    @Log(title = "修改产品显示状态", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping(value = "updateShow")
    public AjaxResult updateShow(@RequestParam("ids")List<Long> ids, Integer status) {
        return toAjax(futuresProductService.updateShow(ids,status));
    }


    /**
     * 批量修改产品合约收益系数
     */
    @PreAuthorize("@ss.hasPermi('system:futuresProduct:edit')")
    @Log(title = "批量修改期货产品合约收益系数", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping(value = "batchUpdatePositionIncomeCoefficient")
    public AjaxResult batchUpdatePositionIncomeCoefficient(@RequestParam("ids")List<Long> ids, BigDecimal positionIncomeCoefficient) {
        return toAjax(futuresProductService.batchUpdatePositionIncomeCoefficient(ids,positionIncomeCoefficient));
    }
}
