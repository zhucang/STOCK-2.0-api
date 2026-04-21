package com.ruoyi.web.controller.system;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.logDict.FastTradeOrderLogDict;
import com.ruoyi.system.domain.FastTradeOrder;
import com.ruoyi.system.service.IFastTradeOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 极速交易订单Controller
 * 
 * @author ruoyi
 * @date 2023-11-02
 * 日志优化完成
 */
@RestController
@RequestMapping("/system/fastTradeOrder")
public class FastTradeOrderController extends BaseController
{
    @Autowired
    private IFastTradeOrderService fastTradeOrderService;

    /**
     * 查询极速交易订单列表
     */
    @PreAuthorize("@ss.hasPermi('system:fastTradeOrder:list')")
    @GetMapping("/list")
    public TableDataInfo list(FastTradeOrder fastTradeOrder)
    {
        startPage();
        if (fastTradeOrder.getOrderStatus() == null){
            startOrderBy("order_status,id desc");
        }else {
            startOrderBy("id desc");
        }
        List<FastTradeOrder> list = fastTradeOrderService.selectFastTradeOrderList(fastTradeOrder);
        PageHelper.clearPage();
        fastTradeOrderService.fillOtherInfo(list);
        return getDataTable(list);
    }

    /**
     * 获取极速交易订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:fastTradeOrder:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(fastTradeOrderService.selectFastTradeOrderById(id));
    }

    /**
     * 修改极速交易订单信息
     */
    @PreAuthorize("@ss.hasPermi('system:fastTradeOrder:edit')")
    @Log(title = "修改极速交易订单信息", businessType = BusinessType.UPDATE,dict = FastTradeOrderLogDict.class,
            saveParamNames = {"id","orderCode","productType","productCode","winProfitRatio","loseProfitRatio"})
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody FastTradeOrder fastTradeOrder) {
        FastTradeOrder vo = fastTradeOrderService.selectFastTradeOrderById(fastTradeOrder.getId());
        if (!vo.getOrderStatus().equals(0)){
            throw new ServiceException("订单已结算");
        }
        vo.setWinProfitRatio(fastTradeOrder.getWinProfitRatio());
        vo.setLoseProfitRatio(fastTradeOrder.getLoseProfitRatio());
        return toAjax(fastTradeOrderService.updateFastTradeOrder(vo));
    }

    /**
     * 极速交易订单单控
     */
    @PreAuthorize("@ss.hasPermi('system:fastTradeOrder:fastOrderControl')")
    @Log(title = "极速交易订单单控", businessType = BusinessType.UPDATE,dict = FastTradeOrderLogDict.class,
            saveParamNames = {"id","orderCode","productType","productCode","orderControlFlag"})
    @RepeatSubmit
    @PostMapping(value = "fastOrderControl")
    public AjaxResult fastOrderControl(Long fastTradeOrderId,Integer orderControlFlag) {
        if (fastTradeOrderId == null){
            throw new ServiceException("请选择需要控制的订单");
        }
        if (orderControlFlag == null){
            throw new ServiceException("请选择需要控制的订单状态");
        }
        if (!orderControlFlag.equals(0) && !orderControlFlag.equals(1) && !orderControlFlag.equals(2) && !orderControlFlag.equals(3)){
            throw new ServiceException("控制状态错误");
        }
        return toAjax(fastTradeOrderService.fastOrderControl(fastTradeOrderId,orderControlFlag));
    }


    /**
     * 极速交易订单批量控制
     */
    @PreAuthorize("@ss.hasPermi('system:fastTradeOrder:fastOrderControl')")
    @Log(title = "极速交易订单批量控制", businessType = BusinessType.UPDATE,dict = FastTradeOrderLogDict.class,
            saveParamNames = {"id","orderCode","productType","productCode","orderControlFlag","fastTradeOrders"})
    @RepeatSubmit
    @PostMapping(value = "batchFastOrderControl")
    public AjaxResult batchFastOrderControl(@RequestBody FastTradeOrder fastTradeOrder) {
        List<Long> fastTradeOrderIds = new ArrayList<>();
        try{
            fastTradeOrderIds = (List<Long>) fastTradeOrder.getParams().get("fastTradeOrderIds");
            if (fastTradeOrderIds.size() == 0){
                throw new ServiceException();
            }
        }catch (Exception e){
            throw new ServiceException("请选择需要控制的订单");
        }
        Integer orderControlFlag = fastTradeOrder.getOrderControlFlag();
        if (orderControlFlag == null){
            throw new ServiceException("请选择需要控制的订单状态");
        }
        if (!orderControlFlag.equals(0) && !orderControlFlag.equals(1) && !orderControlFlag.equals(2) && !orderControlFlag.equals(3)){
            throw new ServiceException("控制状态错误");
        }
        return toAjax(fastTradeOrderService.batchFastOrderControl(fastTradeOrderIds, orderControlFlag));
    }
}
