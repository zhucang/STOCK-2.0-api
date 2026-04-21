package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.Language;
import com.ruoyi.system.service.ILanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 国家语言Controller
 * 
 * @author ruoyi
 * @date 2023-11-12
 */
@RestController
@RequestMapping("/system/language")
public class LanguageController extends BaseController
{
    @Autowired
    private ILanguageService languageService;

    /**
     * 查询国家语言列表
     */
//    @PreAuthorize("@ss.hasPermi('system:language:list')")
    @GetMapping("/list")
    public TableDataInfo list(Language language)
    {
        startPage();
        startOrderBy("sort is null,sort");
        List<Language> list = languageService.selectLanguageList(language);
        return getDataTable(list);
    }

    /**
     * 获取国家语言详细信息
     */
//    @PreAuthorize("@ss.hasPermi('system:language:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(languageService.selectLanguageById(id));
    }


    /**
     * 修改国家语言
     */
    @PreAuthorize("@ss.hasPermi('system:language:edit')")
    @Log(title = "修改国家语言", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody Language language)
    {
        if (language.getId() == null){
            throw new ServiceException("请选择需要修改的选项");
        }
        if (StringUtils.isEmpty(language.getValue())){
            throw new ServiceException("请输入译本");
        }
        return toAjax(languageService.updateLanguage(language));
    }
}
