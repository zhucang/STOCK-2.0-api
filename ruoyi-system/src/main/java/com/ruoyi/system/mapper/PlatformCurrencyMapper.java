package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.PlatformCurrency;

import java.util.List;

/**
 * 平台交易币种配置信息Mapper接口
 * 
 * @author ruoyi
 * @date 2023-10-28
 */
public interface PlatformCurrencyMapper 
{
    /**
     * 查询平台交易币种配置信息
     * 
     * @param id 平台交易币种配置信息主键
     * @return 平台交易币种配置信息
     */
    public PlatformCurrency selectPlatformCurrencyById(Long id);

    /**
     * 查询平台可用货币币种
     *
     * @param name 货币名称
     * @return 平台可用货币币种
     */
    public PlatformCurrency selectPlatformCurrencyByName(String name);

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
     * 删除平台交易币种配置信息
     * 
     * @param id 平台交易币种配置信息主键
     * @return 结果
     */
    public int deletePlatformCurrencyById(Long id);

    /**
     * 批量删除平台交易币种配置信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePlatformCurrencyByIds(Long[] ids);
}
