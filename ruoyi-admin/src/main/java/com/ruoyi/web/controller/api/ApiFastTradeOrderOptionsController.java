package com.ruoyi.web.controller.api;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.FastTradeOrderOptions;
import com.ruoyi.system.service.IFastTradeOrderOptionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 极速交易下单选项Controller
 * 
 * @author ruoyi
 * @date 2023-11-02
 * 已优化
 */
@RestController
@RequestMapping("/api/fastTradeOrderOptions")
public class ApiFastTradeOrderOptionsController extends BaseController
{
    @Autowired
    private IFastTradeOrderOptionsService fastTradeOrderOptionsService;

    /**
     * 查询极速交易下单选项列表
     */
    @GetMapping("/list")
    public TableDataInfo list(FastTradeOrderOptions fastTradeOrderOptions)
    {
        if (fastTradeOrderOptions.getProductCode() == null){
            return getDataTable(new ArrayList<>());
//            return AjaxResult.error(HintConstants.PARAM_NULL,"请选择查询玩法的产品");
        }
        if (fastTradeOrderOptions.getProductType() == null){
            return getDataTable(new ArrayList<>());
//            return AjaxResult.error(HintConstants.PARAM_NULL,"参数不能为空");
        }
        startPage();
        fastTradeOrderOptions.setStatus(0);
//        startOrderBy("duration_label_value*duration_value");
        startOrderBy("product_code,sort is null,sort");
        List<FastTradeOrderOptions> list = fastTradeOrderOptionsService.selectFastTradeOrderOptionsList(fastTradeOrderOptions);
        return getDataTable(list);
    }
}
