package com.ruoyi.web.controller.api;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.WebMenuConfig;
import com.ruoyi.system.service.IWebMenuConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 官网菜单配置Controller
 * 
 * @author ruoyi
 * @date 2023-12-11
 */
@RestController
@RequestMapping("/api/webMenuConfig")
public class ApiWebMenuConfigController extends BaseController
{
    @Autowired
    private IWebMenuConfigService webMenuConfigService;

    /**
     * 查询官网菜单配置列表
     */
    @GetMapping("/list")
    public TableDataInfo list(WebMenuConfig webMenuConfig)
    {
        startPage();
        startOrderBy("sort is null,sort");
        List<WebMenuConfig> list = webMenuConfigService.selectWebMenuConfigList(webMenuConfig);
        return getDataTable(list);
    }
}
