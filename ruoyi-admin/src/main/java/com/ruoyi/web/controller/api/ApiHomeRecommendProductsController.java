package com.ruoyi.web.controller.api;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.HomeRecommendProducts;
import com.ruoyi.system.service.IHomeRecommendProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 首页推荐产品Controller
 * 
 * @author ruoyi
 * @date 2024-01-09
 * cache待优化
 */
@RestController
@RequestMapping("/api/homeRecommendProducts")
public class ApiHomeRecommendProductsController extends BaseController
{
    @Autowired
    private IHomeRecommendProductsService homeRecommendProductsService;

    /**
     * 查询首页推荐产品列表
     */
    @GetMapping("/list")
    public TableDataInfo list(HomeRecommendProducts homeRecommendProducts)
    {
        startPage();
        startOrderBy("sort is null,sort");
        homeRecommendProducts.setIsVisible(0);
        homeRecommendProducts.getParams().put("getQuote",0);
        List<HomeRecommendProducts> list = homeRecommendProductsService.selectHomeRecommendProductsList(homeRecommendProducts);
        return getDataTable(list);
    }
}
