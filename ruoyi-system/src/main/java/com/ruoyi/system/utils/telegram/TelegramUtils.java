package com.ruoyi.system.utils.telegram;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.utils.cache.CacheUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Telegram工具类
 */
public class TelegramUtils {

    private static final Logger log = LoggerFactory.getLogger(TelegramUtils.class);

    private static ExecutorService executor = Executors.newFixedThreadPool(5);

    /**
     *
     * @param message
     */
    public static void sendAsyncMessage(String message, String botToken, String chatId) {
        executor.submit(() -> {
            sendTelegramMessage(message, botToken, chatId);
        });
    }

    /**
     * 发送消息
     * @param message
     */
    public static void sendTelegramMessage(String message, String botToken, String chatId) {
        if ("default".equals(botToken)) {
            botToken = CacheUtils.getOtherValueByKey("telegram_bot_token", String.class);
        }
        if ("default".equals(chatId)) {
            chatId = CacheUtils.getOtherValueByKey("telegram_message_chat_id", String.class);
        }
        if (StringUtils.isEmpty(botToken) || StringUtils.isEmpty(chatId)) {
            return;
        }
        try {
            String urlString = "https://api.telegram.org/bot" + botToken + "/sendMessage";
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // 设置请求属性
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setConnectTimeout(5000); // 5秒连接超时
            conn.setReadTimeout(5000);    // 5秒读取超时

            // 构造请求体
            String body = "chat_id=" + chatId + "&text=" + java.net.URLEncoder.encode(message, "UTF-8");

            // 写入请求体
            OutputStream os = conn.getOutputStream();
            os.write(body.getBytes(StandardCharsets.UTF_8));
            os.flush();
            os.close();

            // 获取响应
            int responseCode = conn.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                log.error("Telegram消息发送失败，响应码: " + responseCode);
            }
        } catch (Exception e) {
            log.error("Telegram消息发送失败", e);
        }
    }
}
