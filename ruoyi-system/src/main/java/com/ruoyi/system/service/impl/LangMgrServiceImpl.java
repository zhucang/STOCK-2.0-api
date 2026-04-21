package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.constant.CacheableKey;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.LangMgr;
import com.ruoyi.system.mapper.LangMgrMapper;
import com.ruoyi.system.service.ILangMgrService;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 多语言配置包Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-11-07
 */
@Service
public class LangMgrServiceImpl implements ILangMgrService 
{
    @Resource
    private LangMgrMapper langMgrMapper;

    /**
     * 查询多语言配置包
     * 
     * @param id 多语言配置包主键
     * @return 多语言配置包
     */
    @Override
//    @Cacheable(value = CacheableKey.LANG_MGR + CacheableKey.ENTITY,key = "#id")
    public LangMgr selectLangMgrById(Long id)
    {
        return langMgrMapper.selectLangMgrById(id);
    }

    /**
     * 查询多语言配置包列表
     *
     * @param lang 语言简称
     * @return 多语言配置包集合
     */
    @Override
    @Cacheable(value = CacheableKey.LANG_MGR + CacheableKey.LIST,key = "#lang")
    public Map<String,String> selectLangMgrListByLang(String lang){
        List<LangMgr> langMgrs = langMgrMapper.selectLangMgrList(new LangMgr());
        Map<String, String> map = langMgrs.stream().collect(Collectors.toMap(a -> a.getLangKey(), a -> {
            try {
                return PropertyUtils.describe(a).get(lang) != null ? String.valueOf(PropertyUtils.describe(a).get(lang)) : "";
            } catch (Exception e) {
                return "";
            }
        }));
        return map;
    }

    /**
     * 查询多语言配置包列表
     * 
     * @param langMgr 多语言配置包
     * @return 多语言配置包
     */
    @Override
    @Cacheable(value = CacheableKey.LANG_MGR + CacheableKey.LIST,key = "#langMgr.cacheableKey()")
    public List<LangMgr> selectLangMgrList(LangMgr langMgr)
    {
        return langMgrMapper.selectLangMgrList(langMgr);
    }

    /**
     * 新增多语言配置包
     * 
     * @param langMgr 多语言配置包
     * @return 结果
     */
    @Override
    @CacheEvict(value = CacheableKey.LANG_MGR + CacheableKey.LIST,allEntries = true)
    public int insertLangMgr(LangMgr langMgr)
    {
        LangMgr langMgrVo = langMgrMapper.selectLangMgrByLangKey(langMgr.getLangKey());
        if (langMgrVo != null){
            throw new ServiceException("多语言key已存在");
        }
        int count = langMgrMapper.insertLangMgr(langMgr);
        if (count <= 0){
            throw new ServiceException("系统繁忙");
        }
        return 1;
    }

    /**
     * 修改多语言配置包
     * 
     * @param langMgr 多语言配置包
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.LANG_MGR + CacheableKey.ENTITY,key = "#langMgr.id"),
            @CacheEvict(value = CacheableKey.LANG_MGR + CacheableKey.LIST,allEntries = true)})
    public int updateLangMgr(LangMgr langMgr)
    {
        LangMgr langMgrVo = langMgrMapper.selectLangMgrByLangKey(langMgr.getLangKey());
        if (langMgrVo != null && !langMgrVo.getId().equals(langMgr.getId())){
            throw new ServiceException("多语言key已存在");
        }
        int count = langMgrMapper.updateLangMgr(langMgr);
        if (count <= 0){
            throw new ServiceException("系统繁忙");
        }
        return 1;
    }

    /**
     * 批量替换多语言
     * @param from
     * @param to
     * @return
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.LANG_MGR + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.LANG_MGR + CacheableKey.LIST,allEntries = true)})
    public int batchReplaceLangValue(String from, String to){
        langMgrMapper.batchReplaceLangValue(from,to);
        return 1;
    }

    /**
     * 批量删除多语言配置包
     * 
     * @param ids 需要删除的多语言配置包主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.LANG_MGR + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.LANG_MGR + CacheableKey.LIST,allEntries = true)})
    public int deleteLangMgrByIds(Long[] ids)
    {
        return langMgrMapper.deleteLangMgrByIds(ids);
    }

    /**
     * 删除多语言配置包信息
     * 
     * @param id 多语言配置包主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.LANG_MGR + CacheableKey.ENTITY,key = "#id"),
            @CacheEvict(value = CacheableKey.LANG_MGR + CacheableKey.LIST,allEntries = true)})
    public int deleteLangMgrById(Long id)
    {
        return langMgrMapper.deleteLangMgrById(id);
    }

    /**
     * 导入
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.LANG_MGR + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.LANG_MGR + CacheableKey.LIST,allEntries = true)})
    public String importLangMgr(List<LangMgr> list, Boolean isUpdateSupport){
        isUpdateSupport = false;

        //目前所有的多语言
        List<LangMgr> langMgrs = langMgrMapper.selectLangMgrList(new LangMgr());
        Map<String, LangMgr> map = langMgrs.stream().collect(Collectors.toMap(LangMgr::getLangKey, a -> a));
        for (int i = 0; i < list.size(); i++) {
            //新数据
            LangMgr langMgr = list.get(i);
            //key
            String langKey = list.get(i).getLangKey();
            //旧数据
            LangMgr langMgrOld = map.get(langKey);
            if (langMgrOld == null){
                int count = langMgrMapper.insertLangMgr(langMgr);
                if (count <= 0){
                    throw new ServiceException("系统繁忙");
                }
            }else {
                langMgrOld.setId(null);
                if (!JSONObject.toJSONString(langMgrOld).equals(JSONObject.toJSONString(langMgr))){
                    int count = langMgrMapper.updateLangMgrByLangKey(langMgr);
                    if (count <= 0){
                        throw new ServiceException("系统繁忙");
                    }
                }
            }
        }
        return "导入成功";
    }
}
