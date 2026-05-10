package com.ruoyi.system.service;

import com.ruoyi.system.domain.CopyTradeTrader;

import java.util.List;

/**
 * 跟单交易员服务接口。
 * 负责交易员配置表的查询、维护以及业务侧常用读取能力。
 */
public interface ICopyTradeTraderService {
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
     * 根据用户ID查询启用状态的交易员。
     *
     * @param userId 交易员用户ID
     * @return 交易员配置
     */
    CopyTradeTrader selectActiveCopyTradeTraderByUserId(Long userId);

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
     * 更新交易员启停状态。
     *
     * @param id 交易员主键
     * @param status 状态 0启用 1停用
     * @return 影响行数
     */
    int updateCopyTradeTraderStatus(Long id, Integer status);

    /**
     * 批量删除交易员配置。
     *
     * @param ids 主键数组
     * @return 影响行数
     */
    int deleteCopyTradeTraderByIds(Long[] ids);
}
