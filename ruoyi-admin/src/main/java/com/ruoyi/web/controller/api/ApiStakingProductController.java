package com.ruoyi.web.controller.api;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.StakingProduct;
import com.ruoyi.system.service.IStakingProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 质押产品配置Controller
 * 
 * @author ruoyi
 * @date 2025-07-17
 */
@RestController
@RequestMapping("/api/stakingProduct")
public class ApiStakingProductController extends BaseController
{
    @Autowired
    private IStakingProductService stakingProductService;

    /**
     * 查询质押产品配置列表
     */
    @GetMapping("/list")
    public TableDataInfo list(StakingProduct stakingProduct)
    {
        startPage();
        startOrderBy("a.sort is null,a.sort");
        stakingProduct.setStatus(0);
        List<StakingProduct> list = stakingProductService.selectStakingProductList(stakingProduct);
        return getDataTable(list);
    }
}
