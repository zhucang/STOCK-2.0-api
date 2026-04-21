package com.ruoyi.web.controller.system;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.logDict.SpotTradeOrderLogDict;
import com.ruoyi.system.domain.SpotTradeOrder;
import com.ruoyi.system.service.ISpotTradeOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 现货交易订单Controller
 * 
 * @author ruoyi
 * @date 2023-11-05
 */
@RestController
@RequestMapping("/system/spotTradeOrder")
public class SpotTradeOrderController extends BaseController
{
    @Autowired
    private ISpotTradeOrderService spotTradeOrderService;

    /**
     * 查询现货交易订单列表
     */
    @PreAuthorize("@ss.hasPermi('system:spotTradeOrder:list')")
    @GetMapping("/list")
    public TableDataInfo list(SpotTradeOrder spotTradeOrder)
    {
        startPage();
        if (spotTradeOrder.getOrderStatus() == null){
            startOrderBy("order_status,id desc");
        }else {
            startOrderBy("id desc");
        }
        List<SpotTradeOrder> list = spotTradeOrderService.selectSpotTradeOrderList(spotTradeOrder);
        PageHelper.clearPage();
        spotTradeOrderService.fillOtherInfo(list);
        return getDataTable(list);
    }

    /**
     * 获取现货交易订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:spotTradeOrder:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(spotTradeOrderService.selectSpotTradeOrderById(id));
    }

    /**
     * 现货持仓强制平仓操作
     */
    @PreAuthorize("@ss.hasPermi('system:spotTradeOrder:forceSell')")
    @Log(title = "现货持仓强制平仓操作", businessType = BusinessType.UPDATE,dict = SpotTradeOrderLogDict.class,
            saveParamNames = {"id","orderCode","currencyId","currencyName","productType","productCode","buyOrderPrice","sellOrderPrice","orderNum","orderFee","profitAndLose","allProfitAndLose","doType"})
    @RepeatSubmit
    @PostMapping(value = "forceSell")
    public AjaxResult forceSell(Long orderId){
        if (orderId == null){
            throw new ServiceException("请选择需要平仓的订单");
        }
        AjaxResult ajaxResult = spotTradeOrderService.sellSpotTradeOrder(orderId,0);
        ajaxResult.put(AjaxResult.MSG_TAG,ajaxResult.get(AjaxResult.DATA_TAG));
        return ajaxResult;
    }
}
