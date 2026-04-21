package com.ruoyi.common.logDict;

import com.ruoyi.common.logDict.abstractDictMap.AbstractLogDictMap;

/**
 * 充值通道配置对象 recharge_channel_config
 * 
 * @author ruoyi
 * @date 2023-11-04
 */
public class RechargeChannelConfigLogDict extends AbstractLogDictMap
{

    @Override
    public void init() {
        this.put("id","充值通道配置ID");
        this.put("channelName","通道名称");
        this.put("channelAccount","收款账号");
        this.put("walletReceiptCode","钱包收款码");
        this.put("channelImg","通道图标");
        this.put("bankName","银行名称");
        this.put("accountOpenBank","开户行");
        this.put("holder","持有人");
        this.put("channelMinLimit","最小入金");
        this.put("channelMaxLimit","最大入金");
        this.put("channelType","通道类型");
        this.put("channelType","0","加密货币");
        this.put("channelType","1","电汇");
        this.put("channelType","2","客服");
        this.put("currencyId","币种ID");
        this.put("currencyName","币种名称");
        this.put("sort","排序");
        this.put("quickAmounts","快捷充值金额");
        this.put("customerUrl","客服跳转链接");
        this.put("status","状态");
        this.put("status","0","启用");
        this.put("status","1","禁用");
    }
}
