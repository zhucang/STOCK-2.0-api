package com.ruoyi.common.logDict;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.logDict.abstractDictMap.AbstractLogDictMap;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 产品短线行情控制对象 product_quote_control
 * 
 * @author ruoyi
 * @date 2024-01-11
 */
public class ProductQuoteControlLogDict extends AbstractLogDictMap
{

    @Override
    public void init() {
        this.put("id","产品短线行情控制配置ID");
        this.put("productCode","产品代码");
        this.put("presetPrice","预设点位");
        this.put("presetStartTime","值");
        this.put("presetStartTime","启动时间");
        this.put("presetDuration","预设时长");
        this.put("returnDuration","回归时长");
        this.put("arrivePresetPriceTime","到达预设点位时间");
        this.put("presetEndTime","预设结束时间");
        this.put("startPrice","启动点位");
        this.put("startDelayTime","启动延时");
        this.put("status","状态");
        this.put("status","0","已预设");
        this.put("status","1","已启动");
    }
}
