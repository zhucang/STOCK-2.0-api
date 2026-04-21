package com.ruoyi.system.utils;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.ticker.TickerInfo;
import com.ruoyi.common.core.domain.ticker.TickerInfoDay;
import com.ruoyi.common.core.domain.ticker.TickerInfoMin;
import com.ruoyi.common.core.domain.ticker.TickerInfoPrevDay;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.PageUtils;
import com.ruoyi.common.utils.RandomUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.service.IProductTradeTimeSettingService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 产品行情报价工具
 */
@Component
public class ProductQuoteUtils {

    private static StockProductMapper stockProductMapper = SpringUtils.getBean(StockProductMapper.class);

    private static CryptocurrencyProductMapper cryptocurrencyProductMapper = SpringUtils.getBean(CryptocurrencyProductMapper.class);

    private static SelfSellProductRealTimeMapper selfSellProductRealTimeMapper = SpringUtils.getBean(SelfSellProductRealTimeMapper.class);

    private static StockEverydayRecordMapper stockEverydayRecordMapper = SpringUtils.getBean(StockEverydayRecordMapper.class);

    private static CryptocurrencyEverydayRecordMapper cryptocurrencyEverydayRecordMapper = SpringUtils.getBean(CryptocurrencyEverydayRecordMapper.class);

    private static NewProductApplyPurchaseMapper newProductApplyPurchaseMapper = SpringUtils.getBean(NewProductApplyPurchaseMapper.class);

    private static SelfSellProductMapper selfSellProductMapper = SpringUtils.getBean(SelfSellProductMapper.class);

    private static IProductTradeTimeSettingService productTradeTimeSettingService = SpringUtils.getBean(IProductTradeTimeSettingService.class);

    private static RedisCache redisCache = SpringUtils.getBean(RedisCache.class);

    /**
     * 获取股票的报价（现价+涨跌幅）
     * @param productCodes 产品代码，多个用逗号隔开  例： AAPL,AA,A
     * @param isGetDetail 是否获取详细报价
     * @return
     */
    public static Map<String, TickerInfo> getStockQuote(String productCodes,Boolean isGetDetail){
        Map<String, TickerInfo> map = new HashMap<>();
        if (StringUtils.isEmpty(productCodes)){
            return map;
        }
        //产品代码集合
        List<String> productCodeList = Arrays.stream(productCodes.split(",")).distinct().collect(Collectors.toList());
        //获取相应产品信息
        StockProduct search = new StockProduct();
        search.getParams().put("codes",productCodeList);
        //产品信息
        List<StockProduct> products = stockProductMapper.selectStockProductList(search);
        //自营产品
        List<String> selfSellProductCodes = products.stream().filter(a -> a.getIsSelfSell().equals(1)).map(StockProduct::getProductCode).collect(Collectors.toList());
        for (int i = 0; i < selfSellProductCodes.size(); i++) {
            String productCode = selfSellProductCodes.get(i);
            TickerInfo selfSellProductQuote;
            if (isGetDetail){
                selfSellProductQuote = getSelfProductDetailQuote(productCode, 1);
            }else {
                selfSellProductQuote = getSelfSellProductQuote(productCode, 1);
            }
            map.put(productCode,selfSellProductQuote);
        }
        //非自营产品
        productCodeList.removeAll(selfSellProductCodes);
        //获取第三方行情数据
        //行情map
        Map<String, TickerInfo> tickerInfoMap = MiddleQuoteUtil.getTickerInfosByCache(productCodeList.stream().collect(Collectors.joining(",")));
//        //未获取到数据的产品代码
//        List<String> noDataProductCodeList = productCodeList.stream().filter(a -> !tickerInfoMap.containsKey(a)).collect(Collectors.toList());
//        if (noDataProductCodeList.size() > 0){
//            //将产品信息添加进行情服务器
//            ExecutorService service = Executors.newSingleThreadExecutor();
//            service.execute(()->{
//                MiddleQuoteUtil.insertNewProductToMiddleServer(noDataProductCodeList,1);
//            });
//            service.shutdown();
//        }
        //汇总行情map
        map.putAll(tickerInfoMap);
        //填充控制的行情
        fillControlQuote(map,1);
        return map;
    }

    /**
     * 获取加密货币的简单报价（现价+涨跌幅）
     * @param productCodes 加密货币代码，多个用逗号隔开  例： X:TRXUSD,X:BTCUSD,X:CNYUSD
     * @param isGetDetail 是否获取详细报价
     * @return
     */
    public static Map<String, TickerInfo> getCryptoCurrencyQuote(String productCodes,Boolean isGetDetail){
        Map<String, TickerInfo> map = new HashMap<>();
        if (StringUtils.isEmpty(productCodes)){
            return map;
        }
        //产品代码集合
        List<String> productCodeList = Arrays.stream(productCodes.split(",")).distinct().collect(Collectors.toList());
        //获取相应产品信息
        CryptocurrencyProduct search = new CryptocurrencyProduct();
        search.getParams().put("codes",productCodeList);
        //产品信息
        List<CryptocurrencyProduct> products = cryptocurrencyProductMapper.selectCryptocurrencyProductList(search);
        //自营产品
        List<String> selfSellProductCodes = products.stream().filter(a -> a.getIsSelfSell().equals(1)).map(CryptocurrencyProduct::getProductCode).collect(Collectors.toList());
        for (int i = 0; i < selfSellProductCodes.size(); i++) {
            String productCode = selfSellProductCodes.get(i);
            TickerInfo selfSellProductQuote;
            if (isGetDetail){
                selfSellProductQuote = getSelfProductDetailQuote(productCode, 2);
            }else {
                selfSellProductQuote = getSelfSellProductQuote(productCode, 2);
            }
            map.put(productCode,selfSellProductQuote);
        }
        //非自营产品
        productCodeList.removeAll(selfSellProductCodes);
        //获取第三方行情数据
        //行情map
        Map<String, TickerInfo> tickerInfoMap = MiddleQuoteUtil.getTickerInfosByCache(productCodeList.stream().collect(Collectors.joining(",")));
//        //未获取到数据的产品代码
//        List<String> noDataProductCodeList = productCodeList.stream().filter(a -> !tickerInfoMap.containsKey(a)).collect(Collectors.toList());
//        if (noDataProductCodeList.size() > 0){
//            //将产品信息添加进行情服务器
//            ExecutorService service = Executors.newSingleThreadExecutor();
//            service.execute(()->{
//                MiddleQuoteUtil.insertNewProductToMiddleServer(noDataProductCodeList,2);
//            });
//            service.shutdown();
//        }
        //汇总行情map
        map.putAll(tickerInfoMap);
        //填充控制的行情
        fillControlQuote(map,2);
        return map;
    }

    /**
     * 获取外汇的简单报价（现价+涨跌幅）
     * @param productCodes 外汇代码，多个用逗号隔开  例： C:AEDUSD,C:ALLUSD,C:CNYUSD
     * @return
     */
    public static Map<String, TickerInfo> getForexQuote(String productCodes){
        Map<String, TickerInfo> map = new HashMap<>();
        if (StringUtils.isEmpty(productCodes)){
            return map;
        }
        //产品代码集合
        List<String> productCodeList = Arrays.stream(productCodes.split(",")).distinct().collect(Collectors.toList());
        //获取第三方行情数据
        //行情map
        Map<String, TickerInfo> tickerInfoMap = MiddleQuoteUtil.getTickerInfosByCache(productCodeList.stream().collect(Collectors.joining(",")));
//        //未获取到数据的产品代码
//        List<String> noDataProductCodeList = productCodeList.stream().filter(a -> !tickerInfoMap.containsKey(a)).collect(Collectors.toList());
//        if (noDataProductCodeList.size() > 0){
//            //将产品信息添加进行情服务器
//            ExecutorService service = Executors.newSingleThreadExecutor();
//            service.execute(()->{
//                MiddleQuoteUtil.insertNewProductToMiddleServer(noDataProductCodeList,4);
//            });
//            service.shutdown();
//        }
        //汇总行情map
        map.putAll(tickerInfoMap);
        //填充控制的行情
        fillControlQuote(map,4);
        return map;
    }

    /**
     * 获取期货的简单报价（现价+涨跌幅）
     * @param productCodes 期货代码，多个用逗号隔开  例： OUSX,ZIUSD,FCUSX
     * @return
     */
    public static Map<String, TickerInfo> getFuturesQuote(String productCodes){
        Map<String, TickerInfo> map = new HashMap<>();
        if (StringUtils.isEmpty(productCodes)){
            return map;
        }
        //行情map
        Map<String, TickerInfo> tickerInfoMap = MiddleQuoteUtil.getTickerInfosByCache(productCodes);
        //汇总行情map
        map.putAll(tickerInfoMap);
        //填充控制的行情
        fillControlQuote(map,3);
        return map;
    }

    /**
     * 获取自营产品的简单报价（现价+涨跌幅）
     * @param productCode 产品代码
     * @param productType 产品类型 1：美股  2：加密货币
     * @return
     */
    static TickerInfo getSelfSellProductQuote(String productCode,Integer productType){
        //现价
        BigDecimal nowPrice = null;
        //涨跌幅
        BigDecimal changeRate = BigDecimal.ZERO;
        //获取最近一条分时图数据
        SelfSellProductRealTime search = new SelfSellProductRealTime();
        search.setProductCode(productCode);
        search.setProductType(productType);
        search.getParams().put("KlineTime",new Date());
        PageUtils.orderBy("id desc");
        PageUtils.startPage(1,1);
        List<SelfSellProductRealTime> selfSellProductRealTimes = selfSellProductRealTimeMapper.selectSelfSellProductRealTimeList(search);
        PageUtils.clearPage();
        if (selfSellProductRealTimes.size() == 1){
            SelfSellProductRealTime selfSellProductRealTime = selfSellProductRealTimes.get(0);
            //如果是股票
            if (productType.equals(1)){
                //如果在运营时间，跳动
                //产品交易时间
                ProductTradeTimeSetting productTradeTimeSetting = productTradeTimeSettingService.selectProductTradeTimeSettingByDayAndProductType(DateUtils.getDayOfWeek(), productType);
                String am_begin = productTradeTimeSetting.getTransAmBegin();
                String am_end = productTradeTimeSetting.getTransAmEnd();
                String pm_begin = productTradeTimeSetting.getTransPmBegin();
                String pm_end = productTradeTimeSetting.getTransPmEnd();
                boolean am_flag = false;
                boolean pm_flag = false;
                try {
                    am_flag = BuyAndSellUtils.isTransTime(am_begin, am_end);
                    pm_flag = BuyAndSellUtils.isTransTime(pm_begin, pm_end);
                }catch (Exception e){
                    throw new RuntimeException(e);
                }
                if (!am_flag && !pm_flag) {
                    nowPrice = selfSellProductRealTime.getReferencePrice();
                }else {
                    //在交易时间段
                    nowPrice = RandomUtil.randomBigDecimal(selfSellProductRealTime.getLowPrice(),selfSellProductRealTime.getHighPrice()).setScale(6, Constants.BIGDECIMAL_ROUNDINGMODE);
                }
            }else if (productType.equals(2)){
                //如果是加密货币，时刻跳动
                nowPrice = RandomUtil.randomBigDecimal(selfSellProductRealTime.getLowPrice(),selfSellProductRealTime.getHighPrice()).setScale(6, Constants.BIGDECIMAL_ROUNDINGMODE);
            }
            //参考现价
            BigDecimal referencePrice = selfSellProductRealTime.getReferencePrice();
            //参考涨跌幅
            BigDecimal referenceChangeRate = selfSellProductRealTime.getReferenceChangeRate();
            //昨日收盘
            BigDecimal closePricePrev = referencePrice.divide(new BigDecimal(1).add(referenceChangeRate.divide(new BigDecimal(100), Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE)),Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            //涨跌幅
            changeRate =  nowPrice.subtract(closePricePrev).divide(closePricePrev,Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE).multiply(new BigDecimal(100));
            //如果未开市，不跳动
        }
        if (nowPrice == null){
            //如果没有分时图数据,获取其自营初始价格
            SelfSellProduct selfSellProduct = selfSellProductMapper.selectSelfSellProductByProductCodeAndProductType(productCode, productType);
            nowPrice = selfSellProduct.getInitialPrice();
        }

        TickerInfo tickerInfo = new TickerInfo();
        tickerInfo.setNowPrice(nowPrice.toString());
        tickerInfo.setChangeRate(changeRate.toString());
        return tickerInfo;
    }


    /**
     * 获取自营产品、加密货币的详情报价
     * @param productCode 产品代码
     * @param productType 产品类型 1：美股  2：加密货币
     * @return
     */
    static TickerInfo getSelfProductDetailQuote(String productCode,Integer productType){
        //今日开盘
        BigDecimal openPrice = BigDecimal.ZERO;
        //今日最高
        BigDecimal highPrice = BigDecimal.ZERO;
        //今日最低
        BigDecimal lowPrice = BigDecimal.ZERO;
        //今日交易量
        BigDecimal volumes = BigDecimal.ZERO;
        //今日交易金额
        BigDecimal amount = BigDecimal.ZERO;
        //今日均价
        BigDecimal averagePrice = BigDecimal.ZERO;
        //昨日收盘
        BigDecimal closePricePrevDay = BigDecimal.ZERO;
        //实时时间
        Date nowDateTime = new Date();
        //行情详情map
        Map<String, Map> map = selfSellProductRealTimeMapper.selectRealTimeTradeDetail(productCode, productType, nowDateTime, 0);
        //行情详情
        Map<String,BigDecimal> tradeDetail = map.get(productCode);
        if (tradeDetail != null){
            openPrice = tradeDetail.get("openPrice");
            highPrice = tradeDetail.get("highPrice");
            lowPrice = tradeDetail.get("lowPrice");
            volumes = tradeDetail.get("volumes");
            amount = tradeDetail.get("amount").setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            averagePrice = amount.divide(volumes,Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            closePricePrevDay = tradeDetail.get("closePricePrevDay");
        }
        TickerInfo tickerInfo = getSelfSellProductQuote(productCode, productType);
        TickerInfoDay tickerInfoDay = new TickerInfoDay();
        tickerInfoDay.setMaxPriceDay(highPrice.toString());
        tickerInfoDay.setMinPriceDay(lowPrice.toString());
        tickerInfoDay.setOpenPriceDay(openPrice.toString());
        tickerInfoDay.setVolumeDay(volumes.toString());
        tickerInfoDay.setAveragePriceDay(averagePrice.toString());
        tickerInfoDay.setTradeAmount(amount.toString());
        TickerInfoPrevDay tickerInfoPrevDay = new TickerInfoPrevDay();
        tickerInfoPrevDay.setClosePricePrevDay(closePricePrevDay==null?null:closePricePrevDay.toString());
        tickerInfo.setTickerInfoDay(tickerInfoDay);
        tickerInfo.setTickerInfoPrevDay(tickerInfoPrevDay);
        TickerInfoMin tickerInfoMin = new TickerInfoMin();
        tickerInfoMin.setVolumeMin(BigDecimal.ZERO.toString());
        tickerInfoMin.setAveragePriceMin(BigDecimal.ZERO.toString());
        tickerInfo.setTickerInfoMin(tickerInfoMin);
        return tickerInfo;
    }

    /**
     * 控制的行情
     * @param tickerInfoMap 行情报价
     * @return
     */
    public static void fillControlQuote(Map<String,TickerInfo> tickerInfoMap,Integer productType){
        for (Map.Entry<String,TickerInfo> entryMap : tickerInfoMap.entrySet()) {
            //产品代码
            String productCode = entryMap.getKey();
            //行情信息
            TickerInfo tickerInfo = entryMap.getValue();
            //实时行情信息
            Date nowDateTime = new Date();
            try{
                //控制的行情
                Map<String, String> map = redisCache.getCacheObject("productQuoteControlCache:" + productCode + "/" +productType);
                if (map != null){
                    Object o = map.get(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,nowDateTime));
                    if (o != null){
                        String[] split = String.valueOf(o).split("/");
                        BigDecimal nowPrice = new BigDecimal(split[0]);
                        BigDecimal changeRate = new BigDecimal(split[1]);
                        tickerInfo.setNowPrice(nowPrice.toString());
                        tickerInfo.setChangeRate(changeRate.toString());
                    }
                }
            }catch (Exception e){

            }
        }
    }
}
