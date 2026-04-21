package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.constant.CacheableKey;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.domain.HelpCenter;
import com.ruoyi.system.mapper.HelpCenterMapper;
import com.ruoyi.system.service.IHelpCenterService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * 帮助中心Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-11-22
 */
@Service
public class HelpCenterServiceImpl implements IHelpCenterService 
{
    @Resource
    private HelpCenterMapper helpCenterMapper;

    /**
     * 查询帮助中心
     * 
     * @param id 帮助中心主键
     * @return 帮助中心
     */
    @Override
//    @Cacheable(value = CacheableKey.HELP_CENTER + CacheableKey.ENTITY,key = "#id")
    public HelpCenter selectHelpCenterById(Long id)
    {
        return helpCenterMapper.selectHelpCenterById(id);
    }

    /**
     * 查询帮助中心列表
     * 
     * @param helpCenter 帮助中心
     * @return 帮助中心
     */
    @Override
    @Cacheable(value = CacheableKey.HELP_CENTER + CacheableKey.LIST,key = "#helpCenter.cacheableKey()")
    public List<HelpCenter> selectHelpCenterList(HelpCenter helpCenter)
    {
        return helpCenterMapper.selectHelpCenterList(helpCenter);
    }

    /**
     * 新增帮助中心
     * 
     * @param helpCenter 帮助中心
     * @return 结果
     */
    @Override
    @CacheEvict(value = CacheableKey.HELP_CENTER + CacheableKey.LIST,allEntries = true)
    public int insertHelpCenter(HelpCenter helpCenter)
    {
        return helpCenterMapper.insertHelpCenter(helpCenter);
    }

    /**
     * 修改帮助中心
     * 
     * @param helpCenter 帮助中心
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.HELP_CENTER + CacheableKey.ENTITY,key = "#helpCenter.id"),
            @CacheEvict(value = CacheableKey.HELP_CENTER + CacheableKey.LIST,allEntries = true)})
    public int updateHelpCenter(HelpCenter helpCenter)
    {
        return helpCenterMapper.updateHelpCenter(helpCenter);
    }

    /**
     * 批量删除帮助中心
     * 
     * @param ids 需要删除的帮助中心主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.HELP_CENTER + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.HELP_CENTER + CacheableKey.LIST,allEntries = true)})
    public int deleteHelpCenterByIds(Long[] ids)
    {
        HelpCenter search = new HelpCenter();
        search.getParams().put("ids", Arrays.asList(ids));
        List<HelpCenter> helpCenters = helpCenterMapper.selectHelpCenterList(search);
        //日志记录帮助中心信息
        HttpUtils.getRequestLogParams().put("JSONArray:helpCenters", JSONObject.toJSONString(helpCenters));
        return helpCenterMapper.deleteHelpCenterByIds(ids);
    }

    /**
     * 删除帮助中心信息
     * 
     * @param id 帮助中心主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.HELP_CENTER + CacheableKey.ENTITY,key = "#id"),
            @CacheEvict(value = CacheableKey.HELP_CENTER + CacheableKey.LIST,allEntries = true)})
    public int deleteHelpCenterById(Long id)
    {
        return helpCenterMapper.deleteHelpCenterById(id);
    }
}
