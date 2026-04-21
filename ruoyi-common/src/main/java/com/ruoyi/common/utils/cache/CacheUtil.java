package com.ruoyi.common.utils.cache;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.spring.SpringUtils;

/**
 * 缓存工具类
 */
public class CacheUtil {

    private static RedisCache redisCache = SpringUtils.getBean(RedisCache.class);

    /**
     * 获取其他值
     * @param otherKey key
     * @return
     */
    public static <T>T getOtherValueByKey(String otherKey,Class<T> c){
        try {
            //mapKey
            String mapKey = "otherValueCache:";
            String otherValue = redisCache.getCacheMapValue(mapKey,otherKey);
            if (c.equals(String.class)){
                return (T) otherValue;
            }
            return JSONObject.parseObject(otherValue,c);
        }catch (Exception e) {
            return null;
        }
    }
}
