package com.ruoyi.system.service;

import com.ruoyi.system.domain.WebMenuConfig;

import java.util.List;

/**
 * 官网菜单配置Service接口
 * 
 * @author ruoyi
 * @date 2023-12-11
 */
public interface IWebMenuConfigService 
{
    /**
     * 查询官网菜单配置
     * 
     * @param id 官网菜单配置主键
     * @return 官网菜单配置
     */
    public WebMenuConfig selectWebMenuConfigById(Long id);

    /**
     * 查询官网菜单配置列表
     * 
     * @param webMenuConfig 官网菜单配置
     * @return 官网菜单配置集合
     */
    public List<WebMenuConfig> selectWebMenuConfigList(WebMenuConfig webMenuConfig);

    /**
     * 新增官网菜单配置
     * 
     * @param webMenuConfig 官网菜单配置
     * @return 结果
     */
    public int insertWebMenuConfig(WebMenuConfig webMenuConfig);

    /**
     * 修改官网菜单配置
     * 
     * @param webMenuConfig 官网菜单配置
     * @return 结果
     */
    public int updateWebMenuConfig(WebMenuConfig webMenuConfig);

    /**
     * 批量删除官网菜单配置
     * 
     * @param ids 需要删除的官网菜单配置主键集合
     * @return 结果
     */
    public int deleteWebMenuConfigByIds(Long[] ids);

    /**
     * 删除官网菜单配置信息
     * 
     * @param id 官网菜单配置主键
     * @return 结果
     */
    public int deleteWebMenuConfigById(Long id);
}
