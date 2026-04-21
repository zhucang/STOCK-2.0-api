package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.ClientVersion;

import java.util.List;

/**
 * 客户端版本管理Mapper接口
 * 
 * @author ruoyi
 * @date 2024-01-08
 */
public interface ClientVersionMapper 
{
    /**
     * 查询客户端版本管理
     * 
     * @param id 客户端版本管理主键
     * @return 客户端版本管理
     */
    public ClientVersion selectClientVersionById(Long id);

    /**
     * 查询客户端版本管理列表
     * 
     * @param clientVersion 客户端版本管理
     * @return 客户端版本管理集合
     */
    public List<ClientVersion> selectClientVersionList(ClientVersion clientVersion);

    /**
     * 新增客户端版本管理
     * 
     * @param clientVersion 客户端版本管理
     * @return 结果
     */
    public int insertClientVersion(ClientVersion clientVersion);

    /**
     * 修改客户端版本管理
     * 
     * @param clientVersion 客户端版本管理
     * @return 结果
     */
    public int updateClientVersion(ClientVersion clientVersion);

    /**
     * 删除客户端版本管理
     * 
     * @param id 客户端版本管理主键
     * @return 结果
     */
    public int deleteClientVersionById(Long id);

    /**
     * 批量删除客户端版本管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteClientVersionByIds(Long[] ids);
}
