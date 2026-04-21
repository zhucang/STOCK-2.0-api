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
import com.ruoyi.system.domain.CryptocurrencyProduct;
import com.ruoyi.system.service.ICryptocurrencyProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 加密货币产品信息Controller
 * 
 * @author ruoyi
 * @date 2023-11-01
 * 已优化日志
 */
@RestController
@RequestMapping("/system/cryptocurrencyProduct")
public class CryptocurrencyProductController extends BaseController
{
    @Autowired
    private ICryptocurrencyProductService cryptocurrencyProductService;

    /**
     * 查询加密货币产品信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:cryptocurrencyProduct:list')")
    @GetMapping("/list")
    public TableDataInfo list(CryptocurrencyProduct cryptocurrencyProduct)
    {
        startPage();
        startOrderBy("sort is null,sort");
        List<CryptocurrencyProduct> list = cryptocurrencyProductService.selectCryptocurrencyProductList(cryptocurrencyProduct);
        PageHelper.clearPage();
        cryptocurrencyProductService.fillProductQuote(list);
        return getDataTable(list);
    }

    /**
     * 查询加密货币产品信息列表
     */
    @GetMapping("/listNoPreAuthorize")
    public TableDataInfo listNoPreAuthorize(CryptocurrencyProduct cryptocurrencyProduct)
    {
        startPage();
        startOrderBy("sort is null,sort");
        List<CryptocurrencyProduct> list = cryptocurrencyProductService.selectCryptocurrencyProductList(cryptocurrencyProduct);
        return getDataTable(list);
    }

    /**
     * 获取加密货币产品信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:cryptocurrencyProduct:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(cryptocurrencyProductService.selectCryptocurrencyProductById(id));
    }

//    /**
//     * 新增加密货币产品信息
//     */
//    @PreAuthorize("@ss.hasPermi('system:cryptocurrencyProduct:add')")
//    @Log(title = "加密货币产品信息", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody CryptocurrencyProduct cryptocurrencyProduct)
//    {
//        return toAjax(cryptocurrencyProductService.insertCryptocurrencyProduct(cryptocurrencyProduct));
//    }

    /**
     * 批量新增加密货币产品信息
     */
    @PreAuthorize("@ss.hasPermi('system:cryptocurrencyProduct:addProducts')")
    @Log(title = "批量新增加密货币产品信息" , businessType = BusinessType.INSERT,dict = CryptocurrencyProductLogDict.class,
            saveParamNames = {"id","productCode","productName","productsList"})
    @RepeatSubmit
    @PostMapping(value = "addProducts")
    public AjaxResult addProducts(@RequestBody List<CryptocurrencyProduct> products){
        if (products.size() == 0){
            throw new ServiceException("请选择需要添加的产品");
        }
        if (products.stream().filter(a-> StringUtils.isEmpty(a.getProductCode())).count() > 0){
            throw new ServiceException("产品代码不允许为空");
        }
        return toAjax(cryptocurrencyProductService.addProducts(products));
    }

    /**
     * 修改加密货币产品信息
     */
    @PreAuthorize("@ss.hasPermi('system:cryptocurrencyProduct:edit')")
    @Log(title = "修改加密货币产品信息", businessType = BusinessType.UPDATE,dict = CryptocurrencyProductLogDict.class,
            saveParamNames = {"id","productName","productCode","isLock","isLock","sort","standard","productDesc","productImg"})
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody CryptocurrencyProduct cryptocurrencyProduct)
    {
        if (cryptocurrencyProduct.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        return toAjax(cryptocurrencyProductService.updateCryptocurrencyProduct(cryptocurrencyProduct));
    }

    /**
     * 修改加密货币名称多语言
     */
    @PreAuthorize("@ss.hasPermi('system:cryptocurrencyProduct:edit')")
    @Log(title = "修改加密货币名称多语言", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping(value = "updateProductNameLang")
    public AjaxResult updateProductNameLang(@RequestBody CryptocurrencyProduct cryptocurrencyProduct)
    {
        if (cryptocurrencyProduct.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (StringUtils.isEmpty(cryptocurrencyProduct.getProductNameLang().getZh())){
            throw new ServiceException("请输入产品名称");
        }
        return toAjax(cryptocurrencyProductService.updateProductNameLang(cryptocurrencyProduct.getId(),cryptocurrencyProduct.getProductNameLang()));
    }

    /**
     * 删除加密货币产品信息
     */
    @PreAuthorize("@ss.hasPermi('system:cryptocurrencyProduct:remove')")
    @Log(title = "删除加密货币产品信息", businessType = BusinessType.DELETE,dict = CryptocurrencyProductLogDict.class,
            saveParamNames = {"id","productCode","cryptocurrencyProducts"})
    @RepeatSubmit
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(cryptocurrencyProductService.deleteCryptocurrencyProductByIds(ids));
    }

    /**
     * 修改产品锁定状态
     */
    @PreAuthorize("@ss.hasPermi('system:cryptocurrencyProduct:updateLock')")
    @Log(title = "修改产品锁定状态" , businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping(value = "updateLock")
    public AjaxResult updateLock(@RequestParam("ids")List<Long> ids, Integer status) {
        return toAjax(cryptocurrencyProductService.updateLock(ids,status));
    }

    /**
     * 修改产品显示状态
     */
    @PreAuthorize("@ss.hasPermi('system:cryptocurrencyProduct:updateShow')")
    @Log(title = "修改产品显示状态" , businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping(value = "updateShow")
    public AjaxResult updateShow(@RequestParam("ids")List<Long> ids, Integer status) {
        return toAjax(cryptocurrencyProductService.updateShow(ids,status));
    }

    /**
     * 批量修改产品合约收益系数
     */
    @PreAuthorize("@ss.hasPermi('system:cryptocurrencyProduct:edit')")
    @Log(title = "批量修改加密货币产品合约收益系数", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping(value = "batchUpdatePositionIncomeCoefficient")
    public AjaxResult batchUpdatePositionIncomeCoefficient(@RequestParam("ids")List<Long> ids, BigDecimal positionIncomeCoefficient) {
        return toAjax(cryptocurrencyProductService.batchUpdatePositionIncomeCoefficient(ids,positionIncomeCoefficient));
    }
}
