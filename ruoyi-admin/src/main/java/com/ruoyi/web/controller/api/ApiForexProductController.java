package com.ruoyi.web.controller.api;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.ForexProduct;
import com.ruoyi.system.service.IForexProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 外汇产品信息Controller
 * 
 * @author ruoyi
 * @date 2023-11-01
 */
@RestController
@RequestMapping("/api/forexProduct")
public class ApiForexProductController extends BaseController
{
    @Autowired
    private IForexProductService forexProductService;

    /**
     * 查询外汇产品信息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(ForexProduct forexProduct)
    {
        startPage();
        startOrderBy("sort is null,sort");
        forexProduct.setIsShow(0);
        List<ForexProduct> list = forexProductService.selectForexProductList(forexProduct);
        PageHelper.clearPage();
        //填充行情信息
        forexProductService.fillProductQuote(list);
        //如果是登录状态，填充自选标识
        if (SecurityUtils.isAppUser()){
            forexProductService.fillIsOption(list,getUserId());
        }
        return getDataTable(list);
    }

    /**
     * 获取外汇产品信息详细信息
     */
    @GetMapping(value = "/detail")
    public AjaxResult getInfo(Long id)
    {
        ForexProduct product = forexProductService.selectForexProductById(id);
        //填充行情信息
        forexProductService.fillProductQuote(product);
        return success(product);
    }

    /**
     * 获取K线  // TODO: 3/12/2024 待优化
     */
    @GetMapping(value = "getKLine_Echarts")
    public AjaxResult getKLine_Echarts(String code,Integer time,String timespan) {
        if (StringUtils.isEmpty(code)){
            return AjaxResult.error(HintConstants.PARAM_NULL,"请输入产品代码");
        }
        if (time == null){
            return AjaxResult.error(HintConstants.PARAM_NULL,"请选择时间跨度");
        }
        if (StringUtils.isEmpty(timespan)){
            return AjaxResult.error(HintConstants.PARAM_NULL,"请选择时间单位");
        }
        return forexProductService.getKLine_Echarts(code, time,timespan);
    }
}
