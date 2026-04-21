package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.UserApplyPurchaseOrder;
import com.ruoyi.system.service.IUserApplyPurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

/**
 * 用户新股新币申购订单Controller
 * 
 * @author ruoyi
 * @date 2023-11-30
 */
@RestController
@RequestMapping("/system/userApplyPurchaseOrder")
public class UserApplyPurchaseOrderController extends BaseController
{
    @Autowired
    private IUserApplyPurchaseOrderService userApplyPurchaseOrderService;

    /**
     * 查询用户新股新币申购订单列表
     */
    @PreAuthorize("@ss.hasPermi('system:userApplyPurchaseOrder:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserApplyPurchaseOrder userApplyPurchaseOrder)
    {
        startPage();
        startOrderBy("id desc");
        List<UserApplyPurchaseOrder> list = userApplyPurchaseOrderService.selectUserApplyPurchaseOrderList(userApplyPurchaseOrder);
        return getDataTable(list);
    }

    /**
     * 导出用户新股新币申购订单列表
     */
    @PreAuthorize("@ss.hasPermi('system:userApplyPurchaseOrder:export')")
    @Log(title = "用户新股新币申购订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserApplyPurchaseOrder userApplyPurchaseOrder)
    {
        List<UserApplyPurchaseOrder> list = userApplyPurchaseOrderService.selectUserApplyPurchaseOrderList(userApplyPurchaseOrder);
        ExcelUtil<UserApplyPurchaseOrder> util = new ExcelUtil<UserApplyPurchaseOrder>(UserApplyPurchaseOrder.class);
        util.exportExcel(response, list, "用户新股新币申购订单数据");
    }

    /**
     * 获取用户新股新币申购订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:userApplyPurchaseOrder:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(userApplyPurchaseOrderService.selectUserApplyPurchaseOrderById(id));
    }

    /**
     * 新增用户新股新币申购订单
     */
    @PreAuthorize("@ss.hasPermi('system:userApplyPurchaseOrder:add')")
    @Log(title = "用户新股新币申购订单", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody UserApplyPurchaseOrder userApplyPurchaseOrder)
    {
        return toAjax(userApplyPurchaseOrderService.insertUserApplyPurchaseOrder(userApplyPurchaseOrder));
    }

    /**
     * 修改用户新股新币申购订单
     */
    @PreAuthorize("@ss.hasPermi('system:userApplyPurchaseOrder:edit')")
    @Log(title = "用户新股新币申购订单", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody UserApplyPurchaseOrder userApplyPurchaseOrder)
    {
        return toAjax(userApplyPurchaseOrderService.updateUserApplyPurchaseOrder(userApplyPurchaseOrder));
    }

    /**
     * 删除用户新股新币申购订单
     */
    @PreAuthorize("@ss.hasPermi('system:userApplyPurchaseOrder:remove')")
    @Log(title = "用户新股新币申购订单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(userApplyPurchaseOrderService.deleteUserApplyPurchaseOrderByIds(ids));
    }

    /**
     * 设置中签率
     * @param id
     * @param winningRate
     * @return
     */
    @PreAuthorize("@ss.hasPermi('system:userApplyPurchaseOrder:setWinningRate')")
    @Log(title = "设置中签率", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping("/setWinningRate")
    public AjaxResult setWinningRate(Long id, BigDecimal winningRate) {
        if (id == null){
            throw new ServiceException("请选择需要操作的选项");
        }
        if (winningRate == null){
            throw new ServiceException("请输入中签率");
        }
        if (winningRate.compareTo(BigDecimal.ZERO) == -1){
            throw new ServiceException("中签率不允许小于0");
        }
        if (winningRate.compareTo(BigDecimal.ONE) == 1){
            throw new ServiceException("中签率不允许大于1");
        }
        return userApplyPurchaseOrderService.setWinningRate(id,winningRate,null);
    }

    /**
     * 解锁用户申购订单
     */
    @PreAuthorize("@ss.hasPermi('system:userApplyPurchaseOrder:unLockOrder')")
    @Log(title = "解锁用户申购订单", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping("/unLockOrder")
    public AjaxResult unLockOrder(Long id) {
        if (id == null){
            throw new ServiceException("请选择需要操作的选项");
        }
        return toAjax(userApplyPurchaseOrderService.unLockOrder(id,null));
    }
}
