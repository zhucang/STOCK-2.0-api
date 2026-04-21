package com.ruoyi.system.service;

import com.ruoyi.system.domain.WebsiteLang;

import java.util.List;
import java.util.Map;

/**
 * 官网多语言包Service接口
 * 
 * @author ruoyi
 * @date 2023-12-09
 */
public interface IWebsiteLangService 
{
    /**
     * 查询官网多语言包
     * 
     * @param id 官网多语言包主键
     * @return 官网多语言包
     */
    public WebsiteLang selectWebsiteLangById(Long id);

    /**
     * 查询官网多语言包列表
     * 
     * @param websiteLang 官网多语言包
     * @return 官网多语言包集合
     */
    public List<WebsiteLang> selectWebsiteLangList(WebsiteLang websiteLang);

    /**
     * 查询多语言配置包列表
     *
     * @param lang 语言简称
     * @return 多语言配置包集合
     */
    public Map<String,String> selectWebsiteLangListByLang(String lang);

    /**
     * 新增官网多语言包
     * 
     * @param websiteLang 官网多语言包
     * @return 结果
     */
    public int insertWebsiteLang(WebsiteLang websiteLang);

    /**
     * 修改官网多语言包
     * 
     * @param websiteLang 官网多语言包
     * @return 结果
     */
    public int updateWebsiteLang(WebsiteLang websiteLang);

    /**
     * 批量替换官网多语言
     * @param from
     * @param to
     * @return
     */
    public int batchReplaceLangValue(String from, String to);

    /**
     * 批量删除官网多语言包
     * 
     * @param ids 需要删除的官网多语言包主键集合
     * @return 结果
     */
    public int deleteWebsiteLangByIds(Long[] ids);

    /**
     * 删除官网多语言包信息
     * 
     * @param id 官网多语言包主键
     * @return 结果
     */
    public int deleteWebsiteLangById(Long id);

    /**
     * 导入
     * @return 结果
     */
    public String importWebsiteLang(List<WebsiteLang> list, Boolean isUpdateSupport);
}
