package com.ruoyi.system.service;

import com.ruoyi.system.domain.UserBibiAssets;

import java.util.List;

/**
 * 用户币币资产Service接口
 * 
 * @author ruoyi
 * @date 2024-06-24
 */
public interface IUserBibiAssetsService 
{
    /**
     * 查询用户币币资产
     * 
     * @param id 用户币币资产主键
     * @return 用户币币资产
     */
    public UserBibiAssets selectUserBibiAssetsById(Long id);

    /**
     * 查询用户币币资产
     * @param userId 用户id
     * @param productCode 产品代码
     * @param productType 产品类型
     * @return
     */
    public UserBibiAssets getUserBibiAssets(Long userId,String productCode,Integer productType);

    /**
     * 查询用户币币资产列表
     * 
     * @param userBibiAssets 用户币币资产
     * @return 用户币币资产集合
     */
    public List<UserBibiAssets> selectUserBibiAssetsList(UserBibiAssets userBibiAssets);

    /**
     * 填充其他信息
     * @param userBibiAssets 用户币币资产
     */
    public void fillOtherInfo(List<UserBibiAssets> userBibiAssets);

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
     * 批量删除用户币币资产
     * 
     * @param ids 需要删除的用户币币资产主键集合
     * @return 结果
     */
    public int deleteUserBibiAssetsByIds(Long[] ids);

    /**
     * 删除用户币币资产信息
     * 
     * @param id 用户币币资产主键
     * @return 结果
     */
    public int deleteUserBibiAssetsById(Long id);
}
