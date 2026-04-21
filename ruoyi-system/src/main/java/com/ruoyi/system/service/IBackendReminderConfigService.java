package com.ruoyi.system.service;

import com.ruoyi.system.domain.BackendReminderConfig;

import java.util.List;

/**
 * 后台提醒配置Service接口
 * 
 * @author ruoyi
 * @date 2024-07-07
 */
public interface IBackendReminderConfigService 
{
    /**
     * 查询后台提醒配置
     * 
     * @param id 后台提醒配置主键
     * @return 后台提醒配置
     */
    public BackendReminderConfig selectBackendReminderConfigById(Long id);

    /**
     * 查询后台提醒配置列表
     * 
     * @param backendReminderConfig 后台提醒配置
     * @return 后台提醒配置集合
     */
    public List<BackendReminderConfig> selectBackendReminderConfigList(BackendReminderConfig backendReminderConfig);

    /**
     * 修改后台提醒配置
     * 
     * @param backendReminderConfig 后台提醒配置
     * @return 结果
     */
    public int updateBackendReminderConfig(BackendReminderConfig backendReminderConfig);
}
