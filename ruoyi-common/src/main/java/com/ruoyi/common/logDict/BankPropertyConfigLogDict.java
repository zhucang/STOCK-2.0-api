package com.ruoyi.common.logDict;

import com.ruoyi.common.logDict.abstractDictMap.AbstractLogDictMap;

/**
 * 银行卡参数字段配置对象 bank_property_config
 * 
 * @author ruoyi
 * @date 2023-11-23
 */
public class BankPropertyConfigLogDict extends AbstractLogDictMap
{

    @Override
    public void init() {
        this.put("id","银行卡参数字段配置ID");
        this.put("propertyName","字段名称");
        this.put("propertyDesc","字段描述");
        this.put("langKey","多语言key");
        this.put("isVisible","是否展示");
        this.put("isVisible","0","展示");
        this.put("isVisible","1","不展示");
        this.put("isRequire","是否展示");
        this.put("isRequire","0","是");
        this.put("isRequire","1","否");
        this.put("sort","排序");
    }
}
