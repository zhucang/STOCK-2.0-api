package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.constant.CacheableKey;
import com.ruoyi.system.domain.WebsiteLang;
import com.ruoyi.system.mapper.WebsiteLangMapper;
import com.ruoyi.system.service.IWebsiteLangService;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 官网多语言包Service业务层处理
 * 
 * @author ruoyi
 * @date 2023-12-09
 */
@Service
public class WebsiteLangServiceImpl implements IWebsiteLangService 
{
    @Resource
    private WebsiteLangMapper websiteLangMapper;

    /**
     * 查询官网多语言包
     * 
     * @param id 官网多语言包主键
     * @return 官网多语言包
     */
    @Override
//    @Cacheable(value = CacheableKey.WEBSITE_LANG + CacheableKey.ENTITY,key = "#id")
    public WebsiteLang selectWebsiteLangById(Long id)
    {
        return websiteLangMapper.selectWebsiteLangById(id);
    }

    /**
     * 查询官网多语言包列表
     * 
     * @param websiteLang 官网多语言包
     * @return 官网多语言包
     */
    @Override
    @Cacheable(value = CacheableKey.WEBSITE_LANG + CacheableKey.LIST,key = "#websiteLang.cacheableKey()")
    public List<WebsiteLang> selectWebsiteLangList(WebsiteLang websiteLang)
    {
        return websiteLangMapper.selectWebsiteLangList(websiteLang);
    }

    /**
     * 查询官网多语言包列表
     *
     * @param lang 语言简称
     * @return 多语言配置包集合
     */
    @Override
    @Cacheable(value = CacheableKey.WEBSITE_LANG + CacheableKey.LIST,key = "#lang")
    public Map<String,String> selectWebsiteLangListByLang(String lang){
        List<WebsiteLang> websiteLangs = websiteLangMapper.selectWebsiteLangList(new WebsiteLang());
        Map<String, String> map = websiteLangs.stream().collect(Collectors.toMap(a -> a.getLangKey(), a -> {
            try {
                return PropertyUtils.describe(a).get(lang) != null ? String.valueOf(PropertyUtils.describe(a).get(lang)) : "";
            } catch (Exception e) {
                return "";
            }
        }));
        return map;
    }

    /**
     * 新增官网多语言包
     * 
     * @param websiteLang 官网多语言包
     * @return 结果
     */
    @Override
    @CacheEvict(value = CacheableKey.WEBSITE_LANG + CacheableKey.LIST,allEntries = true)
    public int insertWebsiteLang(WebsiteLang websiteLang)
    {
        WebsiteLang websiteLangVo = websiteLangMapper.selectWebsiteLangByLangKey(websiteLang.getLangKey());
        if (websiteLangVo != null){
            throw new RuntimeException("多语言key已存在");
        }
        int count = websiteLangMapper.insertWebsiteLang(websiteLang);
        if (count <= 0){
            throw new RuntimeException("系统繁忙");
        }
        return 1;
    }

    /**
     * 修改官网多语言包
     * 
     * @param websiteLang 官网多语言包
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.WEBSITE_LANG + CacheableKey.ENTITY,key = "#websiteLang.id"),
            @CacheEvict(value = CacheableKey.WEBSITE_LANG + CacheableKey.LIST,allEntries = true)})
    public int updateWebsiteLang(WebsiteLang websiteLang)
    {
        WebsiteLang websiteLangVo = websiteLangMapper.selectWebsiteLangByLangKey(websiteLang.getLangKey());
        if (websiteLangVo != null && !websiteLangVo.getId().equals(websiteLang.getId())){
            throw new RuntimeException("多语言key已存在");
        }
        int count = websiteLangMapper.updateWebsiteLang(websiteLang);
        if (count <= 0){
            throw new RuntimeException("系统繁忙");
        }
        return 1;
    }

    /**
     * 批量替换官网多语言
     * @param from
     * @param to
     * @return
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.WEBSITE_LANG + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.WEBSITE_LANG + CacheableKey.LIST,allEntries = true)})
    public int batchReplaceLangValue(String from, String to){
        websiteLangMapper.batchReplaceLangValue(from,to);
        return 1;
    }

    /**
     * 批量删除官网多语言包
     * 
     * @param ids 需要删除的官网多语言包主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.WEBSITE_LANG + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.WEBSITE_LANG + CacheableKey.LIST,allEntries = true)})
    public int deleteWebsiteLangByIds(Long[] ids)
    {
        return websiteLangMapper.deleteWebsiteLangByIds(ids);
    }

    /**
     * 删除官网多语言包信息
     * 
     * @param id 官网多语言包主键
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.WEBSITE_LANG + CacheableKey.ENTITY,key = "#id"),
            @CacheEvict(value = CacheableKey.WEBSITE_LANG + CacheableKey.LIST,allEntries = true)})
    public int deleteWebsiteLangById(Long id)
    {
        return websiteLangMapper.deleteWebsiteLangById(id);
    }

    /**
     * 导入
     * @return 结果
     */
    @Override
    @Caching(evict = {
//            @CacheEvict(value = CacheableKey.WEBSITE_LANG + CacheableKey.ENTITY,allEntries = true),
            @CacheEvict(value = CacheableKey.WEBSITE_LANG + CacheableKey.LIST,allEntries = true)})
    public String importWebsiteLang(List<WebsiteLang> list, Boolean isUpdateSupport){
        isUpdateSupport = false;

        //目前所有的多语言
        List<WebsiteLang> websiteLangs = websiteLangMapper.selectWebsiteLangList(new WebsiteLang());
        Map<String, WebsiteLang> map = websiteLangs.stream().collect(Collectors.toMap(WebsiteLang::getLangKey, a -> a));
        for (int i = 0; i < list.size(); i++) {
            //新数据
            WebsiteLang websiteLang = list.get(i);
            //key
            String langKey = websiteLang.getLangKey();
            //旧数据
            WebsiteLang websiteLangOld = map.get(langKey);
            if (websiteLangOld == null){
                int count = websiteLangMapper.insertWebsiteLang(websiteLang);
                if (count <= 0){
                    throw new RuntimeException("系统繁忙");
                }
            }else {
                websiteLangOld.setId(null);
                if (!JSONObject.toJSONString(websiteLangOld).equals(JSONObject.toJSONString(websiteLang))){
                    int count = websiteLangMapper.updateWebsiteLangByLangKey(websiteLang);
                    if (count <= 0){
                        throw new RuntimeException("系统繁忙");
                    }
                }
            }
        }
        return "导入成功";
    }
}
