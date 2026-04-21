package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.BibiTradeOrder;
import com.ruoyi.system.service.IBibiTradeOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 币币交易订单Controller
 * 
 * @author ruoyi
 * @date 2024-06-24
 */
@RestController
@RequestMapping("/system/bibiTradeOrder")
public class BibiTradeOrderController extends BaseController
{
    @Autowired
    private IBibiTradeOrderService bibiTradeOrderService;

    /**
     * 查询币币交易订单列表
     */
    @PreAuthorize("@ss.hasPermi('system:bibiTradeOrder:list')")
    @GetMapping("/list")
    public TableDataInfo list(BibiTradeOrder bibiTradeOrder)
    {
        startPage();
        startOrderBy("id desc");
        List<BibiTradeOrder> list = bibiTradeOrderService.selectBibiTradeOrderList(bibiTradeOrder);
        return getDataTable(list);
    }
//
//    /**
//     * 导出币币交易订单列表
//     */
//    @PreAuthorize("@ss.hasPermi('system:bibiTradeOrder:export')")
//    @Log(title = "币币交易订单", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, BibiTradeOrder bibiTradeOrder)
//    {
//        List<BibiTradeOrder> list = bibiTradeOrderService.selectBibiTradeOrderList(bibiTradeOrder);
//        ExcelUtil<BibiTradeOrder> util = new ExcelUtil<BibiTradeOrder>(BibiTradeOrder.class);
//        util.exportExcel(response, list, "币币交易订单数据");
//    }
//
//    /**
//     * 获取币币交易订单详细信息
//     */
//    @PreAuthorize("@ss.hasPermi('system:bibiTradeOrder:query')")
//    @GetMapping(value = "/{id}")
//    public AjaxResult getInfo(@PathVariable("id") Long id)
//    {
//        return success(bibiTradeOrderService.selectBibiTradeOrderById(id));
//    }
//
//    /**
//     * 新增币币交易订单
//     */
//    @PreAuthorize("@ss.hasPermi('system:bibiTradeOrder:add')")
//    @Log(title = "币币交易订单", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody BibiTradeOrder bibiTradeOrder)
//    {
//        return toAjax(bibiTradeOrderService.insertBibiTradeOrder(bibiTradeOrder));
//    }
//
//    /**
//     * 修改币币交易订单
//     */
//    @PreAuthorize("@ss.hasPermi('system:bibiTradeOrder:edit')")
//    @Log(title = "币币交易订单", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody BibiTradeOrder bibiTradeOrder)
//    {
//        return toAjax(bibiTradeOrderService.updateBibiTradeOrder(bibiTradeOrder));
//    }
//
//    /**
//     * 删除币币交易订单
//     */
//    @PreAuthorize("@ss.hasPermi('system:bibiTradeOrder:remove')")
//    @Log(title = "币币交易订单", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        return toAjax(bibiTradeOrderService.deleteBibiTradeOrderByIds(ids));
//    }

    /**
     * 人工正常平仓
     */
    @PreAuthorize("@ss.hasPermi('system:bibiTradeOrder:manualSell')")
    @RepeatSubmit
    @Log(title = "人工正常平仓", businessType = BusinessType.OTHER)
    @PostMapping("/manualSell")
    public AjaxResult manualSell(@RequestBody BibiTradeOrder bibiTradeOrder)
    {
        if (bibiTradeOrder.getUserId() == null) {
            throw new ServiceException("请选择用户");
        }
        if (StringUtils.isEmpty(bibiTradeOrder.getProductCode())){
            throw new ServiceException("请选择买入产品");
        }
        if (bibiTradeOrder.getProductType() == null) {
            throw new ServiceException("请选择产品类型");
        }
        if (bibiTradeOrder.getOrderVolume() == null) {
            throw new ServiceException("请输入卖出数量");
        }
        if (bibiTradeOrder.getOrderVolume().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("卖出数量必须大于0");
        }
        if (bibiTradeOrder.getProductPrice() == null || bibiTradeOrder.getProductPrice().compareTo(BigDecimal.ZERO) <= 0){
            throw new ServiceException("卖出价格必须大于0");
        }
        return toAjax(bibiTradeOrderService.manualSell(bibiTradeOrder));
    }
}
