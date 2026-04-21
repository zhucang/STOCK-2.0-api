package com.ruoyi.common.logDict;

import com.ruoyi.common.logDict.abstractDictMap.AbstractLogDictMap;

/**
 * 弹窗信息对象 pop_up
 * 
 * @author ruoyi
 * @date 2024-03-21
 */
public class PopUpLogDict extends AbstractLogDictMap
{

    @Override
    public void init() {
        this.put("id","弹窗配置ID");
        this.put("popUpTitle","弹窗标题");
        this.put("popUpContent","弹窗内容");
        this.put("showTime","展示时间");
    }
}
