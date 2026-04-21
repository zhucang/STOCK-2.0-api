package com.ruoyi.web.controller.api;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.logDict.UserWithdrawLogDict;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.UserWithdraw;
import com.ruoyi.system.service.ISwitchSetService;
import com.ruoyi.system.service.IUserWithdrawService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * 提现记录Controller
 * 
 * @author ruoyi
 * @date 2023-10-30
 *  *  * cache待优化
 */
@RestController
@RequestMapping("/api/userWithdraw")
public class ApiUserWithdrawController extends BaseController
{
    @Autowired
    private IUserWithdrawService userWithdrawService;

    @Autowired
    private ISwitchSetService switchSetService;

    /**
     * 查询提现记录列表
     */
    @GetMapping("/list")
    public TableDataInfo list(UserWithdraw userWithdraw)
    {
        userWithdraw.setUserId(SecurityUtils.getUserId());
        //app端充值记录是否展示彩金回收记录
        Integer selectSwitchStatusById78 = switchSetService.selectSwitchStatusById(78L);
        //app端充值提现记录是否展示上分下分记录
        Integer selectSwitchStatusById89 = switchSetService.selectSwitchStatusById(89L);
        startPage();
        if (selectSwitchStatusById78.equals(0) || selectSwitchStatusById89.equals(0)){
            startOrderBy("apply_time desc");
            if (selectSwitchStatusById78.equals(0)){
                userWithdraw.getParams().put("unionWinnings",0);
            }
            if (selectSwitchStatusById89.equals(0)){
                userWithdraw.getParams().put("unionDownPoint",0);
            }
            List<UserWithdraw> list = userWithdrawService.selectUserWithdrawListWithOthers(userWithdraw);
            return getDataTable(list);
        }
        startOrderBy("id desc");
        List<UserWithdraw> list = userWithdrawService.selectUserWithdrawList(userWithdraw);
        return getDataTable(list);
    }

    /**
     * 用户提现
     */
    @RepeatSubmit
    @PostMapping(value = "outMoney")
    @Log(title = "用户提现", businessType = BusinessType.OTHER,dict = UserWithdrawLogDict.class,
            saveParamNames = {"id","orderCode","withPwd","withdrawAmount","channelName","withdrawChannelConfigId","currencyId","currencyName","receiptAccountInfo","receivedAmount","withdrawFee","withdrawType"})
    public AjaxResult outMoney(BigDecimal withdrawAmount, String withPwd, Long withdrawAddressId, Long withdrawChannelConfigId) {
        if (withdrawChannelConfigId == null){
            return AjaxResult.error(HintConstants.PARAM_NULL,"请选择提现通道");
        }
        if (StringUtils.isEmpty(withPwd)){
            return AjaxResult.error(HintConstants.PARAM_NULL,"请输入提现密码");
        }
        if (withdrawAmount == null){
            return AjaxResult.error(HintConstants.PARAM_NULL,"请输入提现金额");
        }
        return toAjax(userWithdrawService.outMoney(withdrawAmount,withPwd,withdrawAddressId,withdrawChannelConfigId));
    }

    /**
     * 用户取消提现
     */
    @RepeatSubmit
    @PostMapping(value = "cancel")
    @Log(title = "用户取消提现", businessType = BusinessType.OTHER,dict = UserWithdrawLogDict.class,
            saveParamNames = {"id","orderCode","withdrawAmount","channelName","receiptAccountInfo","receivedAmount","withdrawFee","withdrawType"})
    public AjaxResult userCancel(Long userWithdrawId) {
        if (userWithdrawId == null){
            return AjaxResult.error(HintConstants.PARAM_NULL,"请选择需要取消提现的订单");
        }
        return toAjax(userWithdrawService.userCancel(userWithdrawId));
    }
}
