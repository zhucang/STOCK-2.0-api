package com.ruoyi.system.service;

import com.ruoyi.system.domain.ActivityCenter;
import com.ruoyi.system.domain.LangMgr;

import java.util.List;

/**
 * 活动中心配置Service接口
 * 
 * @author ruoyi
 * @date 2025-07-02
 */
public interface IActivityCenterService 
{
    /**
     * 查询活动中心配置
     * 
     * @param id 活动中心配置主键
     * @return 活动中心配置
     */
    public ActivityCenter selectActivityCenterById(Long id);

    /**
     * 查询活动中心配置列表
     * 
     * @param activityCenter 活动中心配置
     * @return 活动中心配置集合
     */
    public List<ActivityCenter> selectActivityCenterList(ActivityCenter activityCenter);

    /**
     * 新增活动中心配置
     * 
     * @param activityCenter 活动中心配置
     * @return 结果
     */
    public int insertActivityCenter(ActivityCenter activityCenter);

    /**
     * 修改活动中心配置
     * 
     * @param activityCenter 活动中心配置
     * @return 结果
     */
    public int updateActivityCenter(ActivityCenter activityCenter);

    /**
     * 修改活动图片多语言配置
     * @param activityCenterId activityCenterId
     * @param activityImgLang 活动图片语言包
     * @return
     */
    public int updateActivityImgLang(Long activityCenterId, LangMgr activityImgLang);

    /**
     * 修改活动标题多语言
     * @param activityCenterId activityCenterId
     * @param activityTitleLang 活动标题语言包
     * @return
     */
    public int updateActivityTitleLang(Long activityCenterId, LangMgr activityTitleLang);

    /**
     * 修改活动内容多语言
     * @param activityCenterId activityCenterId
     * @param activityContentLang 活动内容语言包
     * @return
     */
    public int updateActivityContentLang(Long activityCenterId, LangMgr activityContentLang);

    /**
     * 批量删除活动中心配置
     * 
     * @param ids 需要删除的活动中心配置主键集合
     * @return 结果
     */
    public int deleteActivityCenterByIds(Long[] ids);

    /**
     * 删除活动中心配置信息
     * 
     * @param id 活动中心配置主键
     * @return 结果
     */
    public int deleteActivityCenterById(Long id);
}
