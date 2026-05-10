package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.CopyTradeRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 跟单关系(跟单人员) Mapper 接口。
 * 负责跟单关系(跟单人员)的查询、创建、恢复、停用等数据库操作。
 */
public interface CopyTradeRelationMapper {
    /**
     * 根据主键查询跟单关系(跟单人员)详情。
     *
     * @param id 跟单关系(跟单人员)主键
     * @return 跟单关系(跟单人员)详情
     */
    CopyTradeRelation selectCopyTradeRelationById(Long id);

    /**
     * 根据交易员用户ID和跟随者用户ID查询历史关系。
     * 该方法不限定状态和逻辑删除标志，用于恢复旧关系。
     *
     * @param traderUserId 交易员用户ID
     * @param followerUserId 跟随者用户ID
     * @return 跟单关系(跟单人员)
     */
    CopyTradeRelation selectRelationByTraderAndFollower(@Param("traderUserId") Long traderUserId, @Param("followerUserId") Long followerUserId);

    /**
     * 根据交易员用户ID和跟随者用户ID查询启用中的关系。
     *
     * @param traderUserId 交易员用户ID
     * @param followerUserId 跟随者用户ID
     * @return 启用中的跟单关系(跟单人员)
     */
    CopyTradeRelation selectActiveRelationByTraderAndFollower(@Param("traderUserId") Long traderUserId, @Param("followerUserId") Long followerUserId);

    /**
     * 查询跟单关系(跟单人员)列表。
     *
     * @param copyTradeRelation 查询条件
     * @return 跟单关系(跟单人员)列表
     */
    List<CopyTradeRelation> selectCopyTradeRelationList(CopyTradeRelation copyTradeRelation);

    /**
     * 查询某个交易员名下所有启用中的跟单关系(跟单人员)。
     *
     * @param traderUserId 交易员用户ID
     * @return 跟单关系(跟单人员)列表
     */
    List<CopyTradeRelation> selectActiveRelationsByTraderUserId(Long traderUserId);

    /**
     * 统计某个交易员当前启用中的跟随人数。
     *
     * @param traderUserId 交易员用户ID
     * @return 跟随人数
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
