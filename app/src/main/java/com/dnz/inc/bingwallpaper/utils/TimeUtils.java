package com.dnz.inc.bingwallpaper.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {

    public static String PATTERN_JS = "yyyyMMdd";
    public static String PATTERN_DISPLAY = "yyyy-MMM-dd";

    // Converts default date to unix time
    public static long getUnixTime(String pattern, String time) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = simpleDateFormat.parse(time);

        return date.getTime();
    }

    public static String getDisplayDate(String pattern, long unixTime){
        Date date = new Date(unixTime);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.getDefault());

        return simpleDateFormat.format(date);
    }
}
