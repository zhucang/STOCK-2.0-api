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
import com.ruoyi.common.logDict.UserCryptocurrencyPositionLogDict;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.UserCryptocurrencyPosition;
import com.ruoyi.system.service.IUserCryptocurrencyPositionService;
import com.ruoyi.system.utils.UserApiKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户加密货币持仓Controller
 * 
 * @author ruoyi
 * @date 2023-11-05
 *  *  * cache待优化
 */
@RestController
@RequestMapping("/api/userCryptocurrencyPosition")
public class ApiUserCryptocurrencyPositionController extends BaseController
{
    @Autowired
    private IUserCryptocurrencyPositionService userCryptocurrencyPositionService;

    /**
     * 查询用户加密货币持仓列表
     */
    @GetMapping("/list")
    public TableDataInfo list(UserCryptocurrencyPosition userCryptocurrencyPosition)
    {
        userCryptocurrencyPosition.setUserId(UserApiKeyUtils.getUserId());
        startPage();
        startOrderBy("id desc");
        List<UserCryptocurrencyPosition> list = userCryptocurrencyPositionService.selectUserCryptocurrencyPositionList(userCryptocurrencyPosition);
        PageHelper.clearPage();
        userCryptocurrencyPositionService.fillOtherInfo(list);
        return getDataTable(list);
    }

    /**
     * 用户加密货币合约交易下单
     */
    @RepeatSubmit
    @PostMapping(value = "buyCryptocurrency")
    @Log(title = "用户加密货币合约交易下单", businessType = BusinessType.OTHER,dict = UserCryptocurrencyPositionLogDict.class,
            saveParamNames = {"id","orderCode","productCode","buyOrderPrice","orderDirection","orderNum","orderLever","orderFee","orderYhsFee","currencyId","currencyName","stopProfitPrice","stopLossPrice","marginAmount"})
    public AjaxResult buy(@RequestBody UserCryptocurrencyPosition position) {
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
        return toAjax(userCryptocurrencyPositionService.buy(position)).put("orderInfo",position);
    }

    /**
     * 用户加密货币合约交易卖出
     */
    @RepeatSubmit
    @PostMapping(value = "sellCryptocurrency")
    @Log(title = "用户加密货币合约交易卖出", businessType = BusinessType.OTHER,dict = UserCryptocurrencyPositionLogDict.class,
            saveParamNames = {"id","orderCode","productCode","buyOrderPrice","sellOrderPrice","orderDirection","orderNum","orderLever","orderFee","orderYhsFee","currencyId","currencyName","profitAndLose","allProfitAndLose","doType"})
    public AjaxResult sell(Long positionId) {
        if (positionId == null){
           throw new LangException(HintConstants.PARAM_NULL,"请选择需要卖出的订单");
        }
        return userCryptocurrencyPositionService.sell(positionId, 1,null);
    }
}
