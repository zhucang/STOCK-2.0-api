package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.core.domain.entity.UserAmount;
import com.ruoyi.common.exception.LangException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.domain.BalanceConvertRecord;
import com.ruoyi.system.domain.PlatformCurrency;
import com.ruoyi.system.domain.UserBillDetail;
import com.ruoyi.system.mapper.BalanceConvertRecordMapper;
import com.ruoyi.system.service.*;
import com.ruoyi.system.utils.cache.CacheUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 资金互转记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-12-01
 */
@Service
public class BalanceConvertRecordServiceImpl implements IBalanceConvertRecordService 
{
    @Resource
    private BalanceConvertRecordMapper balanceConvertRecordMapper;

    @Autowired
    private IPlatformCurrencyService platformCurrencyService;

    @Autowired
    private IUserAmountService userAmountService;

    @Autowired
    private IUserBillDetailService userBillDetailService;

    @Autowired
    private ICurrencyExchangeRateService currencyExchangeRateService;

    /**
     * 查询资金互转记录列表
     * 
     * @param balanceConvertRecord 资金互转记录
     * @return 资金互转记录
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u", isUserInfo = true)
//    @Cacheable(value = CacheableKey.BALANCE_CONVERT_RECORD + CacheableKey.LIST,key = "#balanceConvertRecord.cacheableKey()")
    public List<BalanceConvertRecord> selectBalanceConvertRecordList(BalanceConvertRecord balanceConvertRecord)
    {
        return balanceConvertRecordMapper.selectBalanceConvertRecordList(balanceConvertRecord);
    }

    /**
     * 资金互转
     * @param userId 用户id
     * @param transAmount 转出金额
     * @param currencyIdFrom 转出币种id
     * @param currencyIdTo 转入币种id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
//    @CacheEvict(value = CacheableKey.BALANCE_CONVERT_RECORD + CacheableKey.LIST,allEntries = true)
    public int balanceConvert(Long userId,BigDecimal transAmount, Long currencyIdFrom, Long currencyIdTo){
        if (userId == null){
            userId = SecurityUtils.getUserId();
        }
        //获取今日已转次数
        int balanceConvertCountToday = balanceConvertRecordMapper.getBalanceConvertCountTodayByUserId(userId);
        //每日资金互转限制次数
        Integer tranAmountCountDailyLimit = CacheUtils.getOtherValueByKey("tranAmountCount.dailyLimit",Integer.class);
        if (tranAmountCountDailyLimit != null && balanceConvertCountToday >= tranAmountCountDailyLimit){
            List<Object> list = new ArrayList<>();
            list.add(tranAmountCountDailyLimit);
            throw new LangException("hint_32",list,"资金互转每日限制"+tranAmountCountDailyLimit + "次，今日已达上限");
        }
        //获取转出币种信息
        PlatformCurrency currencyFrom = platformCurrencyService.selectPlatformCurrencyById(currencyIdFrom);
        if (currencyFrom == null){
            throw new LangException(HintConstants.SYSTEM_BUSY,"获取转出币种信息异常");
        }
        //日志记录转出币种名称
        HttpUtils.getRequestLogParams().put("currencyNameFrom",currencyFrom.getCurrencyName());
        //获取转入币种信息
        PlatformCurrency currencyTo = platformCurrencyService.selectPlatformCurrencyById(currencyIdTo);
        if (currencyTo == null){
            throw new LangException(HintConstants.SYSTEM_BUSY,"获取转出币种信息异常");
        }
        //日志记录转入币种名称
        HttpUtils.getRequestLogParams().put("currencyNameTo",currencyTo.getCurrencyName());
        if (currencyFrom.getBalanceConvertMaxLimit().compareTo(transAmount) < 0 || currencyFrom.getBalanceConvertMinLimit().compareTo(transAmount) > 0){
            List<Object> list = new ArrayList<>();
            list.add(currencyFrom.getBalanceConvertMinLimit());
            list.add(currencyFrom.getBalanceConvertMaxLimit());
            throw new LangException("hint_33",list,"此币种单笔转化金额限制为"+currencyFrom.getBalanceConvertMinLimit() + "~" + currencyFrom.getBalanceConvertMaxLimit());
        }
        //转出钱包
        UserAmount userAmountFrom = userAmountService.getUserAmount(userId, currencyIdFrom);
        //转出钱包余额变更前
        BigDecimal userAmountFromBefore = userAmountFrom.getAmount();
        //判断资金是否充足
        if (userAmountFrom.getAmount().compareTo(transAmount) < 0){
            throw new LangException("hint_17","可转化资金不足");
        }
        //获取汇率详情 （汇率和手续费）
        Map<String, BigDecimal> exchangeInfo = currencyExchangeRateService.getExchangeInfo(currencyIdFrom, currencyIdTo);
        //手续费率
        BigDecimal feeRatio = exchangeInfo.get("feeRatio");
        //日志记录手续费率
        HttpUtils.getRequestLogParams().put("feeRatio", feeRatio+"%");
        //汇率
        BigDecimal exchangeRate = exchangeInfo.get("exchangeRate");
        //汇率转化
        //转化的金额
        BigDecimal convertedAmount = transAmount.multiply(exchangeRate).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
        if (convertedAmount.compareTo(BigDecimal.ZERO) == 0){
            throw new LangException("hint_28","获取转化汇率异常，请稍后再尝试");
        }
        //手续费
        BigDecimal fee = convertedAmount.multiply(feeRatio).divide(new BigDecimal(100),Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
        //保存资金互转日志
        BalanceConvertRecord balanceConvertRecord = new BalanceConvertRecord();
        balanceConvertRecord.setUserId(userId);
        balanceConvertRecord.setTransAmt(transAmount);
        balanceConvertRecord.setConvertedAmount(convertedAmount);
        balanceConvertRecord.setExchangeRate(exchangeRate);
        balanceConvertRecord.setHandingFee(fee);
        balanceConvertRecord.setCreateTime(new Date());
        balanceConvertRecord.setCurrencyFromId(currencyIdFrom);
        balanceConvertRecord.setCurrencyToId(currencyIdTo);
        int insertBalanceConvertRecord = balanceConvertRecordMapper.insertBalanceConvertRecord(balanceConvertRecord);
        if (insertBalanceConvertRecord <= 0) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
        //日志记录资金互转信息
        HttpUtils.getRequestLogParams().put("balanceConvertRecord", JSONObject.toJSONString(balanceConvertRecord));

        //转出钱包余额变更后
        BigDecimal userAmountFromAfter = userAmountFromBefore.subtract(transAmount);
        //更新转出钱包余额
        userAmountFrom.setAmount(userAmountFromAfter);
        int countFrom = userAmountService.updateUserAmount(userAmountFrom);
        if (countFrom <= 0){
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
        //账户明细
        UserBillDetail userBillDetail = new UserBillDetail();
        userBillDetail.setUserId(userId);
        userBillDetail.setDeType("资金互转扣除");
        userBillDetail.setDeSummary("资金互转扣除成功");
        userBillDetail.setOrderAmount(transAmount.negate());
        userBillDetail.setOrderTime(new Date());
        userBillDetail.setAmountBefore(userAmountFromBefore);
        userBillDetail.setAmountAfter(userAmountFromAfter);
        userBillDetail.setRelateOrderId(balanceConvertRecord.getId());
        userBillDetail.setOrderClass(11);
        userBillDetail.setCurrencyId(currencyIdFrom);
        int insertUserBillDetail = userBillDetailService.insertUserBillDetail(userBillDetail);
        if (insertUserBillDetail <= 0) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }

        //转入钱包
        UserAmount userAmountTo = userAmountService.getUserAmount(userId, currencyIdTo);
        //转入钱包余额变更前
        BigDecimal userAmountToBefore = userAmountTo.getAmount();
        //转入钱包余额变更后
        BigDecimal userAmountToAfter = userAmountToBefore.add(convertedAmount);
        //账户明细
        UserBillDetail userBillDetail2 = new UserBillDetail();
        userBillDetail2.setUserId(userId);
        userBillDetail2.setDeType("资金互转收入");
        userBillDetail2.setDeSummary("资金互转收入成功");
        userBillDetail2.setOrderAmount(convertedAmount);
        userBillDetail2.setOrderTime(new Date());
        userBillDetail2.setAmountBefore(userAmountToBefore);
        userBillDetail2.setAmountAfter(userAmountToAfter);
        userBillDetail2.setRelateOrderId(balanceConvertRecord.getId());
        userBillDetail2.setOrderClass(12);
        userBillDetail2.setCurrencyId(currencyIdTo);
        try {
            int insertUserBillDetail2 = userBillDetailService.insertUserBillDetail(userBillDetail2);
            if (insertUserBillDetail2 <= 0) {
                throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
            }
        }catch (DataIntegrityViolationException e){
            throw new LangException("hint_1","兑换金额数目过大，兑换失败");
        }
        //如果有手续费
        if (fee.compareTo(BigDecimal.ZERO) > 0){
            //转入钱包余额变更前
            userAmountToBefore = userAmountToAfter;
            //转入钱包余额变更后
            userAmountToAfter = userAmountToBefore.subtract(fee);
            //资金互转手续费扣除
            UserBillDetail userBillDetail3 = new UserBillDetail();
            userBillDetail3.setUserId(userId);
            userBillDetail3.setDeType("资金互转手续费扣除");
            userBillDetail3.setDeSummary("资金互转手续费扣除");
            userBillDetail3.setOrderAmount(fee.negate());
            userBillDetail3.setOrderTime(new Date());
            userBillDetail3.setAmountBefore(userAmountToBefore);
            userBillDetail3.setAmountAfter(userAmountToAfter);
            userBillDetail3.setRelateOrderId(balanceConvertRecord.getId());
            userBillDetail3.setOrderClass(16);
            userBillDetail3.setCurrencyId(currencyIdTo);
            try {
                int insertUserBillDetail3 = userBillDetailService.insertUserBillDetail(userBillDetail3);
                if (insertUserBillDetail3 <= 0) {
                    throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
                }
            }catch (DataIntegrityViolationException e){
                throw new LangException("hint_1","兑换金额数目过大，兑换失败");
            }
        }
        //更新转入钱包余额
        userAmountTo.setAmount(userAmountToAfter);
        try {
            int countTo = userAmountService.updateUserAmount(userAmountTo);
            if (countTo <= 0) {
                throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
            }
        }catch (DataIntegrityViolationException e){
            throw new LangException("hint_1","兑换金额数目过大，兑换失败");
        }
        return 1;
    }
}
