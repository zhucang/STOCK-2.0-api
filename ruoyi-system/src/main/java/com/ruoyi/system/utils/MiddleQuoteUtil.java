package com.ruoyi.system.utils;


import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.core.domain.ticker.TickerInfo;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.cache.CacheUtil;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.websocket.WebsocketClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 中间服务器获取产品行情数据工具类 xx
 */
public class MiddleQuoteUtil {

    private static final Logger log = LoggerFactory.getLogger(MiddleQuoteUtil.class);

    /**
     * redisCache
     */
    private static RedisCache redisCache = SpringUtils.getBean(RedisCache.class);

    /**
     * 中间服务器ip端口
     */
    private static String host = CacheUtil.getOtherValueByKey("middleQuote_hostAddress", String.class);

    /**
     * 根据产品代码从缓存获取产品行情
     * @param productCodes 产品代码
     * @return
     */
    public static Map<String, TickerInfo> getTickerInfosByCache(String productCodes){
        if (StringUtils.isEmpty(productCodes)){
            return new HashMap<>();
        }
        //获取本地redis缓存
        List<Object> productCodeList = Arrays.asList(productCodes.split(",")).stream().distinct().collect(Collectors.toList());
        //行情map
        Map<String, TickerInfo> resultMap = redisCache.getCacheMap("productQuote", productCodeList);
        return resultMap;
    }

    /**
     * 添加新产品到中间服务器
     * @param productCodeList 产品代码集合
     * @param productType 产品类型 1：美股 2：加密货币 4:外汇
     */
    public static void insertNewProductToMiddleServer(List<String> productCodeList,Integer productType){
        //同步产品至中间服务器
        String requestUrl = "http://" + getHost() + "/middle/quoteProduct/addQuoteProducts";
        //创建请求
        HttpRequest post = HttpUtil.createPost(requestUrl);
        //超时时间3秒
        post.timeout(3000);
        //传参
        List<JSONObject> jsonObjects = new ArrayList<>();
        for (int i = 0; i < productCodeList.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            //产品代码
            String productCode = productCodeList.get(i);
            jsonObject.put("productName",productCode);
            jsonObject.put("productCode",productCode);
            jsonObject.put("productType",productType);
            jsonObjects.add(jsonObject);
        }
        post.body(JSONObject.toJSONString(jsonObjects),"application/json");
        //字符编码
        post.charset(StandardCharsets.UTF_8);
        //发送请求并获取响应
        String responseString = post.execute().body();
        //解析响应
        JSONObject jsonObject = JSONObject.parseObject(responseString);
        if (!jsonObject.get("code").equals(200)){
            throw new RuntimeException(jsonObject.get("msg").toString());
        }else {
            //重新加载行情产品
            WebsocketClient.reloadQuoteProduct();
        }
    }

    /**
     * 期货K线
     * @param productCode
     * @param kLineType
     * @param dataRows
     * @return
     */
    public static JSONArray getFutureKlineChart(String productCode, String kLineType, Long dataRows){
        String s = dataRows != null ? dataRows.toString() : "";
        //请求url
        String requestUrl = "http://" + getHost() + "/middle/quoteProduct/getFutureKlineChart?productCode=" + productCode + "&kLineType=" + kLineType + "&dataRows=" + s;
        try {
            HttpRequest httpRequest = HttpRequest.get(requestUrl);
            httpRequest.setReadTimeout(5000);
            httpRequest.header("apiKey", "test999000");
            String response = httpRequest.execute().body();
            JSONObject jsonObject = JSONObject.parseObject(response);
            if ("200".equals(jsonObject.getString("code"))){
                return jsonObject.getJSONArray("data");
            }else {
                throw new Exception(jsonObject.getString("msg"));
            }
        }catch (Exception e){
            throw new RuntimeException("行情服获取期货K线数据异常，原因：" + e.getMessage());
        }
    }

    /**
     * 获取股票、加密货币、外汇K线数据
     * @param productType 产品类型
     * @param productCode 产品代码
     * @param time 时间跨度
     * @param timespan 时间类型 (minute，day，hour，week。。。。)
     * @param size 获取数量
     * @param timeFrom 开始时间
     * @param timeTo 结束实际
     * @param limit 从第三方api库获取limit条数据后筛选我们需要的数据
     * @return
     */
    public static JSONArray getProductKlineChart(Integer productType, String productCode, Integer time, String timespan, Integer size, String timeFrom, String timeTo, Long limit) {
        //请求url
        String requestUrl = "http://" + getHost() + "/middle/quoteProduct/getProductKlineChart?productType="+productType+"&productCode=" + productCode + "&time=" + time + "&timespan=" + timespan + "&size=" + size + "&timeFrom=" + timeFrom + "&timeTo=" + timeTo + "&limit=" + limit;
        try {
            HttpRequest httpRequest = HttpRequest.get(requestUrl);
            httpRequest.setReadTimeout(5000);
            httpRequest.header("apiKey", "test999000");
            String response = httpRequest.execute().body();
            JSONObject jsonObject = JSONObject.parseObject(response);
            if ("200".equals(jsonObject.getString("code"))){
                return jsonObject.getJSONArray("data");
            }else {
                throw new Exception(jsonObject.getString("msg"));
            }
        }catch (Exception e){
            throw new RuntimeException("行情服获取" + productCode + "K线数据异常，原因：" + e.getMessage());
        }
    }


    public static String getHost() {
        if (StringUtils.isEmpty(host)){
            return SpringUtils.getRequiredProperty("middle.ip")+":"+SpringUtils.getRequiredProperty("middle.port");
        }
        return host;
    }

    public static void setHost(String host) {
        MiddleQuoteUtil.host = host;
    }
}
