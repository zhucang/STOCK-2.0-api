package com.ruoyi.common.logDict.abstractDictMap;

import java.util.HashMap;
import java.util.Map;

/**
 * 字典映射抽象类
 *
 * @author fengshuonan
 * @date 2017-05-06 14:58
 */
public abstract class AbstractLogDictMap {

    //参数名称字典map
    protected Map<String, String> paramsNameLogDictMap = new HashMap<>();

    //参数值字典map(例如:性别为1:男,2:女,需要被包装为汉字)
    protected Map<String, Map<String,String>> paramValueLogDictMap = new HashMap<>();

    public AbstractLogDictMap(){
        put("id","id");
        put("createTime","创建时间");
        put("createBy","创建人");
        put("updateTime","操作时间");
        put("updateBy","操作人");
        put("remark","备注");
        init();
    }

    /**
     * 初始化字段英文名称和中文名称对应的字典
     *
     * @author stylefeng
     * @Date 2017/5/9 19:39
     */
    public abstract void init();


    public String get(String key) {
        return this.paramsNameLogDictMap.get(key);
    }

    public void put(String key, String value) {
        this.paramsNameLogDictMap.put(key, value);
    }

    public String get(String key,String hKey){
        try {
            return this.paramValueLogDictMap.get(key).get(hKey);
        }catch (Exception e){
            return null;
        }
    }

    public void put(String key,String hKey,String value){
        Map<String, String> map = this.paramValueLogDictMap.get(key);
        if (map == null){
            map = new HashMap<>();
        }
        map.put(hKey,value);
        this.paramValueLogDictMap.put(key,map);
    }
}
