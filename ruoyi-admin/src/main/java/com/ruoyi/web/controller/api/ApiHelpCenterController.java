package com.ruoyi.web.controller.api;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.HelpCenter;
import com.ruoyi.system.service.IHelpCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 帮助中心Controller
 * 
 * @author ruoyi
 * @date 2023-11-22
 */
@RestController
@RequestMapping("/api/helpCenter")
public class ApiHelpCenterController extends BaseController
{
    @Autowired
    private IHelpCenterService helpCenterService;

    /**
     * 查询帮助中心列表
     */
    @GetMapping("/list")
    public TableDataInfo list(HelpCenter helpCenter)
    {
        startPage();
        startOrderBy("sort is null,sort");
        helpCenter.setStatus(0);
        List<HelpCenter> list = helpCenterService.selectHelpCenterList(helpCenter);
        return getDataTable(list);
    }
}
