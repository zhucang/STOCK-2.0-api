package com.ruoyi.common.core.domain.ticker;


//该代码的最近一分钟柱线
public class TickerInfoMin {

    //开盘价
    private String openPriceMin;

    //收盘价
    private String closePriceMin;

    //最高价
    private String maxPriceMin;

    //最低价
    private String minPriceMin;

    //交易量
    private String volumeMin;

    //成交量加权平均价
    private String averagePriceMin;

    public String getOpenPriceMin() {
        return openPriceMin;
    }

    public void setOpenPriceMin(String openPriceMin) {
        this.openPriceMin = openPriceMin;
    }

    public String getClosePriceMin() {
        return closePriceMin;
    }

    public void setClosePriceMin(String closePriceMin) {
        this.closePriceMin = closePriceMin;
    }

    public String getMaxPriceMin() {
        return maxPriceMin;
    }

    public void setMaxPriceMin(String maxPriceMin) {
        this.maxPriceMin = maxPriceMin;
    }

    public String getMinPriceMin() {
        return minPriceMin;
    }

    public void setMinPriceMin(String minPriceMin) {
        this.minPriceMin = minPriceMin;
    }

    public String getVolumeMin() {
        return volumeMin;
    }

    public void setVolumeMin(String volumeMin) {
        this.volumeMin = volumeMin;
    }

    public String getAveragePriceMin() {
        return averagePriceMin;
    }

    public void setAveragePriceMin(String averagePriceMin) {
        this.averagePriceMin = averagePriceMin;
    }
}
