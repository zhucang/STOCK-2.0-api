package com.ruoyi.web.controller.api;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.ActivityCenter;
import com.ruoyi.system.service.IActivityCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 活动中心配置Controller
 * 
 * @author ruoyi
 * @date 2025-07-02
 */
@RestController
@RequestMapping("/api/activityCenter")
public class ApiActivityCenterController extends BaseController
{
    @Autowired
    private IActivityCenterService activityCenterService;

    /**
     * 查询活动中心配置列表
     */
    @GetMapping("/list")
    public TableDataInfo list(ActivityCenter activityCenter) {
        startPage();
        startOrderBy("sort is null,sort");
        List<ActivityCenter> list = activityCenterService.selectActivityCenterList(activityCenter);
        return getDataTable(list);
    }
}
