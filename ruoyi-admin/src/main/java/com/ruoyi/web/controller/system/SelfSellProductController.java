package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.logDict.SelfSellProductLogDict;
import com.ruoyi.system.domain.SelfSellProduct;
import com.ruoyi.system.service.ISelfSellProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 自营产品Controller
 * 
 * @author ruoyi
 * @date 2023-11-27
 */
@RestController
@RequestMapping("/system/selfSellProduct")
public class SelfSellProductController extends BaseController
{
    @Autowired
    private ISelfSellProductService selfSellProductService;

    /**
     * 查询自营产品列表
     */
    @PreAuthorize("@ss.hasPermi('system:selfSellProduct:list')")
    @GetMapping("/list")
    public TableDataInfo list(SelfSellProduct selfSellProduct)
    {
        startPage();
        startOrderBy("id desc");
        List<SelfSellProduct> list = selfSellProductService.selectSelfSellProductList(selfSellProduct);
        return getDataTable(list);
    }


    /**
     * 查询自营产品列表
     */
    @GetMapping("/listNoPreAuthorize")
    public TableDataInfo listNoPreAuthorize(SelfSellProduct selfSellProduct)
    {
        startPage();
        List<SelfSellProduct> list = selfSellProductService.selectSelfSellProductList(selfSellProduct);
        return getDataTable(list);
    }

    /**
     * 获取自营产品详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:selfSellProduct:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(selfSellProductService.selectSelfSellProductById(id));
    }

    /**
     * 新增自营产品
     */
    @PreAuthorize("@ss.hasPermi('system:selfSellProduct:add')")
    @Log(title = "新增自营产品", businessType = BusinessType.INSERT,dict = SelfSellProductLogDict.class)
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody SelfSellProduct selfSellProduct)
    {
        if (StringUtils.isEmpty(selfSellProduct.getProductCode())){
            throw new ServiceException("请输入产品代码");
        }
        if (StringUtils.isEmpty(selfSellProduct.getProductName())){
            throw new ServiceException("请输入产品名称");
        }
        if (selfSellProduct.getProductType() == null){
            throw new ServiceException("请选择产品类型");
        }
        if (selfSellProduct.getStatus() == null){
            throw new ServiceException("请选择产品状态");
        }
        if (selfSellProduct.getIsDirectListing() == null){
            throw new ServiceException("请选择是否直接上市");
        }
        if (selfSellProduct.getIsDirectListing().equals(0)){
            if (selfSellProduct.getInitialPrice() == null){
                throw new ServiceException("直接上市的自营产品需要设置初始价格");
            }
            if (selfSellProduct.getInitialPrice().compareTo(BigDecimal.ZERO) <= 0){
                throw new ServiceException("自营产品的初始价格必须大于0");
            }
        }
        return toAjax(selfSellProductService.insertSelfSellProduct(selfSellProduct));
    }

    /**
     * 修改自营产品
     */
    @PreAuthorize("@ss.hasPermi('system:selfSellProduct:edit')")
    @Log(title = "修改自营产品", businessType = BusinessType.UPDATE,dict = SelfSellProductLogDict.class)
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody SelfSellProduct selfSellProduct)
    {
        if (selfSellProduct.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (StringUtils.isEmpty(selfSellProduct.getProductCode())){
            throw new ServiceException("请输入产品代码");
        }
        if (StringUtils.isEmpty(selfSellProduct.getProductName())){
            throw new ServiceException("请输入产品名称");
        }
        if (selfSellProduct.getProductType() == null){
            throw new ServiceException("请选择产品类型");
        }
        if (selfSellProduct.getStatus() == null){
            throw new ServiceException("请选择产品状态");
        }
        if (selfSellProduct.getIsDirectListing() == null){
            throw new ServiceException("请选择是否直接上市");
        }
        return toAjax(selfSellProductService.updateSelfSellProduct(selfSellProduct));
    }

    /**
     * 修改自营产品多语言配置
     */
    @PreAuthorize("@ss.hasPermi('system:selfSellProduct:edit')")
    @Log(title = "修改自营产品多语言配置", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping(value = "updateProductNameLangLang")
    public AjaxResult updateProductNameLangLang(@RequestBody SelfSellProduct selfSellProduct)
    {
        if (selfSellProduct.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (selfSellProduct.getProductNameLang().getZh() == null){
            throw new ServiceException("请输入产品名称");
        }
        return toAjax(selfSellProductService.updateProductNameLangLang(selfSellProduct.getId(),selfSellProduct.getProductNameLang()));
    }

    /**
     * 删除自营产品
     */
    @PreAuthorize("@ss.hasPermi('system:selfSellProduct:remove')")
    @Log(title = "删除自营产品", businessType = BusinessType.DELETE,dict = SelfSellProductLogDict.class,
            saveParamNames = {"id","productCode","productName","selfSellProducts"})
    @RepeatSubmit
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(selfSellProductService.deleteSelfSellProductByIds(ids));
    }
}
