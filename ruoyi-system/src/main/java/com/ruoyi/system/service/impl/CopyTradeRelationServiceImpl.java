package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.exception.LangException;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.CopyTradeRelation;
import com.ruoyi.system.domain.CopyTradeTrader;
import com.ruoyi.system.mapper.CopyTradeRelationMapper;
import com.ruoyi.system.service.ICopyTradeRelationService;
import com.ruoyi.system.service.ICopyTradeTraderService;
import com.ruoyi.system.utils.UserApiKeyUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 跟单关系服务实现类。
 * 负责跟单关系表的读写，以及跟随和取消跟随的业务编排。
 */
@Service
public class CopyTradeRelationServiceImpl implements ICopyTradeRelationService {
    /** 跟单关系数据访问层。 */
    @Resource
    private CopyTradeRelationMapper copyTradeRelationMapper;

    /** 交易员服务。 */
    @Resource
    private ICopyTradeTraderService copyTradeTraderService;

    /** 查询跟单关系列表。 */
    @Override
    public List<CopyTradeRelation> selectCopyTradeRelationList(CopyTradeRelation copyTradeRelation) {
        return copyTradeRelationMapper.selectCopyTradeRelationList(copyTradeRelation);
    }

    /** 查询单条跟单关系。 */
    @Override
    public CopyTradeRelation selectCopyTradeRelationById(Long id) {
        return copyTradeRelationMapper.selectCopyTradeRelationById(id);
    }

    /** 根据交易员和跟单人查询关系。 */
    @Override
    public CopyTradeRelation selectRelationByTraderAndFollower(Long traderUserId, Long followerUserId) {
        return copyTradeRelationMapper.selectRelationByTraderAndFollower(traderUserId, followerUserId);
    }

    /** 创建或恢复跟单关系。 */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult followTrader(CopyTradeRelation copyTradeRelation) {
        // 当前登录用户就是跟单人。
        Long followerUserId = UserApiKeyUtils.getUserId();
        // 必须明确指定要跟随哪个交易员。
        if (copyTradeRelation.getTraderId() == null) {
            throw new LangException("hint_copyTradeSelectTrader", "请选择要跟随的交易员");
        }
        // 加载交易员配置并校验其可用状态。
        CopyTradeTrader trader = copyTradeTraderService.selectCopyTradeTraderById(copyTradeRelation.getTraderId());
        if (trader == null || trader.getStatus() == null || !trader.getStatus().equals(0)) {
            throw new LangException("hint_copyTradeTraderNotAvailable", "交易员不存在或已停用");
        }
        // 禁止用户自己跟随自己，避免出现自复制逻辑。
        if (trader.getUserId().equals(followerUserId)) {
            throw new LangException("hint_copyTradeCanNotFollowSelf", "不能跟随自己");
        }
        // 先查询是否存在历史关系，支持“停止后恢复”。
        CopyTradeRelation exists = selectRelationByTraderAndFollower(trader.getUserId(), followerUserId);
        if (exists != null && exists.getStatus() != null && exists.getStatus().equals(0)) {
            throw new LangException("hint_copyTradeAlreadyFollowing", "您已经在跟随该交易员");
        }
        // 如果交易员设置了最大人数，需要先校验名额。
        if (trader.getMaxFollowerCount() != null && trader.getMaxFollowerCount() > 0) {
            int currentFollowerCount = countActiveFollowerByTraderUserId(trader.getUserId());
            if (currentFollowerCount >= trader.getMaxFollowerCount()) {
                throw new LangException("hint_copyTradeFollowerLimitReached", "该交易员跟单名额已满");
            }
        }
        // 没有传模式时，自动使用交易员默认模式。
        Integer followMode = copyTradeRelation.getFollowMode();
        if (followMode == null) {
            followMode = trader.getDefaultFollowMode() == null ? 1 : trader.getDefaultFollowMode();
            copyTradeRelation.setFollowMode(followMode);
        }
        // 目前只支持固定金额和按比例两种模式。
        if (!followMode.equals(0) && !followMode.equals(1)) {
            throw new LangException("hint_copyTradeModeError", "跟单模式错误");
        }
        if (followMode.equals(0)) {
            // 固定金额模式必须传入有效金额。
            if (copyTradeRelation.getFollowAmount() == null || copyTradeRelation.getFollowAmount().compareTo(BigDecimal.ZERO) <= 0) {
                throw new LangException("hint_copyTradeInputFollowAmount", "请输入固定跟单金额");
            }
            // 固定金额不能小于交易员要求的门槛。
            if (trader.getMinFollowAmount() != null && copyTradeRelation.getFollowAmount().compareTo(trader.getMinFollowAmount()) < 0) {
                throw new LangException("hint_copyTradeAmountLtMin", "跟单金额不能低于交易员要求的最小金额");
            }
            // 固定金额也不能高于交易员要求的上限。
            if (trader.getMaxFollowAmount() != null && trader.getMaxFollowAmount().compareTo(BigDecimal.ZERO) > 0
                    && copyTradeRelation.getFollowAmount().compareTo(trader.getMaxFollowAmount()) > 0) {
                throw new LangException("hint_copyTradeAmountGtMax", "跟单金额不能高于交易员要求的最大金额");
            }
        } else {
            // 比例模式如果没传比例，则回退到交易员默认比例。
            if (copyTradeRelation.getFollowRatio() == null || copyTradeRelation.getFollowRatio().compareTo(BigDecimal.ZERO) <= 0) {
                copyTradeRelation.setFollowRatio(trader.getDefaultFollowRatio() == null ? BigDecimal.ONE : trader.getDefaultFollowRatio());
            }
        }
        // 不传最大持仓数时，给一个保守默认值。
        if (copyTradeRelation.getMaxOpenOrders() == null || copyTradeRelation.getMaxOpenOrders() <= 0) {
            copyTradeRelation.setMaxOpenOrders(10);
        }
        // 系统内部字段由后端统一写入，避免前端伪造。
        copyTradeRelation.setTraderUserId(trader.getUserId());
        copyTradeRelation.setFollowerUserId(followerUserId);
        copyTradeRelation.setStatus(0);
        copyTradeRelation.setLastFollowTime(new Date());
        copyTradeRelation.setUpdateTime(new Date());
        int count;
        if (exists != null) {
            // 有历史关系时直接复用原记录，避免唯一索引冲突。
            copyTradeRelation.setId(exists.getId());
            count = updateCopyTradeRelation(copyTradeRelation);
        } else {
            // 首次创建时补齐创建时间。
            copyTradeRelation.setCreateTime(new Date());
            count = insertCopyTradeRelation(copyTradeRelation);
        }
        if (count <= 0) {
            throw new ServiceException("创建跟单关系失败");
        }
        return AjaxResult.success().put("relationId", copyTradeRelation.getId() == null ? exists.getId() : copyTradeRelation.getId());
    }

    /** 停止跟单。 */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult unfollowTrader(Long relationId) {
        // 停止跟单必须先拿到关系主键。
        if (relationId == null) {
            throw new LangException("hint_copyTradeSelectRelation", "请选择要停止的跟单关系");
        }
        Long followerUserId = UserApiKeyUtils.getUserId();
        // 校验关系是否存在。
        CopyTradeRelation relation = selectCopyTradeRelationById(relationId);
        if (relation == null) {
            throw new LangException("hint_copyTradeRelationNotExists", "跟单关系不存在");
        }
        // 只能操作自己的跟单关系。
        if (!relation.getFollowerUserId().equals(followerUserId)) {
            throw new ServiceException("无权操作该跟单关系");
        }
        // 这里只是停止后续跟单，不主动平掉历史已开的仓位。
        CopyTradeRelation update = new CopyTradeRelation();
        update.setId(relationId);
        update.setStatus(1);
        update.setUpdateTime(new Date());
        int count = updateCopyTradeRelation(update);
        if (count <= 0) {
            throw new ServiceException("停止跟单失败");
        }
        return AjaxResult.success();
    }

    /** 查询某个交易员名下所有启用中的跟单关系。 */
    @Override
    public List<CopyTradeRelation> selectActiveRelationsByTraderUserId(Long traderUserId) {
        return copyTradeRelationMapper.selectActiveRelationsByTraderUserId(traderUserId);
    }

    /** 统计某个交易员当前启用中的跟单人数。 */
    @Override
    public int countActiveFollowerByTraderUserId(Long traderUserId) {
        return copyTradeRelationMapper.countActiveFollowerByTraderUserId(traderUserId);
    }

    /** 新增跟单关系。 */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertCopyTradeRelation(CopyTradeRelation copyTradeRelation) {
        return copyTradeRelationMapper.insertCopyTradeRelation(copyTradeRelation);
    }

    /** 修改跟单关系。 */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateCopyTradeRelation(CopyTradeRelation copyTradeRelation) {
        return copyTradeRelationMapper.updateCopyTradeRelation(copyTradeRelation);
    }

    /** 批量删除跟单关系。 */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteCopyTradeRelationByIds(Long[] ids) {
        return copyTradeRelationMapper.deleteCopyTradeRelationByIds(ids);
    }
}
