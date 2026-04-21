package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 帮助中心对象 help_center
 * 
 * @author ruoyi
 * @date 2023-11-22
 */
public class HelpCenter extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 问题 */
    @Excel(name = "问题")
    private String question;

    /** 答案 */
    @Excel(name = "答案")
    private String answer;

    /** 状态 0：启用 1：禁用 */
    @Excel(name = "状态 0：启用 1：禁用")
    private Integer status;

    /** 排序 */
    @Excel(name = "排序")
    private Integer sort;

    /** 问题-英文 */
    @Excel(name = "问题-英文")
    private String questionEn;

    /** 问题-繁体 */
    @Excel(name = "问题-繁体")
    private String questionTc;

    /** 问题-德国 */
    @Excel(name = "问题-德国")
    private String questionDe;

    /** 问题-西班牙 */
    @Excel(name = "问题-西班牙")
    private String questionEs;

    /** 问题-法国 */
    @Excel(name = "问题-法国")
    private String questionFr;

    /** 问题-印度尼西亚 */
    @Excel(name = "问题-印度尼西亚")
    private String questionIdn;

    /** 问题-日本 */
    @Excel(name = "问题-日本")
    private String questionJp;

    /** 问题-韩国 */
    @Excel(name = "问题-韩国")
    private String questionKo;

    /** 问题-马来西亚 */
    @Excel(name = "问题-马来西亚")
    private String questionMy;

    /** 问题-泰国 */
    @Excel(name = "问题-泰国")
    private String questionTh;

    /** 问题-越南 */
    @Excel(name = "问题-越南")
    private String questionVi;

    /** 问题-葡萄牙 */
    @Excel(name = "问题-葡萄牙")
    private String questionPt;

    /** 问题-俄语 */
    @Excel(name = "问题-俄语")
    private String questionRus;

    /** 问题-白俄罗斯 */
    @Excel(name = "问题-白俄罗斯")
    private String questionBlr;

    /** 问题-印度 */
    @Excel(name = "问题-印度")
    private String questionIda;

    /** 问题-沙特阿拉伯 */
    @Excel(name = "问题-沙特阿拉伯")
    private String questionSa;

    /** 问题-阿拉伯 */
    @Excel(name = "问题-阿拉伯")
    private String questionAr;

    /** 问题-意大利 */
    @Excel(name = "问题-意大利")
    private String questionIt;

    /** 问题-土耳其 */
    @Excel(name = "问题-土耳其")
    private String questionTr;

    /** 答案-英文 */
    @Excel(name = "答案-英文")
    private String answerEn;

    /** 答案-繁体 */
    @Excel(name = "答案-繁体")
    private String answerTc;

    /** 答案-德国 */
    @Excel(name = "答案-德国")
    private String answerDe;

    /** 答案-西班牙 */
    @Excel(name = "答案-西班牙")
    private String answerEs;

    /** 答案-法国 */
    @Excel(name = "答案-法国")
    private String answerFr;

    /** 答案-印度尼西亚 */
    @Excel(name = "答案-印度尼西亚")
    private String answerIdn;

    /** 答案-日本 */
    @Excel(name = "答案-日本")
    private String answerJp;

    /** 答案-韩国 */
    @Excel(name = "答案-韩国")
    private String answerKo;

    /** 答案-马来西亚 */
    @Excel(name = "答案-马来西亚")
    private String answerMy;

    /** 答案-泰国 */
    @Excel(name = "答案-泰国")
    private String answerTh;

    /** 答案-越南 */
    @Excel(name = "答案-越南")
    private String answerVi;

    /** 答案-葡萄牙 */
    @Excel(name = "答案-葡萄牙")
    private String answerPt;

    /** 答案-俄语 */
    @Excel(name = "答案-俄语")
    private String answerRus;

    /** 答案-白俄罗斯 */
    @Excel(name = "答案-白俄罗斯")
    private String answerBlr;

    /** 答案-印度 */
    @Excel(name = "答案-印度")
    private String answerIda;

    /** 答案-沙特阿拉伯 */
    @Excel(name = "答案-沙特阿拉伯")
    private String answerSa;

    /** 答案-阿拉伯 */
    @Excel(name = "答案-阿拉伯")
    private String answerAr;

    /** 答案-意大利 */
    @Excel(name = "答案-意大利")
    private String answerIt;

    /** 答案-土耳其 */
    @Excel(name = "答案-土耳其")
    private String answerTr;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setQuestion(String question) 
    {
        this.question = question;
    }

    public String getQuestion() 
    {
        return question;
    }
    public void setAnswer(String answer) 
    {
        this.answer = answer;
    }

    public String getAnswer() 
    {
        return answer;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public void setQuestionEn(String questionEn)
    {
        this.questionEn = questionEn;
    }

    public String getQuestionEn() 
    {
        return questionEn;
    }
    public void setQuestionTc(String questionTc) 
    {
        this.questionTc = questionTc;
    }

    public String getQuestionTc() 
    {
        return questionTc;
    }
    public void setQuestionDe(String questionDe) 
    {
        this.questionDe = questionDe;
    }

    public String getQuestionDe() 
    {
        return questionDe;
    }
    public void setQuestionEs(String questionEs) 
    {
        this.questionEs = questionEs;
    }

    public String getQuestionEs() 
    {
        return questionEs;
    }
    public void setQuestionFr(String questionFr) 
    {
        this.questionFr = questionFr;
    }

    public String getQuestionFr() 
    {
        return questionFr;
    }
    public void setQuestionIdn(String questionIdn) 
    {
        this.questionIdn = questionIdn;
    }

    public String getQuestionIdn() 
    {
        return questionIdn;
    }
    public void setQuestionJp(String questionJp) 
    {
        this.questionJp = questionJp;
    }

    public String getQuestionJp() 
    {
        return questionJp;
    }
    public void setQuestionKo(String questionKo) 
    {
        this.questionKo = questionKo;
    }

    public String getQuestionKo() 
    {
        return questionKo;
    }
    public void setQuestionMy(String questionMy) 
    {
        this.questionMy = questionMy;
    }

    public String getQuestionMy() 
    {
        return questionMy;
    }
    public void setQuestionTh(String questionTh) 
    {
        this.questionTh = questionTh;
    }

    public String getQuestionTh() 
    {
        return questionTh;
    }
    public void setQuestionVi(String questionVi) 
    {
        this.questionVi = questionVi;
    }

    public String getQuestionVi() 
    {
        return questionVi;
    }
    public void setQuestionPt(String questionPt) 
    {
        this.questionPt = questionPt;
    }

    public String getQuestionPt() 
    {
        return questionPt;
    }
    public void setQuestionRus(String questionRus) 
    {
        this.questionRus = questionRus;
    }

    public String getQuestionRus() 
    {
        return questionRus;
    }
    public void setQuestionBlr(String questionBlr) 
    {
        this.questionBlr = questionBlr;
    }

    public String getQuestionBlr() 
    {
        return questionBlr;
    }
    public void setQuestionIda(String questionIda) 
    {
        this.questionIda = questionIda;
    }

    public String getQuestionIda() 
    {
        return questionIda;
    }
    public void setQuestionSa(String questionSa) 
    {
        this.questionSa = questionSa;
    }

    public String getQuestionSa() 
    {
        return questionSa;
    }
    public void setQuestionAr(String questionAr) 
    {
        this.questionAr = questionAr;
    }

    public String getQuestionAr() 
    {
        return questionAr;
    }
    public void setQuestionIt(String questionIt) 
    {
        this.questionIt = questionIt;
    }

    public String getQuestionIt() 
    {
        return questionIt;
    }

    public String getQuestionTr() {
        return questionTr;
    }

    public void setQuestionTr(String questionTr) {
        this.questionTr = questionTr;
    }

    public void setAnswerEn(String answerEn)
    {
        this.answerEn = answerEn;
    }

    public String getAnswerEn() 
    {
        return answerEn;
    }
    public void setAnswerTc(String answerTc) 
    {
        this.answerTc = answerTc;
    }

    public String getAnswerTc() 
    {
        return answerTc;
    }
    public void setAnswerDe(String answerDe) 
    {
        this.answerDe = answerDe;
    }

    public String getAnswerDe() 
    {
        return answerDe;
    }
    public void setAnswerEs(String answerEs) 
    {
        this.answerEs = answerEs;
    }

    public String getAnswerEs() 
    {
        return answerEs;
    }
    public void setAnswerFr(String answerFr) 
    {
        this.answerFr = answerFr;
    }

    public String getAnswerFr() 
    {
        return answerFr;
    }
    public void setAnswerIdn(String answerIdn) 
    {
        this.answerIdn = answerIdn;
    }

    public String getAnswerIdn() 
    {
        return answerIdn;
    }
    public void setAnswerJp(String answerJp) 
    {
        this.answerJp = answerJp;
    }

    public String getAnswerJp() 
    {
        return answerJp;
    }

    public String getAnswerKo() {
        return answerKo;
    }

    public void setAnswerKo(String answerKo) {
        this.answerKo = answerKo;
    }

    public void setAnswerMy(String answerMy)
    {
        this.answerMy = answerMy;
    }

    public String getAnswerMy() 
    {
        return answerMy;
    }
    public void setAnswerTh(String answerTh) 
    {
        this.answerTh = answerTh;
    }

    public String getAnswerTh() 
    {
        return answerTh;
    }
    public void setAnswerVi(String answerVi) 
    {
        this.answerVi = answerVi;
    }

    public String getAnswerVi() 
    {
        return answerVi;
    }
    public void setAnswerPt(String answerPt) 
    {
        this.answerPt = answerPt;
    }

    public String getAnswerPt() 
    {
        return answerPt;
    }
    public void setAnswerRus(String answerRus) 
    {
        this.answerRus = answerRus;
    }

    public String getAnswerRus() 
    {
        return answerRus;
    }
    public void setAnswerBlr(String answerBlr) 
    {
        this.answerBlr = answerBlr;
    }

    public String getAnswerBlr() 
    {
        return answerBlr;
    }
    public void setAnswerIda(String answerIda) 
    {
        this.answerIda = answerIda;
    }

    public String getAnswerIda() 
    {
        return answerIda;
    }
    public void setAnswerSa(String answerSa) 
    {
        this.answerSa = answerSa;
    }

    public String getAnswerSa() 
    {
        return answerSa;
    }
    public void setAnswerAr(String answerAr) 
    {
        this.answerAr = answerAr;
    }

    public String getAnswerAr() 
    {
        return answerAr;
    }
    public void setAnswerIt(String answerIt) 
    {
        this.answerIt = answerIt;
    }

    public String getAnswerIt() 
    {
        return answerIt;
    }

    public String getAnswerTr() {
        return answerTr;
    }

    public void setAnswerTr(String answerTr) {
        this.answerTr = answerTr;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("question", getQuestion())
            .append("answer", getAnswer())
            .append("questionEn", getQuestionEn())
            .append("questionTc", getQuestionTc())
            .append("questionDe", getQuestionDe())
            .append("questionEs", getQuestionEs())
            .append("questionFr", getQuestionFr())
            .append("questionIdn", getQuestionIdn())
            .append("questionJp", getQuestionJp())
            .append("questionKo", getQuestionKo())
            .append("questionMy", getQuestionMy())
            .append("questionTh", getQuestionTh())
            .append("questionVi", getQuestionVi())
            .append("questionPt", getQuestionPt())
            .append("questionRus", getQuestionRus())
            .append("questionBlr", getQuestionBlr())
            .append("questionIda", getQuestionIda())
            .append("questionSa", getQuestionSa())
            .append("questionAr", getQuestionAr())
            .append("questionIt", getQuestionIt())
            .append("answerEn", getAnswerEn())
            .append("answerTc", getAnswerTc())
            .append("answerDe", getAnswerDe())
            .append("answerEs", getAnswerEs())
            .append("answerFr", getAnswerFr())
            .append("answerIdn", getAnswerIdn())
            .append("answerJp", getAnswerJp())
            .append("answerMy", getAnswerMy())
            .append("answerTh", getAnswerTh())
            .append("answerVi", getAnswerVi())
            .append("answerPt", getAnswerPt())
            .append("answerRus", getAnswerRus())
            .append("answerBlr", getAnswerBlr())
            .append("answerIda", getAnswerIda())
            .append("answerSa", getAnswerSa())
            .append("answerAr", getAnswerAr())
            .append("answerIt", getAnswerIt())
            .toString();
    }
}
