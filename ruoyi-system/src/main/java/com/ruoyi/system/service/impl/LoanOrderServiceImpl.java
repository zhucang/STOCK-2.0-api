package com.ruoyi.system.service.impl;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.entity.UserAmount;
import com.ruoyi.common.core.domain.entity.UserInfo;
import com.ruoyi.common.exception.LangException;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.CodeUtils;
import com.ruoyi.common.utils.DateUtils;
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
 * 贷款订单Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-05-22
 */
@Service("LoanOrderServiceImpl")
public class LoanOrderServiceImpl implements ILoanOrderService 
{
    @Resource
    private LoanOrderMapper loanOrderMapper;

    @Autowired
    private ILoanProductService loanProductService;

    @Autowired
    private IPlatformCurrencyService platformCurrencyService;

    @Autowired
    private IUserInfoService userInfoService;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Autowired
    private IUserAmountService userAmountService;

    @Autowired
    private IUserBillDetailService userBillDetailService;

    @Autowired
    private ILoanOrderService loanOrderService;

    @Resource
    private ScheduledTaskExceptionLogMapper scheduledTaskExceptionLogMapper;

    @Autowired
    private ILoanOrderInterestRecordService loanOrderInterestRecordService;

    @Autowired
    private ISwitchSetService switchSetService;

    @Autowired
    private IUserDmAmountChangeRecordService userDmAmountChangeRecordService;

    @Resource
    private UserLoanRepaymentOrderMapper userLoanRepaymentOrderMapper;

    @Autowired
    private IAgentTeamLevelLineService agentTeamLevelLineService;

    @Resource
    private SysUserMapper sysUserMapper;

    @Autowired
    private ICurrencyExchangeRateService currencyExchangeRateService;

    @Autowired
    private IVipExperienceValueService vipExperienceValueService;

    @Autowired
    private IUserVipLevelConfigService userVipLevelConfigService;

    /**
     * 查询贷款订单
     * 
     * @param id 贷款订单主键
     * @return 贷款订单
     */
    @Override
    public LoanOrder selectLoanOrderById(Long id)
    {
        return loanOrderMapper.selectLoanOrderById(id);
    }

    /**
     * 查询贷款订单列表
     * 
     * @param loanOrder 贷款订单
     * @return 贷款订单
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u", isUserInfo = true)
    public List<LoanOrder> selectLoanOrderList(LoanOrder loanOrder)
    {
        return loanOrderMapper.selectLoanOrderList(loanOrder);
    }

    /**
     * 获取统计数据
     * @param loanOrder
     * @return
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u", isUserInfo = true)
    public List<LoanOrder> getStatisticalData(LoanOrder loanOrder){
        return loanOrderMapper.getStatisticalData(loanOrder);
    }

    /**
     * 填充其他信息
     * @param loanOrders 贷款订单
     */
    @Override
    public void fillOtherInfo(List<LoanOrder> loanOrders){
        fillAgentLine(loanOrders);
    }

    /**
     * 填充代理线
     * @param loanOrders 贷款订单
     *      */

    public void fillAgentLine(List<LoanOrder> loanOrders){
        //用户的代理集合
        List<Long> agentIds = loanOrders.stream().map(LoanOrder::getAgentId).distinct().collect(Collectors.toList());
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
        for (int i = 0; i < loanOrders.size(); i++) {
            //用户贷款订单信息
            LoanOrder loanOrder = loanOrders.get(i);
            //代理id
            Long agentId = loanOrder.getAgentId();
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
            loanOrder.setAgentName(agentLine);
        }
    }

    /**
     * 新增贷款订单
     * 
     * @param loanOrder 贷款订单
     * @return 结果
     */
    @Override
    public int insertLoanOrder(LoanOrder loanOrder)
    {
        loanOrder.setCreateTime(DateUtils.getNowDate());
        return loanOrderMapper.insertLoanOrder(loanOrder);
    }

    /**
     * 修改贷款订单
     * 
     * @param loanOrder 贷款订单
     * @return 结果
     */
    @Override
    public int updateLoanOrder(LoanOrder loanOrder)
    {
        return loanOrderMapper.updateLoanOrder(loanOrder);
    }

    /**
     * 批量删除贷款订单
     * 
     * @param ids 需要删除的贷款订单主键
     * @return 结果
     */
    @Override
    public int deleteLoanOrderByIds(Long[] ids)
    {
        return loanOrderMapper.deleteLoanOrderByIds(ids);
    }

    /**
     * 删除贷款订单信息
     * 
     * @param id 贷款订单主键
     * @return 结果
     */
    @Override
    public int deleteLoanOrderById(Long id)
    {
        return loanOrderMapper.deleteLoanOrderById(id);
    }

    /**
     * 贷款订单审核
     * @param loanOrderId 贷款订单id
     * @param orderStatus 状态：1:通过 3：驳回
     * @param loanMsg 驳回信息
     * @param remark 备注
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateLoanOrderStatus(Long loanOrderId,Integer orderStatus,BigDecimal realLoanAmount, BigDecimal loanDailyRate, String loanMsg,String remark){
        //贷款订单信息
        LoanOrder loanOrder = loanOrderMapper.selectLoanOrderById(loanOrderId);
        if (loanOrder == null) {
            throw new ServiceException("获取贷款订单信息异常");
        }
        //检验订单是否是审核中
        if (!loanOrder.getOrderStatus().equals(0)) {
            throw new ServiceException("订单已处理，无需重复操作");
        }
        //用户id
        Long userId = loanOrder.getUserId();
        //日志记录用户id
        HttpUtils.getRequestLogParams().put("userId",userId);
        //用户信息
        UserInfo userInfo = userInfoService.selectUserInfoById(userId);
        if (userInfo == null){
            throw new ServiceException("获取用户信息异常");
        }
        //如果审核通过
        if (orderStatus.equals(1)) {
            if (realLoanAmount == null){
                throw new ServiceException("请输入放款额度");
            }
            if (realLoanAmount.compareTo(BigDecimal.ZERO) <= 0){
                throw new ServiceException("放款额度必须大于0");
            }
            if (loanDailyRate == null){
                throw new ServiceException("请输入贷款每日利息率");
            }
            if (loanDailyRate.compareTo(BigDecimal.ZERO) < 0){
                throw new ServiceException("请输入贷款每日利息率不允许小于0");
            }
            //币种id
            Long currencyId = loanOrder.getCurrencyId();
            //用户钱包信息
            UserAmount userAmount = userAmountService.getUserAmount(userId,currencyId);
            //余额变更前
            BigDecimal userAmountBefore = userAmount.getAmount();
            //余额变更后
            BigDecimal userAmountAfter = userAmountBefore.add(realLoanAmount);
            userAmount.setAmount(userAmountAfter);
            int updateUserAmount = userAmountService.updateUserAmount(userAmount);
            if (updateUserAmount <= 0) {
                throw new ServiceException("系统繁忙");
            }
            //贷款入账明细
            UserBillDetail userBillDetail = new UserBillDetail();
            userBillDetail.setUserId(userId);
            userBillDetail.setDeType("贷款入账");
            userBillDetail.setDeSummary("贷款入账成功");
            userBillDetail.setOrderAmount(realLoanAmount);
            //实时时间
            Date date = new Date();
            userBillDetail.setOrderTime(date);
            userBillDetail.setAmountBefore(userAmountBefore);
            userBillDetail.setAmountAfter(userAmountAfter);
            userBillDetail.setRelateOrderId(loanOrderId);
            userBillDetail.setOrderClass(63);
            userBillDetail.setCurrencyId(userAmount.getCurrencyId());
            int insertUserBillDetail = userBillDetailService.insertUserBillDetail(userBillDetail);
            if (insertUserBillDetail <= 0) {
                throw new ServiceException("系统繁忙");
            }
            //贷款金额是否计入打码量
            Integer switchStatus90 = switchSetService.selectSwitchStatusById(90L);
            //如果贷款金额计入打码量
            if (switchStatus90.equals(0)){
                //更新打码量
                //打码倍数
                BigDecimal userDefaultDmMultiples = CacheUtils.getOtherValueByKey("user_default_dm_multiples",BigDecimal.class);
                if (userDefaultDmMultiples != null && userDefaultDmMultiples.compareTo(BigDecimal.ZERO) >= 0){
                    //汇率
                    BigDecimal exchangeRate = currencyExchangeRateService.getExchangeInfo(currencyId,3L).get("exchangeRate");
                    //折合USDT
                    BigDecimal USDTValue = exchangeRate.multiply(realLoanAmount);
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
                        userDmAmountChangeRecord.setOrderType(4);
                        int insertUserDmAmountChangeRecord = userDmAmountChangeRecordService.insertUserDmAmountChangeRecord(userDmAmountChangeRecord);
                        if (insertUserDmAmountChangeRecord <= 0){
                            throw new ServiceException("系统繁忙");
                        }
                    }
                }
            }

            //与USDT汇率
            BigDecimal exchangeRate = currencyExchangeRateService.getExchangeInfo(currencyId,3L).get("exchangeRate");
            //折合USDT
            BigDecimal USDTValue = exchangeRate.multiply(realLoanAmount);
            //用户贷款计算VIP等级
            Integer selectSwitchStatus136 = switchSetService.selectSwitchStatusById(136L);
            if (selectSwitchStatus136.equals(0)) {
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
                if (userVipLevelConfigNew != null && !userVipLevelConfigNew.getVipLevel().equals(userInfo.getVipLevel())){
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
                vipExperienceValue.setRelateOrderCode(loanOrder.getOrderCode());
                vipExperienceValue.setExperienceValue(experienceValue);
                vipExperienceValue.setExperienceValueBefore(vipExperienceValueBefore);
                vipExperienceValue.setExperienceValueAfter(vipExperienceValueAfter);
                vipExperienceValue.setCreateTime(new Date());
                int insertVipExperienceValue = vipExperienceValueService.insertVipExperienceValue(vipExperienceValue);
                if (insertVipExperienceValue <= 0){
                    throw new ServiceException("插入VIP经验值记录异常");
                }
            }

            loanOrder.setLoanStartTime(DateUtils.getStartOfDay(DateUtils.getDateBeforeOrAfterDate(date, Calendar.DAY_OF_YEAR,1)));
            loanOrder.setLoanEndTime(DateUtils.getStartOfDay(DateUtils.getDateBeforeOrAfterDate(date, Calendar.DAY_OF_YEAR,1+loanOrder.getLoanDays())));
        }else {
//            if (StringUtils.isEmpty(loanMsg)){
//                throw new ServiceException("请填写驳回原因");
//            }
        }
        //更新贷款订单信息
        loanOrder.setRealLoanAmount(realLoanAmount);
        loanOrder.setLoanDailyRate(loanDailyRate);
        loanOrder.setOrderStatus(orderStatus);
        loanOrder.setUpdateTime(new Date());
        loanOrder.setUpdateBy(SecurityUtils.getUsername());
//        loanOrder.setWithdrawMsg(loanMsg);
        loanOrder.setRemark(remark);
        int updateCount = loanOrderMapper.updateLoanOrder(loanOrder);
        if (updateCount <= 0) {
            throw new ServiceException("系统繁忙");
        }
        //日志记录订单信息
        HttpUtils.getRequestLogParams().put("loanOrder", JSONObject.toJSONString(loanOrder));
        return 1;
    }

    /**
     * 修改贷款订单是否免客损状态
     * @param loanOrderId 贷款订单id
     * @param statisticalReport 是否统计报表 0：是 1：否
     * @return
     */
    @Override
    public int updateStatisticalReport(Long loanOrderId, Integer statisticalReport){
        LoanOrder loanOrder = new LoanOrder();
        loanOrder.setId(loanOrderId);
        loanOrder.setStatisticalReport(statisticalReport);
        int updateLoanOrder = loanOrderMapper.updateLoanOrder(loanOrder);
        if (updateLoanOrder <= 0){
            throw new ServiceException("系统繁忙");
        }
        return 1;
    }

    /**
     * 获取用户的各币种的贷款金额
     * @param userId 用户id
     * @return
     */
    @Override
    public List<LoanOrder> selectUserLoanAmountAllCurrencyByUserId(Long userId) {
        return loanOrderMapper.selectUserLoanAmountAllCurrencyByUserId(userId);
    }

    /**
     * 用户贷款
     * @param loanOrder
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addLoanOrder(LoanOrder loanOrder){
        //贷款产品id
        Long loanProductId = loanOrder.getLoanProductId();
        //贷款产品信息
        LoanProduct loanProduct = loanProductService.selectLoanProductById(loanProductId);
        if (loanProduct == null){
            throw new LangException(HintConstants.SYSTEM_BUSY,"获取贷款产品信息异常");
        }
        if (!loanProduct.getStatus().equals(0)){
            throw new LangException(HintConstants.SYSTEM_BUSY,"该贷款产品已禁用");
        }
        //日志记录贷款产品名称
        HttpUtils.getRequestLogParams().put("productName",loanProduct.getProductName());
        //币种id
        Long currencyId = loanProduct.getCurrencyId();
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
        //用户此产品已贷款次数
        //用户id
        Long userId = loanOrder.getUserId();
        //用户信息
        UserInfo userInfo = userInfoMapper.selectUserInfoById(userId);
        int userAlreadyLoanCount = loanOrderMapper.getUserAlreadyLoanCountByLoanProductId(loanProductId, userId);
        //最大贷款次数
        Integer maxLoanCount = loanProduct.getMaxLoanCount();
        if (userAlreadyLoanCount >= loanProduct.getMaxLoanCount()){
            List<Object> params = new ArrayList<>();
            params.add(maxLoanCount);
            throw new LangException("hint_76",params,"此产品最多允许贷款"+maxLoanCount+"次");
        }
        //有未结清的贷款禁止继续贷款
        Integer switchStatusById121 = switchSetService.selectSwitchStatusById(121L);
        if (switchStatusById121.equals(0)){
            //贷款未结清前禁止继续贷款
            LoanOrder search = new LoanOrder();
            search.setUserId(userId);
            search.setOrderStatus(1);
            if (loanOrderService.selectLoanOrderList(search).size() > 0){
                throw new LangException("hint_80","还有未结清的贷款订单，无法继续贷款");
            }
        }
        //有申请中的贷款禁止继续贷款
        Integer switchStatusById122 = switchSetService.selectSwitchStatusById(122L);
        if (switchStatusById122.equals(0)){
            //贷款未结清前禁止继续贷款
            LoanOrder search = new LoanOrder();
            search.setUserId(userId);
            //有申请中的贷款，无法继续申请
            search.setOrderStatus(0);
            if (loanOrderService.selectLoanOrderList(search).size() > 0){
                throw new LangException("hint_81","还有申请中的贷款订单，无法继续贷款");
            }
        }

        //订单金额
        BigDecimal orderPrice = loanOrder.getOrderPrice();
        //最大可贷款金额
        BigDecimal maxPrice = loanProduct.getMaxPrice();
        //最小可贷款金额
        BigDecimal minPrice = loanProduct.getMinPrice();
        if (orderPrice.compareTo(minPrice) == -1 || orderPrice.compareTo(maxPrice) == 1){
            List<Object> params = new ArrayList<>();
            params.add(minPrice);
            params.add(maxPrice);
            throw new LangException("hint_73",params,"贷款金额范围"+minPrice+"~"+maxPrice);
        }
        loanOrder.setOrderCode(CodeUtils.generateOrderCode("LoanOrder"));
        loanOrder.setLoanDays(loanProduct.getLoanDays());
        loanOrder.setRealLoanAmount(orderPrice);
        loanOrder.setInterestFreeDays(loanProduct.getInterestFreeDays());
        loanOrder.setLoanDailyRate(loanProduct.getLoanDailyRate());
        loanOrder.setBreakContractDailyRate(loanProduct.getBreakContractDailyRate());
        loanOrder.setLoanStartTime(null);
        loanOrder.setLoanEndTime(null);
        loanOrder.setCreateTime(new Date());
        loanOrder.setInterestSettlementMethod(loanProduct.getInterestSettlementMethod());
        loanOrder.setRepaymentMethod(loanProduct.getRepaymentMethod());
        loanOrder.setOrderStatus(0);
        loanOrder.setCurrencyId(loanProduct.getCurrencyId());
        //新增用户贷款订单
        int insertLoanOrder = loanOrderMapper.insertLoanOrder(loanOrder);
        if (insertLoanOrder <= 0){
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
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

        //telegram通知（贷款申请）
        Integer switchStatusById132 = switchSetService.selectSwitchStatusById(132L);
        if (switchStatusById132.equals(0)) {
            //telegram消息
            String telegramMsg = "\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89贷款申请\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89\uD83C\uDF89\n" +
                    "⏰时间：" + loanOrder.getCreateTime() + "\n" +
                    "ID: " + userInfo.getUserNo() + "\n" +
                    "用户账号: " + userInfo.getUserAccount() + "\n" +
                    "用户昵称: " + userInfo.getNickName() + "\n" +
                    "用户备注: " + userInfo.getRemark() + "\n" +
                    "邀请码: " + userInfo.getInviteCode() + "\n" +
                    "所属代理: " + userInfo.getAgentId() + "/" + userInfo.getAgentName() + "\n" +
                    "代理昵称: " + userInfo.getAgentNickName() + "\n" +
                    "贷款金额: " + orderPrice.stripTrailingZeros().toPlainString() + platformCurrency.getCurrencyName() + "\n" +
                    "已申请充值,请及时处理！";
            TelegramUtils.sendAsyncMessage(telegramMsg, "default", "default");
        }
        return 1;
    }

    /**
     * 用户贷款信息面板
     * @return
     */
    @Override
    public Map<String, Object> userLoanPanelData() {
        //用户ID
        Long userId = SecurityUtils.getUserId();
        //
        LoanOrder loanOrder = new LoanOrder();
        loanOrder.setUserId(userId);
        loanOrder.setOrderStatus(1);
        //未结清的贷款订单
        List<LoanOrder> loanOrders = loanOrderService.selectLoanOrderList(loanOrder);
        //未结清的贷款金额
        BigDecimal loanAmount = loanOrders.stream().map(a -> a.getRealLoanAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
        //待还利息
        BigDecimal needPayInterest = loanOrders.stream().map(a -> a.getNeedPayInterest()).reduce(BigDecimal.ZERO, BigDecimal::add);
        Map<String, Object> result = new HashMap<>();
        result.put("loanAmount", loanAmount.stripTrailingZeros().toPlainString());
        result.put("needPayInterest", needPayInterest.stripTrailingZeros().toPlainString());
        return result;
    }

    /**
     * 贷款订单后台人工结算
     * @param loanOrderId 贷款订单id
     * @param settlementType 结算类型 0：从余额扣除 1：直接结算
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int loanRepayment(Long loanOrderId,Integer settlementType){
        //贷款订单信息
        LoanOrder loanOrder = loanOrderMapper.selectLoanOrderById(loanOrderId);
        if (loanOrder == null){
            throw new ServiceException("获取贷款订单信息异常");
        }
        if (!loanOrder.getOrderStatus().equals(1)){
            throw new ServiceException("订单未在进行中");
        }
        if (!loanOrder.getRepaymentMethod().equals(0)){
            throw new ServiceException("该订单的还款方式不支持后台手动结算");
        }
        //用户id
        Long userId = loanOrder.getUserId();
        //用户信息
        UserInfo userInfo = userInfoService.selectUserInfoById(userId);
        if (userInfo == null){
            throw new ServiceException("获取用户信息异常");
        }
        //实际放款金额
        BigDecimal realLoanAmount = loanOrder.getRealLoanAmount();
        //币种id
        Long currencyId = loanOrder.getCurrencyId();
        //需要支付利息
        BigDecimal needPayInterest = loanOrder.getNeedPayInterest();
        //实时时间
        Date nowDateTime = new Date();
        //如果结算方式是从余额直接扣除
        if (settlementType.equals(0)){
            //用户钱包信息
            UserAmount userAmount = userAmountService.getUserAmount(userId, currencyId);
            //余额变更前
            BigDecimal userAmountBefore = userAmount.getAmount();
            //共需支付
            BigDecimal needPay = realLoanAmount.add(needPayInterest);
            if (needPay.compareTo(userAmountBefore) > 0){
                throw new ServiceException("还款失败，余额不足，贷款本金"+realLoanAmount+"与贷款利息"+needPayInterest+"共需支付"+needPay);
            }
            //余额变更后
            BigDecimal userAmountAfter = userAmountBefore.subtract(needPay);
            //插入贷款还款流水
            UserBillDetail userBillDetail = new UserBillDetail();
            userBillDetail.setUserId(userId);
            userBillDetail.setDeType("贷款还款");
            userBillDetail.setDeSummary("贷款还款成功");
            userBillDetail.setOrderAmount(needPay.negate());
            userBillDetail.setOrderTime(nowDateTime);
            userBillDetail.setAmountBefore(userAmountBefore);
            userBillDetail.setAmountAfter(userAmountAfter);
            userBillDetail.setRelateOrderId(loanOrderId);
            userBillDetail.setOrderClass(64);
            userBillDetail.setCurrencyId(userAmount.getCurrencyId());
            int insertUserBillDetail = userBillDetailService.insertUserBillDetail(userBillDetail);
            if (insertUserBillDetail <= 0) {
                throw new ServiceException("系统繁忙");
            }
            //更新钱包信息
            userAmount.setAmount(userAmountAfter);
            int updateUserAmount = userAmountService.updateUserAmount(userAmount);
            if (updateUserAmount <= 0){
                throw new ServiceException("系统繁忙");
            }
        }
        //更新订单状态为已完成
        loanOrder.setOrderStatus(2);
        int updateLoanOrder = loanOrderMapper.updateLoanOrder(loanOrder);
        if (updateLoanOrder <= 0){
            throw new ServiceException("系统繁忙");
        }
        //日志记录贷款订单信息
        HttpUtils.getRequestLogParams().put("loanOrder",JSONObject.toJSONString(loanOrder));
        //插入用户贷款还款明细
        UserLoanRepaymentOrder userLoanRepaymentOrder = new UserLoanRepaymentOrder();
        userLoanRepaymentOrder.setLoanOrderId(loanOrderId);
        userLoanRepaymentOrder.setUserId(userId);
        userLoanRepaymentOrder.setOrderCode(CodeUtils.generateOrderCode("LoanRepayment"));
        userLoanRepaymentOrder.setOrderAmount(realLoanAmount.add(needPayInterest));
        userLoanRepaymentOrder.setOrderStatus(1);
        userLoanRepaymentOrder.setCreateTime(nowDateTime);
        userLoanRepaymentOrder.setUpdateTime(nowDateTime);
        userLoanRepaymentOrder.setOperatorName(SecurityUtils.getUsername());
        userLoanRepaymentOrder.setPayChannelName("后台人工结算");
        userLoanRepaymentOrder.setPayChannelId(0L);
        userLoanRepaymentOrder.setCurrencyId(currencyId);
        //新增贷款还款订单
        int insertCount = userLoanRepaymentOrderMapper.insertUserLoanRepaymentOrder(userLoanRepaymentOrder);
        if (insertCount <= 0) {
            throw new ServiceException("系统繁忙");
        }
        //日志记录还款订单号
        HttpUtils.getRequestLogParams().put("还款订单号",userLoanRepaymentOrder.getOrderCode());
        return 1;
    }

    /**
     * 贷款收取利息定时器
     */
    @Override
    public void chargeInterestTask() {
        //获取需要收取利息的贷款订单
        LoanOrder search = new LoanOrder();
        search.setOrderStatus(1);
        List<LoanOrder> loanOrders = loanOrderMapper.selectLoanOrderList(search);
        if (loanOrders.size() == 0){
            return;
        }
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        try {
            //收取利息
            for (int i = 0; i < loanOrders.size(); i++) {
                LoanOrder loanOrder = loanOrders.get(i);
                executorService.execute(()->{
                    try {
                        loanOrderService.doChargeInterestTask(loanOrder);
                    } catch (Exception e) {
                        //记录异常日志
                        ScheduledTaskExceptionLog scheduledTaskExceptionLog = new ScheduledTaskExceptionLog();
                        scheduledTaskExceptionLog.setJobName("贷款收取利息任务");
                        scheduledTaskExceptionLog.setExceptionInfo(e.getMessage());
                        scheduledTaskExceptionLog.setCreateTime(new Date());
                        scheduledTaskExceptionLog.setExceptionInfoDetail(ExceptionUtil.stacktraceToString(e));
                        scheduledTaskExceptionLog.setRelateInfo("id:"+loanOrder.getId());
                        scheduledTaskExceptionLog.setType(17);
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
     * 贷款收取利息定时器
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doChargeInterestTask(LoanOrder loanOrder){
        //如果订单不是进行中
        if (!loanOrder.getOrderStatus().equals(1)){
            return;
        }
        //此贷款订单有正在审核中的还款订单
        UserLoanRepaymentOrder userLoanRepaymentOrder = new UserLoanRepaymentOrder();
        userLoanRepaymentOrder.setLoanOrderId(loanOrder.getId());
        userLoanRepaymentOrder.setOrderStatus(0);
        List<UserLoanRepaymentOrder> userLoanRepaymentOrders = userLoanRepaymentOrderMapper.selectUserLoanRepaymentOrderList(userLoanRepaymentOrder);
        //如果正在还款中，不做操作
        if (userLoanRepaymentOrders.size() > 0){
            return;
        }
        //订单贷款天数
        Integer loanDays = loanOrder.getLoanDays();
        //已贷款天数
        Integer alreadyLoanDays = loanOrder.getAlreadyLoanDays() + 1;
        //贷款每日利息率
        BigDecimal loanDailyRate = loanOrder.getLoanDailyRate();
        //如果还未逾期
        if (alreadyLoanDays <= loanDays){
            //免息天数
            Integer interestFreeDays = loanOrder.getInterestFreeDays();
            //如果在免息期内
            if (alreadyLoanDays <= interestFreeDays){
                //不收取利息
                loanDailyRate = BigDecimal.ZERO;
            }
        }else {
            //如果已经逾期
            //收取违约利息
            //贷款每日违约利息率
            loanDailyRate = loanOrder.getBreakContractDailyRate();
        }
        //实际放款金额
        BigDecimal realLoanAmount = loanOrder.getRealLoanAmount();
        //今日利息
        BigDecimal loanDailyInterest = realLoanAmount.multiply(loanDailyRate).divide(new BigDecimal(100),Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);

        //如果有收取利息
        if (loanDailyInterest.compareTo(BigDecimal.ZERO) > 0){
            //插入利息记录
            LoanOrderInterestRecord loanOrderInterestRecord = new LoanOrderInterestRecord();
            loanOrderInterestRecord.setLoanOrderId(loanOrder.getId());
            loanOrderInterestRecord.setOrderPrice(loanDailyInterest);

            //每日余额扣除利息
            if (loanOrder.getInterestSettlementMethod().equals(0)){
                //币种id
                Long currencyId = loanOrder.getCurrencyId();
                //用户id
                Long userId = loanOrder.getUserId();
                //用户钱包信息
                UserAmount userAmount = userAmountService.getUserAmount(userId, currencyId);
                //余额变更前
                BigDecimal userAmountBefore = userAmount.getAmount();
                //余额变更后
                BigDecimal userAmountAfter = userAmountBefore.subtract(loanDailyInterest);
                //更新钱包余额
                userAmount.setAmount(userAmountAfter);
                int updateUserAmount = userAmountService.updateUserAmount(userAmount);
                if (updateUserAmount <= 0){
                    throw new ServiceException("系统繁忙");
                }
                //插入贷款还款流水
                UserBillDetail userBillDetail = new UserBillDetail();
                userBillDetail.setUserId(userId);
                userBillDetail.setDeType("贷款每日利息扣除");
                userBillDetail.setDeSummary("贷款每日利息扣除");
                userBillDetail.setOrderAmount(loanDailyInterest.negate());
                userBillDetail.setOrderTime(new Date());
                userBillDetail.setAmountBefore(userAmountBefore);
                userBillDetail.setAmountAfter(userAmountAfter);
                userBillDetail.setRelateOrderId(loanOrder.getId());
                userBillDetail.setOrderClass(65);
                userBillDetail.setCurrencyId(userAmount.getCurrencyId());
                int insertUserBillDetail = userBillDetailService.insertUserBillDetail(userBillDetail);
                if (insertUserBillDetail <= 0) {
                    throw new ServiceException("系统繁忙");
                }
            }else {
                loanOrder.setNeedPayInterest(loanOrder.getNeedPayInterest().add(loanDailyInterest));
            }
            //新增
            loanOrderInterestRecordService.insertLoanOrderInterestRecord(loanOrderInterestRecord);
        }
        //更新贷款订单信息
        loanOrder.setAlreadyLoanDays(alreadyLoanDays);
        int updateLoanOrder = loanOrderMapper.updateLoanOrder(loanOrder);
        if (updateLoanOrder <= 0){
            throw new ServiceException("系统繁忙");
        }
    }
}
