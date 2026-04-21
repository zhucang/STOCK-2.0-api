package com.ruoyi.system.utils;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.domain.FuturesProduct;
import com.ruoyi.common.core.domain.ticker.TickerInfo;
import com.ruoyi.common.core.domain.ticker.TickerInfoDay;
import com.ruoyi.common.core.domain.ticker.TickerInfoPrevDay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

/**
 * 期货行情获取工具类 xx
 */
@Component
public class FuturesMarketUtil {

    private static final Logger log = LoggerFactory.getLogger(FuturesMarketUtil.class);

    //期货行情链接
    private static String url;

    //apiKey
    private static String apiKay;

    /**
     * 获取市场所有期货信息
     * @return
     */
    public static List<FuturesProduct> futuresMarket(){
        List<FuturesProduct> resultList = new ArrayList<>();
        String requestUrl = url+"/api/v3/symbol/available-commodities"+"?apikey="+apiKay;
        try {
            //response
            String response = HttpUtils.sendGet(requestUrl);
            JSONArray futuresArray = JSONArray.parseArray(response);
            for (int i = 0; i < futuresArray.size(); i++) {
                //json对象
                JSONObject jsonObject = futuresArray.getJSONObject(i);
                //交易币种
                String tradeCurrency = String.valueOf(jsonObject.get("currency"));
                if (!tradeCurrency.equals("USD") || !tradeCurrency.equals("usd")){
                    continue;
                }
                //期货代码
                String futuresCode = String.valueOf(jsonObject.get("symbol"));
                //期货名称
                String futuresName = String.valueOf(jsonObject.get("name"));
                //期货信息
                FuturesProduct futuresProduct = new FuturesProduct();
                futuresProduct.setProductCode(futuresCode);
                futuresProduct.setProductName(futuresName);
                resultList.add(futuresProduct);
            }
        }catch (Exception e){
            log.info("获取市场所有期货信息异常");
        }
        return resultList;
    }

    /**
     * 获取期货实时报价
     * @param futuresCodes
     * @return
     */
    public static Map<String, TickerInfo> getFuturesQuote(String futuresCodes){
        Map<String, TickerInfo> resultMap = new HashMap<>();
        if (StringUtils.isEmpty(futuresCodes)){
            return resultMap;
        }

        //优先取中间服务器数据
        resultMap = MiddleQuoteUtil.getTickerInfosByCache(futuresCodes);
        String[] split = futuresCodes.split(",");
        if (resultMap.size() == split.length){
            return resultMap;
        }
        String noValueTickers = "";
        for (int i = 0; i < split.length; i++) {
            String stockCode = split[i];
            if (!resultMap.containsKey(stockCode)){
                noValueTickers = noValueTickers + stockCode + ",";
            }
        }
        futuresCodes = noValueTickers;

        String requestUrl = url+"/api/v3/quote/"+futuresCodes+"?apikey="+apiKay;
        String s = "";
        try {
            s = HttpUtils.sendGet(requestUrl);
        }catch (Exception e){
            log.info("市场所有期货信息获取异常");
        }
        if (StringUtils.isEmpty(s)){
            return resultMap;
        }else {
            JSONArray futuresArray = JSONArray.parseArray(s);
            for (int i = 0; i < futuresArray.size(); i++) {
                JSONObject jsonObject = futuresArray.getJSONObject(i);
                //期货代码
                String futuresCode = jsonObject.get("symbol").toString();
                //现价
                String nowPrice = jsonObject.get("price").toString();
                //涨跌幅
                String changeRate = jsonObject.get("changesPercentage").toString();
                //今日最低
                String minPriceDay = jsonObject.get("dayLow").toString();
                //今日最高
                String maxPriceDay = jsonObject.get("dayHigh").toString();
                //平均价
                String priceAvg200 = jsonObject.get("priceAvg200").toString();
                //交易量
                String volumeDay = jsonObject.get("volume") == null ? "0" : jsonObject.get("volume").toString();
                //开盘价
                String openPriceDay = jsonObject.get("open").toString();
                //昨日收盘价
                String closePricePrevDay = jsonObject.get("previousClose").toString();

                //今日数据
                TickerInfoDay tickerInfoDay = new TickerInfoDay();
                tickerInfoDay.setMaxPriceDay(maxPriceDay);
                tickerInfoDay.setMinPriceDay(minPriceDay);
                tickerInfoDay.setOpenPriceDay(openPriceDay);
                tickerInfoDay.setVolumeDay(volumeDay);
                tickerInfoDay.setAveragePriceDay(priceAvg200);
                tickerInfoDay.setTradeAmount(new BigDecimal(volumeDay).multiply(new BigDecimal(priceAvg200)).toString());
                //昨日数据
                TickerInfoPrevDay tickerInfoPrevDay = new TickerInfoPrevDay();
                tickerInfoPrevDay.setClosePricePrevDay(closePricePrevDay);
                //期货行情数据
                TickerInfo tickerInfo = new TickerInfo();
                tickerInfo.setTicker(futuresCode);
                tickerInfo.setNowPrice(nowPrice);
                tickerInfo.setChangeRate(changeRate);
                tickerInfo.setTickerInfoDay(tickerInfoDay);
                tickerInfo.setTickerInfoPrevDay(tickerInfoPrevDay);
                resultMap.put(futuresCode,tickerInfo);
            }
            return resultMap;
        }
    }

    //获取期货K线所需数据
    //time 时间跨度
    //timespan 时间类型 (minute，day，hour，week。。。。)
    public static List<Map<String,String>> getKLineData(String ticker, Integer time, String timespan){
        if (timespan.equals("minute")){
            timespan = "min";
        }
        List<Map<String,String>> resultList = new ArrayList<>();
        String requestUrl = url+"/api/v3/historical-chart/"+ time + timespan + "/"+ ticker +"?apikey="+apiKay;
        String s = "";
        try {
            s = HttpUtils.sendGet(requestUrl);
        }catch (Exception e){
            log.info("获取K线数据异常");
        }
        if (StringUtils.isEmpty(s)){
            return resultList;
        }else {
            JSONArray results = JSONArray.parseArray(s);
            for (int i = 0; i < results.size(); i++) {
                JSONObject jsonObject = results.getJSONObject(i);
                //开盘价
                String openPrice = jsonObject.get("open").toString();
                //收盘价
                String closePrice = jsonObject.get("close").toString();
                //最高价
                String maxPrice = jsonObject.get("high").toString();
                //最低价
                String minPrice = jsonObject.get("low").toString();
                //交易量
                String volume = jsonObject.get("volume").toString();
                //时间戳
                String date = jsonObject.get("date").toString();
                HashMap<String, String> map = new HashMap<>();
                map.put("openPrice",openPrice);
                map.put("closePrice",closePrice);
                map.put("maxPrice",maxPrice);
                map.put("minPrice",minPrice);
                map.put("volume",volume);
                map.put("date",date);
                resultList.add(map);
            }
        }
        //倒序
        Collections.reverse(resultList);
        return resultList;
    }


    @Value("${financialmodelingprep.market.url}")
    public void setUrl(String url) {
        this.url = url;
    }

    @Value("${financialmodelingprep.market.apiKey}")
    public void setApiKay(String apiKay) {
        this.apiKay = apiKay;
    }
}
