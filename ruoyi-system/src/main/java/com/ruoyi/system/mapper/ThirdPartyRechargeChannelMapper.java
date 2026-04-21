package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.ThirdPartyRechargeChannel;

import java.util.List;

/**
 * 第三方充值通道配置Mapper接口
 * 
 * @author ruoyi
 * @date 2023-11-23
 */
public interface ThirdPartyRechargeChannelMapper 
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
     * 删除第三方充值通道配置
     * 
     * @param id 第三方充值通道配置主键
     * @return 结果
     */
    public int deleteThirdPartyRechargeChannelById(Long id);

    /**
     * 批量删除第三方充值通道配置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteThirdPartyRechargeChannelByIds(Long[] ids);
}
