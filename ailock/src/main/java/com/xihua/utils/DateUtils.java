package com.xihua.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalUnit;
import java.util.Calendar;
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


    public static LocalDateTime date2LocalDateTime(Date date, ZoneId zoneId) {
        return LocalDateTime.ofInstant(date.toInstant(), zoneId);
    }

    public static LocalDateTime date2LocalDateTime(Date date) {
        ZoneId zoneId = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(date.toInstant(), zoneId);
    }

    public static Duration calculateDuration(Date startDate, Date endDate) {
        return Duration.between(date2LocalDateTime(startDate), date2LocalDateTime(endDate));
    }

    public static String getDatePoor(Date startDate, Date endDate) {
        Duration duration = calculateDuration(startDate, endDate);
        StringBuilder builder = new StringBuilder();
        long days = duration.toDays();
        if (days > 0) {
            builder.append(days + "天");
        }
        long hours = duration.minusDays(days).toHours();
        if (hours > 0) {
            builder.append(hours + "小时");
        }
        long minutes = duration.minusDays(days).minusHours(hours).toMinutes();
        if (minutes > 0) {
            builder.append(minutes + "分钟");
        }
        long senconds = duration.minusDays(days).minusHours(hours).minusMinutes(minutes).getSeconds();
        if (senconds > 0) {
            builder.append(senconds + "秒");
        }
        return builder.toString();
    }


    /**
     * 计算多少个半小时
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static long getHalfHour(Date startDate, Date endDate) {
        Duration duration = calculateDuration(startDate, endDate);
        long hours = duration.toHours();
        long minutes = duration.minusHours(hours).toMinutes();
        long result = 0;
        if (minutes >= 30) {
            result += 1;
        }
        return result + hours * 2;
    }


}