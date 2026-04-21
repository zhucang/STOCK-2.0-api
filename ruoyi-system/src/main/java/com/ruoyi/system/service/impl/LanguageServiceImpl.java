package com.ruoyi.system.service.impl;

import com.ruoyi.common.constant.CacheableKey;
import com.ruoyi.system.domain.Language;
import com.ruoyi.system.mapper.LanguageMapper;
import com.ruoyi.system.service.ILanguageService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 国家语言Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-11-12
 */
@Service
public class LanguageServiceImpl implements ILanguageService 
{
    @Resource
    private LanguageMapper languageMapper;

    /**
     * 查询国家语言
     * 
     * @param id 国家语言主键
     * @return 国家语言
     */
    @Override
//    @Cacheable(value = CacheableKey.LANGUAGE + CacheableKey.ENTITY,key = "#id")
    public Language selectLanguageById(Long id)
    {
        return languageMapper.selectLanguageById(id);
    }

    /**
     * 查询国家语言列表
     * 
     * @param language 国家语言
     * @return 国家语言
     */
    @Override
    @Cacheable(value = CacheableKey.LANGUAGE + CacheableKey.LIST,key = "#language.cacheableKey()")
    public List<Language> selectLanguageList(Language language)
    {
        return languageMapper.selectLanguageList(language);
    }

    /**
     * 新增国家语言
     * 
     * @param language 国家语言
     * @return 结果
     */
    @Override
    @CacheEvict(value = CacheableKey.LANGUAGE + CacheableKey.LIST,allEntries = true)
    public int insertLanguage(Language language)
    {
        return languageMapper.insertLanguage(language);
    }

    /**
     * 修改国家语言
     * 
     * @param language 国家语言
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.LANGUAGE + CacheableKey.ENTITY,key = "#language.id"),
            @CacheEvict(value = CacheableKey.LANGUAGE + CacheableKey.LIST,allEntries = true)})
    public int updateLanguage(Language language)
    {
        return languageMapper.updateLanguage(language);
    }

    /**
     * 批量删除国家语言
     * 
     * @param ids 需要删除的国家语言主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.LANGUAGE + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.LANGUAGE + CacheableKey.LIST,allEntries = true)})
    public int deleteLanguageByIds(Long[] ids)
    {
        return languageMapper.deleteLanguageByIds(ids);
    }

    /**
     * 删除国家语言信息
     * 
     * @param id 国家语言主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.LANGUAGE + CacheableKey.ENTITY,key = "#id"),
            @CacheEvict(value = CacheableKey.LANGUAGE + CacheableKey.LIST,allEntries = true)})
    public int deleteLanguageById(Long id)
    {
        return languageMapper.deleteLanguageById(id);
    }
}
