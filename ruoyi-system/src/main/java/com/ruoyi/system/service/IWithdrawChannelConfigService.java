package com.ruoyi.system.service;

import com.ruoyi.system.domain.LangMgr;
import com.ruoyi.system.domain.WithdrawChannelConfig;

import java.util.List;

/**
 * 提现通道配置Service接口
 * 
 * @author ruoyi
 * @date 2023-11-04
 */
public interface IWithdrawChannelConfigService 
{
    /**
     * 查询提现通道配置
     * 
     * @param id 提现通道配置主键
     * @return 提现通道配置
     */
    public WithdrawChannelConfig selectWithdrawChannelConfigById(Long id);

    /**
     * 查询提现通道配置列表
     * 
     * @param withdrawChannelConfig 提现通道配置
     * @return 提现通道配置集合
     */
    public List<WithdrawChannelConfig> selectWithdrawChannelConfigList(WithdrawChannelConfig withdrawChannelConfig);

    /**
     * 新增提现通道配置
     * 
     * @param withdrawChannelConfig 提现通道配置
     * @return 结果
     */
    public int insertWithdrawChannelConfig(WithdrawChannelConfig withdrawChannelConfig);

    /**
     * 修改提现通道配置
     * 
     * @param withdrawChannelConfig 提现通道配置
     * @return 结果
     */
    public int updateWithdrawChannelConfig(WithdrawChannelConfig withdrawChannelConfig);

    /**
     * 修改提现通道名称多语言配置
     * @param withdrawChannelConfigId 提现通道id
     * @param channelNameLang 通道名称多语言
     * @return
     */
    public int updateChannelNameLang(Long withdrawChannelConfigId, LangMgr channelNameLang);

    /**
     * 批量删除提现通道配置
     * 
     * @param ids 需要删除的提现通道配置主键集合
     * @return 结果
     */
    public int deleteWithdrawChannelConfigByIds(Long[] ids);

    /**
     * 删除提现通道配置信息
     * 
     * @param id 提现通道配置主键
     * @return 结果
     */
    public int deleteWithdrawChannelConfigById(Long id);
}
