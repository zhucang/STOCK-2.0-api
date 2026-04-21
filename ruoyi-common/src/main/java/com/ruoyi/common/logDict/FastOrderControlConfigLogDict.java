package com.ruoyi.common.logDict;

import com.ruoyi.common.logDict.abstractDictMap.AbstractLogDictMap;

/**
 * 极速交易控制配置对象 fast_order_control_config
 * 
 * @author ruoyi
 * @date 2023-11-28
 */
public class FastOrderControlConfigLogDict extends AbstractLogDictMap
{

    @Override
    public void init() {
        this.put("id","极速交易控制配置ID");
        this.put("productCode","产品代码");
        this.put("productType","产品类型");
        this.put("productType","1","股票");
        this.put("productType","2","加密货币");
        this.put("productType","3","期货");
        this.put("productType","4","外汇");
        this.put("beginTime","开始时间");
        this.put("finishTime","结束时间");
        this.put("tradeDirect","交易方向");
        this.put("tradeDirect","0","做多");
        this.put("tradeDirect","1","做空");
        this.put("status","状态");
        this.put("status","0","启用");
        this.put("status","1","禁用");
        this.put("winOrLose","状态");
        this.put("winOrLose","0","输");
        this.put("winOrLose","1","赢");
        this.put("winOrLose","2","平");
        this.put("winOrLose","3","未控");
    }
}
