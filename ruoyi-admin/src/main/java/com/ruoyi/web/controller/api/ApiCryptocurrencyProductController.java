package com.ruoyi.web.controller.api;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.CryptocurrencyProduct;
import com.ruoyi.system.mapper.SelfSellProductRealTimeMapper;
import com.ruoyi.system.service.ICryptocurrencyProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 加密货币产品信息Controller
 * 
 * @author ruoyi
 * @date 2023-11-01
 * 已优化
 */
@RestController
@RequestMapping("/api/cryptocurrencyProduct")
public class ApiCryptocurrencyProductController extends BaseController
{
    @Autowired
    private ICryptocurrencyProductService cryptocurrencyProductService;

    @Resource
    private SelfSellProductRealTimeMapper selfSellProductRealTimeMapper;

    /**
     * 查询加密货币产品信息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(CryptocurrencyProduct cryptocurrencyProduct)
    {
        startPage();
        startOrderBy("sort is null,sort");
        cryptocurrencyProduct.setIsShow(0);
        List<CryptocurrencyProduct> list = cryptocurrencyProductService.selectCryptocurrencyProductList(cryptocurrencyProduct);
        PageHelper.clearPage();
        //填充行情信息
        cryptocurrencyProductService.fillProductQuote(list);
        //如果是登录状态，填充自选标识
        if (SecurityUtils.isAppUser()){
            cryptocurrencyProductService.fillIsOption(list,getUserId());
        }
        return getDataTable(list);
    }

    /**
     * 查询自营产品每日行情数据
     */
    @GetMapping("/selfSellProductData")
    public TableDataInfo selfSellProductData(CryptocurrencyProduct cryptocurrencyProduct)
    {
        startPage();
        startOrderBy("sort is null,sort");
        cryptocurrencyProduct.setIsShow(0);
        cryptocurrencyProduct.setIsSelfSell(1);
        List<CryptocurrencyProduct> products = cryptocurrencyProductService.selectCryptocurrencyProductList(cryptocurrencyProduct);
        PageHelper.clearPage();
        Date date = new Date();
        //填充行情信息
        for (int i = 0; i < products.size(); i++) {
            CryptocurrencyProduct product = products.get(i);
            try {
                Map<String, Map> map = selfSellProductRealTimeMapper.selectRealTimeTradeFinalDetail(product.getProductCode(),2,date);
                product.getParams().put("data",map.get(product.getProductCode()));
            }catch (Exception e){
                throw new ServiceException("今日数据还未生成");
            }
        }
        return getDataTable(products);
    }

    /**
     * 获取加密货币产品信息详细信息
     */
    @GetMapping(value = "/detail")
    public AjaxResult getInfo(Long id)
    {
        CryptocurrencyProduct product = cryptocurrencyProductService.selectCryptocurrencyProductById(id);
        //填充行情信息
        cryptocurrencyProductService.fillProductQuote(product);
        return success(product);
    }

    /**
     * 获取K线
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
        return AjaxResult.success(cryptocurrencyProductService.getKLine_Echarts(code, time,timespan));
    }
}
