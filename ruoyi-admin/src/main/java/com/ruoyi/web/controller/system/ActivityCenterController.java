package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.ActivityCenter;
import com.ruoyi.system.service.IActivityCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 活动中心配置Controller
 * 
 * @author ruoyi
 * @date 2025-07-02
 */
@RestController
@RequestMapping("/system/activityCenter")
public class ActivityCenterController extends BaseController
{
    @Autowired
    private IActivityCenterService activityCenterService;

    /**
     * 查询活动中心配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:activityCenter:list')")
    @GetMapping("/list")
    public TableDataInfo list(ActivityCenter activityCenter)
    {
        startPage();
        startOrderBy("sort is null,sort");
        List<ActivityCenter> list = activityCenterService.selectActivityCenterList(activityCenter);
        return getDataTable(list);
    }

    /**
     * 导出活动中心配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:activityCenter:export')")
    @Log(title = "活动中心配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ActivityCenter activityCenter)
    {
        List<ActivityCenter> list = activityCenterService.selectActivityCenterList(activityCenter);
        ExcelUtil<ActivityCenter> util = new ExcelUtil<ActivityCenter>(ActivityCenter.class);
        util.exportExcel(response, list, "活动中心配置数据");
    }

    /**
     * 获取活动中心配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:activityCenter:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(activityCenterService.selectActivityCenterById(id));
    }

    /**
     * 新增活动中心配置
     */
    @PreAuthorize("@ss.hasPermi('system:activityCenter:add')")
    @Log(title = "活动中心配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ActivityCenter activityCenter)
    {
        return toAjax(activityCenterService.insertActivityCenter(activityCenter));
    }

    /**
     * 修改活动中心配置
     */
    @PreAuthorize("@ss.hasPermi('system:activityCenter:edit')")
    @Log(title = "活动中心配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ActivityCenter activityCenter)
    {
        return toAjax(activityCenterService.updateActivityCenter(activityCenter));
    }


    /**
     * 修改活动中心图片多语言
     */
    @PreAuthorize("@ss.hasPermi('system:activityCenter:edit')")
    @Log(title = "修改活动中心图片多语言", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping("updateActivityImgLang")
    public AjaxResult updateActivityImgLang(@RequestBody ActivityCenter activityCenter)
    {
        if (activityCenter.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (StringUtils.isEmpty(activityCenter.getActivityImgLang().getZh())){
            throw new ServiceException("请上传活动图片");
        }
        return toAjax(activityCenterService.updateActivityImgLang(activityCenter.getId(),activityCenter.getActivityImgLang()));
    }

    /**
     * 修改活动中心标题多语言
     */
    @PreAuthorize("@ss.hasPermi('system:activityCenter:edit')")
    @Log(title = "修改活动中心标题多语言", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping("updateActivityTitleLang")
    public AjaxResult updateActivityTitleLang(@RequestBody ActivityCenter activityCenter)
    {
        if (activityCenter.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (StringUtils.isEmpty(activityCenter.getActivityTitleLang().getZh())){
            throw new ServiceException("请输入活动标题");
        }
        return toAjax(activityCenterService.updateActivityTitleLang(activityCenter.getId(),activityCenter.getActivityTitleLang()));
    }

    /**
     * 修改活动中心内容多语言
     */
    @PreAuthorize("@ss.hasPermi('system:activityCenter:edit')")
    @Log(title = "修改活动中心内容多语言", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping("updateActivityContentLang")
    public AjaxResult updateActivityContentLang(@RequestBody ActivityCenter activityCenter)
    {
        if (activityCenter.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (StringUtils.isEmpty(activityCenter.getActivityContentLang().getZh())){
            throw new ServiceException("请输入活动内容");
        }
        return toAjax(activityCenterService.updateActivityContentLang(activityCenter.getId(),activityCenter.getActivityContentLang()));
    }

    /**
     * 删除活动中心配置
     */
    @PreAuthorize("@ss.hasPermi('system:activityCenter:remove')")
    @Log(title = "活动中心配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(activityCenterService.deleteActivityCenterByIds(ids));
    }
}
