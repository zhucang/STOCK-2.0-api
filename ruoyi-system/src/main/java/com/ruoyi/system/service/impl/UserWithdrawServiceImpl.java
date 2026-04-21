package com.ruoyi.system.service.impl;

import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.entity.UserAmount;
import com.ruoyi.common.core.domain.entity.UserInfo;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.LangException;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.service.TokenService;
import com.ruoyi.common.utils.CodeUtils;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.mapper.UserInfoMapper;
import com.ruoyi.system.mapper.UserWithdrawMapper;
import com.ruoyi.system.service.*;
import com.ruoyi.system.utils.cache.CacheUtils;
import com.ruoyi.system.utils.telegram.TelegramUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 提现记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-10-30
 */
@Service
public class UserWithdrawServiceImpl implements IUserWithdrawService
{
    @Resource
    private UserWithdrawMapper userWithdrawMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Autowired
    private IUserAmountService userAmountService;

    @Autowired
    private IUserBillDetailService userBillDetailService;

    @Autowired
    private ISwitchSetService switchSetService;

    @Autowired
    private IWithdrawChannelConfigService withdrawChannelConfigService;

    @Autowired
    private IUserBankService userBankService;

    @Autowired
    private IUserWalletAddressService userWalletAddressService;

    @Autowired
    private IAgentTeamLevelLineService agentTeamLevelLineService;

    @Resource
    private SysUserMapper sysUserMapper;

    @Autowired
    private IPlatformCurrencyService platformCurrencyService;

    @Autowired
    private ILoanOrderService loanOrderService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private TokenService tokenService;

    /**
     * 查询提现记录
     * 
     * @param id 提现记录主键
     * @return 提现记录
     */
    @Override
    public UserWithdraw selectUserWithdrawById(Long id)
    {
        return userWithdrawMapper.selectUserWithdrawById(id);
    }

    /**
     * 查询提现记录列表
     * 
     * @param userWithdraw 提现记录
     * @return 提现记录
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u", isUserInfo = true)
    public List<UserWithdraw> selectUserWithdrawList(UserWithdraw userWithdraw)
    {
        return userWithdrawMapper.selectUserWithdrawList(userWithdraw);
    }

    /**
     * 获取统计数据
     * @param userWithdraw
     * @return
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u", isUserInfo = true)
    public List<UserWithdraw> getStatisticalData(UserWithdraw userWithdraw){
        return userWithdrawMapper.getStatisticalData(userWithdraw);
    }

    /**
     * 填充其他信息
     * @param userWithdraws 用户提现订单列表
     */
    @Override
    public void fillOtherInfo(List<UserWithdraw> userWithdraws){
        fillAgentLine(userWithdraws);
    }

    /**
     * 填充代理线
     * @param userWithdraws 用户提现订单列表
     */

    public void fillAgentLine(List<UserWithdraw> userWithdraws){
        //用户的代理集合
        List<Long> agentIds = userWithdraws.stream().map(UserWithdraw::getAgentId).distinct().collect(Collectors.toList());
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
        for (int i = 0; i < userWithdraws.size(); i++) {
            //用户充值订单信息
            UserWithdraw userWithdraw = userWithdraws.get(i);
            //代理id
            Long agentId = userWithdraw.getAgentId();
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
            userWithdraw.setAgentName(agentLine);
        }
    }

    /**
     * 查询提现记录列表
     *
     * @param userWithdraw 提现记录
     * @return 提现记录集合
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u", isUserInfo = true)
    public List<UserWithdraw> selectUserWithdrawListWithOthers(UserWithdraw userWithdraw){
        return userWithdrawMapper.selectUserWithdrawListWithOthers(userWithdraw);
    }

    /**
     * 新增提现记录
     * 
     * @param userWithdraw 提现记录
     * @return 结果
     */
    @Override
    public int insertUserWithdraw(UserWithdraw userWithdraw)
    {
        return userWithdrawMapper.insertUserWithdraw(userWithdraw);
    }

    /**
     * 修改提现记录
     * 
     * @param userWithdraw 提现记录
     * @return 结果
     */
    @Override
    public int updateUserWithdraw(UserWithdraw userWithdraw)
    {
        return userWithdrawMapper.updateUserWithdraw(userWithdraw);
    }

    /**
     * 批量删除提现记录
     * 
     * @param ids 需要删除的提现记录主键
     * @return 结果
     */
    @Override
    public int deleteUserWithdrawByIds(Long[] ids)
    {
        return userWithdrawMapper.deleteUserWithdrawByIds(ids);
    }

    /**
     * 删除提现记录信息
     * 
     * @param id 提现记录主键
     * @return 结果
     */
    @Override
    public int deleteUserWithdrawById(Long id)
    {
        return userWithdrawMapper.deleteUserWithdrawById(id);
    }

    /**
     * 提现订单审核
     * @param userWithdrawId 提现订单id
     * @param orderStatus 状态 1：审核通过 2：审核驳回
     * @param withdrawMsg 驳回原因
     * @param remark 备注
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateWithdrawOrderStatus(Long userWithdrawId, Integer orderStatus,String withdrawMsg,String remark){
        //用户提现记录信息
        UserWithdraw userWithdraw = userWithdrawMapper.selectUserWithdrawById(userWithdrawId);
        if (userWithdraw == null) {
            throw new ServiceException("获取提现订单信息异常");
        }
        //检验订单是否是审核中
        if (!userWithdraw.getWithdrawStatus().equals(0)) {
            throw new ServiceException("提现订单已处理，不要重复操作");
        }
        //用户id
        Long userId = userWithdraw.getUserId();
        //日志记录用户id
        HttpUtils.getRequestLogParams().put("userId",userId);
        //用户信息
        UserInfo userInfo = userInfoMapper.selectUserInfoById(userId);
        if (userInfo == null){
            throw new ServiceException("获取用户信息异常");
        }
        //提现金额
        BigDecimal withdrawAmount = userWithdraw.getWithdrawAmount();
        //币种id
        Long currencyId = userWithdraw.getCurrencyId();
        //用户钱包信息
        UserAmount userAmount = userAmountService.getUserAmount(userId,currencyId);
        //如果审核驳回
        if (orderStatus.equals(2)) {
//            if (StringUtils.isEmpty(withdrawMsg)){
//                return AjaxResult.error("请填写驳回原因");
//            }
            //余额变更前
            BigDecimal userAmountBefore = userAmount.getAmount();
            //实际到账金额
            BigDecimal receivedAmount = userWithdraw.getReceivedAmount();
            //提现手续费
            BigDecimal withdrawFee = userWithdraw.getWithdrawFee();
            //如果提现金额与时间到账金额相等
            if (withdrawAmount.compareTo(receivedAmount) == 0){
                //如果有手续费，则返还手续费
                withdrawAmount = withdrawAmount.add(withdrawFee);
            }
            //余额变更后
            BigDecimal userAmountAfter = userAmountBefore.add(withdrawAmount);
            userAmount.setAmount(userAmountAfter);
            //提现驳回明细
            UserBillDetail userBillDetail = new UserBillDetail();
            userBillDetail.setUserId(userId);
            userBillDetail.setDeType("出金返还");
            userBillDetail.setDeSummary("提现申请被驳回，金额返还");
            userBillDetail.setOrderAmount(withdrawAmount);
            userBillDetail.setOrderTime(new Date());
            userBillDetail.setAmountBefore(userAmountBefore);
            userBillDetail.setAmountAfter(userAmountAfter);
            userBillDetail.setRelateOrderId(userWithdrawId);
            userBillDetail.setOrderClass(14);
            userBillDetail.setCurrencyId(userAmount.getCurrencyId());
            int insertUserBillDetail = userBillDetailService.insertUserBillDetail(userBillDetail);
            if (insertUserBillDetail <= 0) {
                throw new ServiceException("系统繁忙");
            }
        }
        //更新钱包信息
        userAmount.setFrozenAmount(userAmount.getFrozenAmount().subtract(userWithdraw.getReceivedAmount().add(userWithdraw.getWithdrawFee())));
        int updateUserAmount = userAmountService.updateUserAmount(userAmount);
        if (updateUserAmount <= 0) {
            throw new ServiceException("系统繁忙");
        }
        //更新提现订单信息
        userWithdraw.setWithdrawStatus(orderStatus);
        userWithdraw.setTransTime(new Date());
        userWithdraw.setOperatorName(SecurityUtils.getUsername());
        userWithdraw.setWithdrawMsg(withdrawMsg);
        userWithdraw.setRemark(remark);
        int updateCount = userWithdrawMapper.updateUserWithdraw(userWithdraw);
        if (updateCount <= 0) {
            throw new ServiceException("系统繁忙");
        }
        //日志记录订单信息
        HttpUtils.getRequestLogParams().put("userWithdraw",JSONObject.toJSONString(userWithdraw));
        return 1;
    }

    /**
     * 修改订单备注
     * @param userWithdrawId 提现订单id
     * @param remark 备注
     * @return
     */
    @Override
    public AjaxResult updateOrderRemark(Long userWithdrawId, String remark) {
        // 提现订单信息
        UserWithdraw userWithdraw = userWithdrawMapper.selectUserWithdrawById(userWithdrawId);
        if (userWithdraw == null) {
            throw new ServiceException("获取订单信息异常");
        }
        userWithdraw.setRemark(remark);
        int count = userWithdrawMapper.updateUserWithdraw(userWithdraw);
        if (count <= 0) {
            throw new ServiceException("系统繁忙");
        }
        //日志记录提现订单信息
        HttpUtils.getRequestLogParams().put("userWithdraw",JSONObject.toJSONString(userWithdraw));
        return AjaxResult.success();
    }

    /**
     * 修改提现订单是否免客损状态
     * @param userWithdrawId 提现订单id
     * @param statisticalReport 是否统计报表 0：是 1：否
     * @return
     */
    @Override
    public int updateStatisticalReport(Long userWithdrawId, Integer statisticalReport){
        UserWithdraw userWithdraw = new UserWithdraw();
        userWithdraw.setId(userWithdrawId);
        userWithdraw.setStatisticalReport(statisticalReport);
        int updateUserWithdraw = userWithdrawMapper.updateUserWithdraw(userWithdraw);
        if (updateUserWithdraw <= 0){
            throw new ServiceException("系统繁忙");
        }
        return 1;
    }

    /**
     * 获取用户的各币种的提现金额
     * @param userId 用户id
     * @return
     */
    @Override
    public List<UserWithdraw> selectUserWithdrawAmountAllCurrency(Long userId){
        return userWithdrawMapper.selectUserWithdrawAmountAllCurrency(userId);
    }

    /**
     * 用户提现
     * @param withdrawAmount 提现金额
     * @param withPwd 提现密码
     * @param withdrawAddressId 提现地址id (钱包id或银行卡id)
     * @param withdrawChannelConfigId 提现通道id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int outMoney(BigDecimal withdrawAmount, String withPwd, Long withdrawAddressId, Long withdrawChannelConfigId){
        //用户id
        Long userId = SecurityUtils.getUserId();
        //用户信息
        UserInfo userInfo = userInfoMapper.selectUserInfoById(userId);
        if (userInfo == null){
            throw new ServiceException("获取用户信息异常", HttpStatus.UNAUTHORIZED);
        }
        //如果被禁止提现
        if (!userInfo.getIsCanWithdraw().equals(0)){
            throw new LangException("hint_43","用户被禁止提现");
        }
        if (userInfo.getAccountType().equals(2)){
            throw new LangException("hint_91","模拟账号禁止提现");
        }
        //提现是否要求打码量达到要求
        Integer selectSwitchStatusById82 = switchSetService.selectSwitchStatusById(82L);
        if (selectSwitchStatusById82.equals(0)){
            if (userInfo.getNeedOrderAmount().compareTo(BigDecimal.ZERO) > 0){
                List<Object> param = new ArrayList<>();
                param.add(userInfo.getNeedOrderAmount());
                throw new LangException("hint_44", param, "还需" + userInfo.getNeedOrderAmount() + "打码量");
            }
        }
        //用户是否可以提现开关
        Integer allowedWithdrawSwitch = switchSetService.selectSwitchStatusById(43L);
        if (!allowedWithdrawSwitch.equals(0)){
            throw new LangException("hint_43","当前无法提现");
        }

        //提现通道信息
        WithdrawChannelConfig withdrawChannelConfig = withdrawChannelConfigService.selectWithdrawChannelConfigById(withdrawChannelConfigId);
        if (withdrawChannelConfig == null){
            throw new LangException(HintConstants.SYSTEM_BUSY,"获取提现通道信息异常");
        }
        //日志记录通道名称
        HttpUtils.getRequestLogParams().put("channelName",withdrawChannelConfig.getChannelName());
        //最高提现金额
        BigDecimal withdrawMaxAmount = withdrawChannelConfig.getWithdrawMaxAmount();
        //最低提现金额
        BigDecimal withdrawMinAmount = withdrawChannelConfig.getWithdrawMinAmount();
        List<Object> param = new ArrayList<>();
        param.add(withdrawMinAmount);
        param.add(withdrawMaxAmount);
        if (withdrawAmount.compareTo(withdrawMaxAmount) > 0 || withdrawAmount.compareTo(withdrawMinAmount) < 0){
            throw new LangException("hint_withdrawAmountLimit",param,"提现金额范围为"+withdrawMinAmount+"~"+withdrawMaxAmount);
        }
        //用户提现密码
        String userWithPwd = userInfoMapper.selectUserWithPwdByUserId(userId);
        //验证密码
        //密码错误，则记录错误次数
        String mapKey = "withdrawPwdErrorCount:";
        //密码错误
        if (!SecurityUtils.matchesPassword(withPwd,userWithPwd)){
            //获取已登录错误次数
            Integer withdrawPwdErrorCount = redisCache.getCacheMapValue(mapKey,String.valueOf(userId));
            //记录登录错误次数
            if (withdrawPwdErrorCount == null){
                redisCache.setCacheMapValue(mapKey,String.valueOf(userId),1);
            }else {
                //已经错误次数
                Integer alreadyWrongCount = withdrawPwdErrorCount + 1;
                //提现最多允许错误次数
                Integer maxWithdrawErrorCount = CacheUtils.getOtherValueByKey("max_withdraw_error_count", Integer.class);
                //如果错误次数超过允许错误的次数
                if (alreadyWrongCount >= maxWithdrawErrorCount){
                    ThreadUtil.execute(()->{
                        //禁用相关账号
                        UserInfo userInfoVo = new UserInfo();
                        userInfoVo.setId(userId);
                        userInfoVo.setStatus(1);
                        int updateUserInfo = userInfoMapper.updateUserInfo(userInfoVo);
                        if (updateUserInfo <= 0){
                            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
                        }
                        redisCache.deleteCacheMapValue(mapKey,String.valueOf(userInfo.getId()));
                        //踢下线
                        //token标识
                        String tokenFlag = "app";
                        //此用户当前登录的token
                        String tokenCurrent = redisCache.getCacheMapValue(tokenFlag, String.valueOf(userId));
                        //删除登录信息
                        tokenService.delLoginUser(tokenCurrent);
                    });
                }else {
                    redisCache.setCacheMapValue(mapKey,String.valueOf(userInfo.getId()),alreadyWrongCount);
                }
            }
            throw new LangException("hint_withdrawPwdIsWrong","提现密码错误");
        }else {
            redisCache.deleteCacheMapValue(mapKey,String.valueOf(userId));
        }



        //提现信用分要求
        Integer creditScoreWithdrawLimit = CacheUtils.getOtherValueByKey("credit_score_withdraw_limit",Integer.class);
        if (userInfo.getCreditScore() < creditScoreWithdrawLimit){
            param.clear();
            param.add(creditScoreWithdrawLimit);
            throw new LangException("hint_48",param,"提现需要信用分达到"+creditScoreWithdrawLimit);
        }

        //是否开启初级实名认证
        Integer selectSwitchStatusById68 = switchSetService.selectSwitchStatusById(68L);
        //如果初级认证开启
        if (selectSwitchStatusById68.equals(0)){
            //完成初级实名认证才能提现
            Integer selectSwitchStatusById69 = switchSetService.selectSwitchStatusById(69L);
            if (selectSwitchStatusById69.equals(0) && !userInfo.getAuthStatusJunior().equals(2)){
                throw new LangException(HintConstants.AUTH_FIRST_JUNIOR,"请先完成初级实名认证");
            }
        }
        //是否开启高级实名认证
        Integer selectSwitchStatusById75 = switchSetService.selectSwitchStatusById(75L);
        //如果高级认证开启
        if (selectSwitchStatusById75.equals(0)) {
            //完成高级实名认证才能提现
            Integer selectSwitchStatusById71 = switchSetService.selectSwitchStatusById(71L);
            if (selectSwitchStatusById71.equals(0) && !userInfo.getAuthStatusSenior().equals(2)){
                throw new LangException(HintConstants.AUTH_FIRST_SENIOR,"请先完成高级实名认证");
            }
        }
        //有持仓单不允许提现开关
        Integer havePositionNotAllowWithdrawSwitch = switchSetService.selectSwitchStatusById(12L);
        if (havePositionNotAllowWithdrawSwitch.equals(0)){
            //用户持仓单数量
            int userHoldingPositionNum = userWithdrawMapper.getUserHoldingPositionNumByUserId(userId);
            if (userHoldingPositionNum > 0) {
                throw new LangException("hint_havePositionOrderCanNotWithdraw","有持仓单不能出金");
            }
        }
        //提现开始时间
        String withdrawStartTime = withdrawChannelConfig.getWithdrawStartTime();
        //提现结束时间
        String withdrawEndTime = withdrawChannelConfig.getWithdrawEndTime();
        //如果不在提现时间内
        if (!DateUtils.checkIsWithTime(withdrawStartTime, withdrawEndTime)) {
            param.clear();
            param.add(withdrawStartTime);
            param.add(withdrawEndTime);
            throw new LangException("hint_withdrawTimeLimit",param,"出金时间在"+withdrawStartTime+"~"+withdrawEndTime+"之间");
        }
        //有未审核提现订单不允许提现开关
        Integer haveNotReviewedWithOrderNotAllowWith = switchSetService.selectSwitchStatusById(13L);
        if (haveNotReviewedWithOrderNotAllowWith.equals(0)){
            //未审核订单数量
            int notReviewedWithOrderCount = userWithdrawMapper.getUserWithdrawPendingReviewNumByUserId(userId);
            if (notReviewedWithOrderCount > 0 ){
                throw new LangException("hint_haveNotReviewedWithOrderNotAllowWith","还有未审核提现订单，无法继续提现");
            }
        }
        //贷款未结清前是否禁止提现
        Integer switchStatus91 = switchSetService.selectSwitchStatusById(91L);
        if (switchStatus91.equals(0)){
            LoanOrder loanOrder = new LoanOrder();
            loanOrder.setUserId(userId);
            loanOrder.setOrderStatus(1);
            //未结清的贷款订单
            List<LoanOrder> loanOrders = loanOrderService.selectLoanOrderList(loanOrder);
            //未结清的贷款金额
            BigDecimal loanAmount = loanOrders.stream().map(a -> a.getRealLoanAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
            //待还利息
            BigDecimal needPayInterest = loanOrders.stream().map(a -> a.getNeedPayInterest()).reduce(BigDecimal.ZERO, BigDecimal::add);
            if (loanAmount.compareTo(BigDecimal.ZERO) > 0){
                param.clear();
                param.add(loanAmount.stripTrailingZeros().toPlainString());
                param.add(needPayInterest.stripTrailingZeros().toPlainString());
                throw new LangException("hint_77", param, "还有未结清的贷款金额" + loanAmount.stripTrailingZeros().toPlainString() + "以及待还利息" + needPayInterest.stripTrailingZeros().toPlainString() + "，无法继续提现");
            }
        }
        //币种id
        Long currencyId = withdrawChannelConfig.getCurrencyId();
        //币种信息
        PlatformCurrency platformCurrency = platformCurrencyService.selectPlatformCurrencyById(currencyId);
        if (platformCurrency == null){
            throw new LangException(HintConstants.SYSTEM_BUSY,"获取币种信息异常");
        }
        //日志记录币种名称
        HttpUtils.getRequestLogParams().put("currencyName",platformCurrency.getCurrencyName());
        //每日可提现次数
        int withCountEveryday = withdrawChannelConfig.getWithdrawCount();
        //今日已提现次数
        int withCountToday = userWithdrawMapper.getWithCountTodayByUserId(userId, new Date(),currencyId);
        if (withCountToday >= withCountEveryday){
            List<Object> params = new ArrayList<>();
            params.add(withCountEveryday);
            throw new LangException("hint_dailyWithdrawalLimit",params,"该通道每日最多提现"+withCountEveryday+"次");
        }
        //提现订单信息
        UserWithdraw userWithdraw = new UserWithdraw();
        //收款账户信息
        JSONObject receiptAccountInfo = new JSONObject();
        //如果提现类型是银行卡
        if (withdrawChannelConfig.getWithdrawType().equals(0)){
            //提现的银行卡信息
            UserBank userBank = userBankService.selectUserBankById(withdrawAddressId);
            if (userBank == null){
                throw new LangException(HintConstants.SYSTEM_BUSY,"获取提现地址信息异常");
            }
            if (!userBank.getUserId().equals(userId)){
                throw new ServiceException("校验用户信息异常", HttpStatus.UNAUTHORIZED);
            }
            receiptAccountInfo.put("银行名称", userBank.getBankName());
            receiptAccountInfo.put("银行账号", userBank.getBankNo());
            receiptAccountInfo.put("开户行", userBank.getBankOpenAddress());
            receiptAccountInfo.put("开户人姓名", userBank.getHolder());
            receiptAccountInfo.put("绑定手机号", userBank.getBankPhone());
            receiptAccountInfo.put("银行国家", userBank.getBankCountry());
            receiptAccountInfo.put("ABA代码", userBank.getAbaCode());
            receiptAccountInfo.put("SWIFT", userBank.getswift());
            receiptAccountInfo.put("邮编号码", userBank.getPostCode());
            receiptAccountInfo.put("用户地址", userBank.getUserRealAddress());
            receiptAccountInfo.put("账户类型", userBank.getAccountType());
            receiptAccountInfo.put("备注", userBank.getRemark());
            //用户提现自动删除提现地址
            Integer switchStatusById126 = switchSetService.selectSwitchStatusById(126L);
            if (switchStatusById126.equals(0)) {
                int deleteUserBankById = userBankService.deleteUserBankById(userBank.getId());
                if (deleteUserBankById == 0) {
                    throw new LangException(HintConstants.SYSTEM_BUSY, "删除用户银行卡异常");
                }
            }
        }else if (withdrawChannelConfig.getWithdrawType().equals(1)){
            //如果是虚拟钱包提现
            UserWalletAddress userWalletAddress = userWalletAddressService.selectUserWalletAddressById(withdrawAddressId);
            if (userWalletAddress == null){
                throw new LangException(HintConstants.SYSTEM_BUSY,"获取提现地址信息异常");
            }
            if (!userWalletAddress.getUserId().equals(userId)){
                throw new ServiceException("校验用户信息异常", HttpStatus.UNAUTHORIZED);
            }
            if (!userWalletAddress.getCurrencyId().equals(withdrawChannelConfig.getArrivalCurrencyId())){
                throw new LangException("hint_95","该钱包币种与通道币种不符");
            }
            if (StringUtils.isNotEmpty(withdrawChannelConfig.getWalletAddressType())){
                if (!withdrawChannelConfig.getWalletAddressType().equals(userWalletAddress.getWalletAddressType())){
                    throw new LangException("hint_95","提现钱包与提现通道的币种类型不符");
                }
            }else {
                if (StringUtils.isNotEmpty(userWalletAddress.getWalletAddressType())){
                    throw new LangException("hint_95","提现钱包与提现通道的币种类型不符");
                }
            }
            //钱包地址类型
            String walletAddressType = StringUtils.isNotEmpty(userWalletAddress.getWalletAddressType()) ? ("(" + userWalletAddress.getWalletAddressType() + ")") : "";
            receiptAccountInfo.put("钱包类型", userWalletAddress.getCurrencyName() + walletAddressType);
            receiptAccountInfo.put("钱包地址", userWalletAddress.getWalletAddress());
            receiptAccountInfo.put("钱包标签", userWalletAddress.getWalletTag());
            userWithdraw.setWalletReceiptQrCode(userWalletAddress.getWalletReceiptQrCode());
            //用户提现自动删除提现地址
            Integer switchStatusById126 = switchSetService.selectSwitchStatusById(126L);
            if (switchStatusById126.equals(0)) {
                int deleteUserWalletAddressById = userWalletAddressService.deleteUserWalletAddressById(userWalletAddress.getId());
                if (deleteUserWalletAddressById == 0) {
                    throw new LangException(HintConstants.SYSTEM_BUSY, "删除用户钱包地址异常");
                }
            }
        }else {
            throw new LangException(HintConstants.SYSTEM_BUSY,"请联系客服");
        }
        //到账币种信息ID
        Long arrivalCurrencyId = withdrawChannelConfig.getArrivalCurrencyId();
        //如果到账币种与扣款币种不同
        if (!currencyId.equals(arrivalCurrencyId)){
            PlatformCurrency arrivalCurrency = platformCurrencyService.selectPlatformCurrencyById(arrivalCurrencyId);
            if (arrivalCurrency == null){
                throw new LangException(HintConstants.SYSTEM_BUSY, "获取币种信息异常");
            }
            receiptAccountInfo.put("到账币种", arrivalCurrency.getCurrencyName());
        }
        //收款账户信息
        userWithdraw.setReceiptAccountInfo(JSONObject.toJSONString(receiptAccountInfo));
        //提现手续费
        BigDecimal withFee = BigDecimal.ZERO;
        //百分比提现手续费
        if (withdrawChannelConfig.getHandingFeeMethod() == 0){
            //手续费率
            BigDecimal handingFeeRate = withdrawChannelConfig.getHandingFeeRate();
            withFee = withdrawAmount.multiply(handingFeeRate).divide(new BigDecimal(100),Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
        }else {
            //固定提现手续费
            //固定金额
            BigDecimal handingFeeFixedAmount = withdrawChannelConfig.getHandingFeeFixedAmount();
            withFee = handingFeeFixedAmount;
        }
        //用户钱包信息
        UserAmount userAmount = userAmountService.getUserAmount(userId, currencyId);
        //余额变更前
        BigDecimal userAmountBefore = userAmount.getAmount();
        //提现总计扣费金额
        BigDecimal withdrawAllAmount = withdrawAmount.add(withFee);
        //是否在实际出金中扣除提现手续费
        Integer switchStatus94 = switchSetService.selectSwitchStatusById(94L);
        if (switchStatus94.equals(0)){
            withdrawAllAmount = withdrawAmount;
            userWithdraw.setReceivedAmount(withdrawAmount.subtract(withFee));
        }else {
            //手续费额外扣除
            userWithdraw.setReceivedAmount(withdrawAmount);
        }
        //判断账户余额是否足够
        if (userAmountBefore.compareTo(withdrawAllAmount) < 0) {
            List<Object> params = new ArrayList<>();
            params.add(withdrawAllAmount);
            throw new LangException("hint_withdrawFailedInsufficientBalance",params,"用户可用资金不足，提现金额合计手续费"+withdrawAllAmount);
        }
        //用户剩余余额 = 之前余额-提现总计扣费
        BigDecimal userAmountAfter = userAmountBefore.subtract(withdrawAllAmount);
        userAmount.setAmount(userAmountAfter);
        userAmount.setFrozenAmount(userAmount.getFrozenAmount().add(withdrawAllAmount));
        int updateUserAmount = userAmountService.updateUserAmount(userAmount);
        if (updateUserAmount <= 0) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }

        userWithdraw.setUserId(userId);
        userWithdraw.setOrderCode(CodeUtils.generateOrderCode("WITHDRAW"));
        userWithdraw.setWithdrawAmount(withdrawAmount);
        userWithdraw.setApplyTime(new Date());
        userWithdraw.setWithdrawStatus(0);
        userWithdraw.setWithdrawFee(withFee);
        userWithdraw.setCurrencyId(withdrawChannelConfig.getCurrencyId());
        userWithdraw.setWithdrawType(withdrawChannelConfig.getWithdrawType());
        userWithdraw.setUserAmtBefore(userAmountBefore);
        userWithdraw.setUserAmtAfter(userAmountAfter);
        int count = userWithdrawMapper.insertUserWithdraw(userWithdraw);
        if (count <= 0) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
        //日志记录提现订单信息
        HttpUtils.getRequestLogParams().put("userWithdraw", JSONObject.toJSONString(userWithdraw));

        //插入账户明细记录
        UserBillDetail userBillDetail = new UserBillDetail();
        userBillDetail.setUserId(userId);
        userBillDetail.setDeType("用户提现");
        userBillDetail.setDeSummary("用户提现申请成功");
        userBillDetail.setOrderAmount(withdrawAmount.negate());
        userBillDetail.setOrderTime(new Date());
        userBillDetail.setAmountBefore(userAmountBefore);
        userBillDetail.setAmountAfter(userAmountBefore.subtract(withdrawAmount));
        userBillDetail.setRelateOrderId(userWithdraw.getId());
        userBillDetail.setOrderClass(1);
        userBillDetail.setCurrencyId(withdrawChannelConfig.getCurrencyId());
        int insertUserBillDetail = userBillDetailService.insertUserBillDetail(userBillDetail);
        if (insertUserBillDetail <= 0) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }

        if (!switchStatus94.equals(0) && withFee.compareTo(BigDecimal.ZERO) > 0){
            //扣除提现手续费
            //插入扣除提现手续费账户明细记录
            UserBillDetail userBillDetail2 = new UserBillDetail();
            userBillDetail2.setUserId(userId);
            userBillDetail2.setDeType("用户提现手续费扣除");
            userBillDetail2.setDeSummary("用户提现手续费扣除");
            userBillDetail2.setOrderAmount(withFee.negate());
            userBillDetail2.setOrderTime(new Date());
            userBillDetail2.setAmountBefore(userAmountBefore.subtract(withdrawAmount));
            userBillDetail2.setAmountAfter(userAmountAfter);
            userBillDetail2.setRelateOrderId(userWithdraw.getId());
            userBillDetail2.setOrderClass(66);
            userBillDetail2.setCurrencyId(withdrawChannelConfig.getCurrencyId());
            int insertUserBillDetail2 = userBillDetailService.insertUserBillDetail(userBillDetail2);
            if (insertUserBillDetail2 <= 0) {
                throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
            }
        }
        //如果是模拟用户
        if (userInfo.getAccountType().equals(1)){
            //自动审核通过开关
            Integer autoAgree = switchSetService.selectSwitchStatusById(45L);
            if (autoAgree != null && autoAgree.equals(0)){
                try{
                    int updateWithdrawOrderStatus = updateWithdrawOrderStatus(userWithdraw.getId(), 1, null, null);
                    if (updateWithdrawOrderStatus <= 0){
                        throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
                    }
                }catch (Exception e){
                    throw new LangException(HintConstants.SYSTEM_BUSY,e.getMessage());
                }
            }
        }

        //如果不是真实用户
        if (!userInfo.getAccountType().equals(0)) {
            //如果是模拟账号，不推消息
            if (userInfo.getAccountType().equals(2)) {
                return 1;
            }
            //telegram通知是否只推送真实用户通知
            Integer switchStatusById123 = switchSetService.selectSwitchStatusById(123L);
            if (switchStatusById123.equals(0)){
                return 1;
            }
        }

        //telegram通知（提现申请）
        Integer switchStatusById128 = switchSetService.selectSwitchStatusById(128L);
        if (switchStatusById128.equals(0)) {
            //telegram消息
            String telegramMsg = "\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89提现申请\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89\n" +
                    "⏰时间：" + userWithdraw.getApplyTime() + "\n" +
                    "ID: " + userInfo.getUserNo() + "\n" +
                    "用户账号: " + userInfo.getUserAccount() + "\n" +
                    "用户昵称: " + userInfo.getNickName() + "\n" +
                    "用户备注: " + userInfo.getRemark() + "\n" +
                    "邀请码: " + userInfo.getInviteCode() + "\n" +
                    "所属代理: " + userInfo.getAgentId() + "/" + userInfo.getAgentName() + "\n" +
                    "代理昵称: " + userInfo.getAgentNickName() + "\n" +
                    "提现金额: " + withdrawAmount.stripTrailingZeros().toPlainString() + platformCurrency.getCurrencyName() + "\n" +
                    "手续费: " + withFee.stripTrailingZeros().toPlainString() + platformCurrency.getCurrencyName() + "\n" +
                    "实际出金: " + userWithdraw.getReceivedAmount().stripTrailingZeros().toPlainString() + platformCurrency.getCurrencyName() + "\n" +
                    "提现地址: " + receiptAccountInfo.toJSONString() + "\n" +
                    "余额变更后: " + userAmountAfter.stripTrailingZeros().toPlainString() + platformCurrency.getCurrencyName() + "\n" +
                    "已申请提现,请及时处理！";
            TelegramUtils.sendAsyncMessage(telegramMsg, "default", "default");
        }
        return 1;
    }

    /**
     * 用户取消提现
     * @param userWithdrawId 提现记录id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int userCancel(Long userWithdrawId){
        //用户id
        Long userId = SecurityUtils.getUserId();
        //是否允许用户取消提现
        Integer allowedCancelWithdrawOrderSwitch = switchSetService.selectSwitchStatusById(29L);
        if (!allowedCancelWithdrawOrderSwitch.equals(0)){
            throw new LangException("hint_34","暂时不允许用户取消提现");
        }
        //提现订单信息
        UserWithdraw userWithdraw = userWithdrawMapper.selectUserWithdrawById(userWithdrawId);
        if (userWithdraw == null) {
            throw new LangException("hint_orderNotExist","订单不存在");
        }
        HttpUtils.getRequestLogParams().put("userWithdraw", JSONObject.toJSONString(userWithdraw));
        if (!userWithdraw.getWithdrawStatus().equals(0)) {
            throw new LangException("hint_orderCanNotCancel","当前订单不能取消");
        }
        //用户信息
        UserInfo userInfo = userInfoMapper.selectUserInfoById(userId);
        //校验用户信息
        if (!userInfo.getId().equals(userWithdraw.getUserId())){
            throw new ServiceException("校验用户信息异常", HttpStatus.UNAUTHORIZED);
        }
        userWithdraw.setWithdrawStatus(3);
        userWithdraw.setWithdrawMsg("用户取消出金");
        int updateCount = userWithdrawMapper.updateUserWithdraw(userWithdraw);
        if (updateCount <= 0) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
        //提现金额
        BigDecimal withdrawAmount = userWithdraw.getWithdrawAmount();
        //实际到账金额
        BigDecimal receivedAmount = userWithdraw.getReceivedAmount();
        //提现手续费
        BigDecimal withdrawFee = userWithdraw.getWithdrawFee();
        //提现总计扣费金额
        BigDecimal withdrawAllAmount = withdrawAmount;
        //如果提现金额与时间到账金额相等
        if (withdrawAmount.compareTo(receivedAmount) == 0){
            //如果有手续费，则返还手续费
            withdrawAllAmount = withdrawAmount.add(withdrawFee);
        }
        //用户钱包信息
        UserAmount userAmount = userAmountService.getUserAmount(userId, userWithdraw.getCurrencyId());
        //余额变更前
        BigDecimal userAmountBefore = userAmount.getAmount();
        //余额变更后
        BigDecimal userAmountAfter = userAmountBefore.add(withdrawAllAmount);
        userAmount.setAmount(userAmountAfter);
        userAmount.setFrozenAmount(userAmount.getFrozenAmount().subtract(withdrawAllAmount));
        //更新余额
        int updateUserAmount = userAmountService.updateUserAmount(userAmount);
        if (updateUserAmount <= 0){
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }

        //取消出金明细
        UserBillDetail userBillDetail = new UserBillDetail();
        userBillDetail.setUserId(userId);
        userBillDetail.setDeType("取消出金返还");
        userBillDetail.setDeSummary("取消出金返还成功");
        userBillDetail.setOrderAmount(withdrawAllAmount);
        userBillDetail.setOrderTime(new Date());
        userBillDetail.setAmountBefore(userAmountBefore);
        userBillDetail.setAmountAfter(userAmountAfter);
        userBillDetail.setRelateOrderId(userWithdrawId);
        userBillDetail.setOrderClass(13);
        userBillDetail.setCurrencyId(userAmount.getCurrencyId());
        int insertUserBillDetail = userBillDetailService.insertUserBillDetail(userBillDetail);
        if (insertUserBillDetail <= 0) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
        return 1;
    }
}
