package com.ruoyi.system.service;

import com.ruoyi.system.domain.LangMgr;

import java.util.List;
import java.util.Map;

/**
 * 多语言配置包Service接口
 * 
 * @author ruoyi
 * @date 2023-11-07
 */
public interface ILangMgrService 
{
    /**
     * 查询多语言配置包
     * 
     * @param id 多语言配置包主键
     * @return 多语言配置包
     */
    public LangMgr selectLangMgrById(Long id);

    /**
     * 查询多语言配置包列表
     * 
     * @param lang 语言简称
     * @return 多语言配置包集合
     */
    public Map<String,String> selectLangMgrListByLang(String lang);

    /**
     * 查询多语言配置包列表
     *
     * @param langMgr 多语言配置包
     * @return 多语言配置包集合
     */
    public List<LangMgr> selectLangMgrList(LangMgr langMgr);

    /**
     * 新增多语言配置包
     * 
     * @param langMgr 多语言配置包
     * @return 结果
     */
    public int insertLangMgr(LangMgr langMgr);

    /**
     * 修改多语言配置包
     * 
     * @param langMgr 多语言配置包
     * @return 结果
     */
    public int updateLangMgr(LangMgr langMgr);

    /**
     * 批量替换多语言
     * @param from
     * @param to
     * @return
     */
    public int batchReplaceLangValue(String from, String to);

    /**
     * 批量删除多语言配置包
     * 
     * @param ids 需要删除的多语言配置包主键集合
     * @return 结果
     */
    public int deleteLangMgrByIds(Long[] ids);

    /**
     * 删除多语言配置包信息
     * 
     * @param id 多语言配置包主键
     * @return 结果
     */
    public int deleteLangMgrById(Long id);

    /**
     * 导入
     * @return 结果
     */
    public String importLangMgr(List<LangMgr> list, Boolean isUpdateSupport);
}
