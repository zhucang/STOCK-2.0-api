package com.ruoyi.system.service;


import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.system.domain.LangMgr;

import java.util.List;

public interface IWebBackgroundService {

    /**
     * 获取后台提醒
     */
    public AjaxResult getReminder(BaseEntity baseEntity);

    /**
     * 获取所有产品名称多语言
     * @return
     */
    List<LangMgr> selectProductNameLang();

    /**
     * 导入所有产品名称多语言
     * @param list
     * @param isUpdateSupport
     * @return
     */
    String importProductNameLang(List<LangMgr> list, Boolean isUpdateSupport);

    /**
     * 后台首页报表
     */
    public AjaxResult indexReport();
}
