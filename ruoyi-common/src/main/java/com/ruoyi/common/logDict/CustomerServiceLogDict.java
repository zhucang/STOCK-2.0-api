package com.ruoyi.common.logDict;

import com.ruoyi.common.logDict.abstractDictMap.AbstractLogDictMap;

/**
 * 客服配置对象 customer_service
 * 
 * @author ruoyi
 * @date 2023-11-04
 */
public class CustomerServiceLogDict extends AbstractLogDictMap
{

    @Override
    public void init() {
        this.put("id","客服配置ID");
        this.put("customerServiceName","客服名称");
        this.put("customerServiceLine","客服链接");
        this.put("status","状态");
        this.put("status","0","启用");
        this.put("status","1","禁用");
        this.put("isJump","是否跳转");
        this.put("isJump","0","跳转");
        this.put("isJump","1","不跳转");
        this.put("webOrOnline","客服类型");
        this.put("webOrOnline","0","网页客服");
        this.put("webOrOnline","1","在线客服");
        this.put("webOrOnline","2","Telegram");
        this.put("webOrOnline","3","Whatsapp");
    }
}
