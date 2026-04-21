package com.ruoyi.web.controller.api;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.SiteBanner;
import com.ruoyi.system.service.ISiteBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * banner横幅图片配置Controller
 * 
 * @author ruoyi
 * @date 2023-11-04
 */
@RestController
@RequestMapping("/api/siteBanner")
public class ApiSiteBannerController extends BaseController
{
    @Autowired
    private ISiteBannerService siteBannerService;

    /**
     * 查询banner横幅图片配置列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SiteBanner siteBanner)
    {
        startPage();
        startOrderBy("sort is null,sort");
        List<SiteBanner> list = siteBannerService.selectSiteBannerList(siteBanner);
        return getDataTable(list);
    }
}
