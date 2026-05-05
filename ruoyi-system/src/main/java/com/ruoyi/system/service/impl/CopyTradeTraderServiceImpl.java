package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.CopyTradeTrader;
import com.ruoyi.system.mapper.CopyTradeTraderMapper;
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

    /** 查询交易员列表。 */
    @Override
    public List<CopyTradeTrader> selectCopyTradeTraderList(CopyTradeTrader copyTradeTrader) {
        return copyTradeTraderMapper.selectCopyTradeTraderList(copyTradeTrader);
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

    /** 批量删除交易员配置。 */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteCopyTradeTraderByIds(Long[] ids) {
        return copyTradeTraderMapper.deleteCopyTradeTraderByIds(ids);
    }
}
