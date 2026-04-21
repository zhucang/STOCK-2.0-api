package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.UserRebateRate;
import com.ruoyi.system.service.IUserRebateRateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户返佣比率配置Controller
 * 
 * @author ruoyi
 * @date 2023-11-05
 */
@RestController
@RequestMapping("/system/userRebateRate")
public class UserRebateRateController extends BaseController
{
    @Autowired
    private IUserRebateRateService userRebateRateService;

    /**
     * 查询用户返佣比率配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:userRebateRate:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserRebateRate userRebateRate)
    {
        startPage();
        List<UserRebateRate> list = userRebateRateService.selectUserRebateRateList(userRebateRate);
        return getDataTable(list);
    }

    /**
     * 获取用户返佣比率配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:userRebateRate:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(userRebateRateService.selectUserRebateRateById(id));
    }

    /**
     * 新增用户返佣比率配置
     */
    @PreAuthorize("@ss.hasPermi('system:userRebateRate:add')")
    @Log(title = "新增用户返佣比率配置", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody UserRebateRate userRebateRate)
    {
        if (StringUtils.isEmpty(userRebateRate.getRebateName())){
            return AjaxResult.error("请输入返佣名称");
        }
        if (userRebateRate.getRebateRate() == null){
            return AjaxResult.error("请输入返佣比率");
        }
        if (userRebateRate.getRebateRate().compareTo(BigDecimal.ZERO) == -1){
            return AjaxResult.error("返佣比率不允许小于0");
        }
        if (userRebateRate.getRebateLevel() == null){
            return AjaxResult.error("请输入返佣等级");
        }
        return userRebateRateService.insertUserRebateRate(userRebateRate);
    }

    /**
     * 修改用户返佣比率配置
     */
    @PreAuthorize("@ss.hasPermi('system:userRebateRate:edit')")
    @Log(title = "修改用户返佣比率配置", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody UserRebateRate userRebateRate)
    {
        if (userRebateRate.getId() == null){
            return AjaxResult.error("请选择需要修改的信息");
        }
        if (StringUtils.isEmpty(userRebateRate.getRebateName())){
            return AjaxResult.error("请输入返佣名称");
        }
        if (userRebateRate.getRebateRate() == null){
            return AjaxResult.error("请输入返佣比率");
        }
        if (userRebateRate.getRebateRate().compareTo(BigDecimal.ZERO) == -1){
            return AjaxResult.error("返佣比率不允许小于0");
        }
        if (userRebateRate.getRebateLevel() == null){
            return AjaxResult.error("请输入返佣等级");
        }
        return userRebateRateService.updateUserRebateRate(userRebateRate);
    }

    /**
     * 删除用户返佣比率配置
     */
    @PreAuthorize("@ss.hasPermi('system:userRebateRate:remove')")
    @Log(title = "删除用户返佣比率配置", businessType = BusinessType.DELETE)
    @RepeatSubmit
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(userRebateRateService.deleteUserRebateRateByIds(ids));
    }
}
