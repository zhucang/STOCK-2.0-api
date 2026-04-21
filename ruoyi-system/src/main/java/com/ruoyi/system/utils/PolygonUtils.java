package com.ruoyi.system.utils;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.domain.StockProduct;
import com.ruoyi.common.core.domain.ticker.TickerInfo;
import com.ruoyi.common.core.domain.ticker.TickerInfoDay;
import com.ruoyi.common.core.domain.ticker.TickerInfoMin;
import com.ruoyi.common.core.domain.ticker.TickerInfoPrevDay;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

/**
 * 产品行情utils xx
 * Polygon
 */
@Component
public class PolygonUtils {

    //股票代码获取股票信息url
    private static String stockTickerInfoUrl;

    //加密货币代码获取股票信息url
    private static String cryptocurrencyTickerInfoUrl;

    //外汇代码获取股票信息url
    private static String forexTickerInfoUrl;

    //apiKey
    private static String apiKay;

    //获取市场所有产品url
    private static String productMarketUrl;

    /**
     * 根据股票代码获取股票即时信息(同时多个股票代码，使用逗号隔开)
     * @param tickers
     * @param type 1:股票  2:加密货币 4:外汇
     * @return
     */
    public static Map<String, TickerInfo> getInfosByTickers(String tickers,int type){
        //resultMap
        Map<String, TickerInfo>  resultMap = new HashMap<>();
        if (StringUtils.isEmpty(tickers)){
            return resultMap;
        }
        //优先取中间服务器数据
        resultMap = MiddleQuoteUtil.getTickerInfosByCache(tickers);
        String[] split = tickers.split(",");
        if (resultMap.size() == split.length){
            return resultMap;
        }
        //未获取到行情信息的产品
        String noValueTickers = "";
        for (int i = 0; i < split.length; i++) {
            String stockCode = split[i];
            if (!resultMap.containsKey(stockCode)){
                noValueTickers = noValueTickers + stockCode + ",";
            }
        }
        tickers = noValueTickers;

        //默认查股票行情
        String prefix = stockTickerInfoUrl;
        //如果type=2查加密货币行情
        if (type == 2){
            prefix = cryptocurrencyTickerInfoUrl;
        }else if (type == 4){
            prefix = forexTickerInfoUrl;
        }
        String requestUrl = prefix + tickers + "&apiKey=" + apiKay;
        String responseString = "";
        try {
            responseString = HttpUtils.sendGet(requestUrl);
        }catch (Exception e){
            System.out.printf("获取行情数据出错，url:"+requestUrl+",出错原因:"+e);
            return resultMap;
        }
        if (StringUtils.isBlank(responseString)){
            return resultMap;
        }else {
            JSONObject responseObject = null;
            try {
                //如果未解析到数据，则返回null
                responseObject = JSONObject.parseObject(responseString);
            }catch (Exception e){
                return resultMap;
            }
            JSONArray tickersArray = responseObject.getJSONArray("tickers");
            for (int i = 0; i < tickersArray.size(); i++) {
                JSONObject tickerObject = tickersArray.getJSONObject(i);
                //股票代码
                String ticker = tickerObject.get("ticker").toString();
                //今日涨跌幅
                String todaysChangePerc = tickerObject.get("todaysChangePerc").toString();
                //今日金额变化
                String todaysChange = tickerObject.get("todaysChange").toString().toString();

                //今日信息
                JSONObject day = tickerObject.getJSONObject("day");
                //今日最高价格
                String dayH = day.get("h").toString();
                //今日最低价格
                String dayL = day.get("l").toString();
                //今日开盘价
                String dayO = day.get("o").toString();
                //今日收盘价
                String dayC = day.get("c").toString();
                //今日交易量
                String dayV = day.get("v").toString();
                //今日成交量加权平均价
                String dayVw = day.get("vw").toString();
                //股票今日信息对象
                TickerInfoDay tickerInfoDay = new TickerInfoDay();
                tickerInfoDay.setMaxPriceDay(dayH);
                tickerInfoDay.setMinPriceDay(dayL);
                tickerInfoDay.setOpenPriceDay(dayO);
                tickerInfoDay.setClosePriceDay(dayC);
                tickerInfoDay.setVolumeDay(dayV);
                tickerInfoDay.setAveragePriceDay(dayVw);
                tickerInfoDay.setTradeAmount(new BigDecimal(dayV).multiply(new BigDecimal(dayVw)).toString());


                //最近一分钟信息
                JSONObject min = tickerObject.getJSONObject("min");
                //最近一分钟交易量
                String minH = min.get("h").toString();
                //最近一分钟最低价格
                String minL = min.get("l").toString();
                //最近一分钟开盘价
                String minO = min.get("o").toString();
                //最近一分钟收盘价
                String minC = min.get("c").toString();
                //最近一分钟的交易量
                String minV = min.get("v").toString();
                //最近一分钟的成交量加权平均价
                String minVw = min.get("vw").toString();
                //股票最近一分钟信息对象
                TickerInfoMin tickerInfoMin = new TickerInfoMin();
                tickerInfoMin.setMaxPriceMin(minH);
                tickerInfoMin.setMinPriceMin(minL);
                tickerInfoMin.setOpenPriceMin(minO);
                tickerInfoMin.setClosePriceMin(minC);
                tickerInfoMin.setVolumeMin(minV);
                tickerInfoMin.setAveragePriceMin(minVw);

                //昨日信息
                JSONObject prevDay = tickerObject.getJSONObject("prevDay");
                //前一天最高价格
                String prevDayH = prevDay.get("h").toString();
                //前一天最低价格
                String prevDayL = prevDay.get("l").toString();
                //前一天开盘价
                String prevDayO = prevDay.get("o").toString();
                //前一天收盘价
                String prevDayC = prevDay.get("c").toString();
                //前一天交易量
                String prevDayV = prevDay.get("v").toString();
                //前一天成交量加权平均价
                String prevDayVw = prevDay.get("vw").toString();
                //股票最近一分钟信息对象
                TickerInfoPrevDay tickerInfoPrevDay = new TickerInfoPrevDay();
                tickerInfoPrevDay.setMaxPricePrevDay(prevDayH);
                tickerInfoPrevDay.setMinPricePrevDay(prevDayL);
                tickerInfoPrevDay.setOpenPricePrevDay(prevDayO);
                tickerInfoPrevDay.setClosePricePrevDay(prevDayC);
                tickerInfoPrevDay.setVolumePrevDay(prevDayV);
                tickerInfoPrevDay.setAveragePricePrevDay(prevDayVw);

                //现价 = 前一天收盘价 + 今日金额变化
                String nowPrice = new BigDecimal(prevDayC).add(new BigDecimal(todaysChange)).toString();

                //股票快照信息
                TickerInfo tickerInfo = new TickerInfo();
                tickerInfo.setTicker(ticker);
                tickerInfo.setChangeRate(todaysChangePerc);
                tickerInfo.setChangePrice(todaysChange);
                tickerInfo.setNowPrice(nowPrice);
                tickerInfo.setTickerInfoDay(tickerInfoDay);
                tickerInfo.setTickerInfoMin(tickerInfoMin);
                tickerInfo.setTickerInfoPrevDay(tickerInfoPrevDay);
                resultMap.put(ticker,tickerInfo);
            }
        }
        return resultMap;
    }

    /**
     * 市场所有产品信息获取
     * @param productCode 产品代码
     * @param productType 产品类型 1：美股 2：加密货币 4:外汇
     * @return
     */
    public static Map<String,Object> tickersMarket(String productCode,Integer productType,String nextPage){
        //数量
        Long limit = 100L;
        String market = "stocks";
        if (productType.equals(2)){
            market = "crypto";
        }else if(productType.equals(4)){
            market = "fx";
        }
        String requestUrl = "";
        if (StringUtils.isNotEmpty(nextPage)){
            requestUrl = nextPage + "&apiKey="+apiKay;
        }else {
            requestUrl = productMarketUrl+"market="+market+"&active=true"+"&limit="+limit+"&apiKey="+apiKay;
            if (StringUtils.isNotEmpty(productCode)){
                requestUrl = requestUrl + "&ticker=" + productCode;
            }
        }
        Map<String, Object> map = new HashMap<>();
        List<StockProduct> resultList = new ArrayList<>();
        String nextPageUrl = null;
        map.put("results",resultList);
        map.put("nextPageUrl",nextPageUrl);
        try {
            String response = HttpUtils.sendGet(requestUrl);
            if (StringUtils.isNotEmpty(response)){
                //响应object
                JSONObject responseObject = JSONObject.parseObject(response);
                //产品数据列表
                JSONArray resultsArray = responseObject.getJSONArray("results");
                for (int i = 0; i < resultsArray.size(); i++) {
                    //json对象
                    JSONObject jsonObject = resultsArray.getJSONObject(i);
                    //交易币种名称
                    Object currencyName = productType.equals(1) ? jsonObject.get("currency_name") : jsonObject.get("currency_symbol");
                    if (currencyName == null || (!currencyName.equals("usd") && !currencyName.equals("USD"))){
                        continue;
                    }
                    //产品代码
                    Object ticker = jsonObject.get("ticker");
                    if (ticker == null){
                        continue;
                    }
                    //产品名称
                    Object name = jsonObject.get("name");
                    if (ticker == null){
                        continue;
                    }
                    //产品信息
                    StockProduct product = new StockProduct();
                    product.setCurrencyName(currencyName.toString());
                    product.setProductCode(ticker.toString());
                    product.setProductName(name.toString());
                    resultList.add(product);
                }
                //下一页链接
                Object nextUrl = responseObject.get("next_url");
                if (nextUrl != null){
                    nextPageUrl = String.valueOf(nextUrl);
                    map.put("nextPageUrl",nextPageUrl);
                }
            }
        } catch (Exception e) {
            return map;
        }
        return map;
    }

    /**
     * 获取股票昨日开盘价、最高价、最低价和收盘价信息
     * @param ticker
     * @return
     */
    public static Map<String,String> getPrevTickerInfo(String ticker){
        //股票代码
        String requestUrl = "https://api.polygon.io/v2/aggs/ticker/"+ticker+"/prev?adjusted=true&apiKey=11qCtTt2JKHGoggYACyeQS_431uNMQEy";
        String s = HttpUtils.sendGet(requestUrl);
        if (s.equals("")){
            return null;
        }else {
            JSONObject responseJsonObject = JSONObject.parseObject(s);
            JSONArray results = responseJsonObject.getJSONArray("results");
            JSONObject jsonObject = results.getJSONObject(0);
            //昨日开盘价
            String o = jsonObject.get("o").toString();
            //昨日收盘价
            String c = jsonObject.get("c").toString();
            //昨日最高价
            String h = jsonObject.get("h").toString();
            //昨日最低价
            String l = jsonObject.get("l").toString();
            //昨日交易量
            String v = jsonObject.get("v").toString();
            HashMap<String, String> map = new HashMap<>();
            map.put("o",o);
            map.put("c",c);
            map.put("h",h);
            map.put("l",l);
            map.put("v",v);
            return map;
        }
    }

    //获取K线所需数据
    //time 时间跨度
    //timespan 时间类型 (minute，day，hour，week。。。。)
    //from 时间开始
    //to 时间截止
    //limit 从第三方api库获取limit条数据后筛选我们需要的数据（limit值越大越好）
    public static List<Map<String,String>> getKLineData(String ticker,int time,String timespan,int size,String timeFrom,String timeTo,Long limit){
        List<Map<String,String>> resultArray = new ArrayList<>();
        String requestUrl = "https://api.polygon.io/v2/aggs/ticker/"+ticker+"/range/"+time+"/"+timespan+"/"+timeFrom+"/"+timeTo+"?adjusted=true&sort=desc&limit="+limit+"&apiKey="+apiKay;
        String responseString = "";
        try {
            responseString = HttpUtils.sendGet(requestUrl);
        }catch (Exception e){
            System.out.printf("获取股票行情数据出错，url:"+requestUrl+",出错原因:"+e);
        }
        if (StringUtils.isBlank(responseString)){
            return null;
        }else {
            JSONObject responseJsonObject = null;
            try {
                //如果未解析到数据，则返回null
                responseJsonObject = JSONObject.parseObject(responseString);
            }catch (Exception e){
                return null;
            }
            JSONArray results = responseJsonObject.getJSONArray("results");
            if (results != null){
                List<Object> list = results.toJavaList(Object.class);
                if (results.size() >= size){
                    //只取两百条
                    list = results.subList(0, size);
                }
                for (int i = 0; i < list.size(); i++) {
                    JSONObject jsonObject = (JSONObject)list.get(i);
                    //昨日开盘价
                    String o = jsonObject.get("o").toString();
                    //昨日收盘价
                    String c = jsonObject.get("c").toString();
                    //昨日最高价
                    String h = jsonObject.get("h").toString();
                    //昨日最低价
                    String l = jsonObject.get("l").toString();
                    //昨日交易量
                    String v = jsonObject.get("v").toString();
                    //成交量加权平均价
                    String vw = jsonObject.get("vw")!=null?jsonObject.get("vw").toString():"0";
                    //时间戳
                    String t = jsonObject.get("t").toString();
                    HashMap<String, String> map = new HashMap<>();
                    map.put("o",o);
                    map.put("c",c);
                    map.put("h",h);
                    map.put("l",l);
                    map.put("v",v);
                    map.put("vw",vw);
                    map.put("t",t);
                    resultArray.add(map);
                }
            }
        }
        Collections.reverse(resultArray);
        return resultArray;
    }




    @Value("${polygon.stock.url}")
    public void setStockTickerInfoUrl(String stockTickerInfoUrl) {
        this.stockTickerInfoUrl = stockTickerInfoUrl;
    }

    @Value("${polygon.cryptocurrency.url}")
    public void setCryptocurrencyTickerInfoUrl(String cryptocurrencyTickerInfoUrl) {
        this.cryptocurrencyTickerInfoUrl = cryptocurrencyTickerInfoUrl;
    }

    @Value("${polygon.forex.url}")
    public void setForexTickerInfoUrl(String forexTickerInfoUrl) {
        this.forexTickerInfoUrl = forexTickerInfoUrl;
    }

    @Value("${polygon.market.apiKey}")
    public void setApiKay(String apiKay) {
        this.apiKay = apiKay;
    }

    @Value("${polygon.market.url}")
    public void setProductMarketUrl(String productMarketUrl) {
        this.productMarketUrl = productMarketUrl;
    }


}
