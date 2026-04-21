package com.ruoyi.system.service.impl;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.entity.UserAmount;
import com.ruoyi.common.core.domain.entity.UserInfo;
import com.ruoyi.common.exception.LangException;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.CodeUtils;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.RandomUtil;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.ScheduledTaskExceptionLogMapper;
import com.ruoyi.system.mapper.StakingOrderMapper;
import com.ruoyi.system.mapper.UserInfoMapper;
import com.ruoyi.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 质押订单Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-07-17
 */
@Service
public class StakingOrderServiceImpl implements IStakingOrderService 
{
    @Resource
    private StakingOrderMapper stakingOrderMapper;

    @Autowired
    private IStakingProductService stakingProductService;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Autowired
    private IPlatformCurrencyService platformCurrencyService;

    @Autowired
    private IUserAmountService userAmountService;

    @Autowired
    private IUserBillDetailService userBillDetailService;

    @Autowired
    private IStakingOrderService stakingOrderService;

    @Resource
    private ScheduledTaskExceptionLogMapper scheduledTaskExceptionLogMapper;

    @Autowired
    private IStakingOrderInterestRecordService stakingOrderInterestRecordService;

    /**
     * 查询质押订单
     * 
     * @param id 质押订单主键
     * @return 质押订单
     */
    @Override
    public StakingOrder selectStakingOrderById(Long id)
    {
        return stakingOrderMapper.selectStakingOrderById(id);
    }

    /**
     * 查询质押订单列表
     * 
     * @param stakingOrder 质押订单
     * @return 质押订单
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u", isUserInfo = true)
    public List<StakingOrder> selectStakingOrderList(StakingOrder stakingOrder)
    {
        return stakingOrderMapper.selectStakingOrderList(stakingOrder);
    }

    /**
     * 新增质押订单
     * 
     * @param stakingOrder 质押订单
     * @return 结果
     */
    @Override
    public int insertStakingOrder(StakingOrder stakingOrder)
    {
        stakingOrder.setCreateTime(DateUtils.getNowDate());
        return stakingOrderMapper.insertStakingOrder(stakingOrder);
    }

    /**
     * 修改质押订单
     * 
     * @param stakingOrder 质押订单
     * @return 结果
     */
    @Override
    public int updateStakingOrder(StakingOrder stakingOrder)
    {
        stakingOrder.setUpdateTime(DateUtils.getNowDate());
        return stakingOrderMapper.updateStakingOrder(stakingOrder);
    }

    /**
     * 批量删除质押订单
     * 
     * @param ids 需要删除的质押订单主键
     * @return 结果
     */
    @Override
    public int deleteStakingOrderByIds(Long[] ids)
    {
        return stakingOrderMapper.deleteStakingOrderByIds(ids);
    }

    /**
     * 删除质押订单信息
     * 
     * @param id 质押订单主键
     * @return 结果
     */
    @Override
    public int deleteStakingOrderById(Long id)
    {
        return stakingOrderMapper.deleteStakingOrderById(id);
    }

    /**
     * 质押订单审核
     * @param stakingOrderId 质押订单id
     * @param orderStatus 订单状态 1：通过 3：驳回
     * @return
     */
    @Override
    public int updateStakingOrderStatus(Long stakingOrderId, Integer orderStatus){
        //质押订单信息
        StakingOrder stakingOrder = stakingOrderMapper.selectStakingOrderById(stakingOrderId);
        if (stakingOrder == null) {
            throw new ServiceException("获取质押订单信息异常");
        }
        if (!stakingOrder.getOrderStatus().equals(0)){
            throw new ServiceException("订单状态已审核");
        }
        //实时时间
        Date nowDateTime = new Date();
        //审核通过
        if (orderStatus.equals(1)) {
            stakingOrder.setOrderStatus(1);
            //质押天数
            Integer stakingTime = stakingOrder.getStakingTime();
            //今日的凌晨0点
            Date startOfDay = DateUtils.getStartOfDay(nowDateTime);
            //今日的凌晨1点
            Date interestTime = DateUtils.addHours(DateUtils.getStartOfDay(nowDateTime), 1);
            //如果是在今日派息前下单
            if (nowDateTime.before(interestTime)){
                stakingOrder.setStartDate(DateUtils.addDays(startOfDay, 1));
            }else {
                stakingOrder.setStartDate(DateUtils.addDays(startOfDay, 2));
            }
            //如果有质押天数
            if (stakingTime != null){
                stakingOrder.setEndDate(DateUtils.addDays(stakingOrder.getStartDate(), stakingTime - 1));
            }
        }else {
            //审核驳回
            //退款
            //用户id
            Long userId = stakingOrder.getUserId();
            //币种id
            Long currencyId = stakingOrder.getCurrencyId();
            //用户余额信息
            UserAmount userAmount = userAmountService.getUserAmount(userId, currencyId);
            //变更前余额
            BigDecimal userAmountBefore = userAmount.getAmount();
            //质押订单购买金额
            BigDecimal buyPrice = stakingOrder.getBuyPrice();
            //变更后余额
            BigDecimal userAmountAfter = userAmountBefore.add(buyPrice);
            userAmount.setAmount(userAmountAfter);
            //变更余额
            int updateUserAmount = userAmountService.updateUserAmount(userAmount);
            if (updateUserAmount <= 0){
                throw new ServiceException("系统繁忙");
            }

            //插入账户明细记录
            UserBillDetail userBillDetail = new UserBillDetail();
            userBillDetail.setUserId(userId);
            userBillDetail.setDeType("质押订单驳回本金返还");
            userBillDetail.setDeSummary("质押申请被驳回，金额返还");
            userBillDetail.setOrderAmount(buyPrice);
            userBillDetail.setOrderTime(nowDateTime);
            userBillDetail.setAmountBefore(userAmountBefore);
            userBillDetail.setAmountAfter(userAmountAfter);
            userBillDetail.setRelateOrderId(stakingOrder.getId());
            userBillDetail.setOrderClass(81);
            userBillDetail.setCurrencyId(currencyId);
            int insertUserBillDetail = userBillDetailService.insertUserBillDetail(userBillDetail);
            if (insertUserBillDetail <= 0) {
                throw new ServiceException("系统繁忙");
            }
        }
        stakingOrder.setOrderStatus(orderStatus);
        stakingOrder.setUpdateBy(SecurityUtils.getUsername());
        stakingOrder.setUpdateTime(nowDateTime);
        int updateCount = stakingOrderMapper.updateStakingOrder(stakingOrder);
        if (updateCount <= 0) {
            throw new ServiceException("系统繁忙");
        }
        //日志记录质押订单信息
        HttpUtils.getRequestLogParams().put("stakingOrder",JSONObject.toJSONString(stakingOrder));
        return 1;
    }

    /**
     * 用户质押代币
     * @param stakingProductId 质押产品配置ID
     * @param buyPrice 质押金额
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addStakingOrderOrder(Long stakingProductId, BigDecimal buyPrice) {
        //用户id
        Long userId = SecurityUtils.getUserId();
        //用户信息
        UserInfo userInfo = userInfoMapper.selectUserInfoById(userId);
        //验证用户交易状态
        if (!userInfo.getIsLock().equals(0)) {
            throw new LangException("hint_dealErrorAccountLocked", "下单失败，用户已被锁定");
        }
        //实时时间
        Date nowDateTime = new Date();
        //质押产品配置信息
        StakingProduct stakingProduct = stakingProductService.selectStakingProductById(stakingProductId);
        if (stakingProduct == null){
            throw new LangException(HintConstants.SYSTEM_BUSY, "获取质押产品配置信息异常");
        }
        //产品剩余数量
        BigDecimal remainingQuantity = stakingProduct.getRemainingQuantity();
        if (remainingQuantity != null){
            if (buyPrice.compareTo(remainingQuantity) > 0){
                throw new LangException("hint_84","产品余量不足");
            }
            //更新质押产品剩余数量
            stakingProduct.setRemainingQuantity(stakingProduct.getRemainingQuantity().subtract(buyPrice));
        }
        //vip等级要求
        Integer vipLevelLimit = stakingProduct.getVipLevelLimit();
        if (vipLevelLimit > userInfo.getVipLevel()){
            List<Object> param = new ArrayList<>();
            param.add(vipLevelLimit);
            throw new LangException("hint_49", param, "会员等级需达到" + vipLevelLimit + "才可购买");
        }
        //判断下单金额是否在允许范围内
        if (buyPrice.compareTo(stakingProduct.getMinPrice()) < 0 || buyPrice.compareTo(stakingProduct.getMaxPrice()) > 0){
            List<Object> list = new ArrayList<>();
            list.add(stakingProduct.getMinPrice());
            list.add(stakingProduct.getMaxPrice());
            throw new LangException("hint_21", list, "单笔购买金额范围为" + stakingProduct.getMinPrice() + "~" + stakingProduct.getMaxPrice());
        }
        //币种id
        Long currencyId = stakingProduct.getCurrencyId();
        //币种信息
        PlatformCurrency platformCurrency = platformCurrencyService.selectPlatformCurrencyById(currencyId);
        if (platformCurrency == null){
            throw new LangException(HintConstants.SYSTEM_BUSY, "获取币种信息异常");
        }
        if (!platformCurrency.getStatus().equals(0)){
            throw new LangException(HintConstants.SYSTEM_BUSY, "此币种已禁用");
        }
        //用户钱包信息
        UserAmount userAmount = userAmountService.getUserAmount(userId, currencyId);
        //变更前金额
        BigDecimal userAmountBefore = userAmount.getAmount();
        //用户余额最低限制
        BigDecimal userAmountLimit = stakingProduct.getUserAmountLimit();
        if (userAmountLimit.compareTo(userAmountBefore) > 0){
            List<Object> list = new ArrayList<>();
            list.add(userAmountLimit);
            throw new LangException("hint_16",list,"质押，余额必须达到"+userAmountLimit);
        }
        //判断余额是否充足
        if (buyPrice.compareTo(userAmountBefore) > 0){
            throw new LangException("hint_17","质押失败，余额不足");
        }
        StakingOrder stakingOrder = new StakingOrder();
        stakingOrder.setOrderCode(CodeUtils.generateOrderCode("stakingOrder"));
        stakingOrder.setStakingProductId(stakingProductId);
        stakingOrder.setStakingTime(stakingProduct.getStakingTime());
        stakingOrder.setBuyPrice(buyPrice);
        stakingOrder.setStakingPoolAmount(buyPrice);
        stakingOrder.setUserId(userId);
        stakingOrder.setBreakContractRate(stakingProduct.getBreakContractRate());
        stakingOrder.setDailyIncomeRate(stakingProduct.getFixedIncomeRate());
        stakingOrder.setFloatDailyIncomeMaxRate(stakingProduct.getFloatDailyIncomeMaxRate());
        stakingOrder.setFloatDailyIncomeMinRate(stakingProduct.getFloatDailyIncomeMinRate());
        stakingOrder.setCreateTime(nowDateTime);
        //如果自动审核开启
        if (stakingProduct.getAutoApprove().equals(0)){
            stakingOrder.setOrderStatus(1);
            //质押天数
            Integer stakingTime = stakingProduct.getStakingTime();
            //今日的凌晨0点
            Date startOfDay = DateUtils.getStartOfDay(nowDateTime);
            //今日的凌晨1点
            Date interestTime = DateUtils.addHours(DateUtils.getStartOfDay(nowDateTime), 1);
            //如果是在今日派息前下单
            if (nowDateTime.before(interestTime)){
                stakingOrder.setStartDate(DateUtils.addDays(startOfDay, 1));
            }else {
                stakingOrder.setStartDate(DateUtils.addDays(startOfDay, 2));
            }
            //如果有质押天数
            if (stakingTime != null){
                stakingOrder.setEndDate(DateUtils.addDays(stakingOrder.getStartDate(), stakingTime - 1));
            }
        }
        stakingOrder.setCurrencyId(currencyId);
        //新增质押订单
        int insertStakingOrder = stakingOrderMapper.insertStakingOrder(stakingOrder);
        if (insertStakingOrder <= 0){
            throw new LangException(HintConstants.SYSTEM_BUSY, "新增质押订单异常");
        }
        stakingOrder.setStakingName(stakingProduct.getStakingName());
        stakingOrder.setCurrencyName(platformCurrency.getCurrencyName());
        //日志记录质押订单信息
        HttpUtils.getRequestLogParams().put("stakingOrder", JSONObject.toJSONString(stakingOrder));
        int updateStakingProduct = stakingProductService.updateStakingProduct(stakingProduct);
        if (updateStakingProduct <= 0){
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
        //用户余额变更后
        BigDecimal userAmountAfter = userAmountBefore.subtract(buyPrice);
        userAmount.setAmount(userAmountAfter);
        //更新用户余额
        int updateUserAmount = userAmountService.updateUserAmount(userAmount);
        if (updateUserAmount <= 0){
            throw new LangException(HintConstants.SYSTEM_BUSY, "更新用户钱包异常");
        }
        //用户流水记录
        UserBillDetail userBillDetail = new UserBillDetail();
        userBillDetail.setUserId(userId);
        userBillDetail.setDeType("质押代币扣除");
        userBillDetail.setDeSummary("质押代币扣除");
        userBillDetail.setOrderAmount(buyPrice.negate());
        userBillDetail.setOrderTime(nowDateTime);
        userBillDetail.setAmountBefore(userAmountBefore);
        userBillDetail.setAmountAfter(userAmountAfter);
        userBillDetail.setRelateOrderId(stakingOrder.getId());
        userBillDetail.setOrderClass(78);
        userBillDetail.setCurrencyId(currencyId);
        int insert = userBillDetailService.insertUserBillDetail(userBillDetail);
        if (insert <= 0) {
            throw new LangException(HintConstants.SYSTEM_BUSY, "插入质押代币扣除流水异常");
        }
        return 1;
    }

    /**
     * 用户赎回质押金
     * @param stakingOrderId 质押订单ID
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int redemption(Long stakingOrderId) {
        //用户id
        Long userId = SecurityUtils.getUserId();
        //质押订单信息
        StakingOrder stakingOrder = stakingOrderMapper.selectStakingOrderById(stakingOrderId);
        if (stakingOrder == null){
            throw new LangException(HintConstants.SYSTEM_BUSY, "获取质押订单信息异常");
        }
        //日志记录质押订单信息
        HttpUtils.getRequestLogParams().put("stakingOrder", JSONObject.toJSONString(stakingOrder));
        //校验用户信息
        if (!stakingOrder.getUserId().equals(userId)){
            throw new ServiceException("校验用户信息异常", HttpStatus.UNAUTHORIZED);
        }
        //如果订单不是进行中
        if (!stakingOrder.getOrderStatus().equals(1)){
            throw new LangException(HintConstants.SYSTEM_BUSY, "订单未在进行中");
        }
        //质押池金额
        BigDecimal stakingPoolAmount = stakingOrder.getStakingPoolAmount();
        //质押天数
        Integer stakingTime = stakingOrder.getStakingTime();
        //币种ID
        Long currencyId = stakingOrder.getCurrencyId();
        //用户钱包
        UserAmount userAmount = userAmountService.getUserAmount(userId, currencyId);
        //金额变更前
        BigDecimal userAmountBefore = userAmount.getAmount();
        //金额变更后
        BigDecimal userAmountAfter = userAmountBefore.add(stakingPoolAmount);
        //用户流水记录
        UserBillDetail userBillDetail = new UserBillDetail();
        userBillDetail.setUserId(userId);
        userBillDetail.setDeType("质押结算");
        userBillDetail.setDeSummary("质押结算");
        userBillDetail.setOrderAmount(stakingPoolAmount);
        userBillDetail.setOrderTime(new Date());
        userBillDetail.setAmountBefore(userAmountBefore);
        userBillDetail.setAmountAfter(userAmountAfter);
        userBillDetail.setRelateOrderId(stakingOrder.getId());
        userBillDetail.setOrderClass(79);
        userBillDetail.setCurrencyId(currencyId);
        int insert = userBillDetailService.insertUserBillDetail(userBillDetail);
        if (insert <= 0) {
            throw new LangException(HintConstants.SYSTEM_BUSY, "插入质押代币赎回订单异常");
        }
        //如果不是灵活质押，并且是提前赎回,支付违约金
        if (stakingTime != null && stakingOrder.getAlreadyInterestCount() < stakingTime){
            //违约金比例
            BigDecimal breakContractRate = stakingOrder.getBreakContractRate();
            //违约金
            BigDecimal breakContractAmount = stakingPoolAmount.multiply(breakContractRate).multiply(new BigDecimal("0.01")).multiply(new BigDecimal(stakingTime - stakingOrder.getAlreadyInterestCount())).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            //用户余额变更后
            userAmountAfter = userAmountAfter.subtract(breakContractAmount);
            //用户流水记录
            UserBillDetail userBillDetail2 = new UserBillDetail();
            userBillDetail2.setUserId(userId);
            userBillDetail2.setDeType("质押提前赎回违约金");
            userBillDetail2.setDeSummary("质押提前赎回违约金");
            userBillDetail2.setOrderAmount(breakContractAmount.negate());
            userBillDetail2.setOrderTime(new Date());
            userBillDetail2.setAmountBefore(userAmountAfter.add(breakContractAmount));
            userBillDetail2.setAmountAfter(userAmountAfter);
            userBillDetail2.setRelateOrderId(stakingOrder.getId());
            userBillDetail2.setOrderClass(80);
            userBillDetail2.setCurrencyId(currencyId);
            int insertUserBillDetail = userBillDetailService.insertUserBillDetail(userBillDetail2);
            if (insertUserBillDetail <= 0) {
                throw new LangException(HintConstants.SYSTEM_BUSY, "插入质押提前赎回违约金流水异常");
            }
            //日志记录质押订单赎回违约金
            HttpUtils.getRequestLogParams().put("breakContractAmount", breakContractAmount);
        }
        //更新用户钱包信息
        userAmount.setAmount(userAmountAfter);
        int updateUserAmount = userAmountService.updateUserAmount(userAmount);
        if (updateUserAmount == 0){
            throw new LangException(HintConstants.SYSTEM_BUSY, "更新用户钱包信息异常");
        }
        //更新质押订单信息
        //订单状态变更为已赎回
        stakingOrder.setOrderStatus(4);
        int updateStakingOrder = stakingOrderMapper.updateStakingOrder(stakingOrder);
        if (updateStakingOrder == 0){
            throw new LangException(HintConstants.SYSTEM_BUSY, "更新质押订单信息异常");
        }
        return 1;
    }

    /**
     * 质押派息定时任务
     */
    @Override
    public void payInterestTask() {
        //获取需要派息的质押订单
        StakingOrder stakingOrder = new StakingOrder();
        stakingOrder.setOrderStatus(1);
        //符合发放收益条件时间的
        stakingOrder.getParams().put("notDoneOrder", 0);
        List<StakingOrder> stakingOrders = stakingOrderMapper.selectStakingOrderList(stakingOrder);
        if (stakingOrders.size() == 0){
            return;
        }
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        try {
            //派发利息
            for (int i = 0; i < stakingOrders.size(); i++) {
                StakingOrder stakingOrderVo = stakingOrders.get(i);
                executorService.execute(()->{
                    try {
                        stakingOrderService.doPayInterestTask(stakingOrderVo);
                    } catch (Exception e) {
                        //记录异常日志
                        ScheduledTaskExceptionLog scheduledTaskExceptionLog = new ScheduledTaskExceptionLog();
                        scheduledTaskExceptionLog.setJobName("质押派发利息任务");
                        scheduledTaskExceptionLog.setExceptionInfo(e.getMessage());
                        scheduledTaskExceptionLog.setCreateTime(new Date());
                        scheduledTaskExceptionLog.setExceptionInfoDetail(ExceptionUtil.stacktraceToString(e));
                        scheduledTaskExceptionLog.setRelateInfo("id:" + stakingOrderVo.getId());
                        scheduledTaskExceptionLog.setType(19);
                        scheduledTaskExceptionLogMapper.insertScheduledTaskExceptionLog(scheduledTaskExceptionLog);
                        throw new RuntimeException(e);
                    }
                });
            }
        }catch (Exception e){
            throw new RuntimeException("创建定时任务异常");
        }finally {
            executorService.shutdown();
        }
    }

    /**
     * 质押派息定时任务
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doPayInterestTask(StakingOrder stakingOrder) {
        //实时时间
        Date nowDateTime = new Date();
        //一天前的时间
        Date oneDayBefore = DateUtils.getDateBeforeOrAfterDate(nowDateTime, Calendar.DAY_OF_YEAR, -1);
        //如果购买未满24小时，不结算
        if (stakingOrder.getCreateTime().after(oneDayBefore)) {
            return;
        }
        //日收益率
        BigDecimal dailyIncomeRate = stakingOrder.getDailyIncomeRate();
        //日收益最高浮动
        BigDecimal maxRate = stakingOrder.getFloatDailyIncomeMaxRate();
        //日收益最低浮动
        BigDecimal minRate = stakingOrder.getFloatDailyIncomeMinRate();
        //浮动
        BigDecimal randomRate = RandomUtil.randomBigDecimal(minRate, maxRate);
        //本次收益率
        dailyIncomeRate = dailyIncomeRate.add(randomRate);
        //质押池金额
        BigDecimal stakingPoolAmount = stakingOrder.getStakingPoolAmount();
        //日收益
        BigDecimal dailyIncome = stakingPoolAmount.multiply(dailyIncomeRate).divide(new BigDecimal(100), Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
        //已派发次数
        Integer alreadyInterestCount = stakingOrder.getAlreadyInterestCount();
        //用户id
        Long userId = stakingOrder.getUserId();
        //货币id
        Long currencyId = stakingOrder.getCurrencyId();
        //已派发次数+1
        alreadyInterestCount = alreadyInterestCount + 1;
        //质押池金额叠加
        stakingPoolAmount = stakingPoolAmount.add(dailyIncome);
        //如果已经完成所有派发,订单状态变更为已完成,并且返还本金
        if (stakingOrder.getStakingTime() != null && alreadyInterestCount.equals(stakingOrder.getStakingTime())) {
            //订单状态变更为已完成
            stakingOrder.setOrderStatus(2);
            //用户钱包
            UserAmount userAmount = userAmountService.getUserAmount(userId, currencyId);
            //金额变更前
            BigDecimal userAmountBefore = userAmount.getAmount();
            //金额变更后
            BigDecimal userAmountAfter = userAmountBefore.add(stakingPoolAmount);
            //更新钱包余额
            userAmount.setAmount(userAmountAfter);
            int updateUserAmount = userAmountService.updateUserAmount(userAmount);
            if (updateUserAmount <= 0) {
                throw new RuntimeException("更新钱包余额异常");
            }
            //用户流水记录（质押到期结算）
            UserBillDetail userBillDetail = new UserBillDetail();
            userBillDetail.setUserId(userId);
            userBillDetail.setDeType("质押结算");
            userBillDetail.setDeSummary("质押结算");
            userBillDetail.setOrderAmount(stakingPoolAmount);
            userBillDetail.setOrderTime(nowDateTime);
            userBillDetail.setAmountBefore(userAmountBefore);
            userBillDetail.setAmountAfter(userAmountAfter);
            userBillDetail.setRelateOrderId(stakingOrder.getId());
            userBillDetail.setOrderClass(79);
            userBillDetail.setCurrencyId(currencyId);
            int insertUserBillDetail2 = userBillDetailService.insertUserBillDetail(userBillDetail);
            if (insertUserBillDetail2 <= 0) {
                throw new RuntimeException("插入质押到期结算流水记录异常");
            }
        }
        //插入收益发放记录
        StakingOrderInterestRecord stakingOrderInterestRecord = new StakingOrderInterestRecord();
        stakingOrderInterestRecord.setUserId(userId);
        stakingOrderInterestRecord.setStakingOrderId(stakingOrder.getId());
        stakingOrderInterestRecord.setStakingPoolAmount(stakingPoolAmount.subtract(dailyIncome));
        stakingOrderInterestRecord.setDailyIncomeRate(dailyIncomeRate);
        stakingOrderInterestRecord.setPayTime(nowDateTime);
        stakingOrderInterestRecord.setPayAmount(dailyIncome);
        int insertStakingOrderInterestRecord = stakingOrderInterestRecordService.insertStakingOrderInterestRecord(stakingOrderInterestRecord);
        if (insertStakingOrderInterestRecord <= 0) {
            throw new RuntimeException("插入质押收益发放记录异常");
        }

        //更新质押订单信息
        stakingOrder.setStakingPoolAmount(stakingPoolAmount);
        stakingOrder.setAlreadyInterestCount(alreadyInterestCount);
        stakingOrder.setLastInterestTime(nowDateTime);
        int updateStakingOrder = stakingOrderMapper.updateStakingOrder(stakingOrder);
        if (updateStakingOrder <= 0) {
            throw new RuntimeException("更新质押订单信息异常");
        }
    }

}
