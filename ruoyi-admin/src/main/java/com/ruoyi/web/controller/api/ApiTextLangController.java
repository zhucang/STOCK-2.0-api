package com.ruoyi.web.controller.api;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.TextLang;
import com.ruoyi.system.service.ITextLangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 文本多语言Controller
 * 
 * @author ruoyi
 * @date 2023-12-13
 */
@RestController
@RequestMapping("/api/textLang")
public class ApiTextLangController extends BaseController
{
    @Autowired
    private ITextLangService textLangService;

    /**
     * 查询文本多语言列表
     */
    @GetMapping("/list")
    public TableDataInfo list(TextLang textLang)
    {
        startPage();
        List<TextLang> list = textLangService.selectTextLangList(textLang);
        return getDataTable(list);
    }
}
