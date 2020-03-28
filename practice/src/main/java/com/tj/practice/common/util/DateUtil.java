package com.tj.practice.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: Mr-Thomas
 * @Date: 2020/1/17 13:56
 */
@Slf4j
public class DateUtil {
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String DATE_SIMPLE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_FULL_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_HOUR_FORMAT = "yyyy-MM-dd HH";
    public static final String DATE_MONTH_FORMAT = "yyyy-MM";
    public static final String TIME_SIMPLE_FORMAT = "HH:mm:ss";
    public static final String DATE_YEAR_FORMAT = "yyyy";
    public static final String DATE_FIXED_FORMAT = "yyyyMMddHHmmssSSS";
    public static final String FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_SIMPLE_FORMAT2 = "yyyyMMdd";
    public static final SimpleDateFormat DATE_FORMAT_S = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//    public static final SimpleDateFormat DATE_SIMPLE_FORMAT_S = new SimpleDateFormat("yyyy-MM-dd");
//    public static final SimpleDateFormat DATE_HOUR_FORMAT_S = new SimpleDateFormat("yyyy-MM-dd HH");
//    public static final SimpleDateFormat DATE_FULL_FORMAT_S = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    public static final SimpleDateFormat DATE_MONTH_FORMAT_S = new SimpleDateFormat("yyyy-MM");
//    public static final SimpleDateFormat DATE_YEAR_FORMAT_S = new SimpleDateFormat("yyyy");

    public DateUtil() {
    }

    public static long parseDate(String date, String format) {
        return toDate(date, format).getTime();
    }

    public static Date toDate(String date, String format) {
        if ("".equals(format) || format == null) {
            format = "yyyy-MM-dd HH:mm:ss.SSS";
        }

        try {
            format = StringUtils.isEmpty(format) ? "yyyy-MM-dd HH:mm:ss.SSS" : format;
            return (new SimpleDateFormat(format)).parse(date);
        } catch (ParseException e) {
            log.error("toDate exception", e);
            return getCurrentDate();
        }
    }

    public static Date toDate(String date) {
        try {
            return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(date);
        } catch (ParseException var2) {
            return getCurrentDate();
        }
    }

    public static Date toSafeDate(String date, String format) {
        return toDate(date, format);
    }

    public static Date toDate(long time) {
        return new Date(time);
    }

    public static String getCurrentDate(String format) {
        if (StringUtils.isEmpty(format)) {
            format = "yyyy-MM-dd HH:mm:ss.SSS";
        }

        return toString(new Date(), format);
    }

    public static Date getCurrentDate() {
        return new Date();
    }

    public static Date getYesterday() {
        return addDays(new Date(), -1);
    }

    public static String toSimpleDate(Date date) {
        return toString(date, "yyyy-MM-dd");
    }

    public static String toFullDate(Date date) {
        return toString(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String toString(Date date, String format) {
        return (new SimpleDateFormat(format)).format(date);
    }

    public static String getDate(long timeStamp) {
        return getDate(timeStamp, "yyyy-MM-dd HH:mm:ss.SSS");
    }

    public static String getDate(long timeStamp, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date(timeStamp));
    }

    public static Date parseDate(String date) {
        if (date == null) {
            return null;
        } else {
            String[] formats = new String[] { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", "yyyy-MM" };
            SimpleDateFormat format = new SimpleDateFormat();
            int var4 = formats.length;
            int var5 = 0;

            while (var5 < var4) {
                String fmt = formats[var5];
                format.applyPattern(fmt);

                try {
                    return format.parse(date);
                } catch (Exception var9) {
                    ++var5;
                }
            }

            try {
                return new Date(Long.parseLong(date));
            } catch (Exception var8) {
                return null;
            }
        }
    }

    public static Date getDateStart(Date date) {
        if (null == date) {
            date = getCurrentDate();
        }

        return getDateTime(date, 0, 0, 0);
    }

    public static Date getDateEnd(Date date) {
        if (null == date) {
            date = getCurrentDate();
        }

        return getDateTime(date, 23, 59, 59);
    }

    public static Date getMonthStart(Date date) {
        return getMonthStart(date, false);
    }

    public static Date getMonthStart(Date date, boolean reset) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(5, 1);
        if (reset) {
            calendar.set(10, 0);
            calendar.set(12, 0);
            calendar.set(13, 0);
        }

        return calendar.getTime();
    }

    public static Date addSecond(Date date, Integer seconds) {
        return addTime(date, 13, seconds.intValue());
    }

    public static Date addHour(Date date, Integer hours) {
        return addTime(date, 11, hours.intValue());
    }

    public static Date addDays(Date date, Integer days) {
        return addTime(date, 6, days.intValue());
    }

    public static Date addMonths(Date date, Integer months) {
        return addTime(date, 2, months.intValue());
    }

    public static Date addYears(Date date, Integer years) {
        return addTime(date, 1, years.intValue());
    }

    public static Date addTime(Date date, int field, int value) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field, value);
        return calendar.getTime();
    }

    private static Date getDateTime(Date date, int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(10, hour);
        calendar.set(12, minute);
        calendar.set(13, second);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getMinDate() {
        String strDate = "0001-01-01 00:00:00";
        String strDateFormat = "yyyy-MM-dd HH:mm:ss";
        Date minDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(strDateFormat);

        try {
            minDate = dateFormat.parse(strDate);
        } catch (ParseException var5) {
            var5.printStackTrace();
        }

        return minDate;
    }

    public static boolean isLeapYear(String date) {
        try {
            Date d = toDate(date, "yyyy-MM-dd");
            GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
            gc.setTime(d);
            int year = gc.get(1);
            if (year % 400 == 0) {
                return true;
            } else if (year % 4 == 0) {
                return year % 100 != 0;
            } else {
                return false;
            }
        } catch (Exception var4) {
            return false;
        }
    }

    public static int compareDate(Date date1, Date date2) {
        if (date1.getTime() > date2.getTime()) {
            return 1;
        } else {
            return date1.getTime() < date2.getTime() ? -1 : 0;
        }
    }

    /*
     * 本周第一天0时0分0秒
     */
    public static Date getWeekStartDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    /*
     * 本周最后一天23:59:59
     */
    public static Date getLastWeektDateTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, 7);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }

    /*
     * 获取当天0点0分0秒0毫秒
     */
    public static Date getCurrentDayStartTime() {
        return getDayStartTime(new Date());
    }

    /**
     *
     *  Created on 2019年1月9日
     * <p>Discription:[获取指定日期的0点0分0秒0毫秒]</p>
     * @author:[wangyunliang]
     * @param date
     * @return
     */
    public static Date getDayStartTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /*
     * 当天23:59:59:999秒
     */
    public static Date getDayEndTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        if(date == null) {
            calendar.setTime(new Date());
        }else{
            calendar.setTime(date);
        }
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /*
     * 当前小时的0分0秒
     */
    public static Date getCurrentHourStartTime() {
        return getHourStartTime(new Date());
    }

    public static Date getHourStartTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }


    /**
     * 当前时间省略到小时后的时间戳
     * @return
     */
    public static Long getHourTimesplate(){
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH");
        String dateString = formatter.format(currentTime);
        return  DateUtil.parseDate(dateString,"yyyy-MM-dd HH");
    }

    /*
     * 当前小时的59分59秒
     */
    public static Date getCurrentHourEndTime() {
        return getHourEndTime(new Date());
    }

    public static Date getHourEndTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public static Date getCurrentMonthStartTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    public static Date getCurrentMonthEndTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /*
     * 获取本年的第一天
     */
    public static Date getCurrentYearStartTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /*
     * 获取上一年最后一天
     */
    public static Date getCurrentYearEndTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }


    public static Date getIntENdDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(5, -1);
        Date now = null;

        try {
            now = getSimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(getSimpleDateFormat("yyyy-MM-dd").format(cal.getTime()) + " 23:59:59");
        } catch (ParseException var3) {
            var3.printStackTrace();
        }

        return now;
    }

    private static SimpleDateFormat getSimpleDateFormat(String format) {
        return new SimpleDateFormat(format);
    }

    public static String getBeforeTime(int hour, int minute, String formatTimeType) {
        String formatType = "yyyy-MM-dd HH:mm:ss";
        if (!StringUtils.isEmpty(formatTimeType)) {
            formatType = formatTimeType;
        }

        SimpleDateFormat format = new SimpleDateFormat(formatType);
        Calendar calendar = Calendar.getInstance();
        calendar.add(11, hour);
        calendar.add(12, minute);
        return format.format(calendar.getTime());
    }

    /**
     *
     * Created on 2018年7月18日
     * <p>
     * Discription:[是否是同一天]
     * </p>
     *
     * @author:[wangyunliang]
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameDay(Date date1, Date date2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(date2);
        return c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH);
    }

    public static String getWeekOfDate(Date date) {
        String[] weekDays = { "7", "1", "2", "3", "4", "5", "6" };
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }

    /**
     * 根据开始时间和结束时间返回时间段内的时间集合,包括起始和结束时间
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static List<Date> getDatesBetweenTwoDate(Date beginDate, Date endDate) {
        List<Date> lDate = new ArrayList<>();
        // 把开始时间加入集合
        lDate.add(beginDate);
        Calendar cal = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        cal.setTime(beginDate);
        boolean bContinue = true;
        while (bContinue) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cal.add(Calendar.DAY_OF_MONTH, 1);
            // 测试此日期是否在指定日期之后
            if (endDate.after(cal.getTime())) {
                lDate.add(cal.getTime());
            } else {
                break;
            }
        }
        // 把结束时间加入集合
        lDate.add(endDate);
        return lDate;
    }
    /**
     *  Created on 2019年3月20日
     * <p>Discription:[计算两个日期之间的日期相差天数]</p>
     * @author:[raoyijun]
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    public static Long getDayNumBetweenTwoDays(Date startDate,Date endDate) throws ParseException {
        if(null == startDate) {
            startDate = new Date();
        }
        if(null == endDate) {
            endDate = new Date();
        }
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        startDate=sdf.parse(sdf.format(startDate));
        endDate=sdf.parse(sdf.format(endDate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(endDate);
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);
        return between_days;
    }

    public static Long getDayBetweenDates(Date startDate, Date endDate) {
        try {
            return getDayNumBetweenTwoDays(startDate, endDate);
        } catch (ParseException e) {
            log.error("getDayBetweenDates exception!", e);
        }
        return 0L;
    }

    public static void main(String[] args) throws NumberFormatException, ParseException {
        // Date week = getCurrentDayEndTime();
        //Date date = getCurrentYearEndTime();
        //System.out.println(date);
        // Date currentTime = DateUtil.toDate(DateUtil.getCurrentDate(DateUtil.TIME_SIMPLE_FORMAT), DateUtil.TIME_SIMPLE_FORMAT);
        // Date captureStartTime = DateUtil.toDate("9:12:12", DateUtil.TIME_SIMPLE_FORMAT);
        // Date captureEndTime = DateUtil.toDate("23:12:12", DateUtil.TIME_SIMPLE_FORMAT);
        //
        // System.out.println(currentTime);
        // System.out.println(captureStartTime);
        // System.out.println(captureEndTime);
        //
        // System.out.println(DateUtil.compareDate(currentTime,captureStartTime));
        // System.out.println(DateUtil.compareDate(currentTime,captureEndTime));
        // Date beginDate = DateUtil.toDate("2018-05-30", DateUtil.DATE_SIMPLE_FORMAT);
        // Date endDate = DateUtil.toDate("2018-06-05", DateUtil.DATE_SIMPLE_FORMAT);
        // List<Date> s = DateUtil.getDatesBetweenTwoDate(beginDate, endDate);

//        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.YEAR, 3);
//        //validEndTime = cal.getTime();
//        cal.set(10, 23);
//        cal.set(12, 59);
//        cal.set(13, 59);
//        System.out.println(cal.getTime().getTime());
        System.out.println(DateUtil.getDayNumBetweenTwoDays(new Date(Long.valueOf("1552952681000")), new Date()));
    }

}

