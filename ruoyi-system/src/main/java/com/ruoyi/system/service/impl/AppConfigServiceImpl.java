package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.constant.CacheableKey;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.domain.AppConfig;
import com.ruoyi.system.mapper.AppConfigMapper;
import com.ruoyi.system.service.IAppConfigService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * app配置Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-12-20
 */
@Service
public class AppConfigServiceImpl implements IAppConfigService 
{
    @Resource
    private AppConfigMapper appConfigMapper;

    /**
     * 查询app配置
     * 
     * @param id app配置主键
     * @return app配置
     */
    @Override
//    @Cacheable(value = CacheableKey.APP_CONFIG + CacheableKey.ENTITY,key = "#id")
    public AppConfig selectAppConfigById(Long id)
    {
        return appConfigMapper.selectAppConfigById(id);
    }

    /**
     * 查询app配置列表
     * 
     * @param appConfig app配置
     * @return app配置
     */
    @Override
    @Cacheable(value = CacheableKey.APP_CONFIG + CacheableKey.LIST,key = "#appConfig.cacheableKey()",unless = "#appConfig.cacheableKey() == ''")
    public List<AppConfig> selectAppConfigList(AppConfig appConfig)
    {
        return appConfigMapper.selectAppConfigList(appConfig);
    }

    /**
     * 新增app配置
     * 
     * @param appConfig app配置
     * @return 结果
     */
    @Override
    @CacheEvict(value = CacheableKey.APP_CONFIG + CacheableKey.LIST,allEntries = true)
    public int insertAppConfig(AppConfig appConfig)
    {
        return appConfigMapper.insertAppConfig(appConfig);
    }

    /**
     * 修改app配置
     * 
     * @param appConfig app配置
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.APP_CONFIG + CacheableKey.ENTITY,key = "#appConfig.id"),
            @CacheEvict(value = CacheableKey.APP_CONFIG + CacheableKey.LIST,allEntries = true)})
    public int updateAppConfig(AppConfig appConfig)
    {
        return appConfigMapper.updateAppConfig(appConfig);
    }

    /**
     * 批量删除app配置
     * 
     * @param ids 需要删除的app配置主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.APP_CONFIG + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.APP_CONFIG + CacheableKey.LIST,allEntries = true)})
    public int deleteAppConfigByIds(Long[] ids)
    {
        AppConfig search = new AppConfig();
        search.getParams().put("ids", Arrays.asList(ids));
        List<AppConfig> appConfigs = appConfigMapper.selectAppConfigList(search);
        //日志记录app配置信息列表
        HttpUtils.getRequestLogParams().put("JSONArray:appConfigs", JSONObject.toJSONString(appConfigs));
        return appConfigMapper.deleteAppConfigByIds(ids);
    }

    /**
     * 删除app配置信息
     * 
     * @param id app配置主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.APP_CONFIG + CacheableKey.ENTITY,key = "#id"),
            @CacheEvict(value = CacheableKey.APP_CONFIG + CacheableKey.LIST,allEntries = true)})
    public int deleteAppConfigById(Long id)
    {
        return appConfigMapper.deleteAppConfigById(id);
    }

    /**
     * 导入
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.APP_CONFIG + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.APP_CONFIG + CacheableKey.LIST,allEntries = true)})
    public String importAppConfig(List<AppConfig> list, Boolean isUpdateSupport){
        //目前所有的app配置
        List<AppConfig> appConfigs = appConfigMapper.selectAppConfigList(new AppConfig());
        //配置map
        Map<Long, AppConfig> map = appConfigs.stream().collect(Collectors.toMap(AppConfig::getId, a -> a));
        //即将插入的新数据
        List<AppConfig> newData = new ArrayList<>();
        //遍历更新数据
        for (int i = 0; i < list.size(); i++) {
            //新数据
            AppConfig appConfig = list.get(i);
            //key
            Long key = list.get(i).getId();
            //旧数据
            AppConfig appConfigOld = map.get(key);
            if (appConfigOld == null){
                newData.add(appConfig);
            }else {
                appConfigOld.setIsVisible(null);
                appConfig.setIsVisible(null);
                if (!JSONObject.toJSONString(appConfigOld).equals(JSONObject.toJSONString(appConfig))){
                    int count = appConfigMapper.updateAppConfig(appConfig);
                    if (count <= 0){
                        throw new ServiceException("系统繁忙");
                    }
                }
            }
        }
        if (newData.size() > 0){
            int insertAppConfigs = appConfigMapper.insertAppConfigs(newData);
            if (insertAppConfigs != newData.size()){
                throw new ServiceException("系统繁忙");
            }
        }
        return "导入成功";
    }
}
