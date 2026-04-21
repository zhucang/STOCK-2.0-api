package com.ruoyi.system.service.impl;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.core.domain.entity.UserAmount;
import com.ruoyi.common.core.domain.entity.UserInfo;
import com.ruoyi.common.exception.LangException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.PlatformCurrency;
import com.ruoyi.system.domain.UserBillDetail;
import com.ruoyi.system.domain.UserTransferMoneyRecord;
import com.ruoyi.system.mapper.UserInfoMapper;
import com.ruoyi.system.mapper.UserTransferMoneyRecordMapper;
import com.ruoyi.system.service.IPlatformCurrencyService;
import com.ruoyi.system.service.IUserAmountService;
import com.ruoyi.system.service.IUserBillDetailService;
import com.ruoyi.system.service.IUserTransferMoneyRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 用户转账记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-05-14
 */
@Service
public class UserTransferMoneyRecordServiceImpl implements IUserTransferMoneyRecordService 
{
    @Resource
    private UserTransferMoneyRecordMapper userTransferMoneyRecordMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Autowired
    private IPlatformCurrencyService platformCurrencyService;

    @Autowired
    private IUserAmountService userAmountService;

    @Autowired
    private IUserBillDetailService userBillDetailService;

    /**
     * 查询用户转账记录
     * 
     * @param userTransferMoneyRecordId 用户转账记录主键
     * @return 用户转账记录
     */
    @Override
    public UserTransferMoneyRecord selectUserTransferMoneyRecordByUserTransferMoneyRecordId(Long userTransferMoneyRecordId)
    {
        return userTransferMoneyRecordMapper.selectUserTransferMoneyRecordByUserTransferMoneyRecordId(userTransferMoneyRecordId);
    }

    /**
     * 查询用户转账记录列表
     * 
     * @param userTransferMoneyRecord 用户转账记录
     * @return 用户转账记录
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u", isUserInfo = true)
    public List<UserTransferMoneyRecord> selectUserTransferMoneyRecordList(UserTransferMoneyRecord userTransferMoneyRecord)
    {
        return userTransferMoneyRecordMapper.selectUserTransferMoneyRecordList(userTransferMoneyRecord);
    }

    /**
     * 新增用户转账记录
     * 
     * @param userTransferMoneyRecord 用户转账记录
     * @return 结果
     */
    @Override
    public int insertUserTransferMoneyRecord(UserTransferMoneyRecord userTransferMoneyRecord)
    {
        userTransferMoneyRecord.setCreateTime(DateUtils.getNowDate());
        return userTransferMoneyRecordMapper.insertUserTransferMoneyRecord(userTransferMoneyRecord);
    }

    /**
     * 修改用户转账记录
     * 
     * @param userTransferMoneyRecord 用户转账记录
     * @return 结果
     */
    @Override
    public int updateUserTransferMoneyRecord(UserTransferMoneyRecord userTransferMoneyRecord)
    {
        return userTransferMoneyRecordMapper.updateUserTransferMoneyRecord(userTransferMoneyRecord);
    }

    /**
     * 批量删除用户转账记录
     * 
     * @param userTransferMoneyRecordIds 需要删除的用户转账记录主键
     * @return 结果
     */
    @Override
    public int deleteUserTransferMoneyRecordByUserTransferMoneyRecordIds(Long[] userTransferMoneyRecordIds)
    {
        return userTransferMoneyRecordMapper.deleteUserTransferMoneyRecordByUserTransferMoneyRecordIds(userTransferMoneyRecordIds);
    }

    /**
     * 删除用户转账记录信息
     * 
     * @param userTransferMoneyRecordId 用户转账记录主键
     * @return 结果
     */
    @Override
    public int deleteUserTransferMoneyRecordByUserTransferMoneyRecordId(Long userTransferMoneyRecordId)
    {
        return userTransferMoneyRecordMapper.deleteUserTransferMoneyRecordByUserTransferMoneyRecordId(userTransferMoneyRecordId);
    }

    /**
     * 用户转账
     * @param userTransferMoneyRecord
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int transferMoneyToOtherUser(UserTransferMoneyRecord userTransferMoneyRecord){
        //转账用户ID
        Long userIdFrom = userTransferMoneyRecord.getUserIdFrom();
        //收款用户账号
        String userAccountTo = userTransferMoneyRecord.getUserAccountTo().trim();
        //收款用户信息
        UserInfo userInfo = userInfoMapper.selectUserInfoByUserAccount(userAccountTo);
        if (userInfo == null){
            throw new LangException("hint_92","请输入有效的收款用户账号");
        }
        //收款用户ID
        Long userIdTo = userInfo.getId();
        if (userIdFrom.equals(userIdTo)){
            throw new LangException("hint_92","请勿向自己转账");
        }
        //提现密码
        Object withPwdObj = userTransferMoneyRecord.getParams().get("withPwd");
        if (withPwdObj == null){
            throw new LangException(HintConstants.PARAM_NULL,"请输入提现密码");
        }
        //提现密码
        String withPwd = String.valueOf(withPwdObj);
        //用户提现密码
        String userWithPwd = userInfoMapper.selectUserWithPwdByUserId(userIdFrom);
        //密码错误
        if (!SecurityUtils.matchesPassword(withPwd,userWithPwd)){
            throw new LangException("hint_withdrawPwdIsWrong","提现密码错误");
        }
        userTransferMoneyRecord.setUserIdTo(userIdTo);
        userTransferMoneyRecord.setCreateTime(new Date());

        //转账币种ID
        Long currencyId = userTransferMoneyRecord.getCurrencyId();
        //获取转账币种信息
        PlatformCurrency platformCurrency = platformCurrencyService.selectPlatformCurrencyById(currencyId);
        if (platformCurrency == null || !platformCurrency.getStatus().equals(0)){
            throw new LangException(HintConstants.SYSTEM_BUSY,"获取转出币种信息异常");
        }
        //插入转账记录
        int insertUserTransferMoneyRecord = userTransferMoneyRecordMapper.insertUserTransferMoneyRecord(userTransferMoneyRecord);
        if (insertUserTransferMoneyRecord == 0){
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }

        //转账金额
        BigDecimal transferAmount = userTransferMoneyRecord.getTransferAmount();
        //转账钱包信息
        UserAmount userAmountFrom = userAmountService.getUserAmount(userIdFrom,currencyId);
        //转账钱包余额变更前
        BigDecimal userAmountFromBefore = userAmountFrom.getAmount();
        //判断资金是否充足
        if (userAmountFromBefore.compareTo(transferAmount) < 0){
            throw new LangException("hint_17","钱包余额不足");
        }
        //转账钱包余额变更后
        BigDecimal userAmountFromAfter = userAmountFromBefore.subtract(transferAmount);
        //更新转出钱包余额
        userAmountFrom.setAmount(userAmountFromAfter);
        int countFrom = userAmountService.updateUserAmount(userAmountFrom);
        if (countFrom <= 0){
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
        //账户明细
        UserBillDetail userBillDetail = new UserBillDetail();
        userBillDetail.setUserId(userIdFrom);
        userBillDetail.setDeType("转账扣除");
        userBillDetail.setDeSummary("转账扣除");
        userBillDetail.setOrderAmount(transferAmount.negate());
        userBillDetail.setOrderTime(new Date());
        userBillDetail.setAmountBefore(userAmountFromBefore);
        userBillDetail.setAmountAfter(userAmountFromAfter);
        userBillDetail.setRelateOrderId(userTransferMoneyRecord.getUserTransferMoneyRecordId());
        userBillDetail.setOrderClass(73);
        userBillDetail.setCurrencyId(currencyId);
        int insertUserBillDetail = userBillDetailService.insertUserBillDetail(userBillDetail);
        if (insertUserBillDetail <= 0) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }

        //收款钱包信息
        UserAmount userAmountTo = userAmountService.getUserAmount(userIdTo,currencyId);
        //收款钱包余额变更前
        BigDecimal userAmountToBefore = userAmountTo.getAmount();
        //收款钱包余额变更后
        BigDecimal userAmountToAfter = userAmountToBefore.add(transferAmount);
        //更新转出钱包余额
        userAmountTo.setAmount(userAmountToAfter);
        int countTo = userAmountService.updateUserAmount(userAmountTo);
        if (countTo <= 0){
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
        //账户明细
        UserBillDetail userBillDetail2 = new UserBillDetail();
        userBillDetail2.setUserId(userIdTo);
        userBillDetail2.setDeType("转账收入");
        userBillDetail2.setDeSummary("转账收入");
        userBillDetail2.setOrderAmount(transferAmount);
        userBillDetail2.setOrderTime(new Date());
        userBillDetail2.setAmountBefore(userAmountToBefore);
        userBillDetail2.setAmountAfter(userAmountToAfter);
        userBillDetail2.setRelateOrderId(userTransferMoneyRecord.getUserTransferMoneyRecordId());
        userBillDetail2.setOrderClass(74);
        userBillDetail2.setCurrencyId(currencyId);
        int insertUserBillDetail2 = userBillDetailService.insertUserBillDetail(userBillDetail2);
        if (insertUserBillDetail2 <= 0) {
            throw new LangException(HintConstants.SYSTEM_BUSY,"系统繁忙");
        }
        return 1;
    }
}
