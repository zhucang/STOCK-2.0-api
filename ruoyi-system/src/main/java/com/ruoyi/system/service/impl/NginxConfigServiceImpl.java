package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.NginxConfig;
import com.ruoyi.system.mapper.NginxConfigMapper;
import com.ruoyi.system.service.INginxConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * nginx配置转发Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-04-23
 */
@Service
public class NginxConfigServiceImpl implements INginxConfigService 
{
    @Resource
    private NginxConfigMapper nginxConfigMapper;

    /**
     * 查询nginx配置转发
     * 
     * @param id nginx配置转发主键
     * @return nginx配置转发
     */
    @Override
    public NginxConfig selectNginxConfigById(Long id)
    {
        return nginxConfigMapper.selectNginxConfigById(id);
    }

    /**
     * 查询nginx配置转发列表
     * 
     * @param nginxConfig nginx配置转发
     * @return nginx配置转发
     */
    @Override
    public List<NginxConfig> selectNginxConfigList(NginxConfig nginxConfig)
    {
        return nginxConfigMapper.selectNginxConfigList(nginxConfig);
    }

    /**
     * 新增nginx配置转发
     * 
     * @param nginxConfig nginx配置转发
     * @return 结果
     */
    @Override
    public int insertNginxConfig(NginxConfig nginxConfig)
    {
        return nginxConfigMapper.insertNginxConfig(nginxConfig);
    }

    /**
     * 修改nginx配置转发
     * 
     * @param nginxConfig nginx配置转发
     * @return 结果
     */
    @Override
    public int updateNginxConfig(NginxConfig nginxConfig)
    {
        return nginxConfigMapper.updateNginxConfig(nginxConfig);
    }

    /**
     * 批量删除nginx配置转发
     * 
     * @param ids 需要删除的nginx配置转发主键
     * @return 结果
     */
    @Override
    public int deleteNginxConfigByIds(Long[] ids)
    {
        return nginxConfigMapper.deleteNginxConfigByIds(ids);
    }

    /**
     * 删除nginx配置转发信息
     * 
     * @param id nginx配置转发主键
     * @return 结果
     */
    @Override
    public int deleteNginxConfigById(Long id)
    {
        return nginxConfigMapper.deleteNginxConfigById(id);
    }
}
