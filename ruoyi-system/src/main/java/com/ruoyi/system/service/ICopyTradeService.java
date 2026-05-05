package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.CopyTradeOrder;
import com.ruoyi.system.domain.CopyTradeRelation;
import com.ruoyi.system.domain.CopyTradeTrader;
import com.ruoyi.system.domain.UserCryptocurrencyPosition;

import java.util.List;

/**
 * 跟单功能服务接口。
 * 对外提供交易员管理、跟单关系管理和跟单同步能力。
 */
public interface ICopyTradeService {
    /**
     * 查询交易员列表。
     *
     * @param copyTradeTrader 交易员筛选条件
     * @return 交易员列表
     */
    List<CopyTradeTrader> selectCopyTradeTraderList(CopyTradeTrader copyTradeTrader);

    /**
     * 根据主键查询交易员详情。
     *
     * @param id 交易员主键
     * @return 交易员详情
     */
    CopyTradeTrader selectCopyTradeTraderById(Long id);

    /**
     * 新增交易员配置。
     *
     * @param copyTradeTrader 交易员信息
     * @return 影响行数
     */
    int insertCopyTradeTrader(CopyTradeTrader copyTradeTrader);

    /**
     * 修改交易员配置。
     *
     * @param copyTradeTrader 交易员信息
     * @return 影响行数
     */
    int updateCopyTradeTrader(CopyTradeTrader copyTradeTrader);

    /**
     * 批量删除交易员配置。
     *
     * @param ids 主键数组
     * @return 影响行数
     */
    int deleteCopyTradeTraderByIds(Long[] ids);

    /**
     * 查询跟单关系列表。
     *
     * @param copyTradeRelation 跟单关系筛选条件
     * @return 跟单关系列表
     */
    List<CopyTradeRelation> selectCopyTradeRelationList(CopyTradeRelation copyTradeRelation);

    /**
     * 根据主键查询跟单关系。
     *
     * @param id 跟单关系主键
     * @return 跟单关系详情
     */
    CopyTradeRelation selectCopyTradeRelationById(Long id);

    /**
     * 批量删除跟单关系。
     *
     * @param ids 主键数组
     * @return 影响行数
     */
    int deleteCopyTradeRelationByIds(Long[] ids);

    /**
     * 创建或恢复用户对交易员的跟单关系。
     *
     * @param copyTradeRelation 跟单关系参数
     * @return 处理结果
     */
    AjaxResult followTrader(CopyTradeRelation copyTradeRelation);

    /**
     * 停止一条跟单关系。
     *
     * @param relationId 跟单关系主键
     * @return 处理结果
     */
    AjaxResult unfollowTrader(Long relationId);

    /**
     * 交易员开仓后触发批量跟单开仓。
     *
     * @param leaderPosition 交易员主仓位
     */
    void handleLeaderOpenPosition(UserCryptocurrencyPosition leaderPosition);

    /**
     * 交易员平仓后触发批量跟单平仓。
     *
     * @param leaderPosition 交易员主仓位
     */
    void handleLeaderClosePosition(UserCryptocurrencyPosition leaderPosition);

    /**
     * 为单个跟单关系同步开仓。
     *
     * @param relation 跟单关系
     * @param leaderPosition 交易员主仓位
     */
    void syncFollowerOpenPosition(CopyTradeRelation relation, UserCryptocurrencyPosition leaderPosition);

    /**
     * 为单个跟单订单同步平仓。
     *
     * @param order 跟单订单映射
     * @param leaderPosition 交易员主仓位
     */
    void syncFollowerClosePosition(CopyTradeOrder order, UserCryptocurrencyPosition leaderPosition);
}
