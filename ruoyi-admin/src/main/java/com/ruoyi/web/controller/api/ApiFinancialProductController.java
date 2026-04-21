package com.ruoyi.web.controller.api;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.FinancialProduct;
import com.ruoyi.system.service.IFinancialProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 理财产品配置Controller
 * 
 * @author ruoyi
 * @date 2023-11-26
 */
@RestController
@RequestMapping("/api/financialProduct")
public class ApiFinancialProductController extends BaseController
{
    @Autowired
    private IFinancialProductService financialProductService;

    /**
     * 查询理财产品配置列表
     */
    @GetMapping("/list")
    public TableDataInfo list(FinancialProduct financialProduct)
    {
        startPage();
        startOrderBy("a.sort is null,a.sort");
        financialProduct.setStatus(0);
        List<FinancialProduct> list = financialProductService.selectFinancialProductList(financialProduct);
        return getDataTable(list);
    }
}
