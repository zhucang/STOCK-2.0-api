package com.ruoyi.system.service;

import com.ruoyi.system.domain.SwitchSet;

import java.util.List;

/**
 * 开关配置Service接口
 * 
 * @author ruoyi
 * @date 2023-10-28
 */
public interface ISwitchSetService 
{
    /**
     * 查询开关配置
     * 
     * @param id 开关配置主键
     * @return 开关配置
     */
    public SwitchSet selectSwitchSetById(Long id);

    /**
     * 根据id获取开关状态
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

//    /**
//     * 新增开关配置
//     *
//     * @param switchSet 开关配置
//     * @return 结果
//     */
//    public int insertSwitchSet(SwitchSet switchSet);

    /**
     * 修改开关配置
     * 
     * @param switchSet 开关配置
     * @return 结果
     */
    public int updateSwitchSet(SwitchSet switchSet);

//    /**
//     * 批量删除开关配置
//     *
//     * @param ids 需要删除的开关配置主键集合
//     * @return 结果
//     */
//    public int deleteSwitchSetByIds(Long[] ids);
//
//    /**
//     * 删除开关配置信息
//     *
//     * @param id 开关配置主键
//     * @return 结果
//     */
//    public int deleteSwitchSetById(Long id);
}
