package com.ruoyi.system.service;

import com.ruoyi.system.domain.LoanOrderInterestRecord;

import java.util.List;

/**
 * 贷款订单利息生成记录Service接口
 * 
 * @author ruoyi
 * @date 2024-05-23
 */
public interface ILoanOrderInterestRecordService 
{
    /**
     * 查询贷款订单利息生成记录
     * 
     * @param id 贷款订单利息生成记录主键
     * @return 贷款订单利息生成记录
     */
    public LoanOrderInterestRecord selectLoanOrderInterestRecordById(Long id);

    /**
     * 查询贷款订单利息生成记录列表
     * 
     * @param loanOrderInterestRecord 贷款订单利息生成记录
     * @return 贷款订单利息生成记录集合
     */
    public List<LoanOrderInterestRecord> selectLoanOrderInterestRecordList(LoanOrderInterestRecord loanOrderInterestRecord);

    /**
     * 新增贷款订单利息生成记录
     * 
     * @param loanOrderInterestRecord 贷款订单利息生成记录
     * @return 结果
     */
    public int insertLoanOrderInterestRecord(LoanOrderInterestRecord loanOrderInterestRecord);

    /**
     * 修改贷款订单利息生成记录
     * 
     * @param loanOrderInterestRecord 贷款订单利息生成记录
     * @return 结果
     */
    public int updateLoanOrderInterestRecord(LoanOrderInterestRecord loanOrderInterestRecord);

    /**
     * 批量删除贷款订单利息生成记录
     * 
     * @param ids 需要删除的贷款订单利息生成记录主键集合
     * @return 结果
     */
    public int deleteLoanOrderInterestRecordByIds(Long[] ids);

    /**
     * 删除贷款订单利息生成记录信息
     * 
     * @param id 贷款订单利息生成记录主键
     * @return 结果
     */
    public int deleteLoanOrderInterestRecordById(Long id);
}
