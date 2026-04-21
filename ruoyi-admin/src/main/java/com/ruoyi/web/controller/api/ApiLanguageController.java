package com.ruoyi.web.controller.api;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.Language;
import com.ruoyi.system.service.ILanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 国家语言Controller
 * 
 * @author ruoyi
 * @date 2023-11-12
 */
@RestController
@RequestMapping("/api/language")
public class ApiLanguageController extends BaseController
{
    @Autowired
    private ILanguageService languageService;

    /**
     * 查询国家语言列表
     */
    @GetMapping("/list")
    public TableDataInfo list(Language language)
    {
        startPage();
        startOrderBy("sort is null,sort");
        language.setStatus(0);
        List<Language> list = languageService.selectLanguageList(language);
        return getDataTable(list);
    }
}
