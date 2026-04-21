package com.ruoyi.system.service;

import com.ruoyi.system.domain.TrumpetInfo;

import java.util.List;

/**
 * 喇叭信息Service接口
 * 
 * @author ruoyi
 * @date 2023-11-04
 */
public interface ITrumpetInfoService 
{
    /**
     * 查询喇叭信息
     * 
     * @param id 喇叭信息主键
     * @return 喇叭信息
     */
    public TrumpetInfo selectTrumpetInfoById(Long id);

    /**
     * 查询喇叭信息列表
     * 
     * @param trumpetInfo 喇叭信息
     * @return 喇叭信息集合
     */
    public List<TrumpetInfo> selectTrumpetInfoList(TrumpetInfo trumpetInfo);

    /**
     * 新增喇叭信息
     * 
     * @param trumpetInfo 喇叭信息
     * @return 结果
     */
    public int insertTrumpetInfo(TrumpetInfo trumpetInfo);

    /**
     * 修改喇叭信息
     * 
     * @param trumpetInfo 喇叭信息
     * @return 结果
     */
    public int updateTrumpetInfo(TrumpetInfo trumpetInfo);

    /**
     * 批量删除喇叭信息
     * 
     * @param ids 需要删除的喇叭信息主键集合
     * @return 结果
     */
    public int deleteTrumpetInfoByIds(Long[] ids);

    /**
     * 删除喇叭信息信息
     * 
     * @param id 喇叭信息主键
     * @return 结果
     */
    public int deleteTrumpetInfoById(Long id);
}
