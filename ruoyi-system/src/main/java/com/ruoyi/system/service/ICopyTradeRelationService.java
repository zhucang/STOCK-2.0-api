package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.CopyTradeRelation;

import java.util.List;

/**
 * 跟单关系(跟单人员)服务接口。
 * 负责跟单关系(跟单人员)表的查询、维护以及业务同步过程中的关系读取。
 */
public interface ICopyTradeRelationService {
    /**
     * 查询跟单关系(跟单人员)列表。
     *
     * @param copyTradeRelation 跟单关系(跟单人员)筛选条件
     * @return 跟单关系(跟单人员)列表
     */
    List<CopyTradeRelation> selectCopyTradeRelationList(CopyTradeRelation copyTradeRelation);

    /**
     * 填充跟单关系(跟单人员)展示所需的扩展信息。
     *
     * @param copyTradeRelation 跟单关系(跟单人员)
     */
    void fillOtherInfo(CopyTradeRelation copyTradeRelation);

    /**
     * 根据主键查询跟单关系(跟单人员)。
     *
     * @param id 跟单关系(跟单人员)主键
     * @return 跟单关系(跟单人员)详情
     */
    CopyTradeRelation selectCopyTradeRelationById(Long id);

    /**
     * 根据交易员和跟单用户查询历史关系。
     * 该方法不限定状态和逻辑删除标志，用于恢复旧关系。
     *
     * @param traderUserId 交易员用户ID
     * @param followerUserId 跟单用户ID
     * @return 跟单关系(跟单人员)
     */
    CopyTradeRelation selectRelationByTraderAndFollower(Long traderUserId, Long followerUserId);

    /**
     * 创建或恢复用户对交易员的跟单关系(跟单人员)。
     *
     * @param copyTradeRelation 跟单关系(跟单人员)参数
     * @return 处理结果
     */
    AjaxResult followTrader(CopyTradeRelation copyTradeRelation);

    /**
     * 停止一条跟单关系(跟单人员)。
     *
     * @param relationId 跟单关系(跟单人员)主键
     * @param followerUserId 跟单用户ID，传空时不校验归属
     * @return 处理结果
     */
    AjaxResult unfollowTrader(Long relationId, Long followerUserId);

    /**
     * 修改跟单关系(跟单人员)执行配置。
     * 仅允许修改 followMode、followAmount、followRatio、maxOpenOrders。
     * status 由跟随/停止跟随接口维护，traderId、traderUserId、followerUserId 创建后禁止修改。
     *
     * @param copyTradeRelation 跟单关系(跟单人员)参数
     * @param followerUserId 跟单用户ID，传空时不校验归属
     * @return 影响行数
     */
    int updateCopyTradeRelationConfig(CopyTradeRelation copyTradeRelation, Long followerUserId);

    /**
     * 查询某个交易员名下所有启用的跟单关系(跟单人员)。
     *
     * @param traderUserId 交易员用户ID
     * @return 跟单关系(跟单人员)列表
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
     * 新增跟单关系(跟单人员)。
     *
     * @param copyTradeRelation 跟单关系(跟单人员)信息
     * @return 影响行数
     */
    int insertCopyTradeRelation(CopyTradeRelation copyTradeRelation);

    /**
     * 修改跟单关系(跟单人员)。
     *
     * @param copyTradeRelation 跟单关系(跟单人员)信息
     * @return 影响行数
     */
    int updateCopyTradeRelation(CopyTradeRelation copyTradeRelation);

    /**
     * 批量删除跟单关系(跟单人员)。
     *
     * @param ids 主键数组
     * @return 影响行数
     */
    int deleteCopyTradeRelationByIds(Long[] ids);
}
