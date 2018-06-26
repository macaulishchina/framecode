package com.sinoyd.frame.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 作者： 王一凡
 * 创建时间： 2017/9/5
 * 版权： 江苏远大信息股份有限公司
 * 描述：日期工具类
 */
public class DateUtil {
    public static String DateFormat_1 = "yyyy-MM-dd hh:mm:ss";
    public static String DateFormat_24 = "yyyy-MM-dd HH:mm:ss";

    public DateUtil() {
    }

    public static String switchDay(int day) {
        String daystr = String.valueOf(day);
        return daystr.length() == 2?daystr:"0" + daystr;
    }

    /**
     * String格式日期转为String格式日期
     * @param date
     * @param oldPattern
     * @param newPattern
     * @return
     */
    public static String dateStringPattern(String date, String oldPattern, String newPattern) {
        if (date == null || oldPattern == null || newPattern == null) {
            return "";
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat(oldPattern);
        SimpleDateFormat sdf2 = new SimpleDateFormat(newPattern);
        Date d = null;
        try {
            d = sdf1.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sdf2.format(d);
    }

    /**
     * 获取时间差
     * @param endDate
     * @param nowDate
     * @return
     */
    public static String getDatePoor(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        // long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        //long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        String datePoor = "";
        if(day < 0){
            datePoor = "超"+ Math.abs(day) + "天" + Math.abs(hour) + "小时";
        }else if(day == 0){
            if(hour < 0){
                datePoor = "超" + Math.abs(hour) + "小时";
            } else {
                datePoor = "今日到期 余" + Math.abs(hour) + "小时";
            }
        }else if(day >0){
            datePoor = "余"+ Math.abs(day) + "天" + Math.abs(hour) + "小时";
        }
        return datePoor;
    }

    public static String convertDate(Date date, String format) {
        if(date != null) {
            SimpleDateFormat format1 = new SimpleDateFormat(format);
            String s = format1.format(date);
            return s;
        } else {
            return "";
        }
    }

    public static String getYesterdayDate(){
        Date date=new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, -1);//把日期往后增加一天.整数往后推,负数往前移动
        date=calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);
        return dateString;
    }

    public static String getTodayDate(){
        return convertDate(new Date(), "yyyy-MM-dd");
    }

    public static String getCurrentDateByFormat(String format){
        return convertDate(new Date(), format);
    }

    public static String getCurrentTimeYM() {
        return convertDate(new Date(), "yyyy-MM");
    }

    public static String getCurrentTime() {
        return convertDate(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    public static String getCurrentTimeHM() {
        return convertDate(new Date(), "HH:mm");
    }

    public static String getWeekNameByNum(int num) {
        String name = null;
        switch(num) {
            case 1:
                name = "日";
                break;
            case 2:
                name = "一";
                break;
            case 3:
                name = "二";
                break;
            case 4:
                name = "三";
                break;
            case 5:
                name = "四";
                break;
            case 6:
                name = "五";
                break;
            case 7:
                name = "六";
                break;
            default:
                name = "";
        }

        return name;
    }

    public static String getWeekNameByDate(Date d) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(d);
        String name = null;
        switch(ca.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                name = "日";
                break;
            case 2:
                name = "一";
                break;
            case 3:
                name = "二";
                break;
            case 4:
                name = "三";
                break;
            case 5:
                name = "四";
                break;
            case 6:
                name = "五";
                break;
            case 7:
                name = "六";
                break;
            default:
                name = "";
        }

        return name;
    }

    public static String Num2Haizi_Week(int day) {
        switch(day) {
            case 0:
                return "星期日";
            case 1:
                return "星期一";
            case 2:
                return "星期二";
            case 3:
                return "星期三";
            case 4:
                return "星期四";
            case 5:
                return "星期五";
            case 6:
                return "星期六";
            default:
                return "";
        }
    }

    public static String Num2Haizi_Week_HTML_Color(int day) {
        switch(day) {
            case 0:
                return "<font color=red>星期日</font>";
            case 1:
                return "星期一";
            case 2:
                return "星期二";
            case 3:
                return "星期三";
            case 4:
                return "星期四";
            case 5:
                return "星期五";
            case 6:
                return "<font color=red>星期六</font>";
            default:
                return "";
        }
    }

    public static Date convertString2Date(String str, String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        Date date = null;

        try {
            date = format.parse(str);
        } catch (ParseException var5) {
            var5.printStackTrace();
        }

        return date;
    }

    public static String getWeekByDate(Date date) {
        Calendar time = Calendar.getInstance();
        time.clear();
        time.setTime(date);
        int week = time.get(Calendar.DAY_OF_WEEK) - 1;
        return Num2Haizi_Week(week);
    }

    public static String getWeekByDateSingleChar(Date date) {
        Calendar time = Calendar.getInstance();
        time.clear();
        time.setTime(date);
        int week = time.get(Calendar.DAY_OF_WEEK) - 1;
        return getWeekNameByNum(week);
    }

    public static int getWeekByDateTime(Date date) {
        Calendar time = Calendar.getInstance();
        time.setTime(date);
        return time.get(Calendar.DAY_OF_WEEK) - 1;
    }

    public static String getWeekByDateStr(String dateStr) {
        Calendar time = Calendar.getInstance();
        time.clear();
        time.setTime(convertString2Date(dateStr, "yyyy-MM-dd"));
        int week = time.get(Calendar.DAY_OF_WEEK) - 1;
        return Num2Haizi_Week(week);
    }

    public static String getWeekByDate_HTML_Color(Date date) {
        Calendar time = Calendar.getInstance();
        time.clear();
        time.setTime(date);
        int week = time.get(Calendar.DAY_OF_WEEK) - 1;
        return Num2Haizi_Week_HTML_Color(week);
    }

    public static String getWeekByFormatedDateStr(String s) {
        Calendar time = Calendar.getInstance();
        String[] ss = s.split("-");
        time.set(Integer.parseInt(ss[0]), Integer.parseInt(ss[1]) - 1, Integer.parseInt(ss[2]));
        return getWeekByDate(time.getTime());
    }

    public static String getWeekByFormatedDateStr_HTML_Color(String s) {
        Calendar time = Calendar.getInstance();
        String[] ss = s.split("-");
        time.set(Integer.parseInt(ss[0]), Integer.parseInt(ss[1]) - 1, Integer.parseInt(ss[2]));
        return getWeekByDate_HTML_Color(time.getTime());
    }

    public static String AddZero(int i) {
        return i >= 0 && i <= 9?"0" + i:String.valueOf(i);
    }

    public static String getFormatedDate(String strs, String tag) {
        String[] ss = strs.split(tag);
        String year = ss[0];
        String month = switchDay(Integer.parseInt(ss[1]));
        String day = switchDay(Integer.parseInt(ss[2]));
        return year + "-" + month + "-" + day;
    }

    public static String getTimeStrHanzi() {
        return convertDate(new Date(), "yyyy/MM/dd HH:mm:ss");
    }

    public static String getListUpdateStr() {
        return (new SimpleDateFormat("MM-dd HH:mm")).format(new Date(System.currentTimeMillis()));
    }

    public static long timeLag(String time1, String time2) {
        return (long)time1.compareTo(time2);
    }

    public static String getTimeByDateTimeStr(String datetime) {
        String timeStr = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        SimpleDateFormat sdftime = new SimpleDateFormat("HH:mm");

        try {
            Date e = sdf.parse(datetime);
            timeStr = sdftime.format(e);
        } catch (ParseException var5) {
            var5.printStackTrace();
        }

        return timeStr;
    }

    public static String getDateByDateTime(String datetime) {
        String timeStr = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        SimpleDateFormat sdftime = new SimpleDateFormat("yyyy/MM/dd");

        try {
            Date e = sdf.parse(datetime);
            timeStr = sdftime.format(e);
        } catch (ParseException var5) {
            var5.printStackTrace();
        }

        return timeStr;
    }
}
