package com.ruoyi.web.controller.system;

import cn.hutool.core.date.DateUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.logDict.UserRechargeLogDict;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.PageUtils;
import com.ruoyi.system.domain.UserRecharge;
import com.ruoyi.system.service.IUserRechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户充值订单Controller
 * 
 * @author ruoyi
 * @date 2023-10-30
 */
@RestController
@RequestMapping("/system/userRecharge")
public class UserRechargeController extends BaseController
{

    @Autowired
    private IUserRechargeService userRechargeService;

    /**
     * 查询用户充值订单列表
     */
    @PreAuthorize("@ss.hasPermi('system:userRecharge:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserRecharge userRecharge)
    {
        startPage();
        PageUtils.getLocalPage().setUnsafeOrderBy("field(`order_status`,3,1,0,2),id desc");
        List<UserRecharge> list = userRechargeService.selectUserRechargeList(userRecharge);
        userRechargeService.fillOtherInfo(list);
        TableDataInfo dataTable = getDataTable(list);
        //统计数据
        userRecharge.setPageNum(null);
        userRecharge.setPageSize(null);
        List<UserRecharge> statisticalData = userRechargeService.getStatisticalData(userRecharge);
        dataTable.getMapData().put("statisticalData",statisticalData);
        return dataTable;
    }

    /**
     * 查询用户充值订单列表
     */
    @GetMapping("/listNoPreAuthorize")
    public TableDataInfo listNoPreAuthorize(UserRecharge userRecharge)
    {
        startPage();
        startOrderBy("id desc");
        //如果只查看上月
        String isLastMonth = String.valueOf(userRecharge.getParams().get("isLastMonth"));
        if (isLastMonth != null && isLastMonth.equals("1")){
            userRecharge.setStartTime(DateUtil.date(DateUtils.getLastMonthStartTime()));
            userRecharge.setEndTime(DateUtil.date(DateUtils.getLastMonthEndTime()));
        }
        userRecharge.getParams().put("currencyStatus",0);
        List<UserRecharge> list = userRechargeService.selectUserRechargeList(userRecharge);
        return getDataTable(list);
    }

    /**
     * 获取用户充值订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:userRecharge:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(userRechargeService.selectUserRechargeById(id));
    }

    /**
     * 充值订单审核
     */
    @PreAuthorize("@ss.hasPermi('system:userRecharge:updateRechargeOrderStatus')")
    @Log(title = "充值订单审核", businessType = BusinessType.UPDATE,dict = UserRechargeLogDict.class,
            saveParamNames = {"id","orderCode","currencyId","currencyName","rechargeAmount","orderStatus","rechargeMsg","rechargeMethod","userAmountBefore","userAmountAfter","remark","payChannelName"})
    @RepeatSubmit
    @PostMapping(value = "updateRechargeOrderStatus")
    public AjaxResult updateRechargeOrderStatus(Long userRechargeId,Integer orderStatus,String rechargeMsg,String remark) {
        if (userRechargeId == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (orderStatus == null){
            throw new ServiceException("请选择审核状态");
        }
        if (!orderStatus.equals(0) && !orderStatus.equals(1) && !orderStatus.equals(2) && !orderStatus.equals(3)){
            throw new ServiceException("审核状态错误");
        }
        return userRechargeService.updateRechargeOrderStatus(userRechargeId, orderStatus,rechargeMsg,remark);
    }

    /**
     * 修改充值订单金额
     */
    @PreAuthorize("@ss.hasPermi('system:userRecharge:updateRechargeAmount')")
    @Log(title = "修改充值订单金额", businessType = BusinessType.UPDATE,dict = UserRechargeLogDict.class,
            saveParamNames = {"id","orderCode","rechargeAmount","修改前金额"})
    @RepeatSubmit
    @PostMapping(value = "updateRechargeAmount")
    public AjaxResult updateRechargeAmount(Long userRechargeId, BigDecimal rechargeAmount) {
        if (userRechargeId == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (rechargeAmount == null){
            throw new ServiceException("请输入充值金额");
        }
        if (rechargeAmount.compareTo(BigDecimal.ZERO) <= 0){
            throw new ServiceException("充值金额必须大于0");
        }
        return userRechargeService.updateRechargeAmount(userRechargeId, rechargeAmount);
    }

    /**
     * 修改订单备注
     */
    @PreAuthorize("@ss.hasPermi('system:userRecharge:updateOrderRemark')")
    @Log(title = "修改订单备注", businessType = BusinessType.UPDATE,dict = UserRechargeLogDict.class,
            saveParamNames = {"id","orderCode","remark"})
    @RepeatSubmit
    @PostMapping(value = "updateOrderRemark")
    public AjaxResult updateRechargeRemark(Long userRechargeId, String remark) {
        if (userRechargeId == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        return userRechargeService.updateOrderRemark(userRechargeId, remark);
    }
}
