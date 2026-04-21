package com.ruoyi.system.service;

import com.ruoyi.system.domain.SpotTradeOrder;
import com.ruoyi.system.domain.UserCryptocurrencyPosition;

/**
 * 量化Service接口
 *
 * @author ruoyi
 * @date 2023-11-12
 */
public interface ILiangHuaService {

    /**
     * 加密货币合约机器人订单录入
     * @return
     */
    public int robotUserCryptocurrencyPosition(UserCryptocurrencyPosition position);

    /**
     * 现货交易机器人订单录入
     * @param spotTradeOrder
     * @return
     */
    public int robotSpotTradeOrder(SpotTradeOrder spotTradeOrder);
}
