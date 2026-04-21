package com.ruoyi.system.utils.cache;

import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.cache.CacheUtil;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.domain.CurrencyExchangeRate;

/**
 * 缓存工具类
 */
public class CacheUtils extends CacheUtil {

    private static RedisCache redisCache = SpringUtils.getBean(RedisCache.class);


    /**
     * 获取汇率信息
     * @param fromId 转化币种id
     * @param toId 被转化币种id
     * @return
     */
    public static CurrencyExchangeRate getCurrencyExchangeRate(Long fromId,Long toId){
        //mapKey
        String mapKey = "currencyExchangeRate:";
        return redisCache.getCacheMapValue(mapKey,fromId + "/" + toId);
    }

}
