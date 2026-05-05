package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.CopyTradeRelation;

import java.util.List;

/**
 * 跟单关系服务接口。
 * 负责跟单关系表的查询、维护以及业务同步过程中的关系读取。
 */
public interface ICopyTradeRelationService {
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
     * 根据交易员和跟单用户查询关系。
     *
     * @param traderUserId 交易员用户ID
     * @param followerUserId 跟单用户ID
     * @return 跟单关系
     */
    CopyTradeRelation selectRelationByTraderAndFollower(Long traderUserId, Long followerUserId);

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
     * 查询某个交易员名下所有启用的跟单关系。
     *
     * @param traderUserId 交易员用户ID
     * @return 跟单关系列表
     */
    List<CopyTradeRelation> selectActiveRelationsByTraderUserId(Long traderUserId);

    /**
     * 统计某个交易员当前启用的跟单人数。
     *
     * @param traderUserId 交易员用户ID
     * @return 启用中的跟单人数
     */
    int countActiveFollowerByTraderUserId(Long traderUserId);

    /**
     * 新增跟单关系。
     *
     * @param copyTradeRelation 跟单关系信息
     * @return 影响行数
     */
    int insertCopyTradeRelation(CopyTradeRelation copyTradeRelation);

    /**
     * 修改跟单关系。
     *
     * @param copyTradeRelation 跟单关系信息
     * @return 影响行数
     */
    int updateCopyTradeRelation(CopyTradeRelation copyTradeRelation);

    /**
     * 批量删除跟单关系。
     *
     * @param ids 主键数组
     * @return 影响行数
     */
    int deleteCopyTradeRelationByIds(Long[] ids);
}
