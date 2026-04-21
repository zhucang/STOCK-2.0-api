package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.UserApiKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 应用秘钥apiKeyMapper接口
 * 
 * @author ruoyi
 * @date 2024-12-10
 */
public interface UserApiKeyMapper 
{
    /**
     * 查询应用秘钥apiKey
     * 
     * @param id 应用秘钥apiKey主键
     * @return 应用秘钥apiKey
     */
    public UserApiKey selectUserApiKeyById(Long id);

    /**
     * 根据apiKey获取用户ID
     * @param appId
     * @param apiKey
     * @return
     */
    public Long selectUserIdByApiKey(@Param("appId") String appId,@Param("apiKey") String apiKey);

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
     * 删除应用秘钥apiKey
     * 
     * @param id 应用秘钥apiKey主键
     * @return 结果
     */
    public int deleteUserApiKeyById(Long id);

    /**
     * 批量删除应用秘钥apiKey
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserApiKeyByIds(Long[] ids);
}
