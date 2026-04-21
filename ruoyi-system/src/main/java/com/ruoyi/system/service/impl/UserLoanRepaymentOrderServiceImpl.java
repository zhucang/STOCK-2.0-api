package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.entity.UserInfo;
import com.ruoyi.common.exception.LangException;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.CodeUtils;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.mapper.UserInfoMapper;
import com.ruoyi.system.mapper.UserLoanRepaymentOrderMapper;
import com.ruoyi.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户贷款还款订单Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-05-28
 */
@Service
public class UserLoanRepaymentOrderServiceImpl implements IUserLoanRepaymentOrderService 
{
    @Resource
    private UserLoanRepaymentOrderMapper userLoanRepaymentOrderMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Autowired
    private IRechargeChannelConfigService rechargeChannelConfigService;

    @Autowired
    private IPlatformCurrencyService platformCurrencyService;

    @Autowired
    private ILoanOrderService loanOrderService;

    @Autowired
    private IAgentTeamLevelLineService agentTeamLevelLineService;

    @Resource
    private SysUserMapper sysUserMapper;

    /**
     * 查询用户贷款还款订单
     * 
     * @param id 用户贷款还款订单主键
     * @return 用户贷款还款订单
     */
    @Override
    public UserLoanRepaymentOrder selectUserLoanRepaymentOrderById(Long id)
    {
        return userLoanRepaymentOrderMapper.selectUserLoanRepaymentOrderById(id);
    }

    /**
     * 查询用户贷款还款订单列表
     * 
     * @param userLoanRepaymentOrder 用户贷款还款订单
     * @return 用户贷款还款订单
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u", isUserInfo = true)
    public List<UserLoanRepaymentOrder> selectUserLoanRepaymentOrderList(UserLoanRepaymentOrder userLoanRepaymentOrder)
    {
        return userLoanRepaymentOrderMapper.selectUserLoanRepaymentOrderList(userLoanRepaymentOrder);
    }

    /**
     * 获取统计数据
     * @param userLoanRepaymentOrder
     * @return
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u", isUserInfo = true)
    public List<UserLoanRepaymentOrder> getStatisticalData(UserLoanRepaymentOrder userLoanRepaymentOrder){
        return userLoanRepaymentOrderMapper.getStatisticalData(userLoanRepaymentOrder);
    }

    /**
     * 填充其他信息
     * @param userLoanRepaymentOrders 用户贷款还款订单
     */
    @Override
    public void fillOtherInfo(List<UserLoanRepaymentOrder> userLoanRepaymentOrders){
        fillAgentLine(userLoanRepaymentOrders);
    }

    /**
     * 填充代理线
     * @param userLoanRepaymentOrders 用户贷款还款订单
     *      */

    public void fillAgentLine(List<UserLoanRepaymentOrder> userLoanRepaymentOrders){
    //用户的代理集合
    List<Long> agentIds = userLoanRepaymentOrders.stream().map(UserLoanRepaymentOrder::getAgentId).distinct().collect(Collectors.toList());
    //取这些代理各自的最高级别代理
    List<AgentTeamLevelLine> agentTeamLevelLines = agentTeamLevelLineService.selectMaxLevelAgentTeamLevelLineByUserIds(agentIds);
    //上级团队信息map
    Map<Long, AgentTeamLevelLine> agentTeamMap = agentTeamLevelLines.stream().collect(Collectors.toMap(a->a.getUserId(), a->a));
    //获取这些代理的信息
    agentIds.addAll(agentTeamLevelLines.stream().map(AgentTeamLevelLine::getSupUserId).distinct().collect(Collectors.toList()));
    SysUser sysUser = new SysUser();
    sysUser.getParams().put("userIds",agentIds);
    sysUser.getParams().put("agentData",1);
    Map<Long, SysUser> agentUsersMap = sysUserMapper.selectUserList(sysUser).stream().collect(Collectors.toMap(a -> a.getUserId(), a -> a));
    //遍历
    for (int i = 0; i < userLoanRepaymentOrders.size(); i++) {
        //用户贷款还款订单信息
        UserLoanRepaymentOrder userLoanRepaymentOrder = userLoanRepaymentOrders.get(i);
        //代理id
        Long agentId = userLoanRepaymentOrder.getAgentId();
        //代理线
        if (agentUsersMap.get(agentId) == null){
            //如果代理信息不存在
            continue;
        }
        String agentLine = agentUsersMap.get(agentId).getUserName();
        //代理的上级信息
        AgentTeamLevelLine line = agentTeamMap.get(agentId);
        if (line != null){
            SysUser agentUser = agentUsersMap.get(line.getSupUserId());
            if (agentUser != null){
                agentLine = agentUser.getUserName() + "——" + agentLine;
            }
        }
        userLoanRepaymentOrder.setAgentName(agentLine);
    }
}

    /**
     * 新增用户贷款还款订单
     * 
     * @param userLoanRepaymentOrder 用户贷款还款订单
     * @return 结果
     */
    @Override
    public int insertUserLoanRepaymentOrder(UserLoanRepaymentOrder userLoanRepaymentOrder)
    {
        userLoanRepaymentOrder.setCreateTime(DateUtils.getNowDate());
        return userLoanRepaymentOrderMapper.insertUserLoanRepaymentOrder(userLoanRepaymentOrder);
    }

    /**
     * 修改用户贷款还款订单
     * 
     * @param userLoanRepaymentOrder 用户贷款还款订单
     * @return 结果
     */
    @Override
    public int updateUserLoanRepaymentOrder(UserLoanRepaymentOrder userLoanRepaymentOrder)
    {
        userLoanRepaymentOrder.setUpdateTime(DateUtils.getNowDate());
        return userLoanRepaymentOrderMapper.updateUserLoanRepaymentOrder(userLoanRepaymentOrder);
    }

    /**
     * 批量删除用户贷款还款订单
     * 
     * @param ids 需要删除的用户贷款还款订单主键
     * @return 结果
     */
    @Override
    public int deleteUserLoanRepaymentOrderByIds(Long[] ids)
    {
        return userLoanRepaymentOrderMapper.deleteUserLoanRepaymentOrderByIds(ids);
    }

    /**
     * 删除用户贷款还款订单信息
     * 
     * @param id 用户贷款还款订单主键
     * @return 结果
     */
    @Override
    public int deleteUserLoanRepaymentOrderById(Long id)
    {
        return userLoanRepaymentOrderMapper.deleteUserLoanRepaymentOrderById(id);
    }

    /**
     * 贷款还款订单审核
     * @param loanRepaymentOrderId 贷款还款订单id
     * @param orderStatus 订单状态
     * @param message 返回信息
     * @param remark 备注
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateLoanRepaymentOrderStatus(Long loanRepaymentOrderId,Integer orderStatus,String message,String remark){
        //还款订单信息
        UserLoanRepaymentOrder userLoanRepaymentOrder = userLoanRepaymentOrderMapper.selectUserLoanRepaymentOrderById(loanRepaymentOrderId);
        if (userLoanRepaymentOrder == null){
            throw new ServiceException("获取还款订单信息异常");
        }
        if (!userLoanRepaymentOrder.getOrderStatus().equals(0)){
            throw new ServiceException("该还款订单已审核过，无需重复操作");
        }
        //通过
        if (orderStatus.equals(1)){
            //贷款订单id
            Long loanOrderId = userLoanRepaymentOrder.getLoanOrderId();
            LoanOrder loanOrder = loanOrderService.selectLoanOrderById(loanOrderId);
            if (loanOrder == null){
                throw new ServiceException("获取关联的贷款订单信息异常");
            }
            if (!loanOrder.getOrderStatus().equals(1)){
                throw new ServiceException("所关联的贷款订单非进行中");
            }
            //贷款订单状态设置为已完成
            loanOrder.setOrderStatus(2);
            //更新贷款订单
            int updateLoanOrder = loanOrderService.updateLoanOrder(loanOrder);
            if (updateLoanOrder <= 0){
                throw new ServiceException("系统繁忙");
            }
        }else if (orderStatus.equals(2)){
            //驳回

        }

        //更新用户还款订单
        userLoanRepaymentOrder.setOrderStatus(orderStatus);
        userLoanRepaymentOrder.setUpdateTime(new Date());
        userLoanRepaymentOrder.setOperatorName(SecurityUtils.getUsername());
        userLoanRepaymentOrder.setRechargeMsg(message);
        userLoanRepaymentOrder.setRemark(remark);
        int updateUserLoanRepaymentOrder = userLoanRepaymentOrderMapper.updateUserLoanRepaymentOrder(userLoanRepaymentOrder);
        if (updateUserLoanRepaymentOrder <= 0){
            throw new ServiceException("系统繁忙");
        }
        return 1;
    }

    /**
     * 用户贷款还款
     * @param userLoanRepaymentOrder
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int userLoanRepayment(UserLoanRepaymentOrder userLoanRepaymentOrder){
        //用户id
        Long userId = userLoanRepaymentOrder.getUserId();
        //用户信息
        UserInfo userInfo = userInfoMapper.selectUserInfoById(userId);
        if (userInfo == null){
            throw new LangException(HintConstants.SYSTEM_BUSY,"获取贷款订单信息异常");
        }
        //还款的支付渠道信息
        RechargeChannelConfig rechargeChannelConfig = rechargeChannelConfigService.selectRechargeChannelConfigById(userLoanRepaymentOrder.getPayChannelId());
        if (rechargeChannelConfig == null){
            throw new LangException("hint_getSitePayInfoError","获取该支付渠道信息异常，请稍后重新尝试");
        }
        if (!rechargeChannelConfig.getStatus().equals(0)){
            throw new LangException("hint_29","此支付渠道已经弃用，请尝试其他充值渠道");
        }
        //币种id
        Long currencyId = rechargeChannelConfig.getCurrencyId();
        //币种信息
        PlatformCurrency platformCurrency = platformCurrencyService.selectPlatformCurrencyById(currencyId);
        if (platformCurrency == null){
            throw new LangException(HintConstants.SYSTEM_BUSY,"获取币种信息异常");
        }
        if (!platformCurrency.getStatus().equals(0)){
            throw new LangException(HintConstants.SYSTEM_BUSY,"此币种已禁用");
        }
        //日志记录币种名称
        HttpUtils.getRequestLogParams().put("currencyName",platformCurrency.getCurrencyName());
        //贷款订单id
        Long loanOrderId = userLoanRepaymentOrder.getLoanOrderId();
        //贷款订单信息
        LoanOrder loanOrder = loanOrderService.selectLoanOrderById(loanOrderId);
        if (loanOrder == null){
            throw new LangException(HintConstants.SYSTEM_BUSY,"获取贷款订单信息异常");
        }
        //搜索该贷款订单是否有正在审核的还款记录
        UserLoanRepaymentOrder search = new UserLoanRepaymentOrder();
        search.setLoanOrderId(loanOrderId);
        search.setOrderStatus(0);
        if (userLoanRepaymentOrderMapper.selectUserLoanRepaymentOrderList(search).size() > 0){
            throw new LangException("hint_79","该贷款订单有正在审核的还款订单");
        }
        //日志记录贷款订单号
        HttpUtils.getRequestLogParams().put("loanOrderCode",loanOrder.getOrderCode());
        //校验身份
        if (!loanOrder.getUserId().equals(userId)){
            throw new ServiceException("校验用户信息异常", HttpStatus.UNAUTHORIZED);
        }
        //还款本金
        BigDecimal realLoanAmount = loanOrder.getRealLoanAmount();
        //日志记录还款本金
        HttpUtils.getRequestLogParams().put("loanOrderPrice",realLoanAmount);
        //需还利息
        BigDecimal needPayInterest = loanOrder.getNeedPayInterest();
        //日志记录需还利息
        HttpUtils.getRequestLogParams().put("needPayInterest",needPayInterest);

        //贷款还款订单信息
        userLoanRepaymentOrder.setOrderCode(CodeUtils.generateOrderCode("LoanRepayment"));
        userLoanRepaymentOrder.setOrderAmount(realLoanAmount.add(needPayInterest));
        userLoanRepaymentOrder.setOrderStatus(0);
        userLoanRepaymentOrder.setCreateTime(new Date());
        userLoanRepaymentOrder.setUpdateTime(null);
        userLoanRepaymentOrder.setOperatorName(null);
        userLoanRepaymentOrder.setPayChannelName(rechargeChannelConfig.getChannelName());
        userLoanRepaymentOrder.setCurrencyId(currencyId);
        userLoanRepaymentOrder.setRechargeMsg(null);
        userLoanRepaymentOrder.setRechargeMethod(0);
        userLoanRepaymentOrder.setRemark(null);
        //新增贷款还款订单
        int insertCount = userLoanRepaymentOrderMapper.insertUserLoanRepaymentOrder(userLoanRepaymentOrder);
        if (insertCount <= 0) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
        //日志记录充值订单信息
        HttpUtils.getRequestLogParams().put("userLoanRepaymentOrder", JSONObject.toJSONString(userLoanRepaymentOrder));
        return 1;
    }
}
