package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.logDict.PopUpLogDict;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.PopUp;
import com.ruoyi.system.service.IPopUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 弹窗信息Controller
 * 
 * @author ruoyi
 * @date 2024-03-21
 */
@RestController
@RequestMapping("/system/popUp")
public class PopUpController extends BaseController
{
    @Autowired
    private IPopUpService popUpService;

    /**
     * 查询弹窗信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:popUp:list')")
    @GetMapping("/list")
    public TableDataInfo list(PopUp popUp)
    {
        startPage();
        List<PopUp> list = popUpService.selectPopUpList(popUp);
        return getDataTable(list);
    }

    /**
     * 导出弹窗信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:popUp:export')")
    @Log(title = "弹窗信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PopUp popUp)
    {
        List<PopUp> list = popUpService.selectPopUpList(popUp);
        ExcelUtil<PopUp> util = new ExcelUtil<PopUp>(PopUp.class);
        util.exportExcel(response, list, "弹窗信息数据");
    }

    /**
     * 获取弹窗信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:popUp:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(popUpService.selectPopUpById(id));
    }

    /**
     * 新增弹窗信息
     */
    @PreAuthorize("@ss.hasPermi('system:popUp:add')")
    @Log(title = "新增弹窗信息", businessType = BusinessType.INSERT,dict = PopUpLogDict.class,
            saveParamNames = {"id","popUpTitle","popUpContent"})
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody PopUp popUp)
    {
        if (StringUtils.isEmpty(popUp.getPopUpTitle())){
            throw new ServiceException("请输入弹窗标题");
        }
        if (StringUtils.isEmpty(popUp.getPopUpContent())){
            throw new ServiceException("请输入弹窗内容");
        }
        return toAjax(popUpService.insertPopUp(popUp));
    }

    /**
     * 修改弹窗信息
     */
    @PreAuthorize("@ss.hasPermi('system:popUp:edit')")
    @Log(title = "修改弹窗信息", businessType = BusinessType.UPDATE,dict = PopUpLogDict.class,
            saveParamNames = {"id","popUpTitle","popUpContent"})
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody PopUp popUp)
    {
        if (popUp.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (StringUtils.isEmpty(popUp.getPopUpTitle())){
            throw new ServiceException("请输入弹窗标题");
        }
        if (StringUtils.isEmpty(popUp.getPopUpContent())){
            throw new ServiceException("请输入弹窗内容");
        }
        return toAjax(popUpService.updatePopUp(popUp));
    }

    /**
     * 删除弹窗信息
     */
    @PreAuthorize("@ss.hasPermi('system:popUp:remove')")
    @Log(title = "删除弹窗信息", businessType = BusinessType.DELETE,dict = PopUpLogDict.class,
            saveParamNames = {"id","popUpTitle","popUpContent","popUps"})
    @RepeatSubmit
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(popUpService.deletePopUpByIds(ids));
    }
}
