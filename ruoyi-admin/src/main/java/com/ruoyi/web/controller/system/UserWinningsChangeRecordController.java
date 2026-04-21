package com.ruoyi.web.controller.system;

import cn.hutool.core.date.DateUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.UserWinningsChangeRecord;
import com.ruoyi.system.service.IUserWinningsChangeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户彩金出入记录Controller
 * 
 * @author ruoyi
 * @date 2024-04-04
 */
@RestController
@RequestMapping("/system/userWinningsChangeRecord")
public class UserWinningsChangeRecordController extends BaseController
{
    @Autowired
    private IUserWinningsChangeRecordService userWinningsChangeRecordService;

    /**
     * 查询用户彩金出入记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:userWinningsChangeRecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserWinningsChangeRecord userWinningsChangeRecord)
    {
        startPage();
        startOrderBy("id desc");
        List<UserWinningsChangeRecord> list = userWinningsChangeRecordService.selectUserWinningsChangeRecordList(userWinningsChangeRecord);
        TableDataInfo dataTable = getDataTable(list);
        //统计数据
        userWinningsChangeRecord.setPageNum(null);
        userWinningsChangeRecord.setPageSize(null);
        userWinningsChangeRecord.setOrderType(0);
        List<UserWinningsChangeRecord> statisticalData = userWinningsChangeRecordService.getStatisticalData(userWinningsChangeRecord);
        dataTable.getMapData().put("winnings1",statisticalData);
        userWinningsChangeRecord.setOrderType(1);
        statisticalData = userWinningsChangeRecordService.getStatisticalData(userWinningsChangeRecord);
        dataTable.getMapData().put("winnings2",statisticalData);
        userWinningsChangeRecord.setOrderType(2);
        statisticalData = userWinningsChangeRecordService.getStatisticalData(userWinningsChangeRecord);
        dataTable.getMapData().put("winnings3",statisticalData);
        userWinningsChangeRecord.setOrderType(3);
        statisticalData = userWinningsChangeRecordService.getStatisticalData(userWinningsChangeRecord);
        dataTable.getMapData().put("winnings4",statisticalData);
        userWinningsChangeRecord.setOrderType(4);
        statisticalData = userWinningsChangeRecordService.getStatisticalData(userWinningsChangeRecord);
        dataTable.getMapData().put("winnings5",statisticalData);
        return dataTable;
    }

    /**
     * 查询用户彩金出入记录列表
     */
    @GetMapping("/listNoPreAuthorize")
    public TableDataInfo listNoPreAuthorize(UserWinningsChangeRecord userWinningsChangeRecord)
    {
        startPage();
        startOrderBy("id desc");
        //如果只查看上月
        String isLastMonth = String.valueOf(userWinningsChangeRecord.getParams().get("isLastMonth"));
        if (isLastMonth != null && isLastMonth.equals("1")){
            userWinningsChangeRecord.setStartTime(DateUtil.date(DateUtils.getLastMonthStartTime()));
            userWinningsChangeRecord.setEndTime(DateUtil.date(DateUtils.getLastMonthEndTime()));
        }
        userWinningsChangeRecord.getParams().put("currencyStatus",0);
        List<UserWinningsChangeRecord> list = userWinningsChangeRecordService.selectUserWinningsChangeRecordList(userWinningsChangeRecord);
        return getDataTable(list);
    }

    /**
     * 导出用户彩金出入记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:userWinningsChangeRecord:export')")
    @Log(title = "导出用户彩金出入记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserWinningsChangeRecord userWinningsChangeRecord)
    {
        List<UserWinningsChangeRecord> list = userWinningsChangeRecordService.selectUserWinningsChangeRecordList(userWinningsChangeRecord);
        ExcelUtil<UserWinningsChangeRecord> util = new ExcelUtil<UserWinningsChangeRecord>(UserWinningsChangeRecord.class);
        util.exportExcel(response, list, "用户彩金出入记录数据");
    }

    /**
     * 获取用户彩金出入记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:userWinningsChangeRecord:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(userWinningsChangeRecordService.selectUserWinningsChangeRecordById(id));
    }

//    /**
//     * 新增用户彩金出入记录
//     */
//    @PreAuthorize("@ss.hasPermi('system:userWinningsChangeRecord:add')")
//    @Log(title = "用户彩金出入记录", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody UserWinningsChangeRecord userWinningsChangeRecord)
//    {
//        return toAjax(userWinningsChangeRecordService.insertUserWinningsChangeRecord(userWinningsChangeRecord));
//    }
//
//    /**
//     * 修改用户彩金出入记录
//     */
//    @PreAuthorize("@ss.hasPermi('system:userWinningsChangeRecord:edit')")
//    @Log(title = "用户彩金出入记录", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody UserWinningsChangeRecord userWinningsChangeRecord)
//    {
//        return toAjax(userWinningsChangeRecordService.updateUserWinningsChangeRecord(userWinningsChangeRecord));
//    }
//
//    /**
//     * 删除用户彩金出入记录
//     */
//    @PreAuthorize("@ss.hasPermi('system:userWinningsChangeRecord:remove')")
//    @Log(title = "用户彩金出入记录", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        return toAjax(userWinningsChangeRecordService.deleteUserWinningsChangeRecordByIds(ids));
//    }
}
