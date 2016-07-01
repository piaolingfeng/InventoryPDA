package com.pda.birdex.pda.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by huwei on 16/3/28.
 * 对时间格式和转换的操作
 */
public class TimeUtil {
    private static SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");

    public static String calendar2String(Calendar calendar) {
        if (calendar != null) {
            return format1.format(calendar.getTime());
        } else {
            return "";
        }
    }

    public static String date2String(Date date) {
        if (date != null) {
            return format1.format(date);
        } else {
            return "";
        }
    }

    public static String calendar2SimpleString(Calendar calendar) {
        if (calendar != null) {
            return format2.format(calendar.getTime());
        } else {
            return "";
        }
    }


    /**
     * 获取今天时间
     */
    public static String getCurrentData() {
        Date date = new Date();
        String time = format2.format(date);
        return time;
    }

    public static String getWeekdelayData() {
//		推迟一周示例：
        Calendar curr = Calendar.getInstance();
        curr.set(Calendar.DAY_OF_MONTH, curr.get(Calendar.DAY_OF_MONTH) - 7);
        Date date = curr.getTime();
        String time = format2.format(date);
        return time;
    }

    public static String getMonthDelayData() {
//		推迟一个月示例：
        Calendar curr = Calendar.getInstance();
        curr.set(Calendar.MONTH, curr.get(Calendar.MONTH) - 1);
        Date date = curr.getTime();
        String time = format2.format(date);
        return time;
    }

    public static String getThreeMonthDelayData() {
//		推迟一个月示例：
        Calendar curr = Calendar.getInstance();
        curr.set(Calendar.MONTH, curr.get(Calendar.MONTH) - 3);
        Date date = curr.getTime();
        String time = format2.format(date);
        return time;
    }

    public static String getYearDelayData() {
//		推迟一年示例：
        Calendar curr = Calendar.getInstance();
        curr.set(Calendar.YEAR, curr.get(Calendar.YEAR) - 1);
        Date date = curr.getTime();
        String time = format2.format(date);
        return time;
    }
    /*
     *long型转时间字符串
     */
    public static String long2Date(long timenum){
        String time="";
        Date date=new Date(timenum);
        time=format1.format(date);
        return time;
    }

}
