package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.WebsiteBanner;
import com.ruoyi.system.service.IWebsiteBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 官网B图片Controller
 * 
 * @author ruoyi
 * @date 2023-12-09
 */
@RestController
@RequestMapping("/system/websiteBanner")
public class WebsiteBannerController extends BaseController
{
    @Autowired
    private IWebsiteBannerService websiteBannerService;

    /**
     * 查询官网B图片列表
     */
    @PreAuthorize("@ss.hasPermi('system:websiteBanner:list')")
    @GetMapping("/list")
    public TableDataInfo list(WebsiteBanner websiteBanner)
    {
        startPage();
        List<WebsiteBanner> list = websiteBannerService.selectWebsiteBannerList(websiteBanner);
        return getDataTable(list);
    }

    /**
     * 获取官网B图片详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:websiteBanner:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(websiteBannerService.selectWebsiteBannerById(id));
    }

    /**
     * 修改官网B图片
     */
    @PreAuthorize("@ss.hasPermi('system:websiteBanner:edit')")
    @Log(title = "官网B图片", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WebsiteBanner websiteBanner)
    {
        if (websiteBanner.getId() == null){
            return AjaxResult.error("请选择需要修改的选项");
        }
        if (StringUtils.isEmpty(websiteBanner.getBannerImg())){
            return AjaxResult.error("请上传图片");
        }
        return toAjax(websiteBannerService.updateWebsiteBanner(websiteBanner));
    }
}
