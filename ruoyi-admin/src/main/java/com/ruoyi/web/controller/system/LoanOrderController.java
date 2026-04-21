package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.logDict.LoanOrderLogDict;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.LoanOrder;
import com.ruoyi.system.service.ILoanOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

/**
 * 贷款订单Controller
 * 
 * @author ruoyi
 * @date 2024-05-22
 */
@RestController
@RequestMapping("/system/loanOrder")
public class LoanOrderController extends BaseController
{
    @Autowired
    private ILoanOrderService loanOrderService;

    /**
     * 查询贷款订单列表
     */
    @PreAuthorize("@ss.hasPermi('system:loanOrder:list')")
    @GetMapping("/list")
    public TableDataInfo list(LoanOrder loanOrder)
    {
        startPage();
        startOrderBy("id desc");
        List<LoanOrder> list = loanOrderService.selectLoanOrderList(loanOrder);
        loanOrderService.fillOtherInfo(list);
        TableDataInfo dataTable = getDataTable(list);
        //统计数据
        loanOrder.setPageNum(null);
        loanOrder.setPageSize(null);
        loanOrder.getParams().put("statisticalData",0);
        loanOrder.setStatisticalReport(0);
        List<LoanOrder> statisticalData = loanOrderService.getStatisticalData(loanOrder);
        loanOrder.setStatisticalReport(1);
        List<LoanOrder> noStatisticalData = loanOrderService.getStatisticalData(loanOrder);
        dataTable.getMapData().put("statisticalData",statisticalData);
        dataTable.getMapData().put("noStatisticalData",noStatisticalData);
        return dataTable;
    }

    /**
     * 查询贷款订单列表
     */
    @GetMapping("/listNoPreAuthorize")
    public TableDataInfo listNoPreAuthorize(LoanOrder loanOrder)
    {
        startPage();
        startOrderBy("id desc");
        List<LoanOrder> list = loanOrderService.selectLoanOrderList(loanOrder);
        return getDataTable(list);
    }

    /**
     * 导出贷款订单列表
     */
    @PreAuthorize("@ss.hasPermi('system:loanOrder:export')")
    @Log(title = "贷款订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LoanOrder loanOrder)
    {
        List<LoanOrder> list = loanOrderService.selectLoanOrderList(loanOrder);
        ExcelUtil<LoanOrder> util = new ExcelUtil<LoanOrder>(LoanOrder.class);
        util.exportExcel(response, list, "贷款订单数据");
    }

    /**
     * 获取贷款订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:loanOrder:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(loanOrderService.selectLoanOrderById(id));
    }

//    /**
//     * 新增贷款订单
//     */
//    @PreAuthorize("@ss.hasPermi('system:loanOrder:add')")
//    @Log(title = "贷款订单", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody LoanOrder loanOrder)
//    {
//        return toAjax(loanOrderService.insertLoanOrder(loanOrder));
//    }

//    /**
//     * 修改贷款订单
//     */
//    @PreAuthorize("@ss.hasPermi('system:loanOrder:edit')")
//    @Log(title = "贷款订单", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody LoanOrder loanOrder)
//    {
//        return toAjax(loanOrderService.updateLoanOrder(loanOrder));
//    }

//    /**
//     * 删除贷款订单
//     */
//    @PreAuthorize("@ss.hasPermi('system:loanOrder:remove')")
//    @Log(title = "贷款订单", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        return toAjax(loanOrderService.deleteLoanOrderByIds(ids));
//    }

    /**
     * 贷款订单审核
     */
    @PreAuthorize("@ss.hasPermi('system:loanOrder:updateLoanOrderStatus')")
    @Log(title = "贷款订单审核", businessType = BusinessType.UPDATE,dict = LoanOrderLogDict.class)
    @RepeatSubmit
    @PostMapping(value = "updateLoanOrderStatus")
    public AjaxResult updateLoanOrderStatus(Long loanOrderId, Integer orderStatus, BigDecimal realLoanAmount, BigDecimal loanDailyRate, String loanMsg,String remark) {
        if (loanOrderId == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (orderStatus == null){
            throw new ServiceException("请选择审核状态");
        }
        if (!orderStatus.equals(1) && !orderStatus.equals(3)){
            throw new ServiceException("审核状态错误");
        }
        return toAjax(loanOrderService.updateLoanOrderStatus(loanOrderId, orderStatus,realLoanAmount,loanDailyRate,loanMsg,remark));
    }

    /**
     * 修改贷款订单免客损状态
     */
    @PreAuthorize("@ss.hasPermi('system:loanOrder:updateStatisticalReport')")
    @Log(title = "修改贷款订单免客损状态", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping(value = "updateStatisticalReport")
    public AjaxResult updateStatisticalReport(Long loanOrderId,Integer statisticalReport) {
        if (loanOrderId == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (statisticalReport == null){
            throw new ServiceException("请选择是否免客损状态");
        }
        if (!statisticalReport.equals(0) && !statisticalReport.equals(1)){
            throw new ServiceException("状态错误");
        }
        return toAjax(loanOrderService.updateStatisticalReport(loanOrderId,statisticalReport));
    }

    /**
     * 贷款订单后台人工结算
     */
    @PreAuthorize("@ss.hasPermi('system:loanOrder:loanRepayment')")
    @Log(title = "贷款订单后台人工结算", businessType = BusinessType.UPDATE,dict = LoanOrderLogDict.class)
    @RepeatSubmit
    @PostMapping(value = "loanRepayment")
    public AjaxResult loanRepayment(Long loanOrderId, Integer settlementType) {
        if (loanOrderId == null){
            throw new ServiceException("请选择要还款的贷款订单");
        }
        if (settlementType == null){
            throw new ServiceException("请选择结算类型");
        }
        if (!settlementType.equals(0) && !settlementType.equals(1)){
            throw new ServiceException("结算类型错误");
        }
        return toAjax(loanOrderService.loanRepayment(loanOrderId,settlementType));
    }
}