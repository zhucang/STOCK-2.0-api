package com.ruoyi.common.exception;

import cn.hutool.json.JSONObject;

import java.util.List;

/**
 * 多语言异常
 *
 * @author ruoyi
 */
public class LangException extends RuntimeException{

    private String langKey;

    private String msg;

    private List<Object> params;

    public LangException(String msg)
    {
        this.msg = msg;
    }

    public LangException(String langKey, String msg)
    {
        this.langKey = langKey;
        this.msg = msg;
    }

    public LangException(String langKey, List<Object> params, String msg)
    {
        this.langKey = langKey;
        this.msg = msg;
        this.params = params;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Object> getParams() {
        return params;
    }

    public void setParams(List<Object> params) {
        this.params = params;
    }

    @Override
    public String getMessage() {
        JSONObject object = new JSONObject();
        object.put("langKey",getLangKey());
        object.put("msg",getMsg());
        object.put("params",getParams());
        return object.toString();
    }
}
