package com.ruoyi.system.utils;


import com.ruoyi.common.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class BuyAndSellUtils {

    private static final Logger log = LoggerFactory.getLogger(BuyAndSellUtils.class);


    /**
     * 校验当前时间是否在交易时间内（只验证时分）
     * @param beginTimeStr 开始时间
     * @param endTimeStr 结束时间
     * @return true 在时间段内   false不在时间段内
     */
    public static boolean isTransTime(String beginTimeStr, String endTimeStr){
        if (beginTimeStr == null || endTimeStr == null){
            return false;
        }
        beginTimeStr = beginTimeStr.replace(" ","");
        endTimeStr = endTimeStr.replace(" ","");
        if (StringUtils.isEmpty(beginTimeStr) || StringUtils.isEmpty(endTimeStr)) {
            return false;
        }
        //实时时间
        Date nowDateTime = new Date();
//        //验证是否工作日
//        if (!isWorkDay(nowDateTime)) {
//            return false;
//        }

        //美东时区日期
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtils.YYYY_MM_DD);
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Toronto"));
        String date = dateFormat.format(new Date());
        beginTimeStr = date + " " + beginTimeStr;
        endTimeStr = date + " " + endTimeStr;

        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Toronto"));
        try {
            Date beginTime = dateFormat.parse(beginTimeStr);
            Date endTime = dateFormat.parse(endTimeStr);
            if (nowDateTime.after(beginTime) && nowDateTime.before(endTime)){
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

    public static boolean isWorkDay(Date currentDate){
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        if (cal.get(7) == 7 || cal.get(7) == 1) {
            return false;
        }
        return true;

    }

    public static boolean isCanSell(Date buyDate, int maxMinutes) {
        Long buyDateTimes = Long.valueOf(buyDate.getTime() / 1000L);

        buyDateTimes = Long.valueOf(buyDateTimes.longValue() + (maxMinutes * 60));

        Long nowDateTimes = Long.valueOf((new Date()).getTime() / 1000L);

        if (nowDateTimes.longValue() > buyDateTimes.longValue()) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        //美东时区日期
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtils.YYYY_MM_DD);
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Toronto"));
        String format = dateFormat.format(new Date());
    }
}
