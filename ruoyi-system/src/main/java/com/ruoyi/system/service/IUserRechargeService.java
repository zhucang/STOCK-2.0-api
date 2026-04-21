package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.UserRecharge;
import com.ruoyi.system.domain.vo.UserOnlineRechargeOrderVo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 用户充值订单Service接口
 * 
 * @author ruoyi
 * @date 2023-10-30
 */
public interface IUserRechargeService 
{
    /**
     * 查询用户充值订单
     * 
     * @param id 用户充值订单主键
     * @return 用户充值订单
     */
    public UserRecharge selectUserRechargeById(Long id);

    /**
     * 查询用户充值订单列表
     * 
     * @param userRecharge 用户充值订单
     * @return 用户充值订单集合
     */
    public List<UserRecharge> selectUserRechargeList(UserRecharge userRecharge);

    /**
     * 获取统计数据
     * @param userRecharge
     * @return
     */
    public List<UserRecharge> getStatisticalData(UserRecharge userRecharge);

    /**
     * 查询用户充值订单列表（含彩金赠送记录)
     *
     * @param userRecharge 用户充值订单
     * @return 用户充值订单集合
     */
    public List<UserRecharge> selectUserRechargeListWithOthers(UserRecharge userRecharge);

    /**
     * 填充其他信息
     * @param userRecharges 用户充值订单列表
     */
    public void fillOtherInfo(List<UserRecharge> userRecharges);

    /**
     * 新增用户充值订单
     * 
     * @param userRecharge 用户充值订单
     * @return 结果
     */
    public int insertUserRecharge(UserRecharge userRecharge);

    /**
     * 修改用户充值订单
     * 
     * @param userRecharge 用户充值订单
     * @return 结果
     */
    public int updateUserRecharge(UserRecharge userRecharge);

    /**
     * 批量删除用户充值订单
     * 
     * @param ids 需要删除的用户充值订单主键集合
     * @return 结果
     */
    public int deleteUserRechargeByIds(Long[] ids);

    /**
     * 删除用户充值订单信息
     * 
     * @param id 用户充值订单主键
     * @return 结果
     */
    public int deleteUserRechargeById(Long id);

    /**
     * 获取用户的各币种的充值金额
     * @param userIds 用户ids
     * @return
     */
    public List<UserRecharge> selectUserRechargeAmountAllCurrency(List<Long> userIds,Date startTime,Date endTime);

    /**
     * 获取用户的各币种的充值金额
     * @param userId 用户id
     * @return
     */
    public List<UserRecharge> selectUserRechargeAmountAllCurrencyByUserId(Long userId);

    /**
     * 充值订单审核
     * @param userRechargeId 充值订单id
     * @param orderStatus 状态：1:通过 2：驳回
     * @param rechargeMsg 驳回信息
     * @param remark 备注
     * @return
     */
    public AjaxResult updateRechargeOrderStatus(Long userRechargeId, Integer orderStatus,String rechargeMsg,String remark);

    /**
     * 修改充值金额
     * @param userRechargeId 充值订单id
     * @param rechargeAmount 充值金额
     * @return
     */
    public AjaxResult updateRechargeAmount(Long userRechargeId, BigDecimal rechargeAmount);

    /**
     * 修改订单备注
     * @param userRechargeId 充值订单id
     * @param remark 备注
     * @return
     */
    public AjaxResult updateOrderRemark(Long userRechargeId, String remark);

    /**
     * 用户充值
     * @param rechargeAmount 充值金额
     * @param payChannelId 充值通道id
     * @param rechargeImg 充值凭证
     * @return
     */
    public int inMoney(BigDecimal rechargeAmount,Long payChannelId,String rechargeImg);

    /**
     * 用户在线支付
     * @param rechargeAmount 充值金额
     * @param payType 支付类型(信用卡A: CA, 信用卡B: CB, 加密货币：CW)
     * @param fiat 法币单位,默认USD
     * @param userId 用户id
     * @return
     */
    public String userOnlineRecharge(BigDecimal rechargeAmount,String payType,String fiat,Long userId);

    /**
     * 在线支付成功异步回调
     */
    public String receiveUserOnlineRechargeOrderState(UserOnlineRechargeOrderVo userOnlineRechargeOrderVo);
}
