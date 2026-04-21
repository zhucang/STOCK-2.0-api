package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 文本多语言对象 text_lang
 * 
 * @author ruoyi
 * @date 2023-12-13
 */
public class TextLang extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 文本内容 */
    @Excel(name = "文本内容")
    private String content;

    /** 文本内容多语言 */
    @Excel(name = "文本内容多语言")
    private LangMgr contentLang;

    /** 类型：0：App实名认证页面规格 1：App银行卡页面规则 2：App提现页面规格 3:App按天配资规则 4:App挖矿收益计算 5:App挖矿关于违约金 6:公司简介 7:开户须知 8:操盘须知 9:投资风险 10：产品细节 11:隐私协议 12：注册协议 13：推广规则 14：福利中心 */
    @Excel(name = "类型：0：App实名认证页面规格 1：App银行卡页面规则 2：App提现页面规格 3:App按天配资规则 4:App挖矿收益计算 5:App挖矿关于违约金 6:公司简介 7:开户须知 8:操盘须知 9:投资风险 10：产品细节 11:隐私协议 12：注册协议 13：推广规则 14：福利中心")
    private Long contentType;

    /** 官网类型:  null:公用 1：官网：2:官网B */
    @Excel(name = "官网类型:  null:公用 1：官网：2:官网B")
    private Integer websiteClass;

    /** 内容是否显示 0：是 1：否 */
    @Excel(name = "内容是否显示 0：是 1：否")
    private Integer contentVisibleFlag;

    public Integer getContentVisibleFlag() {
        return contentVisibleFlag;
    }

    public void setContentVisibleFlag(Integer contentVisibleFlag) {
        this.contentVisibleFlag = contentVisibleFlag;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }
    public void setContentType(Long contentType) 
    {
        this.contentType = contentType;
    }

    public Long getContentType() 
    {
        return contentType;
    }
    public void setWebsiteClass(Integer websiteClass) 
    {
        this.websiteClass = websiteClass;
    }

    public Integer getWebsiteClass() 
    {
        return websiteClass;
    }

    public LangMgr getContentLang() {
        return contentLang;
    }

    public void setContentLang(LangMgr contentLang) {
        this.contentLang = contentLang;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("content", getContent())
            .append("remark", getRemark())
            .append("contentType", getContentType())
            .append("websiteClass", getWebsiteClass())
            .toString();
    }
}
