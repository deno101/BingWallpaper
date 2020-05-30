package com.dnz.inc.bingwallpaper.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {
    private static final String TAG = "TimeUtils";

    public static String PATTERN_JSON_DB = "yyyyMMdd";
    public static String PATTERN_DISPLAY = "yyyy-MMM-dd";

    public static double ONE_DAY = 24 * 60 * 60 * 1000;


    public static Date getDate(String pattern, String date) {
        Date date1 = null;
        try {
            date1 = new SimpleDateFormat(pattern, Locale.getDefault()).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1;
    }

    public static String getCustomFormat(Date date, String format) {
        return new SimpleDateFormat(format, Locale.getDefault()).format(date);
    }

    public static String forDisplay(Date date) {
        Calendar today = Calendar.getInstance();
        Calendar yesterday = Calendar.getInstance();

        yesterday.add(Calendar.DAY_OF_YEAR, -1);
        String sDate = new SimpleDateFormat(PATTERN_DISPLAY, Locale.getDefault()).format(date);

        if (compareDates(today, date)) {
            sDate = "Today";
        } else if (compareDates(yesterday, date)) {
            sDate = "Yesterday";
        }

        return sDate;
    }

    public static String forDB_JSON_FS(Date date) {
        return new SimpleDateFormat(PATTERN_JSON_DB, Locale.getDefault()).format(date);
    }

    private static boolean compareDates(Calendar calendar, Date date) {
        Calendar calenderDate = Calendar.getInstance();
        calenderDate.setTime(date);

        return calendar.get(Calendar.YEAR) == calenderDate.get(Calendar.YEAR) && calendar.get(Calendar.MONTH) == calenderDate.get(Calendar.MONTH)
                && calendar.get(Calendar.DAY_OF_MONTH) == calenderDate.get(Calendar.DAY_OF_MONTH);
    }

    public static Calendar getTimeAsOfMidnight() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                0,
                0,
                0
        );
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar;
    }
}
