package com.ruoyi.web.controller.api;

import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.exception.LangException;
import com.ruoyi.system.domain.CurrencyExchangeRate;
import com.ruoyi.system.service.ICurrencyExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 货币兑换汇率Controller
 * 
 * @author ruoyi
 * @date 2023-11-25
 * 已优化
 */
@RestController
@RequestMapping("/api/currencyExchangeRate")
public class ApiCurrencyExchangeRateController extends BaseController
{
    @Autowired
    private ICurrencyExchangeRateService currencyExchangeRateService;

    /**
     * 查询货币兑换汇率列表
     */
    @GetMapping("/list")
    public TableDataInfo list(CurrencyExchangeRate currencyExchangeRate)
    {
        startPage();
        List<CurrencyExchangeRate> list = currencyExchangeRateService.selectCurrencyExchangeRateList(currencyExchangeRate);
        return getDataTable(list);
    }

    /**
     * 获取汇率详情 （汇率和手续费）
     */
    @GetMapping(value = "getExchangeInfo")
    public AjaxResult getExchangeInfo(Long currencyIdFrom, Long currencyIdTo){
        if (currencyIdFrom == null){
            throw new LangException(HintConstants.PARAM_NULL,"请选择转出币种");
        }
        if (currencyIdTo == null){
            throw new LangException(HintConstants.PARAM_NULL,"请选择转入币种");
        }
        return AjaxResult.success(currencyExchangeRateService.getExchangeInfo(currencyIdFrom,currencyIdTo));
    }
}
