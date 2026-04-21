package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.TextLang;

import java.util.List;

/**
 * 文本多语言Mapper接口
 * 
 * @author ruoyi
 * @date 2023-12-13
 */
public interface TextLangMapper 
{
    /**
     * 查询文本多语言
     * 
     * @param id 文本多语言主键
     * @return 文本多语言
     */
    public TextLang selectTextLangById(Long id);

    /**
     * 查询文本多语言列表
     * 
     * @param textLang 文本多语言
     * @return 文本多语言集合
     */
    public List<TextLang> selectTextLangList(TextLang textLang);

    /**
     * 新增文本多语言
     * 
     * @param textLang 文本多语言
     * @return 结果
     */
    public int insertTextLang(TextLang textLang);

    /**
     * 修改文本多语言
     * 
     * @param textLang 文本多语言
     * @return 结果
     */
    public int updateTextLang(TextLang textLang);

    /**
     * 删除文本多语言
     * 
     * @param id 文本多语言主键
     * @return 结果
     */
    public int deleteTextLangById(Long id);

    /**
     * 批量删除文本多语言
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTextLangByIds(Long[] ids);
}
