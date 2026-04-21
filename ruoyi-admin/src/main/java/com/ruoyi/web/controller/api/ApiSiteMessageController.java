package com.ruoyi.web.controller.api;

import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.SiteMessage;
import com.ruoyi.system.service.ISiteMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 平台站内信发送记录Controller
 * 
 * @author ruoyi
 * @date 2023-11-10
 *  * cache待优化
 */
@RestController
@RequestMapping("/api/siteMessage")
public class ApiSiteMessageController extends BaseController
{
    @Autowired
    private ISiteMessageService siteMessageService;

    /**
     * 查询平台站内信发送记录列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SiteMessage siteMessage)
    {
        siteMessage.setUserId(SecurityUtils.getUserId());
        startPage();
        startOrderBy("id desc");
        siteMessage.setIsVisible(0);
        List<SiteMessage> list = siteMessageService.selectSiteMessageList(siteMessage);
        return getDataTable(list);
    }

    /**
     * 标记已读
     */
    @GetMapping("/markReadFlag")
    @RepeatSubmit
    public AjaxResult markReadFlag(Long siteMessageId)
    {
        if (siteMessageId == null){
            return AjaxResult.error(HintConstants.PARAM_NULL,"请选择需要标记已读的消息");
        }
        //用户id
        Long userId = SecurityUtils.getUserId();
        return toAjax(siteMessageService.markReadFlag(siteMessageId,userId));
    }

    /**
     * 未读消息数量
     */
    @GetMapping("/notReadMessageCount")
    public AjaxResult isHaveNotReadMessage()
    {
        return AjaxResult.success().put("notReadMessageCount",siteMessageService.notReadMessageCount(SecurityUtils.getUserId()));
    }


}
