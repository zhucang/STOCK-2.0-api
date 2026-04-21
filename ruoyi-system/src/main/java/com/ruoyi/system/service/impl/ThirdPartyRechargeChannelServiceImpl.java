package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.constant.CacheableKey;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.domain.LangMgr;
import com.ruoyi.system.domain.ThirdPartyRechargeChannel;
import com.ruoyi.system.mapper.ThirdPartyRechargeChannelMapper;
import com.ruoyi.system.service.IThirdPartyRechargeChannelService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * 第三方充值通道配置Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-11-23
 */
@Service
public class ThirdPartyRechargeChannelServiceImpl implements IThirdPartyRechargeChannelService 
{
    @Resource
    private ThirdPartyRechargeChannelMapper thirdPartyRechargeChannelMapper;

    /**
     * 查询第三方充值通道配置
     * 
     * @param id 第三方充值通道配置主键
     * @return 第三方充值通道配置
     */
    @Override
//    @Cacheable(value = CacheableKey.THIRD_PARTY_RECHARGE_CHANNEL + CacheableKey.ENTITY,key = "#id")
    public ThirdPartyRechargeChannel selectThirdPartyRechargeChannelById(Long id)
    {
        return thirdPartyRechargeChannelMapper.selectThirdPartyRechargeChannelById(id);
    }

    /**
     * 查询第三方充值通道配置列表
     * 
     * @param thirdPartyRechargeChannel 第三方充值通道配置
     * @return 第三方充值通道配置
     */
    @Override
    @Cacheable(value = CacheableKey.THIRD_PARTY_RECHARGE_CHANNEL + CacheableKey.LIST,key = "#thirdPartyRechargeChannel.cacheableKey()")
    public List<ThirdPartyRechargeChannel> selectThirdPartyRechargeChannelList(ThirdPartyRechargeChannel thirdPartyRechargeChannel)
    {
        return thirdPartyRechargeChannelMapper.selectThirdPartyRechargeChannelList(thirdPartyRechargeChannel);
    }

    /**
     * 新增第三方充值通道配置
     * 
     * @param thirdPartyRechargeChannel 第三方充值通道配置
     * @return 结果
     */
    @Override
    @CacheEvict(value = CacheableKey.THIRD_PARTY_RECHARGE_CHANNEL + CacheableKey.LIST,allEntries = true)
    public int insertThirdPartyRechargeChannel(ThirdPartyRechargeChannel thirdPartyRechargeChannel)
    {
        return thirdPartyRechargeChannelMapper.insertThirdPartyRechargeChannel(thirdPartyRechargeChannel);
    }

    /**
     * 修改第三方充值通道配置
     * 
     * @param thirdPartyRechargeChannel 第三方充值通道配置
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.THIRD_PARTY_RECHARGE_CHANNEL + CacheableKey.ENTITY,key = "#thirdPartyRechargeChannel.id"),
            @CacheEvict(value = CacheableKey.THIRD_PARTY_RECHARGE_CHANNEL + CacheableKey.LIST,allEntries = true)})
    public int updateThirdPartyRechargeChannel(ThirdPartyRechargeChannel thirdPartyRechargeChannel)
    {
        return thirdPartyRechargeChannelMapper.updateThirdPartyRechargeChannel(thirdPartyRechargeChannel);
    }

    /**
     * 修改第三方充值通道名称多语言
     * @param thirdPartyRechargeChannelId 第三方充值通道id
     * @param channelNameLang 通道名称多语言
     * @return
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.THIRD_PARTY_RECHARGE_CHANNEL + CacheableKey.ENTITY,key = "#thirdPartyRechargeChannelId"),
            @CacheEvict(value = CacheableKey.THIRD_PARTY_RECHARGE_CHANNEL + CacheableKey.LIST,allEntries = true)})
    public int updateChannelNameLang(Long thirdPartyRechargeChannelId, LangMgr channelNameLang){
        ThirdPartyRechargeChannel thirdPartyRechargeChannel = new ThirdPartyRechargeChannel();
        thirdPartyRechargeChannel.setId(thirdPartyRechargeChannelId);
        thirdPartyRechargeChannel.setChannelNameLang(channelNameLang);
        return thirdPartyRechargeChannelMapper.updateThirdPartyRechargeChannel(thirdPartyRechargeChannel);
    }

    /**
     * 批量删除第三方充值通道配置
     * 
     * @param ids 需要删除的第三方充值通道配置主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.THIRD_PARTY_RECHARGE_CHANNEL + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.THIRD_PARTY_RECHARGE_CHANNEL + CacheableKey.LIST,allEntries = true)})
    public int deleteThirdPartyRechargeChannelByIds(Long[] ids)
    {
        ThirdPartyRechargeChannel search = new ThirdPartyRechargeChannel();
        search.getParams().put("ids", Arrays.asList(ids));
        List<ThirdPartyRechargeChannel> thirdPartyRechargeChannels = thirdPartyRechargeChannelMapper.selectThirdPartyRechargeChannelList(search);
        //日志记录第三方充值通道配置信息
        HttpUtils.getRequestLogParams().put("JSONArray:thirdPartyRechargeChannels", JSONObject.toJSONString(thirdPartyRechargeChannels));
        return thirdPartyRechargeChannelMapper.deleteThirdPartyRechargeChannelByIds(ids);
    }

    /**
     * 删除第三方充值通道配置信息
     * 
     * @param id 第三方充值通道配置主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.THIRD_PARTY_RECHARGE_CHANNEL + CacheableKey.ENTITY,key = "#id"),
            @CacheEvict(value = CacheableKey.THIRD_PARTY_RECHARGE_CHANNEL + CacheableKey.LIST,allEntries = true)})
    public int deleteThirdPartyRechargeChannelById(Long id)
    {
        return thirdPartyRechargeChannelMapper.deleteThirdPartyRechargeChannelById(id);
    }
}
