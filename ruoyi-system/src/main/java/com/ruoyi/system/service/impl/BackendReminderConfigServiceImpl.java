package com.ruoyi.system.service.impl;

import com.ruoyi.common.constant.CacheableKey;
import com.ruoyi.system.domain.BackendReminderConfig;
import com.ruoyi.system.mapper.BackendReminderConfigMapper;
import com.ruoyi.system.service.IBackendReminderConfigService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 后台提醒配置Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-07-07
 */
@Service
public class BackendReminderConfigServiceImpl implements IBackendReminderConfigService 
{
    @Resource
    private BackendReminderConfigMapper backendReminderConfigMapper;

    /**
     * 查询后台提醒配置
     * 
     * @param id 后台提醒配置主键
     * @return 后台提醒配置
     */
    @Override
//    @Cacheable(value = CacheableKey.BACKEND_REMINDER_CONFIG + CacheableKey.ENTITY,key = "#id")
    public BackendReminderConfig selectBackendReminderConfigById(Long id)
    {
        return backendReminderConfigMapper.selectBackendReminderConfigById(id);
    }

    /**
     * 查询后台提醒配置列表
     * 
     * @param backendReminderConfig 后台提醒配置
     * @return 后台提醒配置
     */
    @Override
//    @Cacheable(value = CacheableKey.BACKEND_REMINDER_CONFIG + CacheableKey.LIST,key = "#backendReminderConfig.cacheableKey()")
    public List<BackendReminderConfig> selectBackendReminderConfigList(BackendReminderConfig backendReminderConfig)
    {
        return backendReminderConfigMapper.selectBackendReminderConfigList(backendReminderConfig);
    }

    /**
     * 修改后台提醒配置
     * 
     * @param backendReminderConfig 后台提醒配置
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.BACKEND_REMINDER_CONFIG + CacheableKey.ENTITY,key = "#backendReminderConfig.id"),
            @CacheEvict(value = CacheableKey.BACKEND_REMINDER_CONFIG + CacheableKey.LIST,allEntries = true)})
    public int updateBackendReminderConfig(BackendReminderConfig backendReminderConfig)
    {
        return backendReminderConfigMapper.updateBackendReminderConfig(backendReminderConfig);
    }
}
