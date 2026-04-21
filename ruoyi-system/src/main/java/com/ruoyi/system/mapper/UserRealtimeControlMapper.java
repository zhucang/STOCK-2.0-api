package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.UserRealtimeControl;

/**
 * 产品分时图数据(用户控制数据)Mapper接口
 * 
 * @author ruoyi
 * @date 2023-12-30
 */
public interface UserRealtimeControlMapper 
{
    /**
     * 查询产品分时图数据(用户控制数据)
     * 
     * @param id 产品分时图数据(用户控制数据)主键
     * @return 产品分时图数据(用户控制数据)
     */
    public UserRealtimeControl selectUserRealtimeControlById(Long id);

    /**
     * 查询产品分时图数据(用户控制数据)列表
     * 
     * @param userRealtimeControl 产品分时图数据(用户控制数据)
     * @return 产品分时图数据(用户控制数据)集合
     */
    public List<UserRealtimeControl> selectUserRealtimeControlList(UserRealtimeControl userRealtimeControl);

    /**
     * 新增产品分时图数据(用户控制数据)
     * 
     * @param userRealtimeControl 产品分时图数据(用户控制数据)
     * @return 结果
     */
    public int insertUserRealtimeControl(UserRealtimeControl userRealtimeControl);

    /**
     * 修改产品分时图数据(用户控制数据)
     * 
     * @param userRealtimeControl 产品分时图数据(用户控制数据)
     * @return 结果
     */
    public int updateUserRealtimeControl(UserRealtimeControl userRealtimeControl);

    /**
     * 删除产品分时图数据(用户控制数据)
     * 
     * @param id 产品分时图数据(用户控制数据)主键
     * @return 结果
     */
    public int deleteUserRealtimeControlById(Long id);

    /**
     * 批量删除产品分时图数据(用户控制数据)
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserRealtimeControlByIds(Long[] ids);

    /**
     * 清空除了今天的所有分时图数据
     * @param productType 产品类型
     */
    int cleanProductRealTimeDataTaskWithoutToday(Integer productType);
}
