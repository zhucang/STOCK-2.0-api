package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 站内信类型对象 site_message_type
 * 
 * @author ruoyi
 * @date 2026-04-12
 */
public class SiteMessageType extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 站内信类型 */
    private Long siteMessageTypeId;

    /** 站内信类型名称 */
    @Excel(name = "站内信类型名称")
    private String siteMessageTypeName;

    /** 站内信类型名称-英文 */
    @Excel(name = "站内信类型名称-英文")
    private String siteMessageTypeNameEn;

    /** 站内信类型名称-繁体 */
    @Excel(name = "站内信类型名称-繁体")
    private String siteMessageTypeNameTc;

    /** 站内信类型名称-德国 */
    @Excel(name = "站内信类型名称-德国")
    private String siteMessageTypeNameDe;

    /** 站内信类型名称-西班牙 */
    @Excel(name = "站内信类型名称-西班牙")
    private String siteMessageTypeNameEs;

    /** 站内信类型名称-法国 */
    @Excel(name = "站内信类型名称-法国")
    private String siteMessageTypeNameFr;

    /** 站内信类型名称-印度尼西亚 */
    @Excel(name = "站内信类型名称-印度尼西亚")
    private String siteMessageTypeNameIdn;

    /** 站内信类型名称-日本 */
    @Excel(name = "站内信类型名称-日本")
    private String siteMessageTypeNameJp;

    /** 站内信类型名称-韩国 */
    @Excel(name = "站内信类型名称-韩国")
    private String siteMessageTypeNameKo;

    /** 站内信类型名称-马来西亚 */
    @Excel(name = "站内信类型名称-马来西亚")
    private String siteMessageTypeNameMy;

    /** 站内信类型名称-泰国 */
    @Excel(name = "站内信类型名称-泰国")
    private String siteMessageTypeNameTh;

    /** 站内信类型名称-越南 */
    @Excel(name = "站内信类型名称-越南")
    private String siteMessageTypeNameVi;

    /** 站内信类型名称-葡萄牙 */
    @Excel(name = "站内信类型名称-葡萄牙")
    private String siteMessageTypeNamePt;

    /** 站内信类型名称-俄语 */
    @Excel(name = "站内信类型名称-俄语")
    private String siteMessageTypeNameRus;

    /** 站内信类型名称-白俄罗斯 */
    @Excel(name = "站内信类型名称-白俄罗斯")
    private String siteMessageTypeNameBlr;

    /** 站内信类型名称-印度 */
    @Excel(name = "站内信类型名称-印度")
    private String siteMessageTypeNameIda;

    /** 站内信类型名称-沙特阿拉伯 */
    @Excel(name = "站内信类型名称-沙特阿拉伯")
    private String siteMessageTypeNameSa;

    /** 站内信类型名称-阿拉伯 */
    @Excel(name = "站内信类型名称-阿拉伯")
    private String siteMessageTypeNameAr;

    /** 站内信类型名称-意大利 */
    @Excel(name = "站内信类型名称-意大利")
    private String siteMessageTypeNameIt;

    /** 站内信类型名称-土耳其 */
    @Excel(name = "站内信类型名称-土耳其")
    private String siteMessageTypeNameTr;

    public void setSiteMessageTypeId(Long siteMessageTypeId) 
    {
        this.siteMessageTypeId = siteMessageTypeId;
    }

    public Long getSiteMessageTypeId() 
    {
        return siteMessageTypeId;
    }
    public void setSiteMessageTypeName(String siteMessageTypeName) 
    {
        this.siteMessageTypeName = siteMessageTypeName;
    }

    public String getSiteMessageTypeName() 
    {
        return siteMessageTypeName;
    }
    public void setSiteMessageTypeNameEn(String siteMessageTypeNameEn) 
    {
        this.siteMessageTypeNameEn = siteMessageTypeNameEn;
    }

    public String getSiteMessageTypeNameEn() 
    {
        return siteMessageTypeNameEn;
    }
    public void setSiteMessageTypeNameTc(String siteMessageTypeNameTc) 
    {
        this.siteMessageTypeNameTc = siteMessageTypeNameTc;
    }

    public String getSiteMessageTypeNameTc() 
    {
        return siteMessageTypeNameTc;
    }
    public void setSiteMessageTypeNameDe(String siteMessageTypeNameDe) 
    {
        this.siteMessageTypeNameDe = siteMessageTypeNameDe;
    }

    public String getSiteMessageTypeNameDe() 
    {
        return siteMessageTypeNameDe;
    }
    public void setSiteMessageTypeNameEs(String siteMessageTypeNameEs) 
    {
        this.siteMessageTypeNameEs = siteMessageTypeNameEs;
    }

    public String getSiteMessageTypeNameEs() 
    {
        return siteMessageTypeNameEs;
    }
    public void setSiteMessageTypeNameFr(String siteMessageTypeNameFr) 
    {
        this.siteMessageTypeNameFr = siteMessageTypeNameFr;
    }

    public String getSiteMessageTypeNameFr() 
    {
        return siteMessageTypeNameFr;
    }
    public void setSiteMessageTypeNameIdn(String siteMessageTypeNameIdn) 
    {
        this.siteMessageTypeNameIdn = siteMessageTypeNameIdn;
    }

    public String getSiteMessageTypeNameIdn() 
    {
        return siteMessageTypeNameIdn;
    }
    public void setSiteMessageTypeNameJp(String siteMessageTypeNameJp) 
    {
        this.siteMessageTypeNameJp = siteMessageTypeNameJp;
    }

    public String getSiteMessageTypeNameJp() 
    {
        return siteMessageTypeNameJp;
    }
    public void setSiteMessageTypeNameKo(String siteMessageTypeNameKo) 
    {
        this.siteMessageTypeNameKo = siteMessageTypeNameKo;
    }

    public String getSiteMessageTypeNameKo() 
    {
        return siteMessageTypeNameKo;
    }
    public void setSiteMessageTypeNameMy(String siteMessageTypeNameMy) 
    {
        this.siteMessageTypeNameMy = siteMessageTypeNameMy;
    }

    public String getSiteMessageTypeNameMy() 
    {
        return siteMessageTypeNameMy;
    }
    public void setSiteMessageTypeNameTh(String siteMessageTypeNameTh) 
    {
        this.siteMessageTypeNameTh = siteMessageTypeNameTh;
    }

    public String getSiteMessageTypeNameTh() 
    {
        return siteMessageTypeNameTh;
    }
    public void setSiteMessageTypeNameVi(String siteMessageTypeNameVi) 
    {
        this.siteMessageTypeNameVi = siteMessageTypeNameVi;
    }

    public String getSiteMessageTypeNameVi() 
    {
        return siteMessageTypeNameVi;
    }
    public void setSiteMessageTypeNamePt(String siteMessageTypeNamePt) 
    {
        this.siteMessageTypeNamePt = siteMessageTypeNamePt;
    }

    public String getSiteMessageTypeNamePt() 
    {
        return siteMessageTypeNamePt;
    }
    public void setSiteMessageTypeNameRus(String siteMessageTypeNameRus) 
    {
        this.siteMessageTypeNameRus = siteMessageTypeNameRus;
    }

    public String getSiteMessageTypeNameRus() 
    {
        return siteMessageTypeNameRus;
    }
    public void setSiteMessageTypeNameBlr(String siteMessageTypeNameBlr) 
    {
        this.siteMessageTypeNameBlr = siteMessageTypeNameBlr;
    }

    public String getSiteMessageTypeNameBlr() 
    {
        return siteMessageTypeNameBlr;
    }
    public void setSiteMessageTypeNameIda(String siteMessageTypeNameIda) 
    {
        this.siteMessageTypeNameIda = siteMessageTypeNameIda;
    }

    public String getSiteMessageTypeNameIda() 
    {
        return siteMessageTypeNameIda;
    }
    public void setSiteMessageTypeNameSa(String siteMessageTypeNameSa) 
    {
        this.siteMessageTypeNameSa = siteMessageTypeNameSa;
    }

    public String getSiteMessageTypeNameSa() 
    {
        return siteMessageTypeNameSa;
    }
    public void setSiteMessageTypeNameAr(String siteMessageTypeNameAr) 
    {
        this.siteMessageTypeNameAr = siteMessageTypeNameAr;
    }

    public String getSiteMessageTypeNameAr() 
    {
        return siteMessageTypeNameAr;
    }
    public void setSiteMessageTypeNameIt(String siteMessageTypeNameIt) 
    {
        this.siteMessageTypeNameIt = siteMessageTypeNameIt;
    }

    public String getSiteMessageTypeNameIt() 
    {
        return siteMessageTypeNameIt;
    }
    public void setSiteMessageTypeNameTr(String siteMessageTypeNameTr) 
    {
        this.siteMessageTypeNameTr = siteMessageTypeNameTr;
    }

    public String getSiteMessageTypeNameTr() 
    {
        return siteMessageTypeNameTr;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("siteMessageTypeId", getSiteMessageTypeId())
            .append("siteMessageTypeName", getSiteMessageTypeName())
            .append("siteMessageTypeNameEn", getSiteMessageTypeNameEn())
            .append("siteMessageTypeNameTc", getSiteMessageTypeNameTc())
            .append("siteMessageTypeNameDe", getSiteMessageTypeNameDe())
            .append("siteMessageTypeNameEs", getSiteMessageTypeNameEs())
            .append("siteMessageTypeNameFr", getSiteMessageTypeNameFr())
            .append("siteMessageTypeNameIdn", getSiteMessageTypeNameIdn())
            .append("siteMessageTypeNameJp", getSiteMessageTypeNameJp())
            .append("siteMessageTypeNameKo", getSiteMessageTypeNameKo())
            .append("siteMessageTypeNameMy", getSiteMessageTypeNameMy())
            .append("siteMessageTypeNameTh", getSiteMessageTypeNameTh())
            .append("siteMessageTypeNameVi", getSiteMessageTypeNameVi())
            .append("siteMessageTypeNamePt", getSiteMessageTypeNamePt())
            .append("siteMessageTypeNameRus", getSiteMessageTypeNameRus())
            .append("siteMessageTypeNameBlr", getSiteMessageTypeNameBlr())
            .append("siteMessageTypeNameIda", getSiteMessageTypeNameIda())
            .append("siteMessageTypeNameSa", getSiteMessageTypeNameSa())
            .append("siteMessageTypeNameAr", getSiteMessageTypeNameAr())
            .append("siteMessageTypeNameIt", getSiteMessageTypeNameIt())
            .append("siteMessageTypeNameTr", getSiteMessageTypeNameTr())
            .toString();
    }
}
