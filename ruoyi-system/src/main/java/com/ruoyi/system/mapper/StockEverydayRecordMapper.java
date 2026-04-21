package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.StockEverydayRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 股票日涨幅记录Mapper接口
 * 
 * @author ruoyi
 * @date 2023-11-02
 */
public interface StockEverydayRecordMapper 
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
     * 批量新增股票日涨幅记录
     *
     * @param stockEverydayRecords 股票日涨幅记录
     */
    public int insertStockEverydayRecords(@Param("stockEverydayRecords") List<StockEverydayRecord> stockEverydayRecords);

    /**
     * 修改股票日涨幅记录
     * 
     * @param stockEverydayRecord 股票日涨幅记录
     * @return 结果
     */
    public int updateStockEverydayRecord(StockEverydayRecord stockEverydayRecord);

    /**
     * 删除股票日涨幅记录
     * 
     * @param id 股票日涨幅记录主键
     * @return 结果
     */
    public int deleteStockEverydayRecordById(Long id);

    /**
     * 批量删除股票日涨幅记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStockEverydayRecordByIds(Long[] ids);

    /**
     * 清空股票日涨幅记录信息
     *
     * @param productCodes 股票代码
     * @return 结果
     */
    public int cleanStockEverydayRecord(@Param("productCodes") List<String> productCodes);

    /**
     * 根据产品代码获取产品的最新数据
     * @param productCode 产品代码
     * @return
     */
    StockEverydayRecord selectLastRecordByProductCode(String productCode);
}
