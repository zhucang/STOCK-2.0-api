package com.ruoyi.web.controller.api;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.service.ILangMgrService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 多语言配置包Controller
 * 
 * @author ruoyi
 * @date 2023-11-07
 */
@RestController
@RequestMapping("/api/langMgr")
public class ApiLangMgrController extends BaseController
{
    @Autowired
    private ILangMgrService langMgrService;

    /**
     * 查询多语言配置包列表
     */
    @GetMapping("/list")
    public AjaxResult list(String lang)
    {
        if (StringUtils.isEmpty(lang)){
            lang = "zh";
        }
        return AjaxResult.success(langMgrService.selectLangMgrListByLang(lang));
    }
}
