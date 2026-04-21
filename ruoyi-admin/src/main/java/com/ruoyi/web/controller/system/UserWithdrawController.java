package com.ruoyi.web.controller.system;

import cn.hutool.core.date.DateUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.logDict.UserWithdrawLogDict;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.PageUtils;
import com.ruoyi.system.domain.UserWithdraw;
import com.ruoyi.system.service.IUserWithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 提现记录Controller
 * 
 * @author ruoyi
 * @date 2023-10-30
 */
@RestController
@RequestMapping("/system/userWithdraw")
public class UserWithdrawController extends BaseController
{
    @Autowired
    private IUserWithdrawService userWithdrawService;

    /**
     * 查询提现记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:userWithdraw:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserWithdraw userWithdraw)
    {
        startPage();
        PageUtils.getLocalPage().setUnsafeOrderBy("field(`withdraw_status`,0,1,2,3),id desc");
        List<UserWithdraw> list = userWithdrawService.selectUserWithdrawList(userWithdraw);
        userWithdrawService.fillOtherInfo(list);
        TableDataInfo dataTable = getDataTable(list);
        //统计数据
        userWithdraw.setPageNum(null);
        userWithdraw.setPageSize(null);
        List<UserWithdraw> statisticalData = userWithdrawService.getStatisticalData(userWithdraw);
        dataTable.getMapData().put("statisticalData",statisticalData);
        return dataTable;
    }

    /**
     * 查询提现记录列表
     */
    @GetMapping("/listNoPreAuthorize")
    public TableDataInfo listNoPreAuthorize(UserWithdraw userWithdraw)
    {
        startPage();
        startOrderBy("id desc");
        //如果只查看上月
        String isLastMonth = String.valueOf(userWithdraw.getParams().get("isLastMonth"));
        if (isLastMonth != null && isLastMonth.equals("1")){
            userWithdraw.setStartTime(DateUtil.date(DateUtils.getLastMonthStartTime()));
            userWithdraw.setEndTime(DateUtil.date(DateUtils.getLastMonthEndTime()));
        }
        userWithdraw.getParams().put("currencyStatus",0);
        List<UserWithdraw> list = userWithdrawService.selectUserWithdrawList(userWithdraw);
        return getDataTable(list);
    }

    /**
     * 获取提现记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:userWithdraw:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(userWithdrawService.selectUserWithdrawById(id));
    }


    /**
     * 提现订单审核
     */
    @PreAuthorize("@ss.hasPermi('system:userWithdraw:updateWithdrawOrderStatus')")
    @Log(title = "提现订单审核", businessType = BusinessType.UPDATE,dict = UserWithdrawLogDict.class,
            saveParamNames = {"id","orderCode","withdrawMsg","remark","withdrawStatus","withdrawAmount","receivedAmount","receiptAccountInfo","withdrawFee","currencyId","currencyName","withdrawType"})
    @RepeatSubmit
    @PostMapping(value = "updateWithdrawOrderStatus")
    public AjaxResult updateWithdrawOrderStatus(Long userWithdrawId,Integer orderStatus,String withdrawMsg,String remark) {
        if (userWithdrawId == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (orderStatus == null){
            throw new ServiceException("请选择审核状态");
        }
        if (!orderStatus.equals(1) && !orderStatus.equals(2)){
            throw new ServiceException("审核状态错误");
        }
        return toAjax(userWithdrawService.updateWithdrawOrderStatus(userWithdrawId,orderStatus,withdrawMsg,remark));
    }

    /**
     * 修改提现订单免客损状态
     */
    @PreAuthorize("@ss.hasPermi('system:userWithdraw:updateStatisticalReport')")
    @Log(title = "修改提现订单免客损状态", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping(value = "updateStatisticalReport")
    public AjaxResult updateStatisticalReport(Long userWithdrawId,Integer statisticalReport) {
        if (userWithdrawId == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (statisticalReport == null){
            throw new ServiceException("请选择是否免客损状态");
        }
        if (!statisticalReport.equals(0) && !statisticalReport.equals(1)){
            throw new ServiceException("审核状态错误");
        }
        return toAjax(userWithdrawService.updateStatisticalReport(userWithdrawId,statisticalReport));
    }


    /**
     * 修改提现收款账户信息
     */
    @PreAuthorize("@ss.hasPermi('system:userWithdraw:updateReceiptAccountInfo')")
    @Log(title = "修改提现收款账户信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping(value = "/updateReceiptAccountInfo")
    public AjaxResult updateWithdrawAddress(@RequestBody UserWithdraw userWithdraw) {
        if (userWithdraw.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        UserWithdraw vo = new UserWithdraw();
        vo.setId(userWithdraw.getId());
        vo.setReceiptAccountInfo(userWithdraw.getReceiptAccountInfo());
        return toAjax(userWithdrawService.updateUserWithdraw(vo));
    }

    /**
     * 修改订单备注
     */
    @PreAuthorize("@ss.hasPermi('system:userWithdraw:updateOrderRemark')")
    @Log(title = "修改订单备注", businessType = BusinessType.UPDATE,dict = UserWithdrawLogDict.class,
            saveParamNames = {"id","orderCode","remark"})
    @RepeatSubmit
    @PostMapping(value = "updateOrderRemark")
    public AjaxResult updateRechargeRemark(Long userWithdrawId, String remark) {
        if (userWithdrawId == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        return userWithdrawService.updateOrderRemark(userWithdrawId, remark);
    }

}
