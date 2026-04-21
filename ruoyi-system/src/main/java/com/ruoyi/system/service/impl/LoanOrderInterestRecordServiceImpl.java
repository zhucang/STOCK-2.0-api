package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.LoanOrderInterestRecord;
import com.ruoyi.system.mapper.LoanOrderInterestRecordMapper;
import com.ruoyi.system.service.ILoanOrderInterestRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 贷款订单利息生成记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-05-23
 */
@Service
public class LoanOrderInterestRecordServiceImpl implements ILoanOrderInterestRecordService 
{
    @Resource
    private LoanOrderInterestRecordMapper loanOrderInterestRecordMapper;

    /**
     * 查询贷款订单利息生成记录
     * 
     * @param id 贷款订单利息生成记录主键
     * @return 贷款订单利息生成记录
     */
    @Override
    public LoanOrderInterestRecord selectLoanOrderInterestRecordById(Long id)
    {
        return loanOrderInterestRecordMapper.selectLoanOrderInterestRecordById(id);
    }

    /**
     * 查询贷款订单利息生成记录列表
     * 
     * @param loanOrderInterestRecord 贷款订单利息生成记录
     * @return 贷款订单利息生成记录
     */
    @Override
    public List<LoanOrderInterestRecord> selectLoanOrderInterestRecordList(LoanOrderInterestRecord loanOrderInterestRecord)
    {
        return loanOrderInterestRecordMapper.selectLoanOrderInterestRecordList(loanOrderInterestRecord);
    }

    /**
     * 新增贷款订单利息生成记录
     * 
     * @param loanOrderInterestRecord 贷款订单利息生成记录
     * @return 结果
     */
    @Override
    public int insertLoanOrderInterestRecord(LoanOrderInterestRecord loanOrderInterestRecord)
    {
        loanOrderInterestRecord.setCreateTime(DateUtils.getNowDate());
        return loanOrderInterestRecordMapper.insertLoanOrderInterestRecord(loanOrderInterestRecord);
    }

    /**
     * 修改贷款订单利息生成记录
     * 
     * @param loanOrderInterestRecord 贷款订单利息生成记录
     * @return 结果
     */
    @Override
    public int updateLoanOrderInterestRecord(LoanOrderInterestRecord loanOrderInterestRecord)
    {
        return loanOrderInterestRecordMapper.updateLoanOrderInterestRecord(loanOrderInterestRecord);
    }

    /**
     * 批量删除贷款订单利息生成记录
     * 
     * @param ids 需要删除的贷款订单利息生成记录主键
     * @return 结果
     */
    @Override
    public int deleteLoanOrderInterestRecordByIds(Long[] ids)
    {
        return loanOrderInterestRecordMapper.deleteLoanOrderInterestRecordByIds(ids);
    }

    /**
     * 删除贷款订单利息生成记录信息
     * 
     * @param id 贷款订单利息生成记录主键
     * @return 结果
     */
    @Override
    public int deleteLoanOrderInterestRecordById(Long id)
    {
        return loanOrderInterestRecordMapper.deleteLoanOrderInterestRecordById(id);
    }
}
