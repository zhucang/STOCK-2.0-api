package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.logDict.BonusConfigLogDict;
import com.ruoyi.system.domain.BonusConfig;
import com.ruoyi.system.service.IBonusConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 赠送彩金配置Controller
 * 
 * @author ruoyi
 * @date 2023-11-29
 */
@RestController
@RequestMapping("/system/bonusConfig")
public class BonusConfigController extends BaseController
{
    @Autowired
    private IBonusConfigService bonusConfigService;

    /**
     * 查询赠送彩金配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:bonusConfig:list')")
    @GetMapping("/list")
    public TableDataInfo list(BonusConfig bonusConfig)
    {
        startPage();
        List<BonusConfig> list = bonusConfigService.selectBonusConfigList(bonusConfig);
        return getDataTable(list);
    }

    /**
     * 获取赠送彩金配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:bonusConfig:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(bonusConfigService.selectBonusConfigById(id));
    }

    /**
     * 新增赠送彩金配置
     */
    @PreAuthorize("@ss.hasPermi('system:bonusConfig:add')")
    @Log(title = "新增赠送彩金配置", businessType = BusinessType.INSERT,dict = BonusConfigLogDict.class)
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody BonusConfig bonusConfig)
    {
        if (bonusConfig.getStartTime() == null){
            throw new ServiceException("请选择活动开始时间");
        }
        if (bonusConfig.getEndTime() == null){
            throw new ServiceException("请选择活动结束时间");
        }
        if (bonusConfig.getStartTime().after(bonusConfig.getEndTime())){
            throw new ServiceException("活动开始时间不能晚于活动结束时间");
        }
        if (bonusConfig.getBonusAmount() == null){
            throw new ServiceException("请输入赠送彩金金额");
        }
        if (bonusConfig.getBonusAmount().compareTo(BigDecimal.ZERO) <= 0){
            throw new ServiceException("赠送彩金金额必须大于0");
        }
        if (bonusConfig.getCurrencyId() == null){
            throw new ServiceException("请选择赠送彩金币种");
        }
        return toAjax(bonusConfigService.insertBonusConfig(bonusConfig));
    }

    /**
     * 修改赠送彩金配置
     */
    @PreAuthorize("@ss.hasPermi('system:bonusConfig:edit')")
    @Log(title = "修改赠送彩金配置", businessType = BusinessType.UPDATE,dict = BonusConfigLogDict.class)
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody BonusConfig bonusConfig)
    {
        if (bonusConfig.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (bonusConfig.getStartTime() == null){
            throw new ServiceException("请选择活动开始时间");
        }
        if (bonusConfig.getEndTime() == null){
            throw new ServiceException("请选择活动结束时间");
        }
        if (bonusConfig.getStartTime().after(bonusConfig.getEndTime())){
            throw new ServiceException("活动开始时间不能晚于活动结束时间");
        }
        if (bonusConfig.getBonusAmount() == null){
            throw new ServiceException("请输入赠送彩金金额");
        }
        if (bonusConfig.getBonusAmount().compareTo(BigDecimal.ZERO) <= 0){
            throw new ServiceException("赠送彩金金额必须大于0");
        }
        if (bonusConfig.getCurrencyId() == null){
            throw new ServiceException("请选择赠送彩金币种");
        }
        return toAjax(bonusConfigService.updateBonusConfig(bonusConfig));
    }

    /**
     * 删除赠送彩金配置
     */
    @PreAuthorize("@ss.hasPermi('system:bonusConfig:remove')")
    @Log(title = "删除赠送彩金配置", businessType = BusinessType.DELETE,dict = BonusConfigLogDict.class)
    @RepeatSubmit
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(bonusConfigService.deleteBonusConfigByIds(ids));
    }
}
