package com.ruoyi.system.service.impl;

import com.ruoyi.common.constant.CacheableKey;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.ActivityCenter;
import com.ruoyi.system.domain.LangMgr;
import com.ruoyi.system.mapper.ActivityCenterMapper;
import com.ruoyi.system.service.IActivityCenterService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 活动中心配置Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-07-02
 */
@Service
public class ActivityCenterServiceImpl implements IActivityCenterService 
{
    @Resource
    private ActivityCenterMapper activityCenterMapper;

    /**
     * 查询活动中心配置
     * 
     * @param id 活动中心配置主键
     * @return 活动中心配置
     */
    @Override
    public ActivityCenter selectActivityCenterById(Long id)
    {
        return activityCenterMapper.selectActivityCenterById(id);
    }

    /**
     * 查询活动中心配置列表
     * 
     * @param activityCenter 活动中心配置
     * @return 活动中心配置
     */
    @Override
    @Cacheable(value = CacheableKey.ACTIVITY_CENTER + CacheableKey.LIST,key = "#activityCenter.cacheableKey()")
    public List<ActivityCenter> selectActivityCenterList(ActivityCenter activityCenter)
    {
        return activityCenterMapper.selectActivityCenterList(activityCenter);
    }

    /**
     * 新增活动中心配置
     * 
     * @param activityCenter 活动中心配置
     * @return 结果
     */
    @Override
    @CacheEvict(value = CacheableKey.ACTIVITY_CENTER + CacheableKey.LIST,allEntries = true)
    public int insertActivityCenter(ActivityCenter activityCenter)
    {
        activityCenter.setCreateTime(DateUtils.getNowDate());
        return activityCenterMapper.insertActivityCenter(activityCenter);
    }

    /**
     * 修改活动中心配置
     * 
     * @param activityCenter 活动中心配置
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.ACTIVITY_CENTER + CacheableKey.ENTITY,key = "#activityCenter.id"),
            @CacheEvict(value = CacheableKey.ACTIVITY_CENTER + CacheableKey.LIST,allEntries = true)})
    public int updateActivityCenter(ActivityCenter activityCenter)
    {
        return activityCenterMapper.updateActivityCenter(activityCenter);
    }

    /**
     * 修改活动图片多语言配置
     * @param activityCenterId activityCenterId
     * @param bannerImgLang 活动图片语言包
     * @return
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.ACTIVITY_CENTER + CacheableKey.ENTITY,key = "#activityCenterId"),
            @CacheEvict(value = CacheableKey.ACTIVITY_CENTER + CacheableKey.LIST,allEntries = true)})
    public int updateActivityImgLang(Long activityCenterId, LangMgr bannerImgLang){
        ActivityCenter activityCenter = new ActivityCenter();
        activityCenter.setId(activityCenterId);
        activityCenter.setActivityImgLang(bannerImgLang);
        return activityCenterMapper.updateActivityCenter(activityCenter);
    }

    /**
     * 修改活动标题多语言
     * @param activityCenterId activityCenterId
     * @param bannerTitleLang 活动标题语言包
     * @return
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.ACTIVITY_CENTER + CacheableKey.ENTITY,key = "#activityCenterId"),
            @CacheEvict(value = CacheableKey.ACTIVITY_CENTER + CacheableKey.LIST,allEntries = true)})
    public int updateActivityTitleLang(Long activityCenterId, LangMgr bannerTitleLang){
        ActivityCenter activityCenter = new ActivityCenter();
        activityCenter.setId(activityCenterId);
        activityCenter.setActivityTitleLang(bannerTitleLang);
        return activityCenterMapper.updateActivityCenter(activityCenter);
    }

    /**
     * 修改活动内容多语言
     * @param activityCenterId activityCenterId
     * @param bannerContentLang 活动内容语言包
     * @return
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.ACTIVITY_CENTER + CacheableKey.ENTITY,key = "#activityCenterId"),
            @CacheEvict(value = CacheableKey.ACTIVITY_CENTER + CacheableKey.LIST,allEntries = true)})
    public int updateActivityContentLang(Long activityCenterId, LangMgr bannerContentLang){
        ActivityCenter activityCenter = new ActivityCenter();
        activityCenter.setId(activityCenterId);
        activityCenter.setActivityContentLang(bannerContentLang);
        return activityCenterMapper.updateActivityCenter(activityCenter);
    }

    /**
     * 批量删除活动中心配置
     * 
     * @param ids 需要删除的活动中心配置主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.ACTIVITY_CENTER + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.ACTIVITY_CENTER + CacheableKey.LIST,allEntries = true)})
    public int deleteActivityCenterByIds(Long[] ids)
    {
        return activityCenterMapper.deleteActivityCenterByIds(ids);
    }

    /**
     * 删除活动中心配置信息
     * 
     * @param id 活动中心配置主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.ACTIVITY_CENTER + CacheableKey.ENTITY,key = "#id"),
            @CacheEvict(value = CacheableKey.ACTIVITY_CENTER + CacheableKey.LIST,allEntries = true)})
    public int deleteActivityCenterById(Long id)
    {
        return activityCenterMapper.deleteActivityCenterById(id);
    }
}
