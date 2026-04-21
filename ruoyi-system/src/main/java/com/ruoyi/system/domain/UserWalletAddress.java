package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.utils.cache.CacheUtil;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 用户虚拟货币钱包地址对象 user_wallet_address
 * 
 * @author ruoyi
 * @date 2023-10-29
 */
public class UserWalletAddress extends BaseEntity
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

    /** 币种id */
    @Excel(name = "币种id")
    private Long currencyId;

    /** 币种名称 */
    @Excel(name = "币种名称")
    private String currencyName;

    /** 币种名称多语言 */
    @Excel(name = "币种名称多语言")
    private LangMgr currencyNameLang;

    /** 钱包地址 */
    @Excel(name = "钱包地址")
    private String walletAddress;

    /** 钱包地址类型 */
    @Excel(name = "钱包地址类型")
    private String walletAddressType;

    /** 钱包收款码 */
    @Excel(name = "钱包收款码")
    private String walletReceiptQrCode;

    /** 钱包标签 */
    @Excel(name = "钱包标签")
    private String walletTag;

    public String getWalletReceiptQrCode() {
        return walletReceiptQrCode;
    }

    public void setWalletReceiptQrCode(String walletReceiptQrCode) {
        this.walletReceiptQrCode = walletReceiptQrCode;
    }

    public String getWalletAddressType() {
        return walletAddressType;
    }

    public void setWalletAddressType(String walletAddressType) {
        this.walletAddressType = walletAddressType;
    }


    public String getCurrencyName() {
        return currencyName;
    }


    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public LangMgr getCurrencyNameLang() {
        return currencyNameLang;
    }

    public void setCurrencyNameLang(LangMgr currencyNameLang) {
        this.currencyNameLang = currencyNameLang;
    }

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
    public void setCurrencyId(Long currencyId) 
    {
        this.currencyId = currencyId;
    }

    public Long getCurrencyId() 
    {
        return currencyId;
    }
    public void setWalletAddress(String walletAddress) 
    {
        this.walletAddress = walletAddress;
    }

    public String getWalletAddress() 
    {
        return walletAddress;
    }

    public String getWalletTag() {
        return walletTag;
    }

    public void setWalletTag(String walletTag) {
        this.walletTag = walletTag;
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
            .append("currencyId", getCurrencyId())
            .append("walletAddress", getWalletAddress())
            .append("createTime", getCreateTime())
            .toString();
    }
}
