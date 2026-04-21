package com.ruoyi.system.service.impl;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.entity.UserInfo;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.CustomerLossReportMapper;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.service.*;
import com.ruoyi.system.utils.currencyExchangeRate.ExchangeRateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CustomerLossReportServiceImpl implements ICustomerLossReportService {

    @Resource
    private CustomerLossReportMapper customerLossReportMapper;

    @Autowired
    private IUserInfoService userInfoService;

    @Resource
    private SysUserMapper sysUserMapper;

    @Autowired
    private IAgentTeamLevelLineService agentTeamLevelLineService;

    @Autowired
    private IPlatformCurrencyService platformCurrencyService;

    @Autowired
    private ISwitchSetService switchSetService;

    /**
     * 获取代理客损信息列表
     * @param customerLossReport
     * @return
     */
    @Override
    public List<CustomerLossReport> getBillAnalysis(CustomerLossReport customerLossReport){
        //结果集
        List<CustomerLossReport> result = new ArrayList<>();

        //如果只查看上月
        if (customerLossReport.getIsLastMonth() != null && customerLossReport.getIsLastMonth() == 1){
            customerLossReport.setStartTime(DateUtil.date(DateUtils.getLastMonthStartTime()));
            customerLossReport.setEndTime(DateUtil.date(DateUtils.getLastMonthEndTime()));
        }

        //上级代理id
        Long agentId = customerLossReport.getAgentId();
        customerLossReport.setAgentId(null);
        //上级代理信息
        SysUser agentUser = sysUserMapper.selectUserById(agentId);
        //查询的代理等级
        Integer queryLevel = null;
        //如果只查看下一级代理
        if (customerLossReport.getType().equals(1)){
            queryLevel=1;
        }
        //团队信息
        List<AgentTeamLevelLine> lowerTeamLine = agentTeamLevelLineService.getLowerTeamLine(agentId, queryLevel, 0);
        SysUser sysUser = new SysUser();
        sysUser.getParams().put("userIds",lowerTeamLine.stream().map(AgentTeamLevelLine::getUserId).collect(Collectors.toList()));
        sysUser.getParams().put("agentData",1);
        //下级代理列表
        List<SysUser> lowerAgentUsers = sysUserMapper.selectUserList(sysUser);
        //贷款是否计入总客损开关
        Integer switchStatus92 = (Integer) customerLossReport.getParams().get("switchStatus92");
        for (int i = 0; i < lowerAgentUsers.size(); i++) {
            //下级代理信息
            SysUser lowerAgentUser = lowerAgentUsers.get(i);
            //下级代理管辖的用户
            UserInfo userInfo = new UserInfo();
            userInfo.setAccountType(0);
            userInfo.setAgentId(lowerAgentUser.getUserId());
            //如果只查看下一级代理
            if (customerLossReport.getType().equals(1)){
                //统计时把这些代理的下级代理数据一起统计
                userInfo.getParams().put("isAllAgentData",0);
            }
            List<UserInfo> userInfos = userInfoService.selectUserInfoList(userInfo);
            //用户ids
            List<Long> userIds = userInfos.stream().map(a -> a.getId()).collect(Collectors.toList());
            if (userIds.size() == 0){
                //如果该代理没有发展用户
                CustomerLossReport customerLossReportVo = new CustomerLossReport();
                customerLossReportVo.setUserAccount(lowerAgentUsers.get(i).getUserName() + "(代理)");
                customerLossReportVo.setUserId(lowerAgentUsers.get(i).getUserId());
                customerLossReportVo.setAgentOrUserFlag(0);
                result.add(customerLossReportVo);
                continue;
            }
            customerLossReport.getParams().put("userIds",userIds);
            List<CustomerLossReport> customerLossReports = customerLossReportMapper.getCustomerLossReport(customerLossReport);

            CustomerLossReport customerLossReportVo = new CustomerLossReport();
            //总充值金额
            BigDecimal rechargeAmount = customerLossReports.stream().map(a -> a.getRechargeAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
            //总充值次数
            Integer rechargeCount = customerLossReports.stream().map(a -> a.getRechargeCount()).reduce(0, Integer::sum);

            //总在线支付充值金额
            BigDecimal onlineRechargeAmount = customerLossReports.stream().map(a -> a.getOnlineRechargeAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
            //总在线支付充值次数
            Integer onlineRechargeCount = customerLossReports.stream().map(a -> a.getOnlineRechargeCount()).reduce(0, Integer::sum);

            //总提现金额
            BigDecimal withdrawAmount = customerLossReports.stream().map(a -> a.getWithdrawAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
            //总提现次数
            Integer withdrawCount = customerLossReports.stream().map(a -> a.getWithdrawCount()).reduce(0, Integer::sum);

            //总上分金额
            BigDecimal upPointAmount = customerLossReports.stream().map(a -> a.getUpPointAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
            //总上分次数
            Integer upPointCount = customerLossReports.stream().map(a -> a.getUpPointCount()).reduce(0, Integer::sum);

            //总下分金额
            BigDecimal downPointAmount = customerLossReports.stream().map(a -> a.getDownPointAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
            //总下分次数
            Integer downPointCount = customerLossReports.stream().map(a -> a.getDownPointCount()).reduce(0, Integer::sum);

            //总赠送彩金金额
            BigDecimal inWinningsAmount = customerLossReports.stream().map(a -> a.getInWinningsAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
            //总赠送彩金金额次数
            Integer inWinningsCount = customerLossReports.stream().map(a -> a.getInWinningsCount()).reduce(0, Integer::sum);

            //总回收彩金金额
            BigDecimal outWinningsAmount = customerLossReports.stream().map(a -> a.getOutWinningsAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
            //总回收彩金金额次数
            Integer outWinningsCount = customerLossReports.stream().map(a -> a.getOutWinningsCount()).reduce(0, Integer::sum);

            //总贷款金额
            BigDecimal loanAmount = customerLossReports.stream().map(a -> a.getLoanAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
            //总贷款次数
            Integer loanCount = customerLossReports.stream().map(a -> a.getLoanCount()).reduce(0, Integer::sum);

            //免客损贷款金额
            BigDecimal loanAmountNoStatistical = customerLossReports.stream().map(a -> a.getLoanAmountNoStatistical()).reduce(BigDecimal.ZERO, BigDecimal::add);
            //免客损贷款次数
            Integer loanCountNoStatistical = customerLossReports.stream().map(a -> a.getLoanCountNoStatistical()).reduce(0, Integer::sum);


            //有充值的用户id
            Map<Long, Integer> rechargePersonIds = customerLossReports.stream().filter(a -> a.getRechargeCount()+a.getOnlineRechargeCount() > 0).collect(Collectors.toMap(CustomerLossReport::getUserId, a->a.getRechargeCount()+a.getOnlineRechargeCount()));
            //有提现的用户id
            Map<Long, Integer> withdrawPersonIds = customerLossReports.stream().filter(a -> a.getWithdrawCount() > 0).collect(Collectors.toMap(CustomerLossReport::getUserId, a -> a.getWithdrawCount()));

            customerLossReportVo.setUserAccount(lowerAgentUsers.get(i).getUserName() + "(代理)");
            customerLossReportVo.setUserId(lowerAgentUsers.get(i).getUserId());
            customerLossReportVo.setAgentOrUserFlag(0);
            customerLossReportVo.setRechargeAmount(rechargeAmount);
            customerLossReportVo.setRechargeCount(rechargeCount);
            customerLossReportVo.setOnlineRechargeAmount(onlineRechargeAmount);
            customerLossReportVo.setOnlineRechargeCount(onlineRechargeCount);
            customerLossReportVo.setWithdrawAmount(withdrawAmount);
            customerLossReportVo.setWithdrawCount(withdrawCount);
            customerLossReportVo.setUpPointAmount(upPointAmount);
            customerLossReportVo.setUpPointCount(upPointCount);
            customerLossReportVo.setDownPointAmount(downPointAmount);
            customerLossReportVo.setDownPointCount(downPointCount);
            customerLossReportVo.setInWinningsAmount(inWinningsAmount);
            customerLossReportVo.setInWinningsCount(inWinningsCount);
            customerLossReportVo.setOutWinningsAmount(outWinningsAmount);
            customerLossReportVo.setOutWinningsCount(outWinningsCount);
            customerLossReportVo.setLoanAmount(loanAmount);
            customerLossReportVo.setLoanCount(loanCount);
            customerLossReportVo.setLoanAmountNoStatistical(loanAmountNoStatistical);
            customerLossReportVo.setLoanCountNoStatistical(loanCountNoStatistical);
            customerLossReportVo.setRechargePersonIds(rechargePersonIds);
            customerLossReportVo.setWithdrawPersonIds(withdrawPersonIds);
            customerLossReportVo.setRegisterNum(customerLossReportMapper.getRegNum(customerLossReport));

            //总客损(充值+上分-提现)
            BigDecimal customerLossAmount = customerLossReportVo.getRechargeAmount().add(customerLossReportVo.getOnlineRechargeAmount()).add(customerLossReportVo.getUpPointAmount()).subtract(customerLossReportVo.getWithdrawAmount());
            if (switchStatus92.equals(0)){
                customerLossAmount = customerLossAmount.add(customerLossReportVo.getLoanAmount());
            }
            customerLossReportVo.setCustomerLossAmount(customerLossAmount);
            result.add(customerLossReportVo);
        }

        //下级代理推广的注册人数
        Integer num = result.stream().map(a -> a.getRegisterNum() == null ? 0 : a.getRegisterNum()).reduce(0, Integer::sum);

        //自己管辖的用户
        UserInfo userInfo = new UserInfo();
        userInfo.setAgentId(agentId);
        userInfo.setAccountType(0);
        PageHelper.orderBy("id desc");
        List<UserInfo> userInfos = userInfoService.selectUserInfoList(userInfo);
        //自己管辖的用户ids
        List<Long> userIds = userInfos.stream().map(a -> a.getId()).collect(Collectors.toList());
        //如果自己有管辖的用户
        if (userIds.size() != 0){
            customerLossReport.getParams().put("userIds",userIds);
            List<CustomerLossReport> customerLossReports = customerLossReportMapper.getCustomerLossReport(customerLossReport);
            for (int i = 0; i < customerLossReports.size(); i++) {
                CustomerLossReport customerLossReportUser = customerLossReports.get(i);
                //用户账号
                String account = userInfos.get(i).getUserAccount();
                customerLossReportUser.setUserAccount(account+"(用户)");
                customerLossReportUser.setUserId(userInfos.get(i).getId());
                customerLossReportUser.setAgentOrUserFlag(1);
                //总客损(充值+上分-提现)
                BigDecimal customerLossAmount = customerLossReportUser.getRechargeAmount().add(customerLossReportUser.getOnlineRechargeAmount()).add(customerLossReportUser.getUpPointAmount()).subtract(customerLossReportUser.getWithdrawAmount());
                if (switchStatus92.equals(0)){
                    customerLossAmount = customerLossAmount.add(customerLossReportUser.getLoanAmount());
                }
                customerLossReportUser.setCustomerLossAmount(customerLossAmount);
                result.add(customerLossReportUser);
            }
        }

        //统计自身客损数据
        CustomerLossReport customerLossReportVo = new CustomerLossReport();
        //总充值金额
        BigDecimal rechargeAmount = result.stream().map(a -> a.getRechargeAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
        //总充值次数
        Integer rechargeCount = result.stream().map(a -> a.getRechargeCount()).reduce(0, Integer::sum);

        //总在线支付充值金额
        BigDecimal onlineRechargeAmount = result.stream().map(a -> a.getOnlineRechargeAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
        //总在线支付充值次数
        Integer onlineRechargeCount = result.stream().map(a -> a.getOnlineRechargeCount()).reduce(0, Integer::sum);

        //总提现金额
        BigDecimal withdrawAmount = result.stream().map(a -> a.getWithdrawAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
        //总提现次数
        Integer withdrawCount = result.stream().map(a -> a.getWithdrawCount()).reduce(0, Integer::sum);

        //总上分金额
        BigDecimal upPointAmount = result.stream().map(a -> a.getUpPointAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
        //总上分次数
        Integer upPointCount = result.stream().map(a -> a.getUpPointCount()).reduce(0, Integer::sum);

        //总下分金额
        BigDecimal downPointAmount = result.stream().map(a -> a.getDownPointAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
        //总下分次数
        Integer downPointCount = result.stream().map(a -> a.getDownPointCount()).reduce(0, Integer::sum);

        //总赠送彩金金额
        BigDecimal inWinningsAmount = result.stream().map(a -> a.getInWinningsAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
        //总赠送彩金金额次数
        Integer inWinningsCount = result.stream().map(a -> a.getInWinningsCount()).reduce(0, Integer::sum);

        //总回收彩金金额
        BigDecimal outWinningsAmount = result.stream().map(a -> a.getOutWinningsAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
        //总回收彩金金额次数
        Integer outWinningsCount = result.stream().map(a -> a.getOutWinningsCount()).reduce(0, Integer::sum);

        //总贷款金额
        BigDecimal loanAmount = result.stream().map(a -> a.getLoanAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
        //总贷款次数
        Integer loanCount = result.stream().map(a -> a.getLoanCount()).reduce(0, Integer::sum);

        //免客损贷款金额
        BigDecimal loanAmountNoStatistical = result.stream().map(a -> a.getLoanAmountNoStatistical()).reduce(BigDecimal.ZERO, BigDecimal::add);
        //免客损贷款次数
        Integer loanCountNoStatistical = result.stream().map(a -> a.getLoanCountNoStatistical()).reduce(0, Integer::sum);

        //充值人数
        Integer rechargePersonNum = Integer.valueOf(String.valueOf(result.stream().filter(a->a.getRechargeCount() > 0).count()));
        //提现人数
        Integer withdrawPersonNum = Integer.valueOf(String.valueOf(result.stream().filter(a->a.getWithdrawCount() > 0).count()));

        //有充值的用户id
        Map<Long, Integer> rechargePersonIds = new HashMap<>();
        //有提现的用户id
        Map<Long, Integer> withdrawPersonIds = new HashMap<>();

        //
        for (int i = 0; i < result.size(); i++) {
            //
            Map<Long, Integer> rechargePersonIdsVo = result.get(i).getRechargePersonIds();
            if (rechargePersonIdsVo != null) {
                rechargePersonIds.putAll(rechargePersonIdsVo);
            } else {
                rechargePersonIds.put(result.get(i).getUserId(), result.get(i).getRechargeCount() + result.get(i).getOnlineRechargeCount());
            }
            //
            Map<Long, Integer> withdrawPersonIdsVo = result.get(i).getWithdrawPersonIds();
            if (withdrawPersonIdsVo != null) {
                withdrawPersonIds.putAll(withdrawPersonIdsVo);
            } else {
                withdrawPersonIds.put(result.get(i).getUserId(), result.get(i).getWithdrawCount());
            }
        }

        customerLossReportVo.setUserAccount(agentUser.getUserName() + "(自己)");
        customerLossReportVo.setUserId(agentUser.getUserId());
        customerLossReportVo.setAgentOrUserFlag(0);
        customerLossReportVo.setRechargeAmount(rechargeAmount);
        customerLossReportVo.setRechargeCount(rechargeCount);
        customerLossReportVo.setOnlineRechargeAmount(onlineRechargeAmount);
        customerLossReportVo.setOnlineRechargeCount(onlineRechargeCount);
        customerLossReportVo.setWithdrawAmount(withdrawAmount);
        customerLossReportVo.setWithdrawCount(withdrawCount);
        customerLossReportVo.setUpPointAmount(upPointAmount);
        customerLossReportVo.setUpPointCount(upPointCount);
        customerLossReportVo.setDownPointAmount(downPointAmount);
        customerLossReportVo.setDownPointCount(downPointCount);
        customerLossReportVo.setInWinningsAmount(inWinningsAmount);
        customerLossReportVo.setInWinningsCount(inWinningsCount);
        customerLossReportVo.setOutWinningsAmount(outWinningsAmount);
        customerLossReportVo.setOutWinningsCount(outWinningsCount);
        customerLossReportVo.setLoanAmount(loanAmount);
        customerLossReportVo.setLoanCount(loanCount);
        customerLossReportVo.setLoanAmountNoStatistical(loanAmountNoStatistical);
        customerLossReportVo.setLoanCountNoStatistical(loanCountNoStatistical);
        customerLossReportVo.setRechargePersonNum(rechargePersonNum);
        customerLossReportVo.setWithdrawPersonNum(withdrawPersonNum);
        customerLossReportVo.setRechargePersonIds(rechargePersonIds);
        customerLossReportVo.setWithdrawPersonIds(withdrawPersonIds);
        customerLossReportVo.setRegisterNum(customerLossReportMapper.getRegNum(customerLossReport)+num);

        //总客损(充值+上分-提现)
        BigDecimal customerLossAmount = customerLossReportVo.getRechargeAmount().add(customerLossReportVo.getOnlineRechargeAmount()).add(customerLossReportVo.getUpPointAmount()).subtract(customerLossReportVo.getWithdrawAmount());
        if (switchStatus92.equals(0)){
            customerLossAmount = customerLossAmount.add(customerLossReportVo.getLoanAmount());
        }
        customerLossReportVo.setCustomerLossAmount(customerLossAmount);
        //自己的信息放在首位
        result.add(0,customerLossReportVo);
        return result;
    }

    //获取代理客损总统计
    public List<CustomerLossReport> getAllAnalysis(CustomerLossReport customerLossReport){
        //如果只查看上月
        if (customerLossReport.getIsLastMonth() != null && customerLossReport.getIsLastMonth() == 1){
            customerLossReport.setStartTime(DateUtil.date(DateUtils.getLastMonthStartTime()));
            customerLossReport.setEndTime(DateUtil.date(DateUtils.getLastMonthEndTime()));
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setAccountType(0);
        List<UserInfo> userInfos = userInfoService.selectUserInfoList(userInfo);
        customerLossReport.getParams().put("userIds", userInfos.stream().map(UserInfo::getId).collect(Collectors.toList()));
        List<CustomerLossReport> customerLossReports = customerLossReportMapper.getCustomerLossReport(customerLossReport);
        if (customerLossReports.size() == 0){
            //如果没有实盘用户
            List<CustomerLossReport> result = new ArrayList<>();
            CustomerLossReport customerLossReportNew = new CustomerLossReport();
            customerLossReportNew.setUserAccount("总统计");
            result.add(customerLossReportNew);
            return result;
        }
        //总充值金额
        BigDecimal rechargeAmount = customerLossReports.stream().map(a -> a.getRechargeAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
        //总充值次数
        Integer rechargeCount = customerLossReports.stream().map(a -> a.getRechargeCount()).reduce(0, Integer::sum);

        //总在线支付充值金额
        BigDecimal onlineRechargeAmount = customerLossReports.stream().map(a -> a.getOnlineRechargeAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
        //总在线支付充值次数
        Integer onlineRechargeCount = customerLossReports.stream().map(a -> a.getOnlineRechargeCount()).reduce(0, Integer::sum);

        //总提现金额
        BigDecimal withdrawAmount = customerLossReports.stream().map(a -> a.getWithdrawAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
        //总提现次数
        Integer withdrawCount = customerLossReports.stream().map(a -> a.getWithdrawCount()).reduce(0, Integer::sum);

        //总上分金额
        BigDecimal upPointAmount = customerLossReports.stream().map(a -> a.getUpPointAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
        //总上分次数
        Integer upPointCount = customerLossReports.stream().map(a -> a.getUpPointCount()).reduce(0, Integer::sum);

        //总下分金额
        BigDecimal downPointAmount = customerLossReports.stream().map(a -> a.getDownPointAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
        //总下分次数
        Integer downPointCount = customerLossReports.stream().map(a -> a.getDownPointCount()).reduce(0, Integer::sum);

        //总赠送彩金金额
        BigDecimal inWinningsAmount = customerLossReports.stream().map(a -> a.getInWinningsAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
        //总赠送彩金金额次数
        Integer inWinningsCount = customerLossReports.stream().map(a -> a.getInWinningsCount()).reduce(0, Integer::sum);

        //总回收彩金金额
        BigDecimal outWinningsAmount = customerLossReports.stream().map(a -> a.getOutWinningsAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
        //总回收彩金金额次数
        Integer outWinningsCount = customerLossReports.stream().map(a -> a.getOutWinningsCount()).reduce(0, Integer::sum);

        //总贷款金额
        BigDecimal loanAmount = customerLossReports.stream().map(a -> a.getLoanAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
        //总贷款次数
        Integer loanCount = customerLossReports.stream().map(a -> a.getLoanCount()).reduce(0, Integer::sum);

        //免客损贷款金额
        BigDecimal loanAmountNoStatistical = customerLossReports.stream().map(a -> a.getLoanAmountNoStatistical()).reduce(BigDecimal.ZERO, BigDecimal::add);
        //免客损贷款次数
        Integer loanCountNoStatistical = customerLossReports.stream().map(a -> a.getLoanCountNoStatistical()).reduce(0, Integer::sum);


        //有充值的用户id
        Map<Long, Integer> rechargePersonIds = customerLossReports.stream().filter(a -> a.getRechargeCount()+a.getOnlineRechargeCount() > 0).collect(Collectors.toMap(CustomerLossReport::getUserId, a->a.getRechargeCount()+a.getOnlineRechargeCount()));
        //有提现的用户id
        Map<Long, Integer> withdrawPersonIds = customerLossReports.stream().filter(a -> a.getWithdrawCount() > 0).collect(Collectors.toMap(CustomerLossReport::getUserId, a -> a.getWithdrawCount()));

        //总统计
        CustomerLossReport customerLossReportVo = new CustomerLossReport();
        customerLossReportVo.setUserAccount("总统计");
        customerLossReportVo.setRechargeAmount(rechargeAmount);
        customerLossReportVo.setRechargeCount(rechargeCount);
        customerLossReportVo.setOnlineRechargeAmount(onlineRechargeAmount);
        customerLossReportVo.setOnlineRechargeCount(onlineRechargeCount);
        customerLossReportVo.setWithdrawAmount(withdrawAmount);
        customerLossReportVo.setWithdrawCount(withdrawCount);
        customerLossReportVo.setUpPointAmount(upPointAmount);
        customerLossReportVo.setUpPointCount(upPointCount);
        customerLossReportVo.setDownPointAmount(downPointAmount);
        customerLossReportVo.setDownPointCount(downPointCount);
        customerLossReportVo.setInWinningsAmount(inWinningsAmount);
        customerLossReportVo.setInWinningsCount(inWinningsCount);
        customerLossReportVo.setOutWinningsAmount(outWinningsAmount);
        customerLossReportVo.setOutWinningsCount(outWinningsCount);
        customerLossReportVo.setLoanAmount(loanAmount);
        customerLossReportVo.setLoanCount(loanCount);
        customerLossReportVo.setLoanAmountNoStatistical(loanAmountNoStatistical);
        customerLossReportVo.setLoanCountNoStatistical(loanCountNoStatistical);
        customerLossReportVo.setRechargePersonIds(rechargePersonIds);
        customerLossReportVo.setWithdrawPersonIds(withdrawPersonIds);
        //注册人数
        customerLossReportVo.setRegisterNum(customerLossReportMapper.getRegNum(customerLossReport));
        //总客损(充值+上分-提现)
        BigDecimal customerLossAmount = customerLossReportVo.getRechargeAmount().add(customerLossReportVo.getOnlineRechargeAmount()).add(customerLossReportVo.getUpPointAmount()).subtract(customerLossReportVo.getWithdrawAmount());
        //贷款是否计入总客损开关
        Integer switchStatus92 = (Integer) customerLossReport.getParams().get("switchStatus92");
        if (switchStatus92.equals(0)){
            customerLossAmount = customerLossAmount.add(customerLossReportVo.getLoanAmount());
        }
        customerLossReportVo.setCustomerLossAmount(customerLossAmount);
        List<CustomerLossReport> result = new ArrayList<>();
        result.add(customerLossReportVo);
        return result;
    }

    /**
     *  用户客损报表
     * @param customerLossReport
     * @return
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u", isUserInfo = true)
    public List<CustomerLossReportNew> userCustomerLossReport(CustomerLossReportNew customerLossReport){
        //用户客损报表
        PageHelper.orderBy("pl.sort,sort is null");
        List<CustomerLossReportNew> customerLossReports = customerLossReportMapper.getCustomerLossReportNew(customerLossReport);
        //如果没有用户
        if (customerLossReports.size() == 0){
            return customerLossReports;
        }
        //所有币种
        PlatformCurrency search = new PlatformCurrency();
        search.setStatus(0);
        List<PlatformCurrency> platformCurrencies = platformCurrencyService.selectPlatformCurrencyList(search);
        ExchangeRateUtil.fillExchangeRate(platformCurrencies);
        //币种map
        Map<Long, PlatformCurrency> platformCurrencyMap = platformCurrencies.stream().collect(Collectors.toMap(PlatformCurrency::getId, a -> a));
        //USDT币种ID
        Long defaultTradeCurrencyId = 3L;
//        //平台默认交易币种
//        Long defaultTradeCurrencyId = CacheUtil.getOtherValueByKey("default_trade_currency_id", Long.class);
//        if (defaultTradeCurrencyId == null){
//            throw new ServiceException("请先设置平台默认交易币种");
//        }
        //汇率map
        Map<String,BigDecimal> exchangeRateMap = platformCurrencies.stream().collect(Collectors.toMap(a->a.getId()+"/"+defaultTradeCurrencyId,a->{
            //平台默认交易币种信息
            PlatformCurrency platformCurrency = platformCurrencyMap.get(defaultTradeCurrencyId);
            //汇率
            BigDecimal exchangeRate = ExchangeRateUtil.getExchangeInfo(a.getId(),defaultTradeCurrencyId,platformCurrencyMap).get("exchangeRate");
            if (exchangeRate.compareTo(BigDecimal.ZERO) == 0){
                throw new ServiceException("获取"+a.getCurrencyName()+"与"+platformCurrency.getCurrencyName()+"的汇率异常");
            }
            return exchangeRate;
        }));
        //贷款是否计入总客损开关
        Integer switchStatus92 = switchSetService.selectSwitchStatusById(92L);
        //计算客损
        customerLossReports.stream().map(a->{
            a.getReports().stream().map(b->{
                //贷款金额
                BigDecimal loanAmount = BigDecimal.ZERO;
                if (switchStatus92.equals(0)){
                    loanAmount = b.getLoanAmount();
                }
                b.setCustomerLossAmount(b.getRechargeAmount().add(b.getOnlineRechargeAmount()).add(b.getUpPointAmount()).add(loanAmount).subtract(b.getWithdrawAmount()));
                return b;
            }).collect(Collectors.toList());
            return a;
        }).collect(Collectors.toList());

        //遍历计算合计U
        for (int i = 0; i < customerLossReports.size(); i++) {
            //客损报表信息
            CustomerLossReportNew customerLossReportNew = customerLossReports.get(i);
            //各币种报表信息
            List<CustomerLossReportDetail> reports = customerLossReportNew.getReports();

            //充值金额合U
            BigDecimal rechargeAmountU = reports.stream().map(a->a.getRechargeAmount().multiply(exchangeRateMap.get(a.getCurrencyId()+"/"+defaultTradeCurrencyId))).reduce(BigDecimal.ZERO,BigDecimal::add).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            //充值笔数合计
            Integer rechargeCountAll = reports.stream().map(a->a.getRechargeCount()).reduce(0,Integer::sum);
            //在线支付充值金额合U
            BigDecimal onlineRechargeAmountU = reports.stream().map(a->a.getOnlineRechargeAmount().multiply(exchangeRateMap.get(a.getCurrencyId()+"/"+defaultTradeCurrencyId))).reduce(BigDecimal.ZERO,BigDecimal::add).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            //在线支付充值笔数合计
            Integer onlineRechargeCountAll = reports.stream().map(a->a.getOnlineRechargeCount()).reduce(0,Integer::sum);
            //提现金额合U
            BigDecimal withdrawAmountU = reports.stream().map(a->a.getWithdrawAmount().multiply(exchangeRateMap.get(a.getCurrencyId()+"/"+defaultTradeCurrencyId))).reduce(BigDecimal.ZERO,BigDecimal::add).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            //提现笔数合计
            Integer withdrawCountAll = reports.stream().map(a->a.getWithdrawCount()).reduce(0,Integer::sum);
            //上分金额合U
            BigDecimal upPointAmountU = reports.stream().map(a->a.getUpPointAmount().multiply(exchangeRateMap.get(a.getCurrencyId()+"/"+defaultTradeCurrencyId))).reduce(BigDecimal.ZERO,BigDecimal::add).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            //上分笔数合计
            Integer upPointCountAll = reports.stream().map(a->a.getUpPointCount()).reduce(0,Integer::sum);
            //下分金额合U
            BigDecimal downPointAmountU = reports.stream().map(a->a.getDownPointAmount().multiply(exchangeRateMap.get(a.getCurrencyId()+"/"+defaultTradeCurrencyId))).reduce(BigDecimal.ZERO,BigDecimal::add).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            //下分笔数合计
            Integer downPointCountAll = reports.stream().map(a->a.getDownPointCount()).reduce(0,Integer::sum);
            //赠送彩金金额合U
            BigDecimal inWinningsAmountU = reports.stream().map(a->a.getInWinningsAmount().multiply(exchangeRateMap.get(a.getCurrencyId()+"/"+defaultTradeCurrencyId))).reduce(BigDecimal.ZERO,BigDecimal::add).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            //赠送彩金笔数合计
            Integer inWinningsCountAll = reports.stream().map(a->a.getInWinningsCount()).reduce(0,Integer::sum);
            //回收彩金金额合U
            BigDecimal outWinningsAmountU = reports.stream().map(a->a.getOutWinningsAmount().multiply(exchangeRateMap.get(a.getCurrencyId()+"/"+defaultTradeCurrencyId))).reduce(BigDecimal.ZERO,BigDecimal::add).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            //回收彩金笔数合计
            Integer outWinningsCountAll = reports.stream().map(a->a.getOutWinningsCount()).reduce(0,Integer::sum);
            //贷款金额合U
            BigDecimal loanAmountU = reports.stream().map(a->a.getLoanAmount().multiply(exchangeRateMap.get(a.getCurrencyId()+"/"+defaultTradeCurrencyId))).reduce(BigDecimal.ZERO,BigDecimal::add).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            //贷款笔数合计
            Integer loanCountAll = reports.stream().map(a->a.getLoanCount()).reduce(0,Integer::sum);
            //免客损贷款金额合U
            BigDecimal loanAmountNoStatisticalU = reports.stream().map(a->a.getLoanAmountNoStatistical().multiply(exchangeRateMap.get(a.getCurrencyId()+"/"+defaultTradeCurrencyId))).reduce(BigDecimal.ZERO,BigDecimal::add).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            //免客损贷款笔数合计
            Integer loanCountNoStatisticalAll = reports.stream().map(a->a.getLoanCountNoStatistical()).reduce(0,Integer::sum);
            //客损合U
            BigDecimal customerLossAmountU = reports.stream().map(a->a.getCustomerLossAmount().multiply(exchangeRateMap.get(a.getCurrencyId()+"/"+defaultTradeCurrencyId))).reduce(BigDecimal.ZERO,BigDecimal::add).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);

            //合计USDT
            CustomerLossReportDetail customerLossReportDetailVo = new CustomerLossReportDetail();
            customerLossReportDetailVo.setCurrencyName("合U");
            customerLossReportDetailVo.setRechargeAmount(rechargeAmountU);
            customerLossReportDetailVo.setRechargeCount(rechargeCountAll);
            customerLossReportDetailVo.setOnlineRechargeAmount(onlineRechargeAmountU);
            customerLossReportDetailVo.setOnlineRechargeCount(onlineRechargeCountAll);
            customerLossReportDetailVo.setWithdrawAmount(withdrawAmountU);
            customerLossReportDetailVo.setWithdrawCount(withdrawCountAll);
            customerLossReportDetailVo.setUpPointAmount(upPointAmountU);
            customerLossReportDetailVo.setUpPointCount(upPointCountAll);
            customerLossReportDetailVo.setDownPointAmount(downPointAmountU);
            customerLossReportDetailVo.setDownPointCount(downPointCountAll);
            customerLossReportDetailVo.setInWinningsAmount(inWinningsAmountU);
            customerLossReportDetailVo.setInWinningsCount(inWinningsCountAll);
            customerLossReportDetailVo.setOutWinningsAmount(outWinningsAmountU);
            customerLossReportDetailVo.setOutWinningsCount(outWinningsCountAll);
            customerLossReportDetailVo.setLoanAmount(loanAmountU);
            customerLossReportDetailVo.setLoanCount(loanCountAll);
            customerLossReportDetailVo.setLoanAmountNoStatistical(loanAmountNoStatisticalU);
            customerLossReportDetailVo.setLoanCountNoStatistical(loanCountNoStatisticalAll);
            customerLossReportDetailVo.setCustomerLossAmount(customerLossAmountU);
            reports.add(customerLossReportDetailVo);
        }

        //加一条总合计放在第一条的位置
        CustomerLossReportNew customerLossReportNew = new CustomerLossReportNew();
        customerLossReportNew.setUserAccount("总合计");
        customerLossReportNew.setAgentId(customerLossReport.getAgentId());
        List<CustomerLossReportDetail> reports = new ArrayList<>();
        customerLossReportNew.setReports(reports);
        for (int i = 0; i < customerLossReports.get(0).getReports().size(); i++) {
            //币种id
            Long currencyId = customerLossReports.get(0).getReports().get(i).getCurrencyId();
            //币种名称
            String currencyName = customerLossReports.get(0).getReports().get(i).getCurrencyName();
            int index = i;
            //充值金额
            BigDecimal rechargeAmount = customerLossReports.stream().map(a -> a.getReports().get(index).getRechargeAmount()).reduce(BigDecimal.ZERO,BigDecimal::add).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            //充值笔数合计
            Integer rechargeCountAll = customerLossReports.stream().map(a -> a.getReports().get(index).getRechargeCount()).reduce(0,Integer::sum);
            //在线支付充值金额
            BigDecimal onlineRechargeAmount = customerLossReports.stream().map(a -> a.getReports().get(index).getOnlineRechargeAmount()).reduce(BigDecimal.ZERO,BigDecimal::add).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            //在线支付充值笔数合计
            Integer onlineRechargeCountAll = customerLossReports.stream().map(a -> a.getReports().get(index).getOnlineRechargeCount()).reduce(0,Integer::sum);
            //提现金额
            BigDecimal withdrawAmount = customerLossReports.stream().map(a -> a.getReports().get(index).getWithdrawAmount()).reduce(BigDecimal.ZERO,BigDecimal::add).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            //提现笔数合计
            Integer withdrawCountAll = customerLossReports.stream().map(a -> a.getReports().get(index).getWithdrawCount()).reduce(0,Integer::sum);
            //上分金额
            BigDecimal upPointAmount = customerLossReports.stream().map(a -> a.getReports().get(index).getUpPointAmount()).reduce(BigDecimal.ZERO,BigDecimal::add).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            //上分笔数合计
            Integer upPointCountAll = customerLossReports.stream().map(a -> a.getReports().get(index).getUpPointCount()).reduce(0,Integer::sum);
            //下分金额
            BigDecimal downPointAmount = customerLossReports.stream().map(a -> a.getReports().get(index).getDownPointAmount()).reduce(BigDecimal.ZERO,BigDecimal::add).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            //下分笔数合计
            Integer downPointCountAll = customerLossReports.stream().map(a -> a.getReports().get(index).getDownPointCount()).reduce(0,Integer::sum);
            //赠送彩金金额
            BigDecimal inWinningsAmount = customerLossReports.stream().map(a -> a.getReports().get(index).getInWinningsAmount()).reduce(BigDecimal.ZERO,BigDecimal::add).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            //赠送彩金笔数合计
            Integer inWinningsCountAll = customerLossReports.stream().map(a -> a.getReports().get(index).getInWinningsCount()).reduce(0,Integer::sum);
            //回收彩金金额
            BigDecimal outWinningsAmount = customerLossReports.stream().map(a -> a.getReports().get(index).getOutWinningsAmount()).reduce(BigDecimal.ZERO,BigDecimal::add).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            //回收彩金笔数合计
            Integer outWinningsCountAll = customerLossReports.stream().map(a -> a.getReports().get(index).getOutWinningsCount()).reduce(0,Integer::sum);
            //贷款金额
            BigDecimal loanAmount = customerLossReports.stream().map(a -> a.getReports().get(index).getLoanAmount()).reduce(BigDecimal.ZERO,BigDecimal::add).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            //贷款笔数合计
            Integer loanCountAll = customerLossReports.stream().map(a -> a.getReports().get(index).getLoanCount()).reduce(0,Integer::sum);
            //贷款金额
            BigDecimal loanAmountNoStatistical = customerLossReports.stream().map(a -> a.getReports().get(index).getLoanAmountNoStatistical()).reduce(BigDecimal.ZERO,BigDecimal::add).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            //贷款笔数合计
            Integer loanCountNoStatisticalAll = customerLossReports.stream().map(a -> a.getReports().get(index).getLoanCountNoStatistical()).reduce(0,Integer::sum);
            //客损
            BigDecimal customerLossAmount = customerLossReports.stream().map(a -> a.getReports().get(index).getCustomerLossAmount()).reduce(BigDecimal.ZERO,BigDecimal::add).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);

            //总合计
            CustomerLossReportDetail customerLossReportDetailVo = new CustomerLossReportDetail();
            customerLossReportDetailVo.setCurrencyName(currencyName);
            customerLossReportDetailVo.setCurrencyId(currencyId);
            customerLossReportDetailVo.setRechargeAmount(rechargeAmount);
            customerLossReportDetailVo.setRechargeCount(rechargeCountAll);
            customerLossReportDetailVo.setOnlineRechargeAmount(onlineRechargeAmount);
            customerLossReportDetailVo.setOnlineRechargeCount(onlineRechargeCountAll);
            customerLossReportDetailVo.setWithdrawAmount(withdrawAmount);
            customerLossReportDetailVo.setWithdrawCount(withdrawCountAll);
            customerLossReportDetailVo.setUpPointAmount(upPointAmount);
            customerLossReportDetailVo.setUpPointCount(upPointCountAll);
            customerLossReportDetailVo.setDownPointAmount(downPointAmount);
            customerLossReportDetailVo.setDownPointCount(downPointCountAll);
            customerLossReportDetailVo.setInWinningsAmount(inWinningsAmount);
            customerLossReportDetailVo.setInWinningsCount(inWinningsCountAll);
            customerLossReportDetailVo.setOutWinningsAmount(outWinningsAmount);
            customerLossReportDetailVo.setOutWinningsCount(outWinningsCountAll);
            customerLossReportDetailVo.setLoanAmount(loanAmount);
            customerLossReportDetailVo.setLoanCount(loanCountAll);
            customerLossReportDetailVo.setLoanAmountNoStatistical(loanAmountNoStatistical);
            customerLossReportDetailVo.setLoanCountNoStatistical(loanCountNoStatisticalAll);
            customerLossReportDetailVo.setCustomerLossAmount(customerLossAmount);
            reports.add(customerLossReportDetailVo);
        }
        customerLossReports.add(0,customerLossReportNew);
        return customerLossReports;
    }
}
