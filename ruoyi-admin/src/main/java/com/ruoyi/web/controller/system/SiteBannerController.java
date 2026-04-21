package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SiteBanner;
import com.ruoyi.system.service.ISiteBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * banner横幅图片配置Controller
 * 
 * @author ruoyi
 * @date 2023-11-04
 */
@RestController
@RequestMapping("/system/siteBanner")
public class SiteBannerController extends BaseController
{
    @Autowired
    private ISiteBannerService siteBannerService;

    /**
     * 查询banner横幅图片配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:siteBanner:list')")
    @GetMapping("/list")
    public TableDataInfo list(SiteBanner siteBanner)
    {
        startPage();
        startOrderBy("sort is null,sort");
        List<SiteBanner> list = siteBannerService.selectSiteBannerList(siteBanner);
        return getDataTable(list);
    }

    /**
     * 获取banner横幅图片配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:siteBanner:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(siteBannerService.selectSiteBannerById(id));
    }

    /**
     * 新增banner横幅图片配置
     */
    @PreAuthorize("@ss.hasPermi('system:siteBanner:add')")
    @Log(title = "新增banner横幅图片配置", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody SiteBanner siteBanner)
    {
        if (StringUtils.isEmpty(siteBanner.getBannerImg())){
            throw new ServiceException("请上传横幅图片");
        }
        return toAjax(siteBannerService.insertSiteBanner(siteBanner));
    }

    /**
     * 修改banner横幅图片配置
     */
    @PreAuthorize("@ss.hasPermi('system:siteBanner:edit')")
    @Log(title = "修改banner横幅图片配置", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody SiteBanner siteBanner)
    {
        if (siteBanner.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (StringUtils.isEmpty(siteBanner.getBannerImgLang().getZh())){
            throw new ServiceException("请上传横幅图片");
        }
        return toAjax(siteBannerService.updateSiteBanner(siteBanner));
    }

    /**
     * 修改banner横幅图片多语言
     */
    @PreAuthorize("@ss.hasPermi('system:siteBanner:edit')")
    @Log(title = "修改banner横幅图片多语言", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping("updateBannerImgLang")
    public AjaxResult updateBannerImgLang(@RequestBody SiteBanner siteBanner)
    {
        if (siteBanner.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (StringUtils.isEmpty(siteBanner.getBannerImgLang().getZh())){
            throw new ServiceException("请上传横幅图片");
        }
        return toAjax(siteBannerService.updateBannerImgLang(siteBanner.getId(),siteBanner.getBannerImgLang()));
    }

    /**
     * 修改banner横幅标题多语言
     */
    @PreAuthorize("@ss.hasPermi('system:siteBanner:edit')")
    @Log(title = "修改banner横幅标题多语言", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping("updateBannerTitleLang")
    public AjaxResult updateBannerTitleLang(@RequestBody SiteBanner siteBanner)
    {
        if (siteBanner.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (StringUtils.isEmpty(siteBanner.getBannerTitleLang().getZh())){
            throw new ServiceException("请输入横幅标题");
        }
        return toAjax(siteBannerService.updateBannerTitleLang(siteBanner.getId(),siteBanner.getBannerTitleLang()));
    }

    /**
     * 修改banner横幅内容多语言
     */
    @PreAuthorize("@ss.hasPermi('system:siteBanner:edit')")
    @Log(title = "修改banner横幅内容多语言", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping("updateBannerContentLang")
    public AjaxResult updateBannerContentLang(@RequestBody SiteBanner siteBanner)
    {
        if (siteBanner.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (StringUtils.isEmpty(siteBanner.getBannerContentLang().getZh())){
            throw new ServiceException("请输入横幅内容");
        }
        return toAjax(siteBannerService.updateBannerContentLang(siteBanner.getId(),siteBanner.getBannerContentLang()));
    }

    /**
     * 删除banner横幅图片配置
     */
    @PreAuthorize("@ss.hasPermi('system:siteBanner:remove')")
    @Log(title = "删除banner横幅图片配置", businessType = BusinessType.DELETE)
    @RepeatSubmit
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(siteBannerService.deleteSiteBannerByIds(ids));
    }
}
