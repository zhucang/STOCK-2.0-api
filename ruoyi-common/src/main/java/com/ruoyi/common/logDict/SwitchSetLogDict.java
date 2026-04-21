package com.ruoyi.common.logDict;

import com.ruoyi.common.logDict.abstractDictMap.AbstractLogDictMap;

/**
 * 开关配置对象 switch_set
 * 
 * @author ruoyi
 * @date 2023-10-28
 */
public class SwitchSetLogDict extends AbstractLogDictMap
{

    @Override
    public void init() {
        this.put("id","开关ID");
        this.put("switchName","开关名称");
        this.put("status","状态");
        this.put("status","0","开启");
        this.put("status","1","关闭");
        this.put("type","状态");
        this.put("type","0","登陆注册开关");
        this.put("type","1","其他开关");
        this.put("type","2","交易方式开关");
    }
}
