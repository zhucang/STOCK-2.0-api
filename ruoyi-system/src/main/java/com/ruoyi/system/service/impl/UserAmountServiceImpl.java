package com.ruoyi.system.service.impl;

import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.core.domain.entity.UserAmount;
import com.ruoyi.common.exception.LangException;
import com.ruoyi.common.utils.PageUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.PlatformCurrency;
import com.ruoyi.system.domain.UserBillDetail;
import com.ruoyi.system.mapper.PlatformCurrencyMapper;
import com.ruoyi.system.mapper.UserAmountMapper;
import com.ruoyi.system.service.IUserAmountService;
import com.ruoyi.system.service.IUserBillDetailService;
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
 * 用户各币种余额Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-10-28
 */
@Service
public class UserAmountServiceImpl implements IUserAmountService 
{
    @Resource
    private UserAmountMapper userAmountMapper;

    @Resource
    private PlatformCurrencyMapper platformCurrencyMapper;

    @Autowired
    private IUserAmountService userAmountService;

    @Autowired
    private IUserBillDetailService userBillDetailService;

    /**
     * 查询用户各币种余额
     * 
     * @param id 用户各币种余额主键
     * @return 用户各币种余额
     */
    @Override
    public UserAmount selectUserAmountById(Long id)
    {
        return userAmountMapper.selectUserAmountById(id);
    }

    /**
     * 查询用户各币种余额列表
     * 
     * @param userAmount 用户各币种余额
     * @return 用户各币种余额
     */
    @Override
    public List<UserAmount> selectUserAmountList(UserAmount userAmount)
    {
        return userAmountMapper.selectUserAmountList(userAmount);
    }

    /**
     * 查询用户各货币余额信息列表
     *
     * @param userId 用户id
     * @return 用户各货币余额信息集合
     */
    @Override
    public List<UserAmount> selectUserAmountListByUserId(Long userId,List<PlatformCurrency> platformCurrencies){
        if (platformCurrencies == null){
            //平台币种
            PlatformCurrency platformCurrency = new PlatformCurrency();
            platformCurrency.setStatus(0);
            PageUtils.orderBy("sort is null,sort");
            platformCurrencies = platformCurrencyMapper.selectPlatformCurrencyList(platformCurrency);
            PageUtils.clearPage();
        }
        //用户钱包余额
        UserAmount userAmount = new UserAmount();
        userAmount.setUserId(userId);
        //用户现有的钱包余额信息
        List<UserAmount> userAmounts = userAmountMapper.selectUserAmountList(userAmount);
        //map
        Map<Long, UserAmount> userAmountMap = userAmounts.stream().collect(Collectors.toMap(a->a.getCurrencyId(), a -> a));
        //结果（只展示启用的钱包信息）
        List<UserAmount> result = new ArrayList<>();
        for (int i = 0; i < platformCurrencies.size(); i++) {
            //币种信息
            PlatformCurrency platformCurrency = platformCurrencies.get(i);
            //币种id
            Long currencyId = platformCurrency.getId();
            //该币种钱包信息
            UserAmount userAmountVo = userAmountMap.get(currencyId);
            //如果该用户没有该币种钱包信息
            if (userAmountVo == null){
                userAmountVo = new UserAmount();
                userAmountVo.setUserId(userId);
                userAmountVo.setCurrencyId(currencyId);
                userAmountVo.setAmount(BigDecimal.ZERO);
                userAmountVo.setFrozenAmount(BigDecimal.ZERO);
                userAmountVo.setFlexibleInvestmentFunds(BigDecimal.ZERO);
            }
            userAmountVo.setCurrencyName(platformCurrencies.get(i).getCurrencyName());
            result.add(userAmountVo);
        }
        return result;
    }

    /**
     * 新增用户各币种余额
     * 
     * @param userAmount 用户各币种余额
     * @return 结果
     */
    @Override
    public int insertUserAmount(UserAmount userAmount)
    {
        return userAmountMapper.insertUserAmount(userAmount);
    }

    /**
     * 修改用户各币种余额
     * 
     * @param userAmount 用户各币种余额
     * @return 结果
     */
    @Override
    public int updateUserAmount(UserAmount userAmount)
    {
        //还没有此账户余额记录
        if (userAmount.getId() == null){
            return userAmountMapper.insertUserAmount(userAmount);
        }else {
            return userAmountMapper.updateUserAmount(userAmount);
        }
    }

    /**
     * 批量删除用户各币种余额
     * 
     * @param ids 需要删除的用户各币种余额主键
     * @return 结果
     */
    @Override
    public int deleteUserAmountByIds(Long[] ids)
    {
        return userAmountMapper.deleteUserAmountByIds(ids);
    }

    /**
     * 删除用户各币种余额信息
     * 
     * @param id 用户各币种余额主键
     * @return 结果
     */
    @Override
    public int deleteUserAmountById(Long id)
    {
        return userAmountMapper.deleteUserAmountById(id);
    }

    /**
     * 获取用户某币种余额
     * @param userId 用户id
     * @param currencyId 货币id
     */
    @Override
    public UserAmount getUserAmount(Long userId, Long currencyId) {
        //用户该币种余额信息
        UserAmount userAmount = userAmountMapper.getUserAmount(userId, currencyId);
        if (userAmount == null) {
            userAmount = new UserAmount();
            userAmount.setUserId(userId);
            userAmount.setCurrencyId(currencyId);
            userAmount.setAmount(BigDecimal.ZERO);
            userAmount.setFrozenAmount(BigDecimal.ZERO);
            userAmount.setFlexibleInvestmentFunds(BigDecimal.ZERO);
        }
        return userAmount;
    }

    /**
     * 转入/转出灵活投资资金
     * @param transferType 转移类型 0：转入 1：转出
     * @param transferAmount 转移金额
     * @param currencyId 币种
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int transferFlexibleInvestmentFunds(Integer transferType, BigDecimal transferAmount, Long currencyId){
        //用户ID
        Long userId = SecurityUtils.getUserId();
        //用户钱包信息
        UserAmount userAmount = userAmountService.getUserAmount(userId, currencyId);
        //用户余额变更前
        BigDecimal userAmountBefore = userAmount.getAmount();
        //用户灵活投资资金变更前
        BigDecimal flexibleInvestmentFundsBefore = userAmount.getFlexibleInvestmentFunds();
        //如果是转入
        if (transferType.equals(0)){
            if (userAmountBefore.compareTo(transferAmount) < 0){
                throw new LangException("hint_17", "钱包余额不足");
            }
            transferAmount = transferAmount.negate();
        }else {
            if (flexibleInvestmentFundsBefore.compareTo(transferAmount) < 0){
                throw new LangException("hint_94", "灵活投资资金不足");
            }
        }
        //用户余额变更后
        BigDecimal userAmountAfter = userAmountBefore.add(transferAmount);
        //用户灵活投资资金变更后
        BigDecimal flexibleInvestmentFundsAfter = flexibleInvestmentFundsBefore.subtract(transferAmount);
        //更新用户钱包信息
        userAmount.setAmount(userAmountAfter);
        userAmount.setFlexibleInvestmentFunds(flexibleInvestmentFundsAfter);
        int updateUserAmount = userAmountService.updateUserAmount(userAmount);
        if (updateUserAmount == 0){
            throw new LangException(HintConstants.SYSTEM_ERR, "更新用户钱包信息异常");
        }
        //用户流水记录
        UserBillDetail userBillDetail = new UserBillDetail();
        userBillDetail.setUserId(userId);
        userBillDetail.setDeType(transferAmount.compareTo(BigDecimal.ZERO) > 0 ? "转入灵活投资资金返还" : "转入灵活投资资金扣除");
        userBillDetail.setDeSummary(transferAmount.compareTo(BigDecimal.ZERO) > 0 ? "转入灵活投资资金返还" : "转入灵活投资资金扣除");
        userBillDetail.setOrderAmount(transferAmount);
        userBillDetail.setOrderTime(new Date());
        userBillDetail.setAmountBefore(userAmountBefore);
        userBillDetail.setAmountAfter(userAmountAfter);
        userBillDetail.setRelateOrderId(null);
        userBillDetail.setOrderClass(transferAmount.compareTo(BigDecimal.ZERO) > 0 ? 76 : 75);
        userBillDetail.setCurrencyId(currencyId);
        int insert = userBillDetailService.insertUserBillDetail(userBillDetail);
        if (insert <= 0) {
            throw new LangException(HintConstants.SYSTEM_ERR, "插入用户流水异常");
        }
        return 1;
    }
}
