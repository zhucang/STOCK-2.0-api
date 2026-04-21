package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.entity.UserInfoDetailVo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 用户实名认证信息对象 user_auth_record
 * 
 * @author ruoyi
 * @date 2024-04-05
 */
public class UserAuthRecord extends UserInfoDetailVo
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    /** 证件号码 */
    @Excel(name = "证件号码")
    private String idNumber;

    /** 真实名称 */
    @Excel(name = "真实名称")
    private String realName;

    /** 证件图片（正面） */
    @Excel(name = "证件图片", readConverterExp = "正=面")
    private String img1Key;

    /** 证件图片（背面） */
    @Excel(name = "证件图片", readConverterExp = "背=面")
    private String img2Key;

    /** 证件图片（手持） */
    @Excel(name = "证件图片", readConverterExp = "手=持")
    private String img3Key;

    /** 实名审核反馈信息 */
    @Excel(name = "实名审核反馈信息")
    private String authMsg;

    /** 0：审核中 1：审核通过 2：审核驳回 */
    @Excel(name = "实名状态 0：审核中 1：审核通过 2：审核驳回")
    private Integer authStatus;

    /** 证件类型 0：身份证 1：驾驶证 2：护照 */
    @Excel(name = "证件类型 0：身份证 1：驾驶证 2：护照")
    private Integer authMethod;

    /** 实名等级 0:初级 1：高级 */
    @Excel(name = "实名等级 0:初级 1：高级")
    private Integer authLevel;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date approveTime;

    /** 审核人 */
    @Excel(name = "审核人")
    private String approveName;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setIdNumber(String idNumber) 
    {
        this.idNumber = idNumber;
    }

    public String getIdNumber() 
    {
        return idNumber;
    }
    public void setRealName(String realName) 
    {
        this.realName = realName;
    }

    public String getRealName() 
    {
        return realName;
    }
    public void setImg1Key(String img1Key) 
    {
        this.img1Key = img1Key;
    }

    public String getImg1Key() 
    {
        return img1Key;
    }
    public void setImg2Key(String img2Key) 
    {
        this.img2Key = img2Key;
    }

    public String getImg2Key() 
    {
        return img2Key;
    }
    public void setImg3Key(String img3Key) 
    {
        this.img3Key = img3Key;
    }

    public String getImg3Key() 
    {
        return img3Key;
    }
    public void setAuthMsg(String authMsg) 
    {
        this.authMsg = authMsg;
    }

    public String getAuthMsg() 
    {
        return authMsg;
    }

    public Integer getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(Integer authStatus) {
        this.authStatus = authStatus;
    }

    public void setAuthMethod(Integer authMethod)
    {
        this.authMethod = authMethod;
    }

    public Integer getAuthMethod() 
    {
        return authMethod;
    }
    public void setAuthLevel(Integer authLevel) 
    {
        this.authLevel = authLevel;
    }

    public Integer getAuthLevel() 
    {
        return authLevel;
    }
    public void setApproveTime(Date approveTime) 
    {
        this.approveTime = approveTime;
    }

    public Date getApproveTime() 
    {
        return approveTime;
    }
    public void setApproveName(String approveName) 
    {
        this.approveName = approveName;
    }

    public String getApproveName() 
    {
        return approveName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("idNumber", getIdNumber())
            .append("realName", getRealName())
            .append("img1Key", getImg1Key())
            .append("img2Key", getImg2Key())
            .append("img3Key", getImg3Key())
            .append("authMsg", getAuthMsg())
            .append("authStatus", getAuthStatus())
            .append("authMethod", getAuthMethod())
            .append("authLevel", getAuthLevel())
            .append("createTime", getCreateTime())
            .append("approveTime", getApproveTime())
            .append("approveName", getApproveName())
            .toString();
    }
}
