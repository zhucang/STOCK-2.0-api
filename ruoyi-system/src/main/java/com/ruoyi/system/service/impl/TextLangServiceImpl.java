package com.ruoyi.system.service.impl;

import com.ruoyi.common.constant.CacheableKey;
import com.ruoyi.system.domain.LangMgr;
import com.ruoyi.system.domain.TextLang;
import com.ruoyi.system.mapper.TextLangMapper;
import com.ruoyi.system.service.ITextLangService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 文本多语言Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-12-13
 */
@Service
public class TextLangServiceImpl implements ITextLangService 
{
    @Resource
    private TextLangMapper textLangMapper;

    /**
     * 查询文本多语言
     * 
     * @param id 文本多语言主键
     * @return 文本多语言
     */
    @Override
//    @Cacheable(value = CacheableKey.TEXT_LANG + CacheableKey.ENTITY,key = "#id")
    public TextLang selectTextLangById(Long id)
    {
        return textLangMapper.selectTextLangById(id);
    }

    /**
     * 查询文本多语言列表
     * 
     * @param textLang 文本多语言
     * @return 文本多语言
     */
    @Override
    @Cacheable(value = CacheableKey.TEXT_LANG + CacheableKey.LIST,key = "#textLang.cacheableKey()")
    public List<TextLang> selectTextLangList(TextLang textLang)
    {
        return textLangMapper.selectTextLangList(textLang);
    }

    /**
     * 新增文本多语言
     * 
     * @param textLang 文本多语言
     * @return 结果
     */
    @Override
    @CacheEvict(value = CacheableKey.TEXT_LANG + CacheableKey.LIST,allEntries = true)
    public int insertTextLang(TextLang textLang)
    {
        return textLangMapper.insertTextLang(textLang);
    }

    /**
     * 修改文本多语言
     * 
     * @param textLang 文本多语言
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.TEXT_LANG + CacheableKey.ENTITY,key = "#textLang.id"),
            @CacheEvict(value = CacheableKey.TEXT_LANG + CacheableKey.LIST,allEntries = true)})
    public int updateTextLang(TextLang textLang)
    {
        return textLangMapper.updateTextLang(textLang);
    }

    /**
     * 修改文本内容多语言
     * @param textLangId 文本多语言id
     * @param contentLang 文本内容语言包
     * @return
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.TEXT_LANG + CacheableKey.ENTITY,key = "#textLangId"),
            @CacheEvict(value = CacheableKey.TEXT_LANG + CacheableKey.LIST,allEntries = true)})
    public int updateContentLang(Long textLangId, LangMgr contentLang){
        TextLang textLang = new TextLang();
        textLang.setId(textLangId);
        textLang.setContentLang(contentLang);
        return textLangMapper.updateTextLang(textLang);
    }

    /**
     * 批量删除文本多语言
     * 
     * @param ids 需要删除的文本多语言主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.TEXT_LANG + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.TEXT_LANG + CacheableKey.LIST,allEntries = true)})
    public int deleteTextLangByIds(Long[] ids)
    {
        return textLangMapper.deleteTextLangByIds(ids);
    }

    /**
     * 删除文本多语言信息
     * 
     * @param id 文本多语言主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.TEXT_LANG + CacheableKey.ENTITY,key = "#id"),
            @CacheEvict(value = CacheableKey.TEXT_LANG + CacheableKey.LIST,allEntries = true)})
    public int deleteTextLangById(Long id)
    {
        return textLangMapper.deleteTextLangById(id);
    }
}
