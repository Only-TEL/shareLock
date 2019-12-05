package com.xihua.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    public static String YYYY = "yyyy";
    public static String YYYY_MM = "yyyy-MM";
    public static String YYYY_MM_DD = "yyyy-MM-dd";
    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    private static String[] parsePatterns = new String[]{"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM", "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    public DateUtils() {
    }

    public static Date getNowDate() {
        return new Date();
    }

    public static String getDate() {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime() {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow() {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(String format) {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(Date date) {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(String format, Date date) {
        return (new SimpleDateFormat(format)).format(date);
    }

    public static final Date dateTime(String format, String ts) {
        try {
            return (new SimpleDateFormat(format)).parse(ts);
        } catch (ParseException var3) {
            throw new RuntimeException(var3);
        }
    }

    public static final String datePath() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    public static final String dateTime() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        } else {
            try {
                return DateUtils.parseDate(str.toString(), parsePatterns);
            } catch (ParseException var2) {
                return null;
            }
        }
    }

    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    public static String getDatePoor(Date endDate, Date nowDate) {
        long nd = 86400000L;
        long nh = 3600000L;
        long nm = 60000L;
        long diff = endDate.getTime() - nowDate.getTime();
        long day = diff / nd;
        long hour = diff % nd / nh;
        long min = diff % nd % nh / nm;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    public static long getHalfHour(Date endDate, Date nowDate) {
        long nd = 86400000L;
        long nh = 3600000L;
        long nm = 60000L;
        long diff = endDate.getTime() - nowDate.getTime();
        long day = diff / nd;
        long hour = diff % nd / nh;
        long min = diff % nd % nh / nm >= 30 ? 1 : 0;
        // 多少个半小时
        return day * 24 * 2 + hour * 2 + min;
    }
}