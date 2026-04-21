package com.ruoyi.common.logDict;

import com.ruoyi.common.logDict.abstractDictMap.AbstractLogDictMap;

/**
 * 极速交易用户单控参数对象 user_fast_trade_control
 * 
 * @author ruoyi
 * @date 2023-12-20
 */
public class UserFastTradeControlLogDict extends AbstractLogDictMap
{

    @Override
    public void init() {
        this.put("id","极速交易用户单控配置ID");
        this.put("controlType","控制类型");
        this.put("controlType","0","全部未控");
        this.put("controlType","1","全部控赢");
        this.put("controlType","2","全部控输");
        this.put("controlType","3","注额概率控（全部）");
        this.put("controlType","4","注额概率控（买涨）");
        this.put("controlType","5","注额概率控（买跌）");
        this.put("controlType","6","账户余额概率控（全部）");
        this.put("controlType","7","账户余额固定控（全部）");
        this.put("controlType","8","交易方向控（买多赢买空输）");
        this.put("controlType","9","交易方向控（买多输买空赢）");
        this.put("minLimitTradeAmount","最小界限注额");
        this.put("maxLimitTradeAmount","最大界限注额");
        this.put("winPercentWithinTradeAmount","注额界限范围之内赢率");
        this.put("winPercentOutsideTradeAmount","注额界限范围之外赢率");
        this.put("minLimitUserBalance","最小界限用户余额");
        this.put("maxLimitUserBalance","最大界限用户余额");
        this.put("winPercentWithinUserBalance","用户余额界限范围内赢率");
        this.put("winPercentOutsideUserBalance","用户余额界限范围之外赢率");
        this.put("accountType","0","会员");
        this.put("accountType","1","游客");

    }
}
