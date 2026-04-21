package com.ruoyi.system.service;

import com.ruoyi.system.domain.ClientTemplate;

import java.util.List;

/**
 * 客户端模板管理Service接口
 * 
 * @author ruoyi
 * @date 2024-01-08
 */
public interface IClientTemplateService 
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
     * 批量删除客户端模板管理
     * 
     * @param ids 需要删除的客户端模板管理主键集合
     * @return 结果
     */
    public int deleteClientTemplateByIds(Long[] ids);

    /**
     * 删除客户端模板管理信息
     * 
     * @param id 客户端模板管理主键
     * @return 结果
     */
    public int deleteClientTemplateById(Long id);
}
