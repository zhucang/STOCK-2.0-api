package com.ruoyi.system.utils.udun;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.cache.CacheUtil;

import java.nio.charset.StandardCharsets;

/**
 * 优盾工具类
 */
public class UdunUtils{

    /**
     * api节点
     */
    private static String gateway;

    /**
     * 商户id
     */
    public static String merchantId;

    /**
     * 商户key
     */
    public static String merchantKey;

    /**
     * 回调地址
     */
    private static String callUrl;

    /**
     * 初始化信息
     */
    public static void init() {
        //优盾key参数
        String otherValueByKey = CacheUtil.getOtherValueByKey("udun_key",String.class);
        if (StringUtils.isNotEmpty(otherValueByKey)){
            String[] arr = otherValueByKey.split(",");
            if (arr.length != 4){
                throw new ServiceException("请检查udun秘钥是否正确设置");
            }
            gateway = arr[0];
            merchantId = arr[1];
            merchantKey = arr[2];
            callUrl = arr[3];
        }else {
            throw new ServiceException("请检查udun秘钥是否正确设置");
        }
    }

    /**
     * 获取商户支持币种
     * @return
     */
    public static JSONArray getMerchantSupportCoins(){
        //初始化
        init();
        //请求url
        String requestUrl = gateway + "/mch/support-coins";
        //post请求
        HttpRequest post = HttpUtil.createPost(requestUrl);
        //设置3秒超时
        post.timeout(3000);
        //设置请求头
        post.header("Content-Type","application/json");
        //body
        JSONObject jsonObject = new JSONObject();
        //商户id
        jsonObject.put("merchantId",merchantId);
        //是否查询余额，false不获取，ture获取
        jsonObject.put("showBalance", "false");
        //params
        String params = parseParams(merchantKey, jsonObject.toJSONString());
        //请求参数body
        post.body(params,"application/json");
        //编码utf-8
        post.charset(StandardCharsets.UTF_8);
        //执行请求
        String responseString = post.execute().body();
        //响应
        JSONObject response = JSONObject.parseObject(responseString);
        //状态
        String code = response.getString("code");
        //如果调用成功
        if (!"200".equals(code.toString())){
            throw new ServiceException(response.getString("message"));
        }
        return response.getJSONArray("data");
    }

    /**
     * 创建钱包地址
     * @param mainCoinType 主币种编号
     * @param callUrl 回调地址
     * @return
     */
    public static String createAddress(String mainCoinType,String callUrl){
        if (StringUtils.isEmpty(mainCoinType)){
            throw new ServiceException("主币种编号不能为空");
        }
        if (StringUtils.isEmpty(callUrl)){
            throw new ServiceException("回调地址不能为空");
        }
        //初始化
        init();
        //请求url
        String requestUrl = gateway + "/mch/address/create";
        //post请求
        HttpRequest post = HttpUtil.createPost(requestUrl);
        //设置3秒超时
        post.timeout(3000);
        //设置请求头
        post.header("Content-Type","application/json");
        //body
        JSONObject jsonObject = new JSONObject();
        //商户id
        jsonObject.put("merchantId",merchantId);
        //主币种编号
        jsonObject.put("coinType", mainCoinType);
        //回调地址
        jsonObject.put("callUrl", callUrl);
        //params
        String params = parseParams(merchantKey, "[" + jsonObject.toJSONString() + "]");
        //请求参数body
        post.body(params,"application/json");
        //编码utf-8
        post.charset(StandardCharsets.UTF_8);
        //执行请求
        String responseString = post.execute().body();
        //响应
        JSONObject response = JSONObject.parseObject(responseString);
        //状态
        String code = response.getString("code");
        //如果调用成功
        if (!"200".equals(code.toString())){
            throw new ServiceException(response.getString("message"));
        }
        return response.getJSONObject("data").getString("address");
    }

    /**
     * 解析参数
     * @param merchantKey 商户秘钥
     * @param body body参数
     * @return
     */
    public static String parseParams(String merchantKey, String body) {
        //时间戳
        String timestamp = System.currentTimeMillis() + "";
        //随机数
        String nonce = RandomUtil.randomString(6);
        //签名
        String sign = SecureUtil.md5(body + merchantKey + nonce + timestamp).toLowerCase();
        //params
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("timestamp", timestamp);
        jsonObject.put("nonce", nonce);
        jsonObject.put("sign", sign);
        jsonObject.put("body", body);
        return jsonObject.toJSONString();
    }


}
