package com.ruoyi.system.service;

import com.ruoyi.system.domain.CryptocurrencyEverydayRecord;

import java.util.List;

/**
 * 加密货币日涨幅记录Service接口
 * 
 * @author ruoyi
 * @date 2023-11-02
 */
public interface ICryptocurrencyEverydayRecordService 
{
    /**
     * 查询加密货币日涨幅记录
     * 
     * @param id 加密货币日涨幅记录主键
     * @return 加密货币日涨幅记录
     */
    public CryptocurrencyEverydayRecord selectCryptocurrencyEverydayRecordById(Long id);

    /**
     * 查询加密货币日涨幅记录列表
     * 
     * @param cryptocurrencyEverydayRecord 加密货币日涨幅记录
     * @return 加密货币日涨幅记录集合
     */
    public List<CryptocurrencyEverydayRecord> selectCryptocurrencyEverydayRecordList(CryptocurrencyEverydayRecord cryptocurrencyEverydayRecord);

    /**
     * 新增加密货币日涨幅记录
     * 
     * @param cryptocurrencyEverydayRecord 加密货币日涨幅记录
     * @return 结果
     */
    public int insertCryptocurrencyEverydayRecord(CryptocurrencyEverydayRecord cryptocurrencyEverydayRecord);

    /**
     * 修改加密货币日涨幅记录
     * 
     * @param cryptocurrencyEverydayRecord 加密货币日涨幅记录
     * @return 结果
     */
    public int updateCryptocurrencyEverydayRecord(CryptocurrencyEverydayRecord cryptocurrencyEverydayRecord);

    /**
     * 批量删除加密货币日涨幅记录
     * 
     * @param ids 需要删除的加密货币日涨幅记录主键集合
     * @return 结果
     */
    public int deleteCryptocurrencyEverydayRecordByIds(Long[] ids);

    /**
     * 删除加密货币日涨幅记录信息
     * 
     * @param id 加密货币日涨幅记录主键
     * @return 结果
     */
    public int deleteCryptocurrencyEverydayRecordById(Long id);


    /**
     * 清空加密货币日涨幅记录信息
     *
     * @param productCodes 产品代码
     * @return 结果
     */
    public int cleanCryptocurrencyEverydayRecord(List<String> productCodes);

    /**
     * 每日收盘时保存每日数据
     */
    public void saveCryptocurrencyEverydayRecordTask();
}
