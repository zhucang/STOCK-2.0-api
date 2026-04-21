package com.ruoyi.common.logDict;

import com.ruoyi.common.logDict.abstractDictMap.AbstractLogDictMap;

/**
 * 用户加密货币持仓对象 user_cryptocurrency_position
 * 
 * @author ruoyi
 * @date 2023-11-05
 */
public class UserForexPositionLogDict extends AbstractLogDictMap
{

    @Override
    public void init() {
        this.put("id","持仓订单ID");
        this.put("userNo","用户编号");
        this.put("orderCode","持仓订单号");
        this.put("productCode","产品代码");
        this.put("buyOrderPrice","购买价格");
        this.put("sellOrderPrice","卖出价格");
        this.put("orderDirection","下单方向");
        this.put("orderDirection","0","买涨");
        this.put("orderDirection","1","买跌");
        this.put("orderNum","订单数量");
        this.put("orderLever","杠杆倍数");
        this.put("orderFee","手续费");
        this.put("orderYhsFee","印花税");
        this.put("profitAndLose","浮动盈亏");
        this.put("allProfitAndLose","总盈亏");
        this.put("currencyId","币种ID");
        this.put("currencyName","币种名称");
        this.put("stopProfitPrice","止盈线");
        this.put("stopLossPrice","止损线");
        this.put("doType","平仓类型");
        this.put("doType","0","强制平仓");
        this.put("doType","1","用户平仓");
        this.put("marginAmount","保证金");
    }
}
