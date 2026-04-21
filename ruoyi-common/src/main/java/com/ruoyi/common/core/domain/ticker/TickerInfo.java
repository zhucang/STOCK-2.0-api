package com.ruoyi.common.core.domain.ticker;


import com.ruoyi.common.constant.Constants;

import java.math.BigDecimal;

//股票快照
public class TickerInfo {

    //股票代码
    private String ticker;

    //现价
    private String nowPrice;

    //涨跌幅比率
    private String changeRate;

    //金额变化
    private String changePrice;

    //该代码的最新每日柱线
    private TickerInfoDay tickerInfoDay;

    //该代码的最近一分钟柱线
    private TickerInfoMin tickerInfoMin;

    //该代码的前一天柱线
    private TickerInfoPrevDay tickerInfoPrevDay;


    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getNowPrice() {
        if (nowPrice != null){
            return new BigDecimal(nowPrice).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE).stripTrailingZeros().toPlainString();
        }
        return BigDecimal.ZERO.toString();
    }

    public void setNowPrice(String nowPrice) {
        this.nowPrice = nowPrice;
    }

    public String getChangeRate() {
        if (changeRate != null){
            return new BigDecimal(changeRate).setScale(Constants.BIGDECIMAL_SCALE, Constants.BIGDECIMAL_ROUNDINGMODE).stripTrailingZeros().toPlainString();
        }
        return BigDecimal.ZERO.toString();
    }

    public void setChangeRate(String changeRate) {
        this.changeRate = changeRate;
    }

    public String getChangePrice() {
        return changePrice;
    }

    public void setChangePrice(String changePrice) {
        this.changePrice = changePrice;
    }

    public TickerInfoDay getTickerInfoDay() {
        return tickerInfoDay;
    }

    public void setTickerInfoDay(TickerInfoDay tickerInfoDay) {
        this.tickerInfoDay = tickerInfoDay;
    }

    public TickerInfoMin getTickerInfoMin() {
        return tickerInfoMin;
    }

    public void setTickerInfoMin(TickerInfoMin tickerInfoMin) {
        this.tickerInfoMin = tickerInfoMin;
    }

    public TickerInfoPrevDay getTickerInfoPrevDay() {
        return tickerInfoPrevDay;
    }

    public void setTickerInfoPrevDay(TickerInfoPrevDay tickerInfoPrevDay) {
        this.tickerInfoPrevDay = tickerInfoPrevDay;
    }
}
