package com.ruoyi.web.controller.api;

import com.ruoyi.common.constant.HintConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.exception.LangException;
import com.ruoyi.system.domain.NewProductApplyPurchase;
import com.ruoyi.system.service.INewProductApplyPurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 新股新币申购初始配置Controller
 * 
 * @author ruoyi
 * @date 2023-11-30
 * cache待优化
 */
@RestController
@RequestMapping("/api/newProductApplyPurchase")
public class ApiNewProductApplyPurchaseController extends BaseController
{
    @Autowired
    private INewProductApplyPurchaseService newProductApplyPurchaseService;

    /**
     * 查询新股新币申购初始配置列表
     */
    @GetMapping("/list")
    public TableDataInfo list(NewProductApplyPurchase newProductApplyPurchase)
    {
        if (newProductApplyPurchase.getProductType() == null){
            throw new LangException(HintConstants.SYSTEM_BUSY,"请选择产品类型");
        }
        startPage();
        startOrderBy("id desc");
        newProductApplyPurchase.getParams().put("appView",0);
        List<NewProductApplyPurchase> list = newProductApplyPurchaseService.selectNewProductApplyPurchaseList(newProductApplyPurchase);
        return getDataTable(list);
    }
}
