package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.CryptocurrencyEverydayRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 加密货币日涨幅记录Mapper接口
 * 
 * @author ruoyi
 * @date 2023-11-02
 */
public interface CryptocurrencyEverydayRecordMapper 
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
     * 批量新增加密货币日涨幅记录
     *
     * @param everydayRecords 加密货币日涨幅记录
     * @return 结果
     */
    public int insertCryptocurrencyEverydayRecords(@Param("everydayRecords") List<CryptocurrencyEverydayRecord> everydayRecords);

    /**
     * 修改加密货币日涨幅记录
     * 
     * @param cryptocurrencyEverydayRecord 加密货币日涨幅记录
     * @return 结果
     */
    public int updateCryptocurrencyEverydayRecord(CryptocurrencyEverydayRecord cryptocurrencyEverydayRecord);

    /**
     * 删除加密货币日涨幅记录
     * 
     * @param id 加密货币日涨幅记录主键
     * @return 结果
     */
    public int deleteCryptocurrencyEverydayRecordById(Long id);

    /**
     * 批量删除加密货币日涨幅记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCryptocurrencyEverydayRecordByIds(Long[] ids);

    /**
     * 清空加密货币日涨幅记录信息
     *
     * @param productCodes 产品代码
     * @return 结果
     */
    public int cleanCryptocurrencyEverydayRecord(@Param("productCodes") List<String> productCodes);

    /**
     * 根据产品代码获取产品的最新数据
     * @param productCode 产品代码
     * @return
     */
    CryptocurrencyEverydayRecord selectLastRecordByProductCode(String productCode);
}
