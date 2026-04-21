package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.constant.CacheableKey;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.domain.BankPropertyConfig;
import com.ruoyi.system.mapper.BankPropertyConfigMapper;
import com.ruoyi.system.service.IBankPropertyConfigService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * 银行卡参数字段配置Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-11-23
 */
@Service
public class BankPropertyConfigServiceImpl implements IBankPropertyConfigService 
{
    @Resource
    private BankPropertyConfigMapper bankPropertyConfigMapper;

    /**
     * 查询银行卡参数字段配置
     * 
     * @param id 银行卡参数字段配置主键
     * @return 银行卡参数字段配置
     */
    @Override
//    @Cacheable(value = CacheableKey.BANK_PROPERTY_CONFIG + CacheableKey.ENTITY,key = "#id")
    public BankPropertyConfig selectBankPropertyConfigById(Long id)
    {
        return bankPropertyConfigMapper.selectBankPropertyConfigById(id);
    }

    /**
     * 查询银行卡参数字段配置列表
     * 
     * @param bankPropertyConfig 银行卡参数字段配置
     * @return 银行卡参数字段配置
     */
    @Override
    @Cacheable(value = CacheableKey.BANK_PROPERTY_CONFIG + CacheableKey.LIST,key = "#bankPropertyConfig.cacheableKey()")
    public List<BankPropertyConfig> selectBankPropertyConfigList(BankPropertyConfig bankPropertyConfig)
    {
        return bankPropertyConfigMapper.selectBankPropertyConfigList(bankPropertyConfig);
    }

    /**
     * 新增银行卡参数字段配置
     * 
     * @param bankPropertyConfig 银行卡参数字段配置
     * @return 结果
     */
    @Override
    @CacheEvict(value = CacheableKey.BANK_PROPERTY_CONFIG + CacheableKey.LIST,allEntries = true)
    public int insertBankPropertyConfig(BankPropertyConfig bankPropertyConfig)
    {
        return bankPropertyConfigMapper.insertBankPropertyConfig(bankPropertyConfig);
    }

    /**
     * 修改银行卡参数字段配置
     * 
     * @param bankPropertyConfig 银行卡参数字段配置
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.BANK_PROPERTY_CONFIG + CacheableKey.ENTITY,key = "#bankPropertyConfig.id"),
            @CacheEvict(value = CacheableKey.BANK_PROPERTY_CONFIG + CacheableKey.LIST,allEntries = true)})
    public int updateBankPropertyConfig(BankPropertyConfig bankPropertyConfig)
    {
        return bankPropertyConfigMapper.updateBankPropertyConfig(bankPropertyConfig);
    }

    /**
     * 批量删除银行卡参数字段配置
     * 
     * @param ids 需要删除的银行卡参数字段配置主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.BANK_PROPERTY_CONFIG + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.BANK_PROPERTY_CONFIG + CacheableKey.LIST,allEntries = true)})
    public int deleteBankPropertyConfigByIds(Long[] ids)
    {
        BankPropertyConfig search = new BankPropertyConfig();
        search.getParams().put("ids", Arrays.asList(ids));
        List<BankPropertyConfig> bankPropertyConfigs = bankPropertyConfigMapper.selectBankPropertyConfigList(search);
        //日志记录银行卡参数字段配置
        HttpUtils.getRequestLogParams().put("JSONArray:bankPropertyConfigs", JSONObject.toJSONString(bankPropertyConfigs));
        return bankPropertyConfigMapper.deleteBankPropertyConfigByIds(ids);
    }

    /**
     * 删除银行卡参数字段配置信息
     * 
     * @param id 银行卡参数字段配置主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.BANK_PROPERTY_CONFIG + CacheableKey.ENTITY,key = "#id"),
            @CacheEvict(value = CacheableKey.BANK_PROPERTY_CONFIG + CacheableKey.LIST,allEntries = true)})
    public int deleteBankPropertyConfigById(Long id)
    {
        return bankPropertyConfigMapper.deleteBankPropertyConfigById(id);
    }
}
