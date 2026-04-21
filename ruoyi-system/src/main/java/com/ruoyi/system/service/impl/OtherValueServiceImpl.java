package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.core.domain.entity.UserInfo;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.domain.OtherValue;
import com.ruoyi.system.domain.UserFastTradeControl;
import com.ruoyi.system.mapper.OtherValueMapper;
import com.ruoyi.system.mapper.UserInfoMapper;
import com.ruoyi.system.service.IOtherValueService;
import com.ruoyi.system.service.IUserFastTradeControlService;
import com.ruoyi.system.task.inactivityNotification.InactivityNotification;
import com.ruoyi.system.utils.MiddleQuoteUtil;
import com.ruoyi.system.utils.cache.CacheUtils;
import com.ruoyi.system.websocket.WebsocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 其他值Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-03-27
 */
@Service
public class OtherValueServiceImpl implements IOtherValueService 
{
    @Resource
    private OtherValueMapper otherValueMapper;

    @Autowired
    private RedisCache redisCache;

    /**
     * 查询其他值
     * 
     * @param id 其他值主键
     * @return 其他值
     */
    @Override
    public OtherValue selectOtherValueById(Long id)
    {
        return otherValueMapper.selectOtherValueById(id);
    }

    /**
     * 查询其他值列表
     * 
     * @param otherValue 其他值
     * @return 其他值
     */
    @Override
    public List<OtherValue> selectOtherValueList(OtherValue otherValue)
    {
        return otherValueMapper.selectOtherValueList(otherValue);
    }

    /**
     * 获取其他值列表
     */
    public Map<String,String> getOtherValueMap(OtherValue otherValue){
        //mapKey
        String mapKey = "otherValueCache:";
        //其他值map
        Map<String,String> otherValueMap = redisCache.getCacheMap(mapKey);
        //如果缓存是空的
        if (otherValueMap == null){
            //重新加载缓存
            reloadOtherValueCache();
            otherValueMap = redisCache.getCacheMap(mapKey);
        }
        return otherValueMap;
    }

    /**
     * 获取其他值
     * @param otherKey key
     * @return
     */
    public <T>T getOtherValueByKey(String otherKey,Class<T> c){
        try {
            //mapKey
            String mapKey = "otherValueCache:";
            String otherValue = redisCache.getCacheMapValue(mapKey,otherKey);
            if (otherValue == null){
                //重新加载缓存
                reloadOtherValueCache();
                otherValue = redisCache.getCacheMapValue(mapKey,otherKey);
            }
            if (c.equals(String.class)){
                return (T) otherValue;
            }
            return JSONObject.parseObject(otherValue,c);
        }catch (Exception e) {
            return null;
        }
    }

    /**
     * 新增其他值
     * 
     * @param otherValue 其他值
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertOtherValue(OtherValue otherValue)
    {
        if (StringUtils.isNotEmpty(CacheUtils.getOtherValueByKey(otherValue.getOtherKey(),String.class))){
            throw new ServiceException("此key已存在");
        }
        int count = otherValueMapper.insertOtherValue(otherValue);
        if (count <= 0){
            throw new ServiceException("系统繁忙");
        }
        //附加操作
        additionalActionFilters(otherValue);
        //更新缓存
        //mapKey
        String mapKey = "otherValueCache:";
        redisCache.setCacheMapValue(mapKey,otherValue.getOtherKey(),otherValue.getOtherValue());
        return 1;
    }

    /**
     * 修改其他值
     * 
     * @param otherValue 其他值
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateOtherValue(OtherValue otherValue)
    {
        //旧信息
        OtherValue old = otherValueMapper.selectOtherValueById(otherValue.getId());
        if (!old.getOtherKey().equals(otherValue.getOtherKey())){
            throw new ServiceException("key不允许修改");
        }
        int count = otherValueMapper.updateOtherValue(otherValue);
        if (count <= 0){
            throw new ServiceException("系统繁忙");
        }
        //附加操作
        additionalActionFilters(otherValue);
        //更新缓存
        //mapKey
        String mapKey = "otherValueCache:";
        redisCache.setCacheMapValue(mapKey,otherValue.getOtherKey(),otherValue.getOtherValue());
        return 1;
    }

    /**
     * 附加操作过滤器
     * @param otherValue
     */
    void additionalActionFilters(OtherValue otherValue){
        //key
        String key = otherValue.getOtherKey();
        //值
        String value = otherValue.getOtherValue();
        if ("middleQuote_hostAddress".equals(key)){
            WebsocketClient.setHost(value);
            MiddleQuoteUtil.setHost(value);
            try {
                //重连当前socket
                WebsocketClient.getInstance().close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else if ("websocket_securityKey".equals(key)){
            WebsocketClient.securityKey = value;
        }else if ("allDemoAccount_fastTrade_control".equals(key)){
            if (!"0".equals(value) && !"1".equals(value) && !"2".equals(value)){
                throw new ServiceException("控制类型错误");
            }
            //获取所有模拟账号ID
            UserInfo userInfo = new UserInfo();
            userInfo.setAccountType(2);
            List<Long> userIds = SpringUtils.getBean(UserInfoMapper.class).selectUserInfoList(userInfo).stream().map(a -> a.getId()).collect(Collectors.toList());
            UserFastTradeControl userFastTradeControl = new UserFastTradeControl();
            userFastTradeControl.setControlType(Integer.valueOf(value));
            userFastTradeControl.getParams().put("userIds",userIds);
            SpringUtils.getBean(IUserFastTradeControlService.class).updateUserFastTradeControl(userFastTradeControl);
        } else if ("activity_monitoring_notification_token".equals(key) || "activity_monitoring_notification_chat_id".equals(key)) {
            SpringUtils.getBean(InactivityNotification.class).activityMonitoring();
        }
    }

    /**
     * 批量删除其他值
     * 
     * @param ids 需要删除的其他值主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteOtherValueByIds(Long[] ids)
    {
        OtherValue search = new OtherValue();
        search.getParams().put("ids", Arrays.asList(ids));
        List<OtherValue> otherValues = otherValueMapper.selectOtherValueList(search);
        //日志记录其他值配置信息
        HttpUtils.getRequestLogParams().put("JSONArray:otherValues", JSONObject.toJSONString(otherValues));
        int count = otherValueMapper.deleteOtherValueByIds(ids);
        if (count <= 0){
            throw new ServiceException("系统繁忙");
        }
        //重新加载缓存
        reloadOtherValueCache();
        return 1;
    }

    /**
     * 删除其他值信息
     * 
     * @param id 其他值主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteOtherValueById(Long id)
    {
        int count = otherValueMapper.deleteOtherValueById(id);
        if (count <= 0){
            throw new ServiceException("系统繁忙");
        }
        reloadOtherValueCache();
        return 1;
    }

    /**
     * 重载缓存
     */
    @Override
    public void reloadOtherValueCache(){
        //获取最新数据map
        Map<String, String> otherValueMap = otherValueMapper.selectOtherValueList(new OtherValue()).stream().collect(Collectors.toMap(a -> a.getOtherKey(), a -> a.getOtherValue()));
        //mapKey
        String mapKey = "otherValueCache:";
        //清空旧缓存
        redisCache.deleteObject(mapKey);
        //保存新缓存
        redisCache.setCacheMap(mapKey,otherValueMap);
    }
}
