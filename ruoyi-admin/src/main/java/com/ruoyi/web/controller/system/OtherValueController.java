package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.logDict.OtherValueLogDict;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.OtherValue;
import com.ruoyi.system.service.IOtherValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 其他值Controller
 * 
 * @author ruoyi
 * @date 2024-03-27
 * 日志已优化
 */
@RestController
@RequestMapping("/system/otherValue")
public class OtherValueController extends BaseController
{
    @Autowired
    private IOtherValueService otherValueService;

    /**
     * 查询其他值列表
     */
    @GetMapping("/list")
    public TableDataInfo list(OtherValue otherValue)
    {
        startPage();
        //当前用户部门
        Long deptId = SecurityUtils.getDeptId();
        if (1L != deptId){
            otherValue.getParams().put("permission",1);
        }
        List<OtherValue> list = otherValueService.selectOtherValueList(otherValue);
        return getDataTable(list);
    }

    /**
     * 导出其他值列表
     */
    @PreAuthorize("@ss.hasPermi('system:otherValue:export')")
    @Log(title = "其他值", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OtherValue otherValue)
    {
        List<OtherValue> list = otherValueService.selectOtherValueList(otherValue);
        ExcelUtil<OtherValue> util = new ExcelUtil<OtherValue>(OtherValue.class);
        util.exportExcel(response, list, "其他值数据");
    }

    /**
     * 获取其他值详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:otherValue:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(otherValueService.selectOtherValueById(id));
    }

    /**
     * 新增其他值
     */
    @PreAuthorize("@ss.hasPermi('system:otherValue:add')")
    @Log(title = "其他值", businessType = BusinessType.INSERT,dict = OtherValueLogDict.class)
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody OtherValue otherValue)
    {
        if (StringUtils.isEmpty(otherValue.getOtherKey())){
            return AjaxResult.error("请输入key");
        }
        if (StringUtils.isEmpty(otherValue.getOtherValue())){
            return AjaxResult.error("请输入值");
        }
        return toAjax(otherValueService.insertOtherValue(otherValue));
    }

    /**
     * 修改其他值
     */
    @PreAuthorize("@ss.hasPermi('system:otherValue:edit')")
    @Log(title = "其他值", businessType = BusinessType.UPDATE,dict = OtherValueLogDict.class)
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody OtherValue otherValue)
    {
        if (otherValue.getId() == null){
            return AjaxResult.error("请选择需要修改的选项");
        }
        if (StringUtils.isEmpty(otherValue.getOtherKey())){
            return AjaxResult.error("请输入key");
        }
        if (StringUtils.isEmpty(otherValue.getOtherValue())){
            return AjaxResult.error("请输入值");
        }
        return toAjax(otherValueService.updateOtherValue(otherValue));
    }

    /**
     * 删除其他值
     */
    @PreAuthorize("@ss.hasPermi('system:otherValue:remove')")
    @Log(title = "其他值", businessType = BusinessType.DELETE,dict = OtherValueLogDict.class)
    @RepeatSubmit
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(otherValueService.deleteOtherValueByIds(ids));
    }
}
