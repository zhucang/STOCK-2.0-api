package com.ruoyi.web.controller.api;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.SwitchSet;
import com.ruoyi.system.service.ISwitchSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 开关配置Controller
 * 
 * @author ruoyi
 * @date 2023-10-28
 */
@RestController
@RequestMapping("/api/switchSet")
public class ApiSwitchSetController extends BaseController
{
    @Autowired
    private ISwitchSetService switchSetService;

    /**
     * 查询开关配置列表
     */
    @GetMapping("/list")
    public AjaxResult list(SwitchSet switchSet)
    {
        startPage();
        List<SwitchSet> list = switchSetService.selectSwitchSetList(switchSet);
        return AjaxResult.success(list);
    }

    /**
     * 开关状态
     */
    @GetMapping(value = "/switchById")
    public AjaxResult switchById(Long switchSetId)
    {
        return AjaxResult.success(switchSetService.selectSwitchSetById(switchSetId));
    }
}
