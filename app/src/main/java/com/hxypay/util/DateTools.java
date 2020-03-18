package com.hxypay.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateTools {


    /**
     * 当前时间
     * @return String
     */
    public static String getDateToday() {
        String dayStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        return dayStr;
    }


    /**
     * 当前时间
     * @return String
     */
    public static String getDateToday1() {
        String dayStr = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        return dayStr;
    }

    /**
     * 当前时间
     * @return String
     */
    public static String getDateTodayYMD() {
        String dayStr = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
        return dayStr;
    }
    /**
     * 回退天数
     */
    public static String backDate(int day) {
        Date date = new Date();// 取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, day);// 把日期往后增加一天.整数往后推,负数往前移动
        date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);
        return dateString;

    }



//    private static String[] parsePatterns = {
//            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
//            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
//            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};
//
//    /**
//     * 得到当前日期字符串 格式（yyyy-MM-dd）
//     */
//    public static String getDate() {
//        return getDate("yyyy-MM-dd");
//    }
//
//    /**
//     * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
//     */
//    public static String getDate(String pattern) {
//        return DateFormatUtils.format(new Date(), pattern);
//    }
//
//    /**
//     * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
//     */
//    public static String formatDate(Date date, Object... pattern) {
//        String formatDate = null;
//        if (pattern != null && pattern.length > 0) {
//            formatDate = DateFormatUtils.format(date, pattern[0].toString());
//        } else {
//            formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
//        }
//        return formatDate;
//    }
//
//    /**
//     * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
//     */
//    public static String formatDateTime(Date date) {
//        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
//    }
//
//    /**
//     * 得到当前时间字符串 格式（HH:mm:ss）
//     */
//    public static String getTime() {
//        return formatDate(new Date(), "HH:mm:ss");
//    }
//
//    /**
//     * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
//     */
//    public static String getDateTime() {
//        return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
//    }
//
//    /**
//     * 得到当前年份字符串 格式（yyyy）
//     */
//    public static String getYear() {
//        return formatDate(new Date(), "yyyy");
//    }
//
//    /**
//     * 得到当前月份字符串 格式（MM）
//     */
//    public static String getMonth() {
//        return formatDate(new Date(), "MM");
//    }
//
//    /**
//     * 得到当天字符串 格式（dd）
//     */
//    public static String getDay() {
//        return formatDate(new Date(), "dd");
//    }
//
//    /**
//     * 得到当前星期字符串 格式（E）星期几
//     */
//    public static String getWeek() {
//        return formatDate(new Date(), "E");
//    }
//
//    /**
//     * 日期型字符串转化为日期 格式
//     * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
//     * "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm",
//     * "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
//     */
//    public static Date parseDate(Object str) {
//        if (str == null) {
//            return null;
//        }
//        try {
//            return parseDate(str.toString(), parsePatterns);
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    /**
//     * 获取过去的天数
//     *
//     * @param date
//     * @return
//     */
//    public static long pastDays(Date date) {
//        long t = new Date().getTime() - date.getTime();
//        return t / (24 * 60 * 60 * 1000);
//    }
//
//    /**
//     * 获取过去的小时
//     *
//     * @param date
//     * @return
//     */
//    public static long pastHour(Date date) {
//        long t = new Date().getTime() - date.getTime();
//        return t / (60 * 60 * 1000);
//    }
//
//    /**
//     * 获取过去的分钟
//     *
//     * @param date
//     * @return
//     */
//    public static long pastMinutes(Date date) {
//        long t = new Date().getTime() - date.getTime();
//        return t / (60 * 1000);
//    }
//
//    /**
//     * 转换为时间（天,时:分:秒.毫秒）
//     *
//     * @param timeMillis
//     * @return
//     */
//    public static String formatDateTime(long timeMillis) {
//        long day = timeMillis / (24 * 60 * 60 * 1000);
//        long hour = (timeMillis / (60 * 60 * 1000) - day * 24);
//        long min = ((timeMillis / (60 * 1000)) - day * 24 * 60 - hour * 60);
//        long s = (timeMillis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
//        long sss = (timeMillis - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000 - s * 1000);
//        return (day > 0 ? day + "," : "") + hour + ":" + min + ":" + s + "." + sss;
//    }
//
//    /**
//     * 功能：传入时间按所需格式返回时间字符串
//     *
//     * @param date   java.util.Date格式
//     * @param format yyyy-MM-dd HH:mm:ss | yyyy年MM月dd日 HH时mm分ss秒
//     * @return
//     */
//    public static String format(Date date, String format) {
//        String result = "";
//        try {
//            if (date == null) {
//                date = new Date();// 如果时间为空,则默认为当前时间
//            }
//            if (StringUtil.isBlank(format)) {// 默认格式化形式
//                format = "yyyy-MM-dd";
//            }
//            DateFormat df = new SimpleDateFormat(format);
//            result = df.format(date);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    /**
//     * 得到报文时间格式yyyyMMddHHmmss
//     *
//     * @return
//     */
//    public static String getMessageTextTime() {
//        return format(new Date(), "yyyyMMddHHmmss");
//    }
//
//    /**
//     * 计算两个日期之间相差的天数
//     *
//     * @param beginDate 较小的时间
//     * @param endDate   较大的时间
//     * @return 相差天数
//     */
//    public static Integer daysBetween(Date beginDate, Date endDate) {
//        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            beginDate = sdf.parse(sdf.format(beginDate));
//            endDate = sdf.parse(sdf.format(endDate));
//            Calendar cal = Calendar.getInstance();
//            cal.setTime(beginDate);
//            long time1 = cal.getTimeInMillis();
//            cal.setTime(endDate);
//            long time2 = cal.getTimeInMillis();
//            long between_days = (time2 - time1) / (1000 * 3600 * 24);
//            return Integer.parseInt(String.valueOf(between_days));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    /**
//     * 字符串的日期格式的计算
//     *
//     * @param beginDate 较小的时间
//     * @param endDate   较大的时间
//     * @return 相差天数
//     */
//    public static Integer daysBetween(String beginDate, String endDate) {
//        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            Calendar cal = Calendar.getInstance();
//            cal.setTime(sdf.parse(beginDate));
//            long time1 = cal.getTimeInMillis();
//            cal.setTime(sdf.parse(endDate));
//            long time2 = cal.getTimeInMillis();
//            long between_days = (time2 - time1) / (1000 * 3600 * 24);
//
//            return Integer.parseInt(String.valueOf(between_days));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    /**
//     * 字符串的日期格式的计算
//     *
//     * @param beginTime 较小的时间
//     * @param endTime   较大的时间
//     * @return 相差分钟数
//     */
//    public static Integer minuteBetween(String beginTime, String endTime) {
//        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
//            Date begin = sdf.parse(beginTime);
//            Date end = sdf.parse(endTime);
//            Long between = (end.getTime() - begin.getTime()) / 1000;
//            Long min = between / 60;
//            return min.intValue();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public static String getSimpleYearAndMonthAndDay() {
//        String strDate = formatDate(new Date(), "yyyyMMdd");
//        return strDate.substring(2, 8);
//    }
//
//    //获得时分秒毫秒
//    public static String getHourAndMinAndSecAndMil() {
//        String strDate = formatDate(new Date(), "yyyy-MM-dd HH:mm:ss:SSS");
//        return strDate.substring(11).replace(":", "");
//    }
}
