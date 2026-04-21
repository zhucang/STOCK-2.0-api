package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.logDict.HomeRecommendProductsLogDict;
import com.ruoyi.system.domain.HomeRecommendProducts;
import com.ruoyi.system.service.IHomeRecommendProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 首页推荐产品Controller
 * 
 * @author ruoyi
 * @date 2024-01-09
 */
@RestController
@RequestMapping("/system/homeRecommendProducts")
public class HomeRecommendProductsController extends BaseController
{
    @Autowired
    private IHomeRecommendProductsService homeRecommendProductsService;

    /**
     * 查询首页推荐产品列表
     */
    @PreAuthorize("@ss.hasPermi('system:homeRecommendProducts:list')")
    @GetMapping("/list")
    public TableDataInfo list(HomeRecommendProducts homeRecommendProducts)
    {
        startPage();
        startOrderBy("sort is null,sort");
        List<HomeRecommendProducts> list = homeRecommendProductsService.selectHomeRecommendProductsList(homeRecommendProducts);
        return getDataTable(list);
    }

    /**
     * 获取首页推荐产品详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:homeRecommendProducts:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(homeRecommendProductsService.selectHomeRecommendProductsById(id));
    }

    /**
     * 新增首页推荐产品
     */
    @PreAuthorize("@ss.hasPermi('system:homeRecommendProducts:add')")
    @Log(title = "新增首页推荐产品", businessType = BusinessType.INSERT,dict = HomeRecommendProductsLogDict.class)
    @PostMapping
    public AjaxResult add(@RequestBody HomeRecommendProducts homeRecommendProducts)
    {
        if (homeRecommendProducts.getProductId() == null){
            return AjaxResult.error("请选择产品信息");
        }
        if (homeRecommendProducts.getProductType() == null){
            return AjaxResult.error("请选择产品类型");
        }
        return toAjax(homeRecommendProductsService.insertHomeRecommendProducts(homeRecommendProducts));
    }

    /**
     * 修改首页推荐产品
     */
    @PreAuthorize("@ss.hasPermi('system:homeRecommendProducts:edit')")
    @Log(title = "修改首页推荐产品", businessType = BusinessType.UPDATE,dict = HomeRecommendProductsLogDict.class)
    @PutMapping
    public AjaxResult edit(@RequestBody HomeRecommendProducts homeRecommendProducts)
    {
        if (homeRecommendProducts.getId() == null){
            return AjaxResult.error("请选择需要修改的选项");
        }
        if (homeRecommendProducts.getProductId() == null){
            return AjaxResult.error("请选择产品信息");
        }
        if (homeRecommendProducts.getProductType() == null){
            return AjaxResult.error("请选择产品类型");
        }
        return toAjax(homeRecommendProductsService.updateHomeRecommendProducts(homeRecommendProducts));
    }

    /**
     * 删除首页推荐产品
     */
    @PreAuthorize("@ss.hasPermi('system:homeRecommendProducts:remove')")
    @Log(title = "删除首页推荐产品", businessType = BusinessType.DELETE,dict = HomeRecommendProductsLogDict.class,
            saveParamNames = {"id","productCode","productType","homeRecommendProducts"})
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(homeRecommendProductsService.deleteHomeRecommendProductsByIds(ids));
    }
}
