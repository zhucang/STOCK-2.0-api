package com.ruoyi.system.utils;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.utils.PageUtils;
import com.ruoyi.common.utils.RandomUtil;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.domain.*;
import com.ruoyi.common.core.domain.ticker.TickerInfo;
import com.ruoyi.common.core.domain.ticker.TickerInfoDay;
import com.ruoyi.common.core.domain.ticker.TickerInfoMin;
import com.ruoyi.common.core.domain.ticker.TickerInfoPrevDay;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.service.IProductTradeTimeSettingService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class StockQuoteUtilsNew {

    private static StockProductMapper stockProductMapper = SpringUtils.getBean(StockProductMapper.class);

    private static CryptocurrencyProductMapper cryptocurrencyProductMapper = SpringUtils.getBean(CryptocurrencyProductMapper.class);

    private static SelfSellProductRealTimeMapper selfSellProductRealTimeMapper = SpringUtils.getBean(SelfSellProductRealTimeMapper.class);

    private static StockEverydayRecordMapper stockEverydayRecordMapper = SpringUtils.getBean(StockEverydayRecordMapper.class);

    private static CryptocurrencyEverydayRecordMapper cryptocurrencyEverydayRecordMapper = SpringUtils.getBean(CryptocurrencyEverydayRecordMapper.class);

    private static NewProductApplyPurchaseMapper newProductApplyPurchaseMapper = SpringUtils.getBean(NewProductApplyPurchaseMapper.class);

    private static ForexEverydayRecordMapper forexEverydayRecordMapper = SpringUtils.getBean(ForexEverydayRecordMapper.class);
    private static SelfSellProductMapper selfSellProductMapper = SpringUtils.getBean(SelfSellProductMapper.class);


    private static IProductTradeTimeSettingService productTradeTimeSettingService = SpringUtils.getBean(IProductTradeTimeSettingService.class);

    private static RedisCache redisCache = SpringUtils.getBean(RedisCache.class);

    /**
     * 获取股票的简单报价（现价+涨跌幅）
     * @param stockCodes 股票代码，多个用逗号隔开  例： AAPL,AA,A
     * @return
     */
    public static Map<String, TickerInfo> getStockQuote(String stockCodes){
        Map<String, TickerInfo> map = new HashMap<>();
        if (StringUtils.isEmpty(stockCodes)){
            return map;
        }
        String[] split = stockCodes.split(",");
        //股票代码集合
        List<String> stockCodeList = Arrays.stream(split).distinct().collect(Collectors.toList());

        //所有股票信息
        StockProduct stockProduct = new StockProduct();
        Map<String, Object> params = new HashMap<>();
        params.put("codes",stockCodeList);
        stockProduct.setParams(params);
        List<StockProduct> stocks = stockProductMapper.selectStockProductList(stockProduct);
        //自营产品
        List<String> selfSellStockCodes = stocks.stream().filter(a -> a.getIsSelfSell().equals(1)).map(StockProduct::getProductCode).collect(Collectors.toList());
        for (int i = 0; i < selfSellStockCodes.size(); i++) {
            String stockCode = selfSellStockCodes.get(i);
            TickerInfo selfStockQuote = getSelfStockQuote(stockCode, 1);
            map.put(stockCode,selfStockQuote);
        }
        //非自营产品
        stockCodeList.removeAll(selfSellStockCodes);
        //如果不是自营
        //获取第三方行情数据
        Map<String, TickerInfo> tickerInfoMap = PolygonUtils.getInfosByTickers(stockCodeList.stream().collect(Collectors.joining(",")),1);
        for (int i = 0; i < stockCodeList.size(); i++) {
            String stockCode = stockCodeList.get(i);
            //如果没有获取到实时行情
            if (!tickerInfoMap.containsKey(stockCode)){
                //取昨日数据
                TickerInfo tickerInfo = new TickerInfo();
                StockEverydayRecord stockMarketsDay = stockEverydayRecordMapper.selectLastRecordByProductCode(stockCode);
                if (stockMarketsDay != null){
                    BigDecimal nowPrice = stockMarketsDay.getClosePrice();
                    tickerInfo.setNowPrice(String.valueOf(nowPrice));
                    tickerInfo.setChangeRate(BigDecimal.ZERO.toString());
                }else {
                    tickerInfo.setNowPrice(BigDecimal.ZERO.toString());
                    tickerInfo.setChangeRate(BigDecimal.ZERO.toString());
                }
                tickerInfoMap.put(stockCode,tickerInfo);
            }
        }
        map.putAll(tickerInfoMap);
        fillControlQuote(map,1);
        return map;
    }

    /**
     * 获取加密货币的简单报价（现价+涨跌幅）
     * @param stockCodes 股票代码，多个用逗号隔开  例： X:TRXUSD,X:BTCUSD,X:CNYUSD
     * @return
     */
    public static Map<String, TickerInfo> getCryptoCurrencyQuote(String stockCodes){
        Map<String, TickerInfo> map = new HashMap<>();
        if (StringUtils.isEmpty(stockCodes)){
            return map;
        }
        String[] split = stockCodes.split(",");
        //股票代码集合
        List<String> stockCodeList = Arrays.stream(split).distinct().collect(Collectors.toList());

        //所有加密货币信息
        CryptocurrencyProduct cryptocurrencyProduct = new CryptocurrencyProduct();
        Map<String, Object> params = new HashMap<>();
        params.put("codes",stockCodeList);
        cryptocurrencyProduct.setParams(params);
        List<CryptocurrencyProduct> stocks = cryptocurrencyProductMapper.selectCryptocurrencyProductList(cryptocurrencyProduct);
        //自营产品
        List<String> selfSellStockCodes = stocks.stream().filter(a -> a.getIsSelfSell().equals(1)).map(CryptocurrencyProduct::getProductCode).collect(Collectors.toList());
        for (int i = 0; i < selfSellStockCodes.size(); i++) {
            String stockCode = selfSellStockCodes.get(i);
            TickerInfo selfStockQuote = getSelfStockQuote(stockCode, 2);
            map.put(stockCode,selfStockQuote);
        }
        //非自营产品
        stockCodeList.removeAll(selfSellStockCodes);
        //如果不是自营
        //获取第三方行情数据
        Map<String, TickerInfo> tickerInfoMap = PolygonUtils.getInfosByTickers(stockCodeList.stream().collect(Collectors.joining(",")),2);
        for (int i = 0; i < stockCodeList.size(); i++) {
            String stockCode = stockCodeList.get(i);
            //如果没有获取到实时行情
            if (!tickerInfoMap.containsKey(stockCode)){
                //取昨日数据
                TickerInfo tickerInfo = new TickerInfo();
                CryptocurrencyEverydayRecord cryptocurrencyEverydayRecord = cryptocurrencyEverydayRecordMapper.selectLastRecordByProductCode(stockCode);
                if (cryptocurrencyEverydayRecord != null){
                    BigDecimal nowPrice = cryptocurrencyEverydayRecord.getClosePrice();
                    tickerInfo.setNowPrice(nowPrice.toString());
                    tickerInfo.setChangeRate(BigDecimal.ZERO.toString());
                }else {
                    tickerInfo.setNowPrice(BigDecimal.ZERO.toString());
                    tickerInfo.setChangeRate(BigDecimal.ZERO.toString());
                }
                tickerInfoMap.put(stockCode,tickerInfo);
            }
        }
        map.putAll(tickerInfoMap);
        fillControlQuote(map,2);
        return map;
    }

    /**
     * 获取外汇的简单报价（现价+涨跌幅）
     * @param stockCodes 股票代码，多个用逗号隔开  例： C:AEDUSD,C:ALLUSD,C:CNYUSD
     * @return
     */
    public static Map<String, TickerInfo> getForexQuote(String stockCodes){
        Map<String, TickerInfo> map = new HashMap<>();
        if (StringUtils.isEmpty(stockCodes)){
            return map;
        }
        String[] split = stockCodes.split(",");
        //股票代码集合
        List<String> stockCodeList = Arrays.stream(split).distinct().collect(Collectors.toList());
        //获取第三方行情数据
        Map<String, TickerInfo> tickerInfoMap = PolygonUtils.getInfosByTickers(stockCodeList.stream().collect(Collectors.joining(",")),4);
        for (int i = 0; i < stockCodeList.size(); i++) {
            String stockCode = stockCodeList.get(i);
            //如果没有获取到实时行情
            if (!tickerInfoMap.containsKey(stockCode)){
                //取昨日数据
                TickerInfo tickerInfo = new TickerInfo();
                ForexEverydayRecord forexEverydayRecord = forexEverydayRecordMapper.selectLastRecordByProductCode(stockCode);
                if (forexEverydayRecord != null){
                    BigDecimal nowPrice = forexEverydayRecord.getClosePrice();
                    tickerInfo.setNowPrice(nowPrice.toString());
                    tickerInfo.setChangeRate(BigDecimal.ZERO.toString());
                }else {
                    tickerInfo.setNowPrice(BigDecimal.ZERO.toString());
                    tickerInfo.setChangeRate(BigDecimal.ZERO.toString());
                }
                tickerInfoMap.put(stockCode,tickerInfo);
            }
        }
        map.putAll(tickerInfoMap);
        fillControlQuote(map,4);
        return map;
    }

    /**
     * 获取期货的简单报价（现价+涨跌幅）
     * @param stockCodes 股票代码，多个用逗号隔开  例： OUSX,ZIUSD,FCUSX
     * @return
     */
    public static Map<String, TickerInfo> getFuturesQuote(String stockCodes){
        Map<String, TickerInfo> map = new HashMap<>();
        if (StringUtils.isEmpty(stockCodes)){
            return map;
        }
        map = FuturesMarketUtil.getFuturesQuote(stockCodes);
        fillControlQuote(map,3);
        return map;
    }

    /**
     * 获取自营产品、加密货币的简单报价（现价+涨跌幅）
     * @param stockCode 股票代码
     * @param stockType 股票类型 1：美股  2：加密货币
     * @return
     */
    static TickerInfo getSelfStockQuote(String stockCode,Integer stockType){
        //现价
        BigDecimal nowPrice = null;
        //涨跌幅
        BigDecimal changeRate = BigDecimal.ZERO;
        //获取最近一条分时图数据
        SelfSellProductRealTime search = new SelfSellProductRealTime();
        search.setProductCode(stockCode);
        search.setProductType(stockType);
        search.getParams().put("KlineTime",new Date());
        PageUtils.orderBy("id desc");
        PageUtils.startPage(1,1);
        List<SelfSellProductRealTime> selfSellProductRealTimes = selfSellProductRealTimeMapper.selectSelfSellProductRealTimeList(search);
        PageUtils.clearPage();
        if (selfSellProductRealTimes.size() == 1){
            SelfSellProductRealTime selfSellProductRealTime = selfSellProductRealTimes.get(0);
            //如果是股票
            if (stockType.equals(1)){
                //如果在运营时间，跳动
                //产品交易时间
                ProductTradeTimeSetting productTradeTimeSetting = productTradeTimeSettingService.selectProductTradeTimeSettingByDayAndProductType(DateUtils.getDayOfWeek(), stockType);
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
                    nowPrice = RandomUtil.randomBigDecimal(selfSellProductRealTime.getLowPrice(),selfSellProductRealTime.getHighPrice()).setScale(6, Constants.BIGDECIMAL_ROUNDINGMODE);;
                }
            }else if (stockType.equals(2)){
                //如果是加密货币，时刻跳动
                nowPrice = RandomUtil.randomBigDecimal(selfSellProductRealTime.getLowPrice(),selfSellProductRealTime.getHighPrice()).setScale(6, Constants.BIGDECIMAL_ROUNDINGMODE);;
            }
            //参考现价
            BigDecimal referencePrice = selfSellProductRealTime.getReferencePrice();
            //参考涨跌幅
            BigDecimal referenceChangeRate = selfSellProductRealTime.getReferenceChangeRate();
            //昨日收盘
            BigDecimal closePricePrev = referencePrice.divide(new BigDecimal(1).add(referenceChangeRate.divide(new BigDecimal(100),Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE)),Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            //涨跌幅
            changeRate =  nowPrice.subtract(closePricePrev).divide(closePricePrev,Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE).multiply(new BigDecimal(100));
            //如果未开市，不跳动
        }
        if (nowPrice == null){
            //如果没有分时图数据,获取其自营初始价格
            SelfSellProduct selfSellProduct = selfSellProductMapper.selectSelfSellProductByProductCodeAndProductType(stockCode, stockType);
            nowPrice = selfSellProduct.getInitialPrice();
        }
        TickerInfo tickerInfo = new TickerInfo();
        tickerInfo.setNowPrice(nowPrice.toString());
        tickerInfo.setChangeRate(changeRate.toString());
        return tickerInfo;
    }











    //=======================================================================================




    /**
     * 获取股票的详情报价
     * @param stockCodes 股票代码，多个用逗号隔开  例： AAPL,AA,A
     * @return
     */
    public static Map<String, TickerInfo> getStockDetailQuote(String stockCodes){
        Map<String, TickerInfo> map = new HashMap<>();
        if (StringUtils.isEmpty(stockCodes)){
            return map;
        }
        String[] split = stockCodes.split(",");
        //股票代码集合
        List<String> stockCodeList = Arrays.stream(split).distinct().collect(Collectors.toList());

        //所有股票信息
        StockProduct stockProduct = new StockProduct();
        Map<String, Object> params = new HashMap<>();
        params.put("codes",stockCodeList);
        stockProduct.setParams(params);
        List<StockProduct> stocks = stockProductMapper.selectStockProductList(stockProduct);
        //自营产品
        List<String> selfSellStockCodes = stocks.stream().filter(a -> a.getIsSelfSell().equals(1)).map(StockProduct::getProductCode).collect(Collectors.toList());
        for (int i = 0; i < selfSellStockCodes.size(); i++) {
            String stockCode = selfSellStockCodes.get(i);
            TickerInfo selfStockQuote = getSelfStockDetailQuote(stockCode, 1);
            map.put(stockCode,selfStockQuote);
        }
        //非自营产品
        stockCodeList.removeAll(selfSellStockCodes);
        //如果不是自营
        //获取第三方行情数据
        Map<String, TickerInfo> tickerInfoMap = PolygonUtils.getInfosByTickers(stockCodeList.stream().collect(Collectors.joining(",")),1);
        for (int i = 0; i < stockCodeList.size(); i++) {
            String stockCode = stockCodeList.get(i);
            //如果没有获取到实时行情
            if (!tickerInfoMap.containsKey(stockCode)){
                //取昨日数据
                TickerInfo tickerInfo = new TickerInfo();
                TickerInfoDay tickerInfoDay = new TickerInfoDay();
                TickerInfoPrevDay tickerInfoPrevDay = new TickerInfoPrevDay();
                TickerInfoMin tickerInfoMin = new TickerInfoMin();
                StockEverydayRecord stockMarketsDay = stockEverydayRecordMapper.selectLastRecordByProductCode(stockCode);
                if (stockMarketsDay != null){
                    String nowPrice = String.valueOf(stockMarketsDay.getClosePrice());
                    tickerInfo.setNowPrice(nowPrice);
                    tickerInfo.setChangeRate(BigDecimal.ZERO.toString());
                    tickerInfoDay.setMaxPriceDay(nowPrice);
                    tickerInfoDay.setMinPriceDay(nowPrice);
                    tickerInfoDay.setOpenPriceDay(nowPrice);
                    tickerInfoDay.setVolumeDay(BigDecimal.ZERO.toString());
                    tickerInfoDay.setAveragePriceDay(BigDecimal.ZERO.toString());
                    tickerInfoDay.setTradeAmount(BigDecimal.ZERO.toString());
                    tickerInfoPrevDay.setClosePricePrevDay(nowPrice);
                    tickerInfoMin.setVolumeMin(BigDecimal.ZERO.toString());
                    tickerInfoMin.setAveragePriceMin(nowPrice);
                }else {
                    tickerInfo.setNowPrice(BigDecimal.ZERO.toString());
                    tickerInfo.setChangeRate(BigDecimal.ZERO.toString());
                    tickerInfoDay.setMaxPriceDay(BigDecimal.ZERO.toString());
                    tickerInfoDay.setMinPriceDay(BigDecimal.ZERO.toString());
                    tickerInfoDay.setOpenPriceDay(BigDecimal.ZERO.toString());
                    tickerInfoDay.setVolumeDay(BigDecimal.ZERO.toString());
                    tickerInfoDay.setAveragePriceDay(BigDecimal.ZERO.toString());
                    tickerInfoDay.setTradeAmount(BigDecimal.ZERO.toString());
                    tickerInfoPrevDay.setClosePricePrevDay(BigDecimal.ZERO.toString());
                    tickerInfoMin.setVolumeMin(BigDecimal.ZERO.toString());
                    tickerInfoMin.setAveragePriceMin(BigDecimal.ZERO.toString());
                }
                tickerInfo.setTickerInfoDay(tickerInfoDay);
                tickerInfo.setTickerInfoPrevDay(tickerInfoPrevDay);
                tickerInfo.setTickerInfoMin(tickerInfoMin);
                tickerInfoMap.put(stockCode,tickerInfo);
            }
        }
        map.putAll(tickerInfoMap);
        fillControlQuote(map,1);
        return map;
    }


    /**
     * 获取加密货币的详情报价
     * @param stockCodes 股票代码，多个用逗号隔开  例： X:TRXUSD,X:BTCUSD,X:CNYUSD
     * @return
     */
    public static Map<String, TickerInfo> getCryptoCurrencyDetailQuote(String stockCodes){
        Map<String, TickerInfo> map = new HashMap<>();
        if (StringUtils.isEmpty(stockCodes)){
            return map;
        }
        String[] split = stockCodes.split(",");
        //股票代码集合
        List<String> stockCodeList = Arrays.stream(split).distinct().collect(Collectors.toList());

        //所有加密货币信息
        CryptocurrencyProduct cryptocurrencyProduct = new CryptocurrencyProduct();
        Map<String, Object> params = new HashMap<>();
        params.put("codes",stockCodeList);
        cryptocurrencyProduct.setParams(params);
        List<CryptocurrencyProduct> stocks = cryptocurrencyProductMapper.selectCryptocurrencyProductList(cryptocurrencyProduct);
        //自营产品
        List<String> selfSellStockCodes = stocks.stream().filter(a -> a.getIsSelfSell().equals(1)).map(CryptocurrencyProduct::getProductCode).collect(Collectors.toList());
        for (int i = 0; i < selfSellStockCodes.size(); i++) {
            String stockCode = selfSellStockCodes.get(i);
            TickerInfo selfStockQuote = getSelfStockDetailQuote(stockCode, 2);
            map.put(stockCode,selfStockQuote);
        }
        //非自营产品
        stockCodeList.removeAll(selfSellStockCodes);
        //如果不是自营
        //获取第三方行情数据
        Map<String, TickerInfo> tickerInfoMap = PolygonUtils.getInfosByTickers(stockCodeList.stream().collect(Collectors.joining(",")),2);
        for (int i = 0; i < stockCodeList.size(); i++) {
            String stockCode = stockCodeList.get(i);
            //如果没有获取到实时行情
            if (!tickerInfoMap.containsKey(stockCode)){
                //取昨日数据
                TickerInfo tickerInfo = new TickerInfo();
                TickerInfoDay tickerInfoDay = new TickerInfoDay();
                TickerInfoPrevDay tickerInfoPrevDay = new TickerInfoPrevDay();
                TickerInfoMin tickerInfoMin = new TickerInfoMin();
                //取昨日数据
                CryptocurrencyEverydayRecord cryptocurrencyEverydayRecord = cryptocurrencyEverydayRecordMapper.selectLastRecordByProductCode(stockCode);
                if (cryptocurrencyEverydayRecord != null){
                    String nowPrice = cryptocurrencyEverydayRecord.getClosePrice().toString();
                    tickerInfo.setNowPrice(nowPrice);
                    tickerInfo.setChangeRate(BigDecimal.ZERO.toString());
                    tickerInfoDay.setMaxPriceDay(nowPrice);
                    tickerInfoDay.setMinPriceDay(nowPrice);
                    tickerInfoDay.setOpenPriceDay(nowPrice);
                    tickerInfoDay.setVolumeDay(BigDecimal.ZERO.toString());
                    tickerInfoDay.setTradeAmount(BigDecimal.ZERO.toString());
                    tickerInfoPrevDay.setClosePricePrevDay(nowPrice);
                    tickerInfoMin.setVolumeMin(BigDecimal.ZERO.toString());
                    tickerInfoMin.setAveragePriceMin(nowPrice);
                }else {
                    tickerInfo.setNowPrice(BigDecimal.ZERO.toString());
                    tickerInfo.setChangeRate(BigDecimal.ZERO.toString());
                    tickerInfoDay.setMaxPriceDay(BigDecimal.ZERO.toString());
                    tickerInfoDay.setMinPriceDay(BigDecimal.ZERO.toString());
                    tickerInfoDay.setOpenPriceDay(BigDecimal.ZERO.toString());
                    tickerInfoDay.setVolumeDay(BigDecimal.ZERO.toString());
                    tickerInfoDay.setTradeAmount(BigDecimal.ZERO.toString());
                    tickerInfoPrevDay.setClosePricePrevDay(BigDecimal.ZERO.toString());
                    tickerInfoMin.setVolumeMin(BigDecimal.ZERO.toString());
                    tickerInfoMin.setAveragePriceMin(BigDecimal.ZERO.toString());
                }
                tickerInfo.setTickerInfoDay(tickerInfoDay);
                tickerInfo.setTickerInfoPrevDay(tickerInfoPrevDay);
                tickerInfo.setTickerInfoMin(tickerInfoMin);
                tickerInfoMap.put(stockCode,tickerInfo);
            }
        }
        map.putAll(tickerInfoMap);
        fillControlQuote(map,2);
        return map;
    }



    /**
     * 获取自营产品、加密货币的详情报价
     * @param stockCode 股票代码
     * @param stockType 股票类型 1：美股  2：加密货币
     * @return
     */
    static TickerInfo getSelfStockDetailQuote(String stockCode,Integer stockType){
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
        Map<String, Map> map = selfSellProductRealTimeMapper.selectRealTimeTradeDetail(stockCode, stockType, nowDateTime, 0);
        //行情详情
        Map<String,BigDecimal> tradeDetail = map.get(stockCode);
        if (tradeDetail != null){
            openPrice = tradeDetail.get("openPrice");
            highPrice = tradeDetail.get("highPrice");
            lowPrice = tradeDetail.get("lowPrice");
            volumes = tradeDetail.get("volumes");
            amount = tradeDetail.get("amount").setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            averagePrice = amount.divide(volumes,Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            closePricePrevDay = tradeDetail.get("closePricePrevDay");
        }
        TickerInfo tickerInfo = getSelfStockQuote(stockCode, stockType);
        TickerInfoDay tickerInfoDay = new TickerInfoDay();
        tickerInfoDay.setMaxPriceDay(highPrice.toString());
        tickerInfoDay.setMinPriceDay(lowPrice.toString());
        tickerInfoDay.setOpenPriceDay(openPrice.toString());
        tickerInfoDay.setVolumeDay(volumes.toString());
        tickerInfoDay.setAveragePriceDay(averagePrice.toString());
        tickerInfoDay.setTradeAmount(amount.toString());
        TickerInfoPrevDay tickerInfoPrevDay = new TickerInfoPrevDay();
        tickerInfoPrevDay.setClosePricePrevDay(closePricePrevDay.toString());
        tickerInfo.setTickerInfoDay(tickerInfoDay);
        tickerInfo.setTickerInfoPrevDay(tickerInfoPrevDay);
        TickerInfoMin tickerInfoMin = new TickerInfoMin();
        tickerInfoMin.setVolumeMin(BigDecimal.ZERO.toString());
        tickerInfoMin.setAveragePriceMin(BigDecimal.ZERO.toString());
        tickerInfo.setTickerInfoMin(tickerInfoMin);
        return tickerInfo;
    }

    /**
     * 获取外汇的详情报价
     * @param stockCodes 股票代码，多个用逗号隔开  例： C:AEDUSD,C:ALLUSD,C:CNYUSD
     * @return
     */
    public static Map<String, TickerInfo> getForexDetailQuote(String stockCodes){
        Map<String, TickerInfo> map = new HashMap<>();
        if (StringUtils.isEmpty(stockCodes)){
            return map;
        }
        String[] split = stockCodes.split(",");
        //股票代码集合
        List<String> stockCodeList = Arrays.stream(split).distinct().collect(Collectors.toList());
        //获取第三方行情数据
        Map<String, TickerInfo> tickerInfoMap = PolygonUtils.getInfosByTickers(stockCodeList.stream().collect(Collectors.joining(",")),4);

        for (int i = 0; i < stockCodeList.size(); i++) {
            String stockCode = stockCodeList.get(i);
            //如果没有获取到实时行情
            if (!tickerInfoMap.containsKey(stockCode)){
                //取昨日数据
                TickerInfo tickerInfo = new TickerInfo();
                TickerInfoDay tickerInfoDay = new TickerInfoDay();
                TickerInfoPrevDay tickerInfoPrevDay = new TickerInfoPrevDay();
                TickerInfoMin tickerInfoMin = new TickerInfoMin();
                //取昨日数据
                ForexEverydayRecord forexEverydayRecord = forexEverydayRecordMapper.selectLastRecordByProductCode(stockCode);
                if (forexEverydayRecord != null){
                    String nowPrice = forexEverydayRecord.getClosePrice().toString();
                    tickerInfo.setNowPrice(nowPrice);
                    tickerInfo.setChangeRate(BigDecimal.ZERO.toString());
                    tickerInfoDay.setMaxPriceDay(nowPrice);
                    tickerInfoDay.setMinPriceDay(nowPrice);
                    tickerInfoDay.setOpenPriceDay(nowPrice);
                    tickerInfoDay.setVolumeDay(BigDecimal.ZERO.toString());
                    tickerInfoDay.setTradeAmount(BigDecimal.ZERO.toString());
                    tickerInfoPrevDay.setClosePricePrevDay(nowPrice);
                    tickerInfoMin.setVolumeMin(BigDecimal.ZERO.toString());
                    tickerInfoMin.setAveragePriceMin(nowPrice);
                }else {
                    tickerInfo.setNowPrice(BigDecimal.ZERO.toString());
                    tickerInfo.setChangeRate(BigDecimal.ZERO.toString());
                    tickerInfoDay.setMaxPriceDay(BigDecimal.ZERO.toString());
                    tickerInfoDay.setMinPriceDay(BigDecimal.ZERO.toString());
                    tickerInfoDay.setOpenPriceDay(BigDecimal.ZERO.toString());
                    tickerInfoDay.setVolumeDay(BigDecimal.ZERO.toString());
                    tickerInfoDay.setTradeAmount(BigDecimal.ZERO.toString());
                    tickerInfoPrevDay.setClosePricePrevDay(BigDecimal.ZERO.toString());
                    tickerInfoMin.setVolumeMin(BigDecimal.ZERO.toString());
                    tickerInfoMin.setAveragePriceMin(BigDecimal.ZERO.toString());
                }
                tickerInfo.setTickerInfoDay(tickerInfoDay);
                tickerInfo.setTickerInfoPrevDay(tickerInfoPrevDay);
                tickerInfo.setTickerInfoMin(tickerInfoMin);
                tickerInfoMap.put(stockCode,tickerInfo);
            }
        }
        map.putAll(tickerInfoMap);
        fillControlQuote(map,4);
        return map;
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
