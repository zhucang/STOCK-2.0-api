package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.FuturesEverydayRecord;

/**
 * 期货日涨幅记录Service接口
 * 
 * @author ruoyi
 * @date 2023-11-02
 */
public interface IFuturesEverydayRecordService 
{
    /**
     * 查询期货日涨幅记录
     * 
     * @param id 期货日涨幅记录主键
     * @return 期货日涨幅记录
     */
    public FuturesEverydayRecord selectFuturesEverydayRecordById(Long id);

    /**
     * 查询期货日涨幅记录列表
     * 
     * @param futuresEverydayRecord 期货日涨幅记录
     * @return 期货日涨幅记录集合
     */
    public List<FuturesEverydayRecord> selectFuturesEverydayRecordList(FuturesEverydayRecord futuresEverydayRecord);

    /**
     * 新增期货日涨幅记录
     * 
     * @param futuresEverydayRecord 期货日涨幅记录
     * @return 结果
     */
    public int insertFuturesEverydayRecord(FuturesEverydayRecord futuresEverydayRecord);

    /**
     * 修改期货日涨幅记录
     * 
     * @param futuresEverydayRecord 期货日涨幅记录
     * @return 结果
     */
    public int updateFuturesEverydayRecord(FuturesEverydayRecord futuresEverydayRecord);

    /**
     * 批量删除期货日涨幅记录
     * 
     * @param ids 需要删除的期货日涨幅记录主键集合
     * @return 结果
     */
    public int deleteFuturesEverydayRecordByIds(Long[] ids);

    /**
     * 删除期货日涨幅记录信息
     * 
     * @param id 期货日涨幅记录主键
     * @return 结果
     */
    public int deleteFuturesEverydayRecordById(Long id);
}
