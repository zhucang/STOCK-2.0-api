package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.ForexEverydayRecord;

/**
 * 外汇日涨幅记录Service接口
 * 
 * @author ruoyi
 * @date 2023-11-02
 */
public interface IForexEverydayRecordService 
{
    /**
     * 查询外汇日涨幅记录
     * 
     * @param id 外汇日涨幅记录主键
     * @return 外汇日涨幅记录
     */
    public ForexEverydayRecord selectForexEverydayRecordById(Long id);

    /**
     * 查询外汇日涨幅记录列表
     * 
     * @param forexEverydayRecord 外汇日涨幅记录
     * @return 外汇日涨幅记录集合
     */
    public List<ForexEverydayRecord> selectForexEverydayRecordList(ForexEverydayRecord forexEverydayRecord);

    /**
     * 新增外汇日涨幅记录
     * 
     * @param forexEverydayRecord 外汇日涨幅记录
     * @return 结果
     */
    public int insertForexEverydayRecord(ForexEverydayRecord forexEverydayRecord);

    /**
     * 修改外汇日涨幅记录
     * 
     * @param forexEverydayRecord 外汇日涨幅记录
     * @return 结果
     */
    public int updateForexEverydayRecord(ForexEverydayRecord forexEverydayRecord);

    /**
     * 批量删除外汇日涨幅记录
     * 
     * @param ids 需要删除的外汇日涨幅记录主键集合
     * @return 结果
     */
    public int deleteForexEverydayRecordByIds(Long[] ids);

    /**
     * 删除外汇日涨幅记录信息
     * 
     * @param id 外汇日涨幅记录主键
     * @return 结果
     */
    public int deleteForexEverydayRecordById(Long id);
}
