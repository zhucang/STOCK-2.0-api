package com.ruoyi.common.logDict;

import com.ruoyi.common.logDict.abstractDictMap.AbstractLogDictMap;

/**
 * app配置对象 app_config
 * 
 * @author ruoyi
 * @date 2023-12-20
 */
public class AppConfigLogDict extends AbstractLogDictMap
{

    @Override
    public void init() {
        this.put("id","app配置ID");
        this.put("pagePath","页面路径");
        this.put("desc","介绍");
        this.put("module","模块");
        this.put("imgUrl","图片路径");
        this.put("hintTxt","提示文本");
        this.put("viewType","视图类型");
        this.put("viewType","0","图片");
        this.put("viewType","1","输入框");
        this.put("viewType","2","纯文本");
        this.put("viewType","3","按钮");
        this.put("isVisible","是否显示");
        this.put("isVisible","0","是");
        this.put("isVisible","1","否");
        this.put("isRequire","是否必填");
        this.put("isRequire","0","是");
        this.put("isRequire","1","否");
        this.put("isPassword","是否密码");
        this.put("isPassword","0","是");
        this.put("isPassword","1","否");
        this.put("patternType","规则类型");
        this.put("callFun","调用方法");
        this.put("objName","显示文本及输入用");
        this.put("objPropertyName","obj字段名");
        this.put("sort","排序");
        this.put("右边按键文本(可控制隐藏)","排序");
    }
}
