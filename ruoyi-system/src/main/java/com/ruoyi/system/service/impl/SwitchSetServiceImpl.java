package com.ruoyi.system.service.impl;

import com.ruoyi.common.constant.CacheableKey;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.domain.SwitchSet;
import com.ruoyi.system.mapper.SwitchSetMapper;
import com.ruoyi.system.service.ISwitchSetService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 开关配置Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-10-28
 */
@Service
public class SwitchSetServiceImpl implements ISwitchSetService 
{
    @Resource
    private SwitchSetMapper switchSetMapper;

    /**
     * 查询开关配置
     * 
     * @param id 开关配置主键
     * @return 开关配置
     */
    @Override
    @Cacheable(value = CacheableKey.SWITCH_SET + CacheableKey.ENTITY,key = "'selectSwitchSetById'+#id")
    public SwitchSet selectSwitchSetById(Long id)
    {
        return switchSetMapper.selectSwitchSetById(id);
    }

    /**
     * 根据id获取开关状态
     *
     * @param id 开关配置主键
     * @return 开关配置
     */
    @Override
    @Cacheable(value = CacheableKey.SWITCH_SET + CacheableKey.ENTITY,key = "'selectSwitchStatusById'+#id")
    public Integer selectSwitchStatusById(Long id){
        return switchSetMapper.selectSwitchStatusById(id);
    }

    /**
     * 查询开关配置列表
     * 
     * @param switchSet 开关配置
     * @return 开关配置
     */
    @Override
    @Cacheable(value = CacheableKey.SWITCH_SET + CacheableKey.LIST,key = "#switchSet.cacheableKey()")
    public List<SwitchSet> selectSwitchSetList(SwitchSet switchSet)
    {
        return switchSetMapper.selectSwitchSetList(switchSet);
    }

//    /**
//     * 新增开关配置
//     *
//     * @param switchSet 开关配置
//     * @return 结果
//     */
//    @Override
//    public int insertSwitchSet(SwitchSet switchSet)
//    {
//        return switchSetMapper.insertSwitchSet(switchSet);
//    }

    /**
     * 修改开关配置
     * 
     * @param switchSet 开关配置
     * @return 结果
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = CacheableKey.SWITCH_SET + CacheableKey.ENTITY,key = "'selectSwitchSetById'+#switchSet.id"),
            @CacheEvict(value = CacheableKey.SWITCH_SET + CacheableKey.ENTITY,key = "'selectSwitchStatusById'+#switchSet.id"),
            @CacheEvict(value = CacheableKey.SWITCH_SET + CacheableKey.LIST,allEntries = true)})
    public int updateSwitchSet(SwitchSet switchSet)
    {
        return switchSetMapper.updateSwitchSet(switchSet);
    }

//    /**
//     * 批量删除开关配置
//     *
//     * @param ids 需要删除的开关配置主键
//     * @return 结果
//     */
//    @Override
//    public int deleteSwitchSetByIds(Long[] ids)
//    {
//        return switchSetMapper.deleteSwitchSetByIds(ids);
//    }
//
//    /**
//     * 删除开关配置信息
//     *
//     * @param id 开关配置主键
//     * @return 结果
//     */
//    @Override
//    public int deleteSwitchSetById(Long id)
//    {
//        return switchSetMapper.deleteSwitchSetById(id);
//    }
}
