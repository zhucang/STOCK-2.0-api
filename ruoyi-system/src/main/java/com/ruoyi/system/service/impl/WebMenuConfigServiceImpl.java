package com.ruoyi.system.service.impl;

import com.ruoyi.common.constant.CacheableKey;
import com.ruoyi.system.domain.WebMenuConfig;
import com.ruoyi.system.mapper.WebMenuConfigMapper;
import com.ruoyi.system.service.IWebMenuConfigService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 官网菜单配置Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-12-11
 */
@Service
public class WebMenuConfigServiceImpl implements IWebMenuConfigService 
{
    @Resource
    private WebMenuConfigMapper webMenuConfigMapper;

    /**
     * 查询官网菜单配置
     * 
     * @param id 官网菜单配置主键
     * @return 官网菜单配置
     */
    @Override
//    @Cacheable(value = CacheableKey.WEB_MENU_CONFIG + CacheableKey.ENTITY,key = "#id")
    public WebMenuConfig selectWebMenuConfigById(Long id)
    {
        return webMenuConfigMapper.selectWebMenuConfigById(id);
    }

    /**
     * 查询官网菜单配置列表
     * 
     * @param webMenuConfig 官网菜单配置
     * @return 官网菜单配置
     */
    @Override
    @Cacheable(value = CacheableKey.WEB_MENU_CONFIG + CacheableKey.LIST,key = "#webMenuConfig.cacheableKey()")
    public List<WebMenuConfig> selectWebMenuConfigList(WebMenuConfig webMenuConfig)
    {
        return webMenuConfigMapper.selectWebMenuConfigList(webMenuConfig);
    }

    /**
     * 新增官网菜单配置
     * 
     * @param webMenuConfig 官网菜单配置
     * @return 结果
     */
    @Override
    @CacheEvict(value = CacheableKey.WEB_MENU_CONFIG + CacheableKey.LIST,allEntries = true)
    public int insertWebMenuConfig(WebMenuConfig webMenuConfig)
    {
        return webMenuConfigMapper.insertWebMenuConfig(webMenuConfig);
    }

    /**
     * 修改官网菜单配置
     * 
     * @param webMenuConfig 官网菜单配置
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.WEB_MENU_CONFIG + CacheableKey.ENTITY,key = "#webMenuConfig.id"),
            @CacheEvict(value = CacheableKey.WEB_MENU_CONFIG + CacheableKey.LIST,allEntries = true)})
    public int updateWebMenuConfig(WebMenuConfig webMenuConfig)
    {
        return webMenuConfigMapper.updateWebMenuConfig(webMenuConfig);
    }

    /**
     * 批量删除官网菜单配置
     * 
     * @param ids 需要删除的官网菜单配置主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.WEB_MENU_CONFIG + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.WEB_MENU_CONFIG + CacheableKey.LIST,allEntries = true)})
    public int deleteWebMenuConfigByIds(Long[] ids)
    {
        return webMenuConfigMapper.deleteWebMenuConfigByIds(ids);
    }

    /**
     * 删除官网菜单配置信息
     * 
     * @param id 官网菜单配置主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.WEB_MENU_CONFIG + CacheableKey.ENTITY,key = "#id"),
            @CacheEvict(value = CacheableKey.WEB_MENU_CONFIG + CacheableKey.LIST,allEntries = true)})
    public int deleteWebMenuConfigById(Long id)
    {
        return webMenuConfigMapper.deleteWebMenuConfigById(id);
    }
}
