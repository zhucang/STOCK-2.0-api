package com.ruoyi.web.controller.api;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.ticker.TickerInfo;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.StockProduct;
import com.ruoyi.system.mapper.SelfSellProductRealTimeMapper;
import com.ruoyi.system.service.IStockProductService;
import com.ruoyi.system.utils.StockQuoteUtilsNew;
import com.ruoyi.system.utils.cache.CacheUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

/**
 * 股票产品信息Controller
 * 
 * @author ruoyi
 * @date 2023-11-01
 */
@RestController
@RequestMapping("/api/stockProduct")
public class ApiStockProductController extends BaseController
{
    @Autowired
    private IStockProductService stockProductService;

    @Resource
    private SelfSellProductRealTimeMapper selfSellProductRealTimeMapper;

    /**
     * 查询股票产品信息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(StockProduct stockProduct)
    {
        startPage();
        startOrderBy("sort is null,sort");
        stockProduct.setIsShow(0);
        List<StockProduct> list = stockProductService.selectStockProductList(stockProduct);
        PageHelper.clearPage();
        //填充行情信息
        stockProductService.fillProductQuote(list);
        //如果是登录状态，填充自选标识
        if (SecurityUtils.isAppUser()){
            stockProductService.fillIsOption(list,getUserId());
        }
        return getDataTable(list);
    }

    /**
     * 查询自营产品每日行情数据
     */
    @GetMapping("/selfSellProductData")
    public TableDataInfo selfSellProductData(StockProduct stockProduct)
    {
        startPage();
        startOrderBy("sort is null,sort");
        stockProduct.setIsShow(0);
        stockProduct.setIsSelfSell(1);
        List<StockProduct> products = stockProductService.selectStockProductList(stockProduct);
        PageHelper.clearPage();
        Date date = new Date();
        //填充行情信息
        for (int i = 0; i < products.size(); i++) {
            StockProduct product = products.get(i);
            try {
                Map<String, Map> map = selfSellProductRealTimeMapper.selectRealTimeTradeFinalDetail(product.getProductCode(),1,date);
                product.getParams().put("data",map.get(product.getProductCode()));
            }catch (Exception e){
                throw new ServiceException("今日数据还未生成");
            }
        }
        return getDataTable(products);
    }

    /**
     * 获取股票产品信息详细信息
     */
    @GetMapping(value = "/detail")
    public AjaxResult getInfo(Long id)
    {
        StockProduct product = stockProductService.selectStockProductById(id);
        //填充行情信息
        stockProductService.fillProductQuote(product);
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
        return AjaxResult.success(stockProductService.getKLine_Echarts(code, time,timespan));
    }

    /**
     * 大盘信息
     */
    @GetMapping("/getMarket")
    public AjaxResult getMarket()
    {
        String productCodes = "AIR,ABT,ACU,AE,BKTI,ADX,AMD,AEM,APD,AAPL";
        String websiteMarketIndexProduct = CacheUtils.getOtherValueByKey("website_market_index_product", String.class);
        if (StringUtils.isNotEmpty(websiteMarketIndexProduct)){
            productCodes = websiteMarketIndexProduct;
        }
        //行情map
        Map<String, TickerInfo> tickerInfoMap = StockQuoteUtilsNew.getStockQuote(productCodes);
        String[] array = productCodes.split(",");
        List<Map<String, Object>> result = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            //产品代码
            String productCode = array[i];
            //行情信息
            TickerInfo tickerInfo = tickerInfoMap.get(productCode);
            if (tickerInfo != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("productCode",productCode);
                map.put("nowPrice",tickerInfo.getNowPrice());
                map.put("changeRate",tickerInfo.getChangeRate());
                result.add(map);
            }
        }
        return AjaxResult.success(result);
    }
}
