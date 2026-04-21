package com.ruoyi.common.core.domain.ticker;

//该代码的昨日柱线
public class TickerInfoPrevDay {

    //开盘价
    private String openPricePrevDay;

    //收盘价
    private String closePricePrevDay;

    //最高价
    private String maxPricePrevDay;

    //最低价
    private String minPricePrevDay;

    //交易量
    private String volumePrevDay;

    //成交量加权平均价
    private String averagePricePrevDay;

    public String getOpenPricePrevDay() {
        return openPricePrevDay;
    }

    public void setOpenPricePrevDay(String openPricePrevDay) {
        this.openPricePrevDay = openPricePrevDay;
    }

    public String getClosePricePrevDay() {
        return closePricePrevDay;
    }

    public void setClosePricePrevDay(String closePricePrevDay) {
        this.closePricePrevDay = closePricePrevDay;
    }

    public String getMaxPricePrevDay() {
        return maxPricePrevDay;
    }

    public void setMaxPricePrevDay(String maxPricePrevDay) {
        this.maxPricePrevDay = maxPricePrevDay;
    }

    public String getMinPricePrevDay() {
        return minPricePrevDay;
    }

    public void setMinPricePrevDay(String minPricePrevDay) {
        this.minPricePrevDay = minPricePrevDay;
    }

    public String getVolumePrevDay() {
        return volumePrevDay;
    }

    public void setVolumePrevDay(String volumePrevDay) {
        this.volumePrevDay = volumePrevDay;
    }

    public String getAveragePricePrevDay() {
        return averagePricePrevDay;
    }

    public void setAveragePricePrevDay(String averagePricePrevDay) {
        this.averagePricePrevDay = averagePricePrevDay;
    }
}
