package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.CopyTradeTrader;

import java.util.List;

/**
 * 跟单交易员 Mapper 接口。
 * 负责交易员配置的增删改查，以及聚合展示查询。
 */
public interface CopyTradeTraderMapper {
    /**
     * 根据主键查询交易员详情。
     *
     * @param id 交易员主键
     * @return 交易员详情
     */
    CopyTradeTrader selectCopyTradeTraderById(Long id);

    /**
     * 根据用户ID查询启用中的交易员配置。
     *
     * @param userId 交易员用户ID
     * @return 启用中的交易员配置
     */
    CopyTradeTrader selectActiveCopyTradeTraderByUserId(Long userId);

    /**
     * 查询交易员列表。
     *
     * @param copyTradeTrader 查询条件
     * @return 交易员列表
     */
    List<CopyTradeTrader> selectCopyTradeTraderList(CopyTradeTrader copyTradeTrader);

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
}
