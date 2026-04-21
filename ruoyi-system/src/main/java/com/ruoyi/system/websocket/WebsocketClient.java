package com.ruoyi.system.websocket;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.core.domain.ticker.TickerInfo;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.cache.CacheUtil;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.domain.CryptocurrencyProduct;
import com.ruoyi.system.domain.ForexProduct;
import com.ruoyi.system.domain.FuturesProduct;
import com.ruoyi.system.domain.StockProduct;
import com.ruoyi.system.mapper.CryptocurrencyProductMapper;
import com.ruoyi.system.mapper.ForexProductMapper;
import com.ruoyi.system.mapper.FuturesProductMapper;
import com.ruoyi.system.mapper.StockProductMapper;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * WebsocketClient
 */
public class WebsocketClient extends WebSocketClient {

    private static final Logger log = LoggerFactory.getLogger(WebsocketClient.class);

    /**
     * 正进行中的websocketClient
     */
    private static WebsocketClient instance;

    /**
     * redisCache
     */
    private RedisCache redisCache = SpringUtils.getBean(RedisCache.class);

    /**
     * 中间服务器ip端口
     */
    private static String host = CacheUtil.getOtherValueByKey("middleQuote_hostAddress", String.class);

    /**
     * 秘钥
     */
    public static String securityKey = CacheUtil.getOtherValueByKey("websocket_securityKey", String.class);

    /**
     * 默认连接
     */
    private static WebsocketClient defaultWebsocketClient;

    /**
     * 单锁
     */
    private static int lock = 0;

    public WebsocketClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        log.info("socket连接成功");
        WebsocketClient.defaultWebsocketClient = this;
        reloadQuoteProduct();
    }

    @Override
    public void onMessage(String s) {
        try {
            Map<String, Object> map = JSONObject.parseObject(s, Map.class);
            for (Map.Entry<String, Object> objectEntry : map.entrySet()) {
                objectEntry.setValue(JSONObject.parseObject(JSONObject.toJSONString(objectEntry.getValue()),TickerInfo.class));
            }
            redisCache.setCacheMap("productQuote",map);
        }catch (Exception e){
            System.out.println("解析行情异常");
        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        //如果锁开启，则跳过，不允许同时进行两个onClose方法
        if (getLock() == 1){
            return;
        }
        //启用锁
        setLock(1);
        log.error("socket连接关闭");
        //五秒后尝试重新连接
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.schedule(()->{
            try {
                initWebsocket();
            } catch (Exception e) {
                log.error("socket重连失败");
            }finally {
                //解开锁
                setLock(0);
            }
        },5, TimeUnit.SECONDS);
        service.shutdown();
    }

    @Override
    public void onError(Exception e) {
        log.error("socket连接异常");
    }

    public void sendMessage(String message){
        this.send(message);
    }

    public static WebsocketClient getInstance(){
        return instance;
    }

    public static void setInstance(WebsocketClient instance) {
        WebsocketClient.instance = instance;
    }

    public static String getHost() {
        if (StringUtils.isEmpty(host)){
            return SpringUtils.getRequiredProperty("middle.ip")+":"+SpringUtils.getRequiredProperty("middle.port");
        }
        return host;
    }

    public static void setHost(String host) {
        WebsocketClient.host = host;
    }

    public static int getLock() {
        return lock;
    }

    public static void setLock(int lock) {
        WebsocketClient.lock = lock;
    }

    /**
     * 初始化连接websocket
     */
    public static void initWebsocket() throws Exception{
        //随机创建连接用户名
        UUID uuid = UUID.randomUUID();
        //创建websocketClient
        WebsocketClient websocketClient = new WebsocketClient(new URI("ws://"+getHost()+"/websocket/productQuote/"+uuid));
        //标记当前websocketClient
        setInstance(websocketClient);
        log.info("socket正在快速连接");
        //连接
        websocketClient.connect();
    }

    /**
     * 重新加载行情产品
     */
    public static void reloadQuoteProduct(){
        /**
         * otherInfo
         */
        Map<String,String> otherInfo = new HashMap<>();
        String[] key = securityKey.split(",");
        otherInfo.put("siteName",key[0]);
        otherInfo.put("securityKey",key[1]);
        otherInfo.put("firstTransfer1","0");
        otherInfo.put("firstTransfer2","0");
        otherInfo.put("firstTransfer3","0");
        otherInfo.put("firstTransfer4","0");
        otherInfo.put("productCodes1",SpringUtils.getBean(StockProductMapper.class).selectStockProductList(new StockProduct()).stream().map(a -> a.getProductCode()).collect(Collectors.joining(",")));
        otherInfo.put("productCodes2",SpringUtils.getBean(CryptocurrencyProductMapper.class).selectCryptocurrencyProductList(new CryptocurrencyProduct()).stream().map(a -> a.getProductCode()).collect(Collectors.joining(",")));
        otherInfo.put("productCodes3",SpringUtils.getBean(FuturesProductMapper.class).selectFuturesProductList(new FuturesProduct()).stream().map(a -> a.getProductCode()).collect(Collectors.joining(",")));
        otherInfo.put("productCodes4",SpringUtils.getBean(ForexProductMapper.class).selectForexProductList(new ForexProduct()).stream().map(a -> a.getProductCode()).collect(Collectors.joining(",")));
        WebsocketClient.defaultWebsocketClient.sendMessage(JSONObject.toJSONString(otherInfo));
    }
}
