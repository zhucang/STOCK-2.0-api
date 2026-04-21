package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.TrumpetInfo;
import com.ruoyi.system.service.ITrumpetInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 喇叭信息Controller
 * 
 * @author ruoyi
 * @date 2023-11-04
 */
@RestController
@RequestMapping("/system/trumpetInfo")
public class TrumpetInfoController extends BaseController
{
    @Autowired
    private ITrumpetInfoService trumpetInfoService;

    /**
     * 查询喇叭信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:trumpetInfo:list')")
    @GetMapping("/list")
    public TableDataInfo list(TrumpetInfo trumpetInfo)
    {
        startPage();
        startOrderBy("id desc");
        List<TrumpetInfo> list = trumpetInfoService.selectTrumpetInfoList(trumpetInfo);
        return getDataTable(list);
    }

    /**
     * 获取喇叭信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:trumpetInfo:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(trumpetInfoService.selectTrumpetInfoById(id));
    }

    /**
     * 新增喇叭信息
     */
    @PreAuthorize("@ss.hasPermi('system:trumpetInfo:add')")
    @Log(title = "新增喇叭信息", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody TrumpetInfo trumpetInfo)
    {
        if (StringUtils.isEmpty(trumpetInfo.getTrumpetTitle())){
            return AjaxResult.error("请输入喇叭标题");
        }
        if (StringUtils.isEmpty(trumpetInfo.getTrumpetContent())){
            return AjaxResult.error("请输入喇叭内容");
        }
        return toAjax(trumpetInfoService.insertTrumpetInfo(trumpetInfo));
    }

    /**
     * 修改喇叭信息
     */
    @PreAuthorize("@ss.hasPermi('system:trumpetInfo:edit')")
    @Log(title = "修改喇叭信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody TrumpetInfo trumpetInfo)
    {
        if (trumpetInfo.getId() == null){
            return AjaxResult.error("请选择需要修改的选项");
        }
        if (StringUtils.isEmpty(trumpetInfo.getTrumpetTitle())){
            return AjaxResult.error("请输入喇叭标题");
        }
        if (StringUtils.isEmpty(trumpetInfo.getTrumpetContent())){
            return AjaxResult.error("请输入喇叭内容");
        }
        return toAjax(trumpetInfoService.updateTrumpetInfo(trumpetInfo));
    }

    /**
     * 删除喇叭信息
     */
    @PreAuthorize("@ss.hasPermi('system:trumpetInfo:remove')")
    @Log(title = "删除喇叭信息", businessType = BusinessType.DELETE)
    @RepeatSubmit
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(trumpetInfoService.deleteTrumpetInfoByIds(ids));
    }
}
