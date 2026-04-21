package com.ruoyi.web.controller.api;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.LangException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.BibiTradeOrder;
import com.ruoyi.system.service.IBibiTradeOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * 币币交易订单Controller
 * 
 * @author ruoyi
 * @date 2024-06-24
 */
@RestController
@RequestMapping("/api/bibiTradeOrder")
public class ApiBibiTradeOrderController extends BaseController
{
    @Autowired
    private IBibiTradeOrderService bibiTradeOrderService;

    /**
     * 查询币币交易订单列表
     */
    @GetMapping("/list")
    public TableDataInfo list(BibiTradeOrder bibiTradeOrder)
    {
        bibiTradeOrder.setUserId(SecurityUtils.getUserId());
        startPage();
        startOrderBy("id desc");
        List<BibiTradeOrder> list = bibiTradeOrderService.selectBibiTradeOrderList(bibiTradeOrder);
        return getDataTable(list);
    }

    /**
     * 用户币币交易买入
     */
    @RepeatSubmit
    @Log(title = "用户币币交易买入", businessType = BusinessType.OTHER)
    @PostMapping("/buy")
    public AjaxResult buy(BibiTradeOrder bibiTradeOrder)
    {
        bibiTradeOrder.setUserId(SecurityUtils.getUserId());
        if (StringUtils.isEmpty(bibiTradeOrder.getProductCode())){
            throw new LangException(HintConstants.PARAM_NULL,"请选择买入产品");
        }
        if (bibiTradeOrder.getProductType() == null) {
            throw new LangException(HintConstants.PARAM_NULL, "请选择产品类型");
        }
        if (bibiTradeOrder.getOrderAmount() == null) {
            throw new LangException(HintConstants.PARAM_NULL, "请输入买入金额");
        }
        if (bibiTradeOrder.getOrderAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new LangException("hint_orderAmountMoreThenZero", "订单金额必须大于0");
        }
        if (bibiTradeOrder.getOrderMethod() == null) {
            throw new LangException(HintConstants.PARAM_NULL, "请选择订单方式");
        }
        if (bibiTradeOrder.getOrderMethod().equals(0) && bibiTradeOrder.getProductPrice() == null){
            throw new LangException(HintConstants.PARAM_NULL, "限价委托必须输入价格");
        }
        return toAjax(bibiTradeOrderService.buy(bibiTradeOrder));
    }

    /**
     * 用户币币交易卖出
     */
    @RepeatSubmit
    @Log(title = "用户币币交易卖出", businessType = BusinessType.OTHER)
    @PostMapping("/sell")
    public AjaxResult sell(BibiTradeOrder bibiTradeOrder)
    {
        bibiTradeOrder.setUserId(SecurityUtils.getUserId());
        if (StringUtils.isEmpty(bibiTradeOrder.getProductCode())){
            throw new LangException(HintConstants.PARAM_NULL,"请选择买入产品");
        }
        if (bibiTradeOrder.getProductType() == null) {
            throw new LangException(HintConstants.PARAM_NULL, "请选择产品类型");
        }
        if (bibiTradeOrder.getOrderVolume() == null) {
            throw new LangException(HintConstants.PARAM_NULL, "请输入卖出数量");
        }
        if (bibiTradeOrder.getOrderVolume().compareTo(BigDecimal.ZERO) <= 0) {
            throw new LangException(HintConstants.SYSTEM_BUSY, "卖出数量必须大于0");
        }
        if (bibiTradeOrder.getOrderMethod() == null) {
            throw new LangException(HintConstants.PARAM_NULL, "请选择订单方式");
        }
        if (bibiTradeOrder.getOrderMethod().equals(0) && bibiTradeOrder.getProductPrice() == null){
            throw new LangException(HintConstants.PARAM_NULL, "限价委托必须输入价格");
        }
        return toAjax(bibiTradeOrderService.sell(bibiTradeOrder));
    }

    /**
     * 撤销委托
     */
    @RepeatSubmit
    @Log(title = "撤销委托", businessType = BusinessType.OTHER)
    @PostMapping("/cancel")
    public AjaxResult cancel(Long bibiTradeOrderId)
    {
        if (bibiTradeOrderId == null) {
            throw new LangException(HintConstants.PARAM_NULL, "请选择需要撤销的订单");
        }
        return toAjax(bibiTradeOrderService.cancel(bibiTradeOrderId));
    }


}
