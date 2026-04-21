package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.ActivityCenter;

import java.util.List;

/**
 * 活动中心配置Mapper接口
 * 
 * @author ruoyi
 * @date 2025-07-02
 */
public interface ActivityCenterMapper 
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
     * 删除活动中心配置
     * 
     * @param id 活动中心配置主键
     * @return 结果
     */
    public int deleteActivityCenterById(Long id);

    /**
     * 批量删除活动中心配置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteActivityCenterByIds(Long[] ids);
}
