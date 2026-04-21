package com.ruoyi.system.utils.sms;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.service.ISiteInfoService;
import com.ruoyi.system.utils.cache.CacheUtils;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * 短信发送工具
 *
 * @Author: 东莞呵呵
 */
@Component
public class SmsSendUtils3 {

    private static ISiteInfoService siteInfoService = SpringUtils.getBean(ISiteInfoService.class);

    /**
     * 发送短信
     * @param numbers 短信接收号码，多个号码之间以英文逗号分隔
     */
    public static String sendSms(String numbers) {
        String baseUrl = "https://api.4321.sh/inter/send";
        String apiKey = "";
        String apiPwd = "";
        String appId = "";
        //发送短信appKey
        String smsAppKey = CacheUtils.getOtherValueByKey("sms_appKey", String.class);
        try{
            String[] split = smsAppKey.split(",");
            apiKey = split[1];
            apiPwd = split[2];
            appId = split[3];
        }catch (Exception e){
            throw new ServiceException("获取appKey异常");
        }
        //平台名称
        String siteName = siteInfoService.selectSiteInfoById(1L).getSiteName();
        //随机六位数验证码
        Random random = new Random();
        String validateCode = "";
        int randString = 0;
        for (int i = 0; i < 6; i++) {
            randString = random.nextInt(10);
            validateCode = validateCode + randString;
        }
        //短信内容
        String content = "Dear new " + siteName + " users you use your cell phone number to register your " + siteName + " account your verification code is " + validateCode + ", wish you happy usi";
        //发送短信内容
        String smsContent = CacheUtils.getOtherValueByKey("sms_content", String.class);
        if (StringUtils.isNotEmpty(smsContent)){
            content = smsContent;
            content = content.replace("{siteName}",siteName);
            content = content.replace("{validateCode}",validateCode);
        }
        //请求url
        final String url = baseUrl.concat("");
        //post请求
        HttpRequest request = HttpRequest.post(url);
        request.header(Header.CONTENT_TYPE, "application/json;charset=UTF-8");
        final String params = JSONUtil.createObj()
                .set("apikey", apiKey)
                .set("secret", apiPwd)
                .set("content", content)
                .set("mobile", numbers)
                .set("template_id", appId)
                .toString();

        HttpResponse response = request.body(params).execute();
        try {
            if (!response.isOk()) {
                throw new Exception("请求失败");
            }
            //响应
            String responseStr = response.body();
            JSONObject jsonObject = JSONObject.parseObject(responseStr);
            //状态码
            String code = jsonObject.getString("code");
            if (!"0".equals(code)){
                throw new Exception(jsonObject.getString("msg"));
            }
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return validateCode;
    }

    public static void main(String[] args) {
//        sendSms("447578324774");
        sendSms("447950690504");
    }

}