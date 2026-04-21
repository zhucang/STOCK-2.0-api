package com.ruoyi.common.logDict;

import com.ruoyi.common.logDict.abstractDictMap.AbstractLogDictMap;

/**
 * 提现记录对象 user_withdraw
 * 
 * @author ruoyi
 * @date 2023-10-30
 */
public class UserWithdrawLogDict extends AbstractLogDictMap
{

    @Override
    public void init() {
        this.put("userNo","用户编号");
        this.put("id","提现订单ID");
        this.put("orderCode","提现订单号");
        this.put("withPwd","提现密码");
        this.put("withdrawAddressId","提现钱包id或银行卡ID");
        this.put("withdrawChannelConfigId","提现通道ID");
        this.put("withdrawAmount","提现金额");
        this.put("receivedAmount","到账金额");
        this.put("applyTime","申请提现时间");
        this.put("transTime","审核时间");
        this.put("operatorName","操作人名称");
        this.put("receiptAccountInfo","收款账户信息");
        this.put("walletReceiptQrCode","钱包收款码");
        this.put("withdrawStatus","提现状态");
        this.put("withdrawStatus","0","审核中");
        this.put("withdrawStatus","1","审核通过");
        this.put("withdrawStatus","2","审核驳回");
        this.put("withdrawStatus","3","取消提现");
        this.put("withdrawFee","提现手续费");
        this.put("withdrawMsg","驳回信息");
        this.put("currencyId","币种ID");
        this.put("currencyName","币种名称");
        this.put("withdrawType","提现类型");
        this.put("withdrawType","0","银行卡");
        this.put("withdrawType","1","虚拟钱包");
        this.put("userAmtBefore","余额变更前");
        this.put("userAmtAfter","余额变更后");
        this.put("statisticalReport","是否统计报表");
        this.put("statisticalReport","0","是");
        this.put("statisticalReport","1","否");
        this.put("channelName","提现通道名称");
    }
}
