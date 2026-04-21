package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 喇叭信息对象 trumpet_info
 * 
 * @author ruoyi
 * @date 2023-11-04
 */
public class TrumpetInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 展示时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "展示时间")
    private Date showTime;

    /** 喇叭标题 */
    @Excel(name = "喇叭标题")
    private String trumpetTitle;

    /** 喇叭 */
    @Excel(name = "喇叭")
    private String trumpetContent;

    /** 喇叭标题-英文 */
    @Excel(name = "喇叭标题-英文")
    private String trumpetTitleEn;

    /** 喇叭标题-繁体 */
    @Excel(name = "喇叭标题-繁体")
    private String trumpetTitleTc;

    /** 喇叭标题-德国 */
    @Excel(name = "喇叭标题-德国")
    private String trumpetTitleDe;

    /** 喇叭标题-西班牙 */
    @Excel(name = "喇叭标题-西班牙")
    private String trumpetTitleEs;

    /** 喇叭标题-法国 */
    @Excel(name = "喇叭标题-法国")
    private String trumpetTitleFr;

    /** 喇叭标题-印度尼西亚 */
    @Excel(name = "喇叭标题-印度尼西亚")
    private String trumpetTitleIdn;

    /** 喇叭标题-日本 */
    @Excel(name = "喇叭标题-日本")
    private String trumpetTitleJp;

    /** 喇叭标题-韩国 */
    @Excel(name = "喇叭标题-韩国")
    private String trumpetTitleKo;

    /** 喇叭标题-马来西亚 */
    @Excel(name = "喇叭标题-马来西亚")
    private String trumpetTitleMy;

    /** 喇叭标题-泰国 */
    @Excel(name = "喇叭标题-泰国")
    private String trumpetTitleTh;

    /** 喇叭标题-越南 */
    @Excel(name = "喇叭标题-越南")
    private String trumpetTitleVi;

    /** 喇叭标题-葡萄牙 */
    @Excel(name = "喇叭标题-葡萄牙")
    private String trumpetTitlePt;

    /** 喇叭标题-俄语 */
    @Excel(name = "喇叭标题-俄语")
    private String trumpetTitleRus;

    /** 喇叭标题-白俄罗斯 */
    @Excel(name = "喇叭标题-白俄罗斯")
    private String trumpetTitleBlr;

    /** 喇叭标题-印度 */
    @Excel(name = "喇叭标题-印度")
    private String trumpetTitleIda;

    /** 喇叭标题-沙特阿拉伯 */
    @Excel(name = "喇叭标题-沙特阿拉伯")
    private String trumpetTitleSa;

    /** 喇叭标题-阿拉伯 */
    @Excel(name = "喇叭标题-阿拉伯")
    private String trumpetTitleAr;

    /** 喇叭标题-意大利 */
    @Excel(name = "喇叭标题-意大利")
    private String trumpetTitleIt;

    /** 喇叭标题-土耳其 */
    @Excel(name = "喇叭标题-土耳其")
    private String trumpetTitleTr;

    /** 喇叭-英文 */
    @Excel(name = "喇叭-英文")
    private String trumpetContentEn;

    /** 喇叭-繁体 */
    @Excel(name = "喇叭-繁体")
    private String trumpetContentTc;

    /** 喇叭-德国 */
    @Excel(name = "喇叭-德国")
    private String trumpetContentDe;

    /** 喇叭-西班牙 */
    @Excel(name = "喇叭-西班牙")
    private String trumpetContentEs;

    /** 喇叭-法国 */
    @Excel(name = "喇叭-法国")
    private String trumpetContentFr;

    /** 喇叭-印度尼西亚 */
    @Excel(name = "喇叭-印度尼西亚")
    private String trumpetContentIdn;

    /** 喇叭-日本 */
    @Excel(name = "喇叭-日本")
    private String trumpetContentJp;

    /** 喇叭-韩国 */
    @Excel(name = "喇叭-韩国")
    private String trumpetContentKo;

    /** 喇叭-马来西亚 */
    @Excel(name = "喇叭-马来西亚")
    private String trumpetContentMy;

    /** 喇叭-泰国 */
    @Excel(name = "喇叭-泰国")
    private String trumpetContentTh;

    /** 喇叭-越南 */
    @Excel(name = "喇叭-越南")
    private String trumpetContentVi;

    /** 喇叭-葡萄牙 */
    @Excel(name = "喇叭-葡萄牙")
    private String trumpetContentPt;

    /** 喇叭-俄语 */
    @Excel(name = "喇叭-俄语")
    private String trumpetContentRus;

    /** 喇叭-白俄罗斯 */
    @Excel(name = "喇叭-白俄罗斯")
    private String trumpetContentBlr;

    /** 喇叭-印度 */
    @Excel(name = "喇叭-印度")
    private String trumpetContentIda;

    /** 喇叭-沙特阿拉伯 */
    @Excel(name = "喇叭-沙特阿拉伯")
    private String trumpetContentSa;

    /** 喇叭-阿拉伯 */
    @Excel(name = "喇叭-阿拉伯")
    private String trumpetContentAr;

    /** 喇叭-意大利 */
    @Excel(name = "喇叭-意大利")
    private String trumpetContentIt;

    /** 喇叭-土耳其 */
    @Excel(name = "喇叭-土耳其")
    private String trumpetContentTr;

    public Date getShowTime() {
        return showTime;
    }

    public void setShowTime(Date showTime) {
        this.showTime = showTime;
    }

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public String getTrumpetTitle() {
        return trumpetTitle;
    }

    public void setTrumpetTitle(String trumpetTitle) {
        this.trumpetTitle = trumpetTitle;
    }

    public String getTrumpetTitleEn() {
        return trumpetTitleEn;
    }

    public void setTrumpetTitleEn(String trumpetTitleEn) {
        this.trumpetTitleEn = trumpetTitleEn;
    }

    public String getTrumpetTitleTc() {
        return trumpetTitleTc;
    }

    public void setTrumpetTitleTc(String trumpetTitleTc) {
        this.trumpetTitleTc = trumpetTitleTc;
    }

    public String getTrumpetTitleDe() {
        return trumpetTitleDe;
    }

    public void setTrumpetTitleDe(String trumpetTitleDe) {
        this.trumpetTitleDe = trumpetTitleDe;
    }

    public String getTrumpetTitleEs() {
        return trumpetTitleEs;
    }

    public void setTrumpetTitleEs(String trumpetTitleEs) {
        this.trumpetTitleEs = trumpetTitleEs;
    }

    public String getTrumpetTitleFr() {
        return trumpetTitleFr;
    }

    public void setTrumpetTitleFr(String trumpetTitleFr) {
        this.trumpetTitleFr = trumpetTitleFr;
    }

    public String getTrumpetTitleIdn() {
        return trumpetTitleIdn;
    }

    public void setTrumpetTitleIdn(String trumpetTitleIdn) {
        this.trumpetTitleIdn = trumpetTitleIdn;
    }

    public String getTrumpetTitleJp() {
        return trumpetTitleJp;
    }

    public void setTrumpetTitleJp(String trumpetTitleJp) {
        this.trumpetTitleJp = trumpetTitleJp;
    }

    public String getTrumpetTitleKo() {
        return trumpetTitleKo;
    }

    public void setTrumpetTitleKo(String trumpetTitleKo) {
        this.trumpetTitleKo = trumpetTitleKo;
    }

    public String getTrumpetTitleMy() {
        return trumpetTitleMy;
    }

    public void setTrumpetTitleMy(String trumpetTitleMy) {
        this.trumpetTitleMy = trumpetTitleMy;
    }

    public String getTrumpetTitleTh() {
        return trumpetTitleTh;
    }

    public void setTrumpetTitleTh(String trumpetTitleTh) {
        this.trumpetTitleTh = trumpetTitleTh;
    }

    public String getTrumpetTitleVi() {
        return trumpetTitleVi;
    }

    public void setTrumpetTitleVi(String trumpetTitleVi) {
        this.trumpetTitleVi = trumpetTitleVi;
    }

    public String getTrumpetTitlePt() {
        return trumpetTitlePt;
    }

    public void setTrumpetTitlePt(String trumpetTitlePt) {
        this.trumpetTitlePt = trumpetTitlePt;
    }

    public String getTrumpetTitleRus() {
        return trumpetTitleRus;
    }

    public void setTrumpetTitleRus(String trumpetTitleRus) {
        this.trumpetTitleRus = trumpetTitleRus;
    }

    public String getTrumpetTitleBlr() {
        return trumpetTitleBlr;
    }

    public void setTrumpetTitleBlr(String trumpetTitleBlr) {
        this.trumpetTitleBlr = trumpetTitleBlr;
    }

    public String getTrumpetTitleIda() {
        return trumpetTitleIda;
    }

    public void setTrumpetTitleIda(String trumpetTitleIda) {
        this.trumpetTitleIda = trumpetTitleIda;
    }

    public String getTrumpetTitleSa() {
        return trumpetTitleSa;
    }

    public void setTrumpetTitleSa(String trumpetTitleSa) {
        this.trumpetTitleSa = trumpetTitleSa;
    }

    public String getTrumpetTitleAr() {
        return trumpetTitleAr;
    }

    public void setTrumpetTitleAr(String trumpetTitleAr) {
        this.trumpetTitleAr = trumpetTitleAr;
    }

    public String getTrumpetTitleIt() {
        return trumpetTitleIt;
    }

    public void setTrumpetTitleIt(String trumpetTitleIt) {
        this.trumpetTitleIt = trumpetTitleIt;
    }

    public String getTrumpetTitleTr() {
        return trumpetTitleTr;
    }

    public void setTrumpetTitleTr(String trumpetTitleTr) {
        this.trumpetTitleTr = trumpetTitleTr;
    }

    public void setTrumpetContent(String trumpetContent)
    {
        this.trumpetContent = trumpetContent;
    }

    public String getTrumpetContent() 
    {
        return trumpetContent;
    }
    public void setTrumpetContentEn(String trumpetContentEn) 
    {
        this.trumpetContentEn = trumpetContentEn;
    }

    public String getTrumpetContentEn() 
    {
        return trumpetContentEn;
    }
    public void setTrumpetContentTc(String trumpetContentTc) 
    {
        this.trumpetContentTc = trumpetContentTc;
    }

    public String getTrumpetContentTc() 
    {
        return trumpetContentTc;
    }
    public void setTrumpetContentDe(String trumpetContentDe) 
    {
        this.trumpetContentDe = trumpetContentDe;
    }

    public String getTrumpetContentDe() 
    {
        return trumpetContentDe;
    }
    public void setTrumpetContentEs(String trumpetContentEs) 
    {
        this.trumpetContentEs = trumpetContentEs;
    }

    public String getTrumpetContentEs() 
    {
        return trumpetContentEs;
    }
    public void setTrumpetContentFr(String trumpetContentFr) 
    {
        this.trumpetContentFr = trumpetContentFr;
    }

    public String getTrumpetContentFr() 
    {
        return trumpetContentFr;
    }
    public void setTrumpetContentIdn(String trumpetContentIdn) 
    {
        this.trumpetContentIdn = trumpetContentIdn;
    }

    public String getTrumpetContentIdn() 
    {
        return trumpetContentIdn;
    }
    public void setTrumpetContentJp(String trumpetContentJp) 
    {
        this.trumpetContentJp = trumpetContentJp;
    }

    public String getTrumpetContentJp() 
    {
        return trumpetContentJp;
    }
    public void setTrumpetContentKo(String trumpetContentKo) 
    {
        this.trumpetContentKo = trumpetContentKo;
    }

    public String getTrumpetContentKo() 
    {
        return trumpetContentKo;
    }
    public void setTrumpetContentMy(String trumpetContentMy) 
    {
        this.trumpetContentMy = trumpetContentMy;
    }

    public String getTrumpetContentMy() 
    {
        return trumpetContentMy;
    }
    public void setTrumpetContentTh(String trumpetContentTh) 
    {
        this.trumpetContentTh = trumpetContentTh;
    }

    public String getTrumpetContentTh() 
    {
        return trumpetContentTh;
    }
    public void setTrumpetContentVi(String trumpetContentVi) 
    {
        this.trumpetContentVi = trumpetContentVi;
    }

    public String getTrumpetContentVi() 
    {
        return trumpetContentVi;
    }
    public void setTrumpetContentPt(String trumpetContentPt) 
    {
        this.trumpetContentPt = trumpetContentPt;
    }

    public String getTrumpetContentPt() 
    {
        return trumpetContentPt;
    }
    public void setTrumpetContentRus(String trumpetContentRus) 
    {
        this.trumpetContentRus = trumpetContentRus;
    }

    public String getTrumpetContentRus() 
    {
        return trumpetContentRus;
    }
    public void setTrumpetContentBlr(String trumpetContentBlr) 
    {
        this.trumpetContentBlr = trumpetContentBlr;
    }

    public String getTrumpetContentBlr() 
    {
        return trumpetContentBlr;
    }
    public void setTrumpetContentIda(String trumpetContentIda) 
    {
        this.trumpetContentIda = trumpetContentIda;
    }

    public String getTrumpetContentIda() 
    {
        return trumpetContentIda;
    }
    public void setTrumpetContentSa(String trumpetContentSa) 
    {
        this.trumpetContentSa = trumpetContentSa;
    }

    public String getTrumpetContentSa() 
    {
        return trumpetContentSa;
    }
    public void setTrumpetContentAr(String trumpetContentAr) 
    {
        this.trumpetContentAr = trumpetContentAr;
    }

    public String getTrumpetContentAr() 
    {
        return trumpetContentAr;
    }
    public void setTrumpetContentIt(String trumpetContentIt) 
    {
        this.trumpetContentIt = trumpetContentIt;
    }

    public String getTrumpetContentIt() 
    {
        return trumpetContentIt;
    }

    public String getTrumpetContentTr() {
        return trumpetContentTr;
    }

    public void setTrumpetContentTr(String trumpetContentTr) {
        this.trumpetContentTr = trumpetContentTr;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("trumpetContent", getTrumpetContent())
            .append("trumpetContentEn", getTrumpetContentEn())
            .append("trumpetContentTc", getTrumpetContentTc())
            .append("trumpetContentDe", getTrumpetContentDe())
            .append("trumpetContentEs", getTrumpetContentEs())
            .append("trumpetContentFr", getTrumpetContentFr())
            .append("trumpetContentIdn", getTrumpetContentIdn())
            .append("trumpetContentJp", getTrumpetContentJp())
            .append("trumpetContentKo", getTrumpetContentKo())
            .append("trumpetContentMy", getTrumpetContentMy())
            .append("trumpetContentTh", getTrumpetContentTh())
            .append("trumpetContentVi", getTrumpetContentVi())
            .append("trumpetContentPt", getTrumpetContentPt())
            .append("trumpetContentRus", getTrumpetContentRus())
            .append("trumpetContentBlr", getTrumpetContentBlr())
            .append("trumpetContentIda", getTrumpetContentIda())
            .append("trumpetContentSa", getTrumpetContentSa())
            .append("trumpetContentAr", getTrumpetContentAr())
            .append("trumpetContentIt", getTrumpetContentIt())
            .toString();
    }
}
