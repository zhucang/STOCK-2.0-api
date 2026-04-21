package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.UserDmAmountChangeRecord;
import com.ruoyi.system.service.IUserDmAmountChangeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户打码量变更记录Controller
 * 
 * @author ruoyi
 * @date 2024-05-24
 */
@RestController
@RequestMapping("/system/userDmAmountChangeRecord")
public class UserDmAmountChangeRecordController extends BaseController
{
    @Autowired
    private IUserDmAmountChangeRecordService userDmAmountChangeRecordService;

    /**
     * 查询用户打码量变更记录列表
     */
    @GetMapping("/list")
    public TableDataInfo list(UserDmAmountChangeRecord userDmAmountChangeRecord)
    {
        startPage();
        startOrderBy("id desc");
        List<UserDmAmountChangeRecord> list = userDmAmountChangeRecordService.selectUserDmAmountChangeRecordList(userDmAmountChangeRecord);
        return getDataTable(list);
    }

    /**
     * 导出用户打码量变更记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:userDmAmountChangeRecord:export')")
    @Log(title = "用户打码量变更记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserDmAmountChangeRecord userDmAmountChangeRecord)
    {
        List<UserDmAmountChangeRecord> list = userDmAmountChangeRecordService.selectUserDmAmountChangeRecordList(userDmAmountChangeRecord);
        ExcelUtil<UserDmAmountChangeRecord> util = new ExcelUtil<UserDmAmountChangeRecord>(UserDmAmountChangeRecord.class);
        util.exportExcel(response, list, "用户打码量变更记录数据");
    }

    /**
     * 获取用户打码量变更记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:userDmAmountChangeRecord:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(userDmAmountChangeRecordService.selectUserDmAmountChangeRecordById(id));
    }

    /**
     * 新增用户打码量变更记录
     */
    @PreAuthorize("@ss.hasPermi('system:userDmAmountChangeRecord:add')")
    @Log(title = "用户打码量变更记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserDmAmountChangeRecord userDmAmountChangeRecord)
    {
        return toAjax(userDmAmountChangeRecordService.insertUserDmAmountChangeRecord(userDmAmountChangeRecord));
    }

    /**
     * 修改用户打码量变更记录
     */
    @PreAuthorize("@ss.hasPermi('system:userDmAmountChangeRecord:edit')")
    @Log(title = "用户打码量变更记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserDmAmountChangeRecord userDmAmountChangeRecord)
    {
        return toAjax(userDmAmountChangeRecordService.updateUserDmAmountChangeRecord(userDmAmountChangeRecord));
    }

    /**
     * 删除用户打码量变更记录
     */
    @PreAuthorize("@ss.hasPermi('system:userDmAmountChangeRecord:remove')")
    @Log(title = "用户打码量变更记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(userDmAmountChangeRecordService.deleteUserDmAmountChangeRecordByIds(ids));
    }
}
