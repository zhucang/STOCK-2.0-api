package com.ruoyi.system.service;

import com.ruoyi.system.domain.LangMgr;
import com.ruoyi.system.domain.RechargeChannelConfig;

import java.util.List;

/**
 * 充值通道配置Service接口
 * 
 * @author ruoyi
 * @date 2023-11-04
 */
public interface IRechargeChannelConfigService 
{
    /**
     * 查询充值通道配置
     * 
     * @param id 充值通道配置主键
     * @return 充值通道配置
     */
    public RechargeChannelConfig selectRechargeChannelConfigById(Long id);

    /**
     * 查询充值通道配置列表
     * 
     * @param rechargeChannelConfig 充值通道配置
     * @return 充值通道配置集合
     */
    public List<RechargeChannelConfig> selectRechargeChannelConfigList(RechargeChannelConfig rechargeChannelConfig);

    /**
     * 新增充值通道配置
     * 
     * @param rechargeChannelConfig 充值通道配置
     * @return 结果
     */
    public int insertRechargeChannelConfig(RechargeChannelConfig rechargeChannelConfig);

    /**
     * 修改充值通道配置
     * 
     * @param rechargeChannelConfig 充值通道配置
     * @return 结果
     */
    public int updateRechargeChannelConfig(RechargeChannelConfig rechargeChannelConfig);

    /**
     * 修改充值通道名称多语言配置
     * @param rechargeChannelConfigId 提现通道id
     * @param channelNameLang 通道名称多语言
     * @return
     */
    public int updateChannelNameLang(Long rechargeChannelConfigId, LangMgr channelNameLang);

    /**
     * 批量删除充值通道配置
     * 
     * @param ids 需要删除的充值通道配置主键集合
     * @return 结果
     */
    public int deleteRechargeChannelConfigByIds(Long[] ids);

    /**
     * 删除充值通道配置信息
     * 
     * @param id 充值通道配置主键
     * @return 结果
     */
    public int deleteRechargeChannelConfigById(Long id);
}
