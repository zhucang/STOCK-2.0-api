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
import com.ruoyi.system.domain.StockProduct;
import com.ruoyi.system.service.IStockProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 股票产品信息Controller
 * 
 * @author ruoyi
 * @date 2023-11-01
 */
@RestController
@RequestMapping("/system/stockProduct")
public class StockProductController extends BaseController
{
    @Autowired
    private IStockProductService stockProductService;

    /**
     * 查询股票产品信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:stockProduct:list')")
    @GetMapping("/list")
    public TableDataInfo list(StockProduct stockProduct)
    {
        startPage();
        startOrderBy("sort is null,sort");
        List<StockProduct> list = stockProductService.selectStockProductList(stockProduct);
        PageHelper.clearPage();
        //填充行情信息
        stockProductService.fillProductQuote(list);
        return getDataTable(list);
    }

    /**
     * 查询股票产品信息列表
     */
    @GetMapping("/listNoPreAuthorize")
    public TableDataInfo listNoPreAuthorize(StockProduct stockProduct)
    {
        startPage();
        startOrderBy("sort is null,sort");
        List<StockProduct> list = stockProductService.selectStockProductList(stockProduct);
        return getDataTable(list);
    }

    /**
     * 获取股票产品信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:stockProduct:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(stockProductService.selectStockProductById(id));
    }

//    /**
//     * 新增股票产品信息
//     */
//    @PreAuthorize("@ss.hasPermi('system:stockProduct:add')")
//    @Log(title = "股票产品信息", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody StockProduct stockProduct)
//    {
//        return toAjax(stockProductService.insertStockProduct(stockProduct));
//    }

    /**
     * 批量新增股票产品信息
     */
    @PreAuthorize("@ss.hasPermi('system:stockProduct:addProducts')")
    @Log(title = "批量新增股票产品信息" , businessType = BusinessType.INSERT,dict = CryptocurrencyProductLogDict.class,
            saveParamNames = {"id","productCode","productName","productsList"})
    @RepeatSubmit
    @PostMapping(value = "addProducts")
    public AjaxResult addProducts(@RequestBody List<StockProduct> products){
        if (products.size() == 0){
            throw new ServiceException("请选择需要添加的产品");
        }
        if (products.stream().filter(a-> StringUtils.isEmpty(a.getProductCode())).count() > 0){
            throw new ServiceException("产品代码不允许为空");
        }
        return toAjax(stockProductService.addProducts(products));
    }

    /**
     * 修改股票产品信息
     */
    @PreAuthorize("@ss.hasPermi('system:stockProduct:edit')")
    @Log(title = "修改股票产品信息", businessType = BusinessType.UPDATE,dict = CryptocurrencyProductLogDict.class,
            saveParamNames = {"id","productName","productCode","isLock","isLock","sort","standard","productDesc","productImg"})
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody StockProduct stockProduct)
    {
        return toAjax(stockProductService.updateStockProduct(stockProduct));
    }

    /**
     * 修改股票名称多语言
     */
    @PreAuthorize("@ss.hasPermi('system:stockProduct:edit')")
    @Log(title = "修改股票名称多语言", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping(value = "updateProductNameLang")
    public AjaxResult updateProductNameLang(@RequestBody StockProduct stockProduct)
    {
        if (stockProduct.getId() == null){
            return AjaxResult.error("请选择需要修改的选项");
        }
        if (StringUtils.isEmpty(stockProduct.getProductNameLang().getZh())){
            return AjaxResult.error("请输入产品名称");
        }
        return toAjax(stockProductService.updateProductNameLang(stockProduct.getId(),stockProduct.getProductNameLang()));
    }

    /**
     * 删除股票产品信息
     */
    @PreAuthorize("@ss.hasPermi('system:stockProduct:remove')")
    @Log(title = "删除股票产品信息", businessType = BusinessType.DELETE,dict = CryptocurrencyProductLogDict.class,
            saveParamNames = {"id","productCode","stockProducts"})
    @RepeatSubmit
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(stockProductService.deleteStockProductByIds(ids));
    }

    /**
     * 修改产品锁定状态
     */
    @PreAuthorize("@ss.hasPermi('system:stockProduct:updateLock')")
    @Log(title = "修改产品锁定状态", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping(value = "updateLock")
    public AjaxResult updateLock(@RequestParam("ids")List<Long> ids, Integer status) {
        return toAjax(stockProductService.updateLock(ids,status));
    }

    /**
     * 修改产品显示状态
     */
    @PreAuthorize("@ss.hasPermi('system:stockProduct:updateShow')")
    @Log(title = "修改产品显示状态", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping(value = "updateShow")
    public AjaxResult updateShow(@RequestParam("ids")List<Long> ids, Integer status) {
        return toAjax(stockProductService.updateShow(ids,status));
    }

    /**
     * 批量修改产品合约收益系数
     */
    @PreAuthorize("@ss.hasPermi('system:stockProduct:edit')")
    @Log(title = "批量修改股票产品合约收益系数", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping(value = "batchUpdatePositionIncomeCoefficient")
    public AjaxResult batchUpdatePositionIncomeCoefficient(@RequestParam("ids")List<Long> ids, BigDecimal positionIncomeCoefficient) {
        return toAjax(stockProductService.batchUpdatePositionIncomeCoefficient(ids,positionIncomeCoefficient));
    }
}
