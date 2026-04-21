package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.UserProductOption;
import com.ruoyi.system.service.IUserProductOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户产品自选关联信息Controller
 * 
 * @author ruoyi
 * @date 2023-11-01
 */
@RestController
@RequestMapping("/system/userProductOption")
public class UserProductOptionController extends BaseController
{
    @Autowired
    private IUserProductOptionService userProductOptionService;

    /**
     * 查询用户产品自选关联信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:userProductOption:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserProductOption userProductOption)
    {
        startPage();
        List<UserProductOption> list = userProductOptionService.selectUserProductOptionList(userProductOption);
        return getDataTable(list);
    }

    /**
     * 导出用户产品自选关联信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:userProductOption:export')")
    @Log(title = "用户产品自选关联信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserProductOption userProductOption)
    {
        List<UserProductOption> list = userProductOptionService.selectUserProductOptionList(userProductOption);
        ExcelUtil<UserProductOption> util = new ExcelUtil<UserProductOption>(UserProductOption.class);
        util.exportExcel(response, list, "用户产品自选关联信息数据");
    }

    /**
     * 获取用户产品自选关联信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:userProductOption:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(userProductOptionService.selectUserProductOptionById(id));
    }

    /**
     * 新增用户产品自选关联信息
     */
    @PreAuthorize("@ss.hasPermi('system:userProductOption:add')")
    @Log(title = "新增用户产品自选关联信息", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody UserProductOption userProductOption)
    {
        return toAjax(userProductOptionService.insertUserProductOption(userProductOption));
    }

    /**
     * 修改用户产品自选关联信息
     */
    @PreAuthorize("@ss.hasPermi('system:userProductOption:edit')")
    @Log(title = "修改用户产品自选关联信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody UserProductOption userProductOption)
    {
        return toAjax(userProductOptionService.updateUserProductOption(userProductOption));
    }

    /**
     * 删除用户产品自选关联信息
     */
    @PreAuthorize("@ss.hasPermi('system:userProductOption:remove')")
    @Log(title = "删除用户产品自选关联信息", businessType = BusinessType.DELETE)
    @RepeatSubmit
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(userProductOptionService.deleteUserProductOptionByIds(ids));
    }
}
