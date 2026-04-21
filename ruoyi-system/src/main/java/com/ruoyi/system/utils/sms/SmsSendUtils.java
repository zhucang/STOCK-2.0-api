package com.ruoyi.system.utils.sms;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.sign.Md5Utils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.service.ISiteInfoService;
import com.ruoyi.system.utils.cache.CacheUtils;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 短信发送工具
 *
 * @Author: 东莞呵呵
 */
@Component
public class SmsSendUtils {

    /**
     * appKey
     */
    private static String appKey = "";

    /**
     * appSecret
     */
    private static String appSecret = "";

    /**
     * appCode
     */
    private static String appCode = "";


    private static ISiteInfoService siteInfoService = SpringUtils.getBean(ISiteInfoService.class);


    /**
     * 发送短信
     * @param phoneNumber 手机号码
     */
    public static String sendMessage(String phoneNumber){
        //平台名称
        String siteName = siteInfoService.selectSiteInfoById(1L).getSiteName();
        //发送短信appKey
        String smsAppKey = CacheUtils.getOtherValueByKey("sms_appKey", String.class);
        try{
            String[] split = smsAppKey.split(",");
            appKey = split[1];
            appSecret = split[2];
            appCode = split[3];
        }catch (Exception e){
            throw new ServiceException("获取appKey异常");
        }
        if (StringUtils.isEmpty(appKey) || StringUtils.isEmpty(appSecret) || StringUtils.isEmpty(appCode)){
            throw new ServiceException("请设置appKey");
        }
        //随机六位数验证码
        Random random = new Random();
        String validateCode = "";
        int randString = 0;
        for (int i = 0; i < 6; i++) {
            randString = random.nextInt(10);
            validateCode = validateCode + randString;
        }
        //短信内容
        String msg = "Dear new " + siteName + " users you use your cell phone number to register your " + siteName + " account your verification code is " + validateCode + ", wish you happy usi";
        //短信发送请求url
        String url="http://47.242.85.7:9090/sms/batch/v2?appkey="+appKey+"&appsecret="+appSecret+"&appcode="+appCode+"&phone="+phoneNumber+"&msg="+msg;
        try {
            //响应
            String response = HttpUtil.get(url);
            JSONObject jsonObject = JSONObject.parseObject(response);
            //状态码
            String code = jsonObject.getString("code");
            if (!"00000".equals(code)){
                throw new Exception(jsonObject.getString("desc"));
            }
            //uid
            String uid = jsonObject.getString("uid");
            //各条短信发送情况
            JSONArray result = jsonObject.getJSONArray("result");
            //短信发送失败信息
            List<String> err = new ArrayList<>();
            for (int i = 0; i < result.size(); i++) {
                JSONObject jsonObjectVo = result.getJSONObject(i);
                //状态
                String status = jsonObjectVo.getString("status");
                if (!"00000".equals(status)){
                    //手机号
                    String phone = jsonObjectVo.getString("phone");
                    //发送详情
                    String desc = jsonObjectVo.getString("desc");
                    err.add("短信发送失败，手机号："+phone+"失败原因："+desc);
                }
            }
            if (err.size()>0){
                throw new RuntimeException(err.toString());
            }
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return validateCode;
    }

    public static void a(){
        Long timestamp = new Date().getTime();
        appKey = "40XyhH";
        appSecret = "fA7FrY";
        appCode = "1000";
        if (StringUtils.isEmpty(appKey) || StringUtils.isEmpty(appSecret) || StringUtils.isEmpty(appCode)){
            throw new ServiceException("请设置appKey");
        }
        String sign = Md5Utils.hash(appKey+appSecret+timestamp);
        Integer number = 200;
        //短信发送请求url
        String url="http://47.242.85.7:9090/sms/report/v1";
        //响应
        HttpRequest post = HttpUtil.createPost(url);
        post.timeout(3000);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("appkey",appKey);
        jsonObject.put("appcode",appCode);
        jsonObject.put("sign",sign);
        jsonObject.put("timestamp",timestamp);
        jsonObject.put("number",number);
        post.body(jsonObject.toJSONString(),"application/json");
        post.charset(StandardCharsets.UTF_8);
        String response = post.execute().body();
        System.out.println(response);
    }

    public static void main(String[] args) {
        a();
    }
}
