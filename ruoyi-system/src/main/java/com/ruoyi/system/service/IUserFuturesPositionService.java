package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.ticker.TickerInfo;
import com.ruoyi.system.domain.ProductSetting;
import com.ruoyi.system.domain.UserFuturesPosition;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 用户期货持仓Service接口
 * 
 * @author ruoyi
 * @date 2023-11-05
 */
public interface IUserFuturesPositionService 
{
    /**
     * 查询用户期货持仓
     * 
     * @param id 用户期货持仓主键
     * @return 用户期货持仓
     */
    public UserFuturesPosition selectUserFuturesPositionById(Long id);

    /**
     * 查询用户期货持仓列表
     * 
     * @param userFuturesPosition 用户期货持仓
     * @return 用户期货持仓集合
     */
    public List<UserFuturesPosition> selectUserFuturesPositionList(UserFuturesPosition userFuturesPosition);

    /**
     * 填充其他信息
     * @param positions 股票持仓列表
     */
    public void fillOtherInfo(List<UserFuturesPosition> positions);

    /**
     * 新增用户期货持仓
     * 
     * @param userFuturesPosition 用户期货持仓
     * @return 结果
     */
    public int insertUserFuturesPosition(UserFuturesPosition userFuturesPosition);

    /**
     * 修改用户期货持仓
     * 
     * @param userFuturesPosition 用户期货持仓
     * @return 结果
     */
    public int updateUserFuturesPosition(UserFuturesPosition userFuturesPosition);

    /**
     * 批量删除用户期货持仓
     * 
     * @param ids 需要删除的用户期货持仓主键集合
     * @return 结果
     */
    public int deleteUserFuturesPositionByIds(Long[] ids);

    /**
     * 删除用户期货持仓信息
     * 
     * @param id 用户期货持仓主键
     * @return 结果
     */
    public int deleteUserFuturesPositionById(Long id);

    /**
     * 获取保证金金额
     * @param userId 用户id
     * @param positions 用户持仓
     * @return
     */
    public BigDecimal getAllMarginAmountAmountByUserId(Long userId, List<UserFuturesPosition> positions);

    /**
     * 获取总盈亏
     * @param userId 用户id
     * @param positions 用户持仓
     * @param tickerInfoMap 行情map
     * @return
     */
    public BigDecimal getAllProfitAndLoseByUserId(Long userId, List<UserFuturesPosition> positions, Map<String, TickerInfo> tickerInfoMap);

    /**
     * 用户持仓锁仓、解仓操作
     * @param positionId 持仓id
     * @param lockStatus 锁定状态 0：解锁 1：锁定
     * @param lockMsg
     * @return
     */
    AjaxResult lockUserPosition(Long positionId, Integer lockStatus, String lockMsg);

    /**
     * 强制平仓操作
     * @param positionId 持仓id
     * @param sellMode 0：平仓价格平仓 1：盈亏比例平仓 2：盈亏金额平仓
     * @return
     */
    AjaxResult forceSell(Long positionId,Integer sellMode,BigDecimal target);

    /**
     * 用户期货合约交易下单
     * @param position
     * @return
     */
    public int buy(UserFuturesPosition position);

    /**
     * 用户期货合约交易卖出
     *
     * @param positionId 持仓id
     * @param doType 平仓类型 0:强制平仓 1:用户平仓
     * @param nowPrice 现价
     * @return 结果
     */
    public AjaxResult sell(Long positionId, Integer doType, BigDecimal nowPrice);

    /**
     * 期货爆仓定时任务
     */
    public void futuresPositionForceSellTask();

    /**
     * 期货爆仓定时任务
     */
    public void doFuturesPositionForceSellTask(Long userId, List<UserFuturesPosition> userPositions, Map<String, TickerInfo> tickerInfoMap, ProductSetting productSetting);

    /**
     * 期货止盈止损定时任务
     */
    public void futuresStopProfitAndLossTask();
}
