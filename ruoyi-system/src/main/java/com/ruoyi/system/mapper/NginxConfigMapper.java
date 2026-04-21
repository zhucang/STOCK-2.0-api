package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.NginxConfig;

import java.util.List;

/**
 * nginx配置转发Mapper接口
 * 
 * @author ruoyi
 * @date 2024-04-23
 */
public interface NginxConfigMapper 
{
    /**
     * 查询nginx配置转发
     * 
     * @param id nginx配置转发主键
     * @return nginx配置转发
     */
    public NginxConfig selectNginxConfigById(Long id);

    /**
     * 查询nginx配置转发列表
     * 
     * @param nginxConfig nginx配置转发
     * @return nginx配置转发集合
     */
    public List<NginxConfig> selectNginxConfigList(NginxConfig nginxConfig);

    /**
     * 新增nginx配置转发
     * 
     * @param nginxConfig nginx配置转发
     * @return 结果
     */
    public int insertNginxConfig(NginxConfig nginxConfig);

    /**
     * 修改nginx配置转发
     * 
     * @param nginxConfig nginx配置转发
     * @return 结果
     */
    public int updateNginxConfig(NginxConfig nginxConfig);

    /**
     * 删除nginx配置转发
     * 
     * @param id nginx配置转发主键
     * @return 结果
     */
    public int deleteNginxConfigById(Long id);

    /**
     * 批量删除nginx配置转发
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteNginxConfigByIds(Long[] ids);
}
