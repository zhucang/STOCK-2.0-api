package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.AppConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * app配置Mapper接口
 * 
 * @author ruoyi
 * @date 2023-12-20
 */
public interface AppConfigMapper 
{
    /**
     * 查询app配置
     * 
     * @param id app配置主键
     * @return app配置
     */
    public AppConfig selectAppConfigById(Long id);

    /**
     * 查询app配置列表
     * 
     * @param appConfig app配置
     * @return app配置集合
     */
    public List<AppConfig> selectAppConfigList(AppConfig appConfig);

    /**
     * 新增app配置
     * 
     * @param appConfig app配置
     * @return 结果
     */
    public int insertAppConfig(AppConfig appConfig);

    /**
     * 批量新增app配置
     *
     * @param appConfigs app配置列表
     * @return 结果
     */
    public int insertAppConfigs(@Param("appConfigs") List<AppConfig> appConfigs);

    /**
     * 修改app配置
     * 
     * @param appConfig app配置
     * @return 结果
     */
    public int updateAppConfig(AppConfig appConfig);

    /**
     * 删除app配置
     * 
     * @param id app配置主键
     * @return 结果
     */
    public int deleteAppConfigById(Long id);

    /**
     * 批量删除app配置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAppConfigByIds(Long[] ids);
}
