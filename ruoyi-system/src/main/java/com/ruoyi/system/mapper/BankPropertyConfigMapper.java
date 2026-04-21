package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.BankPropertyConfig;

import java.util.List;

/**
 * 银行卡参数字段配置Mapper接口
 * 
 * @author ruoyi
 * @date 2023-11-23
 */
public interface BankPropertyConfigMapper 
{
    /**
     * 查询银行卡参数字段配置
     * 
     * @param id 银行卡参数字段配置主键
     * @return 银行卡参数字段配置
     */
    public BankPropertyConfig selectBankPropertyConfigById(Long id);

    /**
     * 查询银行卡参数字段配置列表
     * 
     * @param bankPropertyConfig 银行卡参数字段配置
     * @return 银行卡参数字段配置集合
     */
    public List<BankPropertyConfig> selectBankPropertyConfigList(BankPropertyConfig bankPropertyConfig);

    /**
     * 新增银行卡参数字段配置
     * 
     * @param bankPropertyConfig 银行卡参数字段配置
     * @return 结果
     */
    public int insertBankPropertyConfig(BankPropertyConfig bankPropertyConfig);

    /**
     * 修改银行卡参数字段配置
     * 
     * @param bankPropertyConfig 银行卡参数字段配置
     * @return 结果
     */
    public int updateBankPropertyConfig(BankPropertyConfig bankPropertyConfig);

    /**
     * 删除银行卡参数字段配置
     * 
     * @param id 银行卡参数字段配置主键
     * @return 结果
     */
    public int deleteBankPropertyConfigById(Long id);

    /**
     * 批量删除银行卡参数字段配置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBankPropertyConfigByIds(Long[] ids);
}
