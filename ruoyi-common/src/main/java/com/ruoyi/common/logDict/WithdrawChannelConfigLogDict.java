package com.ruoyi.common.logDict;

import com.ruoyi.common.logDict.abstractDictMap.AbstractLogDictMap;

/**
 * 提现通道配置对象 withdraw_channel_config
 * 
 * @author ruoyi
 * @date 2023-11-04
 */
public class WithdrawChannelConfigLogDict extends AbstractLogDictMap
{

    @Override
    public void init() {
        this.put("id","通道配置ID");
        this.put("channelName","通道名称");
        this.put("withdrawMinAmount","提现最小金额");
        this.put("withdrawMaxAmount","提现最大金额");
        this.put("currencyId","币种ID");
        this.put("currencyName","币种名称");
        this.put("channelImg","通道图片");
        this.put("handingFeeMethod","手续费收取方式");
        this.put("handingFeeMethod","0","百分比手续费");
        this.put("handingFeeMethod","1","固定手续费");
        this.put("handingFeeFixedAmount","固定手续费金额");
        this.put("handingFeeRate","百分比手续费率");
        this.put("withdrawStartTime","每日提现开始时间");
        this.put("withdrawEndTime","每日提现结束时间");
        this.put("withdrawCount","每日可提现次数");
        this.put("status","状态");
        this.put("status","0","启用");
        this.put("status","1","禁用");
        this.put("sort","排序");
        this.put("withdrawType","提现类型");
        this.put("withdrawType","0","银行卡");
        this.put("withdrawType","1","虚拟钱包");
        this.put("withdrawType","2","客服");
        this.put("walletAddressType","钱包地址类型");
    }
}
