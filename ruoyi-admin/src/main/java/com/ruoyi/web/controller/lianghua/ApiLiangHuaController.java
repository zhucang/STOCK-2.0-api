package com.ruoyi.web.controller.lianghua;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.LangException;
import com.ruoyi.common.logDict.SpotTradeOrderLogDict;
import com.ruoyi.common.logDict.UserCryptocurrencyPositionLogDict;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SpotTradeOrder;
import com.ruoyi.system.domain.UserCryptocurrencyPosition;
import com.ruoyi.system.service.ILiangHuaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * app量化Controller
 * 
 * @author ruoyi
 * @date 2023-12-20
 */
@RestController
@RequestMapping("/api/lianghua")
public class ApiLiangHuaController extends BaseController
{

    @Autowired
    private ILiangHuaService liangHuaService;

    /**
     * 加密货币合约机器人订单录入
     */
    @RepeatSubmit
    @PostMapping(value = "robotUserCryptocurrencyPosition")
    @Log(title = "加密货币合约机器人订单录入", businessType = BusinessType.OTHER,dict = UserCryptocurrencyPositionLogDict.class,
            saveParamNames = {"id","orderCode","productCode","buyOrderPrice","sellOrderPrice","orderDirection","orderNum","orderLever","orderFee","orderYhsFee","currencyId","currencyName","stopProfitPrice","stopLossPrice","marginAmount","profitAndLose","allProfitAndLose"})
    public AjaxResult robotUserCryptocurrencyPosition(@RequestBody UserCryptocurrencyPosition position) {
        if (StringUtils.isEmpty(position.getProductCode())){
            throw new LangException(HintConstants.SYSTEM_BUSY,"请选择下单产品");
        }
        if (position.getParams().get("marginAmount") == null && position.getOrderNum() == null){
            throw new LangException(HintConstants.SYSTEM_BUSY,"请输入下单金额或下单数量");
        }
        if (position.getOrderDirection() == null){
            throw new LangException(HintConstants.SYSTEM_BUSY,"请选择下单方向");
        }
        if (position.getOrderLever() == null){
            throw new LangException(HintConstants.SYSTEM_BUSY,"请输入杠杆倍数");
        }
        if (position.getBuyOrderPrice() == null){
            throw new LangException(HintConstants.SYSTEM_BUSY,"请输入购买时的产品价格");
        }
        if (position.getBuyOrderTime() == null){
            throw new LangException(HintConstants.SYSTEM_BUSY,"请输入下单时间");
        }
        if (position.getSellOrderPrice() == null){
            throw new LangException(HintConstants.SYSTEM_BUSY,"请输入购买时的产品价格");
        }
        if (position.getSellOrderTime() == null){
            throw new LangException(HintConstants.SYSTEM_BUSY,"请输入平仓时间");
        }
        return toAjax(liangHuaService.robotUserCryptocurrencyPosition(position)).put("orderInfo",position);
    }

    /**
     * 现货交易机器人订单录入
     */
    @RepeatSubmit
    @PostMapping(value = "robotSpotTradeOrder")
    @Log(title = "现货交易机器人订单录入", businessType = BusinessType.OTHER,dict = SpotTradeOrderLogDict.class,
            saveParamNames = {"id","orderCode","currencyId","currencyName","productType","productCode","buyOrderPrice","sellOrderPrice","orderNum","orderFee","profitAndLose","allProfitAndLose"})
    public AjaxResult robotSpotTradeOrder(@RequestBody SpotTradeOrder spotTradeOrder){
        if (spotTradeOrder.getProductType() == null){
            return AjaxResult.error(HintConstants.PARAM_NULL,"请选择产品类型");
        }
        if (spotTradeOrder.getProductCode() == null){
            return AjaxResult.error(HintConstants.PARAM_NULL,"请选择下单产品");
        }
        if (spotTradeOrder.getOrderNum() == null){
            return AjaxResult.error(HintConstants.PARAM_NULL,"请输入下单数量");
        }
        if (spotTradeOrder.getOrderNum().compareTo(BigDecimal.ZERO) <= 0){
            return AjaxResult.error("hint_58","下单数量必须大于0");
        }
        if (spotTradeOrder.getBuyOrderPrice() == null){
            throw new LangException(HintConstants.SYSTEM_BUSY,"请输入购买时的产品价格");
        }
        if (spotTradeOrder.getBuyOrderTime() == null){
            throw new LangException(HintConstants.SYSTEM_BUSY,"请输入下单时间");
        }
        if (spotTradeOrder.getSellOrderPrice() == null){
            throw new LangException(HintConstants.SYSTEM_BUSY,"请输入购买时的产品价格");
        }
        if (spotTradeOrder.getSellOrderTime() == null){
            throw new LangException(HintConstants.SYSTEM_BUSY,"请输入平仓时间");
        }
        return toAjax(liangHuaService.robotSpotTradeOrder(spotTradeOrder)).put("orderInfo",spotTradeOrder);
    }

}
