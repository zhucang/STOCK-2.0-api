package com.ruoyi.web.controller.api;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.CustomerService;
import com.ruoyi.system.service.ICustomerServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 客服配置Controller
 * 
 * @author ruoyi
 * @date 2023-11-04
 * 已优化
 */
@RestController
@RequestMapping("/api/customerService")
public class ApiCustomerServiceController extends BaseController
{
    @Autowired
    private ICustomerServiceService customerServiceService;

    /**
     * 查询客服配置列表
     */
    @GetMapping("/list")
    public TableDataInfo list(CustomerService customerService)
    {
        startPage();
        customerService.setStatus(0);
        List<CustomerService> list = customerServiceService.selectCustomerServiceList(customerService);
        return getDataTable(list);
    }
}
