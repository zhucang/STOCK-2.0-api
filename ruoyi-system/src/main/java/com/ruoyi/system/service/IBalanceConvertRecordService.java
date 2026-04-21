package com.ruoyi.system.service;

import com.ruoyi.system.domain.BalanceConvertRecord;

import java.math.BigDecimal;
import java.util.List;

/**
 * 资金互转记录Service接口
 * 
 * @author ruoyi
 * @date 2023-12-01
 */
public interface IBalanceConvertRecordService 
{
    /**
     * 查询资金互转记录列表
     * 
     * @param balanceConvertRecord 资金互转记录
     * @return 资金互转记录集合
     */
    public List<BalanceConvertRecord> selectBalanceConvertRecordList(BalanceConvertRecord balanceConvertRecord);

    /**
     * 资金互转
     * @param userId 用户id
     * @param transAmount 转出金额
     * @param currencyIdFrom 转出币种id
     * @param currencyIdTo 转入币种id
     * @return
     */
    public int balanceConvert(Long userId,BigDecimal transAmount, Long currencyIdFrom, Long currencyIdTo);

}
