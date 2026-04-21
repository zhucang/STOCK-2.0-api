package com.ruoyi.common.core.domain.ticker;

//该代码的最新每日柱线
public class TickerInfoDay {

    //开盘价
    private String openPriceDay;

    //收盘价
    private String closePriceDay;

    //最高价
    private String maxPriceDay;

    //最低价
    private String minPriceDay;

    //交易量
    private String volumeDay;

    //成交量加权平均价
    private String averagePriceDay;

    //总交易金额=交易量*成交量加权平均价
    private String tradeAmount;

    public String getOpenPriceDay() {
        return openPriceDay;
    }

    public void setOpenPriceDay(String openPriceDay) {
        this.openPriceDay = openPriceDay;
    }

    public String getClosePriceDay() {
        return closePriceDay;
    }

    public void setClosePriceDay(String closePriceDay) {
        this.closePriceDay = closePriceDay;
    }

    public String getMaxPriceDay() {
        return maxPriceDay;
    }

    public void setMaxPriceDay(String maxPriceDay) {
        this.maxPriceDay = maxPriceDay;
    }

    public String getMinPriceDay() {
        return minPriceDay;
    }

    public void setMinPriceDay(String minPriceDay) {
        this.minPriceDay = minPriceDay;
    }

    public String getVolumeDay() {
        return volumeDay;
    }

    public void setVolumeDay(String volumeDay) {
        this.volumeDay = volumeDay;
    }

    public String getAveragePriceDay() {
        return averagePriceDay;
    }

    public void setAveragePriceDay(String averagePriceDay) {
        this.averagePriceDay = averagePriceDay;
    }

    public String getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(String tradeAmount) {
        this.tradeAmount = tradeAmount;
    }
}
