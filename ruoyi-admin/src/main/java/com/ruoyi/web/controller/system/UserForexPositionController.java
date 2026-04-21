package com.ruoyi.web.controller.system;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.logDict.UserForexPositionLogDict;
import com.ruoyi.system.domain.UserForexPosition;
import com.ruoyi.system.service.IUserForexPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户外汇持仓Controller
 * 
 * @author ruoyi
 * @date 2023-11-05
 */
@RestController
@RequestMapping("/system/userForexPosition")
public class UserForexPositionController extends BaseController
{
    @Autowired
    private IUserForexPositionService userForexPositionService;

    /**
     * 查询用户外汇持仓列表
     */
    @PreAuthorize("@ss.hasPermi('system:userPosition:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserForexPosition userForexPosition)
    {
        startPage();
        if (userForexPosition.getOrderStatus() == null){
            startOrderBy("order_status,id");
        }else {
            startOrderBy("id desc");
        }
        List<UserForexPosition> list = userForexPositionService.selectUserForexPositionList(userForexPosition);
        PageHelper.clearPage();
        userForexPositionService.fillOtherInfo(list);
        return getDataTable(list);
    }

    /**
     * 获取用户外汇持仓详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:userForexPosition:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(userForexPositionService.selectUserForexPositionById(id));
    }

    /**
     * 锁仓、解仓操作
     */
    @PreAuthorize("@ss.hasPermi('system:userForexPosition:lockUserPosition')")
    @Log(title = "锁仓、解仓操作", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping(value = "lockUserPosition")
    public AjaxResult lockUserPosition(Long positionId, Integer lockStatus, String lockMsg) {
        if (positionId == null){
            throw new ServiceException("请选择需要操作的持仓订单");
        }
        if (lockStatus == null){
            throw new ServiceException("请选择操作状态");
        }
        return userForexPositionService.lockUserPosition(positionId,lockStatus,lockMsg);
    }

    /**
     * 强制平仓操作
     */
    @PreAuthorize("@ss.hasPermi('system:userForexPosition:forceSell')")
    @Log(title = "强制平仓操作", businessType = BusinessType.UPDATE,dict = UserForexPositionLogDict.class,
            saveParamNames = {"id","orderCode","productCode","buyOrderPrice","sellOrderPrice","orderDirection","orderNum","orderLever","orderFee","orderYhsFee","currencyId","currencyName","profitAndLose","allProfitAndLose","doType"})
    @RepeatSubmit
    @PostMapping(value = "forceSell")
    public AjaxResult forceSell(Long positionId, Integer sellMode, BigDecimal target){
        if (positionId == null){
            throw new ServiceException("请选择需要平仓的订单");
        }
        if (sellMode == null){
            throw new ServiceException("请选择平仓模式");
        }
        if (target == null){
            throw new ServiceException("请输入价格/比例/金额");
        }
        AjaxResult ajaxResult = userForexPositionService.forceSell(positionId,sellMode,target);
        ajaxResult.put(AjaxResult.MSG_TAG,ajaxResult.get(AjaxResult.DATA_TAG));
        return ajaxResult;
    }
}
