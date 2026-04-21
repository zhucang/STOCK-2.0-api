package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.PageUtils;
import com.ruoyi.system.domain.UserLoanRepaymentOrder;
import com.ruoyi.system.service.IUserLoanRepaymentOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户贷款还款订单Controller
 * 
 * @author ruoyi
 * @date 2024-05-28
 */
@RestController
@RequestMapping("/system/userLoanRepaymentOrder")
public class UserLoanRepaymentOrderController extends BaseController
{
    @Autowired
    private IUserLoanRepaymentOrderService userLoanRepaymentOrderService;

    /**
     * 查询用户贷款还款订单列表
     */
    @PreAuthorize("@ss.hasPermi('system:userLoanRepaymentOrder:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserLoanRepaymentOrder userLoanRepaymentOrder)
    {
        startPage();
        PageUtils.getLocalPage().setUnsafeOrderBy("field(a.`order_status`,0,1,2),id desc");
        List<UserLoanRepaymentOrder> list = userLoanRepaymentOrderService.selectUserLoanRepaymentOrderList(userLoanRepaymentOrder);
        userLoanRepaymentOrderService.fillOtherInfo(list);
        TableDataInfo dataTable = getDataTable(list);
        //统计数据
        userLoanRepaymentOrder.setPageNum(null);
        userLoanRepaymentOrder.setPageSize(null);
        List<UserLoanRepaymentOrder> statisticalData = userLoanRepaymentOrderService.getStatisticalData(userLoanRepaymentOrder);
        dataTable.getMapData().put("statisticalData",statisticalData);
        return dataTable;
    }

    /**
     * 获取用户贷款还款订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:userLoanRepaymentOrder:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(userLoanRepaymentOrderService.selectUserLoanRepaymentOrderById(id));
    }


    /**
     * 贷款还款订单审核
     */
    @PreAuthorize("@ss.hasPermi('system:userLoanRepaymentOrder:updateLoanRepaymentOrderStatus')")
    @Log(title = "贷款还款订单审核", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping(value = "updateLoanRepaymentOrderStatus")
    public AjaxResult updateLoanRepaymentOrderStatus(Long loanRepaymentOrderId,Integer orderStatus,String message,String remark) {
        if (loanRepaymentOrderId == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (orderStatus == null){
            throw new ServiceException("请选择审核状态");
        }
        if (!orderStatus.equals(0) && !orderStatus.equals(1) && !orderStatus.equals(2) && !orderStatus.equals(3)){
            throw new ServiceException("审核状态错误");
        }
        return toAjax(userLoanRepaymentOrderService.updateLoanRepaymentOrderStatus(loanRepaymentOrderId, orderStatus,message,remark));
    }
}
