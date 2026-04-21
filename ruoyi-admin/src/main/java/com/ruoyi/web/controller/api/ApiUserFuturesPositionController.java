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
import com.ruoyi.common.logDict.UserFuturesPositionLogDict;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.UserFuturesPosition;
import com.ruoyi.system.service.IUserFuturesPositionService;
import com.ruoyi.system.utils.UserApiKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户期货持仓Controller
 * 
 * @author ruoyi
 * @date 2023-11-05
 *  *  * cache待优化
 */
@RestController
@RequestMapping("/api/userFuturesPosition")
public class ApiUserFuturesPositionController extends BaseController
{
    @Autowired
    private IUserFuturesPositionService userFuturesPositionService;

    /**
     * 查询用户期货持仓列表
     */
    @GetMapping("/list")
    public TableDataInfo list(UserFuturesPosition userFuturesPosition)
    {
        userFuturesPosition.setUserId(UserApiKeyUtils.getUserId());
        startPage();
        startOrderBy("id desc");
        List<UserFuturesPosition> list = userFuturesPositionService.selectUserFuturesPositionList(userFuturesPosition);
        PageHelper.clearPage();
        userFuturesPositionService.fillOtherInfo(list);
        return getDataTable(list);
    }

    /**
     * 用户期货合约交易下单
     */
    @RepeatSubmit
    @PostMapping(value = "buyFutures")
    @Log(title = "用户期货合约交易下单", businessType = BusinessType.OTHER,dict = UserFuturesPositionLogDict.class,
            saveParamNames = {"id","orderCode","productCode","buyOrderPrice","orderDirection","orderNum","orderLever","orderFee","orderYhsFee","currencyId","currencyName","stopProfitPrice","stopLossPrice","marginAmount"})
    public AjaxResult buy(@RequestBody UserFuturesPosition position) {
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
        return toAjax(userFuturesPositionService.buy(position));
    }

    /**
     * 用户期货合约交易卖出
     */
    @RepeatSubmit
    @PostMapping(value = "sellFutures")
    @Log(title = "用户期货合约交易卖出", businessType = BusinessType.OTHER,dict = UserFuturesPositionLogDict.class,
            saveParamNames = {"id","orderCode","productCode","buyOrderPrice","orderDirection","orderNum","orderLever","orderFee","orderYhsFee","currencyId","currencyName","stopProfitPrice","stopLossPrice"})
    public AjaxResult sell(Long positionId) {
        if (positionId == null){
            return AjaxResult.error(HintConstants.PARAM_NULL,"请选择需要卖出的订单");
        }
        return userFuturesPositionService.sell(positionId, 1,null);
    }
}
