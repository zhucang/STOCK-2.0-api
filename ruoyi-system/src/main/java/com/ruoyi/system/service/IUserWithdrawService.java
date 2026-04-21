package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.UserWithdraw;

import java.math.BigDecimal;
import java.util.List;

/**
 * 提现记录Service接口
 * 
 * @author ruoyi
 * @date 2023-10-30
 */
public interface IUserWithdrawService 
{
    /**
     * 查询提现记录
     * 
     * @param id 提现记录主键
     * @return 提现记录
     */
    public UserWithdraw selectUserWithdrawById(Long id);

    /**
     * 查询提现记录列表
     * 
     * @param userWithdraw 提现记录
     * @return 提现记录集合
     */
    public List<UserWithdraw> selectUserWithdrawList(UserWithdraw userWithdraw);

    /**
     * 获取统计数据
     * @param userWithdraw
     * @return
     */
    public List<UserWithdraw> getStatisticalData(UserWithdraw userWithdraw);

    /**
     * 查询提现记录列表
     *
     * @param userWithdraw 提现记录
     * @return 提现记录集合
     */
    public List<UserWithdraw> selectUserWithdrawListWithOthers(UserWithdraw userWithdraw);

    /**
     * 填充其他信息
     * @param userWithdraws 用户提现订单列表
     */
    public void fillOtherInfo(List<UserWithdraw> userWithdraws);

    /**
     * 新增提现记录
     * 
     * @param userWithdraw 提现记录
     * @return 结果
     */
    public int insertUserWithdraw(UserWithdraw userWithdraw);

    /**
     * 修改提现记录
     * 
     * @param userWithdraw 提现记录
     * @return 结果
     */
    public int updateUserWithdraw(UserWithdraw userWithdraw);

    /**
     * 批量删除提现记录
     * 
     * @param ids 需要删除的提现记录主键集合
     * @return 结果
     */
    public int deleteUserWithdrawByIds(Long[] ids);

    /**
     * 删除提现记录信息
     * 
     * @param id 提现记录主键
     * @return 结果
     */
    public int deleteUserWithdrawById(Long id);

    /**
     * 提现订单审核
     * @param userWithdrawId 提现订单id
     * @param orderStatus 状态 1：审核通过 2：审核驳回
     * @param withdrawMsg 驳回原因
     * @param remark 备注
     * @return
     */
    public int updateWithdrawOrderStatus(Long userWithdrawId, Integer orderStatus,String withdrawMsg,String remark);

    /**
     * 修改订单备注
     * @param userWithdrawId 提现订单id
     * @param remark 备注
     * @return
     */
    public AjaxResult updateOrderRemark(Long userWithdrawId, String remark);

    /**
     * 修改提现订单是否免客损状态
     * @param userWithdrawId 提现订单id
     * @param statisticalReport 是否统计报表 0：是 1：否
     * @return
     */
    public int updateStatisticalReport(Long userWithdrawId, Integer statisticalReport);

    /**
     * 获取用户的各币种的提现金额
     * @param userId 用户id
     * @return
     */
    public List<UserWithdraw> selectUserWithdrawAmountAllCurrency(Long userId);

//    ------------------------------------------------

    /**
     * 用户提现
     * @param withdrawAmount 提现金额
     * @param withPwd 提现密码
     * @param withdrawAddressId 提现地址id (钱包id或银行卡id)
     * @param withdrawChannelConfigId 提现通道id
     * @return
     */
    public int outMoney(BigDecimal withdrawAmount, String withPwd, Long withdrawAddressId, Long withdrawChannelConfigId);

    /**
     * 用户取消提现
     * @param userWithdrawId 提现记录id
     * @return
     */
    public int userCancel(Long userWithdrawId);
}
