package com.ruoyi.web.controller.api;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.FuturesProduct;
import com.ruoyi.system.service.IFuturesProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 期货产品信息Controller
 * 
 * @author ruoyi
 * @date 2023-11-01
 */
@RestController
@RequestMapping("/api/futuresProduct")
public class ApiFuturesProductController extends BaseController
{
    @Autowired
    private IFuturesProductService futuresProductService;

    /**
     * 查询期货产品信息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(FuturesProduct futuresProduct)
    {
        startPage();
        startOrderBy("sort is null,sort");
        futuresProduct.setIsShow(0);
        List<FuturesProduct> list = futuresProductService.selectFuturesProductList(futuresProduct);
        PageHelper.clearPage();
        //填充行情信息
        futuresProductService.fillProductQuote(list);
        //如果是登录状态，填充自选标识
        if (SecurityUtils.isAppUser()){
            futuresProductService.fillIsOption(list,getUserId());
        }
        return getDataTable(list);
    }

    /**
     * 获取期货产品信息详细信息
     */
    @GetMapping(value = "/detail")
    public AjaxResult getInfo(Long id)
    {
        FuturesProduct product = futuresProductService.selectFuturesProductById(id);
        //填充行情信息
        futuresProductService.fillProductQuote(product);
        return success(product);
    }

    /**
     * 获取K线
     */
    @GetMapping(value = "getKLine_Echarts")
    public AjaxResult getKLine_Echarts(String code, Integer time, String timespan) {
        if (StringUtils.isEmpty(code)){
            return AjaxResult.error(HintConstants.PARAM_NULL,"请输入产品代码");
        }
        if (time == null){
            return AjaxResult.error(HintConstants.PARAM_NULL,"请选择时间跨度");
        }
        if (StringUtils.isEmpty(timespan)){
            return AjaxResult.error(HintConstants.PARAM_NULL,"请选择时间单位");
        }
        return futuresProductService.getKLine_Echarts(code, time,timespan);
    }
}
