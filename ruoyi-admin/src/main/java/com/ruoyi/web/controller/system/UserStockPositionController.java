package com.ruoyi.web.controller.system;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.logDict.UserStockPositionLogDict;
import com.ruoyi.system.domain.UserStockPosition;
import com.ruoyi.system.service.IUserStockPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户股票持仓Controller
 *
 * @author ruoyi
 * @date 2023-11-05
 */
@RestController
@RequestMapping("/system/userStockPosition")
public class UserStockPositionController extends BaseController
{
    @Autowired
    private IUserStockPositionService userStockPositionService;

//    /**
//     * 查询用户股票持仓列表
//     */
//    @PreAuthorize("@ss.hasPermi('system:userPosition:list')")
//    @GetMapping("/list")
//    public TableDataInfo list(UserStockPosition userStockPosition)
//    {
//        startPage();
//        if (userStockPosition.getOrderStatus() == null){
//            startOrderBy("order_status,id");
//        }else {
//            startOrderBy("id desc");
//        }
//        List<UserStockPosition> list = userStockPositionService.selectUserStockPositionList(userStockPosition);
//        PageHelper.clearPage();
//        userStockPositionService.fillOtherInfo(list);
//        return getDataTable(list);
//    }

    /**
     * 查询用户所有产品持仓列表
     */
    @PreAuthorize("@ss.hasPermi('system:userPosition:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserStockPosition userStockPosition)
    {
        startPage();
        if (userStockPosition.getOrderStatus() == null){
            startOrderBy("order_status,buy_order_time desc");
        }else {
            startOrderBy("id desc");
        }
        List<UserStockPosition> list = userStockPositionService.selectUserAllPositionList(userStockPosition);
        PageHelper.clearPage();
        userStockPositionService.fillOtherInfo2(list);
        return getDataTable(list);
    }

    /**
     * 获取用户股票持仓详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:userStockPosition:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(userStockPositionService.selectUserStockPositionById(id));
    }

    /**
     * 锁仓、解仓操作
     */
    @PreAuthorize("@ss.hasPermi('system:userStockPosition:lockUserPosition')")
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
        return userStockPositionService.lockUserPosition(positionId,lockStatus,lockMsg);
    }

    /**
     * 强制平仓操作
     */
    @PreAuthorize("@ss.hasPermi('system:userStockPosition:forceSell')")
    @Log(title = "强制平仓操作", businessType = BusinessType.UPDATE,dict = UserStockPositionLogDict.class,
            saveParamNames = {"id","orderCode","productCode","buyOrderPrice","sellOrderPrice","orderDirection","orderNum","orderLever","orderFee","orderYhsFee","currencyId","currencyName","profitAndLose","allProfitAndLose","doType"})
    @RepeatSubmit
    @PostMapping(value = "forceSell")
    public AjaxResult forceSell(Long positionId,Integer sellMode,BigDecimal target){
        if (positionId == null){
            throw new ServiceException("请选择需要平仓的订单");
        }
        if (sellMode == null){
            throw new ServiceException("请选择平仓模式");
        }
        if (target == null){
            throw new ServiceException("请输入价格/比例/金额");
        }
        AjaxResult ajaxResult = userStockPositionService.forceSell(positionId,sellMode,target);
        ajaxResult.put(AjaxResult.MSG_TAG,ajaxResult.get(AjaxResult.DATA_TAG));
        return ajaxResult;
    }



}
