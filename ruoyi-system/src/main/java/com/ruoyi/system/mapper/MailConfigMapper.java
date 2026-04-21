package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.MailConfig;

import java.util.List;

/**
 * 平台邮件配置Mapper接口
 * 
 * @author ruoyi
 * @date 2023-11-03
 */
public interface MailConfigMapper 
{
    /**
     * 查询平台邮件配置
     * 
     * @param id 平台邮件配置主键
     * @return 平台邮件配置
     */
    public MailConfig selectMailConfigById(Long id);

    /**
     * 查询平台邮件配置
     *
     * @param emailAccount 邮箱账号
     * @return 平台邮件配置
     */
    public MailConfig selectMailConfigByEmailAccount(String emailAccount);

    /**
     * 查询平台邮件配置列表
     * 
     * @param mailConfig 平台邮件配置
     * @return 平台邮件配置集合
     */
    public List<MailConfig> selectMailConfigList(MailConfig mailConfig);

    /**
     * 新增平台邮件配置
     * 
     * @param mailConfig 平台邮件配置
     * @return 结果
     */
    public int insertMailConfig(MailConfig mailConfig);

    /**
     * 修改平台邮件配置
     * 
     * @param mailConfig 平台邮件配置
     * @return 结果
     */
    public int updateMailConfig(MailConfig mailConfig);

    /**
     * 删除平台邮件配置
     * 
     * @param id 平台邮件配置主键
     * @return 结果
     */
    public int deleteMailConfigById(Long id);

    /**
     * 批量删除平台邮件配置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMailConfigByIds(Long[] ids);

    /**
     * 禁用除了此id外的所有配置
     * @param id 平台邮件配置主键
     */
    public int disabledOtherConfigWithoutId(Long id);

    /**
     * 查询启用中的平台邮件配置
     * @return 平台邮件配置
     */
    public MailConfig selectEnableMailConfig();

    /**
     * 查询启用中的平台邮件配置的应用密码
     * @return 平台邮件配置
     */
    public String selectEnableMailPassword();
}
