package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.BalanceConvertRecord;

import java.util.List;

/**
 * 资金互转记录Mapper接口
 * 
 * @author ruoyi
 * @date 2023-12-01
 */
public interface BalanceConvertRecordMapper 
{
    /**
     * 查询资金互转记录
     * 
     * @param id 资金互转记录主键
     * @return 资金互转记录
     */
    public BalanceConvertRecord selectBalanceConvertRecordById(Long id);

    /**
     * 查询资金互转记录列表
     * 
     * @param balanceConvertRecord 资金互转记录
     * @return 资金互转记录集合
     */
    public List<BalanceConvertRecord> selectBalanceConvertRecordList(BalanceConvertRecord balanceConvertRecord);

    /**
     * 新增资金互转记录
     * 
     * @param balanceConvertRecord 资金互转记录
     * @return 结果
     */
    public int insertBalanceConvertRecord(BalanceConvertRecord balanceConvertRecord);

    /**
     * 修改资金互转记录
     * 
     * @param balanceConvertRecord 资金互转记录
     * @return 结果
     */
    public int updateBalanceConvertRecord(BalanceConvertRecord balanceConvertRecord);

    /**
     * 删除资金互转记录
     * 
     * @param id 资金互转记录主键
     * @return 结果
     */
    public int deleteBalanceConvertRecordById(Long id);

    /**
     * 批量删除资金互转记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBalanceConvertRecordByIds(Long[] ids);

    /**
     * 获取今日资金互转次数
     * @param userId 用户id
     * @return
     */
    public int getBalanceConvertCountTodayByUserId(Long userId);
}
