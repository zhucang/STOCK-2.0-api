package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.entity.UserInfoDetailVo;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 优盾加密货币钱包信息对象 user_udun_wallet_address
 * 
 * @author ruoyi
 * @date 2024-09-05
 */
public class UserUdunWalletAddress extends UserInfoDetailVo
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    /** 主币种编号 */
    @Excel(name = "主币种编号")
    private String mainCoinType;

    /** 币种名称 */
    @Excel(name = "币种名称")
    private String coinName;

    /** 钱包地址 */
    @Excel(name = "钱包地址")
    private String walletAddress;

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
    public void setMainCoinType(String mainCoinType)
    {
        this.mainCoinType = mainCoinType;
    }

    public String getMainCoinType()
    {
        return mainCoinType;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public void setWalletAddress(String walletAddress)
    {
        this.walletAddress = walletAddress;
    }

    public String getWalletAddress() 
    {
        return walletAddress;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("mainCoinType", getMainCoinType())
            .append("walletAddress", getWalletAddress())
            .append("createTime", getCreateTime())
            .toString();
    }
}
