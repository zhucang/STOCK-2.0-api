package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.entity.UserAmount;
import com.ruoyi.common.core.domain.entity.UserInfo;
import com.ruoyi.common.exception.LangException;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.CodeUtils;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.cache.CacheUtil;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.domain.vo.UserOnlineRechargeOrderVo;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.service.*;
import com.ruoyi.system.utils.cache.CacheUtils;
import com.ruoyi.system.utils.currencyExchangeRate.ExchangeRateUtil;
import com.ruoyi.system.utils.olgpay.OlgpayUtils;
import com.ruoyi.system.utils.telegram.TelegramUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * 用户充值订单Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-10-30
 */
@Service
public class UserRechargeServiceImpl implements IUserRechargeService 
{

    private static Logger log = LoggerFactory.getLogger(UserInfoServiceImpl.class);

    @Resource
    private UserRechargeMapper userRechargeMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Autowired
    private IUserAmountService userAmountService;

    @Autowired
    private IUserBillDetailService userBillDetailService;

    @Autowired
    private IPlatformCurrencyService platformCurrencyService;

    @Autowired
    private IUserVipLevelConfigService userVipLevelConfigService;

    @Resource
    private UserTeamLevelLineMapper userTeamLevelLineMapper;

    @Resource
    private UserCommissionRecordMapper userCommissionRecordMapper;

    @Resource
    private UserRebateRateMapper userRebateRateMapper;

    @Resource
    private RechargeChannelConfigMapper rechargeChannelConfigMapper;

    @Autowired
    private ISwitchSetService switchSetService;

    @Autowired
    private IAgentTeamLevelLineService agentTeamLevelLineService;

    @Resource
    private SysUserMapper sysUserMapper;

    @Autowired
    private IUserWinningsChangeRecordService userWinningsChangeRecordService;
    
    @Autowired
    private IUserDmAmountChangeRecordService userDmAmountChangeRecordService;

    @Autowired
    private ICurrencyExchangeRateService currencyExchangeRateService;

    @Autowired
    private IVipExperienceValueService vipExperienceValueService;

    @Resource
    private RechargeRewardTierMapper rechargeRewardTierMapper;

    @Resource
    private RechargeRewardRecordMapper rechargeRewardRecordMapper;


    /**
     * 查询用户充值订单
     * 
     * @param id 用户充值订单主键
     * @return 用户充值订单
     */
    @Override
    public UserRecharge selectUserRechargeById(Long id)
    {
        return userRechargeMapper.selectUserRechargeById(id);
    }

    /**
     * 查询用户充值订单列表
     * 
     * @param userRecharge 用户充值订单
     * @return 用户充值订单
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u", isUserInfo = true)
    public List<UserRecharge> selectUserRechargeList(UserRecharge userRecharge)
    {
        return userRechargeMapper.selectUserRechargeList(userRecharge);
    }

    /**
     * 获取统计数据
     * @param userRecharge
     * @return
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u", isUserInfo = true)
    public List<UserRecharge> getStatisticalData(UserRecharge userRecharge){
        return userRechargeMapper.getStatisticalData(userRecharge);
    }

    /**
     * 查询用户充值订单列表（含彩金赠送记录)
     *
     * @param userRecharge 用户充值订单
     * @return 用户充值订单集合
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u", isUserInfo = true)
    public List<UserRecharge> selectUserRechargeListWithOthers(UserRecharge userRecharge){
        return userRechargeMapper.selectUserRechargeListWithOthers(userRecharge);
    }

    /**
     * 填充其他信息
     * @param userRecharges 用户充值订单列表
     */
    @Override
    public void fillOtherInfo(List<UserRecharge> userRecharges){
        fillAgentLine(userRecharges);
    }

    /**
     * 填充代理线
     * @param userRecharges 用户充值订单列表
     */

    public void fillAgentLine(List<UserRecharge> userRecharges){
        //用户的代理集合
        List<Long> agentIds = userRecharges.stream().map(UserRecharge::getAgentId).distinct().collect(Collectors.toList());
        //取这些代理各自的最高级别代理
        List<AgentTeamLevelLine> agentTeamLevelLines = agentTeamLevelLineService.selectMaxLevelAgentTeamLevelLineByUserIds(agentIds);
        //上级团队信息map
        Map<Long, AgentTeamLevelLine> agentTeamMap = agentTeamLevelLines.stream().collect(Collectors.toMap(a->a.getUserId(),a->a));
        //获取这些代理的信息
        agentIds.addAll(agentTeamLevelLines.stream().map(AgentTeamLevelLine::getSupUserId).distinct().collect(Collectors.toList()));
        SysUser sysUser = new SysUser();
        sysUser.getParams().put("userIds",agentIds);
        sysUser.getParams().put("agentData",1);
        Map<Long, SysUser> agentUsersMap = sysUserMapper.selectUserList(sysUser).stream().collect(Collectors.toMap(a -> a.getUserId(), a -> a));
        //遍历
        for (int i = 0; i < userRecharges.size(); i++) {
            //用户充值订单信息
            UserRecharge userRecharge = userRecharges.get(i);
            //代理id
            Long agentId = userRecharge.getAgentId();
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
            userRecharge.setAgentName(agentLine);
        }
    }

    /**
     * 新增用户充值订单
     * 
     * @param userRecharge 用户充值订单
     * @return 结果
     */
    @Override
    public int insertUserRecharge(UserRecharge userRecharge)
    {
        userRecharge.setCreateTime(DateUtils.getNowDate());
        return userRechargeMapper.insertUserRecharge(userRecharge);
    }

    /**
     * 修改用户充值订单
     * 
     * @param userRecharge 用户充值订单
     * @return 结果
     */
    @Override
    public int updateUserRecharge(UserRecharge userRecharge)
    {
        return userRechargeMapper.updateUserRecharge(userRecharge);
    }

    /**
     * 批量删除用户充值订单
     * 
     * @param ids 需要删除的用户充值订单主键
     * @return 结果
     */
    @Override
    public int deleteUserRechargeByIds(Long[] ids)
    {
        return userRechargeMapper.deleteUserRechargeByIds(ids);
    }

    /**
     * 删除用户充值订单信息
     * 
     * @param id 用户充值订单主键
     * @return 结果
     */
    @Override
    public int deleteUserRechargeById(Long id)
    {
        return userRechargeMapper.deleteUserRechargeById(id);
    }

    /**
     * 获取用户的各币种的充值金额
     * @param userIds 用户ids
     * @return
     */
    @Override
    public List<UserRecharge> selectUserRechargeAmountAllCurrency(List<Long> userIds,Date startTime,Date endTime){
        return userRechargeMapper.selectUserRechargeAmountAllCurrency(userIds,startTime,endTime);
    }

    /**
     * 获取用户的各币种的充值金额
     * @param userId 用户id
     * @return
     */
    @Override
    public List<UserRecharge> selectUserRechargeAmountAllCurrencyByUserId(Long userId){
        return userRechargeMapper.selectUserRechargeAmountAllCurrencyByUserId(userId);
    }

    /**
     * 充值订单审核
     * @param userRechargeId 充值订单id
     * @param orderStatus 状态：1:通过 2：驳回
     * @param rechargeMsg 驳回信息
     * @param remark 备注
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult updateRechargeOrderStatus(Long userRechargeId, Integer orderStatus,String rechargeMsg,String remark){
        //充值订单信息
        UserRecharge userRecharge = userRechargeMapper.selectUserRechargeById(userRechargeId);
        if (userRecharge == null) {
            throw new ServiceException("获取充值订单信息异常");
        }
        if (!userRecharge.getOrderStatus().equals(0) && !userRecharge.getOrderStatus().equals(3)) {
            throw new ServiceException("订单状态已审核");
        }
        if (userRecharge.getRechargeMethod().equals(1)){
            throw new ServiceException("在线支付订单不允许审核");
        }
        //审核通过
        if (orderStatus.equals(1)) {
            //用户信息
            UserInfo userInfo = userInfoMapper.selectUserInfoById(userRecharge.getUserId());
            if (userInfo == null) {
                throw new ServiceException("获取用户信息异常");
            }
            //用户id
            Long userId = userInfo.getId();
            //日志记录用户id
            HttpUtils.getRequestLogParams().put("userId",userId);
            //币种id
            Long currencyId = userRecharge.getCurrencyId();
            //充值金额
            BigDecimal rechargeAmount = userRecharge.getRechargeAmount();
            //充值自动转化平台默认交易币种
            Integer selectSwitchStatusById125 = switchSetService.selectSwitchStatusById(125L);
            if (selectSwitchStatusById125.equals(0)){
                //平台默认交易币种
                Long defaultTradeCurrencyId = CacheUtil.getOtherValueByKey("default_trade_currency_id", Long.class);
                if (defaultTradeCurrencyId == null){
                    throw new ServiceException("获取平台默认交易币种信息异常");
                }
                if (!defaultTradeCurrencyId.equals(currencyId)){
                    //获取平台币种
                    PlatformCurrency platformCurrencySearch = new PlatformCurrency();
                    platformCurrencySearch.setStatus(0);
                    List<PlatformCurrency> platformCurrencies = platformCurrencyService.selectPlatformCurrencyList(platformCurrencySearch);
                    //币种map
                    Map<Long, PlatformCurrency> platformCurrencyMap = platformCurrencies.stream().collect(Collectors.toMap(a -> a.getId(), a -> a));
                    ExchangeRateUtil.fillExchangeRate(platformCurrencies);
                    //汇率
                    BigDecimal exchangeRate = ExchangeRateUtil.getExchangeInfo(currencyId,defaultTradeCurrencyId,platformCurrencyMap).get("exchangeRate");
                    //折合
                    rechargeAmount = rechargeAmount.multiply(exchangeRate).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
                    //
                    currencyId = defaultTradeCurrencyId;
                }
            }


            //用户余额信息
            UserAmount userAmount = userAmountService.getUserAmount(userId, currencyId);
            //变更前余额
            BigDecimal userAmountBefore = userAmount.getAmount();
            //变更后余额
            BigDecimal userAmountAfter = userAmountBefore.add(rechargeAmount);
            userAmount.setAmount(userAmountAfter);
            //变更余额
            int updateUserAmount = userAmountService.updateUserAmount(userAmount);
            if (updateUserAmount <= 0){
                throw new ServiceException("系统繁忙");
            }

            //插入账户明细记录
            UserBillDetail userBillDetail = new UserBillDetail();
            userBillDetail.setUserId(userId);
            userBillDetail.setDeType("用户充值");
            userBillDetail.setDeSummary("用户充值成功");
            userBillDetail.setOrderAmount(rechargeAmount);
            userBillDetail.setOrderTime(new Date());
            userBillDetail.setAmountBefore(userAmountBefore);
            userBillDetail.setAmountAfter(userAmountAfter);
            userBillDetail.setRelateOrderId(userRechargeId);
            userBillDetail.setOrderClass(0);
            userBillDetail.setCurrencyId(userAmount.getCurrencyId());
            int insertUserBillDetail = userBillDetailService.insertUserBillDetail(userBillDetail);
            if (insertUserBillDetail <= 0) {
                throw new ServiceException("系统繁忙");
            }

            userRecharge.setUserAmountBefore(userAmountBefore);
            userRecharge.setUserAmountAfter(userAmountAfter);
            //充值成功为上级用户返佣
            rebate(userRecharge);

            //更新打码量
            //打码倍数
            BigDecimal userDefaultDmMultiples = CacheUtils.getOtherValueByKey("user_default_dm_multiples",BigDecimal.class);
            if (userDefaultDmMultiples != null && userDefaultDmMultiples.compareTo(BigDecimal.ZERO) >= 0){
                //汇率
                BigDecimal exchangeRate = currencyExchangeRateService.getExchangeInfo(currencyId,3L).get("exchangeRate");
                //折合USDT
                BigDecimal USDTValue = exchangeRate.multiply(rechargeAmount);
                //新增打码量
                BigDecimal dmAmt = userDefaultDmMultiples.multiply(USDTValue).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
                if (dmAmt.compareTo(BigDecimal.ZERO) > 0){
                    //原先的打码量
                    BigDecimal needOrderAmount = userInfo.getNeedOrderAmount();
                    if (needOrderAmount.compareTo(BigDecimal.ZERO) < 0){
                        needOrderAmount = BigDecimal.ZERO;
                    }
                    UserInfo userInfoVo = new UserInfo();
                    userInfoVo.setId(userInfo.getId());
                    userInfoVo.setNeedOrderAmount(needOrderAmount.add(dmAmt));
                    int updateUser = userInfoMapper.updateUserInfo(userInfoVo);
                    if (updateUser <= 0) {
                        throw new ServiceException("系统繁忙");
                    }
                    //插入打码量变更记录
                    UserDmAmountChangeRecord userDmAmountChangeRecord = new UserDmAmountChangeRecord();
                    userDmAmountChangeRecord.setUserId(userId);
                    userDmAmountChangeRecord.setOrderAmount(USDTValue);
                    userDmAmountChangeRecord.setDmMultiples(userDefaultDmMultiples);
                    userDmAmountChangeRecord.setDmAmount(dmAmt);
                    userDmAmountChangeRecord.setDmAmountBefore(needOrderAmount);
                    userDmAmountChangeRecord.setDmAmountAfter(needOrderAmount.add(dmAmt));
                    userDmAmountChangeRecord.setCreateTime(new Date());
                    userDmAmountChangeRecord.setUpdateBy(SecurityUtils.getUsername());
                    userDmAmountChangeRecord.setOrderType(1);
                    int insertUserDmAmountChangeRecord = userDmAmountChangeRecordService.insertUserDmAmountChangeRecord(userDmAmountChangeRecord);
                    if (insertUserDmAmountChangeRecord <= 0){
                        throw new ServiceException("系统繁忙");
                    }
                }
            }

            //与USDT汇率
            BigDecimal exchangeRate = currencyExchangeRateService.getExchangeInfo(currencyId,3L).get("exchangeRate");
            //折合USDT
            BigDecimal USDTValue = exchangeRate.multiply(rechargeAmount);

            //用户当前vip等级
            Integer vipLevel = userInfo.getVipLevel();

            //用户充值计算VIP等级
            Integer selectSwitchStatus114 = switchSetService.selectSwitchStatusById(114L);
            if (selectSwitchStatus114.equals(0)){
                //更新会员等级
                //VIP经验值更新前
                BigDecimal vipExperienceValueBefore = vipExperienceValueService.getUserCurrentVipExperienceValue(userId);
                //本次充值经验值
                BigDecimal experienceValue = USDTValue;
                //VIP经验值更新后
                BigDecimal vipExperienceValueAfter = vipExperienceValueBefore.add(experienceValue);
                //匹配的用户vip等级
                UserVipLevelConfig userVipLevelConfigNew = userVipLevelConfigService.selectUserVipLevelConfigByRechargeAmount(vipExperienceValueAfter);
                //如果匹配的vip等级与当前等级不符，则更新vip等级
                if (userVipLevelConfigNew != null && !userVipLevelConfigNew.getVipLevel().equals(vipLevel)){
                    UserInfo userInfoVo = new UserInfo();
                    userInfoVo.setId(userInfo.getId());
                    userInfoVo.setVipLevel(userVipLevelConfigNew.getVipLevel());
                    int count = userInfoMapper.updateUserInfo(userInfoVo);
                    if (count <= 0){
                        throw new ServiceException("系统繁忙");
                    }
                }
                //新增VIP经验值记录
                VipExperienceValue vipExperienceValue = new VipExperienceValue();
                vipExperienceValue.setUserId(userId);
                vipExperienceValue.setRelateOrderCode(userRecharge.getOrderCode());
                vipExperienceValue.setExperienceValue(experienceValue);
                vipExperienceValue.setExperienceValueBefore(vipExperienceValueBefore);
                vipExperienceValue.setExperienceValueAfter(vipExperienceValueAfter);
                vipExperienceValue.setCreateTime(new Date());
                int insertVipExperienceValue = vipExperienceValueService.insertVipExperienceValue(vipExperienceValue);
                if (insertVipExperienceValue <= 0){
                    throw new ServiceException("插入VIP经验值记录异常");
                }
            }

            //充值彩金
            //vip等级配置
            UserVipLevelConfig userVipLevelConfig = userVipLevelConfigService.selectUserVipLevelConfigByVipLevel(vipLevel);
            if (userVipLevelConfig != null && userVipLevelConfig.getStatus().equals(0)){
                //充值彩金
                BigDecimal rechargeWinningsAmount = BigDecimal.ZERO;
                //充值彩金赠送方式
                Integer bonusMethod = userVipLevelConfig.getBonusMethod();
                if (bonusMethod.equals(0)){
                    //彩金比率
                    BigDecimal rechargeBonusRatio = userVipLevelConfig.getRechargeBonusRatio();
                    if (rechargeBonusRatio.compareTo(BigDecimal.ZERO) > 0){
                        rechargeWinningsAmount = rechargeAmount.multiply(rechargeBonusRatio).divide(new BigDecimal(100),Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
                    }
                }else {
                    rechargeWinningsAmount = userVipLevelConfig.getRechargeBonusAmount();
                }
                if (rechargeWinningsAmount.compareTo(BigDecimal.ZERO) > 0){
                    //用户钱包信息
                    userAmount = userAmountService.getUserAmount(userId, currencyId);
                    //余额变更前
                    userAmountBefore = userAmount.getAmount();
                    //余额变更后
                    userAmountAfter = userAmountBefore.add(rechargeWinningsAmount);
                    userAmount.setAmount(userAmountAfter);
                    //变更余额
                    updateUserAmount = userAmountService.updateUserAmount(userAmount);
                    if (updateUserAmount <= 0){
                        throw new RuntimeException("系统繁忙");
                    }
                    //插入账户明细记录
                    userBillDetail = new UserBillDetail();
                    userBillDetail.setUserId(userId);
                    userBillDetail.setDeType("充值赠送彩金");
                    userBillDetail.setDeSummary("充值赠送彩金成功");
                    userBillDetail.setOrderAmount(rechargeWinningsAmount);
                    userBillDetail.setOrderTime(new Date());
                    userBillDetail.setAmountBefore(userAmountBefore);
                    userBillDetail.setAmountAfter(userAmountAfter);
                    userBillDetail.setRelateOrderId(userRechargeId);
                    userBillDetail.setOrderClass(55);
                    userBillDetail.setCurrencyId(userAmount.getCurrencyId());
                    int insertUserBillDetail2 = userBillDetailService.insertUserBillDetail(userBillDetail);
                    if (insertUserBillDetail2 <= 0) {
                        throw new ServiceException("系统繁忙");
                    }
                    //添加彩金出入记录
                    UserWinningsChangeRecord userWinningsChangeRecord = new UserWinningsChangeRecord();
                    userWinningsChangeRecord.setUserId(userId);
                    userWinningsChangeRecord.setOrderCode(CodeUtils.generateOrderCode("userWinningsChangeRecord"));
                    userWinningsChangeRecord.setOrderAmount(rechargeWinningsAmount);
                    userWinningsChangeRecord.setOrderType(3);
                    userWinningsChangeRecord.setCreateTime(new Date());
                    userWinningsChangeRecord.setOperatorName("充值彩金");
                    userWinningsChangeRecord.setCurrencyId(userAmount.getCurrencyId());
                    userWinningsChangeRecord.setUserAmountBefore(userAmountBefore);
                    userWinningsChangeRecord.setUserAmountAfter(userAmountAfter);
                    int insertUserPointChangeRecord = userWinningsChangeRecordService.insertUserWinningsChangeRecord(userWinningsChangeRecord);
                    if (insertUserPointChangeRecord <= 0) {
                        throw new ServiceException("系统繁忙");
                    }
                }
            }

            //充值通道信息
            RechargeChannelConfig rechargeChannelConfig = rechargeChannelConfigMapper.selectRechargeChannelConfigById(userRecharge.getPayChannelId());
            //首冲彩金
            UserRecharge search = new UserRecharge();
            search.setOrderStatus(1);
            search.setUserId(userId);
            //如果是首次通过充值审核
            if (userRechargeMapper.selectUserRechargeList(search).size() == 0){
                //首次充值赠送彩金金额
                BigDecimal firstRechargeWinningsAmount = BigDecimal.ZERO;
                //日常充值赠送方式
                Integer firstRechargeWinningsMethod = rechargeChannelConfig.getFirstRechargeWinningsMethod();
                //如果按比例赠送
                if (firstRechargeWinningsMethod.equals(0)){
                    //首次充值赠送彩金比率
                    BigDecimal firstRechargeWinningsRate = rechargeChannelConfig.getFirstRechargeWinningsRate();
                    if (firstRechargeWinningsRate.compareTo(BigDecimal.ZERO) > 0){
                        firstRechargeWinningsAmount = rechargeAmount.multiply(firstRechargeWinningsRate).divide(new BigDecimal(100),Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
                    }

                }else {
                    //如果是赠送固定金额
                    firstRechargeWinningsAmount = rechargeChannelConfig.getFirstRechargeWinningsAmount();
                }
                //如果有首充彩金
                if (firstRechargeWinningsAmount.compareTo(BigDecimal.ZERO) > 0){
                    //用户钱包信息
                    userAmount = userAmountService.getUserAmount(userId, currencyId);
                    //余额变更前
                    userAmountBefore = userAmount.getAmount();
                    //余额变更前
                    userAmountAfter = userAmountBefore.add(firstRechargeWinningsAmount);
                    userAmount.setAmount(userAmountAfter);
                    //更新余额
                    int updateUserAmount2 = userAmountService.updateUserAmount(userAmount);
                    if (updateUserAmount2 <= 0){
                        throw new ServiceException("系统繁忙");
                    }
                    //插入账户明细记录
                    userBillDetail = new UserBillDetail();
                    userBillDetail.setUserId(userId);
                    userBillDetail.setDeType("首次充值赠送彩金");
                    userBillDetail.setDeSummary("首次充值赠送彩金成功");
                    userBillDetail.setOrderAmount(firstRechargeWinningsAmount);
                    userBillDetail.setOrderTime(new Date());
                    userBillDetail.setAmountBefore(userAmountBefore);
                    userBillDetail.setAmountAfter(userAmountAfter);
                    userBillDetail.setRelateOrderId(userRechargeId);
                    userBillDetail.setOrderClass(72);
                    userBillDetail.setCurrencyId(userAmount.getCurrencyId());
                    int insertUserBillDetail2 = userBillDetailService.insertUserBillDetail(userBillDetail);
                    if (insertUserBillDetail2 <= 0) {
                        throw new ServiceException("系统繁忙");
                    }
                    //添加彩金出入记录
                    UserWinningsChangeRecord userWinningsChangeRecord = new UserWinningsChangeRecord();
                    userWinningsChangeRecord.setUserId(userId);
                    userWinningsChangeRecord.setOrderCode(CodeUtils.generateOrderCode("userWinningsChangeRecord"));
                    userWinningsChangeRecord.setOrderAmount(firstRechargeWinningsAmount);
                    userWinningsChangeRecord.setOrderType(5);
                    userWinningsChangeRecord.setCreateTime(new Date());
                    userWinningsChangeRecord.setOperatorName("首充彩金");
                    userWinningsChangeRecord.setCurrencyId(userAmount.getCurrencyId());
                    userWinningsChangeRecord.setUserAmountBefore(userAmountBefore);
                    userWinningsChangeRecord.setUserAmountAfter(userAmountAfter);
                    int insertUserPointChangeRecord = userWinningsChangeRecordService.insertUserWinningsChangeRecord(userWinningsChangeRecord);
                    if (insertUserPointChangeRecord <= 0) {
                        throw new ServiceException("系统繁忙");
                    }
                }
            }else {
                //日常充值赠送彩金金额
                BigDecimal dailyRechargeWinningsAmount = BigDecimal.ZERO;
                //日常充值赠送方式
                Integer dailyRechargeWinningsMethod = rechargeChannelConfig.getDailyRechargeWinningsMethod();
                //如果按比例赠送
                if (dailyRechargeWinningsMethod.equals(0)){
                    //日常充值赠送彩金比率
                    BigDecimal dailyRechargeWinningsRate = rechargeChannelConfig.getDailyRechargeWinningsRate();
                    if (dailyRechargeWinningsRate.compareTo(BigDecimal.ZERO) > 0){
                        dailyRechargeWinningsAmount = rechargeAmount.multiply(dailyRechargeWinningsRate).divide(new BigDecimal(100),Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
                    }

                }else {
                    //如果是赠送固定金额
                    dailyRechargeWinningsAmount = rechargeChannelConfig.getDailyRechargeWinningsAmount();
                }
                //如果有日常充值彩金
                if (dailyRechargeWinningsAmount.compareTo(BigDecimal.ZERO) > 0){
                    //用户钱包信息
                    userAmount = userAmountService.getUserAmount(userId, currencyId);
                    //余额变更前
                    userAmountBefore = userAmount.getAmount();
                    //余额变更前
                    userAmountAfter = userAmountBefore.add(dailyRechargeWinningsAmount);
                    userAmount.setAmount(userAmountAfter);
                    //更新余额
                    int updateUserAmount2 = userAmountService.updateUserAmount(userAmount);
                    if (updateUserAmount2 <= 0){
                        throw new ServiceException("系统繁忙");
                    }
                    //插入账户明细记录
                    userBillDetail = new UserBillDetail();
                    userBillDetail.setUserId(userId);
                    userBillDetail.setDeType("充值赠送彩金");
                    userBillDetail.setDeSummary("充值赠送彩金成功");
                    userBillDetail.setOrderAmount(dailyRechargeWinningsAmount);
                    userBillDetail.setOrderTime(new Date());
                    userBillDetail.setAmountBefore(userAmountBefore);
                    userBillDetail.setAmountAfter(userAmountAfter);
                    userBillDetail.setRelateOrderId(userRechargeId);
                    userBillDetail.setOrderClass(55);
                    userBillDetail.setCurrencyId(userAmount.getCurrencyId());
                    int insertUserBillDetail2 = userBillDetailService.insertUserBillDetail(userBillDetail);
                    if (insertUserBillDetail2 <= 0) {
                        throw new ServiceException("系统繁忙");
                    }
                    //添加彩金出入记录
                    UserWinningsChangeRecord userWinningsChangeRecord = new UserWinningsChangeRecord();
                    userWinningsChangeRecord.setUserId(userId);
                    userWinningsChangeRecord.setOrderCode(CodeUtils.generateOrderCode("userWinningsChangeRecord"));
                    userWinningsChangeRecord.setOrderAmount(dailyRechargeWinningsAmount);
                    userWinningsChangeRecord.setOrderType(3);
                    userWinningsChangeRecord.setCreateTime(new Date());
                    userWinningsChangeRecord.setOperatorName("充值彩金");
                    userWinningsChangeRecord.setCurrencyId(userAmount.getCurrencyId());
                    userWinningsChangeRecord.setUserAmountBefore(userAmountBefore);
                    userWinningsChangeRecord.setUserAmountAfter(userAmountAfter);
                    int insertUserPointChangeRecord = userWinningsChangeRecordService.insertUserWinningsChangeRecord(userWinningsChangeRecord);
                    if (insertUserPointChangeRecord <= 0) {
                        throw new ServiceException("系统繁忙");
                    }
                }
            }

            // 单笔充值档位奖励
            RechargeRewardTier rechargeRewardTier = rechargeRewardTierMapper.selectRechargeRewardTierByRechargeAmount(rechargeAmount, currencyId, userId);
            // 如果未领取过该档位奖励
            if (rechargeRewardTier != null) {
                // 奖励比例
                BigDecimal rewardRate = rechargeRewardTier.getRewardRate();
                // 奖励金额
                BigDecimal rewardAmount = rechargeAmount.multiply(rewardRate).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);

                // 发放奖励
                if (rewardAmount.compareTo(BigDecimal.ZERO) > 0) {
                    //用户钱包信息
                    userAmount = userAmountService.getUserAmount(userId, currencyId);
                    //余额变更前
                    userAmountBefore = userAmount.getAmount();
                    //余额变更前
                    userAmountAfter = userAmountBefore.add(rewardAmount);
                    userAmount.setAmount(userAmountAfter);
                    //更新余额
                    int updateUserAmount2 = userAmountService.updateUserAmount(userAmount);
                    if (updateUserAmount2 <= 0){
                        throw new ServiceException("更新用户钱包信息异常");
                    }
                    //插入账户明细记录
                    userBillDetail = new UserBillDetail();
                    userBillDetail.setUserId(userId);
                    userBillDetail.setDeType("充值赠送彩金");
                    userBillDetail.setDeSummary("充值赠送彩金成功");
                    userBillDetail.setOrderAmount(rewardAmount);
                    userBillDetail.setOrderTime(new Date());
                    userBillDetail.setAmountBefore(userAmountBefore);
                    userBillDetail.setAmountAfter(userAmountAfter);
                    userBillDetail.setRelateOrderId(userRechargeId);
                    userBillDetail.setOrderClass(55);
                    userBillDetail.setCurrencyId(userAmount.getCurrencyId());
                    int insertUserBillDetail2 = userBillDetailService.insertUserBillDetail(userBillDetail);
                    if (insertUserBillDetail2 <= 0) {
                        throw new ServiceException("插入账户明细记录异常");
                    }

                    // 插入奖励记录
                    RechargeRewardRecord rechargeRewardRecord = new RechargeRewardRecord();
                    rechargeRewardRecord.setUserId(userId);
                    rechargeRewardRecord.setUserRechargeId(userRechargeId);
                    rechargeRewardRecord.setRewardTierId(rechargeRewardTier.getRechargeRewardTierId());
                    rechargeRewardRecord.setRechargeAmount(rechargeAmount);
                    rechargeRewardRecord.setRewardRate(rewardRate);
                    rechargeRewardRecord.setRewardAmount(rewardAmount);
                    rechargeRewardRecord.setCreateTime(new Date());
                    int insertRechargeRewardRecord = rechargeRewardRecordMapper.insertRechargeRewardRecord(rechargeRewardRecord);
                    if (insertRechargeRewardRecord == 0) {
                        throw new ServiceException("插入充值奖励领取记录异常");
                    }
                }
            }

        }

        userRecharge.setPayTime(new Date());
        userRecharge.setOperatorName(SecurityUtils.getUsername());
        userRecharge.setOrderStatus(orderStatus);
        userRecharge.setRechargeMsg(rechargeMsg);
        userRecharge.setRemark(remark);
        int updateCount = userRechargeMapper.updateUserRecharge(userRecharge);
        if (updateCount <= 0) {
            throw new ServiceException("系统繁忙");
        }
        //日志记录充值订单信息
        HttpUtils.getRequestLogParams().put("userRecharge",JSONObject.toJSONString(userRecharge));
        return AjaxResult.success();
    }

    /**
     * 充值返佣
     * @param userRecharge 充值订单信息
     */
    void rebate(UserRecharge userRecharge){
        //共有多少个上级需要返利
        Integer rebateNum = CacheUtils.getOtherValueByKey("team_userTeamLevel",Integer.class);
        if (rebateNum == null || rebateNum <= 0){
            return;
        }
        //需要返佣的上级用户List
        List<UserTeamLevelLine> supTeamLine = userTeamLevelLineMapper.getSupTeamLine(userRecharge.getUserId(), rebateNum,0);
        //获取返佣比率
        UserRebateRate userRebateRate = new UserRebateRate();
        userRebateRate.setRebateType(0);
        List<UserRebateRate> userRebateRates = userRebateRateMapper.selectUserRebateRateList(userRebateRate);
        Map<Integer, BigDecimal> RebateRateMap = userRebateRates.stream().collect(Collectors.toMap(UserRebateRate::getRebateLevel, a -> a.getRebateRate()));

        //充值订单的订单id
        Long userRechargeOrderId = userRecharge.getId();
        //币种
        Long currencyId = userRecharge.getCurrencyId();
        //充值金额
        BigDecimal rechargeAmount = userRecharge.getRechargeAmount();

        for (int i = 0; i < supTeamLine.size(); i++) {
            //团队关系信息
            UserTeamLevelLine userTeamLevelLine = supTeamLine.get(i);
            //上级用户id
            Long supUserId = userTeamLevelLine.getSupUserId();
            //上级用户信息
            UserInfo supUser = userInfoMapper.selectUserInfoById(supUserId);
            if (supUser == null){
                continue;
            }
            //等级关系
            Integer teamLevel = userTeamLevelLine.getTeamLevel();
            //返佣比例
            BigDecimal rebateRate = RebateRateMap.get(teamLevel);
            //如果返佣比例是空或者比例不大于0，则跳过
            if (rebateRate == null || rebateRate.compareTo(BigDecimal.ZERO) <= 0){
                continue;
            }
            //返利额度
            BigDecimal rebateAmount = rechargeAmount.multiply(rebateRate).multiply(new BigDecimal("0.01")).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            //如果返利金额不大于0，跳过
            if (rebateAmount.compareTo(BigDecimal.ZERO) <= 0){
                continue;
            }
            //获取上级钱包信息
            UserAmount userAmount = userAmountService.getUserAmount(supUserId, currencyId);
            //余额变更前
            BigDecimal userAmountBefore = userAmount.getAmount();
            //余额变更后
            BigDecimal userAmountAfter = userAmountBefore.add(rebateAmount);
            //更新返利后的总额
            userAmount.setAmount(userAmountAfter);
            int updateUserAmount = userAmountService.updateUserAmount(userAmount);
            if (updateUserAmount <= 0){
                throw new RuntimeException("系统繁忙");
            }

            //充值返佣收入明细
            UserBillDetail userBillDetail = new UserBillDetail();
            userBillDetail.setUserId(supUserId);
            userBillDetail.setDeType("充值返佣");
            userBillDetail.setDeSummary("下级充值返佣");
            userBillDetail.setOrderAmount(rebateAmount);
            userBillDetail.setOrderTime(new Date());
            userBillDetail.setAmountBefore(userAmountBefore);
            userBillDetail.setAmountAfter(userAmountAfter);
            userBillDetail.setRelateOrderId(userRechargeOrderId);
            userBillDetail.setOrderClass(10);
            userBillDetail.setCurrencyId(userAmount.getCurrencyId());
            int insertUserBillDetail = userBillDetailService.insertUserBillDetail(userBillDetail);
            if (insertUserBillDetail <= 0) {
                throw new RuntimeException("系统繁忙");
            }

            //返佣记录
            UserCommissionRecord userCommissionRecord = new UserCommissionRecord();
            userCommissionRecord.setSuperId(supUserId);
            userCommissionRecord.setLowerId(Long.valueOf(userRecharge.getUserId()));
            userCommissionRecord.setCommissionLevel(teamLevel);
            userCommissionRecord.setCommissionAmount(rebateAmount);
            userCommissionRecord.setCommissionProfit(rebateRate);
            userCommissionRecord.setOrderCodeSource(userRecharge.getOrderCode());
            userCommissionRecord.setOrderCodeCommission(String.valueOf(userBillDetail.getId()));
            userCommissionRecord.setCreateTime(new Date());
            userCommissionRecord.setCurrencyId(currencyId);
            userCommissionRecord.setCommissionType(0);
            int insertUserCommissionRecord = userCommissionRecordMapper.insertUserCommissionRecord(userCommissionRecord);
            if (insertUserCommissionRecord <= 0){
                throw new RuntimeException();
            }
        }
    }

    /**
     * 修改充值金额
     * @param userRechargeId 充值订单id
     * @param rechargeAmount 充值金额
     * @return
     */
    @Override
    public AjaxResult updateRechargeAmount(Long userRechargeId, BigDecimal rechargeAmount){
        //充值订单信息
        UserRecharge userRecharge = userRechargeMapper.selectUserRechargeById(userRechargeId);
        if (userRecharge == null){
            throw new ServiceException("获取订单信息异常");
        }
        //日志记录充值订单修改前金额
        HttpUtils.getRequestLogParams().put("修改前金额",userRecharge.getRechargeAmount());
        if (!userRecharge.getOrderStatus().equals(0) && !userRecharge.getOrderStatus().equals(3)){
            throw new ServiceException("此订单已审核过，不允许修改订单金额");
        }
        UserRecharge userRechargeVo = new UserRecharge();
        userRechargeVo.setId(userRechargeId);
        userRechargeVo.setRechargeAmount(rechargeAmount);
        userRechargeVo.setSqlVersion(userRecharge.getSqlVersion());
        int count = userRechargeMapper.updateUserRecharge(userRechargeVo);
        if (count <= 0) {
            throw new ServiceException("系统繁忙");
        }
        //日志记录充值订单信息
        HttpUtils.getRequestLogParams().put("userRecharge",JSONObject.toJSONString(userRechargeVo));
        return AjaxResult.success();
    }

    /**
     * 修改订单备注
     * @param userRechargeId 充值订单id
     * @param remark 备注
     * @return
     */
    @Override
    public AjaxResult updateOrderRemark(Long userRechargeId, String remark) {
        //充值订单信息
        UserRecharge userRecharge = userRechargeMapper.selectUserRechargeById(userRechargeId);
        if (userRecharge == null) {
            throw new ServiceException("获取订单信息异常");
        }
        //
        UserRecharge userRechargeVo = new UserRecharge();
        userRechargeVo.setId(userRechargeId);
        userRechargeVo.setRemark(remark);
        userRechargeVo.setSqlVersion(userRecharge.getSqlVersion());
        int count = userRechargeMapper.updateUserRecharge(userRechargeVo);
        if (count <= 0) {
            throw new ServiceException("系统繁忙");
        }
        //日志记录充值订单信息
        HttpUtils.getRequestLogParams().put("userRecharge",JSONObject.toJSONString(userRechargeVo));
        return AjaxResult.success();
    }

    /**
     * 用户充值
     * @param rechargeAmount 充值金额
     * @param payChannelId 充值通道id
     * @param rechargeImg 充值凭证
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int inMoney(BigDecimal rechargeAmount,Long payChannelId,String rechargeImg){
        //用户id
        Long userId = SecurityUtils.getUserId();
        //用户信息
        UserInfo userInfo = userInfoMapper.selectUserInfoById(userId);
        //是否开启初级实名认证
        Integer selectSwitchStatusById68 = switchSetService.selectSwitchStatusById(68L);
        //如果初级认证开启
        if (selectSwitchStatusById68.equals(0)){
            //完成初级实名认证才能充值
            Integer selectSwitchStatusById73 = switchSetService.selectSwitchStatusById(73L);
            if (selectSwitchStatusById73.equals(0) && !userInfo.getAuthStatusJunior().equals(2)){
                throw new LangException(HintConstants.AUTH_FIRST_JUNIOR,"请先完成初级实名认证");
            }
        }
        //是否开启高级实名认证
        Integer selectSwitchStatusById75 = switchSetService.selectSwitchStatusById(75L);
        //如果高级认证开启
        if (selectSwitchStatusById75.equals(0)) {
            //完成高级实名认证才能充值
            Integer selectSwitchStatusById74 = switchSetService.selectSwitchStatusById(74L);
            if (selectSwitchStatusById74.equals(0) && !userInfo.getAuthStatusSenior().equals(2)){
                throw new LangException(HintConstants.AUTH_FIRST_SENIOR,"请先完成高级实名认证");
            }
        }
        //充值的支付渠道信息
        RechargeChannelConfig rechargeChannelConfig = rechargeChannelConfigMapper.selectRechargeChannelConfigById(payChannelId);
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
//        //该支付渠道最多充值金额
//        Integer channelMaxLimit = sitePay.getChannelMaxLimit();
        //该支付渠道最低充值金额
        BigDecimal channelMinLimit = rechargeChannelConfig.getChannelMinLimit();

        if (rechargeAmount.compareTo(channelMinLimit) < 0) {
            List<Object> list = new ArrayList<>();
            list.add(channelMinLimit);
            throw new LangException("hint_30",list,"充值金额不得低于" + channelMinLimit);
        }
        //查询充值订单限制模式 2 无限制 3 有待审核订单不可充值 4只允许有N条待审核订单
        Integer rechargeOrderLimitModel = switchSetService.selectSwitchStatusById(47L);
        if (rechargeOrderLimitModel != null){
            //用户待审核充值订单数量
            Integer userRechargePendingReviewNum = userRechargeMapper.getUserRechargePendingReviewNum(new BaseEntity(),userId).size();
            if (rechargeOrderLimitModel.equals(3)){
                if (userRechargePendingReviewNum > 0){
                    List<Object> param = new ArrayList<>();
                    param.add(1);
                    throw new LangException("hint_52",param,"最多允许有1条待处理的充值订单");
                }
            }else if (rechargeOrderLimitModel.equals(4)){
                Integer rechargeOrderPendingLimit = CacheUtils.getOtherValueByKey("recharge_order_pending_Limit",Integer.class);
                if (userRechargePendingReviewNum >= rechargeOrderPendingLimit){
                    List<Object> param = new ArrayList<>();
                    param.add(rechargeOrderPendingLimit);
                    throw new LangException("hint_52",param,"最多允许有"+rechargeOrderPendingLimit+"条待处理的充值订单");
                }
            }
        }
        //充值订单信息
        UserRecharge userRecharge = new UserRecharge();
        userRecharge.setUserId(userId);
        userRecharge.setOrderCode(CodeUtils.generateOrderCode("R"));
        userRecharge.setRechargeAmount(rechargeAmount);
        userRecharge.setCreateTime(new Date());
        userRecharge.setPayChannelName(rechargeChannelConfig.getChannelName());
        userRecharge.setPayChannelId(rechargeChannelConfig.getId());
        userRecharge.setCurrencyId(currencyId);
        userRecharge.setRechargeImg(rechargeImg);
        userRecharge.setRechargeMethod(0);
        userRecharge.setOrderStatus(3);
        int insertCount = userRechargeMapper.insertUserRecharge(userRecharge);
        if (insertCount <= 0) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
        //日志记录充值订单信息
        HttpUtils.getRequestLogParams().put("userRecharge",JSONObject.toJSONString(userRecharge));
        //如果是模拟用户
        if (userInfo.getAccountType().equals(1)){
            //自动审核通过开关
            Integer autoAgree = switchSetService.selectSwitchStatusById(45L);
            if (autoAgree != null && autoAgree.equals(0)){
                AjaxResult ajaxResult = updateRechargeOrderStatus(userRecharge.getId(), 1,null,null);
                if (!ajaxResult.isSuccess()){
                    throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
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

        //telegram通知（充值申请）
        Integer switchStatusById127 = switchSetService.selectSwitchStatusById(127L);
        if (switchStatusById127.equals(0)) {
            //telegram消息
            String telegramMsg = "\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89充值申请\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89\n" +
                    "⏰时间：" + userRecharge.getCreateTime() + "\n" +
                    "ID: " + userInfo.getUserNo() + "\n" +
                    "用户账号: " + userInfo.getUserAccount() + "\n" +
                    "用户昵称: " + userInfo.getNickName() + "\n" +
                    "用户备注: " + userInfo.getRemark() + "\n" +
                    "邀请码: " + userInfo.getInviteCode() + "\n" +
                    "所属代理: " + userInfo.getAgentId() + "/" + userInfo.getAgentName() + "\n" +
                    "代理昵称: " + userInfo.getAgentNickName() + "\n" +
                    "充值金额: " + rechargeAmount.stripTrailingZeros().toPlainString() + platformCurrency.getCurrencyName() + "\n" +
                    "已申请充值,请及时处理！";
            TelegramUtils.sendAsyncMessage(telegramMsg, "default", "default");
        }
        return 1;
    }

    /**
     * 用户在线支付
     * @param rechargeAmount 充值金额
     * @param payType 支付类型(信用卡A: CA, 信用卡B: CB, 加密货币：CW)
     * @param fiat 法币单位,默认USD
     * @param userId 用户id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String userOnlineRecharge(BigDecimal rechargeAmount,String payType,String fiat,Long userId){
        //订单号
        String orderCode = CodeUtils.generateOrderCode("R");
        UserRecharge userRecharge = new UserRecharge();
        userRecharge.setUserId(userId);
        userRecharge.setOrderCode(orderCode);
        userRecharge.setRechargeAmount(rechargeAmount);
        userRecharge.setOrderStatus(0);
        userRecharge.setCreateTime(new Date());
        userRecharge.setPayChannelName("在线支付");
        userRecharge.setPayChannelId(0L);
        userRecharge.setCurrencyId(0L);
        userRecharge.setRechargeImg(null);
        userRecharge.setRechargeMethod(1);
        userRecharge.setRemark(fiat);
        int insertCount = userRechargeMapper.insertUserRecharge(userRecharge);
        if (insertCount <= 0) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
        //日志记录充值订单信息
        HttpUtils.getRequestLogParams().put("userRecharge",JSONObject.toJSONString(userRecharge));
        //支付地址
        JSONObject jsonObject = OlgpayUtils.receiveOder(orderCode, rechargeAmount, userId, payType, fiat);
        //支付地址
        String payUrl = jsonObject.getString("payUrl");
        return payUrl;
    }

    /**
     * 在线支付成功异步回调
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String receiveUserOnlineRechargeOrderState(UserOnlineRechargeOrderVo userOnlineRechargeOrderVo){
        //状态：“ 1 ” 代表支付成功
        String state = userOnlineRechargeOrderVo.getState();
        if ("1".equals(state)){
            //订单号
            String orderCode = userOnlineRechargeOrderVo.getOrderNo();
            //获取订单信息
            UserRecharge userRecharge = new UserRecharge();
            userRecharge.setOrderCode(orderCode);
            List<UserRecharge> userRecharges = userRechargeMapper.selectUserRechargeList(userRecharge);
            if (userRecharges.size() == 1){
                //充值订单信息
                userRecharge = userRecharges.get(0);
                if (!userRecharge.getOrderStatus().equals(0)){
                    throw new RuntimeException("订单状态不是支付中，订单已结算，无需重复结算");
                }
//                //签名方式：
//                String signValidate = OlgpayUtils.pkKey + state + orderCode + userOnlineRechargeOrderVo.getFiatAmount() + userOnlineRechargeOrderVo.getPlatformOrderNo();
//                //SHA-256加密
//                signValidate = HmacUtils.hmacSha256Hex(OlgpayUtils.skKey,signValidate);
//                //Base64编码
//                signValidate = Base64Util.encode(signValidate);
//                //签名
//                String sign = userOnlineRechargeOrderVo.getSign();
//                if (sign.equals(signValidate)){
//                    throw new RuntimeException("签名异常");
//                }
                //法币金额
                BigDecimal fiatAmount = new BigDecimal(userOnlineRechargeOrderVo.getFiatAmount());
                if (fiatAmount.compareTo(userRecharge.getRechargeAmount()) != 0){
                    throw new RuntimeException("充值金额核对不正确");
                }
                //法币单位
                String fiat = userOnlineRechargeOrderVo.getFiat();
                if (!fiat.equals(userRecharge.getRemark())){
                    throw new RuntimeException("支付法币核对不正确");
                }
                //用户id
                Long userId = Long.valueOf(userOnlineRechargeOrderVo.getUserId());
                if (!userId.equals(userRecharge.getUserId())){
                    throw new RuntimeException("用户id核对不正确");
                }

                //USDT金額
                BigDecimal usdtAmount = new BigDecimal(userOnlineRechargeOrderVo.getUsdtAmount());
                //USDT汇率
                BigDecimal usdtRate = new BigDecimal(userOnlineRechargeOrderVo.getUsdtRate());
//                //tipMsg
//                String tipMsg = userOnlineRechargeOrderVo.getTipMsg();

                //币种id
                Long currencyId = 3L;
                //更新用户钱包余额
                UserAmount userAmount = userAmountService.getUserAmount(userId, currencyId);
                //余额变更钱
                BigDecimal userAmountBefore = userAmount.getAmount();
                //余额变更后
                BigDecimal userAmountAfter = userAmountBefore.add(usdtAmount);
                //更新用户余额
                userAmount.setAmount(userAmountAfter);
                int updateUserAmount = userAmountService.updateUserAmount(userAmount);
                if (updateUserAmount <= 0){
                    throw new RuntimeException("系统繁忙");
                }

                try{
                    //用户信息
                    UserInfo userInfo = userInfoMapper.selectUserInfoById(userId);
                    if (userInfo == null){
                        throw new RuntimeException("获取用户信息异常");
                    }
                    //更新打码量
                    //打码倍数
                    BigDecimal userDefaultDmMultiples = CacheUtils.getOtherValueByKey("user_default_dm_multiples",BigDecimal.class);
                    if (userDefaultDmMultiples != null && userDefaultDmMultiples.compareTo(BigDecimal.ZERO) >= 0){
                        //新增打码量
                        BigDecimal dmAmt = userDefaultDmMultiples.multiply(usdtAmount).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
                        if (dmAmt.compareTo(BigDecimal.ZERO) > 0){
                            //原先的打码量
                            BigDecimal needOrderAmount = userInfo.getNeedOrderAmount();
                            if (needOrderAmount.compareTo(BigDecimal.ZERO) < 0){
                                needOrderAmount = BigDecimal.ZERO;
                            }
                            UserInfo userInfoVo = new UserInfo();
                            userInfoVo.setId(userInfo.getId());
                            userInfoVo.setNeedOrderAmount(needOrderAmount.add(dmAmt));
                            int updateUser = userInfoMapper.updateUserInfo(userInfoVo);
                            if (updateUser <= 0) {
                                throw new RuntimeException("系统繁忙");
                            }
                            //插入打码量变更记录
                            UserDmAmountChangeRecord userDmAmountChangeRecord = new UserDmAmountChangeRecord();
                            userDmAmountChangeRecord.setUserId(userId);
                            userDmAmountChangeRecord.setOrderAmount(usdtAmount);
                            userDmAmountChangeRecord.setDmMultiples(userDefaultDmMultiples);
                            userDmAmountChangeRecord.setDmAmount(dmAmt);
                            userDmAmountChangeRecord.setDmAmountBefore(needOrderAmount);
                            userDmAmountChangeRecord.setDmAmountAfter(needOrderAmount.add(dmAmt));
                            userDmAmountChangeRecord.setCreateTime(new Date());
                            userDmAmountChangeRecord.setUpdateBy(SecurityUtils.getUsername());
                            userDmAmountChangeRecord.setOrderType(1);
                            int insertUserDmAmountChangeRecord = userDmAmountChangeRecordService.insertUserDmAmountChangeRecord(userDmAmountChangeRecord);
                            if (insertUserDmAmountChangeRecord <= 0){
                                throw new ServiceException("系统繁忙");
                            }
                        }
                    }
                }catch (Exception e){
                    log.error("caused by 增加打码量异常");
                }

                //更新充值订单信息
                userRecharge.setOperatorName("自动通过");
                userRecharge.setCurrencyId(currencyId);
                userRecharge.setRechargeAmount(usdtAmount);
                userRecharge.setOrderStatus(1);
                userRecharge.setPayTime(new Date());
                userRecharge.setUserAmountBefore(userAmountBefore);
                userRecharge.setUserAmountAfter(userAmountAfter);
                String remark = "支付法币金额："+fiatAmount + "，支付法币单位："+fiat +"，USDT汇率："+usdtRate + "，USDT金额："+usdtAmount;
                userRecharge.setRemark(remark);
                int updateUserRecharge = userRechargeMapper.updateUserRecharge(userRecharge);
                if (updateUserRecharge <= 0){
                    throw new RuntimeException("系统繁忙");
                }

                //流水明细
                UserBillDetail userBillDetail = new UserBillDetail();
                userBillDetail.setUserId(userId);
                userBillDetail.setDeType("用户在线支付");
                userBillDetail.setDeSummary("用户在线支付成功");
                userBillDetail.setOrderAmount(usdtAmount);
                userBillDetail.setOrderTime(new Date());
                userBillDetail.setAmountBefore(userAmountBefore);
                userBillDetail.setAmountAfter(userAmountAfter);
                userBillDetail.setRelateOrderId(userRecharge.getId());
                userBillDetail.setOrderClass(0);
                userBillDetail.setCurrencyId(userAmount.getCurrencyId());
                int insertUserBillDetail = userBillDetailService.insertUserBillDetail(userBillDetail);
                if (insertUserBillDetail <= 0) {
                    throw new RuntimeException("系统繁忙");
                }
            }else {
                throw new RuntimeException("未获取到相应的在线支付订单信息");
            }
            return "{isSuccess:true}";
        }
        return "{isSuccess:false}";
    }
}
