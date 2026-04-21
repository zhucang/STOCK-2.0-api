package com.ruoyi.web.controller.api;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.LangException;
import com.ruoyi.common.logDict.LoanOrderLogDict;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.LoanOrder;
import com.ruoyi.system.service.ILoanOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 贷款订单Controller
 * 
 * @author ruoyi
 * @date 2024-05-22
 */
@RestController
@RequestMapping("/api/loanOrder")
public class ApiLoanOrderController extends BaseController
{
    @Autowired
    private ILoanOrderService loanOrderService;

    /**
     * 查询贷款订单列表
     */
    @GetMapping("/list")
    public TableDataInfo list(LoanOrder loanOrder)
    {
        loanOrder.setUserId(SecurityUtils.getUserId());
        startPage();
        startOrderBy("id desc");
        List<LoanOrder> list = loanOrderService.selectLoanOrderList(loanOrder);
        return getDataTable(list);
    }

    /**
     * 用户贷款
     */
    @Log(title = "用户贷款", businessType = BusinessType.OTHER,dict = LoanOrderLogDict.class)
    @RepeatSubmit
    @PostMapping(value = "add")
    public AjaxResult addLoanOrder(@RequestBody LoanOrder loanOrder) {
        loanOrder.setUserId(SecurityUtils.getUserId());
        if (loanOrder.getLoanProductId() == null){
            throw new LangException(HintConstants.PARAM_NULL,"请选择要贷款的贷款产品");
        }
        if (loanOrder.getOrderPrice() == null){
            throw new LangException(HintConstants.PARAM_NULL,"请输入贷款金额");
        }
        return toAjax(loanOrderService.addLoanOrder(loanOrder));
    }

    /**
     * 用户贷款信息面板
     */
    @GetMapping(value = "/userLoanPanelData")
    public AjaxResult userLoanPanelData() {
        return AjaxResult.success(loanOrderService.userLoanPanelData());
    }
}
