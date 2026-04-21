package com.ruoyi.web.controller.api;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.LangException;
import com.ruoyi.common.logDict.FinancialOrderLogDict;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.FinancialOrder;
import com.ruoyi.system.service.IFinancialOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * 理财订单Controller
 * 
 * @author ruoyi
 * @date 2023-11-26
 */
@RestController
@RequestMapping("/api/financialOrder")
public class ApiFinancialOrderController extends BaseController
{
    @Autowired
    private IFinancialOrderService financialOrderService;

    /**
     * 查询理财订单列表
     */
    @GetMapping("/list")
    public TableDataInfo list(FinancialOrder financialOrder)
    {
        financialOrder.setUserId(SecurityUtils.getUserId());
        startPage();
        startOrderBy("id desc");
        List<FinancialOrder> list = financialOrderService.selectFinancialOrderList(financialOrder);
        financialOrderService.fillOtherInfo(list);
        return getDataTable(list);
    }

    /**
     * 用户购买理财订单
     */
    @RepeatSubmit
    @PostMapping(value = "add")
    @Log(title = "用户购买理财订单", businessType = BusinessType.OTHER,dict = FinancialOrderLogDict.class,
            saveParamNames = {"id","orderCode","financialProductId","financialName","financialTime","buyPrice","startDate","endDate","breakContractRate","dailyIncomeRate","floatDailyIncomeMinRate","floatDailyIncomeMaxRate","orderStatus","currencyId","currencyName","settleMethod"})
    public AjaxResult addFinancialOrder(Long financialProductId, BigDecimal buyPrice) {
        if (financialProductId == null){
            throw new LangException(HintConstants.PARAM_NULL,"请选择要购买的理财产品");
        }
        if (buyPrice == null){
            throw new LangException(HintConstants.PARAM_NULL,"请选择购买金额");
        }
        return toAjax(financialOrderService.addFinancialOrder(financialProductId,buyPrice));
    }

    /**
     * 理财数据总统计
     */
    @GetMapping(value = "financialOrderAnalysis")
    public AjaxResult financialOrderAnalysis() {
        return AjaxResult.success(financialOrderService.financialOrderAnalysis(SecurityUtils.getUserId()));
    }

    /**
     * 用户赎回理财订单
     */
    @RepeatSubmit
    @PostMapping(value = "redemption")
    @Log(title = "用户赎回理财订单", businessType = BusinessType.OTHER,dict = FinancialOrderLogDict.class,
            saveParamNames = {"id","financialTime","breakContractRate","alreadyInterestCount","breakContractAmount","buyPrice","orderCode"})
    public AjaxResult redemption(Long financialOrderId) {
        if (financialOrderId == null){
            throw new LangException(HintConstants.PARAM_NULL,"请选择需要赎回的理财产品订单");
        }
        return toAjax(financialOrderService.redemption(financialOrderId));
    }
}
