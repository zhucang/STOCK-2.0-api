package com.ruoyi;

import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.service.IOtherValueService;
import com.ruoyi.system.websocket.WebsocketClient;
import com.ruoyi.web.controller.system.NginxConfigController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 系统启动执行类
 */

@Component
public class ApplicationRunner implements CommandLineRunner {

    @Autowired
    private IOtherValueService otherValueService;

    @Autowired
    private RedisCache redisCache;

    @Override
    public void run(String... args) throws Exception {
        //清空cacheable缓存
        redisCache.deleteObject(redisCache.keys("cacheable:*"));
        redisCache.deleteObject(redisCache.keys("constant_cache:*"));
        //重载其他值缓存
        otherValueService.reloadOtherValueCache();
        //初始化连接websocket
        WebsocketClient.initWebsocket();
        //重新加载跨域白名单与nginx
        SpringUtils.getBean(NginxConfigController.class).reloadConfig();
    }
}
