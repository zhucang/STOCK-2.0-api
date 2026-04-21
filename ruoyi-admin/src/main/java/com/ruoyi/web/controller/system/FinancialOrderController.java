package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.logDict.FinancialOrderLogDict;
import com.ruoyi.system.domain.FinancialOrder;
import com.ruoyi.system.service.IFinancialOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 理财订单Controller
 * 
 * @author ruoyi
 * @date 2023-11-26
 * 日志已经优化
 */
@RestController
@RequestMapping("/system/financialOrder")
public class FinancialOrderController extends BaseController
{
    @Autowired
    private IFinancialOrderService financialOrderService;

    /**
     * 查询理财订单列表
     */
    @PreAuthorize("@ss.hasPermi('system:financialOrder:list')")
    @GetMapping("/list")
    public TableDataInfo list(FinancialOrder financialOrder)
    {
        startPage();
        startOrderBy("id desc");
        List<FinancialOrder> list = financialOrderService.selectFinancialOrderList(financialOrder);
        return getDataTable(list);
    }

    /**
     * 获取理财订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:financialOrder:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(financialOrderService.selectFinancialOrderById(id));
    }

    /**
     * 理财订单审核
     */
    @PreAuthorize("@ss.hasPermi('system:financialOrder:updateFinancialOrderStatus')")
    @Log(title = "理财订单审核", businessType = BusinessType.UPDATE,dict = FinancialOrderLogDict.class,
            saveParamNames = {"id","orderCode","orderStatus","financialOrder"})
    @RepeatSubmit
    @PostMapping(value = "updateFinancialOrderStatus")
    public AjaxResult updateFinancialOrderStatus(Long financialOrderId,Integer orderStatus) {
        if (financialOrderId == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (orderStatus == null){
            throw new ServiceException("请选择审核状态");
        }
        if (!orderStatus.equals(1) && !orderStatus.equals(3)){
            throw new ServiceException("审核状态错误");
        }
        return toAjax(financialOrderService.updateFinancialOrderStatus(financialOrderId, orderStatus));
    }

    /**
     * 理财产品人工赎回
     */
    @PreAuthorize("@ss.hasPermi('system:financialOrder:manualRedemption')")
    @Log(title = "理财产品人工赎回", businessType = BusinessType.UPDATE,dict = FinancialOrderLogDict.class,
            saveParamNames = {"id","orderCode","financialOrder","financialTime","alreadyInterestCount","breakContractRate","breakContractAmount","financialOrder"})
    @RepeatSubmit
    @PostMapping(value = "manualRedemption")
    public AjaxResult manualRedemption(Long id) {
        if (id == null){
            throw new ServiceException("请选择需要人工赎回的选项");
        }
        return toAjax(financialOrderService.manualRedemption(id));
    }

    /**
     * 理财产品人工结算
     */
    @PreAuthorize("@ss.hasPermi('system:financialOrder:manualSettlement')")
    @Log(title = "理财产品人工结算", businessType = BusinessType.UPDATE,dict = FinancialOrderLogDict.class,
            saveParamNames = {"id","orderCode","financialOrder","financialTime","alreadyInterestCount","financialOrder"})
    @RepeatSubmit
    @PostMapping(value = "manualSettlement")
    public AjaxResult manualSettlement(Long id) {
        if (id == null){
            throw new ServiceException("请选择需要人工结算的选项");
        }
        return toAjax(financialOrderService.manualSettlement(id));
    }

    /**
     * 理财数据总统计
     */
    @GetMapping(value = "financialOrderAnalysis")
    public AjaxResult financialOrderAnalysis(Long userId) {
        return AjaxResult.success(financialOrderService.financialOrderAnalysis(userId));
    }
}
