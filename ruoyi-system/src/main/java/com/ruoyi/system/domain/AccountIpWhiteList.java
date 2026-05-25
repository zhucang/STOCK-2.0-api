package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 账号ip白名单对象 account_ip_white_list
 *
 * @author ruoyi
 * @date 2026-05-25
 */
public class AccountIpWhiteList extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 账号类型：0真实用户 1游客 */
    @Excel(name = "账号类型：0真实用户 1游客")
    private Integer accountType;

    /** 账号ID */
    @Excel(name = "账号ID")
    private Long accountId;

    /** ip地址 */
    @Excel(name = "ip地址")
    private String ipAddress;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public void setAccountType(Integer accountType)
    {
        this.accountType = accountType;
    }

    public Integer getAccountType()
    {
        return accountType;
    }

    public void setAccountId(Long accountId)
    {
        this.accountId = accountId;
    }

    public Long getAccountId()
    {
        return accountId;
    }

    public void setIpAddress(String ipAddress)
    {
        this.ipAddress = ipAddress;
    }

    public String getIpAddress()
    {
        return ipAddress;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("accountType", getAccountType())
            .append("accountId", getAccountId())
            .append("ipAddress", getIpAddress())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
