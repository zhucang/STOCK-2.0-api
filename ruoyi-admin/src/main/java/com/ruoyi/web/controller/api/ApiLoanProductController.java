package com.ruoyi.web.controller.api;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.LoanProduct;
import com.ruoyi.system.service.ILoanProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 贷款产品配置Controller
 * 
 * @author ruoyi
 * @date 2024-05-21
 */
@RestController
@RequestMapping("/api/loanProduct")
public class ApiLoanProductController extends BaseController
{
    @Autowired
    private ILoanProductService loanProductService;

    /**
     * 查询贷款产品配置列表
     */
    @GetMapping("/list")
    public TableDataInfo list(LoanProduct loanProduct)
    {
        startPage();
        startOrderBy("a.sort is null,a.sort");
        List<LoanProduct> list = loanProductService.selectLoanProductList(loanProduct);
        return getDataTable(list);
    }
}
