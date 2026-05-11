package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.ticker.TickerInfo;
import com.ruoyi.system.domain.CopyTradePositionSnapshot;
import com.ruoyi.system.domain.CopyTradeRelation;
import com.ruoyi.system.domain.ProductSetting;
import com.ruoyi.system.domain.UserCryptocurrencyPosition;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 用户加密货币持仓Service接口
 * 
 * @author ruoyi
 * @date 2023-11-05
 */
public interface IUserCryptocurrencyPositionService 
{
    /**
     * 查询用户加密货币持仓
     * 
     * @param id 用户加密货币持仓主键
     * @return 用户加密货币持仓
     */
    public UserCryptocurrencyPosition selectUserCryptocurrencyPositionById(Long id);

    /**
     * 查询用户加密货币持仓列表
     * 
     * @param userCryptocurrencyPosition 用户加密货币持仓
     * @return 用户加密货币持仓集合
     */
    public List<UserCryptocurrencyPosition> selectUserCryptocurrencyPositionList(UserCryptocurrencyPosition userCryptocurrencyPosition);

    /**
     * 填充其他信息
     * @param positions 股票持仓列表
     */
    public void fillOtherInfo(List<UserCryptocurrencyPosition> positions);

    /**
     * 新增用户加密货币持仓
     * 
     * @param userCryptocurrencyPosition 用户加密货币持仓
     * @return 结果
     */
    public int insertUserCryptocurrencyPosition(UserCryptocurrencyPosition userCryptocurrencyPosition);

    /**
     * 修改用户加密货币持仓
     * 
     * @param userCryptocurrencyPosition 用户加密货币持仓
     * @return 结果
     */
    public int updateUserCryptocurrencyPosition(UserCryptocurrencyPosition userCryptocurrencyPosition);

    /**
     * 批量删除用户加密货币持仓
     * 
     * @param ids 需要删除的用户加密货币持仓主键集合
     * @return 结果
     */
    public int deleteUserCryptocurrencyPositionByIds(Long[] ids);

    /**
     * 删除用户加密货币持仓信息
     * 
     * @param id 用户加密货币持仓主键
     * @return 结果
     */
    public int deleteUserCryptocurrencyPositionById(Long id);

    /**
     * 获取保证金金额
     * @param userId 用户id
     * @param positions 用户持仓
     * @return
     */
    public BigDecimal getAllMarginAmountAmountByUserId(Long userId, List<UserCryptocurrencyPosition> positions);

    /**
     * 获取总盈亏
     * @param userId 用户id
     * @param positions 用户持仓
     * @param tickerInfoMap 行情map
     * @return
     */
    public BigDecimal getAllProfitAndLoseByUserId(Long userId, List<UserCryptocurrencyPosition> positions, Map<String, TickerInfo> tickerInfoMap);


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
     * 用户加密货币合约交易下单
     * @param position
     * @return
     */
    public int buy(UserCryptocurrencyPosition position);

    /**
     * 为跟单用户开仓
     *
     * @param followerUserId 跟单用户ID
     * @param leaderPosition 交易员持仓
     * @param relation 跟单关系(跟单人员)
     * @return 新创建的跟单持仓
     */
    UserCryptocurrencyPosition openCopyTradePosition(Long followerUserId, CopyTradePositionSnapshot leaderPosition, CopyTradeRelation relation);

    /**
     * 用户加密货币合约交易卖出
     *
     * @param positionId 持仓id
     * @param doType 平仓类型 0:强制平仓 1:用户平仓
     * @param nowPrice 现价
     * @return 结果
     */
    public AjaxResult sell(Long positionId, Integer doType, BigDecimal nowPrice);

    /**
     * 加密货币爆仓定时任务
     */
    public void cryptocurrencyPositionForceSellTask();

    /**
     * 加密货币爆仓定时任务
     */
    public void doCryptocurrencyPositionForceSellTask(Long userId, List<UserCryptocurrencyPosition> userPositions, Map<String, TickerInfo> tickerInfoMap, ProductSetting productSetting);

    /**
     * 加密货币止盈止损定时任务
     */
    public void cryptocurrencyStopProfitAndLossTask();
}
