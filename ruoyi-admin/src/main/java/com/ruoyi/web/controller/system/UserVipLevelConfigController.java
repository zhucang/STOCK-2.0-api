package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.UserVipLevelConfig;
import com.ruoyi.system.service.IUserVipLevelConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户vip等级升级配置Controller
 * 
 * @author ruoyi
 * @date 2023-11-05
 */
@RestController
@RequestMapping("/system/userVipLevelConfig")
public class UserVipLevelConfigController extends BaseController
{
    @Autowired
    private IUserVipLevelConfigService userVipLevelConfigService;

    /**
     * 查询用户vip等级升级配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:userVipLevelConfig:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserVipLevelConfig userVipLevelConfig)
    {
        startPage();
        startOrderBy("vip_level");
        List<UserVipLevelConfig> list = userVipLevelConfigService.selectUserVipLevelConfigList(userVipLevelConfig);
        return getDataTable(list);
    }

    /**
     * 查询用户vip等级升级配置列表
     */
    @GetMapping("/listNoPreAuthorize")
    public TableDataInfo listNoPreAuthorize(UserVipLevelConfig userVipLevelConfig)
    {
        startPage();
        startOrderBy("vip_level");
        List<UserVipLevelConfig> list = userVipLevelConfigService.selectUserVipLevelConfigList(userVipLevelConfig);
        return getDataTable(list);
    }

    /**
     * 获取用户vip等级升级配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:userVipLevelConfig:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(userVipLevelConfigService.selectUserVipLevelConfigById(id));
    }

    /**
     * 新增用户vip等级升级配置
     */
    @PreAuthorize("@ss.hasPermi('system:userVipLevelConfig:add')")
    @Log(title = "新增用户vip等级升级配置", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody UserVipLevelConfig userVipLevelConfig)
    {
        if (userVipLevelConfig.getVipLevel() == null){
            AjaxResult.error("请输入vip等级");
        }
        if (userVipLevelConfig.getVipLevel() < 0){
            return AjaxResult.error("VIP等级不允许小于0");
        }
        if (userVipLevelConfig.getRechargeAmountMin() == null){
            return AjaxResult.error("请输入该等级的最小充值金额");
        }
        if (userVipLevelConfig.getRechargeAmountMin().compareTo(BigDecimal.ZERO) <= 0){
            return AjaxResult.error("最小充值金额必须大于0");
        }
        if (userVipLevelConfig.getRechargeAmountMax() == null){
            return AjaxResult.error("请输入该等级的最大充值金额");
        }
        if (userVipLevelConfig.getRechargeAmountMin().compareTo(userVipLevelConfig.getRechargeAmountMax()) > 0){
            return AjaxResult.error("最小充值金额不允许大于最大充值金额");
        }
        if (userVipLevelConfig.getRechargeBonusRatio() == null){
            return AjaxResult.error("请输入充值赠送比率");
        }
        if (userVipLevelConfig.getRechargeBonusRatio().compareTo(BigDecimal.ZERO) < 0){
            return AjaxResult.error("充值赠送比率不允许小于0");
        }
        if (userVipLevelConfig.getRechargeBonusAmount() == null){
            return AjaxResult.error("请输入充值赠送金额");
        }
        if (userVipLevelConfig.getRechargeBonusAmount().compareTo(BigDecimal.ZERO) < 0){
            return AjaxResult.error("充值赠送金额不允许小于0");
        }
        if (userVipLevelConfig.getDay() == null){
            return AjaxResult.error("请输入天数");
        }
        if (userVipLevelConfig.getDay() < 0){
            return AjaxResult.error("天数不允许小于0");
        }
        if (userVipLevelConfig.getOrderNum() == null){
            return AjaxResult.error("请输入允许交易的总单数");
        }
        if (userVipLevelConfig.getOrderNum() < 0){
            return AjaxResult.error("允许交易的总单数不允许小于0");
        }
        return userVipLevelConfigService.insertUserVipLevelConfig(userVipLevelConfig);
    }

    /**
     * 修改用户vip等级升级配置
     */
    @PreAuthorize("@ss.hasPermi('system:userVipLevelConfig:edit')")
    @Log(title = "修改用户vip等级升级配置", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody UserVipLevelConfig userVipLevelConfig)
    {
        if (userVipLevelConfig.getId() == null){
            AjaxResult.error("请选择需要修改的选项");
        }
        if (userVipLevelConfig.getVipLevel() == null){
            AjaxResult.error("请输入vip等级");
        }
        if (userVipLevelConfig.getVipLevel() < 0){
            return AjaxResult.error("VIP等级不允许小于0");
        }
        if (!userVipLevelConfig.getVipLevel().equals(0)){
            if (userVipLevelConfig.getRechargeAmountMin() == null){
                return AjaxResult.error("请输入该等级的最小充值金额");
            }
            if (userVipLevelConfig.getRechargeAmountMin().compareTo(BigDecimal.ZERO) <= 0){
                return AjaxResult.error("最小充值金额必须大于0");
            }
            if (userVipLevelConfig.getRechargeAmountMax() == null){
                return AjaxResult.error("请输入该等级的最大充值金额");
            }
            if (userVipLevelConfig.getRechargeAmountMin().compareTo(userVipLevelConfig.getRechargeAmountMax()) > 0){
                return AjaxResult.error("最小充值金额不允许大于最大充值金额");
            }
            if (userVipLevelConfig.getRechargeBonusRatio() == null){
                return AjaxResult.error("请输入充值赠送比率");
            }
            if (userVipLevelConfig.getRechargeBonusRatio().compareTo(BigDecimal.ZERO) < 0){
                return AjaxResult.error("充值赠送比率不允许小于0");
            }
            if (userVipLevelConfig.getRechargeBonusAmount() == null){
                return AjaxResult.error("请输入充值赠送金额");
            }
            if (userVipLevelConfig.getRechargeBonusAmount().compareTo(BigDecimal.ZERO) < 0){
                return AjaxResult.error("充值赠送金额不允许小于0");
            }
            if (userVipLevelConfig.getDay() == null){
                return AjaxResult.error("请输入天数");
            }
            if (userVipLevelConfig.getDay() < 0){
                return AjaxResult.error("天数不允许小于0");
            }
            if (userVipLevelConfig.getOrderNum() == null){
                return AjaxResult.error("请输入允许交易的总单数");
            }
            if (userVipLevelConfig.getOrderNum() < 0){
                return AjaxResult.error("允许交易的总单数不允许小于0");
            }
        }
        return userVipLevelConfigService.updateUserVipLevelConfig(userVipLevelConfig);
    }

    /**
     * 删除用户vip等级升级配置
     */
    @PreAuthorize("@ss.hasPermi('system:userVipLevelConfig:remove')")
    @Log(title = "删除用户vip等级升级配置", businessType = BusinessType.DELETE)
    @RepeatSubmit
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(userVipLevelConfigService.deleteUserVipLevelConfigByIds(ids));
    }
}
