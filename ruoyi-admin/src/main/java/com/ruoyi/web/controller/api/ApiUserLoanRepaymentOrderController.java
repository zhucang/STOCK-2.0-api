package com.ruoyi.web.controller.api;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.LangException;
import com.ruoyi.common.logDict.UserLoanRepaymentOrderLogDict;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.UserLoanRepaymentOrder;
import com.ruoyi.system.service.ISwitchSetService;
import com.ruoyi.system.service.IUserLoanRepaymentOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户贷款还款订单Controller
 * 
 * @author ruoyi
 * @date 2024-05-28
 */
@RestController
@RequestMapping("/api/userLoanRepaymentOrder")
public class ApiUserLoanRepaymentOrderController extends BaseController
{
    @Autowired
    private IUserLoanRepaymentOrderService userLoanRepaymentOrderService;

    @Autowired
    private ISwitchSetService switchSetService;

    /**
     * 查询用户贷款还款订单列表
     */
    @GetMapping("/list")
    public TableDataInfo list(UserLoanRepaymentOrder userLoanRepaymentOrder)
    {
        userLoanRepaymentOrder.setUserId(SecurityUtils.getUserId());
        startPage();
        startOrderBy("id desc");
        List<UserLoanRepaymentOrder> list = userLoanRepaymentOrderService.selectUserLoanRepaymentOrderList(userLoanRepaymentOrder);
        return getDataTable(list);
    }

    /**
     * 获取用户贷款还款订单详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(userLoanRepaymentOrderService.selectUserLoanRepaymentOrderById(id));
    }

    /**
     * 用户贷款还款
     */
    @Log(title = "用户贷款还款", businessType = BusinessType.OTHER,dict = UserLoanRepaymentOrderLogDict.class)
    @RepeatSubmit
    @PostMapping(value = "userLoanRepayment")
    public AjaxResult userLoanRepayment(@RequestBody UserLoanRepaymentOrder userLoanRepaymentOrder)
    {
        userLoanRepaymentOrder.setUserId(SecurityUtils.getUserId());
        if (userLoanRepaymentOrder.getLoanOrderId() == null){
            throw new LangException(HintConstants.PARAM_NULL,"请选择需要还款的贷款订单");
        }
        if (userLoanRepaymentOrder.getPayChannelId() == null){
            throw new LangException(HintConstants.PARAM_NULL,"请选择还款通道");
        }
        //是否要求用户充值上传凭证
        Integer selectSwitchStatusById79 = switchSetService.selectSwitchStatusById(79L);
        if (selectSwitchStatusById79.equals(0)){
            if (StringUtils.isEmpty(userLoanRepaymentOrder.getRechargeImg())){
                throw new LangException(HintConstants.PARAM_NULL,"请上传还款凭证");
            }
        }
        return toAjax(userLoanRepaymentOrderService.userLoanRepayment(userLoanRepaymentOrder));
    }

}
