package com.ruoyi.common.logDict;

import com.ruoyi.common.logDict.abstractDictMap.AbstractLogDictMap;

/**
 * 第三方充值通道配置对象 third_party_recharge_channel
 * 
 * @author ruoyi
 * @date 2023-11-23
 */
public class ThirdPartyRechargeChannelLogDict extends AbstractLogDictMap
{

    @Override
    public void init() {
        this.put("id","第三方充值通道配置ID");
        this.put("channelName","通道名称");
        this.put("channelImg","通道图标");
        this.put("jumpUrl","跳转链接");
        this.put("sort","排序");
    }
}
