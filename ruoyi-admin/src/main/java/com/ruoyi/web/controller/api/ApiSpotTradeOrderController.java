package com.ruoyi.web.controller.api;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.logDict.SpotTradeOrderLogDict;
import com.ruoyi.system.domain.SpotTradeOrder;
import com.ruoyi.system.service.ISpotTradeOrderService;
import com.ruoyi.system.utils.UserApiKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * 现货交易订单Controller
 * 
 * @author ruoyi
 * @date 2023-11-05
 *  * cache待优化
 */
@RestController
@RequestMapping("/api/spotTradeOrder")
public class ApiSpotTradeOrderController extends BaseController
{
    @Autowired
    private ISpotTradeOrderService spotTradeOrderService;

    /**
     * 查询现货交易订单列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SpotTradeOrder spotTradeOrder)
    {
        spotTradeOrder.setUserId(UserApiKeyUtils.getUserId());
        startPage();
        startOrderBy("id desc");
        List<SpotTradeOrder> list = spotTradeOrderService.selectSpotTradeOrderList(spotTradeOrder);
        PageHelper.clearPage();
        spotTradeOrderService.fillOtherInfo(list);
        return getDataTable(list);
    }

    /**
     * 用户现货交易订单下单
     */
    @RepeatSubmit
    @PostMapping(value = "addSpotTradeOrder")
    @Log(title = "用户现货交易订单下单", businessType = BusinessType.OTHER,dict = SpotTradeOrderLogDict.class,
            saveParamNames = {"id","orderCode","currencyId","currencyName","productType","productCode","buyOrderPrice","orderNum","orderFee"})
    public AjaxResult addSpotTradeOrder(Integer productType, String productCode, BigDecimal orderNum){
        if (productType == null){
            return AjaxResult.error(HintConstants.PARAM_NULL,"请选择产品类型");
        }
        if (productCode == null){
            return AjaxResult.error(HintConstants.PARAM_NULL,"请选择下单产品");
        }
        if (orderNum == null){
            return AjaxResult.error(HintConstants.PARAM_NULL,"请输入下单数量");
        }
        if (orderNum.compareTo(BigDecimal.ZERO) <= 0){
            return AjaxResult.error("hint_58","下单数量必须大于0");
        }
        return spotTradeOrderService.addSpotTradeOrder(productType,productCode,orderNum);
    }

    /**
     * 用户现货交易订单卖出
     */
    @RepeatSubmit
    @PostMapping(value = "sellSpotTradeOrder")
    @Log(title = "用户现货交易订单卖出", businessType = BusinessType.OTHER,dict = SpotTradeOrderLogDict.class,
            saveParamNames = {"id","orderCode","currencyId","currencyName","productType","productCode","buyOrderPrice","sellOrderPrice","orderNum","orderFee","profitAndLose","allProfitAndLose","doType"})
    public AjaxResult sellSpotTradeOrder(Long spotTradeOrderId){
        if (spotTradeOrderId == null){
            return AjaxResult.error(HintConstants.SYSTEM_BUSY,"请选择需要卖出的订单");
        }
        return spotTradeOrderService.sellSpotTradeOrder(spotTradeOrderId,1);
    }
}
