package com.ruoyi.system.service;

import com.ruoyi.system.domain.UserApiKey;

import java.util.List;

/**
 * 应用秘钥apiKeyService接口
 * 
 * @author ruoyi
 * @date 2024-12-10
 */
public interface IUserApiKeyService 
{
    /**
     * 查询应用秘钥apiKey
     * 
     * @param id 应用秘钥apiKey主键
     * @return 应用秘钥apiKey
     */
    public UserApiKey selectUserApiKeyById(Long id);

    /**
     * 查询应用秘钥apiKey列表
     * 
     * @param userApiKey 应用秘钥apiKey
     * @return 应用秘钥apiKey集合
     */
    public List<UserApiKey> selectUserApiKeyList(UserApiKey userApiKey);

    /**
     * 新增应用秘钥apiKey
     * 
     * @param userApiKey 应用秘钥apiKey
     * @return 结果
     */
    public int insertUserApiKey(UserApiKey userApiKey);

    /**
     * 修改应用秘钥apiKey
     * 
     * @param userApiKey 应用秘钥apiKey
     * @return 结果
     */
    public int updateUserApiKey(UserApiKey userApiKey);

    /**
     * 批量删除应用秘钥apiKey
     * 
     * @param ids 需要删除的应用秘钥apiKey主键集合
     * @return 结果
     */
    public int deleteUserApiKeyByIds(Long[] ids);

    /**
     * 删除应用秘钥apiKey信息
     * 
     * @param id 应用秘钥apiKey主键
     * @return 结果
     */
    public int deleteUserApiKeyById(Long id);
}
