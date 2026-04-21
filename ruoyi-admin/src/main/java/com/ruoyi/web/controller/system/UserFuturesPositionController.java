package com.ruoyi.web.controller.system;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.logDict.UserFuturesPositionLogDict;
import com.ruoyi.system.domain.UserFuturesPosition;
import com.ruoyi.system.service.IUserFuturesPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户期货持仓Controller
 * 
 * @author ruoyi
 * @date 2023-11-05
 */
@RestController
@RequestMapping("/system/userFuturesPosition")
public class UserFuturesPositionController extends BaseController
{
    @Autowired
    private IUserFuturesPositionService userFuturesPositionService;

    /**
     * 查询用户期货持仓列表
     */
    @PreAuthorize("@ss.hasPermi('system:userPosition:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserFuturesPosition userFuturesPosition)
    {
        startPage();
        if (userFuturesPosition.getOrderStatus() == null){
            startOrderBy("order_status,id");
        }else {
            startOrderBy("id desc");
        }
        List<UserFuturesPosition> list = userFuturesPositionService.selectUserFuturesPositionList(userFuturesPosition);
        PageHelper.clearPage();
        userFuturesPositionService.fillOtherInfo(list);
        return getDataTable(list);
    }

    /**
     * 获取用户期货持仓详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:userFuturesPosition:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(userFuturesPositionService.selectUserFuturesPositionById(id));
    }

    /**
     * 锁仓、解仓操作
     */
    @PreAuthorize("@ss.hasPermi('system:userFuturesPosition:lockUserPosition')")
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
        return userFuturesPositionService.lockUserPosition(positionId,lockStatus,lockMsg);
    }

    /**
     * 强制平仓操作
     */
    @PreAuthorize("@ss.hasPermi('system:userFuturesPosition:forceSell')")
    @Log(title = "强制平仓操作", businessType = BusinessType.UPDATE,dict = UserFuturesPositionLogDict.class,
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
        AjaxResult ajaxResult = userFuturesPositionService.forceSell(positionId,sellMode,target);
        ajaxResult.put(AjaxResult.MSG_TAG,ajaxResult.get(AjaxResult.DATA_TAG));
        return ajaxResult;
    }
}
