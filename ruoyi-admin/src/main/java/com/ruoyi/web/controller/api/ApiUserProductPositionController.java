package com.ruoyi.web.controller.api;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.LangException;
import com.ruoyi.common.logDict.UserProductPositionLogDict;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.UserProductPosition;
import com.ruoyi.system.service.IUserProductPositionService;
import com.ruoyi.system.utils.UserApiKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户合约交易订单Controller
 * 
 * @author ruoyi
 * @date 2025-06-25
 */
@RestController
@RequestMapping("/api/userProductPosition")
public class ApiUserProductPositionController extends BaseController
{
    @Autowired
    private IUserProductPositionService userProductPositionService;

    /**
     * 查询用户合约交易订单列表
     */
    @GetMapping("/list")
    public TableDataInfo list(UserProductPosition userProductPosition)
    {
        userProductPosition.setUserId(UserApiKeyUtils.getUserId());
        startPage();
        startOrderBy("a.id desc");
        List<UserProductPosition> list = userProductPositionService.selectUserProductPositionList(userProductPosition);
        PageHelper.clearPage();
        userProductPositionService.fillOtherInfo(list);
        return getDataTable(list);
    }

    /**
     * 用户合约交易下单
     */
    @RepeatSubmit
    @PostMapping(value = "buy")
    @Log(title = "用户合约交易下单", businessType = BusinessType.OTHER,dict = UserProductPositionLogDict.class,
            saveParamNames = {"id","orderCode","productType","productCode","buyOrderPrice","orderDirection","orderNum","orderFee","currencyId","currencyName","stopProfitPrice","stopLossPrice","marginAmount"})
    public AjaxResult buy(@RequestBody UserProductPosition position) {
        if (position.getProductType() == null){
            throw new LangException(HintConstants.SYSTEM_BUSY,"请选择产品类型");
        }
        if (StringUtils.isEmpty(position.getProductCode())){
            throw new LangException(HintConstants.SYSTEM_BUSY,"请选择下单产品");
        }
        if (position.getOrderTotalPrice() == null){
            throw new LangException(HintConstants.SYSTEM_BUSY,"请输入下单金额");
        }
        if (position.getOrderTotalPrice().compareTo(BigDecimal.ZERO) <= 0){
            throw new LangException("hint_orderAmountMoreThenZero","下单金额必须大于0");
        }
        if (position.getOrderDirection() == null){
            throw new LangException(HintConstants.SYSTEM_BUSY,"请选择下单方向");
        }
        return toAjax(userProductPositionService.buy(position)).put("orderInfo",position);
    }

    /**
     * 用户合约交易卖出
     */
    @RepeatSubmit
    @PostMapping(value = "sell")
    @Log(title = "用户合约交易卖出", businessType = BusinessType.OTHER,dict = UserProductPositionLogDict.class,
            saveParamNames = {"id","orderCode","productType","productCode","buyOrderPrice","sellOrderPrice","orderDirection","orderNum","orderFee","currencyId","currencyName","profitAndLose","allProfitAndLose","doType"})
    public AjaxResult sell(Long positionId) {
        if (positionId == null){
            throw new LangException(HintConstants.PARAM_NULL,"请选择需要卖出的订单");
        }
        return toAjax(userProductPositionService.sell(positionId,1,null));
    }

}
