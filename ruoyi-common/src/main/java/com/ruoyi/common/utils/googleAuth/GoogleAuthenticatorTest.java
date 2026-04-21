package com.ruoyi.common.utils.googleAuth;

import com.google.zxing.WriterException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;


public class GoogleAuthenticatorTest {
    public static void main(String[] args) throws WriterException, IOException {

        // 生成随机的密钥
        String secretKey = GoogleAuthenticatorUtils.getRandomSecretKey();
        System.out.println("随机密钥：" + secretKey);

        // 根据验证码，账户，服务商生成 TOPT 密钥的 URI
        String uri = GoogleAuthenticatorUtils.getGoogleAuthenticatorBarCode(secretKey, "", "");
        System.out.println("TOPT密钥URI：" + uri);
        String uploadFileName = UUID.randomUUID() + "." + "png";
        // 根据 TOPT 密钥的 URI生成二维码，存储在本地
        File file = new File("C:\\google-auth");
        if (!file.exists()){
            file.mkdir();
        }
        FileOutputStream fileOutputStream = new FileOutputStream("C:\\google-auth\\"+uploadFileName);
        GoogleAuthenticatorUtils.createQRCode(uri, fileOutputStream, 200, 200);
        fileOutputStream.close();

        String lastCode = null;
        while (true) {
            // 根据密钥获取此刻的动态口令
            String code = GoogleAuthenticatorUtils.getTOTPCode(secretKey);
            if (!code.equals(lastCode)) {
                System.out.println("刷新了验证码：" + code + " " + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
            }
            lastCode = code;
            try {
                Thread.sleep(1000);  // 线程暂停1秒
            } catch (InterruptedException e) {};
        }
    }
}