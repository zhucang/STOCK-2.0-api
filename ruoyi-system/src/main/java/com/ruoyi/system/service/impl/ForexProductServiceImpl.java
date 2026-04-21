package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.constant.CacheableKey;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.ticker.TickerInfo;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.cache.CacheUtil;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.domain.EchartsDataVO;
import com.ruoyi.system.domain.ForexProduct;
import com.ruoyi.system.domain.LangMgr;
import com.ruoyi.system.mapper.ForexProductMapper;
import com.ruoyi.system.service.IFastTradeOrderOptionsService;
import com.ruoyi.system.service.IForexProductService;
import com.ruoyi.system.service.IUserProductOptionService;
import com.ruoyi.system.utils.MiddleQuoteUtil;
import com.ruoyi.system.utils.ProductQuoteUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 外汇产品信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-11-01
 */
@Service
public class ForexProductServiceImpl implements IForexProductService 
{
    @Resource
    private ForexProductMapper forexProductMapper;

    @Autowired
    private IUserProductOptionService userProductOptionService;

    @Autowired
    private IFastTradeOrderOptionsService fastTradeOrderOptionsService;

    /**
     * 查询外汇产品信息
     * 
     * @param id 外汇产品信息主键
     * @return 外汇产品信息
     */
    @Override
    @Cacheable(value = CacheableKey.FOREX_PRODUCT + CacheableKey.ENTITY,key = "#id")
    public ForexProduct selectForexProductById(Long id)
    {
        return forexProductMapper.selectForexProductById(id);
    }

    /**
     * 填充行情信息
     * @param product 产品信息
     */
    @Override
    public void fillProductQuote(ForexProduct product){
        //code
        String productCode = product.getProductCode();
        //行情map
        Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getForexQuote(productCode);
        //行情信息
        TickerInfo tickerInfo = tickerInfoMap.get(product.getProductCode());
        if (tickerInfo != null) {
            product.setTickerInfo(tickerInfo);
        }
    }

    /**
     * 查询外汇产品信息
     *
     * @param productCode 产品代码
     * @return 外汇产品信息
     */
    @Override
    public ForexProduct selectForexProductByCode(String productCode){
        return forexProductMapper.selectForexProductByCode(productCode);
    }

    /**
     * 查询外汇产品信息列表
     * 
     * @param forexProduct 外汇产品信息
     * @return 外汇产品信息
     */
    @Override
    @Cacheable(value = CacheableKey.FOREX_PRODUCT + CacheableKey.LIST,key = "#forexProduct.cacheableKey()")
    public List<ForexProduct> selectForexProductList(ForexProduct forexProduct)
    {
        return  forexProductMapper.selectForexProductList(forexProduct);
    }

    /**
     * 填充行情信息
     * @param products 产品信息列表
     */
    @Override
    public void fillProductQuote(List<ForexProduct> products){
        if (products.size() == 0){
            return;
        }
        //codes
        String productCodes = products.stream().map(ForexProduct::getProductCode).collect(Collectors.joining(","));
        //行情map
        Map<String, TickerInfo> tickerInfoMap = ProductQuoteUtils.getForexQuote(productCodes);
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
    public void fillIsOption(List<ForexProduct> products, Long userId){
        if (products.size() == 0){
            return;
        }
        List<String> productCodes = products.stream().map(ForexProduct::getProductCode).collect(Collectors.toList());
        List<String> userOptionInProducts = userProductOptionService.getUserOptionInProducts(userId, productCodes,4);
        for (int i = 0; i < products.size(); i++) {
            if (userOptionInProducts.contains(products.get(i).getProductCode())){
                products.get(i).setIsOption(1);
            }else {
                products.get(i).setIsOption(0);
            }
        }
    }

    /**
     * 新增外汇产品信息
     *
     * @param products 产品信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheableKey.FOREX_PRODUCT + CacheableKey.LIST,allEntries = true)
    public int addProducts(List<ForexProduct> products){
        //productCodes
        List<String> productCodes = products.stream().map(a -> a.getProductCode()).distinct().collect(Collectors.toList());
        //获取已有的产品信息列表
        ForexProduct search = new ForexProduct();
        search.getParams().put("codes",productCodes);
        List<ForexProduct> list = forexProductMapper.selectForexProductList(search);
        //已有的产品信息map
        Map<String, ForexProduct> map = list.stream().collect(Collectors.toMap(a -> a.getProductCode(), a -> a));
        //即将插入的新产品信息列表
        List<ForexProduct> newInsertList = new ArrayList<>();
        //当前时间
        Date nowDatetime = new Date();
        //遍历获取当前未有的产品信息
        for (int i = 0; i < products.size(); i++) {
            //产品信息
            ForexProduct product = products.get(i);
            //如果还没有此产品
            if (map.get(product.getProductCode()) == null){
                product.setCreateTime(nowDatetime);
                newInsertList.add(product);
            }
        }
        if (newInsertList.size() > 0){
            int count = forexProductMapper.insertForexProducts(newInsertList);
            if (count <= 0){
                throw new ServiceException("批量插入产品信息异常");
            }
            //生成极速交易下单配置
            fastTradeOrderOptionsService.copyTemp(4,newInsertList.stream().map(a->a.getProductCode()).collect(Collectors.toList()));
            //同步新产品到中间服务器
            MiddleQuoteUtil.insertNewProductToMiddleServer(newInsertList.stream().map(a->a.getProductCode()).distinct().collect(Collectors.toList()),4);
            //日志记录产品信息列表
            HttpUtils.getRequestLogParams().put("JSONArray:productsList", JSONObject.toJSONString(newInsertList));
        }
        return 1;
    }

    /**
     * 修改外汇产品信息
     * 
     * @param forexProduct 外汇产品信息
     * @return 结果
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.FOREX_PRODUCT + CacheableKey.ENTITY,key = "#forexProduct.id"),
            @CacheEvict(value = CacheableKey.FOREX_PRODUCT + CacheableKey.LIST,allEntries = true)})
    public int updateForexProduct(ForexProduct forexProduct)
    {
        ForexProduct productVo = new ForexProduct();
        productVo.setId(forexProduct.getId());
        productVo.setProductName(forexProduct.getProductName());
        productVo.setIsLock(forexProduct.getIsLock());
        productVo.setIsShow(forexProduct.getIsShow());
        productVo.setPositionIncomeCoefficient(forexProduct.getPositionIncomeCoefficient());
        productVo.setSort(forexProduct.getSort());
        productVo.setProductNameLang(forexProduct.getProductNameLang());
        productVo.setProductImg(forexProduct.getProductImg());
        return forexProductMapper.updateForexProduct(productVo);
    }

    /**
     * 修改外汇名称多语言
     * @param productId 产品id
     * @param productNameLang 产品名称语言包
     * @return
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.FOREX_PRODUCT + CacheableKey.ENTITY,key = "#productId"),
            @CacheEvict(value = CacheableKey.FOREX_PRODUCT + CacheableKey.LIST,allEntries = true)})
    public int updateProductNameLang(Long productId, LangMgr productNameLang){
        ForexProduct productVo = new ForexProduct();
        productVo.setId(productId);
        productVo.setProductNameLang(productNameLang);
        return forexProductMapper.updateForexProduct(productVo);
    }

    /**
     * 批量删除外汇产品信息
     * 
     * @param ids 需要删除的外汇产品信息主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.FOREX_PRODUCT + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.FOREX_PRODUCT + CacheableKey.LIST,allEntries = true)})
    public int deleteForexProductByIds(Long[] ids)
    {
        ForexProduct search = new ForexProduct();
        search.getParams().put("ids", Arrays.asList(ids));
        List<ForexProduct> forexProducts = forexProductMapper.selectForexProductList(search);
        //日志记录产品信息
        HttpUtils.getRequestLogParams().put("JSONArray:forexProducts", JSONObject.toJSONString(forexProducts));
        return forexProductMapper.deleteForexProductByIds(ids);
    }

    /**
     * 删除外汇产品信息信息
     * 
     * @param id 外汇产品信息主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.FOREX_PRODUCT + CacheableKey.ENTITY,key = "#id"),
            @CacheEvict(value = CacheableKey.FOREX_PRODUCT + CacheableKey.LIST,allEntries = true)})
    public int deleteForexProductById(Long id)
    {
        return forexProductMapper.deleteForexProductById(id);
    }

    /**
     * 修改产品锁定状态
     * @param ids
     * @param status 是否锁定 0：否 1：是
     * @return
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.FOREX_PRODUCT + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.FOREX_PRODUCT + CacheableKey.LIST,allEntries = true)})
    public int updateLock(List<Long> ids, Integer status){
        int count = forexProductMapper.updateLock(ids, status);
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
            @CacheEvict(value = CacheableKey.FOREX_PRODUCT + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.FOREX_PRODUCT + CacheableKey.LIST,allEntries = true)})
    public int updateShow(List<Long> ids, Integer status){
        int count = forexProductMapper.updateShow(ids,status);
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
            @CacheEvict(value = CacheableKey.FOREX_PRODUCT + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.FOREX_PRODUCT + CacheableKey.LIST,allEntries = true)})
    public int batchUpdatePositionIncomeCoefficient(List<Long> ids, BigDecimal positionIncomeCoefficient){
        int count = forexProductMapper.batchUpdatePositionIncomeCoefficient(ids,positionIncomeCoefficient);
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
        //限制为创建聚合结果而查询的基础聚合数
        Long limit = null;
        String today = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,DateUtils.getDateBeforeOrAfterDate(new Date(),Calendar.DAY_OF_YEAR,1));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        if (timespan.equals("minute")){
            limit = 4320L*time;
            calendar.add(Calendar.MINUTE,-limit.intValue());
        }else if (timespan.equals("day")){
            //120个N天一共是limit分钟
            limit = 365L*time;
            calendar.add(Calendar.DAY_OF_YEAR,-limit.intValue());
        }
        String threeMonthAgo = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,calendar.getTime());
        JSONArray productKlineChart = MiddleQuoteUtil.getProductKlineChart(4,code, time, timespan, 200, threeMonthAgo, today, limit);
        double[][] values = new double[productKlineChart.size()][5];
        Object[][] volumes = new Object[productKlineChart.size()][3];
        String[] date = new String[productKlineChart.size()];
        //时区
        String timeZone = CacheUtil.getOtherValueByKey("jackson_time_zone",String.class);
        //时间格式
        SimpleDateFormat simpleDateFormat;
        if (timespan.equals("minute")){
            simpleDateFormat = new SimpleDateFormat(DateUtils.YYYY_MM_DD_HH_MM_SS);
        }else {
            simpleDateFormat = new SimpleDateFormat(DateUtils.YYYY_MM_DD);
        }
        //时区
        TimeZone zone = TimeZone.getTimeZone(timeZone);
        if (zone.getID().equals(timeZone)){
            simpleDateFormat.setTimeZone(zone);
        }
        for (int i = 0; i < productKlineChart.size(); i++) {
            //开盘
            String open = productKlineChart.getJSONObject(i).getString("o");
            //收盘
            String close = productKlineChart.getJSONObject(i).getString("c");
            //最高
            String high = productKlineChart.getJSONObject(i).getString("h");
            //最低
            String low = productKlineChart.getJSONObject(i).getString("l");
            //数量
            String volume = productKlineChart.getJSONObject(i).getString("v");
            //时间戳
            String timeStrap = productKlineChart.getJSONObject(i).getString("t");
            //时间戳转化时间
            String format = simpleDateFormat.format(new Date(new Long(timeStrap)));
            values[i][0] = Double.valueOf(open).doubleValue();
            values[i][1] = Double.valueOf(close).doubleValue();
            values[i][2] = Double.valueOf(high).doubleValue();
            values[i][3] = Double.valueOf(low).doubleValue();
            values[i][4] = Double.valueOf(volume).doubleValue();

            volumes[i][0] = Integer.valueOf(i);
            volumes[i][1] = Double.valueOf(volume);
            volumes[i][2] = (new BigDecimal(close)).compareTo(new BigDecimal(open)) == 1 ? 1 : -1;

            date[i] = format;
        }

        EchartsDataVO echartsDataVo = new EchartsDataVO();
        echartsDataVo.setStockCode(code);
        echartsDataVo.setValues(values);
        echartsDataVo.setVolumes(volumes);
        echartsDataVo.setDate(date);
        return AjaxResult.success(echartsDataVo);
    }
}
