package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.constant.CacheableKey;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.ticker.TickerInfo;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.cache.CacheUtil;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.domain.EchartsDataVO;
import com.ruoyi.system.domain.FuturesProduct;
import com.ruoyi.system.domain.LangMgr;
import com.ruoyi.system.mapper.FuturesProductMapper;
import com.ruoyi.system.service.IFuturesProductService;
import com.ruoyi.system.service.IUserProductOptionService;
import com.ruoyi.system.utils.MiddleQuoteUtil;
import com.ruoyi.system.utils.ProductQuoteUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 期货产品信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-11-01
 */
@Service
public class FuturesProductServiceImpl implements IFuturesProductService 
{
    @Resource
    private FuturesProductMapper futuresProductMapper;

    @Autowired
    private IUserProductOptionService userProductOptionService;

    /**
     * 查询期货产品信息
     * 
     * @param id 期货产品信息主键
     * @return 期货产品信息
     */
    @Override
    @Cacheable(value = CacheableKey.FUTURES_PRODUCT + CacheableKey.ENTITY,key = "#id")
    public FuturesProduct selectFuturesProductById(Long id)
    {
        return futuresProductMapper.selectFuturesProductById(id);
    }

    /**
     * 填充行情信息
     * @param product 产品信息
     */
    @Override
    public void fillProductQuote(FuturesProduct product){
        //code
        String productCode = product.getProductCode();
        //行情map
        Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getFuturesQuote(productCode);
        //行情信息
        TickerInfo tickerInfo = tickerInfoMap.get(product.getProductCode());
        if (tickerInfo != null) {
            product.setTickerInfo(tickerInfo);
        }
    }

    /**
     * 查询期货产品信息
     *
     * @param productCode 产品代码
     * @return 期货产品信息
     */
    @Override
    public FuturesProduct selectFuturesProductByCode(String productCode){
        return futuresProductMapper.selectFuturesProductByCode(productCode);
    }

    /**
     * 查询期货产品信息列表
     * 
     * @param futuresProduct 期货产品信息
     * @return 期货产品信息
     */
    @Override
    @Cacheable(value = CacheableKey.FUTURES_PRODUCT + CacheableKey.LIST,key = "#futuresProduct.cacheableKey()")
    public List<FuturesProduct> selectFuturesProductList(FuturesProduct futuresProduct)
    {
        return futuresProductMapper.selectFuturesProductList(futuresProduct);
    }

    /**
     * 填充行情信息
     * @param products 产品信息列表
     */
    @Override
    public void fillProductQuote(List<FuturesProduct> products){
        if (products.size() == 0){
            return;
        }
        //codes
        String productCodes = products.stream().map(FuturesProduct::getProductCode).collect(Collectors.joining(","));
        //行情map
        Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getFuturesQuote(productCodes);
        for (int i = 0; i < products.size(); i++) {
            //行情信息
            TickerInfo tickerInfo = tickerInfoMap.get(products.get(i).getProductCode());
            if (tickerInfo != null) {
                products.get(i).setTickerInfo(tickerInfo);
            }
        }
    }

    /**
     * 填充产品自选标识
     * @param products 产品信息列表
     * @param userId 用户id
     */
    @Override
    public void fillIsOption(List<FuturesProduct> products, Long userId){
        if (products.size() == 0){
            return;
        }
        List<String> productCodes = products.stream().map(FuturesProduct::getProductCode).collect(Collectors.toList());
        List<String> userOptionInProducts = userProductOptionService.getUserOptionInProducts(userId, productCodes,3);
        for (int i = 0; i < products.size(); i++) {
            if (userOptionInProducts.contains(products.get(i).getProductCode())){
                products.get(i).setIsOption(1);
            }else {
                products.get(i).setIsOption(0);
            }
        }
    }

    /**
     * 修改期货产品信息
     * 
     * @param futuresProduct 期货产品信息
     * @return 结果
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.FUTURES_PRODUCT + CacheableKey.ENTITY,key = "#futuresProduct.id"),
            @CacheEvict(value = CacheableKey.FUTURES_PRODUCT + CacheableKey.LIST,allEntries = true)})
    public int updateFuturesProduct(FuturesProduct futuresProduct)
    {
        FuturesProduct productVo = new FuturesProduct();
        productVo.setId(futuresProduct.getId());
        productVo.setProductName(futuresProduct.getProductName());
        productVo.setIsLock(futuresProduct.getIsLock());
        productVo.setIsShow(futuresProduct.getIsShow());
        productVo.setPositionIncomeCoefficient(futuresProduct.getPositionIncomeCoefficient());
        productVo.setSort(futuresProduct.getSort());
        productVo.setProductNameLang(futuresProduct.getProductNameLang());
        productVo.setProductImg(futuresProduct.getProductImg());
        return futuresProductMapper.updateFuturesProduct(productVo);
    }

    /**
     * 修改期货名称多语言
     * @param productId 产品id
     * @param productNameLang 产品名称语言包
     * @return
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.FUTURES_PRODUCT + CacheableKey.ENTITY,key = "#productId"),
            @CacheEvict(value = CacheableKey.FUTURES_PRODUCT + CacheableKey.LIST,allEntries = true)})
    public int updateProductNameLang(Long productId, LangMgr productNameLang){
        FuturesProduct productVo = new FuturesProduct();
        productVo.setId(productId);
        productVo.setProductNameLang(productNameLang);
        return futuresProductMapper.updateFuturesProduct(productVo);
    }

    /**
     * 批量删除期货产品信息
     * 
     * @param ids 需要删除的期货产品信息主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.FUTURES_PRODUCT + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.FUTURES_PRODUCT + CacheableKey.LIST,allEntries = true)})
    public int deleteFuturesProductByIds(Long[] ids)
    {
        FuturesProduct search = new FuturesProduct();
        search.getParams().put("ids", Arrays.asList(ids));
        List<FuturesProduct> futuresProducts = futuresProductMapper.selectFuturesProductList(search);
        //日志记录产品信息
        HttpUtils.getRequestLogParams().put("JSONArray:futuresProducts", JSONObject.toJSONString(futuresProducts));
        return futuresProductMapper.deleteFuturesProductByIds(ids);
    }

    /**
     * 删除期货产品信息信息
     * 
     * @param id 期货产品信息主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.FUTURES_PRODUCT + CacheableKey.ENTITY,key = "#id"),
            @CacheEvict(value = CacheableKey.FUTURES_PRODUCT + CacheableKey.LIST,allEntries = true)})
    public int deleteFuturesProductById(Long id)
    {
        return futuresProductMapper.deleteFuturesProductById(id);
    }

    /**
     * 修改产品锁定状态
     * @param ids
     * @param status 是否锁定 0：否 1：是
     * @return
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.FUTURES_PRODUCT + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.FUTURES_PRODUCT + CacheableKey.LIST,allEntries = true)})
    public int updateLock(List<Long> ids, Integer status){
        int count = futuresProductMapper.updateLock(ids, status);
        if (ids.size() != count){
            throw new RuntimeException("系统繁忙");
        }
        return 1;
    }

    /**
     * 修改产品显示状态
     * @param ids
     * @param status 是否显示 0：是 1：否
     * @return
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.FUTURES_PRODUCT + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.FUTURES_PRODUCT + CacheableKey.LIST,allEntries = true)})
    public int updateShow(List<Long> ids, Integer status){
        int count = futuresProductMapper.updateShow(ids,status);
        if (ids.size() != count){
            throw new RuntimeException("系统繁忙");
        }
        return 1;
    }

    /**
     * 批量修改产品合约收益系数
     * @param ids
     * @param positionIncomeCoefficient
     * @return
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.FUTURES_PRODUCT + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.FUTURES_PRODUCT + CacheableKey.LIST,allEntries = true)})
    public int batchUpdatePositionIncomeCoefficient(List<Long> ids, BigDecimal positionIncomeCoefficient){
        int count = futuresProductMapper.batchUpdatePositionIncomeCoefficient(ids,positionIncomeCoefficient);
        if (ids.size() != count){
            throw new RuntimeException("系统繁忙");
        }
        return 1;
    }

    /**
     * 获取K线
     * @param code 产品代码
     * @param time 时间跨度
     * @param timespan 时间类型 (minute，day，hour，week。。。。)
     * @return
     */
    @Override
    public AjaxResult getKLine_Echarts(String code,Integer time,String timespan){
        if (timespan.equals("minute")){
            timespan = "min";
        }
        JSONArray futureKlineChart = MiddleQuoteUtil.getFutureKlineChart(code, time + timespan, null);
        //倒序
        Collections.reverse(futureKlineChart);
        double[][] values = new double[futureKlineChart.size()][5];
        Object[][] volumes = new Object[futureKlineChart.size()][3];
        String[] date = new String[futureKlineChart.size()];
        //展示时区
        String jacksonTimeZone = CacheUtil.getOtherValueByKey("jackson_time_zone", String.class);
        for (int i = 0; i < futureKlineChart.size(); i++) {
            //开盘
            String open = futureKlineChart.getJSONObject(i).getString("open");
            //收盘
            String close = futureKlineChart.getJSONObject(i).getString("close");
            //最高
            String high = futureKlineChart.getJSONObject(i).getString("high");
            //最低
            String low = futureKlineChart.getJSONObject(i).getString("low");
            //数量
            String volume = futureKlineChart.getJSONObject(i).getString("volume");
            //时间戳
            String dateTime = futureKlineChart.getJSONObject(i).getString("date");

            values[i][0] = Double.valueOf(open).doubleValue();
            values[i][1] = Double.valueOf(close).doubleValue();
            values[i][2] = Double.valueOf(high).doubleValue();
            values[i][3] = Double.valueOf(low).doubleValue();
            values[i][4] = Double.valueOf(volume).doubleValue();

            volumes[i][0] = Integer.valueOf(i);
            volumes[i][1] = Double.valueOf(volume);
            volumes[i][2] = (new BigDecimal(close)).compareTo(new BigDecimal(open)) == 1 ? 1 : -1;
            date[i] = dateTime;
            date[i] = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, DateUtils.dateTime(DateUtils.YYYY_MM_DD_HH_MM_SS,dateTime),jacksonTimeZone);
        }

        //x
        EchartsDataVO echartsDataVo = new EchartsDataVO();
        echartsDataVo.setStockCode(code);
        echartsDataVo.setValues(values);
        echartsDataVo.setVolumes(volumes);
        echartsDataVo.setDate(date);
        return AjaxResult.success(echartsDataVo);
    }
}
