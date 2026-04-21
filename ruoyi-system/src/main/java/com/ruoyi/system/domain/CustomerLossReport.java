package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.entity.UserInfoDetailVo;
import com.ruoyi.common.utils.cache.CacheUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客损报表
 */
public class CustomerLossReport extends UserInfoDetailVo {


    /** 用户id */
    private Long userId;

    /** 余额 */
    private BigDecimal balance;

    /** 总充值 */
    private BigDecimal rechargeAmount;

    /** 总充值笔数 */
    private Integer rechargeCount;

    /** 总充值人数 */
    private Integer rechargePersonNum;

    /** 有充值的用户id */
    private Map<Long,Integer> rechargePersonIds;

    /** 充值信息 */
    private List<Map<String,Object>> rechargeInfo;

    /** 总在线支付充值 */
    private BigDecimal onlineRechargeAmount;

    /** 总在线支付充值笔数 */
    private Integer onlineRechargeCount;

    /** 总在线支付充值人数 */
    private Integer onlineRechargePersonNum;

    /** 有在线支付充值的用户id */
    private Map<Long,Integer> onlineRechargePersonIds;

    /** 在线支付充值信息 */
    private List<Map<String,Object>> onlineRechargeInfo;

    /** 总提现 */
    private BigDecimal withdrawAmount;

    /** 总提现笔数 */
    private Integer withdrawCount;

    /** 总提现人数 */
    private Integer withdrawPersonNum;

    /** 有提现的用户id */
    private Map<Long,Integer> withdrawPersonIds;

    /** 提现信息 */
    private List<Map<String,Object>> withdrawInfo;

    /** 后台上分 */
    private BigDecimal upPointAmount;

    /** 后台上分笔数 */
    private Integer upPointCount;

    /** 上分信息 */
    private List<Map<String,Object>> upPointInfo;

    /** 后台下分 */
    private BigDecimal downPointAmount;

    /** 后台下分笔数 */
    private Integer downPointCount;

    /** 下分信息 */
    private List<Map<String,Object>> downPointInfo;

    /** 赠送彩金 */
    private BigDecimal inWinningsAmount;

    /** 赠送彩金笔数 */
    private Integer inWinningsCount;

    /** 赠送彩金信息 */
    private List<Map<String,Object>> inWinningsInfo;

    /** 回收彩金 */
    private BigDecimal outWinningsAmount;

    /** 回收彩金笔数 */
    private Integer outWinningsCount;

    /** 回收彩金信息 */
    private List<Map<String,Object>> outWinningsInfo;

    /** 贷款金额 */
    private BigDecimal loanAmount;

    /** 贷款笔数 */
    private Integer loanCount;

    /** 贷款信息 */
    private List<Map<String,Object>> loanInfo;

    /** 贷款金额免客损 */
    private BigDecimal loanAmountNoStatistical;

    /** 贷款笔数免客损 */
    private Integer loanCountNoStatistical;

    /** 贷款信息免客损 */
    private List<Map<String,Object>> loanInfoNoStatistical;

    /** 总客损 */
    private BigDecimal customerLossAmount;

    /** 赠送彩金信息 */
    private List<Map<String,Object>> customerLossInfo;

    /** 1:只查看下一级 2:查看全部 */
    private Integer type;

    /** 1:只查看上月 2:查看全部 */
    private Integer isLastMonth;

    /** 角色类型 ： 0:代理  1:用户 */
    private Integer agentOrUserFlag;

    /** 首充人数 */
    private Integer firstRechargeNum;

    /** 二充人数 */
    private Integer secondRechargeNum;

    /** 首充用户ids */
    private List<Long> firstRechargePersonIds;

    /** 二充用户ids */
    private List<Long> secondRechargePersonIds;

    /** 注册人数 */
    private Integer registerNum;

    /** 币种id */
    private Long currencyId;

    /** 货币名称 */
    private Long currencyName;


    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public Long getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(Long currencyName) {
        this.currencyName = currencyName;
    }

    public Integer getFirstRechargeNum() {
        return firstRechargeNum;
    }

    public void setFirstRechargeNum(Integer firstRechargeNum) {
        this.firstRechargeNum = firstRechargeNum;
    }

    public Integer getSecondRechargeNum() {
        return secondRechargeNum;
    }

    public void setSecondRechargeNum(Integer secondRechargeNum) {
        this.secondRechargeNum = secondRechargeNum;
    }

    public List<Long> getFirstRechargePersonIds() {
        return firstRechargePersonIds;
    }

    public void setFirstRechargePersonIds(List<Long> firstRechargePersonIds) {
        this.firstRechargePersonIds = firstRechargePersonIds;
    }

    public List<Long> getSecondRechargePersonIds() {
        return secondRechargePersonIds;
    }

    public void setSecondRechargePersonIds(List<Long> secondRechargePersonIds) {
        this.secondRechargePersonIds = secondRechargePersonIds;
    }

    public Integer getRegisterNum() {
        return registerNum;
    }

    public void setRegisterNum(Integer registerNum) {
        this.registerNum = registerNum;
    }

    public Integer getAgentOrUserFlag() {
        return agentOrUserFlag;
    }

    public void setAgentOrUserFlag(Integer agentOrUserFlag) {
        this.agentOrUserFlag = agentOrUserFlag;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getOnlineRechargeAmount() {
        if (onlineRechargeAmount == null){
            return BigDecimal.ZERO;
        }
        return onlineRechargeAmount;
    }

    public void setOnlineRechargeAmount(BigDecimal onlineRechargeAmount) {
        this.onlineRechargeAmount = onlineRechargeAmount;
    }

    public Integer getOnlineRechargeCount() {
        if (onlineRechargeCount == null){
            return 0;
        }
        return onlineRechargeCount;
    }

    public void setOnlineRechargeCount(Integer onlineRechargeCount) {
        this.onlineRechargeCount = onlineRechargeCount;
    }

    public Integer getOnlineRechargePersonNum() {
        if (onlineRechargePersonNum == null){
            return 0;
        }
        return onlineRechargePersonNum;
    }

    public void setOnlineRechargePersonNum(Integer onlineRechargePersonNum) {
        this.onlineRechargePersonNum = onlineRechargePersonNum;
    }

    public Map<Long, Integer> getOnlineRechargePersonIds() {
        if (onlineRechargePersonIds == null){
            return new HashMap<>();
        }
        return onlineRechargePersonIds;
    }

    public void setOnlineRechargePersonIds(Map<Long, Integer> onlineRechargePersonIds) {
        this.onlineRechargePersonIds = onlineRechargePersonIds;
    }

    public List<Map<String, Object>> getOnlineRechargeInfo() {
        if (onlineRechargeInfo == null){
            return new ArrayList<Map<String, Object>>();
        }
        return onlineRechargeInfo;
    }

    public void setOnlineRechargeInfo(List<Map<String, Object>> onlineRechargeInfo) {
        this.onlineRechargeInfo = onlineRechargeInfo;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Integer getRechargePersonNum() {
        if (rechargePersonNum == null){
            return 0;
        }
        return rechargePersonNum;
    }

    public Map<Long, Integer> getRechargePersonIds() {
        if (withdrawPersonIds == null){
            return new HashMap<>();
        }
        return rechargePersonIds;
    }

    public void setRechargePersonIds(Map<Long, Integer> rechargePersonIds) {
        this.rechargePersonIds = rechargePersonIds;
    }

    public Map<Long, Integer> getWithdrawPersonIds() {
        if (withdrawPersonIds == null){
           return new HashMap<>();
        }
        return withdrawPersonIds;
    }

    public void setWithdrawPersonIds(Map<Long, Integer> withdrawPersonIds) {
        this.withdrawPersonIds = withdrawPersonIds;
    }

    public void setRechargePersonNum(Integer rechargePersonNum) {
        this.rechargePersonNum = rechargePersonNum;
    }

    public Integer getWithdrawPersonNum() {
        if (withdrawPersonNum == null){
            return 0;
        }
        return withdrawPersonNum;
    }

    public void setWithdrawPersonNum(Integer withdrawPersonNum) {
        this.withdrawPersonNum = withdrawPersonNum;
    }

    public BigDecimal getRechargeAmount() {
        if (rechargeAmount == null){
            return BigDecimal.ZERO;
        }
        return rechargeAmount;
    }

    public void setRechargeAmount(BigDecimal rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }

    public Integer getRechargeCount() {
        if (rechargeCount == null){
            return 0;
        }
        return rechargeCount;
    }

    public void setRechargeCount(Integer rechargeCount) {
        this.rechargeCount = rechargeCount;
    }

    public BigDecimal getWithdrawAmount() {
        if (withdrawAmount == null){
            return BigDecimal.ZERO;
        }
        return withdrawAmount;
    }

    public void setWithdrawAmount(BigDecimal withdrawAmount) {
        this.withdrawAmount = withdrawAmount;
    }

    public Integer getWithdrawCount() {
        if (withdrawCount == null){
            return 0;
        }
        return withdrawCount;
    }

    public void setWithdrawCount(Integer withdrawCount) {
        this.withdrawCount = withdrawCount;
    }

    public BigDecimal getUpPointAmount() {
        if (upPointAmount == null){
            return BigDecimal.ZERO;
        }
        return upPointAmount;
    }

    public void setUpPointAmount(BigDecimal upPointAmount) {
        this.upPointAmount = upPointAmount;
    }

    public Integer getUpPointCount() {
        if (upPointCount == null){
            return 0;
        }
        return upPointCount;
    }

    public void setUpPointCount(Integer upPointCount) {
        this.upPointCount = upPointCount;
    }

    public BigDecimal getDownPointAmount() {
        if (downPointAmount == null){
            return BigDecimal.ZERO;
        }
        return downPointAmount;
    }

    public void setDownPointAmount(BigDecimal downPointAmount) {
        this.downPointAmount = downPointAmount;
    }

    public Integer getDownPointCount() {
        if (downPointCount == null){
            return 0;
        }
        return downPointCount;
    }

    public BigDecimal getInWinningsAmount() {
        if (inWinningsAmount == null){
            return BigDecimal.ZERO;
        }
        return inWinningsAmount;
    }

    public void setInWinningsAmount(BigDecimal inWinningsAmount) {
        this.inWinningsAmount = inWinningsAmount;
    }

    public Integer getInWinningsCount() {
        if (inWinningsCount == null){
            return 0;
        }
        return inWinningsCount;
    }

    public void setInWinningsCount(Integer inWinningsCount) {
        this.inWinningsCount = inWinningsCount;
    }

    public BigDecimal getOutWinningsAmount() {
        if (outWinningsAmount == null){
            return BigDecimal.ZERO;
        }
        return outWinningsAmount;
    }

    public void setOutWinningsAmount(BigDecimal outWinningsAmount) {
        this.outWinningsAmount = outWinningsAmount;
    }

    public Integer getOutWinningsCount() {
        if (outWinningsCount == null){
            return 0;
        }
        return outWinningsCount;
    }

    public void setOutWinningsCount(Integer outWinningsCount) {
        this.outWinningsCount = outWinningsCount;
    }

    public void setDownPointCount(Integer downPointCount) {
        this.downPointCount = downPointCount;
    }

    public BigDecimal getCustomerLossAmount() {
        return customerLossAmount;
    }

    public void setCustomerLossAmount(BigDecimal customerLossAmount) {
        this.customerLossAmount = customerLossAmount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getIsLastMonth() {
        return isLastMonth;
    }

    public void setIsLastMonth(Integer isLastMonth) {
        this.isLastMonth = isLastMonth;
    }


    public List<Map<String, Object>> getRechargeInfo() {
        if (rechargeInfo == null){
            return new ArrayList<Map<String, Object>>();
        }
        return rechargeInfo;
    }

    public void setRechargeInfo(List<Map<String, Object>> rechargeInfo) {
        this.rechargeInfo = rechargeInfo;
    }

    public List<Map<String, Object>> getWithdrawInfo() {
        if (withdrawInfo == null){
            return new ArrayList<Map<String, Object>>();
        }
        return withdrawInfo;
    }

    public void setWithdrawInfo(List<Map<String, Object>> withdrawInfo) {
        this.withdrawInfo = withdrawInfo;
    }

    public List<Map<String, Object>> getUpPointInfo() {
        if (upPointInfo == null){
            return new ArrayList<Map<String, Object>>();
        }
        return upPointInfo;
    }

    public void setUpPointInfo(List<Map<String, Object>> upPointInfo) {
        this.upPointInfo = upPointInfo;
    }

    public List<Map<String, Object>> getDownPointInfo() {
        if (downPointInfo == null){
            return new ArrayList<Map<String, Object>>();
        }
        return downPointInfo;
    }

    public void setDownPointInfo(List<Map<String, Object>> downPointInfo) {
        this.downPointInfo = downPointInfo;
    }

    public List<Map<String, Object>> getInWinningsInfo() {
        if (inWinningsInfo == null){
            return new ArrayList<Map<String, Object>>();
        }
        return inWinningsInfo;
    }

    public void setInWinningsInfo(List<Map<String, Object>> inWinningsInfo) {
        this.inWinningsInfo = inWinningsInfo;
    }

    public List<Map<String, Object>> getOutWinningsInfo() {
        if (outWinningsInfo == null){
            return new ArrayList<Map<String, Object>>();
        }
        return outWinningsInfo;
    }

    public void setOutWinningsInfo(List<Map<String, Object>> outWinningsInfo) {
        this.outWinningsInfo = outWinningsInfo;
    }

    public BigDecimal getLoanAmount() {
        if (loanAmount == null){
            return BigDecimal.ZERO;
        }
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Integer getLoanCount() {
        if (loanCount == null){
            return 0;
        }
        return loanCount;
    }

    public void setLoanCount(Integer loanCount) {
        this.loanCount = loanCount;
    }

    public List<Map<String, Object>> getLoanInfo() {
        if (loanInfo == null){
            return new ArrayList<Map<String, Object>>();
        }
        return loanInfo;
    }

    public void setLoanInfo(List<Map<String, Object>> loanInfo) {
        this.loanInfo = loanInfo;
    }

    public BigDecimal getLoanAmountNoStatistical() {
        if (loanAmountNoStatistical == null){
            return BigDecimal.ZERO;
        }
        return loanAmountNoStatistical;
    }

    public void setLoanAmountNoStatistical(BigDecimal loanAmountNoStatistical) {
        this.loanAmountNoStatistical = loanAmountNoStatistical;
    }

    public Integer getLoanCountNoStatistical() {
        if (loanCountNoStatistical == null){
            return 0;
        }
        return loanCountNoStatistical;
    }

    public void setLoanCountNoStatistical(Integer loanCountNoStatistical) {
        this.loanCountNoStatistical = loanCountNoStatistical;
    }

    public List<Map<String, Object>> getLoanInfoNoStatistical() {
        if (loanInfoNoStatistical == null){
            return new ArrayList<Map<String, Object>>();
        }
        return loanInfoNoStatistical;
    }

    public void setLoanInfoNoStatistical(List<Map<String, Object>> loanInfoNoStatistical) {
        this.loanInfoNoStatistical = loanInfoNoStatistical;
    }

    public List<Map<String, Object>> getCustomerLossInfo() {
        if (customerLossInfo == null){
            return new ArrayList<Map<String, Object>>();
        }
        return customerLossInfo;
    }

    public void setCustomerLossInfo(List<Map<String, Object>> customerLossInfo) {
        this.customerLossInfo = customerLossInfo;
    }

    public Long getUserNo() {
        try{
            return CacheUtil.getOtherValueByKey("appShow_idAddValue", Long.class) + getUserId();
        }catch (Exception e){
            return getUserId();
        }
    }

}
