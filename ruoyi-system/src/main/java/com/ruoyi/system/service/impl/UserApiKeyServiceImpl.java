package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.UserApiKey;
import com.ruoyi.system.mapper.UserApiKeyMapper;
import com.ruoyi.system.service.IUserApiKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 应用秘钥apiKeyService业务层处理
 * 
 * @author ruoyi
 * @date 2024-12-10
 */
@Service
public class UserApiKeyServiceImpl implements IUserApiKeyService 
{
    @Resource
    private UserApiKeyMapper userApiKeyMapper;

    /**
     * 查询应用秘钥apiKey
     * 
     * @param id 应用秘钥apiKey主键
     * @return 应用秘钥apiKey
     */
    @Override
    public UserApiKey selectUserApiKeyById(Long id)
    {
        return userApiKeyMapper.selectUserApiKeyById(id);
    }

    /**
     * 查询应用秘钥apiKey列表
     * 
     * @param userApiKey 应用秘钥apiKey
     * @return 应用秘钥apiKey
     */
    @Override
    public List<UserApiKey> selectUserApiKeyList(UserApiKey userApiKey)
    {
        return userApiKeyMapper.selectUserApiKeyList(userApiKey);
    }

    /**
     * 新增应用秘钥apiKey
     * 
     * @param userApiKey 应用秘钥apiKey
     * @return 结果
     */
    @Override
    public int insertUserApiKey(UserApiKey userApiKey)
    {
        userApiKey.setCreateTime(DateUtils.getNowDate());
        return userApiKeyMapper.insertUserApiKey(userApiKey);
    }

    /**
     * 修改应用秘钥apiKey
     * 
     * @param userApiKey 应用秘钥apiKey
     * @return 结果
     */
    @Override
    public int updateUserApiKey(UserApiKey userApiKey)
    {
        return userApiKeyMapper.updateUserApiKey(userApiKey);
    }

    /**
     * 批量删除应用秘钥apiKey
     * 
     * @param ids 需要删除的应用秘钥apiKey主键
     * @return 结果
     */
    @Override
    public int deleteUserApiKeyByIds(Long[] ids)
    {
        return userApiKeyMapper.deleteUserApiKeyByIds(ids);
    }

    /**
     * 删除应用秘钥apiKey信息
     * 
     * @param id 应用秘钥apiKey主键
     * @return 结果
     */
    @Override
    public int deleteUserApiKeyById(Long id)
    {
        return userApiKeyMapper.deleteUserApiKeyById(id);
    }
}
