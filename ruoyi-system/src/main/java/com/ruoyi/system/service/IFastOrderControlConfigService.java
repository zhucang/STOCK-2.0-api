package com.ruoyi.system.service;

import com.ruoyi.system.domain.FastOrderControlConfig;

import java.util.List;

/**
 * 极速交易控制配置Service接口
 * 
 * @author ruoyi
 * @date 2023-11-25
 */
public interface IFastOrderControlConfigService 
{
    /**
     * 查询极速交易控制配置
     * 
     * @param id 极速交易控制配置主键
     * @return 极速交易控制配置
     */
    public FastOrderControlConfig selectFastOrderControlConfigById(Long id);

    /**
     * 查询极速交易控制配置列表
     * 
     * @param fastOrderControlConfig 极速交易控制配置
     * @return 极速交易控制配置集合
     */
    public List<FastOrderControlConfig> selectFastOrderControlConfigList(FastOrderControlConfig fastOrderControlConfig);

    /**
     * 新增极速交易控制配置
     * 
     * @param fastOrderControlConfig 极速交易控制配置
     * @return 结果
     */
    public int insertFastOrderControlConfig(FastOrderControlConfig fastOrderControlConfig);

    /**
     * 修改极速交易控制配置
     * 
     * @param fastOrderControlConfig 极速交易控制配置
     * @return 结果
     */
    public int updateFastOrderControlConfig(FastOrderControlConfig fastOrderControlConfig);

    /**
     * 批量删除极速交易控制配置
     * 
     * @param ids 需要删除的极速交易控制配置主键集合
     * @return 结果
     */
    public int deleteFastOrderControlConfigByIds(Long[] ids);

    /**
     * 删除极速交易控制配置信息
     * 
     * @param id 极速交易控制配置主键
     * @return 结果
     */
    public int deleteFastOrderControlConfigById(Long id);
}
