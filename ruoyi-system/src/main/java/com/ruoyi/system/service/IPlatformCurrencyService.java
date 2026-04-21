package com.ruoyi.system.service;

import com.ruoyi.system.domain.LangMgr;
import com.ruoyi.system.domain.PlatformCurrency;

import java.util.List;

/**
 * 平台交易币种配置信息Service接口
 * 
 * @author ruoyi
 * @date 2023-10-28
 */
public interface IPlatformCurrencyService 
{
    /**
     * 查询平台交易币种配置信息
     * 
     * @param id 平台交易币种配置信息主键
     * @return 平台交易币种配置信息
     */
    public PlatformCurrency selectPlatformCurrencyById(Long id);

    /**
     * 查询平台交易币种配置信息列表
     * 
     * @param platformCurrency 平台交易币种配置信息
     * @return 平台交易币种配置信息集合
     */
    public List<PlatformCurrency> selectPlatformCurrencyList(PlatformCurrency platformCurrency);

    /**
     * 新增平台交易币种配置信息
     * 
     * @param platformCurrency 平台交易币种配置信息
     * @return 结果
     */
    public int insertPlatformCurrency(PlatformCurrency platformCurrency);

    /**
     * 修改平台交易币种配置信息
     * 
     * @param platformCurrency 平台交易币种配置信息
     * @return 结果
     */
    public int updatePlatformCurrency(PlatformCurrency platformCurrency);

    /**
     * 修改币种名称多语言
     * @param platformCurrencyId 币种配置id
     * @param currencyNameLang 币种名称语言包
     * @return
     */
    public int updateCurrencyNameLang(Long platformCurrencyId, LangMgr currencyNameLang);

    /**
     * 修改币种描述多语言
     * @param platformCurrencyId 币种配置id
     * @param currencyDescLang 币种描述语言包
     * @return
     */
    public int updateCurrencyDescLang(Long platformCurrencyId, LangMgr currencyDescLang);

    /**
     * 批量删除平台交易币种配置信息
     * 
     * @param ids 需要删除的平台交易币种配置信息主键集合
     * @return 结果
     */
    public int deletePlatformCurrencyByIds(Long[] ids);

    /**
     * 删除平台交易币种配置信息信息
     * 
     * @param id 平台交易币种配置信息主键
     * @return 结果
     */
    public int deletePlatformCurrencyById(Long id);
}
