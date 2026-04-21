package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.CurrencyExchangeRate;

import java.util.List;

/**
 * 货币兑换汇率Mapper接口
 * 
 * @author ruoyi
 * @date 2023-11-25
 */
public interface CurrencyExchangeRateMapper 
{
    /**
     * 查询货币兑换汇率
     * 
     * @param id 货币兑换汇率主键
     * @return 货币兑换汇率
     */
    public CurrencyExchangeRate selectCurrencyExchangeRateById(Long id);

    /**
     * 查询货币兑换汇率
     *
     * @param currencyExchangeRate 货币兑换汇率
     * @return 货币兑换汇率
     */
    public CurrencyExchangeRate selectCurrencyExchangeRate(CurrencyExchangeRate currencyExchangeRate);

    /**
     * 查询货币兑换汇率列表
     * 
     * @param currencyExchangeRate 货币兑换汇率
     * @return 货币兑换汇率集合
     */
    public List<CurrencyExchangeRate> selectCurrencyExchangeRateList(CurrencyExchangeRate currencyExchangeRate);

    /**
     * 新增货币兑换汇率
     * 
     * @param currencyExchangeRate 货币兑换汇率
     * @return 结果
     */
    public int insertCurrencyExchangeRate(CurrencyExchangeRate currencyExchangeRate);

    /**
     * 修改货币兑换汇率
     * 
     * @param currencyExchangeRate 货币兑换汇率
     * @return 结果
     */
    public int updateCurrencyExchangeRate(CurrencyExchangeRate currencyExchangeRate);

    /**
     * 删除货币兑换汇率
     * 
     * @param id 货币兑换汇率主键
     * @return 结果
     */
    public int deleteCurrencyExchangeRateById(Long id);

    /**
     * 批量删除货币兑换汇率
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCurrencyExchangeRateByIds(Long[] ids);
}
