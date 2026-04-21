package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.Language;

import java.util.List;

/**
 * 国家语言Mapper接口
 * 
 * @author ruoyi
 * @date 2023-11-12
 */
public interface LanguageMapper 
{
    /**
     * 查询国家语言
     * 
     * @param id 国家语言主键
     * @return 国家语言
     */
    public Language selectLanguageById(Long id);

    /**
     * 查询国家语言列表
     * 
     * @param language 国家语言
     * @return 国家语言集合
     */
    public List<Language> selectLanguageList(Language language);

    /**
     * 新增国家语言
     * 
     * @param language 国家语言
     * @return 结果
     */
    public int insertLanguage(Language language);

    /**
     * 修改国家语言
     * 
     * @param language 国家语言
     * @return 结果
     */
    public int updateLanguage(Language language);

    /**
     * 删除国家语言
     * 
     * @param id 国家语言主键
     * @return 结果
     */
    public int deleteLanguageById(Long id);

    /**
     * 批量删除国家语言
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteLanguageByIds(Long[] ids);
}
