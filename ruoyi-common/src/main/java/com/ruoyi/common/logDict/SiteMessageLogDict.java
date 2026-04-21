package com.ruoyi.common.logDict;

import com.ruoyi.common.logDict.abstractDictMap.AbstractLogDictMap;

/**
 * 平台站内信发送记录对象 site_message
 * 
 * @author ruoyi
 * @date 2023-11-10
 */
public class SiteMessageLogDict extends AbstractLogDictMap
{

    @Override
    public void init() {
        this.put("id","平台站内信ID");
        this.put("msgTitle","信息标题");
        this.put("msgContent","信息内容");
        this.put("isVisible","是否显示");
        this.put("isVisible","0","是");
        this.put("isVisible","1","否");
        this.put("isPrivate","类型");
        this.put("isPrivate","0","用户通知");
        this.put("isPrivate","1","系统公告");
    }
}
