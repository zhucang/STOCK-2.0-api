package com.ruoyi.system.utils.olgpay;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.utils.CodeUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.utils.cache.CacheUtils;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.logging.log4j.util.Base64Util;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Olgpay三方支付工具类
 */
@Component
public class OlgpayUtils {

    /**
     * pk秘钥
     */
    public static String pkKey;

    /**
     * sk秘钥
     */
    public static String skKey;


    /**
     * 获取支持的法币
     */
    public static List<String> getSupportFiatsCurrency(){
        pkKey = CacheUtils.getOtherValueByKey("olgpay_pk_key",String.class);
        List<String> fiatsCurrency = new ArrayList<>();
        try {
            String response = HttpUtils.sendGet("https://api.olgpay.com/v3/Orders/RateFiats");
            JSONObject jsonObject = JSONObject.parseObject(response);
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray fiats = data.getJSONArray("fiats");
            for (int i = 0; i < fiats.size(); i++) {
                JSONObject jsonObjectVo = fiats.getJSONObject(i);
                String fiatCode = jsonObjectVo.getString("fiat_code");
                fiatsCurrency.add(fiatCode);
            }
        }catch (Exception e){

        }
        return fiatsCurrency;
    }

//    /**
//     * 付款
//     * @param orderNo 订单号
//     * @param amount 订单金额
//     * @param fiat 收款帐户法币单位
//     * @param userId 用户id
//     */
//    public static Object payOrder(String orderNo, BigDecimal amount,String fiat,Long userId){
//        //收款账户名称
//        String beneficiaryName = "raoig";
//        //收款帐户
//        String accountNumber = "123513553563865324";
//        //收款人银行SWIFT代码
//        String swiftCode = "41";
//        //notifyUrl
//        String notifyUrl = "null";
//
//        //请求url
//        String requestUrl = "https://api.olgpay.com/v3/Orders/payOder";
//        //post请求
//        HttpRequest post = HttpUtil.createPost(requestUrl);
//        //设置3秒超时
//        post.timeout(3000);
//        //设置请求头
//        post.header("Content-Type","application/json");
//        post.header("Authorization","pk_live_m2FuRexJQDqhUTte");
//        //body
//        JSONObject jsonObject = new JSONObject();
//        //订单号
//        jsonObject.put("orderNo",orderNo);
//        //订单金额
//        jsonObject.put("amount",amount);
//        //收款帐户法币单位(默认USD)
//        jsonObject.put("fiat",fiat);
//        //收款帐户名称
//        jsonObject.put("beneficiaryName",beneficiaryName);
//        //收款帐户
//        jsonObject.put("accountNumber",accountNumber);
//        //收款人银行SWIFT代码
//        jsonObject.put("swiftCode",swiftCode);
//        //用户id
//        jsonObject.put("userId",userId);
//        //接收付款结果通知的回调地址
//        jsonObject.put("notifyUrl",notifyUrl);
//        //收款人地址
//        jsonObject.put("beneficiaryAddress","");
//        //签名方式：
//        String sign = "pk_live_m2FuRexJQDqhUTte" + amount + orderNo + userId + accountNumber;
//        //Base64编码
//        sign = Base64Util.encode(sign);
//        jsonObject.put("sign",sign);
//        post.body(jsonObject.toJSONString(),"application/json");
//        post.charset(StandardCharsets.UTF_8);
//        String responseString = post.execute().body();
//        //响应
//        JSONObject response = JSONObject.parseObject(responseString);
//        //请求状态码:200(成功)、100（失败）
//        String status = response.getString("status");
//        //请求操作完成时间戳
//        String expiresAt = response.getString("expiresAt");
//        //请求执行描述
//        String Msg = response.getString("Msg");
//        if (!status.equals("200")){
//            throw new RuntimeException("发起支付失败");
//        }
//        jsonObject.putAll(response);
//        return jsonObject;
//    }


    /**
     * 收款
     * @param orderNo 订单号
     * @param amount 订单金额
     * @param userId 用户id
     * @param payType 支付类型(信用卡A: CA, 信用卡B: CB, 加密货币：CW)
     * @param fiat 法币单位,默认USD
     * @return
     */
    public static JSONObject receiveOder(String orderNo, BigDecimal amount,Long userId,String payType,String fiat){
        pkKey = CacheUtils.getOtherValueByKey("olgpay_pk_key",String.class);
        skKey = CacheUtils.getOtherValueByKey("olgpay_sk_key",String.class);
        //接收支付结果通知的回调地址
        String notifyUrl = CacheUtils.getOtherValueByKey("onlineRechargeCallBack",String.class) + "/api/userRecharge/receiveUserOnlineRechargeOrderState";
        //请求url
        String requestUrl = "https://api.olgpay.com/v3/Orders/ReceiveOder";
        //post请求
        HttpRequest post = HttpUtil.createPost(requestUrl);
        //设置3秒超时
        post.timeout(3000);
        //设置请求头
        post.header("Content-Type","application/json");
        post.header("Authorization",pkKey);
        //body
        JSONObject jsonObject = new JSONObject();
        //订单号
        jsonObject.put("orderNo",orderNo);
        //订单金额
        jsonObject.put("amount",amount);
        //用户id
        jsonObject.put("userId",userId);
        //支付类型(信用卡A: CA, 信用卡B: CB, 加密货币：CW)
        jsonObject.put("payType",payType);
        //接收支付结果通知的回调地址
        jsonObject.put("notifyUrl",notifyUrl);
        //法币单位,默认USD
        jsonObject.put("fiat",fiat);
        //签名方式：
        String sign = pkKey + amount + orderNo + userId;
        //SHA-256加密
        sign = HmacUtils.hmacSha256Hex(skKey,sign);
        //Base64编码
        sign = Base64Util.encode(sign);
        jsonObject.put("sign",sign);
        post.body(jsonObject.toJSONString(),"application/json");
        post.charset(StandardCharsets.UTF_8);
        String responseString = post.execute().body();
        //响应
        JSONObject response = JSONObject.parseObject(responseString);
        //状态
        String status = response.getString("status");
        if (!"200".equals(status)){
            throw new RuntimeException(responseString);
        }
        jsonObject.putAll(response);
        return jsonObject;
    }


    public static void main(String[] args) {
        Object usd = receiveOder(CodeUtils.generateOrderCode(""),new BigDecimal(100),10086L,"CW","USDT");
        System.out.println(usd);
    }
}
