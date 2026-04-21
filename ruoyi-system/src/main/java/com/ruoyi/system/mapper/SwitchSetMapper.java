package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SwitchSet;

import java.util.List;

/**
 * 开关配置Mapper接口
 * 
 * @author ruoyi
 * @date 2023-10-28
 */
public interface SwitchSetMapper 
{
    /**
     * 查询开关配置
     * 
     * @param id 开关配置主键
     * @return 开关配置
     */
    public SwitchSet selectSwitchSetById(Long id);

    /**
     * 查询开关状态
     *
     * @param id 开关配置主键
     * @return 开关配置
     */
    public Integer selectSwitchStatusById(Long id);

    /**
     * 查询开关配置列表
     * 
     * @param switchSet 开关配置
     * @return 开关配置集合
     */
    public List<SwitchSet> selectSwitchSetList(SwitchSet switchSet);

    /**
     * 新增开关配置
     * 
     * @param switchSet 开关配置
     * @return 结果
     */
    public int insertSwitchSet(SwitchSet switchSet);

    /**
     * 修改开关配置
     * 
     * @param switchSet 开关配置
     * @return 结果
     */
    public int updateSwitchSet(SwitchSet switchSet);

    /**
     * 删除开关配置
     * 
     * @param id 开关配置主键
     * @return 结果
     */
    public int deleteSwitchSetById(Long id);

    /**
     * 批量删除开关配置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSwitchSetByIds(Long[] ids);
}
