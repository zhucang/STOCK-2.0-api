package com.ruoyi.system.service;

import com.ruoyi.system.domain.StockEverydayRecord;

import java.util.List;

/**
 * 股票日涨幅记录Service接口
 * 
 * @author ruoyi
 * @date 2023-11-02
 */
public interface IStockEverydayRecordService 
{
    /**
     * 查询股票日涨幅记录
     * 
     * @param id 股票日涨幅记录主键
     * @return 股票日涨幅记录
     */
    public StockEverydayRecord selectStockEverydayRecordById(Long id);

    /**
     * 查询股票日涨幅记录列表
     * 
     * @param stockEverydayRecord 股票日涨幅记录
     * @return 股票日涨幅记录集合
     */
    public List<StockEverydayRecord> selectStockEverydayRecordList(StockEverydayRecord stockEverydayRecord);

    /**
     * 新增股票日涨幅记录
     * 
     * @param stockEverydayRecord 股票日涨幅记录
     * @return 结果
     */
    public int insertStockEverydayRecord(StockEverydayRecord stockEverydayRecord);

    /**
     * 修改股票日涨幅记录
     * 
     * @param stockEverydayRecord 股票日涨幅记录
     * @return 结果
     */
    public int updateStockEverydayRecord(StockEverydayRecord stockEverydayRecord);

    /**
     * 批量删除股票日涨幅记录
     * 
     * @param ids 需要删除的股票日涨幅记录主键集合
     * @return 结果
     */
    public int deleteStockEverydayRecordByIds(Long[] ids);

    /**
     * 删除股票日涨幅记录信息
     * 
     * @param id 股票日涨幅记录主键
     * @return 结果
     */
    public int deleteStockEverydayRecordById(Long id);

    /**
     * 清空股票日涨幅记录信息
     *
     * @param productCodes 股票代码
     * @return 结果
     */
    public int cleanStockEverydayRecord(List<String> productCodes);

    /**
     * 每日收盘时保存每日数据
     */
    public void saveStockEverydayRecordTask();
}
