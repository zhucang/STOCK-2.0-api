package com.ruoyi.system.service;

import com.ruoyi.system.domain.LangMgr;
import com.ruoyi.system.domain.MailConfig;

import java.util.List;

/**
 * 平台邮件配置Service接口
 * 
 * @author ruoyi
 * @date 2023-11-03
 */
public interface IMailConfigService 
{
    /**
     * 查询平台邮件配置
     * 
     * @param id 平台邮件配置主键
     * @return 平台邮件配置
     */
    public MailConfig selectMailConfigById(Long id);

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
     * 修改平台邮件标题多语言配置
     * @param mailConfigId 邮箱配置id
     * @param langMgr 语言包
     * @return
     */
    public int updateEmailTitleLang(Long mailConfigId,LangMgr langMgr);

    /**
     * 修改平台邮件内容多语言配置
     * @param mailConfigId 邮箱配置id
     * @param langMgr 语言包
     * @return
     */
    public int updateEmailTContentLang(Long mailConfigId,LangMgr langMgr);

    /**
     * 批量删除平台邮件配置
     * 
     * @param ids 需要删除的平台邮件配置主键集合
     * @return 结果
     */
    public int deleteMailConfigByIds(Long[] ids);

    /**
     * 删除平台邮件配置信息
     *
     * @param id 平台邮件配置主键
     * @return 结果
     */
    public int deleteMailConfigById(Long id);
}
