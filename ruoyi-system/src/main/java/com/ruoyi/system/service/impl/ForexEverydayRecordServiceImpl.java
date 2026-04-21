package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.ForexEverydayRecordMapper;
import com.ruoyi.system.domain.ForexEverydayRecord;
import com.ruoyi.system.service.IForexEverydayRecordService;

/**
 * 外汇日涨幅记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-11-02
 */
@Service
public class ForexEverydayRecordServiceImpl implements IForexEverydayRecordService 
{
    @Autowired
    private ForexEverydayRecordMapper forexEverydayRecordMapper;

    /**
     * 查询外汇日涨幅记录
     * 
     * @param id 外汇日涨幅记录主键
     * @return 外汇日涨幅记录
     */
    @Override
    public ForexEverydayRecord selectForexEverydayRecordById(Long id)
    {
        return forexEverydayRecordMapper.selectForexEverydayRecordById(id);
    }

    /**
     * 查询外汇日涨幅记录列表
     * 
     * @param forexEverydayRecord 外汇日涨幅记录
     * @return 外汇日涨幅记录
     */
    @Override
    public List<ForexEverydayRecord> selectForexEverydayRecordList(ForexEverydayRecord forexEverydayRecord)
    {
        return forexEverydayRecordMapper.selectForexEverydayRecordList(forexEverydayRecord);
    }

    /**
     * 新增外汇日涨幅记录
     * 
     * @param forexEverydayRecord 外汇日涨幅记录
     * @return 结果
     */
    @Override
    public int insertForexEverydayRecord(ForexEverydayRecord forexEverydayRecord)
    {
        forexEverydayRecord.setCreateTime(DateUtils.getNowDate());
        return forexEverydayRecordMapper.insertForexEverydayRecord(forexEverydayRecord);
    }

    /**
     * 修改外汇日涨幅记录
     * 
     * @param forexEverydayRecord 外汇日涨幅记录
     * @return 结果
     */
    @Override
    public int updateForexEverydayRecord(ForexEverydayRecord forexEverydayRecord)
    {
        return forexEverydayRecordMapper.updateForexEverydayRecord(forexEverydayRecord);
    }

    /**
     * 批量删除外汇日涨幅记录
     * 
     * @param ids 需要删除的外汇日涨幅记录主键
     * @return 结果
     */
    @Override
    public int deleteForexEverydayRecordByIds(Long[] ids)
    {
        return forexEverydayRecordMapper.deleteForexEverydayRecordByIds(ids);
    }

    /**
     * 删除外汇日涨幅记录信息
     * 
     * @param id 外汇日涨幅记录主键
     * @return 结果
     */
    @Override
    public int deleteForexEverydayRecordById(Long id)
    {
        return forexEverydayRecordMapper.deleteForexEverydayRecordById(id);
    }
}
