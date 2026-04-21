package com.ruoyi.common.logDict;

import com.ruoyi.common.logDict.abstractDictMap.AbstractLogDictMap;

/**
 * 其他值对象 other_value
 * 
 * @author ruoyi
 * @date 2024-03-27
 */
public class OtherValueLogDict extends AbstractLogDictMap
{

    @Override
    public void init() {
        this.put("id","其他值配置ID");
        this.put("otherKey","key");
        this.put("name","名称");
        this.put("otherValue","值");
    }
}
