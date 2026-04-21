package com.ruoyi.system.service;

import com.ruoyi.system.domain.UserUdunWalletAddress;
import com.ruoyi.system.domain.vo.UdunRechargeOrder;

import java.util.List;

public interface IUdunRechargeService {

    /**
     * 查询udun充值商户支持币种
     * @return
     */
    public List getMerchantSupportCoins();

    /**
     * 获取优盾支付钱包地址
     * @param mainCoinType 币种编号
     * @return
     */
    public UserUdunWalletAddress getPayWalletAddress(String mainCoinType,String name);

    /**
     * 优盾支付成功回调
     * @param udunRechargeOrder
     */
    public void udunRechargeCallUrl(UdunRechargeOrder udunRechargeOrder);
}
