/*
 * Copyright (c) 2016.
 * kupats(sz)
 * www.kuparts.com.
 * Created By chenbin on 16-6-27 上午11:05.
 */

package com.kp.dubbotcc.commons.utils;

import java.text.ParseException;
import java.time.Clock;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

/**
 * 日期操作类
 */
public class DateUtils {

    /**
     * 要用到的DATE Format的定义
     */
    public static String DATE_FORMAT_DATEONLY = "yyyy-MM-dd"; // 年/月/日
    public static String DATE_FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss"; // 年/月/日
    public static String SHORTDATEFORMAT = "yyyyMMdd";
    public static String HMS_FORMAT = "HH:mm:ss";

    /**
     * 把字符串转成日期类型
     * 输入的日期格式:yyyy-MM-dd HH:mm:ss
     *
     * @param str 日期字符串
     * @return 转换后的日期
     * @see LocalDateTime
     */
    public static LocalDateTime parseLocalDateTime(String str) {
        return LocalDateTime.parse(str, DateTimeFormatter.ofPattern(DATE_FORMAT_DATETIME));
    }

    /**
     * 获得当前的日期毫秒
     *
     * @return 当前毫秒数
     */
    public static long nowTimeMillis() {
        return Clock.systemDefaultZone().millis();
    }

    /**
     * 获取从1970年到现在的秒数
     *
     * @return 秒数
     */
    public static long nowEpochSecond() {
        return Clock.systemDefaultZone().instant().getEpochSecond();
    }

    /**
     * 获得当前的时间戳
     *
     * @return 时间点
     */
    public static Instant nowTimestamp() {
        return Instant.now(Clock.systemDefaultZone());
    }

    /**
     * yyyy-MM-dd 当前日期
     *
     * @return 当前日期 yyyy-MM-dd
     */
    public static String getCurrentDate() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT_DATEONLY));
    }

    /**
     * 获取当前日期时间 yyyy-MM-dd HH:mm:ss
     *
     * @return 获取当前日期时间 yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrentDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT_DATETIME));
    }

    /**
     * 获取当前时间 HH:mm:ss
     *
     * @return 获取当前时间 HH:mm:ss
     */
    public static String getCurrentTime() {
        return LocalTime.now().format(DateTimeFormatter.ofPattern(HMS_FORMAT));
    }

    /**
     * yyyy-MM-dd 格式化传入日期
     *
     * @param date 日期
     * @return yyyy-MM-dd 日期
     */
    public static String formaterDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(DATE_FORMAT_DATEONLY));
    }

    /**
     * yyyyMMdd 格式化传入日期
     *
     * @param date 传入的日期
     * @return yyyyMMdd 字符串
     */
    public static String formaterDateToyyyyMMdd(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(SHORTDATEFORMAT));
    }

    /**
     * 将localDateTime 格式化成yyyy-MM-dd HH:mm:ss
     *
     * @param dateTime 时间
     * @return 字符时间
     */
    public static String formaterLocalDateTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern(DATE_FORMAT_DATETIME));
    }


    /**
     * yyyy-MM-dd HH:mm:ss
     * 时间点转换成日期字符串
     *
     * @param instant 时间点.
     * @return 日期时间 yyyy-MM-dd HH:mm:ss
     */
    public static String parseInstantToDataStr(Instant instant) {
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern(DATE_FORMAT_DATETIME));
    }

    /**
     * 得到时间戳格式字串
     *
     * @param date 长日期
     * @return UTC 格式的时间戳字符串
     */
    public static String getTimeStampStr(LocalDateTime date) {
        return date.toInstant(ZoneOffset.UTC).toString();
    }

    /**
     * 计算 second 秒后的时间
     *
     * @param date   长日期
     * @param second 需要增加的秒数
     * @return 增加后的日期
     */
    public static LocalDateTime addSecond(LocalDateTime date, int second) {
        return date.plusSeconds(second);
    }

    /**
     * 计算 minute 分钟后的时间
     *
     * @param date   长日期
     * @param minute 需要增加的分钟数
     * @return 增加后的日期
     */
    public static LocalDateTime addMinute(LocalDateTime date, int minute) {
        return date.plusMinutes(minute);
    }

    /**
     * 计算 hour 小时后的时间
     *
     * @param date 长日期
     * @param hour 增加的小时数
     * @return 增加后的日期
     */
    public static LocalDateTime addHour(LocalDateTime date, int hour) {
        return date.plusHours(hour);
    }

    /**
     * 计算 day 天后的时间
     *
     * @param date 长日期
     * @param day  增加的天数
     * @return 增加后的日期
     */
    public static LocalDateTime addDay(LocalDateTime date, int day) {
        return date.plusDays(day);
    }

    /**
     * 计算 month 月后的时间
     *
     * @param date  长日期
     * @param month 需要增加的月数
     * @return 增加后的日期
     */
    public static LocalDateTime addMoth(LocalDateTime date, int month) {
        return date.plusMonths(month);
    }

    /**
     * 计算 year 年后的时间
     *
     * @param date 长日期
     * @param year 需要增加的年数
     * @return 增加后的日期
     */
    public static LocalDateTime addYear(LocalDateTime date, int year) {
        return date.plusYears(year);
    }

    /**
     * 得到day的起始时间点。
     * 一天开始的时间为　0:0:0
     *
     * @param date 短日期
     * @return yyyy-MM-dd HH:mm:ss 字符串
     */
    public static String getDayStart(LocalDate date) {
        LocalDateTime localDateTime = LocalDateTime.of(date, LocalTime.of(0, 0, 0));
        return localDateTime.format(DateTimeFormatter.ofPattern(DATE_FORMAT_DATETIME));
    }

    /**
     * 得到day的结束时间点。
     * 一天结束的时间为 23:59:59
     *
     * @param date date 短日期
     * @return yyyy-MM-dd HH:mm:ss 字符串
     */
    public static String getDayEnd(LocalDate date) {
        LocalDateTime localDateTime = LocalDateTime.of(date, LocalTime.of(23, 59, 59));
        return localDateTime.format(DateTimeFormatter.ofPattern(DATE_FORMAT_DATETIME));
    }

    /**
     * 根据日期字符串获取时间戳
     *
     * @param dateStr yyyy-MM-dd HH:mm:ss
     * @return 时间信息
     */
    public static Instant parseDataStrToInstant(String dateStr) {
        return parseLocalDateTime(dateStr).toInstant(ZoneOffset.UTC);
    }

    /**
     * 取得两个日期之间相差的日数
     *
     * @param t1 开始日期
     * @param t2 结束日期
     * @return t1到t2间的日数，如果t2 在 t1之后，返回正数，否则返回负数
     */
    public static long getDaysBetween(LocalDate t1, LocalDate t2) {
        return t1.until(t2, ChronoUnit.DAYS);
    }

    /**
     * 取得两个日期之间相差的月数
     *
     * @param t1 开始日期
     * @param t2 结束日期
     * @return t1到t2间的日数，如果t2 在 t1之后，返回正数，否则返回负数
     */
    public static long getMonthsBetween(LocalDate t1, LocalDate t2) {
        return t1.until(t2, ChronoUnit.MONTHS);
    }

    /**
     * 取得两个日期之间相差的小时数
     *
     * @param t1 开始长日期
     * @param t2 结束长日期
     * @return t1到t2间的日数，如果t2 在 t1之后，返回正数，否则返回负数
     */
    public static long getHoursBetween(LocalDateTime t1, LocalDateTime t2) {
        return t1.until(t2, ChronoUnit.HOURS);
    }

    /**
     * 取得两个日期之间相差的秒数
     *
     * @param t1 开始长日期
     * @param t2 结束长日期
     * @return t1到t2间的日数，如果t2 在 t1之后，返回正数，否则返回负数
     */
    public static long getSecondsBetween(LocalDateTime t1, LocalDateTime t2) {
        return t1.until(t2, ChronoUnit.SECONDS);
    }

    /**
     * 取得两个日期之间相差的分钟
     *
     * @param t1 开始长日期
     * @param t2 结束长日期
     * @return t1到t2间的日数，如果t2 在 t1之后，返回正数，否则返回负数
     */
    public static long getMinutesBetween(LocalDateTime t1, LocalDateTime t2) {
        return t1.until(t2, ChronoUnit.MINUTES);
    }


    /**
     * 获取今天是星期几
     *
     * @return 返回今天是周几
     * @see DayOfWeek
     */
    public static DayOfWeek getWeek() {
        return LocalDate.now().getDayOfWeek();
    }

    /**
     * 判断时间是否在制定的时间段之类
     *
     * @param date  需要判断的时间
     * @param start 时间段的起始时间
     * @param end   时间段的截止时间
     * @return true or false
     */
    public static boolean isBetween(LocalDateTime date, LocalDateTime start, LocalDateTime end) {
        if (date == null || start == null || end == null) {
            throw new IllegalArgumentException("日期不能为空");
        }
        return date.isAfter(start) && date.isBefore(end);
    }

    /**
     * 得到传入日期,周起始时间
     * 这个方法定义:周一为一个星期开始的第一天
     *
     * @param date 日期
     * @return 返回一周的第一天
     */
    public static LocalDate getWeekStart(LocalDate date) {
        return date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    /**
     * 得到当前周截止时间
     * 这个方法定义:周日为一个星期开始的最后一天
     *
     * @param date 日期
     * @return 返回一周的最后一天
     */
    public static LocalDate getWeekEnd(LocalDate date) {
        return date.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
    }

    /**
     * 得到month的终止时间点.
     *
     * @param date 日期
     * @return 传入的日期当月的结束日期
     */
    public static LocalDate getMonthEnd(LocalDate date) {
        return date.with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * 得到当月起始时间
     *
     * @param date 日期
     * @return 传入的日期当月的开始日期
     */
    public static LocalDate getMonthStart(LocalDate date) {

        return date.with(TemporalAdjusters.firstDayOfMonth());
    }

    /**
     * 得到当前年起始时间
     *
     * @param date 日期
     * @return 传入的日期当年的开始日期
     */
    public static LocalDate getYearStart(LocalDate date) {
        return date.with(TemporalAdjusters.firstDayOfYear());
    }

    /**
     * 得到当前年最后一天
     *
     * @param date 日期
     * @return 传入的日期当年的结束日期
     */
    public static LocalDate getYearEnd(LocalDate date) {
        return date.with(TemporalAdjusters.lastDayOfYear());
    }

    /**
     * 取得季度第一天
     *
     * @param date 日期
     * @return 传入的日期当季的开始日期
     */
    public static LocalDate getSeasonStart(LocalDate date) {
        return getSeasonDate(date)[0];
    }

    /**
     * 取得季度最后一天
     *
     * @param date 日期
     * @return 传入的日期当季的结束日期
     */
    public static LocalDate getSeasonEnd(LocalDate date) {
        return getSeasonDate(date)[2].with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * 取得季度月的第一天
     *
     * @param date 日期
     * @return 返回一个当前季度月的数组
     */
    public static LocalDate[] getSeasonDate(LocalDate date) {
        LocalDate[] season = new LocalDate[3];
        int nSeason = getSeason(date);
        int year = date.getYear();
        if (nSeason == 1) {// 第一季度
            season[0] = LocalDate.of(year, Month.JANUARY, 1);
            season[1] = LocalDate.of(year, Month.FEBRUARY, 1);
            season[2] = LocalDate.of(year, Month.MARCH, 1);
        } else if (nSeason == 2) {// 第二季度
            season[0] = LocalDate.of(year, Month.APRIL, 1);
            season[1] = LocalDate.of(year, Month.MAY, 1);
            season[2] = LocalDate.of(year, Month.JUNE, 1);
        } else if (nSeason == 3) {// 第三季度
            season[0] = LocalDate.of(year, Month.JULY, 1);
            season[1] = LocalDate.of(year, Month.AUGUST, 1);
            season[2] = LocalDate.of(year, Month.SEPTEMBER, 1);
        } else if (nSeason == 4) {// 第四季度
            season[0] = LocalDate.of(year, Month.OCTOBER, 1);
            season[1] = LocalDate.of(year, Month.NOVEMBER, 1);
            season[2] = LocalDate.of(year, Month.DECEMBER, 1);
        }
        return season;
    }

    /**
     * 判断当前期为每几个季度
     *
     * @param date 日期
     * @return 1 第一季度 2 第二季度 3 第三季度 4 第四季度
     */
    public static int getSeason(LocalDate date) {

        int season = 0;

        Month month = date.getMonth();

        switch (month) {
            case JANUARY:
            case FEBRUARY:
            case MARCH:
                season = 1;
                break;
            case APRIL:
            case MAY:
            case JUNE:
                season = 2;
                break;
            case JULY:
            case AUGUST:
            case SEPTEMBER:
                season = 3;
                break;
            case OCTOBER:
            case NOVEMBER:
            case DECEMBER:
                season = 4;
                break;
            default:
                break;
        }
        return season;
    }

    /**
     * 获取当前时间的前多少,yyyy-MM-dd
     *
     * @param days 天数
     * @return yyyy-MM-dd
     */
    public static String subDays(int days) {
        return LocalDate.now().minusDays(days).format(DateTimeFormatter.ofPattern(DATE_FORMAT_DATEONLY));
    }

    /**
     * 判断开始时间和结束时间，是否超出了当前时间的一定的间隔数限制 如：开始时间和结束时间，不能超出距离当前时间90天
     *
     * @param startDate 开始时间
     * @param endDate   结束时间按
     * @param interval  间隔数
     * @return true or false
     */
    public static boolean isOverIntervalLimit(LocalDate startDate, LocalDate endDate, int interval) {
        return getDaysBetween(startDate, endDate) >= interval;
    }


    /**
     * 获取昨天的日期 格式串:yyyy-MM-dd
     *
     * @return yyyy-MM-dd
     */
    public static String getYesterday() {
        return getYesterday(LocalDate.now());
    }

    /**
     * 获取昨天的日期 格式串:yyyy-MM-dd
     *
     * @return yyyy-MM-dd
     */
    public static String getYesterday(LocalDate date) {
        return date.minusDays(1).format(DateTimeFormatter.ofPattern(DATE_FORMAT_DATEONLY));
    }

    /**
     * 上月第一天
     *
     * @return 日期
     */
    public static LocalDate getPreviousMonthFirstDay() {

        return LocalDate.now().minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
    }

    /**
     * 上月最后一天
     *
     * @return 日期
     */
    public static LocalDate getPreviousMonthLastDay() {
        return LocalDate.now().minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
    }

}
