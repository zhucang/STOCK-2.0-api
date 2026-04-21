package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 多语言配置包对象 lang_mgr
 * 
 * @author ruoyi
 * @date 2023-10-28
 */
public class LangMgr extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** key */
    @Excel(name = "key")
    private String langKey;

    /** 简体中文 */
    @Excel(name = "简体中文")
    private String zh;

    /** 英文 */
    @Excel(name = "英文")
    private String en;

    /** 繁体 */
    @Excel(name = "繁体")
    private String tc;

    /** 德国 */
    @Excel(name = "德国")
    private String de;

    /** 西班牙 */
    @Excel(name = "西班牙")
    private String es;

    /** 法国 */
    @Excel(name = "法国")
    private String fr;

    /** 印度尼西亚 */
    @Excel(name = "印度尼西亚")
    private String idn;

    /** 日本 */
    @Excel(name = "日本")
    private String jp;

    /** 韩国 */
    @Excel(name = "韩国")
    private String ko;

    /** 马来西亚 */
    @Excel(name = "马来西亚")
    private String my;

    /** 泰国 */
    @Excel(name = "泰国")
    private String th;

    /** 越南 */
    @Excel(name = "越南")
    private String vi;

    /** 葡萄牙 */
    @Excel(name = "葡萄牙")
    private String pt;

    /** 俄语 */
    @Excel(name = "俄语")
    private String rus;

    /** 白俄罗斯 */
    @Excel(name = "白俄罗斯")
    private String blr;

    /** 印度 */
    @Excel(name = "印度")
    private String ida;

    /** 沙特阿拉伯 */
    @Excel(name = "沙特阿拉伯")
    private String sa;

    /** 阿拉伯 */
    @Excel(name = "阿拉伯")
    private String ar;

    /** 意大利 */
    @Excel(name = "意大利")
    private String it;

    /** 土耳其 */
    @Excel(name = "土耳其")
    private String tr;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public String getZh() {
        return zh;
    }

    public void setZh(String zh) {
        this.zh = zh;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getTc() {
        return tc;
    }

    public void setTc(String tc) {
        this.tc = tc;
    }

    public String getDe() {
        return de;
    }

    public void setDe(String de) {
        this.de = de;
    }

    public String getEs() {
        return es;
    }

    public void setEs(String es) {
        this.es = es;
    }

    public String getFr() {
        return fr;
    }

    public void setFr(String fr) {
        this.fr = fr;
    }

    public String getIdn() {
        return idn;
    }

    public void setIdn(String idn) {
        this.idn = idn;
    }

    public String getJp() {
        return jp;
    }

    public void setJp(String jp) {
        this.jp = jp;
    }

    public String getKo() {
        return ko;
    }

    public void setKo(String ko) {
        this.ko = ko;
    }

    public String getMy() {
        return my;
    }

    public void setMy(String my) {
        this.my = my;
    }

    public String getTh() {
        return th;
    }

    public void setTh(String th) {
        this.th = th;
    }

    public String getVi() {
        return vi;
    }

    public void setVi(String vi) {
        this.vi = vi;
    }

    public String getPt() {
        return pt;
    }

    public void setPt(String pt) {
        this.pt = pt;
    }

    public String getRus() {
        return rus;
    }

    public void setRus(String rus) {
        this.rus = rus;
    }

    public String getBlr() {
        return blr;
    }

    public void setBlr(String blr) {
        this.blr = blr;
    }

    public String getIda() {
        return ida;
    }

    public void setIda(String ida) {
        this.ida = ida;
    }

    public String getSa() {
        return sa;
    }

    public void setSa(String sa) {
        this.sa = sa;
    }

    public String getAr() {
        return ar;
    }

    public void setAr(String ar) {
        this.ar = ar;
    }

    public String getIt() {
        return it;
    }

    public void setIt(String it) {
        this.it = it;
    }

    public String getTr() {
        return tr;
    }

    public void setTr(String tr) {
        this.tr = tr;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
