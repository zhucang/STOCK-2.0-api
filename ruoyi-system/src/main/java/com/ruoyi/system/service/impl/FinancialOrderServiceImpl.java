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
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.service.*;
import com.ruoyi.system.utils.cache.CacheUtils;
import com.ruoyi.system.utils.telegram.TelegramUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * 理财订单Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-11-26
 */
@Service
public class FinancialOrderServiceImpl implements IFinancialOrderService 
{
    @Resource
    private FinancialOrderMapper financialOrderMapper;

    @Autowired
    private ISwitchSetService switchSetService;

    @Autowired
    private IUserAmountService userAmountService;

    @Autowired
    private IUserBillDetailService userBillDetailService;

    @Autowired
    private IFinancialProductService financialProductService;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private ScheduledTaskExceptionLogMapper scheduledTaskExceptionLogMapper;

    @Autowired
    private IFinancialOrderService financialOrderService;

    @Resource
    private FinancialPayInterestRecordMapper financialPayInterestRecordMapper;

    @Autowired
    private IPlatformCurrencyService platformCurrencyService;

    @Resource
    private UserTeamLevelLineMapper userTeamLevelLineMapper;

    @Resource
    private UserRebateRateMapper userRebateRateMapper;

    @Resource
    private UserCommissionRecordMapper userCommissionRecordMapper;

    /**
     * 查询理财订单
     * 
     * @param id 理财订单主键
     * @return 理财订单
     */
    @Override
    public FinancialOrder selectFinancialOrderById(Long id)
    {
        return financialOrderMapper.selectFinancialOrderById(id);
    }

    /**
     * 查询理财订单列表
     * 
     * @param financialOrder 理财订单
     * @return 理财订单
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u", isUserInfo = true)
    public List<FinancialOrder> selectFinancialOrderList(FinancialOrder financialOrder)
    {
        return financialOrderMapper.selectFinancialOrderList(financialOrder);
    }

    /**
     * 填充其他信息
     */
    @Override
    public void fillOtherInfo(List<FinancialOrder> financialOrders) {
        if (financialOrders.size() == 0) {
            return;
        }
        //理财订单IDS
        List<Long> financialOrderIds = financialOrders.stream().map(a -> a.getId()).collect(Collectors.toList());
        //收益统计map
        Map<Long, Map<Long, BigDecimal>> map = financialPayInterestRecordMapper.getTotalPayAmountByOrderId(financialOrderIds);
        //遍历
        for (int i = 0; i < financialOrders.size(); i++) {
            Map<Long, BigDecimal> mapVo = map.get(financialOrders.get(i).getId());
            financialOrders.get(i).getParams().put("totalPayAmount", mapVo != null ? mapVo.get("totalPayAmount") : BigDecimal.ZERO);
        }
    }

    /**
     * 新增理财订单
     * 
     * @param financialOrder 理财订单
     * @return 结果
     */
    @Override
    public int insertFinancialOrder(FinancialOrder financialOrder)
    {
        return financialOrderMapper.insertFinancialOrder(financialOrder);
    }

    /**
     * 修改理财订单
     * 
     * @param financialOrder 理财订单
     * @return 结果
     */
    @Override
    public int updateFinancialOrder(FinancialOrder financialOrder)
    {
        return financialOrderMapper.updateFinancialOrder(financialOrder);
    }

    /**
     * 批量删除理财订单
     * 
     * @param ids 需要删除的理财订单主键
     * @return 结果
     */
    @Override
    public int deleteFinancialOrderByIds(Long[] ids)
    {
        return financialOrderMapper.deleteFinancialOrderByIds(ids);
    }

    /**
     * 删除理财订单信息
     * 
     * @param id 理财订单主键
     * @return 结果
     */
    @Override
    public int deleteFinancialOrderById(Long id)
    {
        return financialOrderMapper.deleteFinancialOrderById(id);
    }

    /**
     * 理财订单审核
     * @param financialOrderId 理财订单id
     * @param orderStatus 订单状态 1：通过 2：驳回
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateFinancialOrderStatus(Long financialOrderId, Integer orderStatus){
        //理财订单信息
        FinancialOrder financialOrder = financialOrderMapper.selectFinancialOrderById(financialOrderId);
        if (financialOrder == null) {
            throw new ServiceException("获取理财单信息异常");
        }
        if (!financialOrder.getOrderStatus().equals(0)){
            throw new ServiceException("订单状态已审核");
        }
        //实时时间
        Date nowDateTime = new Date();
        //审核通过
        if (orderStatus.equals(1)) {
            //今日的凌晨1点
            Date interestTime = DateUtils.getDateBeforeOrAfterDate(DateUtils.getStartOfDay(nowDateTime),Calendar.HOUR_OF_DAY,1);
            //如果是在今日派息前下单
            if (nowDateTime.before(interestTime)){
                financialOrder.setStartDate(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,DateUtils.getDateBeforeOrAfterDate(nowDateTime,Calendar.DAY_OF_YEAR,1)));
                financialOrder.setEndDate(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,DateUtils.getDateBeforeOrAfterDate(nowDateTime,Calendar.DAY_OF_YEAR,financialOrder.getFinancialTime())));
            }else {
                financialOrder.setStartDate(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,DateUtils.getDateBeforeOrAfterDate(nowDateTime,Calendar.DAY_OF_YEAR,2)));
                financialOrder.setEndDate(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,DateUtils.getDateBeforeOrAfterDate(nowDateTime,Calendar.DAY_OF_YEAR,financialOrder.getFinancialTime()+1)));
            }
            // 返佣
            rebate(financialOrder);
        }else {
            //审核驳回
            //退款
            //用户id
            Long userId = financialOrder.getUserId();
            //币种id
            Long currencyId = financialOrder.getCurrencyId();
            //用户余额信息
            UserAmount userAmount = userAmountService.getUserAmount(userId, currencyId);
            //变更前余额
            BigDecimal userAmountBefore = userAmount.getAmount();
            //理财订单购买金额
            BigDecimal buyPrice = financialOrder.getBuyPrice();
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
            userBillDetail.setDeType("理财订单驳回本金返还");
            userBillDetail.setDeSummary("理财申请被驳回，金额返还");
            userBillDetail.setOrderAmount(buyPrice);
            userBillDetail.setOrderTime(nowDateTime);
            userBillDetail.setAmountBefore(userAmountBefore);
            userBillDetail.setAmountAfter(userAmountAfter);
            userBillDetail.setRelateOrderId(financialOrder.getId());
            userBillDetail.setOrderClass(62);
            userBillDetail.setCurrencyId(currencyId);
            int insertUserBillDetail = userBillDetailService.insertUserBillDetail(userBillDetail);
            if (insertUserBillDetail <= 0) {
                throw new ServiceException("系统繁忙");
            }
        }
        financialOrder.setOrderStatus(orderStatus);
        financialOrder.setUpdateBy(SecurityUtils.getUsername());
        financialOrder.setUpdateTime(nowDateTime);
        int updateCount = financialOrderMapper.updateFinancialOrder(financialOrder);
        if (updateCount <= 0) {
            throw new ServiceException("系统繁忙");
        }
        //日志记录理财订单信息
        HttpUtils.getRequestLogParams().put("financialOrder",JSONObject.toJSONString(financialOrder));
        return 1;

    }

    /**
     * 理财返佣
     * @param financialOrder 理财订单信息
     */
    void rebate(FinancialOrder financialOrder){
        // 共有多少个上级需要返利
        Integer rebateNum = CacheUtils.getOtherValueByKey("team_userTeamLevel",Integer.class);
        if (rebateNum == null || rebateNum <= 0){
            return;
        }
        // 需要返佣的上级用户List
        List<UserTeamLevelLine> supTeamLine = userTeamLevelLineMapper.getSupTeamLine(financialOrder.getUserId(), rebateNum, 0);
        // 获取返佣比率
        UserRebateRate userRebateRate = new UserRebateRate();
        userRebateRate.setRebateType(1);
        List<UserRebateRate> userRebateRates = userRebateRateMapper.selectUserRebateRateList(userRebateRate);
        Map<Integer, BigDecimal> RebateRateMap = userRebateRates.stream().collect(Collectors.toMap(UserRebateRate::getRebateLevel, a -> a.getRebateRate()));

        // 理财订单ID
        Long userRechargeOrderId = financialOrder.getId();
        // 币种
        Long currencyId = financialOrder.getCurrencyId();
        // 购买金额
        BigDecimal buyPrice = financialOrder.getBuyPrice();
        //
        for (int i = 0; i < supTeamLine.size(); i++) {
            // 团队关系信息
            UserTeamLevelLine userTeamLevelLine = supTeamLine.get(i);
            // 上级用户id
            Long supUserId = userTeamLevelLine.getSupUserId();
            // 上级用户信息
            UserInfo supUser = userInfoMapper.selectUserInfoById(supUserId);
            if (supUser == null){
                continue;
            }
            // 等级关系
            Integer teamLevel = userTeamLevelLine.getTeamLevel();
            // 返佣比例
            BigDecimal rebateRate = RebateRateMap.get(teamLevel);
            // 如果返佣比例是空或者比例不大于0，则跳过
            if (rebateRate == null || rebateRate.compareTo(BigDecimal.ZERO) <= 0){
                continue;
            }
            // 返利额度
            BigDecimal rebateAmount = buyPrice.multiply(rebateRate).multiply(new BigDecimal("0.01")).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            //如果返利金额不大于0，跳过
            if (rebateAmount.compareTo(BigDecimal.ZERO) <= 0){
                continue;
            }
            // 获取上级钱包信息
            UserAmount userAmount = userAmountService.getUserAmount(supUserId, currencyId);
            // 余额变更前
            BigDecimal userAmountBefore = userAmount.getAmount();
            // 余额变更后
            BigDecimal userAmountAfter = userAmountBefore.add(rebateAmount);
            // 更新返利后的总额
            userAmount.setAmount(userAmountAfter);
            int updateUserAmount = userAmountService.updateUserAmount(userAmount);
            if (updateUserAmount <= 0){
                throw new RuntimeException("系统繁忙");
            }

            // 理财返佣收入明细
            UserBillDetail userBillDetail = new UserBillDetail();
            userBillDetail.setUserId(supUserId);
            userBillDetail.setDeType("下级理财下单返佣");
            userBillDetail.setDeSummary("下级理财下单返佣");
            userBillDetail.setOrderAmount(rebateAmount);
            userBillDetail.setOrderTime(new Date());
            userBillDetail.setAmountBefore(userAmountBefore);
            userBillDetail.setAmountAfter(userAmountAfter);
            userBillDetail.setRelateOrderId(userRechargeOrderId);
            userBillDetail.setOrderClass(82);
            userBillDetail.setCurrencyId(userAmount.getCurrencyId());
            int insertUserBillDetail = userBillDetailService.insertUserBillDetail(userBillDetail);
            if (insertUserBillDetail <= 0) {
                throw new RuntimeException("系统繁忙");
            }

            // 返佣记录
            UserCommissionRecord userCommissionRecord = new UserCommissionRecord();
            userCommissionRecord.setSuperId(supUserId);
            userCommissionRecord.setLowerId(financialOrder.getUserId());
            userCommissionRecord.setCommissionLevel(teamLevel);
            userCommissionRecord.setCommissionAmount(rebateAmount);
            userCommissionRecord.setCommissionProfit(rebateRate);
            userCommissionRecord.setOrderCodeSource(financialOrder.getOrderCode());
            userCommissionRecord.setOrderCodeCommission(String.valueOf(userBillDetail.getId()));
            userCommissionRecord.setCreateTime(new Date());
            userCommissionRecord.setCurrencyId(currencyId);
            userCommissionRecord.setCommissionType(1);
            int insertUserCommissionRecord = userCommissionRecordMapper.insertUserCommissionRecord(userCommissionRecord);
            if (insertUserCommissionRecord <= 0){
                throw new RuntimeException();
            }
        }
    }

    /**
     * 理财产品人工赎回
     * @param id 理财订单id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int manualRedemption(Long id){
        //理财订单信息
        FinancialOrder financialOrder = financialOrderMapper.selectFinancialOrderById(id);
        if (financialOrder == null){
            throw new ServiceException("获取订单信息异常，请稍后重新尝试");
        }
        //日志记录理财订单信息
        HttpUtils.getRequestLogParams().put("financialOrder",JSONObject.toJSONString(financialOrder));
        //日志记录用户id
        HttpUtils.getRequestLogParams().put("userId",financialOrder.getUserId());
        //如果订单不是进行中
        if (!financialOrder.getOrderStatus().equals(1)){
            throw new ServiceException("此订单状态不是进行中");
        }
        //用户id
        Long userId = financialOrder.getUserId();
        UserInfo userInfo = userInfoMapper.selectUserInfoById(userId);
        if (userInfo == null){
            throw new ServiceException("查找订单的用户信息异常，请稍后重新尝试");
        }
        //购买金额
        BigDecimal buyPrice = financialOrder.getBuyPrice();
        //理财产品天数
        Integer financialTime = financialOrder.getFinancialTime();
        //货币id
        Long currencyId = financialOrder.getCurrencyId();
        //用户钱包
        UserAmount userAmount = userAmountService.getUserAmount(userId, currencyId);
        //金额变更前
        BigDecimal userAmountBefore = userAmount.getAmount();
        //金额变更后
        BigDecimal userAmountAfter = userAmountBefore.add(buyPrice);

        //用户流水记录
        UserBillDetail userBillDetail = new UserBillDetail();
        userBillDetail.setUserId(userId);
        userBillDetail.setDeType("理财产品本金退回");
        userBillDetail.setDeSummary("理财产品本金退回");
        userBillDetail.setOrderAmount(buyPrice);
        userBillDetail.setOrderTime(new Date());
        userBillDetail.setAmountBefore(userAmountBefore);
        userBillDetail.setAmountAfter(userAmountAfter);
        userBillDetail.setRelateOrderId(financialOrder.getId());
        userBillDetail.setOrderClass(19);
        userBillDetail.setCurrencyId(currencyId);
        int insert = userBillDetailService.insertUserBillDetail(userBillDetail);
        if (insert <= 0) {
            throw new ServiceException("系统繁忙");
        }
        //如果是提前赎回,支付违约金
        if (financialOrder.getAlreadyInterestCount() < financialTime){
            //违约金比例
            BigDecimal breakContractRate = financialOrder.getBreakContractRate();
            //违约金
            BigDecimal breakContractAmount = buyPrice.multiply(breakContractRate).multiply(new BigDecimal(0.01)).multiply(new BigDecimal(financialTime-financialOrder.getAlreadyInterestCount())).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            //用户余额变更后
            userAmountAfter = userAmountAfter.subtract(breakContractAmount);
            //用户流水记录
            UserBillDetail userBillDetail2 = new UserBillDetail();
            userBillDetail2.setUserId(userId);
            userBillDetail2.setDeType("理财产品提前赎回违约金");
            userBillDetail2.setDeSummary("理财产品提前赎回违约金");
            userBillDetail2.setOrderAmount(breakContractAmount.negate());
            userBillDetail2.setOrderTime(new Date());
            userBillDetail2.setAmountBefore(userAmountAfter.add(breakContractAmount));
            userBillDetail2.setAmountAfter(userAmountAfter);
            userBillDetail2.setRelateOrderId(financialOrder.getId());
            userBillDetail2.setOrderClass(20);
            userBillDetail2.setCurrencyId(currencyId);
            int insertUserBillDetail = userBillDetailService.insertUserBillDetail(userBillDetail2);
            if (insertUserBillDetail <= 0) {
                throw new ServiceException("系统繁忙");
            }
            //日志记录理财订单赎回违约金
            HttpUtils.getRequestLogParams().put("breakContractAmount", breakContractAmount);
        }
        FinancialOrder financialOrderVo = new FinancialOrder();
        financialOrderVo.setId(financialOrder.getId());
        //订单状态变更为已完成
        financialOrderVo.setOrderStatus(4);
        financialOrderVo.setUpdateBy(SecurityUtils.getUsername());
        financialOrderVo.setUpdateTime(new Date());
        financialOrderVo.setSqlVersion(financialOrder.getSqlVersion());
        int updateFinancialOrder = financialOrderMapper.updateFinancialOrder(financialOrderVo);
        if (updateFinancialOrder <= 0){
            throw new ServiceException("系统繁忙");
        }
        //变更用户金额
        userAmount.setAmount(userAmountAfter);
        int updateUserAmount = userAmountService.updateUserAmount(userAmount);
        if (updateUserAmount <= 0){
            throw new ServiceException("系统繁忙");
        }
        return 1;
    }

    /**
     * 理财产品人工结算
     * @param id 理财订单id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int manualSettlement(Long id){
        //理财订单信息
        FinancialOrder financialOrder = financialOrderMapper.selectFinancialOrderById(id);
        if (financialOrder == null){
            throw new ServiceException("获取订单信息异常，请稍后重新尝试");
        }
        //日志记录理财订单信息
        HttpUtils.getRequestLogParams().put("financialOrder",JSONObject.toJSONString(financialOrder));
        //如果订单不是进行中
        if (!financialOrder.getOrderStatus().equals(1)){
            throw new ServiceException("此订单状态不是进行中");
        }
        //用户id
        Long userId = financialOrder.getUserId();
        UserInfo userInfo = userInfoMapper.selectUserInfoById(userId);
        if (userInfo == null){
            throw new ServiceException("查找订单的用户信息异常，请稍后重新尝试");
        }
        //购买金额
        BigDecimal buyPrice = financialOrder.getBuyPrice();
        //货币id
        Long currencyId = financialOrder.getCurrencyId();
        //用户钱包
        UserAmount userAmount = userAmountService.getUserAmount(userId, currencyId);
        //金额变更前
        BigDecimal userAmountBefore = userAmount.getAmount();
        //金额变更后
        BigDecimal userAmountAfter = userAmountBefore.add(buyPrice);

        //用户流水记录
        UserBillDetail userBillDetail = new UserBillDetail();
        userBillDetail.setUserId(userId);
        userBillDetail.setDeType("理财产品本金退回");
        userBillDetail.setDeSummary("理财产品本金退回");
        userBillDetail.setOrderAmount(buyPrice);
        userBillDetail.setOrderTime(new Date());
        userBillDetail.setAmountBefore(userAmountBefore);
        userBillDetail.setAmountAfter(userAmountAfter);
        userBillDetail.setRelateOrderId(financialOrder.getId());
        userBillDetail.setOrderClass(19);
        userBillDetail.setCurrencyId(currencyId);
        int insert = userBillDetailService.insertUserBillDetail(userBillDetail);
        if (insert <= 0) {
            throw new ServiceException("系统繁忙");
        }

        //更新理财订单状态为已完成
        financialOrder.setOrderStatus(2);
        financialOrder.setUpdateBy(SecurityUtils.getUsername());
        financialOrder.setUpdateTime(new Date());
        int updateFinancialOrder = financialOrderMapper.updateFinancialOrder(financialOrder);
        if (updateFinancialOrder <= 0){
            throw new ServiceException("系统繁忙");
        }
        //变更用户金额
        userAmount.setAmount(userAmountAfter);
        int updateUserAmount = userAmountService.updateUserAmount(userAmount);
        if (updateUserAmount <= 0){
            throw new ServiceException("系统繁忙");
        }
        return 1;
    }

    /**
     * 购买理财订单
     * @param financialProductId 理财产品id
     * @param buyPrice 购买金额
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addFinancialOrder(Long financialProductId, BigDecimal buyPrice){
        //用户id
        Long userId = SecurityUtils.getUserId();
        //用户信息
        UserInfo userInfo = userInfoMapper.selectUserInfoById(userId);
        //验证用户交易状态
        if (!userInfo.getIsLock().equals(0)) {
            throw new LangException("hint_dealErrorAccountLocked","下单失败，用户已被锁定");
        }
        //实时时间
        Date nowDateTime = new Date();
        //今日购买次数
        Integer dayBuyCount = financialOrderMapper.getUserBuyCountByPeriodOfTime(userId, DateUtils.getDateBeforeOrAfterDate(nowDateTime, Calendar.DAY_OF_YEAR, -1),nowDateTime);
        //用户理财每日可购买次数
        Integer userFinancialMaxBuyCountEveryDay = CacheUtils.getOtherValueByKey("user_financial_max_buy_count_every_day", Integer.class);
        if (userFinancialMaxBuyCountEveryDay != null && dayBuyCount >= userFinancialMaxBuyCountEveryDay){
            throw new LangException("hint_85","已达到每日可购买上限");
        }
        //本月购买次数
        Integer monthBuyCount = financialOrderMapper.getUserBuyCountByPeriodOfTime(userId, DateUtils.getDateBeforeOrAfterDate(nowDateTime, Calendar.MONTH, -1),nowDateTime);
        //用户理财每月可购买次数
        Integer userFinancialMaxBuyCountEveryMonth = CacheUtils.getOtherValueByKey("user_financial_max_buy_count_every_month", Integer.class);
        if (userFinancialMaxBuyCountEveryMonth != null && monthBuyCount >= userFinancialMaxBuyCountEveryMonth){
            throw new LangException("hint_86","已达到每月可购买上限");
        }
        //理财产品信息
        FinancialProduct financialProduct = financialProductService.selectFinancialProductById(financialProductId);
        if (financialProduct == null){
            throw new LangException(HintConstants.SYSTEM_BUSY,"获取理财产品信息异常");
        }
        //日志记录理财产品名称
        HttpUtils.getRequestLogParams().put("financialName",financialProduct.getFinancialName());
        //产品剩余数量
        BigDecimal remainingQuantity = financialProduct.getRemainingQuantity();
        if (remainingQuantity != null){
            if (buyPrice.compareTo(remainingQuantity) > 0){
                throw new LangException("hint_84","产品余量不足");
            }
            //更新理财产品剩余数量
            financialProduct.setRemainingQuantity(financialProduct.getRemainingQuantity().subtract(buyPrice));
        }
        //vip等级要求
        Integer vipLevelLimit = financialProduct.getVipLevelLimit();
        if (vipLevelLimit > userInfo.getVipLevel()){
            List<Object> param = new ArrayList<>();
            param.add(vipLevelLimit);
            throw new LangException("hint_49",param,"会员等级需达到"+vipLevelLimit+"才可购买");
        }
        //判断下单金额是否在允许范围内
        if (buyPrice.compareTo(financialProduct.getMinPrice()) < 0 || buyPrice.compareTo(financialProduct.getMaxPrice()) > 0){
            List<Object> list = new ArrayList<>();
            list.add(financialProduct.getMinPrice());
            list.add(financialProduct.getMaxPrice());
            throw new LangException("hint_21",list,"单笔购买金额最低为"+ financialProduct.getMinPrice());
        }
        // 用户限购数量
        Integer userBuyLimit = financialProduct.getUserBuyLimit();
        if (userBuyLimit > 0) {
            // 用户已购数量
            FinancialOrder financialOrderSearch = new FinancialOrder();
            financialOrderSearch.setUserId(userId);
            financialOrderSearch.setFinancialProductId(financialProductId);
            financialOrderSearch.getParams().put("asideRejectOrder", "1");
            List<FinancialOrder> financialOrders = financialOrderMapper.selectFinancialOrderList(financialOrderSearch);
            if (financialOrders.size() >= userBuyLimit) {
                List<Object> params = new ArrayList<>();
                params.add(userBuyLimit);
                throw new LangException("hint_98", params, "该产品用户限购" + userBuyLimit + "次");
            }
        }
        //币种id
        Long currencyId = financialProduct.getCurrencyId();
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
        //用户钱包信息
        UserAmount userAmount = userAmountService.getUserAmount(userId, currencyId);
        //变更前金额
        BigDecimal userAmountBefore = userAmount.getAmount();
        //用户余额最低限制
        BigDecimal userAmountLimit = financialProduct.getUserAmountLimit();
        if (userAmountLimit.compareTo(userAmountBefore) > 0){
            List<Object> list = new ArrayList<>();
            list.add(userAmountLimit);
            throw new LangException("hint_16",list,"购买理财产品失败，余额必须达到"+userAmountLimit+"才可购买该理财产品");
        }
        //判断余额是否充足
        if (buyPrice.compareTo(userAmountBefore) > 0){
            throw new LangException("hint_17","购买理财产品失败，余额不足");
        }
        FinancialOrder financialOrder = new FinancialOrder();
        financialOrder.setOrderCode(CodeUtils.generateOrderCode("FinancialOrder"));
        financialOrder.setFinancialProductId(financialProductId);
        financialOrder.setFinancialTime(financialProduct.getFinancialTime());
        financialOrder.setBuyPrice(buyPrice);
        financialOrder.setUserId(userId);
        financialOrder.setBreakContractRate(financialProduct.getBreakContractRate());
        financialOrder.setDailyIncomeRate(financialProduct.getFixedIncomeRate());
        financialOrder.setFloatDailyIncomeMaxRate(financialProduct.getFloatDailyIncomeMaxRate());
        financialOrder.setFloatDailyIncomeMinRate(financialProduct.getFloatDailyIncomeMinRate());
        financialOrder.setCreateTime(nowDateTime);
        //如果自动审核开启
        if (financialProduct.getAutoApprove().equals(0)){
            financialOrder.setOrderStatus(1);
            //今日的凌晨1点
            Date interestTime = DateUtils.getDateBeforeOrAfterDate(DateUtils.getStartOfDay(nowDateTime),Calendar.HOUR_OF_DAY,1);
            //如果是在今日派息前下单
            if (nowDateTime.before(interestTime)){
                financialOrder.setStartDate(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,DateUtils.getDateBeforeOrAfterDate(nowDateTime,Calendar.DAY_OF_YEAR,1)));
                financialOrder.setEndDate(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,DateUtils.getDateBeforeOrAfterDate(nowDateTime,Calendar.DAY_OF_YEAR,financialProduct.getFinancialTime())));
            }else {
                financialOrder.setStartDate(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,DateUtils.getDateBeforeOrAfterDate(nowDateTime,Calendar.DAY_OF_YEAR,2)));
                financialOrder.setEndDate(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,DateUtils.getDateBeforeOrAfterDate(nowDateTime,Calendar.DAY_OF_YEAR,financialProduct.getFinancialTime()+1)));
            }
        }
        financialOrder.setCurrencyId(currencyId);
        financialOrder.setSettleMethod(financialProduct.getSettleMethod());
        //新增理财订单
        int insertFinancialOrder = financialOrderMapper.insertFinancialOrder(financialOrder);
        if (insertFinancialOrder <= 0){
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
        //日志记录理财订单信息
        HttpUtils.getRequestLogParams().put("financialOrder", JSONObject.toJSONString(financialOrder));
        int updateFinancialProduct = financialProductService.updateFinancialProduct(financialProduct);
        if (updateFinancialProduct <= 0){
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
        //用户余额变更后
        BigDecimal userAmountAfter = userAmountBefore.subtract(buyPrice);
        userAmount.setAmount(userAmountAfter);
        //更新用户余额
        int updateUserAmount = userAmountService.updateUserAmount(userAmount);
        if (updateUserAmount <= 0){
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
        //用户流水记录
        UserBillDetail userBillDetail = new UserBillDetail();
        userBillDetail.setUserId(userId);
        userBillDetail.setDeType("理财产品购买");
        userBillDetail.setDeSummary("理财产品购买成功");
        userBillDetail.setOrderAmount(buyPrice.negate());
        userBillDetail.setOrderTime(nowDateTime);
        userBillDetail.setAmountBefore(userAmountBefore);
        userBillDetail.setAmountAfter(userAmountAfter);
        userBillDetail.setRelateOrderId(financialOrder.getId());
        userBillDetail.setOrderClass(17);
        userBillDetail.setCurrencyId(currencyId);
        int insert = userBillDetailService.insertUserBillDetail(userBillDetail);
        if (insert <= 0) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }

        //如果自动审核开启
        if (financialProduct.getAutoApprove().equals(0)){
            // 返佣
            rebate(financialOrder);
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

        //telegram通知（定期理财）
        Integer switchStatusById131 = switchSetService.selectSwitchStatusById(131L);
        if (switchStatusById131.equals(0)) {
            //telegram消息
            String telegramMsg = "\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89定期理财\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89\n" +
                    "⏰时间：" + financialOrder.getCreateTime() + "\n" +
                    "ID: " + userInfo.getUserNo() + "\n" +
                    "用户账号: " + userInfo.getUserAccount() + "\n" +
                    "用户昵称: " + userInfo.getNickName() + "\n" +
                    "邀请码: " + userInfo.getInviteCode() + "\n" +
                    "所属代理: " + userInfo.getAgentId() + "/" + userInfo.getAgentName() + "\n" +
                    "代理昵称: " + userInfo.getAgentNickName() + "\n" +
                    "投资金额: " + buyPrice.stripTrailingZeros().toPlainString() + platformCurrency.getCurrencyName() + "\n" +
                    "订单号: " + financialOrder.getOrderCode() + "\n" +
                    "理财天数: " + financialOrder.getFinancialTime() + "\n" +
                    "余额变更后: " + userAmountAfter.stripTrailingZeros().toPlainString() + platformCurrency.getCurrencyName() + "\n" +
                    "已提交申请，请及时审核！";
            TelegramUtils.sendAsyncMessage(telegramMsg, "default", "default");
        }
        return 1;
    }

    /**
     * 理财数据总统计
     */
    @Override
    public Map<String, Object> financialOrderAnalysis(Long userId){
        //预计今日收益
        BigDecimal estimatedIncomeToday = financialOrderMapper.selectEstimatedIncomeTodayByUserId(userId);
        //总订单数量
        int allOrderCount = financialOrderMapper.selectAllOrderCountByUserId(userId);
        //累计收益
        BigDecimal allPayInterestAmount = financialOrderMapper.selectAllPayInterestAmountByUserId(userId);
        //正在托管资金
        BigDecimal hostingAmount = financialOrderMapper.selectHostingAmountByUserId(userId);

        Map<String, Object> map = new HashMap<>();
        map.put("allOrderCount",allOrderCount);
        map.put("allPayInterestAmount",allPayInterestAmount);
        map.put("hostingAmount",hostingAmount);
        map.put("estimatedIncomeToday",estimatedIncomeToday);
        return map;
    }

    /**
     * 用户赎回
     * @param financialOrderId 理财订单id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int redemption(Long financialOrderId){
        //用户id
        Long userId = SecurityUtils.getUserId();
        //是否允许赎回理财产品开关
        Integer isAllowedRedemption = switchSetService.selectSwitchStatusById(40L);
        if (!isAllowedRedemption.equals(0)){
            throw new LangException("hint_59","禁止赎回理财产品");
        }
        //理财订单信息
        FinancialOrder financialOrder = financialOrderMapper.selectFinancialOrderById(financialOrderId);
        if (financialOrder == null){
            throw new LangException(HintConstants.SYSTEM_BUSY,"获取理财订单信息异常");
        }
        //日志记录理财订单信息
        HttpUtils.getRequestLogParams().put("financialOrder", JSONObject.toJSONString(financialOrder));
        //校验用户信息
        if (!financialOrder.getUserId().equals(userId)){
            throw new ServiceException("校验用户信息异常", HttpStatus.UNAUTHORIZED);
        }
        //如果订单不是进行中
        if (!financialOrder.getOrderStatus().equals(1)){
            throw new LangException(HintConstants.SYSTEM_BUSY,"订单未在进行中");
        }
        //购买金额
        BigDecimal buyPrice = financialOrder.getBuyPrice();
        //理财产品天数
        Integer financialTime = financialOrder.getFinancialTime();
        //货币id
        Long currencyId = financialOrder.getCurrencyId();
        //用户钱包
        UserAmount userAmount = userAmountService.getUserAmount(userId, currencyId);
        //金额变更前
        BigDecimal userAmountBefore = userAmount.getAmount();
        //金额变更后
        BigDecimal userAmountAfter = userAmountBefore.add(buyPrice);

        //用户流水记录
        UserBillDetail userBillDetail = new UserBillDetail();
        userBillDetail.setUserId(userId);
        userBillDetail.setDeType("理财产品本金退回");
        userBillDetail.setDeSummary("理财产品本金退回");
        userBillDetail.setOrderAmount(buyPrice);
        userBillDetail.setOrderTime(new Date());
        userBillDetail.setAmountBefore(userAmountBefore);
        userBillDetail.setAmountAfter(userAmountAfter);
        userBillDetail.setRelateOrderId(financialOrder.getId());
        userBillDetail.setOrderClass(19);
        userBillDetail.setCurrencyId(currencyId);
        int insert = userBillDetailService.insertUserBillDetail(userBillDetail);
        if (insert <= 0) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
        //如果是提前赎回,支付违约金
        if (financialOrder.getAlreadyInterestCount() < financialTime){
            //违约金比例
            BigDecimal breakContractRate = financialOrder.getBreakContractRate();
            //违约金
            BigDecimal breakContractAmount = buyPrice.multiply(breakContractRate).multiply(new BigDecimal(0.01)).multiply(new BigDecimal(financialTime-financialOrder.getAlreadyInterestCount())).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            //用户余额变更后
            userAmountAfter = userAmountAfter.subtract(breakContractAmount);
            //用户流水记录
            UserBillDetail userBillDetail2 = new UserBillDetail();
            userBillDetail2.setUserId(userId);
            userBillDetail2.setDeType("理财产品提前赎回违约金");
            userBillDetail2.setDeSummary("理财产品提前赎回违约金");
            userBillDetail2.setOrderAmount(breakContractAmount.negate());
            userBillDetail2.setOrderTime(new Date());
            userBillDetail2.setAmountBefore(userAmountAfter.add(breakContractAmount));
            userBillDetail2.setAmountAfter(userAmountAfter);
            userBillDetail2.setRelateOrderId(financialOrder.getId());
            userBillDetail2.setOrderClass(20);
            userBillDetail2.setCurrencyId(currencyId);
            int insertUserBillDetail = userBillDetailService.insertUserBillDetail(userBillDetail2);
            if (insertUserBillDetail <= 0) {
                throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
            }
            //日志记录理财订单赎回违约金
            HttpUtils.getRequestLogParams().put("breakContractAmount", breakContractAmount);
        }
        //更新理财订单信息
        FinancialOrder financialOrderVo = new FinancialOrder();
        financialOrderVo.setId(financialOrder.getId());
        //订单状态变更为已赎回
        financialOrderVo.setOrderStatus(4);
        financialOrderVo.setSqlVersion(financialOrder.getSqlVersion());
        int updateFinancialOrder = financialOrderMapper.updateFinancialOrder(financialOrderVo);
        if (updateFinancialOrder <= 0){
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
        //变更用户金额
        userAmount.setAmount(userAmountAfter);
        int updateUserAmount = userAmountService.updateUserAmount(userAmount);
        if (updateUserAmount <= 0){
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
        return 1;
    }

    /**
     * 理财派息定时任务
     */
    @Override
    public void payInterestTask() {
        //获取需要派息的理财订单
        List<FinancialOrder> financialOrders = financialOrderMapper.selectNotDoneOrder();
        if (financialOrders.size() == 0){
            return;
        }
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        try {
            //派发利息
            for (int i = 0; i < financialOrders.size(); i++) {
                FinancialOrder financialOrder = financialOrders.get(i);
                executorService.execute(()->{
                    try {
                        financialOrderService.doPayInterestTask(financialOrder);
                    } catch (Exception e) {
                        //记录异常日志
                        ScheduledTaskExceptionLog scheduledTaskExceptionLog = new ScheduledTaskExceptionLog();
                        scheduledTaskExceptionLog.setJobName("理财派发利息任务");
                        scheduledTaskExceptionLog.setExceptionInfo(e.getMessage());
                        scheduledTaskExceptionLog.setCreateTime(new Date());
                        scheduledTaskExceptionLog.setExceptionInfoDetail(ExceptionUtil.stacktraceToString(e));
                        scheduledTaskExceptionLog.setRelateInfo("id:"+financialOrder.getId());
                        scheduledTaskExceptionLog.setType(1);
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
     * 理财派息定时任务
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doPayInterestTask(FinancialOrder financialOrder){
        //实时时间
        Date nowDateTime = new Date();
        //一天前的时间
        Date oneDayBefore = DateUtils.getDateBeforeOrAfterDate(nowDateTime, Calendar.DAY_OF_YEAR, -1);
        //如果购买未满24小时，不结算
        if (financialOrder.getCreateTime().after(oneDayBefore)){
            return;
        }
        //日收益率
        BigDecimal dailyIncomeRate = financialOrder.getDailyIncomeRate();
        //日收益最高浮动
        BigDecimal maxRate = financialOrder.getFloatDailyIncomeMaxRate();
        //日收益最低浮动
        BigDecimal minRate = financialOrder.getFloatDailyIncomeMinRate();
        //浮动
        BigDecimal randomRate = RandomUtil.randomBigDecimal(minRate, maxRate);
        //本次收益率
        dailyIncomeRate = dailyIncomeRate.add(randomRate);
        //购买金额
        BigDecimal buyPrice = financialOrder.getBuyPrice();
        //日收益
        BigDecimal dailyIncome = buyPrice.multiply(dailyIncomeRate).divide(new BigDecimal(100),Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
        //已派发次数
        Integer alreadyInterestCount = financialOrder.getAlreadyInterestCount();
        //用户id
        Long userId = financialOrder.getUserId();
        //货币id
        Long currencyId = financialOrder.getCurrencyId();

        //插入理财产品日收益结算记录
        FinancialPayInterestRecord financialPayInterestRecord = new FinancialPayInterestRecord();
        financialPayInterestRecord.setUserId(userId);
        financialPayInterestRecord.setFinancialOrderId(financialOrder.getId());
        financialPayInterestRecord.setDailyIncomeRate(dailyIncomeRate);
        financialPayInterestRecord.setPayTime(nowDateTime);
        financialPayInterestRecord.setPayAmount(dailyIncome);
        financialPayInterestRecord.setAlreadySettleFlag(financialOrder.getSettleMethod());
        int insertFinancialPayInterestRecord = financialPayInterestRecordMapper.insertFinancialPayInterestRecord(financialPayInterestRecord);
        if (insertFinancialPayInterestRecord <= 0){
            throw new RuntimeException("新增理财产品日收益结算记录异常");
        }
        //已派发次数+1
        alreadyInterestCount = alreadyInterestCount + 1;
        //如果是日结
        if (financialOrder.getSettleMethod().equals(0)){
            //用户钱包
            UserAmount userAmount = userAmountService.getUserAmount(userId, currencyId);
            //金额变更前
            BigDecimal userAmountBefore = userAmount.getAmount();
            //金额变更后
            BigDecimal userAmountAfter = userAmountBefore.add(dailyIncome);
            //用户流水记录（理财日收益收入流水）
            UserBillDetail userBillDetail = new UserBillDetail();
            userBillDetail.setUserId(userId);
            userBillDetail.setDeType("理财产品日收益结算");
            userBillDetail.setDeSummary("理财产品日收益结算成功");
            userBillDetail.setOrderAmount(dailyIncome);
            userBillDetail.setOrderTime(nowDateTime);
            userBillDetail.setAmountBefore(userAmountBefore);
            userBillDetail.setAmountAfter(userAmountAfter);
            userBillDetail.setRelateOrderId(financialOrder.getId());
            userBillDetail.setOrderClass(18);
            userBillDetail.setCurrencyId(currencyId);
            int insertUserBillDetail = userBillDetailService.insertUserBillDetail(userBillDetail);
            if (insertUserBillDetail <= 0) {
                throw new RuntimeException("插入理财日收益收入流水记录异常");
            }
            //如果已经完成所有派发,订单状态变更为已完成,并且返还理财本金
            if (alreadyInterestCount.equals(financialOrder.getFinancialTime())){
                //订单状态变更为已完成
                financialOrder.setOrderStatus(2);
                //返还理财本金
                userAmountAfter = userAmountAfter.add(buyPrice);
                //用户流水记录（理财订单完成本金返还）
                UserBillDetail userBillDetail2 = new UserBillDetail();
                userBillDetail2.setUserId(userId);
                userBillDetail2.setDeType("理财产品本金退回");
                userBillDetail2.setDeSummary("理财产品本金退回成功");
                userBillDetail2.setOrderAmount(buyPrice);
                userBillDetail2.setOrderTime(nowDateTime);
                userBillDetail2.setAmountBefore(userAmountAfter.subtract(buyPrice));
                userBillDetail2.setAmountAfter(userAmountAfter);
                userBillDetail2.setRelateOrderId(financialOrder.getId());
                userBillDetail2.setOrderClass(19);
                userBillDetail2.setCurrencyId(currencyId);
                int insertUserBillDetail2 = userBillDetailService.insertUserBillDetail(userBillDetail2);
                if (insertUserBillDetail2 <= 0) {
                    throw new RuntimeException("插入理财订单完成本金返还流水记录异常");
                }
            }
            //更新钱包余额
            userAmount.setAmount(userAmountAfter);
            int updateUserAmount = userAmountService.updateUserAmount(userAmount);
            if (updateUserAmount <= 0){
                throw new RuntimeException("更新钱包余额异常");
            }
        }else {
            //到期结
            //如果已经完成所有派发,订单状态变更为已完成,并且返还理财本金和结算所有利息
            if (alreadyInterestCount.equals(financialOrder.getFinancialTime())){
                //订单状态变更为已完成
                financialOrder.setOrderStatus(2);
                //获取该理财订单的派息未结算记录
                FinancialPayInterestRecord search = new FinancialPayInterestRecord();
                search.setFinancialOrderId(financialOrder.getId());
                search.setAlreadySettleFlag(1);
                List<FinancialPayInterestRecord> financialPayInterestRecords = financialPayInterestRecordMapper.selectFinancialPayInterestRecordList(search);
                //未结算利息
                BigDecimal payAmount = financialPayInterestRecords.stream().map(FinancialPayInterestRecord::getPayAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                //用户钱包
                UserAmount userAmount = userAmountService.getUserAmount(userId, currencyId);
                //金额变更前
                BigDecimal userAmountBefore = userAmount.getAmount();
                //金额变更后
                BigDecimal userAmountAfter = userAmountBefore.add(payAmount);
                //用户流水记录（理财日收益收入流水）
                UserBillDetail userBillDetail = new UserBillDetail();
                userBillDetail.setUserId(userId);
                userBillDetail.setDeType("理财产品期满收益结算");
                userBillDetail.setDeSummary("理财产品期满收益结算成功");
                userBillDetail.setOrderAmount(payAmount);
                userBillDetail.setOrderTime(nowDateTime);
                userBillDetail.setAmountBefore(userAmountBefore);
                userBillDetail.setAmountAfter(userAmountAfter);
                userBillDetail.setRelateOrderId(financialOrder.getId());
                userBillDetail.setOrderClass(61);
                userBillDetail.setCurrencyId(currencyId);
                int insertUserBillDetail = userBillDetailService.insertUserBillDetail(userBillDetail);
                if (insertUserBillDetail <= 0) {
                    throw new RuntimeException("插入理财期满收益收入流水记录异常");
                }
                //金额变更前
                userAmountBefore = userAmountAfter;
                //金额变更后
                userAmountAfter = userAmountBefore.add(buyPrice);
                //用户流水记录（理财订单完成本金返还）
                UserBillDetail userBillDetail2 = new UserBillDetail();
                userBillDetail2.setUserId(userId);
                userBillDetail2.setDeType("理财产品本金退回");
                userBillDetail2.setDeSummary("理财产品本金退回成功");
                userBillDetail2.setOrderAmount(buyPrice);
                userBillDetail2.setOrderTime(nowDateTime);
                userBillDetail2.setAmountBefore(userAmountBefore);
                userBillDetail2.setAmountAfter(userAmountAfter);
                userBillDetail2.setRelateOrderId(financialOrder.getId());
                userBillDetail2.setOrderClass(19);
                userBillDetail2.setCurrencyId(currencyId);
                int insertUserBillDetail2 = userBillDetailService.insertUserBillDetail(userBillDetail2);
                if (insertUserBillDetail2 <= 0) {
                    throw new RuntimeException("插入理财订单完成本金返还流水记录异常");
                }
                //更新钱包余额
                userAmount.setAmount(userAmountAfter);
                int updateUserAmount = userAmountService.updateUserAmount(userAmount);
                if (updateUserAmount <= 0){
                    throw new RuntimeException("更新钱包余额异常");
                }
            }
        }

        //更新理财订单信息
        financialOrder.setAlreadyInterestCount(alreadyInterestCount);
        financialOrder.setLastInterestTime(nowDateTime);
        int updateFinancialOrder = financialOrderMapper.updateFinancialOrder(financialOrder);
        if (updateFinancialOrder <= 0){
            throw new RuntimeException("更新理财订单信息异常");
        }
    }

    /**
     * 理财派息定时任务异常修复
     */
    @Override
    public void exceptionPayInterestTask(){
        //获取未修复的异常任务
        ScheduledTaskExceptionLog scheduledTaskExceptionLog = new ScheduledTaskExceptionLog();
        scheduledTaskExceptionLog.setIsFixed(0);
        scheduledTaskExceptionLog.setType(1);
        List<ScheduledTaskExceptionLog> scheduledTaskExceptionLogs = scheduledTaskExceptionLogMapper.selectScheduledTaskExceptionLogList(scheduledTaskExceptionLog);
        if (scheduledTaskExceptionLogs.size() == 0){
            return;
        }
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        try {
            for (int i = 0; i < scheduledTaskExceptionLogs.size(); i++) {
                //异常信息
                ScheduledTaskExceptionLog scheduledTaskExceptionLogVo = scheduledTaskExceptionLogs.get(i);
                executorService.execute(()->{
                    try {
                        //理财订单id
                        Long financialOrderId = Long.valueOf(scheduledTaskExceptionLogVo.getRelateInfo().replace("id:",""));
                        FinancialOrder recordVo = financialOrderMapper.selectFinancialOrderById(financialOrderId);
                        //如果订单还未完成
                        if (recordVo.getOrderStatus().equals(1)){
                            //重新执行派息任务
                            financialOrderService.doPayInterestTask(recordVo);
                        }
                        scheduledTaskExceptionLogVo.setIsFixed(1);
                    } catch (Exception e) {
                        //记录异常日志
                        scheduledTaskExceptionLogVo.setExceptionInfo(scheduledTaskExceptionLogVo.getExceptionInfo() + "/" + e.getMessage());
                        scheduledTaskExceptionLog.setExceptionInfoDetail(scheduledTaskExceptionLogVo.getExceptionInfoDetail() + "/" + ExceptionUtil.stacktraceToString(e));
                    }
                    scheduledTaskExceptionLogVo.setExecuteCount(scheduledTaskExceptionLogVo.getExecuteCount() + 1);
                    scheduledTaskExceptionLogVo.setUpdateTime(new Date());
                    int updateScheduledTaskExceptionLog = scheduledTaskExceptionLogMapper.updateScheduledTaskExceptionLog(scheduledTaskExceptionLogVo);
                    //如果执行成功了，验证日志更新是否成功
                    if (scheduledTaskExceptionLogVo.getIsFixed() == 1){
                        if (updateScheduledTaskExceptionLog <= 0){
                            throw new RuntimeException();
                        }
                    }
                });
            }
        }catch (Exception e){
            throw new RuntimeException("创建定时任务异常");
        }finally {
            executorService.shutdown();
        }
    }
}
