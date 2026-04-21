package com.ruoyi.web.controller.api;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.service.ISiteInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 平台基本信息Controller
 * 
 * @author ruoyi
 * @date 2023-11-13
 */
@RestController
@RequestMapping("/api/siteInfo")
public class ApiSiteInfoController extends BaseController
{
    @Autowired
    private ISiteInfoService siteInfoService;

    /**
     * 查询平台基本信息列表
     */
    @GetMapping("/getSiteInfo")
    public AjaxResult getSiteInfo()
    {
        return AjaxResult.success(siteInfoService.selectSiteInfoById(1L));
    }



}
