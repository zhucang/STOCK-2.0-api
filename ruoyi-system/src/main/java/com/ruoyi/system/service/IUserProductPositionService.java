package com.ruoyi.system.service;

import com.ruoyi.system.domain.UserProductPosition;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户合约交易订单Service接口
 * 
 * @author ruoyi
 * @date 2025-06-25
 */
public interface IUserProductPositionService 
{
    /**
     * 查询用户合约交易订单
     * 
     * @param id 用户合约交易订单主键
     * @return 用户合约交易订单
     */
    public UserProductPosition selectUserProductPositionById(Long id);

    /**
     * 查询用户合约交易订单列表
     * 
     * @param userProductPosition 用户合约交易订单
     * @return 用户合约交易订单集合
     */
    public List<UserProductPosition> selectUserProductPositionList(UserProductPosition userProductPosition);

    /**
     * 填充其他信息
     * @param positions 用户持仓列表
     */
    public void fillOtherInfo(List<UserProductPosition> positions);

    /**
     * 新增用户合约交易订单
     * 
     * @param userProductPosition 用户合约交易订单
     * @return 结果
     */
    public int insertUserProductPosition(UserProductPosition userProductPosition);

    /**
     * 修改用户合约交易订单
     * 
     * @param userProductPosition 用户合约交易订单
     * @return 结果
     */
    public int updateUserProductPosition(UserProductPosition userProductPosition);

    /**
     * 批量删除用户合约交易订单
     * 
     * @param ids 需要删除的用户合约交易订单主键集合
     * @return 结果
     */
    public int deleteUserProductPositionByIds(Long[] ids);

    /**
     * 用户持仓锁仓、解仓操作
     * @param positionId 持仓id
     * @param lockStatus 锁定状态 0：解锁 1：锁定
     * @param lockMsg
     * @return
     */
    public int lockUserPosition(Long positionId, Integer lockStatus, String lockMsg);

    /**
     * 强制平仓操作
     * @param positionId 持仓id
     * @param sellMode 0：平仓价格平仓 1：盈亏比例平仓 2：盈亏金额平仓
     * @return
     */
    public int forceSell(Long positionId, Integer sellMode, BigDecimal target);

    /**
     * 删除用户合约交易订单信息
     * 
     * @param id 用户合约交易订单主键
     * @return 结果
     */
    public int deleteUserProductPositionById(Long id);

    /**
     * 用户合约交易下单
     * @param position
     * @return
     */
    public int buy(UserProductPosition position);

    /**
     * 用户合约交易卖出
     * @param positionId
     * @return
     */
    public int sell(Long positionId, Integer doType, BigDecimal nowPrice);

    /**
     * 止盈止损定时任务
     */
    public void stopProfitAndLossTask();
}
