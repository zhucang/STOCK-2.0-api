package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.TextLang;
import com.ruoyi.system.service.ITextLangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文本多语言Controller
 * 
 * @author ruoyi
 * @date 2023-12-13
 */
@RestController
@RequestMapping("/system/textLang")
public class TextLangController extends BaseController
{
    @Autowired
    private ITextLangService textLangService;

    /**
     * 查询文本多语言列表
     */
    @PreAuthorize("@ss.hasPermi('system:textLang:list')")
    @GetMapping("/list")
    public TableDataInfo list(TextLang textLang)
    {
        startPage();
        List<TextLang> list = textLangService.selectTextLangList(textLang);
        return getDataTable(list);
    }

    /**
     * 获取文本多语言详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:textLang:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(textLangService.selectTextLangById(id));
    }

    /**
     * 新增文本多语言
     */
    @PreAuthorize("@ss.hasPermi('system:textLang:add')")
    @Log(title = "新增文本多语言", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping
    public AjaxResult add(@RequestBody TextLang textLang)
    {
        return toAjax(textLangService.insertTextLang(textLang));
    }

    /**
     * 修改文本多语言
     */
    @PreAuthorize("@ss.hasPermi('system:textLang:edit')")
    @Log(title = "修改文本多语言", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping
    public AjaxResult edit(@RequestBody TextLang textLang)
    {
        return toAjax(textLangService.updateTextLang(textLang));
    }

    /**
     * 修改文本内容多语言
     */
    @PreAuthorize("@ss.hasPermi('system:textLang:edit')")
    @Log(title = "修改文本内容多语言", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PostMapping(value = "updateContentLang")
    public AjaxResult updateContentLang(@RequestBody TextLang textLang)
    {
        if (textLang.getId() == null){
            return AjaxResult.error("请选择需要修改的选项");
        }
        if (StringUtils.isEmpty(textLang.getContentLang().getZh())){
            return AjaxResult.error("请输入币种名称");
        }
        return toAjax(textLangService.updateContentLang(textLang.getId(),textLang.getContentLang()));
    }

    /**
     * 删除文本多语言
     */
    @PreAuthorize("@ss.hasPermi('system:textLang:remove')")
    @Log(title = "删除文本多语言", businessType = BusinessType.DELETE)
    @RepeatSubmit
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(textLangService.deleteTextLangByIds(ids));
    }
}
