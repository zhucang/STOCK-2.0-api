package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.constant.CacheableKey;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.domain.LangMgr;
import com.ruoyi.system.domain.MailConfig;
import com.ruoyi.system.mapper.MailConfigMapper;
import com.ruoyi.system.service.IMailConfigService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * 平台邮件配置Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-11-03
 */
@Service
public class MailConfigServiceImpl implements IMailConfigService 
{
    @Resource
    private MailConfigMapper mailConfigMapper;

    /**
     * 查询平台邮件配置
     * 
     * @param id 平台邮件配置主键
     * @return 平台邮件配置
     */
    @Override
//    @Cacheable(value = CacheableKey.MAIL_CONFIG + CacheableKey.ENTITY,key = "#id")
    public MailConfig selectMailConfigById(Long id)
    {
        return mailConfigMapper.selectMailConfigById(id);
    }

    /**
     * 查询平台邮件配置列表
     * 
     * @param mailConfig 平台邮件配置
     * @return 平台邮件配置
     */
    @Override
    @Cacheable(value = CacheableKey.MAIL_CONFIG + CacheableKey.LIST,key = "#mailConfig.cacheableKey()")
    public List<MailConfig> selectMailConfigList(MailConfig mailConfig)
    {
        return mailConfigMapper.selectMailConfigList(mailConfig);
    }

    /**
     * 新增平台邮件配置
     * 
     * @param mailConfig 平台邮件配置
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheableKey.MAIL_CONFIG + CacheableKey.LIST,allEntries = true)
    public int insertMailConfig(MailConfig mailConfig)
    {
        MailConfig mailConfigVo = mailConfigMapper.selectMailConfigByEmailAccount(mailConfig.getEmailAccount());
        if (mailConfigVo != null){
            throw new ServiceException("此邮箱账号配置已存在");
        }
        int count = mailConfigMapper.insertMailConfig(mailConfig);
        if (count <= 0){
            throw new ServiceException("系统繁忙");
        }
        //新增配置启用，则禁用其他配置
        if (mailConfig.getStatus() != null && mailConfig.getStatus().equals(0)){
            Long id = mailConfig.getId();
            mailConfigMapper.disabledOtherConfigWithoutId(id);
        }
        return 1;
    }

    /**
     * 修改平台邮件配置
     * 
     * @param mailConfig 平台邮件配置
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.MAIL_CONFIG + CacheableKey.ENTITY,key = "#mailConfig.id"),
            @CacheEvict(value = CacheableKey.MAIL_CONFIG + CacheableKey.LIST,allEntries = true)})
    public int updateMailConfig(MailConfig mailConfig)
    {
        MailConfig mailConfigVo = mailConfigMapper.selectMailConfigByEmailAccount(mailConfig.getEmailAccount());
        if (mailConfigVo != null && !mailConfig.getId().equals(mailConfigVo.getId())){
            throw new ServiceException("此邮箱账号配置已存在");
        }
        int count = mailConfigMapper.updateMailConfig(mailConfig);
        if (count <= 0){
            throw new ServiceException("系统繁忙");
        }
        //修改此配置启用，则禁用其他配置
        if (mailConfig.getStatus() != null && mailConfig.getStatus().equals(0)){
            Long id = mailConfig.getId();
            mailConfigMapper.disabledOtherConfigWithoutId(id);
        }
        return 1;
    }

    /**
     * 修改平台邮件标题多语言配置
     * @param mailConfigId 邮箱配置id
     * @param langMgr 语言包
     * @return
     */
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.MAIL_CONFIG + CacheableKey.ENTITY,key = "#mailConfigId"),
            @CacheEvict(value = CacheableKey.MAIL_CONFIG + CacheableKey.LIST,allEntries = true)})
    public int updateEmailTitleLang(Long mailConfigId, LangMgr langMgr){
        MailConfig mailConfig = new MailConfig();
        mailConfig.setId(mailConfigId);
        mailConfig.setEmailTitleLang(langMgr);
        int count = mailConfigMapper.updateMailConfig(mailConfig);
        if (count <= 0){
            throw new ServiceException("系统繁忙");
        }
        return 1;
    }

    /**
     * 修改平台邮件内容多语言配置
     * @param mailConfigId 邮箱配置id
     * @param langMgr 语言包
     * @return
     */
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.MAIL_CONFIG + CacheableKey.ENTITY,key = "#mailConfigId"),
            @CacheEvict(value = CacheableKey.MAIL_CONFIG + CacheableKey.LIST,allEntries = true)})
    public int updateEmailTContentLang(Long mailConfigId,LangMgr langMgr){
        MailConfig mailConfig = new MailConfig();
        mailConfig.setId(mailConfigId);
        mailConfig.setEmailContentLang(langMgr);
        int count = mailConfigMapper.updateMailConfig(mailConfig);
        if (count <= 0){
            throw new ServiceException("系统繁忙");
        }
        return 1;
    }

    /**
     * 批量删除平台邮件配置
     * 
     * @param ids 需要删除的平台邮件配置主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.MAIL_CONFIG + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.MAIL_CONFIG + CacheableKey.LIST,allEntries = true)})
    public int deleteMailConfigByIds(Long[] ids)
    {
        List<Long> idList = Arrays.asList(ids);
        if (idList.contains(1L)){
            throw new ServiceException("默认模板不允许删除");
        }
        MailConfig search = new MailConfig();
        search.getParams().put("ids", Arrays.asList(ids));
        List<MailConfig> mailConfigs = mailConfigMapper.selectMailConfigList(search);
        //日志记录平台邮件配置信息
        HttpUtils.getRequestLogParams().put("JSONArray:mailConfigs", JSONObject.toJSONString(mailConfigs));
        //检查是否有启用中的配置
        if (mailConfigs.stream().filter(a->a.getStatus().equals(0)).count() > 0){
            throw new ServiceException("不允许删除启用中的配置");
        }
        int count = mailConfigMapper.deleteMailConfigByIds(ids);
        if (count <= 0){
            throw new ServiceException("系统繁忙");
        }
        return 1;
    }

    /**
     * 删除平台邮件配置信息
     * 
     * @param id 平台邮件配置主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.MAIL_CONFIG + CacheableKey.ENTITY,key = "#id"),
            @CacheEvict(value = CacheableKey.MAIL_CONFIG + CacheableKey.LIST,allEntries = true)})
    public int deleteMailConfigById(Long id)
    {
        return mailConfigMapper.deleteMailConfigById(id);
    }
}
