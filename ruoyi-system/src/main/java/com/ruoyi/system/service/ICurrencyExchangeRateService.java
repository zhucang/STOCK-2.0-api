package com.ruoyi.system.service;

import com.ruoyi.system.domain.CurrencyExchangeRate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 货币兑换汇率Service接口
 * 
 * @author ruoyi
 * @date 2023-11-25
 */
public interface ICurrencyExchangeRateService 
{
    /**
     * 查询货币兑换汇率
     * 
     * @param id 货币兑换汇率主键
     * @return 货币兑换汇率
     */
    public CurrencyExchangeRate selectCurrencyExchangeRateById(Long id);

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
     * 批量删除货币兑换汇率
     * 
     * @param ids 需要删除的货币兑换汇率主键集合
     * @return 结果
     */
    public int deleteCurrencyExchangeRateByIds(Long[] ids);

    /**
     * 重载缓存
     */
    public void reloadLangCurrencyExchangeRates();

    /**
     * 获取汇率详情 （汇率和手续费）
     * @param currencyIdFrom 转出币种id
     * @param currencyIdTo 转入币种id
     * @return
     */
    public Map<String, BigDecimal> getExchangeInfo(Long currencyIdFrom, Long currencyIdTo);
}
