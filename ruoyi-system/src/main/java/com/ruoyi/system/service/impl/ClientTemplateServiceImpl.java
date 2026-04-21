package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.ClientTemplate;
import com.ruoyi.system.mapper.ClientTemplateMapper;
import com.ruoyi.system.service.IClientTemplateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 客户端模板管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-01-08
 */
@Service
public class ClientTemplateServiceImpl implements IClientTemplateService 
{
    @Resource
    private ClientTemplateMapper clientTemplateMapper;

    /**
     * 查询客户端模板管理
     * 
     * @param id 客户端模板管理主键
     * @return 客户端模板管理
     */
    @Override
    public ClientTemplate selectClientTemplateById(Long id)
    {
        return clientTemplateMapper.selectClientTemplateById(id);
    }

    /**
     * 查询客户端模板管理列表
     * 
     * @param clientTemplate 客户端模板管理
     * @return 客户端模板管理
     */
    @Override
    public List<ClientTemplate> selectClientTemplateList(ClientTemplate clientTemplate)
    {
        return clientTemplateMapper.selectClientTemplateList(clientTemplate);
    }

    /**
     * 新增客户端模板管理
     * 
     * @param clientTemplate 客户端模板管理
     * @return 结果
     */
    @Override
    public int insertClientTemplate(ClientTemplate clientTemplate)
    {
        return clientTemplateMapper.insertClientTemplate(clientTemplate);
    }

    /**
     * 修改客户端模板管理
     * 
     * @param clientTemplate 客户端模板管理
     * @return 结果
     */
    @Override
    public int updateClientTemplate(ClientTemplate clientTemplate)
    {
        return clientTemplateMapper.updateClientTemplate(clientTemplate);
    }

    /**
     * 批量删除客户端模板管理
     * 
     * @param ids 需要删除的客户端模板管理主键
     * @return 结果
     */
    @Override
    public int deleteClientTemplateByIds(Long[] ids)
    {
        return clientTemplateMapper.deleteClientTemplateByIds(ids);
    }

    /**
     * 删除客户端模板管理信息
     * 
     * @param id 客户端模板管理主键
     * @return 结果
     */
    @Override
    public int deleteClientTemplateById(Long id)
    {
        return clientTemplateMapper.deleteClientTemplateById(id);
    }
}
