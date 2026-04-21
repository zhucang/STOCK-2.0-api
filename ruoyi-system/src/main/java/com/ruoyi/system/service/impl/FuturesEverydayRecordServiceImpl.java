package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.FuturesEverydayRecordMapper;
import com.ruoyi.system.domain.FuturesEverydayRecord;
import com.ruoyi.system.service.IFuturesEverydayRecordService;

/**
 * 期货日涨幅记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-11-02
 */
@Service
public class FuturesEverydayRecordServiceImpl implements IFuturesEverydayRecordService 
{
    @Autowired
    private FuturesEverydayRecordMapper futuresEverydayRecordMapper;

    /**
     * 查询期货日涨幅记录
     * 
     * @param id 期货日涨幅记录主键
     * @return 期货日涨幅记录
     */
    @Override
    public FuturesEverydayRecord selectFuturesEverydayRecordById(Long id)
    {
        return futuresEverydayRecordMapper.selectFuturesEverydayRecordById(id);
    }

    /**
     * 查询期货日涨幅记录列表
     * 
     * @param futuresEverydayRecord 期货日涨幅记录
     * @return 期货日涨幅记录
     */
    @Override
    public List<FuturesEverydayRecord> selectFuturesEverydayRecordList(FuturesEverydayRecord futuresEverydayRecord)
    {
        return futuresEverydayRecordMapper.selectFuturesEverydayRecordList(futuresEverydayRecord);
    }

    /**
     * 新增期货日涨幅记录
     * 
     * @param futuresEverydayRecord 期货日涨幅记录
     * @return 结果
     */
    @Override
    public int insertFuturesEverydayRecord(FuturesEverydayRecord futuresEverydayRecord)
    {
        futuresEverydayRecord.setCreateTime(DateUtils.getNowDate());
        return futuresEverydayRecordMapper.insertFuturesEverydayRecord(futuresEverydayRecord);
    }

    /**
     * 修改期货日涨幅记录
     * 
     * @param futuresEverydayRecord 期货日涨幅记录
     * @return 结果
     */
    @Override
    public int updateFuturesEverydayRecord(FuturesEverydayRecord futuresEverydayRecord)
    {
        return futuresEverydayRecordMapper.updateFuturesEverydayRecord(futuresEverydayRecord);
    }

    /**
     * 批量删除期货日涨幅记录
     * 
     * @param ids 需要删除的期货日涨幅记录主键
     * @return 结果
     */
    @Override
    public int deleteFuturesEverydayRecordByIds(Long[] ids)
    {
        return futuresEverydayRecordMapper.deleteFuturesEverydayRecordByIds(ids);
    }

    /**
     * 删除期货日涨幅记录信息
     * 
     * @param id 期货日涨幅记录主键
     * @return 结果
     */
    @Override
    public int deleteFuturesEverydayRecordById(Long id)
    {
        return futuresEverydayRecordMapper.deleteFuturesEverydayRecordById(id);
    }
}
