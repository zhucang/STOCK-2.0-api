package com.ruoyi.web.controller.api;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.OrderFeeSetting;
import com.ruoyi.system.service.IOrderFeeSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 产品买入卖出手续费配置Controller
 * 
 * @author ruoyi
 * @date 2023-11-27
 */
@RestController
@RequestMapping("/api/orderFeeSetting")
public class ApiOrderFeeSettingController extends BaseController
{
    @Autowired
    private IOrderFeeSettingService orderFeeSettingService;

    /**
     * 查询产品买入卖出手续费配置列表
     */
    @GetMapping("/list")
    public TableDataInfo list(OrderFeeSetting orderFeeSetting)
    {
//        startPage();
        List<OrderFeeSetting> list = orderFeeSettingService.selectOrderFeeSettingList(orderFeeSetting);
        return getDataTable(list);
    }
}
