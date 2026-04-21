package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.UserInfo;
import com.ruoyi.system.domain.UserAuthRecord;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 用户信息Service接口
 * 
 * @author ruoyi
 * @date 2023-10-28
 */
public interface IUserInfoService 
{
    /**
     * 查询用户信息
     * 
     * @param id 用户信息主键
     * @return 用户信息
     */
    public UserInfo selectUserInfoById(Long id);

    /**
     * 查询用户信息列表
     * 
     * @param userInfo 用户信息
     * @return 用户信息集合
     */
    public List<UserInfo> selectUserInfoList(UserInfo userInfo);

    /**
     * 填充其他信息
     * @param userInfos 用户信息
     */
    public void fillOtherInfo(List<UserInfo> userInfos);

    /**
     * 新增用户信息
     * 
     * @param userInfo 用户信息
     * @return 结果
     */
    public int insertUserInfo(UserInfo userInfo);

    /**
     * 修改用户信息
     * 
     * @param userInfo 用户信息
     * @return 结果
     */
    public int updateUserInfo(UserInfo userInfo);

    /**
     * 批量删除用户信息
     * 
     * @param ids 需要删除的用户信息主键集合
     * @return 结果
     */
    public int deleteUserInfoByIds(Long[] ids);

    /**
     * 修改用户资金（用户资金入款/扣款）
     * @param userId 用户id
     * @param amt 金额
     * @param direction 0：入款 1：扣款
     * @param currencyId 币种id
     * @return
     */
    public int updateAmt(Long userId, BigDecimal amt, Integer direction, Long currencyId, String remark);

    /**
     * 用户彩金操作
     * @param userId 用户id
     * @param amt 金额
     * @param direction 0：彩金赠送（系统充值） 1：彩金赠送（福利彩金） 2：彩金回收
     * @param currencyId 币种id
     * @return
     */
    public int updateWinnings(Long userId, BigDecimal amt, Integer direction, Long currencyId);

    /**
     * 设置用户等级
     * @param userId 用户id
     * @param vipLevel vip等级
     * @return
     */
    AjaxResult setUserVipLevel(Long userId,Integer vipLevel);

    /**
     * 设置信用分
     * @param userId 用户id
     * @param creditScore 信用分
     * @return
     */
    AjaxResult setUserCreditScore(Long userId,Integer creditScore);

    /**
     * 设置用户可提现状态
     * @param userId 用户id
     * @param isCanWithdraw 是否能提现：0：是 1：否
     * @return
     */
    AjaxResult setUserIsCanWithdraw(Long userId,Integer isCanWithdraw);

    /**
     * 设置用户极速输、赢收益率
     * @param userId 用户id
     * @param fastTradeWinProfitRatio 极速赢收益率
     * @param fastTradeLoseProfitRatio 极速输收益率
     * @return
     */
    AjaxResult setFastTradeWinOrLoseProfitRatio(Long userId,BigDecimal fastTradeWinProfitRatio,BigDecimal fastTradeLoseProfitRatio);

    /**
     * 设置用户是否是代理员
     * @param userId 用户id
     * @param isAgent 是否设置为代理员 0：是 1：否
     * @return
     */
    AjaxResult setIsAgent(Long userId,Integer isAgent);

    /**
     * 冻结、解冻资金
     * @param userId 用户id
     * @param currencyId 币种id
     * @param amount 金额
     * @param operationType 操作类型 0：冻结 1：解冻
     * @return
     */
    public int freezeAndUnfreezeFunds(Long userId,Long currencyId,BigDecimal amount,Integer operationType);

    /**
     * 用户实名审核
     * @param userAuthRecord 用户信息
     * @return
     */
    public int authApprove(UserAuthRecord userAuthRecord);


    //app==========================================================app


    /**
     * 用户登陆
     * @param userAccount 用户账号
     * @param userPwd 用户密码
     * @return
     */
    AjaxResult userLogin(String userAccount,String userPwd);

    /**
     * 用户注册
     * @param userAccount 用户账号
     * @param userPwd 用户密码
     * @param inviteCode 填写的邀请码
     * @param yzmCode 注册验证码
     * @param regType 注册类型 0：手机注册 1：邮箱注册
     * @param uuid uuid
     * @param imgValidateCode 图片验证码
     * @return
     */
    public AjaxResult reg(String userAccount,String userPwd,String inviteCode,String yzmCode,Integer regType,String uuid,String imgValidateCode);

    /**
     * 获取国际区号列表
     */
    public List getPhoneAreaCodeList();

    /**
     * 发送手机验证码
     * @param phone 手机号
     * @return
     */
    public int sendPhoneVerificationCode(String phone);

    /**
     * 发送邮箱验证码
     * @param email 邮箱
     * @return
     */
    public int sendEmailVerificationCode(String email);

    /**
     * 用户修改头像
     * @param avatar 头像
     * @return
     */
    public int modifyAvatar(String avatar);

    /**
     * 修改用户信息
     * @param nickName 用户昵称
     * @param userAccount 用户账号
     * @return
     */
    public int modifyUserInfo(String nickName,String userAccount);

    /**
     * 个人中心面板数据
     * @return
     */
    public AjaxResult personCenterPanelData(Long userId);

    /**
     * 用户钱包余额
     * @param currencyId 币种id
     * @return
     */
    public AjaxResult userBalance(Long currencyId);

    /**
     * 用户钱包余额折合信息
     * @param isPlatformCurrency 资产类型 0：账户资产 1：两融资产
     * @return
     */
    public AjaxResult userBalanceConvert(Integer isPlatformCurrency);

    /**
     * 用户实名认证
     * @param userInfo
     * @return
     */
    public int userAuth(UserInfo userInfo);

    /**
     * 修改用户登录密码
     * @param oldPwd 旧密码
     * @param newPwd 新密码
     * @return
     */
    public AjaxResult updateUserPwd(String oldPwd, String newPwd);

    /**
     * 查询用户提现密码
     * @param userId 用户id
     * @return
     */
    String selectUserWithPwdByUserId(Long userId);

    /**
     * 修改用户提现密码
     * @param oldPwd 旧密码
     * @param newPwd 新密码
     * @return
     */
    public AjaxResult updateUserWithPwd(String oldPwd, String newPwd);

    /**
     * 变更用户的上级
     * @param supUserId 上级用户id
     * @param userId 用户id
     * @return
     */
    public int changeSuperior(Long supUserId, Long userId);

    /**
     * 进入/退出模拟交易模式
     */
    public Map changeLoginModel();

    /**
     * 重置模拟账号余额
     * @param userId 用户ID
     * @return
     */
    public int resetDemoAccountBalance(Long userId);

    /**
     * 绑定邮箱
     * @param email 邮箱
     * @return
     */
    public int bindEmail(String email, String yzmCode);
}
