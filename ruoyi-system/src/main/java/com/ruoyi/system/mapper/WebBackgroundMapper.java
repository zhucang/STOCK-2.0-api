package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.LangMgr;

import java.util.List;


public interface WebBackgroundMapper {

    /**
     * 获取所有产品名称多语言
     * @return
     */
    List<LangMgr> selectProductNameLang();

}
