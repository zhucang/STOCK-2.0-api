package com.ruoyi.system.service.impl;

import com.ruoyi.common.constant.CacheableKey;
import com.ruoyi.system.domain.TrumpetInfo;
import com.ruoyi.system.mapper.TrumpetInfoMapper;
import com.ruoyi.system.service.ITrumpetInfoService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 喇叭信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-11-04
 */
@Service
public class TrumpetInfoServiceImpl implements ITrumpetInfoService 
{
    @Resource
    private TrumpetInfoMapper trumpetInfoMapper;

    /**
     * 查询喇叭信息
     * 
     * @param id 喇叭信息主键
     * @return 喇叭信息
     */
    @Override
//    @Cacheable(value = CacheableKey.TRUMPET_INFO + CacheableKey.ENTITY,key = "#id")
    public TrumpetInfo selectTrumpetInfoById(Long id)
    {
        return trumpetInfoMapper.selectTrumpetInfoById(id);
    }

    /**
     * 查询喇叭信息列表
     * 
     * @param trumpetInfo 喇叭信息
     * @return 喇叭信息
     */
    @Override
    @Cacheable(value = CacheableKey.TRUMPET_INFO + CacheableKey.LIST,key = "#trumpetInfo.cacheableKey()")
    public List<TrumpetInfo> selectTrumpetInfoList(TrumpetInfo trumpetInfo)
    {
        return trumpetInfoMapper.selectTrumpetInfoList(trumpetInfo);
    }

    /**
     * 新增喇叭信息
     * 
     * @param trumpetInfo 喇叭信息
     * @return 结果
     */
    @Override
    @CacheEvict(value = CacheableKey.TRUMPET_INFO + CacheableKey.LIST,allEntries = true)
    public int insertTrumpetInfo(TrumpetInfo trumpetInfo)
    {
        trumpetInfo.setCreateTime(new Date());
        return trumpetInfoMapper.insertTrumpetInfo(trumpetInfo);
    }

    /**
     * 修改喇叭信息
     * 
     * @param trumpetInfo 喇叭信息
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.TRUMPET_INFO + CacheableKey.ENTITY,key = "#trumpetInfo.id"),
            @CacheEvict(value = CacheableKey.TRUMPET_INFO + CacheableKey.LIST,allEntries = true)})
    public int updateTrumpetInfo(TrumpetInfo trumpetInfo)
    {
        return trumpetInfoMapper.updateTrumpetInfo(trumpetInfo);
    }

    /**
     * 批量删除喇叭信息
     * 
     * @param ids 需要删除的喇叭信息主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.TRUMPET_INFO + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.TRUMPET_INFO + CacheableKey.LIST,allEntries = true)})
    public int deleteTrumpetInfoByIds(Long[] ids)
    {
        return trumpetInfoMapper.deleteTrumpetInfoByIds(ids);
    }

    /**
     * 删除喇叭信息信息
     * 
     * @param id 喇叭信息主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.TRUMPET_INFO + CacheableKey.ENTITY,key = "#id"),
            @CacheEvict(value = CacheableKey.TRUMPET_INFO + CacheableKey.LIST,allEntries = true)})
    public int deleteTrumpetInfoById(Long id)
    {
        return trumpetInfoMapper.deleteTrumpetInfoById(id);
    }
}
