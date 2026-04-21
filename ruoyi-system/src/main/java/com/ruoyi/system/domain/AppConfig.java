package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * app配置对象 app_config
 * 
 * @author ruoyi
 * @date 2023-12-20
 */
public class AppConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @Excel(name = "id")
    private Long id;

    /** 页面路径 */
    @Excel(name = "页面路径")
    private String pagePath;

    /** 介绍 */
    @Excel(name = "介绍")
    private String desc;

    /** 模块 */
    @Excel(name = "模块")
    private String module;

    /** 图片路径 */
    @Excel(name = "图片路径")
    private String imgUrl;

    /** 图片路径 */
    @Excel(name = "图片路径2")
    private String imgUrl2;

    /** 提示文本 */
    @Excel(name = "提示文本")
    private String hintTxt;

    /** 视图类型 0：图片1：输入框2：纯文本3：按钮 */
    @Excel(name = "视图类型 0：图片1：输入框2：纯文本3：按钮")
    private Integer viewType;

    /** 是否显示 0：是 1：否 */
    @Excel(name = "是否显示")
    private Boolean isVisible;

    /** 是否必填 0：是 1：否 */
    @Excel(name = "是否必填")
    private Boolean isRequire;

    /** 是否密码 0：是 1：否 */
    @Excel(name = "是否密码")
    private Boolean isPassword;

    /** 规则类型 */
    @Excel(name = "规则类型")
    private String patternType;

    /** 调用方法 */
    @Excel(name = "调用方法")
    private String callFun;

    /** 显示文本及输入用 */
    @Excel(name = "显示文本及输入用")
    private String objName;

    /** obj_字段名 */
    @Excel(name = "obj_字段名")
    private String objPropertyName;

    /** 排序 */
    @Excel(name = "排序")
    private Integer sort;

    /** 右边按键文本，可控制隐藏（举例：快捷复制按钮） */
    @Excel(name = "右边按键文本，可控制隐藏", readConverterExp = "举=例：快捷复制按钮")
    private String strRight;

    /** 客户端版本 */
    @Excel(name = "客户端版本")
    private String clientVersion;

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setPagePath(String pagePath) 
    {
        this.pagePath = pagePath;
    }

    public String getPagePath() 
    {
        return pagePath;
    }
    public void setDesc(String desc) 
    {
        this.desc = desc;
    }

    public String getDesc() 
    {
        return desc;
    }
    public void setModule(String module) 
    {
        this.module = module;
    }

    public String getModule() 
    {
        return module;
    }
    public void setImgUrl(String imgUrl) 
    {
        this.imgUrl = imgUrl;
    }

    public String getImgUrl() 
    {
        return imgUrl;
    }

    public String getImgUrl2() {
        return imgUrl2;
    }

    public void setImgUrl2(String imgUrl2) {
        this.imgUrl2 = imgUrl2;
    }

    public void setHintTxt(String hintTxt)
    {
        this.hintTxt = hintTxt;
    }

    public String getHintTxt() 
    {
        return hintTxt;
    }
    public void setViewType(Integer viewType) 
    {
        this.viewType = viewType;
    }

    public Integer getViewType() 
    {
        return viewType;
    }

    public Boolean getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(Boolean visible) {
        isVisible = visible;
    }

    public Boolean getIsRequire() {
        return isRequire;
    }

    public void setIsRequire(Boolean require) {
        isRequire = require;
    }

    public Boolean getIsPassword() {
        return isPassword;
    }

    public void setIsPassword(Boolean password) {
        isPassword = password;
    }

    public void setPatternType(String patternType)
    {
        this.patternType = patternType;
    }

    public String getPatternType() 
    {
        return patternType;
    }
    public void setCallFun(String callFun) 
    {
        this.callFun = callFun;
    }

    public String getCallFun() 
    {
        return callFun;
    }
    public void setObjName(String objName) 
    {
        this.objName = objName;
    }

    public String getObjName() 
    {
        return objName;
    }
    public void setObjPropertyName(String objPropertyName) 
    {
        this.objPropertyName = objPropertyName;
    }

    public String getObjPropertyName() 
    {
        return objPropertyName;
    }
    public void setSort(Integer sort) 
    {
        this.sort = sort;
    }

    public Integer getSort() 
    {
        return sort;
    }
    public void setStrRight(String strRight) 
    {
        this.strRight = strRight;
    }

    public String getStrRight() 
    {
        return strRight;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("pagePath", getPagePath())
            .append("desc", getDesc())
            .append("module", getModule())
            .append("imgUrl", getImgUrl())
            .append("hintTxt", getHintTxt())
            .append("viewType", getViewType())
            .append("isVisible", getIsVisible())
            .append("isRequire", getIsRequire())
            .append("isPassword", getIsPassword())
            .append("patternType", getPatternType())
            .append("callFun", getCallFun())
            .append("objName", getObjName())
            .append("objPropertyName", getObjPropertyName())
            .append("sort", getSort())
            .append("strRight", getStrRight())
            .toString();
    }

//    @Override
//    public String cacheableKey() {
//        //cacheableKey
//        String cacheableKey = "";
//        //如果是app请求
//        if (HttpUtils.getHttpServletRequest().getRequestURI().equals("/api/appConfig/list")) {
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("isVisible",isVisible);
//            jsonObject.put("params.pagePath",getParams().get("pagePath"));
//            jsonObject.put("params.orderBy",getParams().get("orderBy"));
//            cacheableKey = jsonObject.toJSONString();
//        }
//        return cacheableKey;
//    }
}
