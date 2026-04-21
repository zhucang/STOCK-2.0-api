package com.ruoyi.common.logDict;

import com.ruoyi.common.logDict.abstractDictMap.AbstractLogDictMap;

/**
 * 资金互转记录对象 balance_convert_record
 *
 * @author ruoyi
 * @date 2023-10-30
 */
public class BalanceConvertRecordLogDict extends AbstractLogDictMap
{

    @Override
    public void init() {
        this.put("id","资金互转记录ID");
        this.put("userNo","用户编号");
        this.put("currencyNameFrom","转出币种名称");
        this.put("currencyIdFrom","转出币种ID");
        this.put("transAmount","转出金额");
        this.put("currencyNameTo","转入币种名称");
        this.put("currencyIdTo","转入币种ID");
        this.put("convertedAmount","转入金额");
        this.put("exchangeRate","兑换汇率");
        this.put("feeRatio","手续费率");
        this.put("handingFee","手续费");
    }
}
