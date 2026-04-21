package com.ruoyi.common.logDict;

import com.ruoyi.common.logDict.abstractDictMap.AbstractLogDictMap;

/**
 * 现货交易订单对象 spot_trade_order
 * 
 * @author ruoyi
 * @date 2023-11-05
 */
public class SpotTradeOrderLogDict extends AbstractLogDictMap
{

    @Override
    public void init() {
        this.put("id","现货订单ID");
        this.put("userNo","用户编号");
        this.put("orderCode","现货订单号");
        this.put("productType","产品类型");
        this.put("productType","1","股票");
        this.put("productType","2","加密货币");
        this.put("productType","3","期货");
        this.put("productType","4","外汇");
        this.put("productCode","产品代码");
        this.put("buyOrderPrice","购买价格");
        this.put("sellOrderPrice","卖出价格");
        this.put("orderNum","订单数量");
        this.put("orderFee","手续费");
        this.put("profitAndLose","浮动盈亏");
        this.put("allProfitAndLose","总盈亏");
        this.put("currencyId","币种ID");
        this.put("currencyName","币种名称");
        this.put("doType","平仓类型");
        this.put("doType","0","强制平仓");
        this.put("doType","1","用户平仓");
    }
}
