package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 弹窗信息对象 pop_up
 * 
 * @author ruoyi
 * @date 2024-03-21
 */
public class PopUp extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 弹窗标题 */
    @Excel(name = "弹窗标题")
    private String popUpTitle;

    /** 弹窗内容 */
    @Excel(name = "弹窗内容")
    private String popUpContent;

    /** 展示时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "展示时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date showTime;

    /** 弹窗标题-英文 */
    @Excel(name = "弹窗标题-英文")
    private String popUpTitleEn;

    /** 弹窗标题-繁体 */
    @Excel(name = "弹窗标题-繁体")
    private String popUpTitleTc;

    /** 弹窗标题-德国 */
    @Excel(name = "弹窗标题-德国")
    private String popUpTitleDe;

    /** 弹窗标题-西班牙 */
    @Excel(name = "弹窗标题-西班牙")
    private String popUpTitleEs;

    /** 弹窗标题-法国 */
    @Excel(name = "弹窗标题-法国")
    private String popUpTitleFr;

    /** 弹窗标题-印度尼西亚 */
    @Excel(name = "弹窗标题-印度尼西亚")
    private String popUpTitleIdn;

    /** 弹窗标题-日本 */
    @Excel(name = "弹窗标题-日本")
    private String popUpTitleJp;

    /** 弹窗标题-韩国 */
    @Excel(name = "弹窗标题-韩国")
    private String popUpTitleKo;

    /** 弹窗标题-马来西亚 */
    @Excel(name = "弹窗标题-马来西亚")
    private String popUpTitleMy;

    /** 弹窗标题-泰国 */
    @Excel(name = "弹窗标题-泰国")
    private String popUpTitleTh;

    /** 弹窗标题-越南 */
    @Excel(name = "弹窗标题-越南")
    private String popUpTitleVi;

    /** 弹窗标题-葡萄牙 */
    @Excel(name = "弹窗标题-葡萄牙")
    private String popUpTitlePt;

    /** 弹窗标题-俄语 */
    @Excel(name = "弹窗标题-俄语")
    private String popUpTitleRus;

    /** 弹窗标题-白俄罗斯 */
    @Excel(name = "弹窗标题-白俄罗斯")
    private String popUpTitleBlr;

    /** 弹窗标题-印度 */
    @Excel(name = "弹窗标题-印度")
    private String popUpTitleIda;

    /** 弹窗标题-沙特阿拉伯 */
    @Excel(name = "弹窗标题-沙特阿拉伯")
    private String popUpTitleSa;

    /** 弹窗标题-阿拉伯 */
    @Excel(name = "弹窗标题-阿拉伯")
    private String popUpTitleAr;

    /** 弹窗标题-意大利 */
    @Excel(name = "弹窗标题-意大利")
    private String popUpTitleIt;

    /** 弹窗标题-土耳其 */
    @Excel(name = "弹窗标题-土耳其")
    private String popUpTitleTr;

    /** 弹窗内容-英文 */
    @Excel(name = "弹窗内容-英文")
    private String popUpContentEn;

    /** 弹窗内容-繁体 */
    @Excel(name = "弹窗内容-繁体")
    private String popUpContentTc;

    /** 弹窗内容-德国 */
    @Excel(name = "弹窗内容-德国")
    private String popUpContentDe;

    /** 弹窗内容-西班牙 */
    @Excel(name = "弹窗内容-西班牙")
    private String popUpContentEs;

    /** 弹窗内容-法国 */
    @Excel(name = "弹窗内容-法国")
    private String popUpContentFr;

    /** 弹窗内容-印度尼西亚 */
    @Excel(name = "弹窗内容-印度尼西亚")
    private String popUpContentIdn;

    /** 弹窗内容-日本 */
    @Excel(name = "弹窗内容-日本")
    private String popUpContentJp;

    /** 弹窗内容-韩国 */
    @Excel(name = "弹窗内容-韩国")
    private String popUpContentKo;

    /** 弹窗内容-马来西亚 */
    @Excel(name = "弹窗内容-马来西亚")
    private String popUpContentMy;

    /** 弹窗内容-泰国 */
    @Excel(name = "弹窗内容-泰国")
    private String popUpContentTh;

    /** 弹窗内容-越南 */
    @Excel(name = "弹窗内容-越南")
    private String popUpContentVi;

    /** 弹窗内容-葡萄牙 */
    @Excel(name = "弹窗内容-葡萄牙")
    private String popUpContentPt;

    /** 弹窗内容-俄语 */
    @Excel(name = "弹窗内容-俄语")
    private String popUpContentRus;

    /** 弹窗内容-白俄罗斯 */
    @Excel(name = "弹窗内容-白俄罗斯")
    private String popUpContentBlr;

    /** 弹窗内容-印度 */
    @Excel(name = "弹窗内容-印度")
    private String popUpContentIda;

    /** 弹窗内容-沙特阿拉伯 */
    @Excel(name = "弹窗内容-沙特阿拉伯")
    private String popUpContentSa;

    /** 弹窗内容-阿拉伯 */
    @Excel(name = "弹窗内容-阿拉伯")
    private String popUpContentAr;

    /** 弹窗内容-意大利 */
    @Excel(name = "弹窗内容-意大利")
    private String popUpContentIt;

    /** 弹窗内容-土耳其 */
    @Excel(name = "弹窗内容-土耳其")
    private String popUpContentTr;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public String getPopUpTitle() {
        return popUpTitle;
    }

    public void setPopUpTitle(String popUpTitle) {
        this.popUpTitle = popUpTitle;
    }

    public void setPopUpContent(String popUpContent)
    {
        this.popUpContent = popUpContent;
    }

    public String getPopUpContent() 
    {
        return popUpContent;
    }
    public void setShowTime(Date showTime) 
    {
        this.showTime = showTime;
    }

    public Date getShowTime() 
    {
        return showTime;
    }
    public void setPopUpTitleEn(String popUpTitleEn) 
    {
        this.popUpTitleEn = popUpTitleEn;
    }

    public String getPopUpTitleEn() 
    {
        return popUpTitleEn;
    }
    public void setPopUpTitleTc(String popUpTitleTc) 
    {
        this.popUpTitleTc = popUpTitleTc;
    }

    public String getPopUpTitleTc() 
    {
        return popUpTitleTc;
    }
    public void setPopUpTitleDe(String popUpTitleDe) 
    {
        this.popUpTitleDe = popUpTitleDe;
    }

    public String getPopUpTitleDe() 
    {
        return popUpTitleDe;
    }
    public void setPopUpTitleEs(String popUpTitleEs) 
    {
        this.popUpTitleEs = popUpTitleEs;
    }

    public String getPopUpTitleEs() 
    {
        return popUpTitleEs;
    }
    public void setPopUpTitleFr(String popUpTitleFr) 
    {
        this.popUpTitleFr = popUpTitleFr;
    }

    public String getPopUpTitleFr() 
    {
        return popUpTitleFr;
    }
    public void setPopUpTitleIdn(String popUpTitleIdn) 
    {
        this.popUpTitleIdn = popUpTitleIdn;
    }

    public String getPopUpTitleIdn() 
    {
        return popUpTitleIdn;
    }
    public void setPopUpTitleJp(String popUpTitleJp) 
    {
        this.popUpTitleJp = popUpTitleJp;
    }

    public String getPopUpTitleJp() 
    {
        return popUpTitleJp;
    }
    public void setPopUpTitleKo(String popUpTitleKo) 
    {
        this.popUpTitleKo = popUpTitleKo;
    }

    public String getPopUpTitleKo() 
    {
        return popUpTitleKo;
    }
    public void setPopUpTitleMy(String popUpTitleMy) 
    {
        this.popUpTitleMy = popUpTitleMy;
    }

    public String getPopUpTitleMy() 
    {
        return popUpTitleMy;
    }
    public void setPopUpTitleTh(String popUpTitleTh) 
    {
        this.popUpTitleTh = popUpTitleTh;
    }

    public String getPopUpTitleTh() 
    {
        return popUpTitleTh;
    }
    public void setPopUpTitleVi(String popUpTitleVi) 
    {
        this.popUpTitleVi = popUpTitleVi;
    }

    public String getPopUpTitleVi() 
    {
        return popUpTitleVi;
    }
    public void setPopUpTitlePt(String popUpTitlePt) 
    {
        this.popUpTitlePt = popUpTitlePt;
    }

    public String getPopUpTitlePt() 
    {
        return popUpTitlePt;
    }
    public void setPopUpTitleRus(String popUpTitleRus) 
    {
        this.popUpTitleRus = popUpTitleRus;
    }

    public String getPopUpTitleRus() 
    {
        return popUpTitleRus;
    }
    public void setPopUpTitleBlr(String popUpTitleBlr) 
    {
        this.popUpTitleBlr = popUpTitleBlr;
    }

    public String getPopUpTitleBlr() 
    {
        return popUpTitleBlr;
    }
    public void setPopUpTitleIda(String popUpTitleIda) 
    {
        this.popUpTitleIda = popUpTitleIda;
    }

    public String getPopUpTitleIda() 
    {
        return popUpTitleIda;
    }
    public void setPopUpTitleSa(String popUpTitleSa) 
    {
        this.popUpTitleSa = popUpTitleSa;
    }

    public String getPopUpTitleSa() 
    {
        return popUpTitleSa;
    }
    public void setPopUpTitleAr(String popUpTitleAr) 
    {
        this.popUpTitleAr = popUpTitleAr;
    }

    public String getPopUpTitleAr() 
    {
        return popUpTitleAr;
    }
    public void setPopUpTitleIt(String popUpTitleIt) 
    {
        this.popUpTitleIt = popUpTitleIt;
    }

    public String getPopUpTitleIt() 
    {
        return popUpTitleIt;
    }

    public String getPopUpTitleTr() {
        return popUpTitleTr;
    }

    public void setPopUpTitleTr(String popUpTitleTr) {
        this.popUpTitleTr = popUpTitleTr;
    }

    public void setPopUpContentEn(String popUpContentEn)
    {
        this.popUpContentEn = popUpContentEn;
    }

    public String getPopUpContentEn() 
    {
        return popUpContentEn;
    }
    public void setPopUpContentTc(String popUpContentTc) 
    {
        this.popUpContentTc = popUpContentTc;
    }

    public String getPopUpContentTc() 
    {
        return popUpContentTc;
    }
    public void setPopUpContentDe(String popUpContentDe) 
    {
        this.popUpContentDe = popUpContentDe;
    }

    public String getPopUpContentDe() 
    {
        return popUpContentDe;
    }
    public void setPopUpContentEs(String popUpContentEs) 
    {
        this.popUpContentEs = popUpContentEs;
    }

    public String getPopUpContentEs() 
    {
        return popUpContentEs;
    }
    public void setPopUpContentFr(String popUpContentFr) 
    {
        this.popUpContentFr = popUpContentFr;
    }

    public String getPopUpContentFr() 
    {
        return popUpContentFr;
    }
    public void setPopUpContentIdn(String popUpContentIdn) 
    {
        this.popUpContentIdn = popUpContentIdn;
    }

    public String getPopUpContentIdn() 
    {
        return popUpContentIdn;
    }
    public void setPopUpContentJp(String popUpContentJp) 
    {
        this.popUpContentJp = popUpContentJp;
    }

    public String getPopUpContentJp() 
    {
        return popUpContentJp;
    }
    public void setPopUpContentKo(String popUpContentKo) 
    {
        this.popUpContentKo = popUpContentKo;
    }

    public String getPopUpContentKo() 
    {
        return popUpContentKo;
    }
    public void setPopUpContentMy(String popUpContentMy) 
    {
        this.popUpContentMy = popUpContentMy;
    }

    public String getPopUpContentMy() 
    {
        return popUpContentMy;
    }
    public void setPopUpContentTh(String popUpContentTh) 
    {
        this.popUpContentTh = popUpContentTh;
    }

    public String getPopUpContentTh() 
    {
        return popUpContentTh;
    }
    public void setPopUpContentVi(String popUpContentVi) 
    {
        this.popUpContentVi = popUpContentVi;
    }

    public String getPopUpContentVi() 
    {
        return popUpContentVi;
    }
    public void setPopUpContentPt(String popUpContentPt) 
    {
        this.popUpContentPt = popUpContentPt;
    }

    public String getPopUpContentPt() 
    {
        return popUpContentPt;
    }
    public void setPopUpContentRus(String popUpContentRus) 
    {
        this.popUpContentRus = popUpContentRus;
    }

    public String getPopUpContentRus() 
    {
        return popUpContentRus;
    }
    public void setPopUpContentBlr(String popUpContentBlr) 
    {
        this.popUpContentBlr = popUpContentBlr;
    }

    public String getPopUpContentBlr() 
    {
        return popUpContentBlr;
    }
    public void setPopUpContentIda(String popUpContentIda) 
    {
        this.popUpContentIda = popUpContentIda;
    }

    public String getPopUpContentIda() 
    {
        return popUpContentIda;
    }
    public void setPopUpContentSa(String popUpContentSa) 
    {
        this.popUpContentSa = popUpContentSa;
    }

    public String getPopUpContentSa() 
    {
        return popUpContentSa;
    }
    public void setPopUpContentAr(String popUpContentAr) 
    {
        this.popUpContentAr = popUpContentAr;
    }

    public String getPopUpContentAr() 
    {
        return popUpContentAr;
    }
    public void setPopUpContentIt(String popUpContentIt) 
    {
        this.popUpContentIt = popUpContentIt;
    }

    public String getPopUpContentIt() 
    {
        return popUpContentIt;
    }

    public String getPopUpContentTr() {
        return popUpContentTr;
    }

    public void setPopUpContentTr(String popUpContentTr) {
        this.popUpContentTr = popUpContentTr;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("popUpTitle", getPopUpTitle())
            .append("popUpContent", getPopUpContent())
            .append("showTime", getShowTime())
            .append("createTime", getCreateTime())
            .append("popUpTitleEn", getPopUpTitleEn())
            .append("popUpTitleTc", getPopUpTitleTc())
            .append("popUpTitleDe", getPopUpTitleDe())
            .append("popUpTitleEs", getPopUpTitleEs())
            .append("popUpTitleFr", getPopUpTitleFr())
            .append("popUpTitleIdn", getPopUpTitleIdn())
            .append("popUpTitleJp", getPopUpTitleJp())
            .append("popUpTitleKo", getPopUpTitleKo())
            .append("popUpTitleMy", getPopUpTitleMy())
            .append("popUpTitleTh", getPopUpTitleTh())
            .append("popUpTitleVi", getPopUpTitleVi())
            .append("popUpTitlePt", getPopUpTitlePt())
            .append("popUpTitleRus", getPopUpTitleRus())
            .append("popUpTitleBlr", getPopUpTitleBlr())
            .append("popUpTitleIda", getPopUpTitleIda())
            .append("popUpTitleSa", getPopUpTitleSa())
            .append("popUpTitleAr", getPopUpTitleAr())
            .append("popUpTitleIt", getPopUpTitleIt())
            .append("popUpContentEn", getPopUpContentEn())
            .append("popUpContentTc", getPopUpContentTc())
            .append("popUpContentDe", getPopUpContentDe())
            .append("popUpContentEs", getPopUpContentEs())
            .append("popUpContentFr", getPopUpContentFr())
            .append("popUpContentIdn", getPopUpContentIdn())
            .append("popUpContentJp", getPopUpContentJp())
            .append("popUpContentKo", getPopUpContentKo())
            .append("popUpContentMy", getPopUpContentMy())
            .append("popUpContentTh", getPopUpContentTh())
            .append("popUpContentVi", getPopUpContentVi())
            .append("popUpContentPt", getPopUpContentPt())
            .append("popUpContentRus", getPopUpContentRus())
            .append("popUpContentBlr", getPopUpContentBlr())
            .append("popUpContentIda", getPopUpContentIda())
            .append("popUpContentSa", getPopUpContentSa())
            .append("popUpContentAr", getPopUpContentAr())
            .append("popUpContentIt", getPopUpContentIt())
            .toString();
    }
}
