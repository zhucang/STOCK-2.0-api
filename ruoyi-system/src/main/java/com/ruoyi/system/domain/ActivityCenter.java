package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 活动中心配置对象 activity_center
 * 
 * @author ruoyi
 * @date 2025-07-02
 */
public class ActivityCenter extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 活动图片 */
    @Excel(name = "活动图片")
    private String activityImg;

    /** 活动图片多语言 */
    @Excel(name = "活动图片多语言")
    private LangMgr activityImgLang;

    /** 活动标题 */
    @Excel(name = "活动标题")
    private String activityTitle;

    /** 活动标题多语言 */
    @Excel(name = "活动标题多语言")
    private LangMgr activityTitleLang;

    /** 活动内容 */
    @Excel(name = "活动内容")
    private String activityContent;

    /** 活动内容多语言 */
    @Excel(name = "活动内容多语言")
    private LangMgr activityContentLang;

    /** 排序 */
    @Excel(name = "排序")
    private Long sort;

    /** 展示时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "展示时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date showTime;

    /** 活动图片-英文 */
    @Excel(name = "活动图片-英文")
    private String activityImgEn;

    /** 活动图片-繁体 */
    @Excel(name = "活动图片-繁体")
    private String activityImgTc;

    /** 活动图片-德国 */
    @Excel(name = "活动图片-德国")
    private String activityImgDe;

    /** 活动图片-西班牙 */
    @Excel(name = "活动图片-西班牙")
    private String activityImgEs;

    /** 活动图片-法国 */
    @Excel(name = "活动图片-法国")
    private String activityImgFr;

    /** 活动图片-印度尼西亚 */
    @Excel(name = "活动图片-印度尼西亚")
    private String activityImgIdn;

    /** 活动图片-日本 */
    @Excel(name = "活动图片-日本")
    private String activityImgJp;

    /** 活动图片-韩国 */
    @Excel(name = "活动图片-韩国")
    private String activityImgKo;

    /** 活动图片-马来西亚 */
    @Excel(name = "活动图片-马来西亚")
    private String activityImgMy;

    /** 活动图片-泰国 */
    @Excel(name = "活动图片-泰国")
    private String activityImgTh;

    /** 活动图片-越南 */
    @Excel(name = "活动图片-越南")
    private String activityImgVi;

    /** 活动图片-葡萄牙 */
    @Excel(name = "活动图片-葡萄牙")
    private String activityImgPt;

    /** 活动图片-俄语 */
    @Excel(name = "活动图片-俄语")
    private String activityImgRus;

    /** 活动图片-白俄罗斯 */
    @Excel(name = "活动图片-白俄罗斯")
    private String activityImgBlr;

    /** 活动图片-印度 */
    @Excel(name = "活动图片-印度")
    private String activityImgIda;

    /** 活动图片-沙特阿拉伯 */
    @Excel(name = "活动图片-沙特阿拉伯")
    private String activityImgSa;

    /** 活动图片-阿拉伯 */
    @Excel(name = "活动图片-阿拉伯")
    private String activityImgAr;

    /** 活动图片-意大利 */
    @Excel(name = "活动图片-意大利")
    private String activityImgIt;

    /** 活动图片-土耳其 */
    @Excel(name = "活动图片-土耳其")
    private String activityImgTr;

    /** 活动标题-英文 */
    @Excel(name = "活动标题-英文")
    private String activityTitleEn;

    /** 活动标题-繁体 */
    @Excel(name = "活动标题-繁体")
    private String activityTitleTc;

    /** 活动标题-德国 */
    @Excel(name = "活动标题-德国")
    private String activityTitleDe;

    /** 活动标题-西班牙 */
    @Excel(name = "活动标题-西班牙")
    private String activityTitleEs;

    /** 活动标题-法国 */
    @Excel(name = "活动标题-法国")
    private String activityTitleFr;

    /** 活动标题-印度尼西亚 */
    @Excel(name = "活动标题-印度尼西亚")
    private String activityTitleIdn;

    /** 活动标题-日本 */
    @Excel(name = "活动标题-日本")
    private String activityTitleJp;

    /** 活动标题-韩国 */
    @Excel(name = "活动标题-韩国")
    private String activityTitleKo;

    /** 活动标题-马来西亚 */
    @Excel(name = "活动标题-马来西亚")
    private String activityTitleMy;

    /** 活动标题-泰国 */
    @Excel(name = "活动标题-泰国")
    private String activityTitleTh;

    /** 活动标题-越南 */
    @Excel(name = "活动标题-越南")
    private String activityTitleVi;

    /** 活动标题-葡萄牙 */
    @Excel(name = "活动标题-葡萄牙")
    private String activityTitlePt;

    /** 活动标题-俄语 */
    @Excel(name = "活动标题-俄语")
    private String activityTitleRus;

    /** 活动标题-白俄罗斯 */
    @Excel(name = "活动标题-白俄罗斯")
    private String activityTitleBlr;

    /** 活动标题-印度 */
    @Excel(name = "活动标题-印度")
    private String activityTitleIda;

    /** 活动标题-沙特阿拉伯 */
    @Excel(name = "活动标题-沙特阿拉伯")
    private String activityTitleSa;

    /** 活动标题-阿拉伯 */
    @Excel(name = "活动标题-阿拉伯")
    private String activityTitleAr;

    /** 活动标题-意大利 */
    @Excel(name = "活动标题-意大利")
    private String activityTitleIt;

    /** 活动标题-土耳其 */
    @Excel(name = "活动标题-土耳其")
    private String activityTitleTr;

    /** 活动内容-英文 */
    @Excel(name = "活动内容-英文")
    private String activityContentEn;

    /** 活动内容-繁体 */
    @Excel(name = "活动内容-繁体")
    private String activityContentTc;

    /** 活动内容-德国 */
    @Excel(name = "活动内容-德国")
    private String activityContentDe;

    /** 活动内容-西班牙 */
    @Excel(name = "活动内容-西班牙")
    private String activityContentEs;

    /** 活动内容-法国 */
    @Excel(name = "活动内容-法国")
    private String activityContentFr;

    /** 活动内容-印度尼西亚 */
    @Excel(name = "活动内容-印度尼西亚")
    private String activityContentIdn;

    /** 活动内容-日本 */
    @Excel(name = "活动内容-日本")
    private String activityContentJp;

    /** 活动内容-韩国 */
    @Excel(name = "活动内容-韩国")
    private String activityContentKo;

    /** 活动内容-马来西亚 */
    @Excel(name = "活动内容-马来西亚")
    private String activityContentMy;

    /** 活动内容-泰国 */
    @Excel(name = "活动内容-泰国")
    private String activityContentTh;

    /** 活动内容-越南 */
    @Excel(name = "活动内容-越南")
    private String activityContentVi;

    /** 活动内容-葡萄牙 */
    @Excel(name = "活动内容-葡萄牙")
    private String activityContentPt;

    /** 活动内容-俄语 */
    @Excel(name = "活动内容-俄语")
    private String activityContentRus;

    /** 活动内容-白俄罗斯 */
    @Excel(name = "活动内容-白俄罗斯")
    private String activityContentBlr;

    /** 活动内容-印度 */
    @Excel(name = "活动内容-印度")
    private String activityContentIda;

    /** 活动内容-沙特阿拉伯 */
    @Excel(name = "活动内容-沙特阿拉伯")
    private String activityContentSa;

    /** 活动内容-阿拉伯 */
    @Excel(name = "活动内容-阿拉伯")
    private String activityContentAr;

    /** 活动内容-意大利 */
    @Excel(name = "活动内容-意大利")
    private String activityContentIt;

    /** 活动内容-土耳其 */
    @Excel(name = "活动内容-土耳其")
    private String activityContentTr;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setActivityImg(String activityImg) 
    {
        this.activityImg = activityImg;
    }

    public String getActivityImg() 
    {
        return activityImg;
    }

    public LangMgr getActivityImgLang() {
        if (activityImgLang == null){
            return new LangMgr();
        }
        return activityImgLang;
    }

    public void setActivityImgLang(LangMgr activityImgLang) {
        this.activityImgLang = activityImgLang;
    }

    public void setActivityTitle(String activityTitle)
    {
        this.activityTitle = activityTitle;
    }

    public String getActivityTitle() 
    {
        return activityTitle;
    }

    public LangMgr getActivityTitleLang() {
        if (activityTitleLang == null){
            return new LangMgr();
        }
        return activityTitleLang;
    }

    public void setActivityTitleLang(LangMgr activityTitleLang) {
        this.activityTitleLang = activityTitleLang;
    }

    public void setActivityContent(String activityContent)
    {
        this.activityContent = activityContent;
    }

    public String getActivityContent() 
    {
        return activityContent;
    }

    public LangMgr getActivityContentLang() {
        if (activityContentLang == null){
            return new LangMgr();
        }
        return activityContentLang;
    }

    public void setActivityContentLang(LangMgr activityContentLang) {
        this.activityContentLang = activityContentLang;
    }

    public void setSort(Long sort)
    {
        this.sort = sort;
    }

    public Long getSort() 
    {
        return sort;
    }
    public void setShowTime(Date showTime) 
    {
        this.showTime = showTime;
    }

    public Date getShowTime() 
    {
        return showTime;
    }
    public void setActivityImgEn(String activityImgEn) 
    {
        this.activityImgEn = activityImgEn;
    }

    public String getActivityImgEn() 
    {
        return activityImgEn;
    }
    public void setActivityImgTc(String activityImgTc) 
    {
        this.activityImgTc = activityImgTc;
    }

    public String getActivityImgTc() 
    {
        return activityImgTc;
    }
    public void setActivityImgDe(String activityImgDe) 
    {
        this.activityImgDe = activityImgDe;
    }

    public String getActivityImgDe() 
    {
        return activityImgDe;
    }
    public void setActivityImgEs(String activityImgEs) 
    {
        this.activityImgEs = activityImgEs;
    }

    public String getActivityImgEs() 
    {
        return activityImgEs;
    }
    public void setActivityImgFr(String activityImgFr) 
    {
        this.activityImgFr = activityImgFr;
    }

    public String getActivityImgFr() 
    {
        return activityImgFr;
    }
    public void setActivityImgIdn(String activityImgIdn) 
    {
        this.activityImgIdn = activityImgIdn;
    }

    public String getActivityImgIdn() 
    {
        return activityImgIdn;
    }
    public void setActivityImgJp(String activityImgJp) 
    {
        this.activityImgJp = activityImgJp;
    }

    public String getActivityImgJp() 
    {
        return activityImgJp;
    }
    public void setActivityImgKo(String activityImgKo) 
    {
        this.activityImgKo = activityImgKo;
    }

    public String getActivityImgKo() 
    {
        return activityImgKo;
    }
    public void setActivityImgMy(String activityImgMy) 
    {
        this.activityImgMy = activityImgMy;
    }

    public String getActivityImgMy() 
    {
        return activityImgMy;
    }
    public void setActivityImgTh(String activityImgTh) 
    {
        this.activityImgTh = activityImgTh;
    }

    public String getActivityImgTh() 
    {
        return activityImgTh;
    }
    public void setActivityImgVi(String activityImgVi) 
    {
        this.activityImgVi = activityImgVi;
    }

    public String getActivityImgVi() 
    {
        return activityImgVi;
    }
    public void setActivityImgPt(String activityImgPt) 
    {
        this.activityImgPt = activityImgPt;
    }

    public String getActivityImgPt() 
    {
        return activityImgPt;
    }
    public void setActivityImgRus(String activityImgRus) 
    {
        this.activityImgRus = activityImgRus;
    }

    public String getActivityImgRus() 
    {
        return activityImgRus;
    }
    public void setActivityImgBlr(String activityImgBlr) 
    {
        this.activityImgBlr = activityImgBlr;
    }

    public String getActivityImgBlr() 
    {
        return activityImgBlr;
    }
    public void setActivityImgIda(String activityImgIda) 
    {
        this.activityImgIda = activityImgIda;
    }

    public String getActivityImgIda() 
    {
        return activityImgIda;
    }
    public void setActivityImgSa(String activityImgSa) 
    {
        this.activityImgSa = activityImgSa;
    }

    public String getActivityImgSa() 
    {
        return activityImgSa;
    }
    public void setActivityImgAr(String activityImgAr) 
    {
        this.activityImgAr = activityImgAr;
    }

    public String getActivityImgAr() 
    {
        return activityImgAr;
    }
    public void setActivityImgIt(String activityImgIt) 
    {
        this.activityImgIt = activityImgIt;
    }

    public String getActivityImgIt() 
    {
        return activityImgIt;
    }
    public void setActivityImgTr(String activityImgTr) 
    {
        this.activityImgTr = activityImgTr;
    }

    public String getActivityImgTr() 
    {
        return activityImgTr;
    }
    public void setActivityTitleEn(String activityTitleEn) 
    {
        this.activityTitleEn = activityTitleEn;
    }

    public String getActivityTitleEn() 
    {
        return activityTitleEn;
    }
    public void setActivityTitleTc(String activityTitleTc) 
    {
        this.activityTitleTc = activityTitleTc;
    }

    public String getActivityTitleTc() 
    {
        return activityTitleTc;
    }
    public void setActivityTitleDe(String activityTitleDe) 
    {
        this.activityTitleDe = activityTitleDe;
    }

    public String getActivityTitleDe() 
    {
        return activityTitleDe;
    }
    public void setActivityTitleEs(String activityTitleEs) 
    {
        this.activityTitleEs = activityTitleEs;
    }

    public String getActivityTitleEs() 
    {
        return activityTitleEs;
    }
    public void setActivityTitleFr(String activityTitleFr) 
    {
        this.activityTitleFr = activityTitleFr;
    }

    public String getActivityTitleFr() 
    {
        return activityTitleFr;
    }
    public void setActivityTitleIdn(String activityTitleIdn) 
    {
        this.activityTitleIdn = activityTitleIdn;
    }

    public String getActivityTitleIdn() 
    {
        return activityTitleIdn;
    }
    public void setActivityTitleJp(String activityTitleJp) 
    {
        this.activityTitleJp = activityTitleJp;
    }

    public String getActivityTitleJp() 
    {
        return activityTitleJp;
    }
    public void setActivityTitleKo(String activityTitleKo) 
    {
        this.activityTitleKo = activityTitleKo;
    }

    public String getActivityTitleKo() 
    {
        return activityTitleKo;
    }
    public void setActivityTitleMy(String activityTitleMy) 
    {
        this.activityTitleMy = activityTitleMy;
    }

    public String getActivityTitleMy() 
    {
        return activityTitleMy;
    }
    public void setActivityTitleTh(String activityTitleTh) 
    {
        this.activityTitleTh = activityTitleTh;
    }

    public String getActivityTitleTh() 
    {
        return activityTitleTh;
    }
    public void setActivityTitleVi(String activityTitleVi) 
    {
        this.activityTitleVi = activityTitleVi;
    }

    public String getActivityTitleVi() 
    {
        return activityTitleVi;
    }
    public void setActivityTitlePt(String activityTitlePt) 
    {
        this.activityTitlePt = activityTitlePt;
    }

    public String getActivityTitlePt() 
    {
        return activityTitlePt;
    }
    public void setActivityTitleRus(String activityTitleRus) 
    {
        this.activityTitleRus = activityTitleRus;
    }

    public String getActivityTitleRus() 
    {
        return activityTitleRus;
    }
    public void setActivityTitleBlr(String activityTitleBlr) 
    {
        this.activityTitleBlr = activityTitleBlr;
    }

    public String getActivityTitleBlr() 
    {
        return activityTitleBlr;
    }
    public void setActivityTitleIda(String activityTitleIda) 
    {
        this.activityTitleIda = activityTitleIda;
    }

    public String getActivityTitleIda() 
    {
        return activityTitleIda;
    }
    public void setActivityTitleSa(String activityTitleSa) 
    {
        this.activityTitleSa = activityTitleSa;
    }

    public String getActivityTitleSa() 
    {
        return activityTitleSa;
    }
    public void setActivityTitleAr(String activityTitleAr) 
    {
        this.activityTitleAr = activityTitleAr;
    }

    public String getActivityTitleAr() 
    {
        return activityTitleAr;
    }
    public void setActivityTitleIt(String activityTitleIt) 
    {
        this.activityTitleIt = activityTitleIt;
    }

    public String getActivityTitleIt() 
    {
        return activityTitleIt;
    }
    public void setActivityTitleTr(String activityTitleTr) 
    {
        this.activityTitleTr = activityTitleTr;
    }

    public String getActivityTitleTr() 
    {
        return activityTitleTr;
    }
    public void setActivityContentEn(String activityContentEn) 
    {
        this.activityContentEn = activityContentEn;
    }

    public String getActivityContentEn() 
    {
        return activityContentEn;
    }
    public void setActivityContentTc(String activityContentTc) 
    {
        this.activityContentTc = activityContentTc;
    }

    public String getActivityContentTc() 
    {
        return activityContentTc;
    }
    public void setActivityContentDe(String activityContentDe) 
    {
        this.activityContentDe = activityContentDe;
    }

    public String getActivityContentDe() 
    {
        return activityContentDe;
    }
    public void setActivityContentEs(String activityContentEs) 
    {
        this.activityContentEs = activityContentEs;
    }

    public String getActivityContentEs() 
    {
        return activityContentEs;
    }
    public void setActivityContentFr(String activityContentFr) 
    {
        this.activityContentFr = activityContentFr;
    }

    public String getActivityContentFr() 
    {
        return activityContentFr;
    }
    public void setActivityContentIdn(String activityContentIdn) 
    {
        this.activityContentIdn = activityContentIdn;
    }

    public String getActivityContentIdn() 
    {
        return activityContentIdn;
    }
    public void setActivityContentJp(String activityContentJp) 
    {
        this.activityContentJp = activityContentJp;
    }

    public String getActivityContentJp() 
    {
        return activityContentJp;
    }
    public void setActivityContentKo(String activityContentKo) 
    {
        this.activityContentKo = activityContentKo;
    }

    public String getActivityContentKo() 
    {
        return activityContentKo;
    }
    public void setActivityContentMy(String activityContentMy) 
    {
        this.activityContentMy = activityContentMy;
    }

    public String getActivityContentMy() 
    {
        return activityContentMy;
    }
    public void setActivityContentTh(String activityContentTh) 
    {
        this.activityContentTh = activityContentTh;
    }

    public String getActivityContentTh() 
    {
        return activityContentTh;
    }
    public void setActivityContentVi(String activityContentVi) 
    {
        this.activityContentVi = activityContentVi;
    }

    public String getActivityContentVi() 
    {
        return activityContentVi;
    }
    public void setActivityContentPt(String activityContentPt) 
    {
        this.activityContentPt = activityContentPt;
    }

    public String getActivityContentPt() 
    {
        return activityContentPt;
    }
    public void setActivityContentRus(String activityContentRus) 
    {
        this.activityContentRus = activityContentRus;
    }

    public String getActivityContentRus() 
    {
        return activityContentRus;
    }
    public void setActivityContentBlr(String activityContentBlr) 
    {
        this.activityContentBlr = activityContentBlr;
    }

    public String getActivityContentBlr() 
    {
        return activityContentBlr;
    }
    public void setActivityContentIda(String activityContentIda) 
    {
        this.activityContentIda = activityContentIda;
    }

    public String getActivityContentIda() 
    {
        return activityContentIda;
    }
    public void setActivityContentSa(String activityContentSa) 
    {
        this.activityContentSa = activityContentSa;
    }

    public String getActivityContentSa() 
    {
        return activityContentSa;
    }
    public void setActivityContentAr(String activityContentAr) 
    {
        this.activityContentAr = activityContentAr;
    }

    public String getActivityContentAr() 
    {
        return activityContentAr;
    }
    public void setActivityContentIt(String activityContentIt) 
    {
        this.activityContentIt = activityContentIt;
    }

    public String getActivityContentIt() 
    {
        return activityContentIt;
    }
    public void setActivityContentTr(String activityContentTr) 
    {
        this.activityContentTr = activityContentTr;
    }

    public String getActivityContentTr() 
    {
        return activityContentTr;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("activityImg", getActivityImg())
            .append("activityTitle", getActivityTitle())
            .append("activityContent", getActivityContent())
            .append("sort", getSort())
            .append("showTime", getShowTime())
            .append("createTime", getCreateTime())
            .append("activityImgEn", getActivityImgEn())
            .append("activityImgTc", getActivityImgTc())
            .append("activityImgDe", getActivityImgDe())
            .append("activityImgEs", getActivityImgEs())
            .append("activityImgFr", getActivityImgFr())
            .append("activityImgIdn", getActivityImgIdn())
            .append("activityImgJp", getActivityImgJp())
            .append("activityImgKo", getActivityImgKo())
            .append("activityImgMy", getActivityImgMy())
            .append("activityImgTh", getActivityImgTh())
            .append("activityImgVi", getActivityImgVi())
            .append("activityImgPt", getActivityImgPt())
            .append("activityImgRus", getActivityImgRus())
            .append("activityImgBlr", getActivityImgBlr())
            .append("activityImgIda", getActivityImgIda())
            .append("activityImgSa", getActivityImgSa())
            .append("activityImgAr", getActivityImgAr())
            .append("activityImgIt", getActivityImgIt())
            .append("activityImgTr", getActivityImgTr())
            .append("activityTitleEn", getActivityTitleEn())
            .append("activityTitleTc", getActivityTitleTc())
            .append("activityTitleDe", getActivityTitleDe())
            .append("activityTitleEs", getActivityTitleEs())
            .append("activityTitleFr", getActivityTitleFr())
            .append("activityTitleIdn", getActivityTitleIdn())
            .append("activityTitleJp", getActivityTitleJp())
            .append("activityTitleKo", getActivityTitleKo())
            .append("activityTitleMy", getActivityTitleMy())
            .append("activityTitleTh", getActivityTitleTh())
            .append("activityTitleVi", getActivityTitleVi())
            .append("activityTitlePt", getActivityTitlePt())
            .append("activityTitleRus", getActivityTitleRus())
            .append("activityTitleBlr", getActivityTitleBlr())
            .append("activityTitleIda", getActivityTitleIda())
            .append("activityTitleSa", getActivityTitleSa())
            .append("activityTitleAr", getActivityTitleAr())
            .append("activityTitleIt", getActivityTitleIt())
            .append("activityTitleTr", getActivityTitleTr())
            .append("activityContentEn", getActivityContentEn())
            .append("activityContentTc", getActivityContentTc())
            .append("activityContentDe", getActivityContentDe())
            .append("activityContentEs", getActivityContentEs())
            .append("activityContentFr", getActivityContentFr())
            .append("activityContentIdn", getActivityContentIdn())
            .append("activityContentJp", getActivityContentJp())
            .append("activityContentKo", getActivityContentKo())
            .append("activityContentMy", getActivityContentMy())
            .append("activityContentTh", getActivityContentTh())
            .append("activityContentVi", getActivityContentVi())
            .append("activityContentPt", getActivityContentPt())
            .append("activityContentRus", getActivityContentRus())
            .append("activityContentBlr", getActivityContentBlr())
            .append("activityContentIda", getActivityContentIda())
            .append("activityContentSa", getActivityContentSa())
            .append("activityContentAr", getActivityContentAr())
            .append("activityContentIt", getActivityContentIt())
            .append("activityContentTr", getActivityContentTr())
            .toString();
    }
}
