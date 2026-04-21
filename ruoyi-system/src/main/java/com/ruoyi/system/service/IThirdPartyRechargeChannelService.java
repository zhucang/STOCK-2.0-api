package com.ruoyi.system.service;

import com.ruoyi.system.domain.LangMgr;
import com.ruoyi.system.domain.ThirdPartyRechargeChannel;

import java.util.List;

/**
 * 第三方充值通道配置Service接口
 * 
 * @author ruoyi
 * @date 2023-11-23
 */
public interface IThirdPartyRechargeChannelService 
{
    /**
     * 查询第三方充值通道配置
     * 
     * @param id 第三方充值通道配置主键
     * @return 第三方充值通道配置
     */
    public ThirdPartyRechargeChannel selectThirdPartyRechargeChannelById(Long id);

    /**
     * 查询第三方充值通道配置列表
     * 
     * @param thirdPartyRechargeChannel 第三方充值通道配置
     * @return 第三方充值通道配置集合
     */
    public List<ThirdPartyRechargeChannel> selectThirdPartyRechargeChannelList(ThirdPartyRechargeChannel thirdPartyRechargeChannel);

    /**
     * 新增第三方充值通道配置
     * 
     * @param thirdPartyRechargeChannel 第三方充值通道配置
     * @return 结果
     */
    public int insertThirdPartyRechargeChannel(ThirdPartyRechargeChannel thirdPartyRechargeChannel);

    /**
     * 修改第三方充值通道配置
     * 
     * @param thirdPartyRechargeChannel 第三方充值通道配置
     * @return 结果
     */
    public int updateThirdPartyRechargeChannel(ThirdPartyRechargeChannel thirdPartyRechargeChannel);

    /**
     * 修改第三方充值通道名称多语言
     * @param thirdPartyRechargeChannelId 第三方充值通道id
     * @param channelNameLang 通道名称多语言
     * @return
     */
    public int updateChannelNameLang(Long thirdPartyRechargeChannelId, LangMgr channelNameLang);

    /**
     * 批量删除第三方充值通道配置
     * 
     * @param ids 需要删除的第三方充值通道配置主键集合
     * @return 结果
     */
    public int deleteThirdPartyRechargeChannelByIds(Long[] ids);

    /**
     * 删除第三方充值通道配置信息
     * 
     * @param id 第三方充值通道配置主键
     * @return 结果
     */
    public int deleteThirdPartyRechargeChannelById(Long id);
}
