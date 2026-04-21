package com.ruoyi.web.controller.api;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.LangException;
import com.ruoyi.system.service.IUserAmountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * 用户余额信息Controller
 * 
 * @author ruoyi
 * @date 2025-07-02
 */
@RestController
@RequestMapping("/api/userAmount")
public class ApiUserAmountController extends BaseController
{
    @Autowired
    private IUserAmountService userAmountService;


    /**
     * 转入/转出灵活投资资金
     */
    @PostMapping(value = "/transferFlexibleInvestmentFunds")
    @RepeatSubmit
    @Log(title = "转入/转出灵活投资资金", businessType = BusinessType.OTHER)
    public AjaxResult transferFlexibleInvestmentFunds(Integer transferType, BigDecimal transferAmount, Long currencyId) {
        if (transferType == null){
            throw new LangException(HintConstants.PARAM_NULL, "请选择转入或者转出");
        }
        if (!transferType.equals(0) && !transferType.equals(1)){
            throw new LangException(HintConstants.SYSTEM_ERR, "转移类型错误");
        }
        if (transferAmount == null){
            throw new LangException(HintConstants.PARAM_NULL, "请输入金额");
        }
        if (transferAmount.compareTo(BigDecimal.ZERO) <= 0){
            throw new LangException("hint_orderAmountMoreThenZero", "订单金额必须大于0");
        }
        if (currencyId == null){
            throw new LangException(HintConstants.PARAM_NULL, "请选择币种");
        }
        return toAjax(userAmountService.transferFlexibleInvestmentFunds(transferType, transferAmount, currencyId));
    }

}
