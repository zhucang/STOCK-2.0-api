package com.ruoyi.web.controller.system;

import cn.hutool.core.date.DateUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.UserBillDetail;
import com.ruoyi.system.service.IUserBillDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 用户流水记录Controller
 * 
 * @author ruoyi
 * @date 2023-10-29
 */
@RestController
@RequestMapping("/system/userBillDetail")
public class UserBillDetailController extends BaseController
{
    @Autowired
    private IUserBillDetailService userBillDetailService;

    /**
     * 查询用户流水记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:userBillDetail:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserBillDetail userBillDetail)
    {
        startPage();
        startOrderBy("id desc");
        List<UserBillDetail> list = userBillDetailService.selectUserBillDetailList(userBillDetail);
        return getDataTable(list);
    }

    /**
     * 查询用户流水记录列表
     */
    @GetMapping("/listNoPreAuthorize")
    public TableDataInfo listNoPreAuthorize(UserBillDetail userBillDetail)
    {
        startPage();
        startOrderBy("id desc");
        //如果只查看上月
        String isLastMonth = String.valueOf(userBillDetail.getParams().get("isLastMonth"));
        if (isLastMonth != null && isLastMonth.equals("1")){
            userBillDetail.setStartTime(DateUtil.date(DateUtils.getLastMonthStartTime()));
            userBillDetail.setEndTime(DateUtil.date(DateUtils.getLastMonthEndTime()));
        }
        List<UserBillDetail> list = userBillDetailService.selectUserBillDetailList(userBillDetail);
        return getDataTable(list);
    }

    /**
     * 导出用户流水记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:userBillDetail:export')")
    @Log(title = "用户流水记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserBillDetail userBillDetail)
    {
        List<UserBillDetail> list = userBillDetailService.selectUserBillDetailList(userBillDetail);
        ExcelUtil<UserBillDetail> util = new ExcelUtil<UserBillDetail>(UserBillDetail.class);
        util.exportExcel(response, list, "用户流水记录数据");
    }

    /**
     * 获取用户流水记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:userBillDetail:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(userBillDetailService.selectUserBillDetailById(id));
    }
}
