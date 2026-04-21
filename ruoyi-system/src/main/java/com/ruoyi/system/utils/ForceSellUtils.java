package com.ruoyi.system.utils;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.system.domain.ProductSetting;

import java.math.BigDecimal;

/**
 * 强制平仓工具类
 */
public class ForceSellUtils {

    /**
     *
     * @param userEnableAmount 用户可用资金
     * @param userMarginAmount 用户保证金
     * @param productSetting 获取forceStopPercent 强制平仓比例 liquidationMethod  爆仓模式  1:保证金+账户余额 2:保证金亏完 3:全亏损
     * @return //返回-1表示不进行爆仓
     */
    public static BigDecimal getUserForceAmount(BigDecimal userEnableAmount, BigDecimal userMarginAmount, ProductSetting productSetting) throws RuntimeException{
        //爆仓比例
        BigDecimal forceStopPercent = productSetting.getForceStopPercent();
        //爆仓模式 1:保证金+账户余额亏完 2:保证金亏完 3:全亏损(余额可到负数)
        Integer liquidationMethod = productSetting.getLiquidationMethod();
        if (userEnableAmount == null || userMarginAmount == null || forceStopPercent == null){
            throw new RuntimeException("获取强制平仓线失败,参数错误！");
        }
        if (liquidationMethod.equals(1)){
            return (userEnableAmount.add(userMarginAmount)).multiply(forceStopPercent).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
        }else if (liquidationMethod.equals(2)){
            return (userMarginAmount).multiply(forceStopPercent).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
        }else if (liquidationMethod.equals(3)){
            return new BigDecimal("-1");
        }else {
            throw new RuntimeException("获取强制平仓线失败，爆仓模式不存在");
        }
    }
}
