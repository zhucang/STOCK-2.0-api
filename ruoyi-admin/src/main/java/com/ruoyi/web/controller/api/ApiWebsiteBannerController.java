package com.ruoyi.web.controller.api;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.WebsiteBanner;
import com.ruoyi.system.service.IWebsiteBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 官网B图片Controller
 * 
 * @author ruoyi
 * @date 2023-12-09
 */
@RestController
@RequestMapping("/api/websiteBanner")
public class ApiWebsiteBannerController extends BaseController
{
    @Autowired
    private IWebsiteBannerService websiteBannerService;

    /**
     * 查询官网B图片列表
     */
    @GetMapping("/list")
    public TableDataInfo list(WebsiteBanner websiteBanner)
    {
        startPage();
        List<WebsiteBanner> list = websiteBannerService.selectWebsiteBannerList(websiteBanner);
        return getDataTable(list);
    }
}
