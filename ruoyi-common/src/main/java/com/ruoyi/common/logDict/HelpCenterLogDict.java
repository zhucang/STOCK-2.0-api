package com.ruoyi.common.logDict;

import com.ruoyi.common.logDict.abstractDictMap.AbstractLogDictMap;

/**
 * 帮助中心对象 help_center
 * 
 * @author ruoyi
 * @date 2023-11-22
 */
public class HelpCenterLogDict extends AbstractLogDictMap
{

    @Override
    public void init() {
        this.put("id","帮助中心ID");
        this.put("question","问题");
        this.put("answer","答案");
    }
}
