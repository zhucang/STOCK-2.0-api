package com.ruoyi.system.utils.email;


import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.domain.LangMgr;
import com.ruoyi.system.domain.MailConfig;
import com.ruoyi.system.mapper.MailConfigMapper;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.*;

/**
 * 邮箱发送工具
 *
 * @Author: 东莞呵呵
 */
@Component
public class SendEmailUtils {

    private static MailConfigMapper mailConfigMapper = SpringUtils.getBean(MailConfigMapper.class);


    public static String sendEmail(String email) throws MessagingException {
        //邮件配置信息
        MailConfig mailConfig = mailConfigMapper.selectEnableMailConfig();
        // 创建Properties 类用于记录邮箱的一些属性
        Properties props = new Properties();
        // 表示SMTP发送邮件，必须进行身份验证
        props.put("mail.smtp.auth", "true");
        //此处填写SMTP服务器
        props.put("mail.smtp.host", mailConfig.getSmtpDomain());
        props.put("mail.smtp.port", mailConfig.getRequestPort());
        //请求方式 0：SSL认证 1：TLS认证
        Integer requestMethod = mailConfig.getRequestMethod();
        if (requestMethod != null && requestMethod.equals(0)){
            //SSL认证（端口 25 或 465）
            props.put("mail.smtp.ssl.enable", "true");
        }else {
            //TLS认证 （端口 587）
            props.put("mail.smtp.starttls.enable", "true");
        }
        // 此处填写，写信人的账号
        props.put("mail.user", mailConfig.getEmailAccount());
        // 此处填写16位STMP口令
        //应用密码
        String mailPassword = mailConfigMapper.selectEnableMailPassword();
        props.put("mail.password", mailPassword);
//        //代理
//        props.put("proxySet", "true");
//        props.put("mail.smtp.socks.host","127.0.0.1");
//        props.put("mail.smtp.socks.port","10010");

        // 构建授权信息，用于进行SMTP进行身份验证
        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码，都不用改直接copy
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };
        // 使用环境属性和授权信息，创建邮件会话
        Session mailSession = Session.getInstance(props, authenticator);
        // 创建邮件消息
        MimeMessage message = new MimeMessage(mailSession);
        // 设置发件人，
        InternetAddress form = new InternetAddress(props.getProperty("mail.user"));
        message.setFrom(form);
        // 设置收件人的邮箱
        InternetAddress to = new InternetAddress(email);
        message.setRecipient(Message.RecipientType.TO, to);
//        //马甲
//        message.addHeader("X-Mailer","Microsoft Outlook Express 6.00.2900.2869");


        //随机六位数验证码
        Random random = new Random();
        String code = "";
        int randString = 0;
        for (int i = 0; i < 6; i++) {
            randString = random.nextInt(10);
            code = code + randString;
        }

        message = template(message, code,mailConfig);

        //发送邮件
        Transport.send(message);
        return code;
    }

    //邮件模板
    public static MimeMessage template(MimeMessage message,String code,MailConfig mailConfig) throws MessagingException {
        //邮件标题
        String emailTitle = mailConfig.getEmailTitle();
        //邮件标题多语言
        LangMgr emailTitleLang = mailConfig.getEmailTitleLang();
        try {
            Object langValue = PropertyUtils.describe(emailTitleLang).get(HttpUtils.getLang());
            emailTitle = langValue != null? String.valueOf(langValue) : "";
        }catch (Exception e){

        }

        //邮件标题
        String emailContent = mailConfig.getEmailContent();
        //邮件标题多语言
        LangMgr emailContentLang = mailConfig.getEmailContentLang();
        try {
            Object langValue = PropertyUtils.describe(emailContentLang).get(HttpUtils.getLang());
            emailContent = langValue != null? String.valueOf(langValue) : "";
        }catch (Exception e){

        }

        // 设置邮件标题
        message.setSubject(emailTitle,"UTF-8");
        // 6. 创建文本"节点"
        MimeBodyPart text = new MimeBodyPart();
        // 这里添加图片的方式是将整个图片包含到邮件内容中, 实际上也可以以 http 链接的形式添加网络图片
        List<String> param = new ArrayList<>();
        param.add(code);
        text.setContent(StringUtils.fillByIndex(emailContent,param),"text/html;charset=UTF-8");
        MimeMultipart mm_text_image = new MimeMultipart();
        mm_text_image.addBodyPart(text);
        mm_text_image.setSubType("related");    // 关联关系
        // 11. 设置整个邮件的关系（将最终的混合"节点"作为邮件的内容添加到邮件对象）
        message.setContent(mm_text_image);
        //设置邮件的发送时间,默认立即发送
        message.setSentDate(new Date());
        return message;
    }
}