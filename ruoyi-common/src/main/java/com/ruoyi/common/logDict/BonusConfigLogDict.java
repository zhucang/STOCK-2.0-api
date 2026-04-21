package com.ruoyi.common.logDict;

import com.ruoyi.common.logDict.abstractDictMap.AbstractLogDictMap;

/**
 * 赠送彩金配置对象 bonus_config
 * 
 * @author ruoyi
 * @date 2023-11-29
 */
public class BonusConfigLogDict extends AbstractLogDictMap
{

    @Override
    public void init() {
        this.put("id","赠送彩金配置ID");
        this.put("startTime","开始时间");
        this.put("endTime","结束时间");
        this.put("bonusAmount","赠送金额");
        this.put("currencyId","币种ID");
        this.put("currencyName","币种名称");
        this.put("bonusType","赠送类型");
        this.put("bonusType","0","注册彩金");
    }
}
