package com.ruoyi.web.controller.api;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.AppConfig;
import com.ruoyi.system.service.IAppConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * app配置Controller
 * 
 * @author ruoyi
 * @date 2023-12-20
 * 已优化
 */
@RestController
@RequestMapping("/api/appConfig")
public class ApiAppConfigController extends BaseController
{
    @Autowired
    private IAppConfigService appConfigService;

    /**
     * 查询app配置列表
     */
    @GetMapping("/list")
    public TableDataInfo list(AppConfig appConfig)
    {
        startPage();
        String orderBy = "sort is null,sort";
        startOrderBy(orderBy);
        appConfig.getParams().put("orderBy",orderBy);
        appConfig.setIsVisible(true);
        List<AppConfig> list = appConfigService.selectAppConfigList(appConfig);
        return getDataTable(list);
    }
}
