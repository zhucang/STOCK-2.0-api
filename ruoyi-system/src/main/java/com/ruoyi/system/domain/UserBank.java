package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.domain.entity.UserInfoDetailVo;
import com.ruoyi.common.utils.cache.CacheUtil;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 用户银行卡对象 user_bank
 * 
 * @author ruoyi
 * @date 2023-11-23
 */
public class UserBank extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    /** 用户编号 */
    @Excel(name = "用户编号")
    private Long userNo;

    /** 银行名称 */
    @Excel(name = "银行名称")
    private String bankName;

    /** 银行卡号 */
    @Excel(name = "银行卡号")
    private String bankNo;

    /** 开户行 */
    @Excel(name = "开户行")
    private String bankOpenAddress;

    /** 开户人姓名 */
    @Excel(name = "开户人姓名")
    private String holder;

    /** 银行卡图片 */
    @Excel(name = "银行卡图片")
    private String bankImg;

    /** 绑定手机号 */
    @Excel(name = "绑定手机号")
    private String bankPhone;

    /** 银行国家 */
    @Excel(name = "银行国家")
    private String bankCountry;

    /** ABA代码 */
    @Excel(name = "ABA代码")
    private String abaCode;

    /** swift */
    @Excel(name = "swift")
    private String swift;

    /** 邮编号码 */
    @Excel(name = "邮编号码")
    private String postCode;

    /** 用户地址 */
    @Excel(name = "用户地址")
    private String userRealAddress;

    /** 账户类型 */
    @Excel(name = "账户类型")
    private String accountType;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setBankName(String bankName) 
    {
        this.bankName = bankName;
    }

    public String getBankName() 
    {
        return bankName;
    }
    public void setBankNo(String bankNo) 
    {
        this.bankNo = bankNo;
    }

    public String getBankNo() 
    {
        return bankNo;
    }
    public void setBankOpenAddress(String bankOpenAddress) 
    {
        this.bankOpenAddress = bankOpenAddress;
    }

    public String getBankOpenAddress() 
    {
        return bankOpenAddress;
    }
    public void setHolder(String holder) 
    {
        this.holder = holder;
    }

    public String getHolder() 
    {
        return holder;
    }
    public void setBankImg(String bankImg) 
    {
        this.bankImg = bankImg;
    }

    public String getBankImg() 
    {
        return bankImg;
    }
    public void setBankPhone(String bankPhone) 
    {
        this.bankPhone = bankPhone;
    }

    public String getBankPhone() 
    {
        return bankPhone;
    }
    public void setBankCountry(String bankCountry) 
    {
        this.bankCountry = bankCountry;
    }

    public String getBankCountry() 
    {
        return bankCountry;
    }
    public void setAbaCode(String abaCode) 
    {
        this.abaCode = abaCode;
    }

    public String getAbaCode() 
    {
        return abaCode;
    }
    public void setswift(String swift) 
    {
        this.swift = swift;
    }

    public String getswift() 
    {
        return swift;
    }
    public void setPostCode(String postCode) 
    {
        this.postCode = postCode;
    }

    public String getPostCode() 
    {
        return postCode;
    }
    public void setUserRealAddress(String userRealAddress) 
    {
        this.userRealAddress = userRealAddress;
    }

    public String getUserRealAddress() 
    {
        return userRealAddress;
    }
    public void setAccountType(String accountType) 
    {
        this.accountType = accountType;
    }

    public String getAccountType() 
    {
        return accountType;
    }

    public Long getUserNo() {
        try{
            return CacheUtil.getOtherValueByKey("appShow_idAddValue", Long.class) + getUserId();
        }catch (Exception e){
            return getUserId();
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("bankName", getBankName())
            .append("bankNo", getBankNo())
            .append("bankOpenAddress", getBankOpenAddress())
            .append("holder", getHolder())
            .append("bankImg", getBankImg())
            .append("bankPhone", getBankPhone())
            .append("bankCountry", getBankCountry())
            .append("abaCode", getAbaCode())
            .append("swift", getswift())
            .append("postCode", getPostCode())
            .append("createTime", getCreateTime())
            .append("remark", getRemark())
            .append("userRealAddress", getUserRealAddress())
            .append("accountType", getAccountType())
            .toString();
    }
}
