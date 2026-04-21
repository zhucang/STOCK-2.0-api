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
import com.ruoyi.system.domain.StakingOrder;
import com.ruoyi.system.service.IStakingOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * 质押订单Controller
 * 
 * @author ruoyi
 * @date 2025-07-17
 */
@RestController
@RequestMapping("/api/stakingOrder")
public class ApiStakingOrderController extends BaseController
{
    @Autowired
    private IStakingOrderService stakingOrderService;

    /**
     * 查询质押订单列表
     */
    @GetMapping("/list")
    public TableDataInfo list(StakingOrder stakingOrder)
    {
        stakingOrder.setUserId(SecurityUtils.getUserId());
        startPage();
        startOrderBy("id desc");
        List<StakingOrder> list = stakingOrderService.selectStakingOrderList(stakingOrder);
        return getDataTable(list);
    }


    /**
     * 用户质押代币
     */
    @RepeatSubmit
    @PostMapping(value = "add")
    @Log(title = "用户质押代币", businessType = BusinessType.OTHER)
    public AjaxResult addStakingOrderOrder(Long stakingProductId, BigDecimal buyPrice) {
        if (stakingProductId == null){
            throw new LangException(HintConstants.PARAM_NULL, "请选择要质押的质押产品");
        }
        if (buyPrice == null){
            throw new LangException(HintConstants.PARAM_NULL, "请选择质押金额");
        }
        return toAjax(stakingOrderService.addStakingOrderOrder(stakingProductId,buyPrice));
    }

    /**
     * 用户赎回质押金
     */
    @RepeatSubmit
    @PostMapping(value = "redemption")
    @Log(title = "用户赎回质押金", businessType = BusinessType.OTHER)
    public AjaxResult redemption(Long stakingOrderId) {
        if (stakingOrderId == null){
            throw new LangException(HintConstants.PARAM_NULL, "请选择需要赎回的质押订单");
        }
        return toAjax(stakingOrderService.redemption(stakingOrderId));
    }

}
