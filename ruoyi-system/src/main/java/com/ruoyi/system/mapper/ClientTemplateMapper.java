package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.ClientTemplate;

import java.util.List;

/**
 * 客户端模板管理Mapper接口
 * 
 * @author ruoyi
 * @date 2024-01-08
 */
public interface ClientTemplateMapper 
{
    /**
     * 查询客户端模板管理
     * 
     * @param id 客户端模板管理主键
     * @return 客户端模板管理
     */
    public ClientTemplate selectClientTemplateById(Long id);

    /**
     * 查询客户端模板管理列表
     * 
     * @param clientTemplate 客户端模板管理
     * @return 客户端模板管理集合
     */
    public List<ClientTemplate> selectClientTemplateList(ClientTemplate clientTemplate);

    /**
     * 新增客户端模板管理
     * 
     * @param clientTemplate 客户端模板管理
     * @return 结果
     */
    public int insertClientTemplate(ClientTemplate clientTemplate);

    /**
     * 修改客户端模板管理
     * 
     * @param clientTemplate 客户端模板管理
     * @return 结果
     */
    public int updateClientTemplate(ClientTemplate clientTemplate);

    /**
     * 删除客户端模板管理
     * 
     * @param id 客户端模板管理主键
     * @return 结果
     */
    public int deleteClientTemplateById(Long id);

    /**
     * 批量删除客户端模板管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteClientTemplateByIds(Long[] ids);
}
