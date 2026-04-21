package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 平台邮件配置对象 mail_config
 * 
 * @author ruoyi
 * @date 2023-11-03
 */
public class MailConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 邮箱账户 */
    @Excel(name = "邮箱账户")
    private String emailAccount;

    /** 邮箱密码 */
    @Excel(name = "邮箱密码")
    private String emailPassword;

    /** smtp域名 */
    @Excel(name = "smtp域名")
    private String smtpDomain;

    /** 端口 */
    @Excel(name = "端口")
    private Integer requestPort;

    /** 状态0：启用 1：禁用 */
    @Excel(name = "状态0：启用 1：禁用")
    private Integer status;

    /** 排序 */
    @Excel(name = "排序")
    private Integer sort;

    /** 邮件标题 */
    @Excel(name = "邮件标题")
    private String emailTitle;

    /** 邮件标题多语言 */
    @Excel(name = "邮件标题多语言")
    private LangMgr emailTitleLang;

    /** 邮件内容 */
    @Excel(name = "邮件内容")
    private String emailContent;

    /** 邮件内容多语言 */
    @Excel(name = "邮件内容多语言")
    private LangMgr emailContentLang;

    /** 请求方式 0：SSL认证 1：TLS认证 */
    @Excel(name = "请求方式 0：SSL认证 1：TLS认证")
    private Integer requestMethod;

    public Integer getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(Integer requestMethod) {
        this.requestMethod = requestMethod;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setEmailAccount(String emailAccount) 
    {
        this.emailAccount = emailAccount;
    }

    public String getEmailAccount() 
    {
        return emailAccount;
    }
    public void setEmailPassword(String emailPassword) 
    {
        this.emailPassword = emailPassword;
    }

    public String getEmailPassword() 
    {
        return emailPassword;
    }
    public void setSmtpDomain(String smtpDomain) 
    {
        this.smtpDomain = smtpDomain;
    }

    public String getSmtpDomain() 
    {
        return smtpDomain;
    }
    public void setRequestPort(Integer requestPort) 
    {
        this.requestPort = requestPort;
    }

    public Integer getRequestPort() 
    {
        return requestPort;
    }
    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }
    public void setSort(Integer sort) 
    {
        this.sort = sort;
    }

    public Integer getSort() 
    {
        return sort;
    }
    public void setEmailTitle(String emailTitle) 
    {
        this.emailTitle = emailTitle;
    }

    public String getEmailTitle() 
    {
        return emailTitle;
    }
    public void setEmailContent(String emailContent) 
    {
        this.emailContent = emailContent;
    }

    public String getEmailContent() 
    {
        return emailContent;
    }

    public LangMgr getEmailTitleLang() {
        if (emailTitleLang == null){
            emailTitleLang = new LangMgr();
        }
        return emailTitleLang;
    }

    public void setEmailTitleLang(LangMgr emailTitleLang) {
        this.emailTitleLang = emailTitleLang;
    }

    public LangMgr getEmailContentLang() {
        if (emailContentLang == null){
            emailContentLang = new LangMgr();
        }
        return emailContentLang;
    }

    public void setEmailContentLang(LangMgr emailContentLang) {
        this.emailContentLang = emailContentLang;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("emailAccount", getEmailAccount())
            .append("emailPassword", getEmailPassword())
            .append("smtpDomain", getSmtpDomain())
            .append("requestPort", getRequestPort())
            .append("status", getStatus())
            .append("sort", getSort())
            .append("emailTitle", getEmailTitle())
            .append("emailContent", getEmailContent())
            .append("emailTitleLang", getEmailTitleLang())
            .append("emailContentLang", getEmailContentLang())
            .toString();
    }
}
