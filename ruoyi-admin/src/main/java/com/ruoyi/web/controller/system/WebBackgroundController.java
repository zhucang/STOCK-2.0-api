package com.ruoyi.web.controller.system;


import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.LangMgr;
import com.ruoyi.system.service.IWebBackgroundService;
import com.ruoyi.system.utils.PolygonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 优化*
 * 系统后台
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/webBackground")
public class WebBackgroundController {

    @Autowired
    private IWebBackgroundService webBackgroundService;


    /**
     * 股票、加密货币、外汇市场
     * @param productCode 产品代码
     * @param productType 产品类型 1：美股 2：加密货币 4：外汇
     * @param nextPage
     * @return
     */
    @PreAuthorize("@ss.hasPermi('system:webBackground:tickersMarket')")
    @GetMapping(value = "tickersMarket")
    public AjaxResult tickersMarket(String productCode, Integer productType, String nextPage){
        if (productType == null){
            return AjaxResult.error("请选择产品类型");
        }
        Map<String, Object> map = PolygonUtils.tickersMarket(productCode, productType,nextPage);
        return AjaxResult.success(map);
    }

    /**
     * 后台提醒
     */
    @GetMapping(value = "reminder")
    public AjaxResult reminder(){
        return webBackgroundService.getReminder(new BaseEntity());
    }

    /**
     * 导出产品名称多语言包
     */
    @PostMapping(value = "exportProductNameLang")
    public void exportProductNameLang(HttpServletResponse response)
    {
        List<LangMgr> list = webBackgroundService.selectProductNameLang();
        ExcelUtil<LangMgr> util = new ExcelUtil<LangMgr>(LangMgr.class);
        util.exportExcel(response, list, "股票、加密货币、期货、外汇名称语言包");
    }

    /**
     * 导入产品名称多语言包
     */
    @PreAuthorize("@ss.hasPermi('system:webBackground:importProductNameLang')")
    @Log(title = "导入产品名称多语言包", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping(value = "importProductNameLang")
    public AjaxResult importData(@RequestParam("file") MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<LangMgr> util = new ExcelUtil<LangMgr>(LangMgr.class);
        List<LangMgr> list = util.importExcel(file.getInputStream());
        String message = webBackgroundService.importProductNameLang(list, updateSupport);
        return AjaxResult.success(message);
    }

    /**
     * 后台首页报表
     */
    @GetMapping(value = "indexReport")
    public AjaxResult indexReport(){
        return webBackgroundService.indexReport();
    }
}
