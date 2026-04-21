package com.ruoyi.web.controller.api;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.SiteMessageType;
import com.ruoyi.system.service.ISiteMessageTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 站内信类型Controller
 * 
 * @author ruoyi
 * @date 2026-04-12
 */
@RestController
@RequestMapping("/api/siteMessageType")
public class ApiSiteMessageTypeController extends BaseController
{
    @Autowired
    private ISiteMessageTypeService siteMessageTypeService;

    /**
     * 查询站内信类型列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SiteMessageType siteMessageType)
    {
        startPage();
        List<SiteMessageType> list = siteMessageTypeService.selectSiteMessageTypeList(siteMessageType);
        return getDataTable(list);
    }
}
