package com.ruoyi.web.controller.api;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.service.IWebsiteLangService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 官网多语言包Controller
 * 
 * @author ruoyi
 * @date 2023-12-09
 */
@RestController
@RequestMapping("/api/websiteLang")
public class ApiWebsiteLangController extends BaseController
{
    @Autowired
    private IWebsiteLangService websiteLangService;

    /**
     * 查询多语言配置包列表
     */
    @GetMapping("/list")
    public AjaxResult list(String lang)
    {
        if (StringUtils.isEmpty(lang)){
            lang = "zh";
        }
        return AjaxResult.success(websiteLangService.selectWebsiteLangListByLang(lang));
    }
}
