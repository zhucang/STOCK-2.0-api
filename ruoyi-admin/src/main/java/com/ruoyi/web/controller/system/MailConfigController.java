package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.logDict.MailConfigLogDict;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.MailConfig;
import com.ruoyi.system.service.IMailConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 平台邮件配置Controller
 * 
 * @author ruoyi
 * @date 2023-11-03
 * 日志已优化
 */
@RestController
@RequestMapping("/system/mailConfig")
public class MailConfigController extends BaseController
{
    @Autowired
    private IMailConfigService mailConfigService;

    /**
     * 查询平台邮件配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:mailConfig:list')")
    @GetMapping("/list")
    public TableDataInfo list(MailConfig mailConfig)
    {
        startPage();
        startOrderBy("sort is null,sort");
        List<MailConfig> list = mailConfigService.selectMailConfigList(mailConfig);
        return getDataTable(list);
    }

    /**
     * 获取平台邮件配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:mailConfig:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(mailConfigService.selectMailConfigById(id));
    }

    /**
     * 新增平台邮件配置
     */
    @PreAuthorize("@ss.hasPermi('system:mailConfig:add')")
    @Log(title = "新增平台邮件配置", businessType = BusinessType.INSERT,dict = MailConfigLogDict.class)
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody MailConfig mailConfig)
    {
        if (StringUtils.isEmpty(mailConfig.getEmailAccount())){
            throw new ServiceException("请输入邮箱账号");
        }
        if (StringUtils.isEmpty(mailConfig.getEmailPassword())){
            throw new ServiceException("请输入邮箱应用密码");
        }
        if (StringUtils.isEmpty(mailConfig.getSmtpDomain())){
            throw new ServiceException("请输入邮箱的smtp服务器域名");
        }
        if (mailConfig.getRequestPort() == null){
            throw new ServiceException("请输入邮箱的smtp服务器请求端口");
        }
        if (StringUtils.isEmpty(mailConfig.getEmailTitle())){
            throw new ServiceException("请输入邮件标题");
        }
        if (StringUtils.isEmpty(mailConfig.getEmailContent())){
            throw new ServiceException("请输入邮件内容");
        }
        if (mailConfig.getStatus() == null){
            throw new ServiceException("请选择邮箱是否启用");
        }
        if (mailConfig.getRequestMethod() == null){
            throw new ServiceException("请选择请求方式");
        }
        return toAjax(mailConfigService.insertMailConfig(mailConfig));
    }

    /**
     * 修改平台邮件配置
     */
    @PreAuthorize("@ss.hasPermi('system:mailConfig:edit')")
    @Log(title = "修改平台邮件配置", businessType = BusinessType.UPDATE,dict = MailConfigLogDict.class)
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody MailConfig mailConfig)
    {
        if (mailConfig.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (StringUtils.isEmpty(mailConfig.getEmailTitle())){
            throw new ServiceException("请输入邮箱标题");
        }
        if (StringUtils.isEmpty(mailConfig.getEmailContent())){
            throw new ServiceException("请输入邮箱内容");
        }
        if (mailConfig.getStatus() == null){
            throw new ServiceException("请选择邮箱是否启用");
        }
        if (mailConfig.getRequestMethod() == null){
            throw new ServiceException("请选择请求方式");
        }
        return toAjax(mailConfigService.updateMailConfig(mailConfig));
    }

    /**
     * 修改平台邮件标题多语言配置
     */
    @PreAuthorize("@ss.hasPermi('system:mailConfig:edit')")
    @Log(title = "修改平台邮件标题多语言配置", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping(value = "updateEmailTitleLang")
    public AjaxResult updateEmailTitleLang(@RequestBody MailConfig mailConfig)
    {
        if (mailConfig.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (StringUtils.isEmpty(mailConfig.getEmailTitleLang().getZh())){
            throw new ServiceException("请输入邮件标题");
        }
        return toAjax(mailConfigService.updateEmailTitleLang(mailConfig.getId(),mailConfig.getEmailTitleLang()));
    }

    /**
     * 修改平台邮件内容多语言配置
     */
    @PreAuthorize("@ss.hasPermi('system:mailConfig:edit')")
    @Log(title = "修改平台邮件内容多语言配置", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping(value = "updateEmailTContentLang")
    public AjaxResult updateEmailTContentLang(@RequestBody MailConfig mailConfig)
    {
        if (mailConfig.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (StringUtils.isEmpty(mailConfig.getEmailContentLang().getZh())) {
            throw new ServiceException("请输入邮件内容");
        }
        return toAjax(mailConfigService.updateEmailTContentLang(mailConfig.getId(),mailConfig.getEmailContentLang()));
    }

    /**
     * 删除平台邮件配置
     */
    @PreAuthorize("@ss.hasPermi('system:mailConfig:remove')")
    @Log(title = "删除平台邮件配置", businessType = BusinessType.DELETE,dict = MailConfigLogDict.class,
            saveParamNames = {"id","emailAccount","mailConfigs"})
    @RepeatSubmit
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(mailConfigService.deleteMailConfigByIds(ids));
    }
}
