package com.ruoyi.system.service.impl;


import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.domain.entity.UserInfo;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.service.TokenService;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.PageUtils;
import com.ruoyi.common.utils.cache.CacheUtil;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.service.*;
import com.ruoyi.system.utils.currencyExchangeRate.ExchangeRateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class WebBackgroundServiceImpl implements IWebBackgroundService {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private UserRechargeMapper userRechargeMapper;

    @Resource
    private UserWithdrawMapper userWithdrawMapper;

    @Resource
    private WebBackgroundMapper webBackgroundMapper;

    @Resource
    private StockProductMapper stockProductMapper;

    @Resource
    private CryptocurrencyProductMapper cryptocurrencyProductMapper;

    @Resource
    private FuturesProductMapper futuresProductMapper;
    @Resource
    private ForexProductMapper forexProductMapper;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private IUserRechargeService userRechargeService;

    @Autowired
    private IUserWithdrawService userWithdrawService;

    @Autowired
    private IPlatformCurrencyService platformCurrencyService;

    @Autowired
    private IUserPointChangeRecordService userPointChangeRecordService;

    @Resource
    private LoanOrderMapper loanOrderMapper;

    @Resource
    private UserLoanRepaymentOrderMapper userLoanRepaymentOrderMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IBackendReminderConfigService backendReminderConfigService;

    @Autowired
    private ISwitchSetService switchSetService;

    @Resource
    private FinancialOrderMapper financialOrderMapper;

    @Resource
    private StakingOrderMapper stakingOrderMapper;

    @Resource
    private FastTradeOrderMapper fastTradeOrderMapper;

    @Autowired
    private TokenService tokenService;

    /**
     * 获取后台提醒
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u", isUserInfo = true)
    public AjaxResult getReminder(BaseEntity baseEntity) {
//        //cacheKey
//        String cacheKey = "webReminder:";
//        //cacheMap
//        Map<String, String> cacheMap = redisCache.getCacheMap(cacheKey);
        //后台提醒是否统计游客开关
        Integer selectSwitchStatus97 = switchSetService.selectSwitchStatusById(97L);
        baseEntity.getParams().put("tourists",selectSwitchStatus97);
        //后台提醒配置列表
        BackendReminderConfig backendReminderConfig = new BackendReminderConfig();
        backendReminderConfig.setStatus(0);
        List<BackendReminderConfig> backendReminderConfigs = backendReminderConfigService.selectBackendReminderConfigList(backendReminderConfig);
        for (int i = 0; i < backendReminderConfigs.size(); i++) {
            //后台提醒配置信息
            BackendReminderConfig vo = backendReminderConfigs.get(i);
            //跳转类型 0：初级实名认证:1：高级实名认证 2：充值 3：提现 4：贷款申请 5：贷款还款
            Integer jumpType = vo.getJumpType();
            //待审核id列表
            List<Long> ids = new ArrayList<>();
            //初级实名认证
            if (jumpType.equals(0)){
                //初级实名认证待审核
                baseEntity.getParams().put("authLevel",0);
                PageUtils.orderBy("id");
                ids = userInfoMapper.getRealNameAuthPendingReviewNum(baseEntity);
            }else if (jumpType.equals(1)){
                //高级实名认证待审核
                baseEntity.getParams().put("authLevel",1);
                PageUtils.orderBy("id");
                ids = userInfoMapper.getRealNameAuthPendingReviewNum(baseEntity);
            }else if (jumpType.equals(2)){
                //充值订单待审核
                PageUtils.orderBy("id");
                ids = userRechargeMapper.getUserRechargePendingReviewNum(baseEntity,null);
            }else if (jumpType.equals(3)){
                //提现订单待审核
                PageUtils.orderBy("id");
                ids = userWithdrawMapper.getUserWithdrawPendingReviewNum(baseEntity);
            }else if (jumpType.equals(4)){
                //贷款申请订单待审核
                PageUtils.orderBy("id");
                ids = loanOrderMapper.getUserLoanPendingReviewNum(baseEntity);
            }else if (jumpType.equals(5)){
                //贷款还款订单待审核
                PageUtils.orderBy("id");
                ids = userLoanRepaymentOrderMapper.getUserLoanRepaymentPendingReviewNum(baseEntity);
            }else if (jumpType.equals(6)){
                //理财订单待审核
                PageUtils.orderBy("id");
                ids = financialOrderMapper.getUserFinancialPendingReviewNum(baseEntity);
            }else if (jumpType.equals(7)){
                //质押订单待审核
                PageUtils.orderBy("id");
                ids = stakingOrderMapper.getUserStakingPendingReviewNum(baseEntity);
            }else if (jumpType.equals(8)){
                //极速订单未结算
                PageUtils.orderBy("id");
                ids = fastTradeOrderMapper.getUserFastTradePendingSettleNum(baseEntity);
            }
            vo.getParams().put("ids",ids);
            vo.getParams().put("num",ids.size());
//            //提醒类型 0：不提示 1：提醒一次 :2：循环提醒
//            Integer reminderType = vo.getReminderType();
//            //如果需要提醒
//            if (!reminderType.equals(0)){
//                //缓存
//                String cache = cacheMap.get(String.valueOf(jumpType));
//                //数组转字符串
//                String idsStr = JSONObject.toJSONString(ids);
//                if (idsStr.equals(cache)){
//                    //如果无变化，则不提醒
//                    vo.setReminderType(0);
//                }
//                cacheMap.put(String.valueOf(jumpType),idsStr);
//            }
        }
//        redisCache.setCacheMap(cacheKey,cacheMap);
        return AjaxResult.success(backendReminderConfigs);
    }

    /**
     * 获取所有产品名称多语言
     * @return
     */
    @Override
    public List<LangMgr> selectProductNameLang(){
        return webBackgroundMapper.selectProductNameLang();
    }

    /**
     * 导入所有产品名称多语言
     * @param list
     * @param isUpdateSupport
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String importProductNameLang(List<LangMgr> list, Boolean isUpdateSupport){
        isUpdateSupport = false;

        //获取所有产品多语言信息
        List<LangMgr> productNameLangList = webBackgroundMapper.selectProductNameLang();
        Map<String, LangMgr> map = productNameLangList.stream().collect(Collectors.toMap(LangMgr::getLangKey, a -> a));
        for (int i = 0; i < list.size(); i++) {
            //新产品名称多语言信息
            LangMgr productNameLang = list.get(i);
            //产品代码
            String productCode = list.get(i).getLangKey();
            //旧产品名称多语言信息
            LangMgr productNameLangVo = map.get(productCode);
            if (map.get(productCode) != null){
                //如果有变动，则更新
                if (!JSONObject.toJSONString(productNameLang).equals(JSONObject.toJSONString(productNameLangVo))){
                    //产品类型
                    String remark = productNameLangVo.getRemark();
                    int count = 0;
                    if (remark.equals("股票")){
                        count = stockProductMapper.updateProductNameLang(productNameLang);
                    }else if(remark.equals("加密货币")){
                        count = cryptocurrencyProductMapper.updateProductNameLang(productNameLang);
                    }else if(remark.equals("期货")){
                        count = futuresProductMapper.updateProductNameLang(productNameLang);
                    }else if(remark.equals("外汇")){
                        count = forexProductMapper.updateProductNameLang(productNameLang);
                    }else {
                        throw new RuntimeException("股票类型错误");
                    }
                    if (count <= 0){
                        throw new RuntimeException("系统繁忙");
                    }
                }
            }
        }
        return "导入成功";
    }

    /**
     * 后台首页报表
     */
    @Override
    public AjaxResult indexReport(){
        //会员人数
        Integer userNum = 0;
        //今日新增会员人数
        Integer newUserNum = 0;

        //平台总充值
        BigDecimal allRechargeAmount = BigDecimal.ZERO;
        //平台总提现
        BigDecimal allWithdrawAmount = BigDecimal.ZERO;
        //平台总上分
        BigDecimal allUpPointAmount = BigDecimal.ZERO;
        //平台总下分
        BigDecimal allDownPointAmount = BigDecimal.ZERO;
        //平台总充值订单数
        Integer allRechargeCount = 0;
        //平台总提现订单数
        Integer allWithdrawCount = 0;
        //平台总上分订单数
        Integer allUpPointCount = 0;
        //平台总下分订单数
        Integer allDownPointCount = 0;
        //今日充值
        BigDecimal rechargeAmountToday = BigDecimal.ZERO;
        //今日提现
        BigDecimal withdrawAmountToday = BigDecimal.ZERO;
        //今日上分
        BigDecimal upPointAmountToday = BigDecimal.ZERO;
        //今日下分
        BigDecimal downPointAmountToday = BigDecimal.ZERO;
        //今日充值订单数
        Integer rechargeCountToday = 0;
        //今日提现订单数
        Integer withdrawCountToday = 0;
        //今日上分订单数
        Integer upPointCountToday = 0;
        //今日下分订单数
        Integer downPointCountToday = 0;

        //平台总余额
        BigDecimal allBalance = BigDecimal.ZERO;
        //今日日期
        String todayDate = DateUtils.getDate();

        UserInfo userInfo = new UserInfo();
        userInfo.setAccountType(0);
        List<UserInfo> userInfos = userInfoService.selectUserInfoList(userInfo);
        userNum = userInfos.size();
        newUserNum = Long.valueOf(userInfos.stream().filter(a->DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,a.getRegTime()).equals(todayDate)).count()).intValue();

        //获取平台币种
        PlatformCurrency platformCurrency = new PlatformCurrency();
        platformCurrency.setStatus(0);
        List<PlatformCurrency> platformCurrencies = platformCurrencyService.selectPlatformCurrencyList(platformCurrency);
        ExchangeRateUtil.fillExchangeRate(platformCurrencies);
        //币种map
        Map<Long, PlatformCurrency> platformCurrencyMap = platformCurrencies.stream().collect(Collectors.toMap(a -> a.getId(), a -> a));
        //查询所有充值订单
        UserRecharge userRecharge = new UserRecharge();
        userRecharge.setAccountType(0);
        userRecharge.setOrderStatus(1);
        List<UserRecharge> userRecharges = userRechargeService.selectUserRechargeList(userRecharge);
        //平台总充值订单数
        allRechargeCount = userRecharges.size();
        //平台默认交易币种
        Long defaultTradeCurrencyId = CacheUtil.getOtherValueByKey("default_trade_currency_id", Long.class);
        if (defaultTradeCurrencyId == null){
            throw new RuntimeException("获取平台默认交易币种信息异常");
        }
        //遍历
        for (int i = 0; i < userRecharges.size(); i++) {
            //币种id
            Long currencyId = userRecharges.get(i).getCurrencyId();
            //汇率
            BigDecimal exchangeRate = ExchangeRateUtil.getExchangeInfo(currencyId,defaultTradeCurrencyId,platformCurrencyMap).get("exchangeRate");
            //充值金额
            BigDecimal rechargeAmount = userRecharges.get(i).getRechargeAmount();
            //折合
            BigDecimal converted = rechargeAmount.multiply(exchangeRate).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            //平台总充值
            allRechargeAmount = allRechargeAmount.add(converted);
        }

        //今日充值的订单
        List<UserRecharge> todayUserRecharges = userRecharges.stream().filter(a -> DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, a.getPayTime()).equals(todayDate)).collect(Collectors.toList());
        //今日充值订单数
        rechargeCountToday = todayUserRecharges.size();
        //遍历
        for (int i = 0; i < todayUserRecharges.size(); i++) {
            //币种id
            Long currencyId = todayUserRecharges.get(i).getCurrencyId();
            //汇率
            BigDecimal exchangeRate = ExchangeRateUtil.getExchangeInfo(currencyId,defaultTradeCurrencyId,platformCurrencyMap).get("exchangeRate");
            //充值金额
            BigDecimal rechargeAmount = todayUserRecharges.get(i).getRechargeAmount();
            //折合
            BigDecimal converted = rechargeAmount.multiply(exchangeRate).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            //平台总充值
            rechargeAmountToday = rechargeAmountToday.add(converted);
        }

        //查询所有提现订单
        UserWithdraw userWithdraw = new UserWithdraw();
        userWithdraw.setAccountType(0);
        userWithdraw.setWithdrawStatus(1);
        userWithdraw.setStatisticalReport(0);
        List<UserWithdraw> userWithdraws = userWithdrawService.selectUserWithdrawList(userWithdraw);
        //平台总提现订单数
        allWithdrawCount = userWithdraws.size();
        //遍历
        for (int i = 0; i < userWithdraws.size(); i++) {
            //币种id
            Long currencyId = userWithdraws.get(i).getCurrencyId();
            //汇率
            BigDecimal exchangeRate = ExchangeRateUtil.getExchangeInfo(currencyId,defaultTradeCurrencyId,platformCurrencyMap).get("exchangeRate");
            //到账金额
            BigDecimal receivedAmount = userWithdraws.get(i).getReceivedAmount();
            //折合
            BigDecimal converted = receivedAmount.multiply(exchangeRate).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            //平台总提现
            allWithdrawAmount = allWithdrawAmount.add(converted);
        }

        //今日提现的订单
        List<UserWithdraw> todayUserWithdraws = userWithdraws.stream().filter(a -> DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, a.getTransTime()).equals(todayDate)).collect(Collectors.toList());
        //今日提现订单数
        withdrawCountToday = todayUserWithdraws.size();
        //遍历
        for (int i = 0; i < todayUserWithdraws.size(); i++) {
            //币种id
            Long currencyId = todayUserWithdraws.get(i).getCurrencyId();
            //汇率
            BigDecimal exchangeRate = ExchangeRateUtil.getExchangeInfo(currencyId,defaultTradeCurrencyId,platformCurrencyMap).get("exchangeRate");
            //到账金额
            BigDecimal receivedAmount = todayUserWithdraws.get(i).getReceivedAmount();
            //折合
            BigDecimal converted = receivedAmount.multiply(exchangeRate).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            //平台今日总提现
            withdrawAmountToday = withdrawAmountToday.add(converted);
        }

        //查询所有上分下分订单
        UserPointChangeRecord userPointChangeRecord = new UserPointChangeRecord();
        userPointChangeRecord.setAccountType(0);
        //所有上下分订单
        List<UserPointChangeRecord> userPointChangeRecords = userPointChangeRecordService.selectUserPointChangeRecordList(userPointChangeRecord);
        //所有上分订单
        List<UserPointChangeRecord> userUpPointRecords = userPointChangeRecords.stream().filter(a -> a.getOrderType().equals(0)).collect(Collectors.toList());
        //平台总上分订单数
        allUpPointCount = userUpPointRecords.size();
        //遍历
        for (int i = 0; i < userUpPointRecords.size(); i++) {
            //币种id
            Long currencyId = userUpPointRecords.get(i).getCurrencyId();
            //汇率
            BigDecimal exchangeRate = ExchangeRateUtil.getExchangeInfo(currencyId,defaultTradeCurrencyId,platformCurrencyMap).get("exchangeRate");
            //上分金额
            BigDecimal orderAmount = userUpPointRecords.get(i).getOrderAmount();
            //折合
            BigDecimal converted = orderAmount.multiply(exchangeRate).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            //平台总上分
            allUpPointAmount = allUpPointAmount.add(converted);
        }

        //今日上分的订单
        List<UserPointChangeRecord> todayUserUpPointRecords = userUpPointRecords.stream().filter(a -> DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, a.getCreateTime()).equals(todayDate)).collect(Collectors.toList());
        //今日上分订单数
        upPointCountToday = todayUserUpPointRecords.size();
        //遍历
        for (int i = 0; i < todayUserUpPointRecords.size(); i++) {
            //币种id
            Long currencyId = todayUserUpPointRecords.get(i).getCurrencyId();
            //汇率
            BigDecimal exchangeRate = ExchangeRateUtil.getExchangeInfo(currencyId,defaultTradeCurrencyId,platformCurrencyMap).get("exchangeRate");
            //上分金额
            BigDecimal orderAmount = todayUserUpPointRecords.get(i).getOrderAmount();
            //折合
            BigDecimal converted = orderAmount.multiply(exchangeRate).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            //今日上分金额
            upPointAmountToday = upPointAmountToday.add(converted);
        }

        //所有下分订单
        List<UserPointChangeRecord> userDownPointRecords = userPointChangeRecords.stream().filter(a -> a.getOrderType().equals(1)).collect(Collectors.toList());
        //平台总下分订单数
        allDownPointCount = userDownPointRecords.size();
        //遍历
        for (int i = 0; i < userDownPointRecords.size(); i++) {
            //币种id
            Long currencyId = userDownPointRecords.get(i).getCurrencyId();
            //汇率
            BigDecimal exchangeRate = ExchangeRateUtil.getExchangeInfo(currencyId,defaultTradeCurrencyId,platformCurrencyMap).get("exchangeRate");
            //下分金额
            BigDecimal orderAmount = userDownPointRecords.get(i).getOrderAmount();
            //折合
            BigDecimal converted = orderAmount.multiply(exchangeRate).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            //平台总下分
            allDownPointAmount = allDownPointAmount.add(converted);
        }

        //今日下分的订单
        List<UserPointChangeRecord> todayUserDownPointRecords = userDownPointRecords.stream().filter(a -> DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, a.getCreateTime()).equals(todayDate)).collect(Collectors.toList());
        //今日下分订单数
        downPointCountToday = todayUserDownPointRecords.size();
        //遍历
        for (int i = 0; i < todayUserDownPointRecords.size(); i++) {
            //币种id
            Long currencyId = todayUserDownPointRecords.get(i).getCurrencyId();
            //汇率
            BigDecimal exchangeRate = ExchangeRateUtil.getExchangeInfo(currencyId,defaultTradeCurrencyId,platformCurrencyMap).get("exchangeRate");
            //下分金额
            BigDecimal orderAmount = todayUserDownPointRecords.get(i).getOrderAmount();
            //折合
            BigDecimal converted = orderAmount.multiply(exchangeRate).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            //今日下分金额
            downPointAmountToday = downPointAmountToday.add(converted);
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("userNum",userNum);
        resultMap.put("newUserNum",newUserNum);
        resultMap.put("allRechargeAmount",allRechargeAmount);
        resultMap.put("allWithdrawAmount",allWithdrawAmount);
        resultMap.put("allUpPointAmount",allUpPointAmount);
        resultMap.put("allDownPointAmount",allDownPointAmount);
        resultMap.put("allBalance",allBalance);
        resultMap.put("rechargeAmountToday",rechargeAmountToday);
        resultMap.put("withdrawAmountToday",withdrawAmountToday);
        resultMap.put("upPointAmountToday",upPointAmountToday);
        resultMap.put("downPointAmountToday",downPointAmountToday);
        resultMap.put("rechargeCountToday",rechargeCountToday);
        resultMap.put("withdrawCountToday",withdrawCountToday);
        resultMap.put("upPointCountToday",upPointCountToday);
        resultMap.put("downPointCountToday",downPointCountToday);
        resultMap.put("allRechargeCount",allRechargeCount);
        resultMap.put("allWithdrawCount",allWithdrawCount);
        resultMap.put("allUpPointCount",allUpPointCount);
        resultMap.put("allDownPointCount",allDownPointCount);
        resultMap.put("appOnlineNum",tokenService.getAppOnlineNum());
        return AjaxResult.success(resultMap);
    }

}
