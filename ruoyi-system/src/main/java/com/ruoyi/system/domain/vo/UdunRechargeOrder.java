package com.ruoyi.system.domain.vo;

import com.ruoyi.common.core.domain.BaseEntity;

public class UdunRechargeOrder extends BaseEntity {

    //时间戳
    private String timestamp;

    //随机数
    private String nonce;

    //签名
    private String sign;

    //body参数
    private String body;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
