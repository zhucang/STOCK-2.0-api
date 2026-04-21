package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * 用户团队关系网（用户代理线）对象 user_team_level_line
 * 
 * @author ruoyi
 * @date 2023-09-09
 */
public class UserTeamLevelLine extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    /** 上级用户id */
    @Excel(name = "上级用户id")
    private Long supUserId;

    /** 团队等级 */
    @Excel(name = "团队等级")
    private Integer teamLevel;

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
    public void setSupUserId(Long supUserId) 
    {
        this.supUserId = supUserId;
    }

    public Long getSupUserId() 
    {
        return supUserId;
    }
    public void setTeamLevel(Integer teamLevel) 
    {
        this.teamLevel = teamLevel;
    }

    public Integer getTeamLevel() 
    {
        return teamLevel;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("supUserId", getSupUserId())
            .append("teamLevel", getTeamLevel())
            .append("createTime", getCreateTime())
            .toString();
    }
}
