package com.ruoyi.common.logDict;

import com.ruoyi.common.logDict.abstractDictMap.AbstractLogDictMap;

/**
 * 用户流水记录对象 user_bill_detail
 * 
 * @author ruoyi
 * @date 2023-10-29
 */
public class UserBillDetailLogDict extends AbstractLogDictMap
{

    @Override
    public void init() {
        this.put("id", "用户钱包余额信息ID");
        this.put("orderAmount", "流水金额");
        this.put("deType", "标题");
        this.put("deSummary", "内容");
        this.put("amountBefore", "变更前总资金");
        this.put("amountAfter", "变更后总资金");
        this.put("frozenAmountBefore", "变更前冻结金额");
        this.put("frozenAmountAfter", "变更后冻结金额");
        this.put("relateOrderId", "相关订单ID");
        this.put("currencyId", "币种ID");
        this.put("currencyName", "币种名称");
    }
}
