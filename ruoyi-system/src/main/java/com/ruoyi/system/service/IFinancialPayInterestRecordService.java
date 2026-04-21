package com.ruoyi.system.service;

import com.ruoyi.system.domain.FinancialPayInterestRecord;

import java.util.List;

/**
 * 理财产品派息记录Service接口
 * 
 * @author ruoyi
 * @date 2023-12-10
 */
public interface IFinancialPayInterestRecordService 
{
    /**
     * 查询理财产品派息记录
     * 
     * @param id 理财产品派息记录主键
     * @return 理财产品派息记录
     */
    public FinancialPayInterestRecord selectFinancialPayInterestRecordById(Long id);

    /**
     * 查询理财产品派息记录列表
     * 
     * @param financialPayInterestRecord 理财产品派息记录
     * @return 理财产品派息记录集合
     */
    public List<FinancialPayInterestRecord> selectFinancialPayInterestRecordList(FinancialPayInterestRecord financialPayInterestRecord);

    /**
     * 新增理财产品派息记录
     * 
     * @param financialPayInterestRecord 理财产品派息记录
     * @return 结果
     */
    public int insertFinancialPayInterestRecord(FinancialPayInterestRecord financialPayInterestRecord);

    /**
     * 修改理财产品派息记录
     * 
     * @param financialPayInterestRecord 理财产品派息记录
     * @return 结果
     */
    public int updateFinancialPayInterestRecord(FinancialPayInterestRecord financialPayInterestRecord);

    /**
     * 批量删除理财产品派息记录
     * 
     * @param ids 需要删除的理财产品派息记录主键集合
     * @return 结果
     */
    public int deleteFinancialPayInterestRecordByIds(Long[] ids);

    /**
     * 删除理财产品派息记录信息
     * 
     * @param id 理财产品派息记录主键
     * @return 结果
     */
    public int deleteFinancialPayInterestRecordById(Long id);
}
