package com.ruoyi.web.controller.api;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.LangException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.UserTransferMoneyRecord;
import com.ruoyi.system.service.IUserTransferMoneyRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户转账记录Controller
 * 
 * @author ruoyi
 * @date 2025-05-14
 */
@RestController
@RequestMapping("/api/userTransferMoneyRecord")
public class ApiUserTransferMoneyRecordController extends BaseController
{
    @Autowired
    private IUserTransferMoneyRecordService userTransferMoneyRecordService;

    /**
     * 查询用户转账记录列表
     */
    @GetMapping("/list")
    public TableDataInfo list(UserTransferMoneyRecord userTransferMoneyRecord)
    {
        startPage();
        startOrderBy("user_transfer_money_record_id desc");
        //转账标志
        Object flag = userTransferMoneyRecord.getParams().get("flag");
        if ("0".equals(String.valueOf(flag))){
            userTransferMoneyRecord.setUserIdFrom(SecurityUtils.getUserId());
        }else {
            userTransferMoneyRecord.setUserIdTo(SecurityUtils.getUserId());
        }
        List<UserTransferMoneyRecord> list = userTransferMoneyRecordService.selectUserTransferMoneyRecordList(userTransferMoneyRecord);
        return getDataTable(list);
    }

    /**
     * 用户转账
     */
    @RepeatSubmit
    @PostMapping("/transferMoneyToOtherUser")
    @Log(title = "用户转账", businessType = BusinessType.OTHER)
    public AjaxResult transferMoneyToOtherUser(@RequestBody UserTransferMoneyRecord userTransferMoneyRecord)
    {
        userTransferMoneyRecord.setUserIdFrom(SecurityUtils.getUserId());
        if (StringUtils.isEmpty(userTransferMoneyRecord.getUserAccountTo())){
            throw new LangException(HintConstants.PARAM_NULL,"请选择收款用户账号");
        }
        if (userTransferMoneyRecord.getCurrencyId() == null){
            throw new LangException(HintConstants.PARAM_NULL,"请选择转账币种");
        }
        if (userTransferMoneyRecord.getTransferAmount() == null){
            throw new LangException(HintConstants.PARAM_NULL,"请输入转账金额");
        }
        if (userTransferMoneyRecord.getTransferAmount().compareTo(BigDecimal.ZERO) <= 0){
            throw new LangException("hint_93","转账金额必须大于0");
        }
        return toAjax(userTransferMoneyRecordService.transferMoneyToOtherUser(userTransferMoneyRecord));
    }
}
