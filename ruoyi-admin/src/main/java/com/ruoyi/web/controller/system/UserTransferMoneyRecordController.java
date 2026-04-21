package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.UserTransferMoneyRecord;
import com.ruoyi.system.service.IUserTransferMoneyRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户转账记录Controller
 * 
 * @author ruoyi
 * @date 2025-05-14
 */
@RestController
@RequestMapping("/system/userTransferMoneyRecord")
public class UserTransferMoneyRecordController extends BaseController
{
    @Autowired
    private IUserTransferMoneyRecordService userTransferMoneyRecordService;

    /**
     * 查询用户转账记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:userTransferMoneyRecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserTransferMoneyRecord userTransferMoneyRecord)
    {
        startPage();
        startOrderBy("user_transfer_money_record_id desc");
        List<UserTransferMoneyRecord> list = userTransferMoneyRecordService.selectUserTransferMoneyRecordList(userTransferMoneyRecord);
        return getDataTable(list);
    }

    /**
     * 导出用户转账记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:userTransferMoneyRecord:export')")
    @Log(title = "用户转账记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserTransferMoneyRecord userTransferMoneyRecord)
    {
        List<UserTransferMoneyRecord> list = userTransferMoneyRecordService.selectUserTransferMoneyRecordList(userTransferMoneyRecord);
        ExcelUtil<UserTransferMoneyRecord> util = new ExcelUtil<UserTransferMoneyRecord>(UserTransferMoneyRecord.class);
        util.exportExcel(response, list, "用户转账记录数据");
    }

    /**
     * 获取用户转账记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:userTransferMoneyRecord:query')")
    @GetMapping(value = "/{userTransferMoneyRecordId}")
    public AjaxResult getInfo(@PathVariable("userTransferMoneyRecordId") Long userTransferMoneyRecordId)
    {
        return success(userTransferMoneyRecordService.selectUserTransferMoneyRecordByUserTransferMoneyRecordId(userTransferMoneyRecordId));
    }

    /**
     * 新增用户转账记录
     */
    @PreAuthorize("@ss.hasPermi('system:userTransferMoneyRecord:add')")
    @Log(title = "用户转账记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserTransferMoneyRecord userTransferMoneyRecord)
    {
        return toAjax(userTransferMoneyRecordService.insertUserTransferMoneyRecord(userTransferMoneyRecord));
    }

    /**
     * 修改用户转账记录
     */
    @PreAuthorize("@ss.hasPermi('system:userTransferMoneyRecord:edit')")
    @Log(title = "用户转账记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserTransferMoneyRecord userTransferMoneyRecord)
    {
        return toAjax(userTransferMoneyRecordService.updateUserTransferMoneyRecord(userTransferMoneyRecord));
    }

    /**
     * 删除用户转账记录
     */
    @PreAuthorize("@ss.hasPermi('system:userTransferMoneyRecord:remove')")
    @Log(title = "用户转账记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{userTransferMoneyRecordIds}")
    public AjaxResult remove(@PathVariable Long[] userTransferMoneyRecordIds)
    {
        return toAjax(userTransferMoneyRecordService.deleteUserTransferMoneyRecordByUserTransferMoneyRecordIds(userTransferMoneyRecordIds));
    }
}
