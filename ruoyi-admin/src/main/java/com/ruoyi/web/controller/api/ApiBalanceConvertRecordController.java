package com.ruoyi.web.controller.api;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.LangException;
import com.ruoyi.common.logDict.BalanceConvertRecordLogDict;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.BalanceConvertRecord;
import com.ruoyi.system.service.IBalanceConvertRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * 资金互转记录Controller
 * 
 * @author ruoyi
 * @date 2023-12-01
 * 已优化
 */
@RestController
@RequestMapping("/api/balanceConvertRecord")
public class ApiBalanceConvertRecordController extends BaseController
{
    @Autowired
    private IBalanceConvertRecordService balanceConvertRecordService;

    /**
     * 查询资金互转记录列表
     */
    @GetMapping("/list")
    public TableDataInfo list(BalanceConvertRecord balanceConvertRecord)
    {
        balanceConvertRecord.setUserId(SecurityUtils.getUserId());
        startPage();
        List<BalanceConvertRecord> list = balanceConvertRecordService.selectBalanceConvertRecordList(balanceConvertRecord);
        return getDataTable(list);
    }

    /**
     * 资金互转
     */
    @RepeatSubmit
    @PostMapping(value = "balanceConvert")
    @Log(title = "用户资金互转", businessType = BusinessType.OTHER,dict = BalanceConvertRecordLogDict.class,
            saveParamNames = {"id","currencyNameFrom","transAmount","currencyNameTo","convertedAmount","currencyIdFrom","currencyIdTo","exchangeRate","handingFee","feeRatio"})
    public AjaxResult balanceConvert(BigDecimal transAmount, Long currencyIdFrom, Long currencyIdTo){
        if (currencyIdFrom == null){
            throw new LangException(HintConstants.PARAM_NULL,"请选择转出币种");
        }
        if (currencyIdTo == null){
            throw new LangException(HintConstants.PARAM_NULL,"请选择转入币种");
        }
        if (transAmount == null){
            throw new LangException(HintConstants.PARAM_NULL,"请输入转出金额");
        }
        if (transAmount.compareTo(BigDecimal.ZERO) <= 0){
            throw new LangException("hint_27","转换金额必须大于0");
        }
        if (currencyIdFrom.equals(currencyIdTo)){
            throw new LangException("hint_26","转化资产的币种类型不允许相同");
        }
        return toAjax(balanceConvertRecordService.balanceConvert(null,transAmount,currencyIdFrom,currencyIdTo));
    }
}
