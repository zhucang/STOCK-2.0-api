package com.ruoyi.web.controller.system;

import cn.hutool.core.date.DateUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.UserPointChangeRecord;
import com.ruoyi.system.service.IUserPointChangeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户上分下分记录Controller
 * 
 * @author ruoyi
 * @date 2024-03-30
 */
@RestController
@RequestMapping("/system/userPointChangeRecord")
public class UserPointChangeRecordController extends BaseController
{
    @Autowired
    private IUserPointChangeRecordService userPointChangeRecordService;

    /**
     * 查询用户上分下分记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:userPointChangeRecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserPointChangeRecord userPointChangeRecord)
    {
        startPage();
        startOrderBy("id desc");
        List<UserPointChangeRecord> list = userPointChangeRecordService.selectUserPointChangeRecordList(userPointChangeRecord);
        userPointChangeRecordService.fillOtherInfo(list);
        TableDataInfo dataTable = getDataTable(list);
        //统计数据
        userPointChangeRecord.setPageNum(null);
        userPointChangeRecord.setPageSize(null);
        userPointChangeRecord.setOrderType(0);
        List<UserPointChangeRecord> statisticalData = userPointChangeRecordService.getStatisticalData(userPointChangeRecord);
        dataTable.getMapData().put("upPointStatisticalData",statisticalData);
        userPointChangeRecord.setOrderType(1);
        statisticalData = userPointChangeRecordService.getStatisticalData(userPointChangeRecord);
        dataTable.getMapData().put("downPointStatisticalData",statisticalData);
        return dataTable;
    }

    /**
     * 查询用户上分下分记录列表
     */
    @GetMapping("/listNoPreAuthorize")
    public TableDataInfo listNoPreAuthorize(UserPointChangeRecord userPointChangeRecord)
    {
        startPage();
        startOrderBy("id desc");
        //如果只查看上月
        String isLastMonth = String.valueOf(userPointChangeRecord.getParams().get("isLastMonth"));
        if (isLastMonth != null && isLastMonth.equals("1")){
            userPointChangeRecord.setStartTime(DateUtil.date(DateUtils.getLastMonthStartTime()));
            userPointChangeRecord.setEndTime(DateUtil.date(DateUtils.getLastMonthEndTime()));
        }
        userPointChangeRecord.getParams().put("currencyStatus",0);
        List<UserPointChangeRecord> list = userPointChangeRecordService.selectUserPointChangeRecordList(userPointChangeRecord);
        return getDataTable(list);
    }

    /**
     * 导出用户上分下分记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:userPointChangeRecord:export')")
    @Log(title = "用户上分下分记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserPointChangeRecord userPointChangeRecord)
    {
        List<UserPointChangeRecord> list = userPointChangeRecordService.selectUserPointChangeRecordList(userPointChangeRecord);
        ExcelUtil<UserPointChangeRecord> util = new ExcelUtil<UserPointChangeRecord>(UserPointChangeRecord.class);
        util.exportExcel(response, list, "用户上分下分记录数据");
    }

    /**
     * 获取用户上分下分记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:userPointChangeRecord:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(userPointChangeRecordService.selectUserPointChangeRecordById(id));
    }

//    /**
//     * 新增用户上分下分记录
//     */
//    @PreAuthorize("@ss.hasPermi('system:userPointChangeRecord:add')")
//    @Log(title = "用户上分下分记录", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody UserPointChangeRecord userPointChangeRecord)
//    {
//        return toAjax(userPointChangeRecordService.insertUserPointChangeRecord(userPointChangeRecord));
//    }
//
//    /**
//     * 修改用户上分下分记录
//     */
//    @PreAuthorize("@ss.hasPermi('system:userPointChangeRecord:edit')")
//    @Log(title = "用户上分下分记录", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody UserPointChangeRecord userPointChangeRecord)
//    {
//        return toAjax(userPointChangeRecordService.updateUserPointChangeRecord(userPointChangeRecord));
//    }
//
//    /**
//     * 删除用户上分下分记录
//     */
//    @PreAuthorize("@ss.hasPermi('system:userPointChangeRecord:remove')")
//    @Log(title = "用户上分下分记录", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        return toAjax(userPointChangeRecordService.deleteUserPointChangeRecordByIds(ids));
//    }
}
