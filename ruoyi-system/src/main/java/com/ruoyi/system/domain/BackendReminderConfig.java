package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 后台提醒配置对象 backend_reminder_config
 * 
 * @author ruoyi
 * @date 2024-07-07
 */
public class BackendReminderConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 名称 */
    @Excel(name = "名称")
    private String reminderName;

    /** 提醒类型 0：不提示 1：提醒一次 :2：循环提醒 */
    @Excel(name = "提醒类型 0：不提示 1：提醒一次 :2：循环提醒")
    private Integer reminderType;

    /** 跳转类型 0：初级实名认证:1：高级实名认证:2：充值:3：提现:4：贷款申请:5：贷款还款 */
    @Excel(name = "跳转类型 0：初级实名认证:1：高级实名认证 2：充值 3：提现 4：贷款申请 5：贷款还款")
    private Integer jumpType;

    /** 跳转路径 */
    @Excel(name = "跳转路径")
    private String jumpUrl;

    /** 音源编号 */
    @Excel(name = "音源编号")
    private Integer musicSourceNumber;

    /** 筛选状态 */
    @Excel(name = "音源编号")
    private Integer searchStatus;

    /** 状态 0：启用 1：禁用 */
    @Excel(name = "状态 0：启用 1：禁用")
    private Integer status;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setReminderName(String reminderName) 
    {
        this.reminderName = reminderName;
    }

    public String getReminderName() 
    {
        return reminderName;
    }
    public void setReminderType(Integer reminderType) 
    {
        this.reminderType = reminderType;
    }

    public Integer getReminderType() 
    {
        return reminderType;
    }
    public void setJumpType(Integer jumpType) 
    {
        this.jumpType = jumpType;
    }

    public Integer getJumpType() 
    {
        return jumpType;
    }

    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }

    public void setMusicSourceNumber(Integer musicSourceNumber)
    {
        this.musicSourceNumber = musicSourceNumber;
    }

    public Integer getMusicSourceNumber() 
    {
        return musicSourceNumber;
    }

    public Integer getSearchStatus() {
        return searchStatus;
    }

    public void setSearchStatus(Integer searchStatus) {
        this.searchStatus = searchStatus;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("reminderName", getReminderName())
            .append("reminderType", getReminderType())
            .append("jumpType", getJumpType())
            .append("musicSourceNumber", getMusicSourceNumber())
            .append("status", getStatus())
            .toString();
    }
}
