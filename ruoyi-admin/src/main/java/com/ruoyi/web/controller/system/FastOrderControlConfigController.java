package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.logDict.FastOrderControlConfigLogDict;
import com.ruoyi.system.domain.FastOrderControlConfig;
import com.ruoyi.system.service.IFastOrderControlConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 极速交易控制配置Controller
 * 
 * @author ruoyi
 * @date 2023-11-25
 * 日志已优化
 */
@RestController
@RequestMapping("/system/fastOrderControlConfig")
public class FastOrderControlConfigController extends BaseController
{
    @Autowired
    private IFastOrderControlConfigService fastOrderControlConfigService;

    /**
     * 查询极速交易控制配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:fastOrderControlConfig:list')")
    @GetMapping("/list")
    public TableDataInfo list(FastOrderControlConfig fastOrderControlConfig)
    {
        startPage();
        List<FastOrderControlConfig> list = fastOrderControlConfigService.selectFastOrderControlConfigList(fastOrderControlConfig);
        return getDataTable(list);
    }

    /**
     * 获取极速交易控制配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:fastOrderControlConfig:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(fastOrderControlConfigService.selectFastOrderControlConfigById(id));
    }

    /**
     * 新增极速交易控制配置
     */
    @PreAuthorize("@ss.hasPermi('system:fastOrderControlConfig:add')")
    @Log(title = "新增极速交易控制配置", businessType = BusinessType.INSERT,dict = FastOrderControlConfigLogDict.class)
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody FastOrderControlConfig fastOrderControlConfig)
    {
        if (fastOrderControlConfig.getProductCode() == null){
            throw new ServiceException("请选择需要控制的产品");
        }
        if (fastOrderControlConfig.getProductType() == null){
            throw new ServiceException("请选择产品类型");
        }
        if (fastOrderControlConfig.getBeginTime() == null){
            throw new ServiceException("请输入开始时间");
        }
        if (fastOrderControlConfig.getFinishTime() == null){
            throw new ServiceException("请输入结束时间");
        }
        if (fastOrderControlConfig.getFinishTime().before(fastOrderControlConfig.getBeginTime())){
            throw new ServiceException("结束时间不能早于开始时间");
        }
        if (fastOrderControlConfig.getTradeDirect() == null){
            throw new ServiceException("请选择交易方向");
        }
        if (fastOrderControlConfig.getWinOrLose() == null){
            throw new ServiceException("请选择输赢控制");
        }
        return toAjax(fastOrderControlConfigService.insertFastOrderControlConfig(fastOrderControlConfig));
    }

    /**
     * 修改极速交易控制配置
     */
    @PreAuthorize("@ss.hasPermi('system:fastOrderControlConfig:edit')")
    @Log(title = "修改极速交易控制配置", businessType = BusinessType.UPDATE,dict = FastOrderControlConfigLogDict.class)
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody FastOrderControlConfig fastOrderControlConfig)
    {
        if (fastOrderControlConfig.getId() == null){
            return AjaxResult.success("请选择需要修改的选项");
        }
        if (fastOrderControlConfig.getProductCode() == null){
            throw new ServiceException("请选择需要控制的产品");
        }
        if (fastOrderControlConfig.getProductType() == null){
            throw new ServiceException("请选择产品类型");
        }
        if (fastOrderControlConfig.getBeginTime() == null){
            throw new ServiceException("请输入开始时间");
        }
        if (fastOrderControlConfig.getFinishTime() == null){
            throw new ServiceException("请输入结束时间");
        }
        if (fastOrderControlConfig.getFinishTime().before(fastOrderControlConfig.getBeginTime())){
            throw new ServiceException("结束时间不能早于开始时间");
        }
        if (fastOrderControlConfig.getTradeDirect() == null){
            throw new ServiceException("请选择交易方向");
        }
        if (fastOrderControlConfig.getWinOrLose() == null){
            throw new ServiceException("请选择输赢控制");
        }
        return toAjax(fastOrderControlConfigService.updateFastOrderControlConfig(fastOrderControlConfig));
    }

    /**
     * 删除极速交易控制配置
     */
    @PreAuthorize("@ss.hasPermi('system:fastOrderControlConfig:remove')")
    @Log(title = "删除极速交易控制配置", businessType = BusinessType.DELETE,dict = FastOrderControlConfigLogDict.class)
    @RepeatSubmit
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(fastOrderControlConfigService.deleteFastOrderControlConfigByIds(ids));
    }
}
