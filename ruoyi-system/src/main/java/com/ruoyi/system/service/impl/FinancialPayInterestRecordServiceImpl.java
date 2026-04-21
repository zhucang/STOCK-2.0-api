package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.FinancialPayInterestRecord;
import com.ruoyi.system.mapper.FinancialPayInterestRecordMapper;
import com.ruoyi.system.service.IFinancialPayInterestRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 理财产品派息记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-12-10
 */
@Service
public class FinancialPayInterestRecordServiceImpl implements IFinancialPayInterestRecordService 
{
    @Resource
    private FinancialPayInterestRecordMapper financialPayInterestRecordMapper;

    /**
     * 查询理财产品派息记录
     * 
     * @param id 理财产品派息记录主键
     * @return 理财产品派息记录
     */
    @Override
    public FinancialPayInterestRecord selectFinancialPayInterestRecordById(Long id)
    {
        return financialPayInterestRecordMapper.selectFinancialPayInterestRecordById(id);
    }

    /**
     * 查询理财产品派息记录列表
     * 
     * @param financialPayInterestRecord 理财产品派息记录
     * @return 理财产品派息记录
     */
    @Override
    public List<FinancialPayInterestRecord> selectFinancialPayInterestRecordList(FinancialPayInterestRecord financialPayInterestRecord)
    {
        return financialPayInterestRecordMapper.selectFinancialPayInterestRecordList(financialPayInterestRecord);
    }

    /**
     * 新增理财产品派息记录
     * 
     * @param financialPayInterestRecord 理财产品派息记录
     * @return 结果
     */
    @Override
    public int insertFinancialPayInterestRecord(FinancialPayInterestRecord financialPayInterestRecord)
    {
        return financialPayInterestRecordMapper.insertFinancialPayInterestRecord(financialPayInterestRecord);
    }

    /**
     * 修改理财产品派息记录
     * 
     * @param financialPayInterestRecord 理财产品派息记录
     * @return 结果
     */
    @Override
    public int updateFinancialPayInterestRecord(FinancialPayInterestRecord financialPayInterestRecord)
    {
        return financialPayInterestRecordMapper.updateFinancialPayInterestRecord(financialPayInterestRecord);
    }

    /**
     * 批量删除理财产品派息记录
     * 
     * @param ids 需要删除的理财产品派息记录主键
     * @return 结果
     */
    @Override
    public int deleteFinancialPayInterestRecordByIds(Long[] ids)
    {
        return financialPayInterestRecordMapper.deleteFinancialPayInterestRecordByIds(ids);
    }

    /**
     * 删除理财产品派息记录信息
     * 
     * @param id 理财产品派息记录主键
     * @return 结果
     */
    @Override
    public int deleteFinancialPayInterestRecordById(Long id)
    {
        return financialPayInterestRecordMapper.deleteFinancialPayInterestRecordById(id);
    }
}
