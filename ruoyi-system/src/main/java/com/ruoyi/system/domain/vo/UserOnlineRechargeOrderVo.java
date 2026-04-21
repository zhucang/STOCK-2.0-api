package com.ruoyi.system.domain.vo;


import java.io.Serializable;

/**
 * 用户在线支付订单信息
 */
public class UserOnlineRechargeOrderVo implements Serializable {

    //平台返回商户提交的订单号
    private String orderNo;

    //平台生成的订单号
    private String platformOrderNo;

    //法币单位
    private String fiat;

    //法币金额
    private String fiatAmount;

    //USDT费率
    private String usdtRate;

    //USDT金额
    private String usdtAmount;

    //状态： “ 1 ” 代表支付成功
    private String state;

    //商户发起订单用户ID
    private String userId;

    //订单备注消息
    private String tipMsg;

    //订单发起通道，代付此参数代表：法币代付OR数字货币代付
    private String payType;

    //签名
    private String sign;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPlatformOrderNo() {
        return platformOrderNo;
    }

    public void setPlatformOrderNo(String platformOrderNo) {
        this.platformOrderNo = platformOrderNo;
    }

    public String getFiat() {
        return fiat;
    }

    public void setFiat(String fiat) {
        this.fiat = fiat;
    }

    public String getFiatAmount() {
        return fiatAmount;
    }

    public void setFiatAmount(String fiatAmount) {
        this.fiatAmount = fiatAmount;
    }

    public String getUsdtRate() {
        return usdtRate;
    }

    public void setUsdtRate(String usdtRate) {
        this.usdtRate = usdtRate;
    }

    public String getUsdtAmount() {
        return usdtAmount;
    }

    public void setUsdtAmount(String usdtAmount) {
        this.usdtAmount = usdtAmount;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTipMsg() {
        return tipMsg;
    }

    public void setTipMsg(String tipMsg) {
        this.tipMsg = tipMsg;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}





