package com.ruoyi.common.utils;

import com.ruoyi.common.utils.cache.CacheUtil;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 时间工具类
 * 
 * @author ruoyi
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils
{
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", 
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     * 
     * @return Date() 当前日期
     */
    public static Date getNowDate()
    {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     * 
     * @return String
     */
    public static String getDate()
    {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime()
    {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow()
    {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format)
    {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date)
    {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date)
    {
        return new SimpleDateFormat(format).format(date);
    }

    public static final String parseDateToStr(final String format, final Date date,String timeZone)
    {
        if (StringUtils.isEmpty(timeZone)){
            timeZone = CacheUtil.getOtherValueByKey("jackson_time_zone",String.class);
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            TimeZone zone = TimeZone.getTimeZone(timeZone);
            if (zone.getID().equals(timeZone)){
                simpleDateFormat.setTimeZone(zone);
            }
        }catch (Exception e){

        }
        return simpleDateFormat.format(date);
    }

    public static final Date dateTime(final String format, final String ts)
    {
        try
        {
            return new SimpleDateFormat(format).parse(ts);
        }
        catch (ParseException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static final Date dateTime(final String format, final String ts,String timeZone)
    {
        if (StringUtils.isEmpty(timeZone)){
            timeZone = CacheUtil.getOtherValueByKey("jackson_time_zone",String.class);
        }
        try
        {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            try {
                TimeZone zone = TimeZone.getTimeZone(timeZone);
                if (zone.getID().equals(timeZone)){
                    simpleDateFormat.setTimeZone(zone);
                }
            }catch (Exception e){

            }
            return simpleDateFormat.parse(ts);
        }
        catch (ParseException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str)
    {
        if (str == null)
        {
            return null;
        }
        try
        {
            return parseDate(str.toString(), parsePatterns);
        }
        catch (ParseException e)
        {
            return null;
        }
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str,String timeZone)
    {
        if (str == null)
        {
            return null;
        }
        try
        {
            if (StringUtils.isEmpty(timeZone)){
                timeZone = CacheUtil.getOtherValueByKey("jackson_time_zone",String.class);
            }
            Date date = parseDate(str.toString(), parsePatterns);
            //转展示时区
            date = dateTime(YYYY_MM_DD_HH_MM_SS, parseDateToStr(YYYY_MM_DD_HH_MM_SS,date),timeZone);
            return date;
        }
        catch (ParseException e)
        {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate()
    {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算相差天数
     */
    public static int differentDaysByMillisecond(Date date1, Date date2)
    {
        return Math.abs((int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24)));
    }

    /**
     * 计算时间差
     *
     * @param endDate 最后时间
     * @param startTime 开始时间
     * @return 时间差（天/小时/分钟）
     */
    public static String timeDistance(Date endDate, Date startTime)
    {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - startTime.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 增加 LocalDateTime ==> Date
     */
    public static Date toDate(LocalDateTime temporalAccessor)
    {
        ZonedDateTime zdt = temporalAccessor.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 增加 LocalDate ==> Date
     */
    public static Date toDate(LocalDate temporalAccessor)
    {
        LocalDateTime localDateTime = LocalDateTime.of(temporalAccessor, LocalTime.of(0, 0, 0));
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 比较两个时间 时分秒 大小
     * @param s1
     * @param s2
     * @return
     */
    public static boolean compTime(String s1,String s2){
        String[]array1 = s1.split(":");
        int total1 = Integer.valueOf(array1[0])*3600+Integer.valueOf(array1[1])*60;
        String[]array2 = s2.split(":");
        int total2 = Integer.valueOf(array2[0])*3600+Integer.valueOf(array2[1])*60;
        return total1-total2>0?true:false;
    }

    /**
     * 验证是否在提现时间内
     * @param beginTime 提现开始时间
     * @param endTime 提现结束时间
     * @return
     */
    public static boolean checkIsWithTime(String beginTime, String endTime) {
        String now = parseDateToStr("HH:mm",new Date());
        if (beginTime.equals(endTime) && beginTime.equals("00:00")){
            //如果设置的时间相等，等于全天开放
            return true;
        }
        if (compTime(now,beginTime) && compTime(endTime,now)){
            return true;
        }
        return false;
    }

    //获取前后N（天、月、年）时间
    public static Date getDateBeforeOrAfterDate(Date date,Integer unit,Integer amount){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(unit,amount);
        return calendar.getTime();
    }

    /**
     * 获取上个月的第一天
     */
    public static Long getLastMonthStartTime() {
        Long currentTime = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.setTimeInMillis(currentTime);
        calendar.add(Calendar.YEAR, 0);
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取上个月的最后一天
     */
    public static Long getLastMonthEndTime() {
        Long currentTime = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.setTimeInMillis(currentTime);
        calendar.add(Calendar.YEAR, 0);
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));// 获取当前月最后一天
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取某天的00:00
     */
    public static Date getStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取某天的23:59:59
     */
    public static Date getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * 获取今天是周几
     */
    public static Integer getDayOfWeek(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int weekday = calendar.get(Calendar.DAY_OF_WEEK);
        switch (weekday){
            case 1 : weekday = 7;break;
            case 2 : weekday = 1;break;
            case 3 : weekday = 2;break;
            case 4 : weekday = 3;break;
            case 5 : weekday = 4;break;
            case 6 : weekday = 5;break;
            case 7 : weekday = 6;break;
        }
        return weekday;
    }

}
