package com.ruoyi.system.utils.currencyExchangeRate;


import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.ticker.TickerInfo;
import com.ruoyi.system.domain.PlatformCurrency;
import com.ruoyi.system.utils.ProductQuoteUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 汇率工具类
 */
public class ExchangeRateUtil {

    /**
     * 填充各币种兑USD的汇率
     * @param platformCurrencies
     */
    public static void fillExchangeRate(List<PlatformCurrency> platformCurrencies){
        //加密货币行情
        Map<String, TickerInfo> cryptoCurrencyQuote = ProductQuoteUtils.getCryptoCurrencyQuote(platformCurrencies.stream().filter(a -> a.getCurrencyType().equals(1) && a.getRealTimeExchangeRateFlag().equals(1)).map(a -> a.getRealTimeExchangeRateProduct()).collect(Collectors.joining(",")),false);
        //外汇行情
        Map<String, TickerInfo> forexQuote = ProductQuoteUtils.getForexQuote(platformCurrencies.stream().filter(a -> a.getCurrencyType().equals(0) && a.getRealTimeExchangeRateFlag().equals(1)).map(a -> a.getRealTimeExchangeRateProduct()).collect(Collectors.joining(",")));
        //遍历填入汇率
        for (int i = 0; i < platformCurrencies.size(); i++) {
            //币种信息
            PlatformCurrency platformCurrency = platformCurrencies.get(i);
            //如果是实时汇率
            if (platformCurrency.getRealTimeExchangeRateFlag().equals(1)){
                //实时汇率品种
                String realTimeExchangeRateProduct = platformCurrency.getRealTimeExchangeRateProduct();
                //行情信息
                TickerInfo tickerInfo;
                //如果是法币
                if (platformCurrency.getCurrencyType().equals(0)){
                    tickerInfo = forexQuote.get(realTimeExchangeRateProduct);
                }else {
                    //如果是加密货币
                    tickerInfo = cryptoCurrencyQuote.get(realTimeExchangeRateProduct);
                }
                if (tickerInfo != null){
                    platformCurrency.setFixedExchangeRate(new BigDecimal(tickerInfo.getNowPrice()));
                }else {
                    platformCurrency.setFixedExchangeRate(BigDecimal.ZERO);
                }
            }
        }
    }

    /**
     * 获取汇率详情 （汇率和手续费）
     * @param currencyIdFrom 转出币种id
     * @param currencyIdTo 转入币种id
     * @return
     */
    public static Map<String,BigDecimal> getExchangeInfo(Long currencyIdFrom, Long currencyIdTo, Map<Long, PlatformCurrency> platformCurrencyMap){
        //手续费率
        BigDecimal feeRate = BigDecimal.ZERO;
        //汇率
        BigDecimal exchangeRate = BigDecimal.ZERO;
        //from
        PlatformCurrency platformCurrencyFrom = platformCurrencyMap.get(currencyIdFrom);
        //to
        PlatformCurrency platformCurrencyTo = platformCurrencyMap.get(currencyIdTo);
        //如果获取币种信息正常
        if (platformCurrencyFrom != null && platformCurrencyTo != null){
            feeRate = platformCurrencyFrom.getExchangeHandlingFeeRate();
            //如果相同
            if (currencyIdFrom.equals(currencyIdTo)){
                exchangeRate = BigDecimal.ONE;
            }else {
                exchangeRate = platformCurrencyFrom.getFixedExchangeRate().divide(platformCurrencyTo.getFixedExchangeRate(),Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE);
            }
        }
        Map<String, BigDecimal> result = new HashMap<>();
        result.put("feeRatio",feeRate);
        result.put("exchangeRate",exchangeRate);
        return result;
    }
}
