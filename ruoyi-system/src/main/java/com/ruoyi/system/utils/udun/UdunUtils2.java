package com.ruoyi.system.utils.udun;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.exception.ServiceException;

import java.nio.charset.StandardCharsets;

/**
 * 优盾工具类
 */
public class UdunUtils2 {


    public static void main(String[] args) {
        //请求url
        String requestUrl = "https://sig11.udun.io" + "/mch/support-coins";
        //post请求
        HttpRequest post = HttpUtil.createPost(requestUrl);
        //设置3秒超时
        post.timeout(3000);
        //设置请求头
        post.header("Content-Type","application/json");
        //body
        JSONObject jsonObject = new JSONObject();
        //商户id
        jsonObject.put("merchantId","314540");
        //是否查询余额，false不获取，ture获取
        jsonObject.put("showBalance", "false");
        //params
        String params = parseParams("e29bcffa7252c3d3e7eaa5435df26f86", jsonObject.toJSONString());
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
        System.out.println(response.getJSONArray("data"));
    }

//    public static void main(String[] args) {
//        //请求url
//        String requestUrl = "https://sig11.udun.io" + "//mch/address/create";
//        //post请求
//        HttpRequest post = HttpUtil.createPost(requestUrl);
//        //设置3秒超时
//        post.timeout(3000);
//        //设置请求头
//        post.header("Content-Type","application/json");
//        //body
//        JSONObject jsonObject = new JSONObject();
//        //商户id
//        jsonObject.put("merchantId","314540");
//        //主币种编号
//        jsonObject.put("coinType", "0");
//        //回调地址
//        jsonObject.put("callUrl", "");
//        //params
//        String params = parseParams("e29bcffa7252c3d3e7eaa5435df26f86", "[" + jsonObject.toJSONString() + "]");
//        //请求参数body
//        post.body(params,"application/json");
//        //编码utf-8
//        post.charset(StandardCharsets.UTF_8);
//        //执行请求
//        String responseString = post.execute().body();
//        //响应
//        JSONObject response = JSONObject.parseObject(responseString);
//        //状态
//        String code = response.getString("code");
//        //如果调用成功
//        if (!"200".equals(code.toString())){
//            throw new ServiceException(response.getString("message"));
//        }
//        System.out.println(response.getJSONObject("data").getString("address"));;
//    }

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
