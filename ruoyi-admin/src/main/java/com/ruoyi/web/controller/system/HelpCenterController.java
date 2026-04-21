package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.logDict.HelpCenterLogDict;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.HelpCenter;
import com.ruoyi.system.service.IHelpCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 帮助中心Controller
 * 
 * @author ruoyi
 * @date 2023-11-22
 * 已优化日志
 */
@RestController
@RequestMapping("/system/helpCenter")
public class HelpCenterController extends BaseController
{
    @Autowired
    private IHelpCenterService helpCenterService;

    /**
     * 查询帮助中心列表
     */
    @PreAuthorize("@ss.hasPermi('system:helpCenter:list')")
    @GetMapping("/list")
    public TableDataInfo list(HelpCenter helpCenter)
    {
        startPage();
        startOrderBy("sort is null,sort");
        List<HelpCenter> list = helpCenterService.selectHelpCenterList(helpCenter);
        return getDataTable(list);
    }

    /**
     * 获取帮助中心详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:helpCenter:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(helpCenterService.selectHelpCenterById(id));
    }

    /**
     * 新增帮助中心
     */
    @PreAuthorize("@ss.hasPermi('system:helpCenter:add')")
    @Log(title = "新增帮助中心", businessType = BusinessType.INSERT,dict = HelpCenterLogDict.class,
            saveParamNames = {"id","question","answer"})
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody HelpCenter helpCenter)
    {
        if (StringUtils.isEmpty(helpCenter.getQuestion())){
            throw new ServiceException("请输入问题");
        }
        if (StringUtils.isEmpty(helpCenter.getAnswer())){
            throw new ServiceException("请输入答案");
        }
        return toAjax(helpCenterService.insertHelpCenter(helpCenter));
    }

    /**
     * 修改帮助中心
     */
    @PreAuthorize("@ss.hasPermi('system:helpCenter:edit')")
    @Log(title = "修改帮助中心", businessType = BusinessType.UPDATE,dict = HelpCenterLogDict.class,
            saveParamNames = {"id","question","answer"})
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody HelpCenter helpCenter)
    {
        if (helpCenter.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (StringUtils.isEmpty(helpCenter.getQuestion())){
            throw new ServiceException("请输入问题");
        }
        if (StringUtils.isEmpty(helpCenter.getAnswer())){
            throw new ServiceException("请输入答案");
        }
        return toAjax(helpCenterService.updateHelpCenter(helpCenter));
    }

    /**
     * 删除帮助中心
     */
    @PreAuthorize("@ss.hasPermi('system:helpCenter:remove')")
    @Log(title = "删除帮助中心", businessType = BusinessType.DELETE,dict = HelpCenterLogDict.class,
            saveParamNames = {"id","question","answer","helpCenters"})
    @RepeatSubmit
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(helpCenterService.deleteHelpCenterByIds(ids));
    }
}
