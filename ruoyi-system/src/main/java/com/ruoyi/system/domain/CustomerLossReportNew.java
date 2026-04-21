package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.utils.cache.CacheUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客损报表
 */
public class CustomerLossReportNew {


    /** 用户id */
    private Long userId;

    /** 用户编号 */
    @Excel(name = "用户编号")
    private Long userNo;

    /** 用户账号 */
    @Excel(name = "用户账号")
    private String userAccount;

    /** 代理id */
    @Excel(name = "代理id")
    private Long agentId;

    /** 报表详情 */
    List<CustomerLossReportDetail> reports;

    /** 开始时间 */
    private Date startTime;

    /** 结束时间 */
    private Date endTime;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, Object> params;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserNo() {
        try{
            return CacheUtil.getOtherValueByKey("appShow_idAddValue", Long.class) + getUserId();
        }catch (Exception e){
            return getUserId();
        }
    }

    public void setUserNo(Long userNo) {
        this.userNo = userNo;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public List<CustomerLossReportDetail> getReports() {
        return reports;
    }

    public void setReports(List<CustomerLossReportDetail> reports) {
        this.reports = reports;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Map<String, Object> getParams()
    {
        if (params == null)
        {
            params = new HashMap<>();
        }
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
