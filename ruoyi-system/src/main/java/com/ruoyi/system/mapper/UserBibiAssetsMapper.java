package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.UserBibiAssets;

/**
 * 用户币币资产Mapper接口
 * 
 * @author ruoyi
 * @date 2024-06-24
 */
public interface UserBibiAssetsMapper 
{
    /**
     * 查询用户币币资产
     * 
     * @param id 用户币币资产主键
     * @return 用户币币资产
     */
    public UserBibiAssets selectUserBibiAssetsById(Long id);

    /**
     * 查询用户币币资产列表
     * 
     * @param userBibiAssets 用户币币资产
     * @return 用户币币资产集合
     */
    public List<UserBibiAssets> selectUserBibiAssetsList(UserBibiAssets userBibiAssets);

    /**
     * 新增用户币币资产
     * 
     * @param userBibiAssets 用户币币资产
     * @return 结果
     */
    public int insertUserBibiAssets(UserBibiAssets userBibiAssets);

    /**
     * 修改用户币币资产
     * 
     * @param userBibiAssets 用户币币资产
     * @return 结果
     */
    public int updateUserBibiAssets(UserBibiAssets userBibiAssets);

    /**
     * 删除用户币币资产
     * 
     * @param id 用户币币资产主键
     * @return 结果
     */
    public int deleteUserBibiAssetsById(Long id);

    /**
     * 批量删除用户币币资产
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserBibiAssetsByIds(Long[] ids);
}
