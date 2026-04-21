package com.ruoyi.web.controller.system;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.logDict.CryptocurrencyProductLogDict;
import com.ruoyi.system.domain.ForexProduct;
import com.ruoyi.system.service.IForexProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 外汇产品信息Controller
 * 
 * @author ruoyi
 * @date 2023-11-01
 * 已优化日志
 */
@RestController
@RequestMapping("/system/forexProduct")
public class ForexProductController extends BaseController
{
    @Autowired
    private IForexProductService forexProductService;

    /**
     * 查询外汇产品信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:forexProduct:list')")
    @GetMapping("/list")
    public TableDataInfo list(ForexProduct forexProduct)
    {
        startPage();
        startOrderBy("sort is null,sort");
        List<ForexProduct> list = forexProductService.selectForexProductList(forexProduct);
        PageHelper.clearPage();
        //填充行情信息
        forexProductService.fillProductQuote(list);
        return getDataTable(list);
    }

    /**
     * 查询外汇产品信息列表
     */
    @GetMapping("/listNoPreAuthorize")
    public TableDataInfo listNoPreAuthorize(ForexProduct forexProduct)
    {
        startPage();
        startOrderBy("sort is null,sort");
        List<ForexProduct> list = forexProductService.selectForexProductList(forexProduct);
        return getDataTable(list);
    }

    /**
     * 获取外汇产品信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:forexProduct:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(forexProductService.selectForexProductById(id));
    }

//    /**
//     * 新增外汇产品信息
//     */
//    @PreAuthorize("@ss.hasPermi('system:forexProduct:add')")
//    @Log(title = "外汇产品信息", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody ForexProduct forexProduct)
//    {
//        return toAjax(forexProductService.insertForexProduct(forexProduct));
//    }

    /**
     * 批量新增外汇产品信息
     */
    @PreAuthorize("@ss.hasPermi('system:forexProduct:addProducts')")
    @Log(title = "批量新增外汇产品信息" , businessType = BusinessType.INSERT,dict = CryptocurrencyProductLogDict.class,
            saveParamNames = {"id","productCode","productName","productsList"})
    @RepeatSubmit
    @PostMapping(value = "addProducts")
    public AjaxResult addProducts(@RequestBody List<ForexProduct> products){
        if (products.size() == 0){
            throw new ServiceException("请选择需要添加的产品");
        }
        if (products.stream().filter(a-> StringUtils.isEmpty(a.getProductCode())).count() > 0){
            throw new ServiceException("产品代码不允许为空");
        }
        return toAjax(forexProductService.addProducts(products));
    }

    /**
     * 修改外汇产品信息
     */
    @PreAuthorize("@ss.hasPermi('system:forexProduct:edit')")
    @Log(title = "修改外汇产品信息", businessType = BusinessType.UPDATE,dict = CryptocurrencyProductLogDict.class,
            saveParamNames = {"id","productName","productCode","isLock","isLock","sort","standard","productDesc","productImg"})
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody ForexProduct forexProduct)
    {
        return toAjax(forexProductService.updateForexProduct(forexProduct));
    }

    /**
     * 修改外汇名称多语言
     */
    @PreAuthorize("@ss.hasPermi('system:forexProduct:edit')")
    @Log(title = "修改外汇名称多语言", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping(value = "updateProductNameLang")
    public AjaxResult updateProductNameLang(@RequestBody ForexProduct forexProduct)
    {
        if (forexProduct.getId() == null){
            return AjaxResult.error("请选择需要修改的选项");
        }
        if (StringUtils.isEmpty(forexProduct.getProductNameLang().getZh())){
            return AjaxResult.error("请输入产品名称");
        }
        return toAjax(forexProductService.updateProductNameLang(forexProduct.getId(),forexProduct.getProductNameLang()));
    }

    /**
     * 删除外汇产品信息
     */
    @PreAuthorize("@ss.hasPermi('system:forexProduct:remove')")
    @Log(title = "删除外汇产品信息", businessType = BusinessType.DELETE,dict = CryptocurrencyProductLogDict.class,
            saveParamNames = {"id","productCode","forexProducts"})
    @RepeatSubmit
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(forexProductService.deleteForexProductByIds(ids));
    }

    /**
     * 修改产品锁定状态
     */
    @PreAuthorize("@ss.hasPermi('system:forexProduct:updateLock')")
    @Log(title = "修改产品锁定状态", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping(value = "updateLock")
    public AjaxResult updateLock(@RequestParam("ids")List<Long> ids, Integer status) {
        return toAjax(forexProductService.updateLock(ids,status));
    }

    /**
     * 修改产品显示状态
     */
    @PreAuthorize("@ss.hasPermi('system:forexProduct:updateShow')")
    @Log(title = "修改产品显示状态", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping(value = "updateShow")
    public AjaxResult updateShow(@RequestParam("ids")List<Long> ids, Integer status) {
        return toAjax(forexProductService.updateShow(ids,status));
    }


    /**
     * 批量修改产品合约收益系数
     */
    @PreAuthorize("@ss.hasPermi('system:forexProduct:edit')")
    @Log(title = "批量修改外汇产品合约收益系数", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping(value = "batchUpdatePositionIncomeCoefficient")
    public AjaxResult batchUpdatePositionIncomeCoefficient(@RequestParam("ids")List<Long> ids, BigDecimal positionIncomeCoefficient) {
        return toAjax(forexProductService.batchUpdatePositionIncomeCoefficient(ids,positionIncomeCoefficient));
    }
}
