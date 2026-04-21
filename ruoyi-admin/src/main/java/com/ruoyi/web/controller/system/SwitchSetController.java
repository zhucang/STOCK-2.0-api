package com.ruoyi.web.controller.system;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.logDict.SwitchSetLogDict;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.system.domain.SwitchSet;
import com.ruoyi.system.service.ISwitchSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 开关配置Controller
 * 
 * @author ruoyi
 * @date 2023-10-28
 */
@RestController
@RequestMapping("/system/switchSet")
public class SwitchSetController extends BaseController
{
    @Autowired
    private ISwitchSetService switchSetService;

    /**
     * 查询开关配置列表
     */
    @GetMapping("/list")
    public AjaxResult list(SwitchSet switchSet)
    {
        startOrderBy("sort is null,sort");
        List<SwitchSet> list = switchSetService.selectSwitchSetList(switchSet);
        PageHelper.clearPage();
        return AjaxResult.success(list);
    }

    /**
     * 开关状态
     */
    @GetMapping(value = "/switchById")
    public AjaxResult switchById(Long id)
    {
        return AjaxResult.success(switchSetService.selectSwitchSetById(id));
    }

    /**
     * 修改开关配置
     */
    @Log(title = "修改开关配置", businessType = BusinessType.UPDATE,dict = SwitchSetLogDict.class)
    @RepeatSubmit
    @PostMapping(value = "updateSwitchStatus")
    public AjaxResult edit(@RequestBody SwitchSet switchSet)
    {
        if (switchSet.getId() == null){
            return AjaxResult.error("请选项需要修改的选项");
        }
        if (switchSet.getStatus() == null){
            return  AjaxResult.error("请选择开关状态");
        }
        AjaxResult filter = filter(switchSet);
        if (!filter.isSuccess()){
            return filter;
        }
        //开关信息
        SwitchSet switchSetVo = switchSetService.selectSwitchSetById(switchSet.getId());
        //日志记录开关名称
        HttpUtils.getRequestLogParams().put("switchName",switchSetVo.getSwitchName());
        return toAjax(switchSetService.updateSwitchSet(switchSet));
    }


    AjaxResult filter(SwitchSet switchSet){
        return AjaxResult.success();
    }

    /**
     * 查询手机注册验证码模式
     */
    @GetMapping(value = "mobileRegisterValidateModel")
    public AjaxResult mobileRegisterValidateModel()
    {
        return AjaxResult.success(switchSetService.selectSwitchSetById(6L));
    }

    /**
     * 查询邮箱注册验证码模式
     */
    @GetMapping(value = "emailRegisterValidateModel")
    public AjaxResult emailRegisterValidateModel()
    {
        return AjaxResult.success(switchSetService.selectSwitchSetById(7L));
    }

    /**
     * 查询注册邀请码开关
     */
    @GetMapping(value = "registerInviteCodeSwitch")
    public AjaxResult registerInviteCodeSwitch()
    {
        return AjaxResult.success(switchSetService.selectSwitchSetById(14L));
    }

    /**
     * 查询充值订单限制模式
     */
    @GetMapping(value = "rechargeOrderLimitModel")
    public AjaxResult rechargeOrderLimitModel()
    {
        return AjaxResult.success(switchSetService.selectSwitchSetById(47L));
    }

    /**
     * 查询后台提示音模式
     */
    @GetMapping(value = "backgroundSoundModel")
    public AjaxResult backgroundSoundModel()
    {
        return AjaxResult.success(switchSetService.selectSwitchSetById(38L));
    }
}
