package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.logDict.FinancialOrderLogDict;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.StakingOrder;
import com.ruoyi.system.service.IStakingOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 质押订单Controller
 * 
 * @author ruoyi
 * @date 2025-07-17
 */
@RestController
@RequestMapping("/system/stakingOrder")
public class StakingOrderController extends BaseController
{
    @Autowired
    private IStakingOrderService stakingOrderService;

    /**
     * 查询质押订单列表
     */
    @PreAuthorize("@ss.hasPermi('system:stakingOrder:list')")
    @GetMapping("/list")
    public TableDataInfo list(StakingOrder stakingOrder)
    {
        startPage();
        startOrderBy("id desc");
        List<StakingOrder> list = stakingOrderService.selectStakingOrderList(stakingOrder);
        return getDataTable(list);
    }

    /**
     * 导出质押订单列表
     */
    @PreAuthorize("@ss.hasPermi('system:stakingOrder:export')")
    @Log(title = "质押订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StakingOrder stakingOrder)
    {
        List<StakingOrder> list = stakingOrderService.selectStakingOrderList(stakingOrder);
        ExcelUtil<StakingOrder> util = new ExcelUtil<StakingOrder>(StakingOrder.class);
        util.exportExcel(response, list, "质押订单数据");
    }

    /**
     * 获取质押订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:stakingOrder:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(stakingOrderService.selectStakingOrderById(id));
    }

    /**
     * 质押订单审核
     */
    @PreAuthorize("@ss.hasPermi('system:stakingOrder:updateStakingOrderStatus')")
    @Log(title = "质押订单审核", businessType = BusinessType.UPDATE,dict = FinancialOrderLogDict.class,
            saveParamNames = {"id","orderCode","orderStatus","stakingOrder"})
    @RepeatSubmit
    @PostMapping(value = "updateStakingOrderStatus")
    public AjaxResult updateStakingOrderStatus(Long stakingOrderId,Integer orderStatus) {
        if (stakingOrderId == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (orderStatus == null){
            throw new ServiceException("请选择审核状态");
        }
        if (!orderStatus.equals(1) && !orderStatus.equals(3)){
            throw new ServiceException("审核状态错误");
        }
        return toAjax(stakingOrderService.updateStakingOrderStatus(stakingOrderId, orderStatus));
    }
}
