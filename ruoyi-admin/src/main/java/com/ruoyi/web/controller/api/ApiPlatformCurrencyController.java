package com.ruoyi.web.controller.api;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.PlatformCurrency;
import com.ruoyi.system.service.IPlatformCurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 平台交易币种配置信息Controller
 * 
 * @author ruoyi
 * @date 2023-10-28
 */
@RestController
@RequestMapping("/api/platformCurrency")
public class ApiPlatformCurrencyController extends BaseController
{
    @Autowired
    private IPlatformCurrencyService platformCurrencyService;

    /**
     * 查询平台交易币种配置信息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(PlatformCurrency platformCurrency)
    {
        startPage();
        startOrderBy("sort is null,sort");
        platformCurrency.getParams().put("cacheableParam_sort","sort is null,sort");
        platformCurrency.setStatus(0);
        List<PlatformCurrency> list = platformCurrencyService.selectPlatformCurrencyList(platformCurrency);
        return getDataTable(list);
    }
}
