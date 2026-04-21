package com.ruoyi.web.controller.api;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.UserApplyPurchaseOrder;
import com.ruoyi.system.service.IUserApplyPurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户新股新币申购订单Controller
 * 
 * @author ruoyi
 * @date 2023-11-30
 * 日志待优化
 */
@RestController
@RequestMapping("/api/userApplyPurchaseOrder")
public class ApiUserApplyPurchaseOrderController extends BaseController
{
    @Autowired
    private IUserApplyPurchaseOrderService userApplyPurchaseOrderService;

    /**
     * 查询用户新股新币申购订单列表
     */
    @GetMapping("/list")
    public TableDataInfo list(UserApplyPurchaseOrder userApplyPurchaseOrder)
    {
        userApplyPurchaseOrder.setUserId(SecurityUtils.getUserId());
        if (userApplyPurchaseOrder.getProductType() == null){
            return getDataTable(new ArrayList<>());
//            return AjaxResult.error("请选择产品类型");
        }
        startPage();
        startOrderBy("id desc");
        List<UserApplyPurchaseOrder> list = userApplyPurchaseOrderService.selectUserApplyPurchaseOrderList(userApplyPurchaseOrder);
        return getDataTable(list);
    }


    /**
     * 用户申购
     * @param userApplyPurchaseOrder
     * @return
     */
    @RepeatSubmit
    @PostMapping(value = "addUserApplyPurchaseOrder")
    @Log(title = "用户申购", businessType = BusinessType.OTHER)
    public AjaxResult addUserApplyPurchaseOrder(@RequestBody UserApplyPurchaseOrder userApplyPurchaseOrder) {
        if (userApplyPurchaseOrder.getNewProductApplyPurchaseId() == null){
            return AjaxResult.error(HintConstants.PARAM_NULL,"请选择申购产品");
        }
        if (userApplyPurchaseOrder.getApplyPurchaseQuantity() == null){
            return AjaxResult.error(HintConstants.PARAM_NULL,"请输入申购数量");
        }
        return toAjax(userApplyPurchaseOrderService.addUserApplyPurchaseOrder(userApplyPurchaseOrder));
    }
}
