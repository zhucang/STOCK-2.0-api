package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.logDict.SiteMessageLogDict;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SiteMessage;
import com.ruoyi.system.service.ISiteMessageService;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户通知Controller
 * 
 * @author ruoyi
 * @date 2023-11-10
 */
@RestController
@RequestMapping("/system/siteMessage")
public class SiteMessageController extends BaseController
{
    @Autowired
    private ISiteMessageService siteMessageService;

    /**
     * 查询用户通知列表
     */
    @PreAuthorize("@ss.hasPermi('system:siteMessage:list')")
    @GetMapping("/list")
    public TableDataInfo list(SiteMessage siteMessage)
    {
        startPage();
        startOrderBy("id desc");
        siteMessage.setIsPrivate(0);
        List<SiteMessage> list = siteMessageService.selectSiteMessageList(siteMessage);
        return getDataTable(list);
    }

    /**
     * 获取用户通知详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:siteMessage:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(siteMessageService.selectSiteMessageById(id));
    }

    /**
     * 新增用户通知
     */
    @PreAuthorize("@ss.hasPermi('system:siteMessage:add')")
    @Log(title = "新增用户通知", businessType = BusinessType.INSERT,dict = SiteMessageLogDict.class)
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody SiteMessage siteMessage) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        if (StringUtils.isEmpty(siteMessage.getMsgTitle())){
            throw new ServiceException("请输入通知标题");
        }
        if (StringUtils.isEmpty(siteMessage.getMsgContent())){
            throw new ServiceException("请输入通知内容");
        }
        siteMessage.setIsPrivate(0);
        //需要操作的用户IDS
        List<Long> userIdsArr = new ArrayList<>();
        //批量操作
        //用户IDS
        Object userIds = siteMessage.getParams().get("userIds");
        if (userIds instanceof List<?>) {
            userIdsArr = ((List<?>) userIds).stream()
                    .map(id -> Long.valueOf(id.toString()))
                    .collect(Collectors.toList());
        }
        // 如果不是批量
        if (userIdsArr.size() == 0){
            if (siteMessage.getUserId() == null){
                throw new ServiceException("请选择需要通知的用户");
            }
            return toAjax(siteMessageService.insertSiteMessage(siteMessage));
        } else {
            // 实时时间
            Date nowDateTime = DateUtils.getNowDate();
            List<SiteMessage> siteMessages = new ArrayList<>();
            for (int i = 0; i < userIdsArr.size(); i++) {
                SiteMessage vo = new SiteMessage();
                PropertyUtils.copyProperties(vo, siteMessage);
                vo.setUserId(userIdsArr.get(i));
                vo.setCreateTime(nowDateTime);
                siteMessages.add(vo);
            }
            return toAjax(siteMessageService.insertSiteMessages(siteMessages));
        }
    }

    /**
     * 修改用户通知
     */
    @PreAuthorize("@ss.hasPermi('system:siteMessage:edit')")
    @Log(title = "新增用户通知", businessType = BusinessType.UPDATE,dict = SiteMessageLogDict.class)
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody SiteMessage siteMessage)
    {
        if (siteMessage.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (siteMessage.getUserId() == null){
            throw new ServiceException("请选择需要通知的用户");
        }
        if (StringUtils.isEmpty(siteMessage.getMsgTitle())){
            throw new ServiceException("请输入通知标题");
        }
        if (StringUtils.isEmpty(siteMessage.getMsgContent())){
            throw new ServiceException("请输入通知内容");
        }
        return toAjax(siteMessageService.updateSiteMessage(siteMessage));
    }

    /**
     * 修改公告标题多语言
     */
    @PreAuthorize("@ss.hasPermi('system:siteMessage:edit')")
    @Log(title = "修改公告标题多语言", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping(value = "updateMsgTitleLang")
    public AjaxResult updateMsgTitleLang(@RequestBody SiteMessage siteMessage)
    {
        if (siteMessage.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (StringUtils.isEmpty(siteMessage.getMsgTitleLang().getZh())){
            throw new ServiceException("请输入公告标题");
        }
        return toAjax(siteMessageService.updateMsgTitleLang(siteMessage.getId(),siteMessage.getMsgTitleLang()));
    }

    /**
     * 修改公告内容多语言
     */
    @PreAuthorize("@ss.hasPermi('system:siteMessage:edit')")
    @Log(title = "修改公告内容多语言", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping(value = "updateMsgContentLang")
    public AjaxResult updateMsgContentLang(@RequestBody SiteMessage siteMessage)
    {
        if (siteMessage.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (StringUtils.isEmpty(siteMessage.getMsgContentLang().getZh())){
            throw new ServiceException("请输入公告内容");
        }
        return toAjax(siteMessageService.updateMsgContentLang(siteMessage.getId(),siteMessage.getMsgContentLang()));
    }

    /**
     * 删除用户通知
     */
    @PreAuthorize("@ss.hasPermi('system:siteMessage:remove')")
    @Log(title = "删除用户通知", businessType = BusinessType.DELETE,dict = SiteMessageLogDict.class,
            saveParamNames = {"id","msgTitle","siteMessages"})
    @RepeatSubmit
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(siteMessageService.deleteSiteMessageByIds(ids));
    }
}
