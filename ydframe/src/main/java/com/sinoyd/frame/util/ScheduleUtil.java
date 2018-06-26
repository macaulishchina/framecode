package com.sinoyd.frame.util;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * 作者： 王一凡
 * 创建时间： 2017/9/21
 * 版权： 江苏远大信息股份有限公司
 * 描述： 日期时间选择工具
 */
public class ScheduleUtil {

    public interface ChooseDate{
        public void onChooseDate();
    }

    /**
     * 选择日期
     *
     * @param con
     * @param v
     * @param date
     */
    public static void chooseDate(Context con, final View v, Date date, final ChooseDate callback) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        DatePickerDialog dialog = new DatePickerDialog(con, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                ++monthOfYear;
                v.setTag((year + "-" + (monthOfYear < 10 ? ("0" + monthOfYear) : monthOfYear) + "-" + (dayOfMonth < 10 ? ("0" + dayOfMonth) : dayOfMonth)));
                ((TextView) v).setText(year + "-" + (monthOfYear < 10 ? ("0" + monthOfYear) : monthOfYear) + "-" + (dayOfMonth < 10 ? ("0" + dayOfMonth) : dayOfMonth));
                if(callback!=null) {
                    callback.onChooseDate();
                }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.setTitle("选择日期");
        dialog.show();
    }

    /**
     * 选择日期和时间
     *
     * @param con
     * @param views
     * @param date
     */
    public static void chooseDateAndTime(final Context con, final View[] views, Date date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        DatePickerDialog dialog = new DatePickerDialog(con, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                final Calendar cal = Calendar.getInstance();
                cal.set(year, monthOfYear, dayOfMonth);
                TimePickerDialog timePick = new TimePickerDialog(con, new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        cal.set(Calendar.MINUTE, minute);
                        String chooseDate = DateUtil.convertDate(cal.getTime(), "yyyy-MM-dd HH:mm");
                        for (View tv : views) {
                            ((TextView) tv).setText(chooseDate);
                        }
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                timePick.setTitle("选择时间");
                timePick.show();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.setTitle("选择日期");
        dialog.show();

    }

    /**
     * 选择日期和时间
     *
     * @param con
     * @param views
     * @param date
     */
    public static void chooseDateByFormat(final Context con, final View[] views, Date date, final String format, final ChooseDate callback) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        DatePickerDialog dialog = new DatePickerDialog(con, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                final Calendar cal = Calendar.getInstance();
                cal.set(year, monthOfYear, dayOfMonth);
                String chooseDate = DateUtil.convertDate(cal.getTime(), format);
                for (View tv : views) {
                    ((TextView) tv).setText(chooseDate);
                }
                if(callback!=null) {
                    callback.onChooseDate();
                }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.setTitle("选择日期");
        dialog.show();

    }

    /**
     * 比较2个日期大小
     *
     * @param date1
     * @param date2
     * @return 大于0 date1晚于date2
     */
    public static int formatDateTime(String date1, String date2) {
        int lag = 0;
        long num1 = 0;
        long num2 = 0;
        try {
            num1 = Long.parseLong(date1.replaceAll("-", "").replace(":", "").replace(" ", ""));
            num2 = Long.parseLong(date2.replaceAll("-", "").replace(":", "").replace(" ", ""));
            lag = (num1 - num2) > 0 ? 1 : -1;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return lag;
    }
}
