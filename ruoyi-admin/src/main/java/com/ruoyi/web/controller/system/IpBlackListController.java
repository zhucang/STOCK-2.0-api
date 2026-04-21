package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.IpBlackList;
import com.ruoyi.system.service.IIpBlackListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ip黑名单Controller
 * 
 * @author ruoyi
 * @date 2023-11-03
 */
@RestController
@RequestMapping("/system/ipBlackList")
public class IpBlackListController extends BaseController
{
    @Autowired
    private IIpBlackListService ipBlackListService;

    /**
     * 查询ip黑名单列表
     */
    @PreAuthorize("@ss.hasPermi('system:ipBlackList:list')")
    @GetMapping("/list")
    public TableDataInfo list(IpBlackList ipBlackList)
    {
        startPage();
        startOrderBy("id desc");
        List<IpBlackList> list = ipBlackListService.selectIpBlackListList(ipBlackList);
        return getDataTable(list);
    }

    /**
     * 获取ip黑名单详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:ipBlackList:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(ipBlackListService.selectIpBlackListById(id));
    }

    /**
     * 新增ip黑名单
     */
    @PreAuthorize("@ss.hasPermi('system:ipBlackList:add')")
    @Log(title = "新增ip黑名单", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody IpBlackList ipBlackList)
    {
        if (StringUtils.isEmpty(ipBlackList.getIpAddress())){
            return AjaxResult.error("请输入ip");
        }
        return toAjax(ipBlackListService.insertIpBlackList(ipBlackList));
    }

    /**
     * 修改ip黑名单
     */
    @PreAuthorize("@ss.hasPermi('system:ipBlackList:edit')")
    @Log(title = "修改ip黑名单", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody IpBlackList ipBlackList)
    {
        if (ipBlackList.getId() == null){
            return AjaxResult.error("请选择需要修改的选项");
        }
        if (StringUtils.isEmpty(ipBlackList.getIpAddress())){
            return AjaxResult.error("请输入ip");
        }
        return toAjax(ipBlackListService.updateIpBlackList(ipBlackList));
    }

    /**
     * 删除ip黑名单
     */
    @PreAuthorize("@ss.hasPermi('system:ipBlackList:remove')")
    @Log(title = "删除ip黑名单", businessType = BusinessType.DELETE)
    @RepeatSubmit
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(ipBlackListService.deleteIpBlackListByIds(ids));
    }
}
