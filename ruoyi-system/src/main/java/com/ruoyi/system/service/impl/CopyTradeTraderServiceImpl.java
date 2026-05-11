package com.ruoyi.system.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.CopyTradeRelation;
import com.ruoyi.system.domain.CopyTradeTrader;
import com.ruoyi.system.mapper.CopyTradeTraderMapper;
import com.ruoyi.system.service.ICopyTradeRelationService;
import com.ruoyi.system.service.ICopyTradeTraderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 跟单交易员服务实现类。
 * 负责交易员配置表的单表读写逻辑。
 */
@Service
public class CopyTradeTraderServiceImpl implements ICopyTradeTraderService {
    /** 交易员数据访问层。 */
    @Resource
    private CopyTradeTraderMapper copyTradeTraderMapper;
    @Resource
    private ICopyTradeRelationService copyTradeRelationService;

    /** 查询交易员列表。 */
    @Override
    public List<CopyTradeTrader> selectCopyTradeTraderList(CopyTradeTrader copyTradeTrader) {
        return copyTradeTraderMapper.selectCopyTradeTraderList(copyTradeTrader);
    }

    @Override
    public void fillFollowStatus(CopyTradeTrader trader, Long userId) {
        if (trader == null) {
            return;
        }
        trader.setIsFollow(0);
        if (trader.getUserId() == null || userId == null) {
            return;
        }
        CopyTradeRelation relation = copyTradeRelationService.selectRelationByTraderAndFollower(trader.getUserId(), userId);
        if (relation != null
                && relation.getStatus() != null && relation.getStatus().equals(0)
                && relation.getDelFlag() != null && relation.getDelFlag().equals(0)) {
            trader.setIsFollow(1);
        }
    }

    /** 查询单个交易员。 */
    @Override
    public CopyTradeTrader selectCopyTradeTraderById(Long id) {
        return copyTradeTraderMapper.selectCopyTradeTraderById(id);
    }

    /** 查询启用状态的交易员。 */
    @Override
    public CopyTradeTrader selectActiveCopyTradeTraderByUserId(Long userId) {
        return copyTradeTraderMapper.selectActiveCopyTradeTraderByUserId(userId);
    }

    /** 新增交易员配置。 */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertCopyTradeTrader(CopyTradeTrader copyTradeTrader) {
        // 新增时统一补齐创建和更新时间。
        copyTradeTrader.setCreateTime(new Date());
        copyTradeTrader.setUpdateTime(new Date());
        return copyTradeTraderMapper.insertCopyTradeTrader(copyTradeTrader);
    }

    /** 修改交易员配置。 */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateCopyTradeTrader(CopyTradeTrader copyTradeTrader) {
        // 修改时刷新更新时间，便于审计。
        copyTradeTrader.setUpdateTime(new Date());
        return copyTradeTraderMapper.updateCopyTradeTrader(copyTradeTrader);
    }

    /** 更新交易员启停状态。 */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateCopyTradeTraderStatus(Long id, Integer status) {
        if (id == null) {
            throw new ServiceException("请选择需要操作的交易员");
        }
        CopyTradeTrader copyTradeTrader = new CopyTradeTrader();
        copyTradeTrader.setId(id);
        copyTradeTrader.setStatus(status);
        copyTradeTrader.setUpdateTime(new Date());
        return copyTradeTraderMapper.updateCopyTradeTrader(copyTradeTrader);
    }

    /** 批量删除交易员配置。 */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteCopyTradeTraderByIds(Long[] ids) {
        return copyTradeTraderMapper.deleteCopyTradeTraderByIds(ids);
    }
}
