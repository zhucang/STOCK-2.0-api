package com.ruoyi.web.controller.api;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.OtherValue;
import com.ruoyi.system.service.IOtherValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 其他值Controller
 * 
 * @author ruoyi
 * @date 2023-10-28
 */
@RestController
@RequestMapping("/api/otherValue")
public class ApiOtherValueController extends BaseController
{
    @Autowired
    private IOtherValueService otherValueService;

    /**
     * 查询其他值列表
     */
    @GetMapping("/list")
    public AjaxResult list(OtherValue otherValue)
    {
        return AjaxResult.success(otherValueService.getOtherValueMap(otherValue));
    }
}
