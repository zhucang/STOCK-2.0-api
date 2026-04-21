package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.logDict.SiteMessageLogDict;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SiteMessage;
import com.ruoyi.system.service.ISiteMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 平台公告Controller
 * 
 * @author ruoyi
 * @date 2023-11-10
 */
@RestController
@RequestMapping("/system/siteNotice")
public class SiteNoticeController extends BaseController
{
    @Autowired
    private ISiteMessageService siteMessageService;

    /**
     * 查询平台公告列表
     */
    @PreAuthorize("@ss.hasPermi('system:siteNotice:list')")
    @GetMapping("/list")
    public TableDataInfo list(SiteMessage siteMessage)
    {
        startPage();
        startOrderBy("id desc");
        siteMessage.setIsPrivate(1);
        List<SiteMessage> list = siteMessageService.selectSiteMessageList(siteMessage);
        return getDataTable(list);
    }

    /**
     * 获取平台公告详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:siteNotice:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(siteMessageService.selectSiteMessageById(id));
    }

    /**
     * 新增平台公告
     */
    @PreAuthorize("@ss.hasPermi('system:siteNotice:add')")
    @Log(title = "新增平台公告", businessType = BusinessType.INSERT,dict = SiteMessageLogDict.class)
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody SiteMessage siteMessage)
    {
        if (StringUtils.isEmpty(siteMessage.getMsgTitle())){
            return AjaxResult.error("请输入公告标题");
        }
        if (StringUtils.isEmpty(siteMessage.getMsgContent())){
            return AjaxResult.error("请输入公告内容");
        }
        siteMessage.setIsPrivate(1);
        return toAjax(siteMessageService.insertSiteMessage(siteMessage));
    }

    /**
     * 修改平台公告
     */
    @PreAuthorize("@ss.hasPermi('system:siteNotice:edit')")
    @Log(title = "修改平台公告", businessType = BusinessType.UPDATE,dict = SiteMessageLogDict.class)
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody SiteMessage siteMessage)
    {
        if (siteMessage.getId() == null){
            return AjaxResult.error("请选择需要修改的选项");
        }
        if (StringUtils.isEmpty(siteMessage.getMsgTitle())){
            return AjaxResult.error("请输入公告标题");
        }
        if (StringUtils.isEmpty(siteMessage.getMsgContent())){
            return AjaxResult.error("请输入公告内容");
        }
        return toAjax(siteMessageService.updateSiteMessage(siteMessage));
    }

    /**
     * 修改公告标题多语言
     */
    @PreAuthorize("@ss.hasPermi('system:siteNotice:edit')")
    @Log(title = "修改公告标题多语言", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping(value = "updateMsgTitleLang")
    public AjaxResult updateMsgTitleLang(@RequestBody SiteMessage siteMessage)
    {
        if (siteMessage.getId() == null){
            return AjaxResult.error("请选择需要修改的选项");
        }
        if (StringUtils.isEmpty(siteMessage.getMsgTitleLang().getZh())){
            return AjaxResult.error("请输入公告标题");
        }
        return toAjax(siteMessageService.updateMsgTitleLang(siteMessage.getId(),siteMessage.getMsgTitleLang()));
    }

    /**
     * 修改公告内容多语言
     */
    @PreAuthorize("@ss.hasPermi('system:siteNotice:edit')")
    @Log(title = "修改公告内容多语言", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping(value = "updateMsgContentLang")
    public AjaxResult updateMsgContentLang(@RequestBody SiteMessage siteMessage)
    {
        if (siteMessage.getId() == null){
            return AjaxResult.error("请选择需要修改的选项");
        }
        if (StringUtils.isEmpty(siteMessage.getMsgContentLang().getZh())){
            return AjaxResult.error("请输入公告内容");
        }
        return toAjax(siteMessageService.updateMsgContentLang(siteMessage.getId(),siteMessage.getMsgContentLang()));
    }

    /**
     * 删除平台公告
     */
    @PreAuthorize("@ss.hasPermi('system:siteNotice:remove')")
    @Log(title = "删除平台公告", businessType = BusinessType.DELETE,dict = SiteMessageLogDict.class,
            saveParamNames = {"id","msgTitle","siteMessages"})
    @RepeatSubmit
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(siteMessageService.deleteSiteMessageByIds(ids));
    }
}
