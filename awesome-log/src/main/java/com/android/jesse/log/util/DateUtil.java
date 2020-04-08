package com.android.jesse.log.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    private static SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static long getCurrentTime() {
        long currentTime = System.currentTimeMillis();
        long tempTime = 0;
        try {
            String dataStr = sDateFormat.format(new Date(currentTime));
            tempTime = sDateFormat.parse(dataStr).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tempTime;
    }

    public static String getCurrentDate() {
        long currentTime = System.currentTimeMillis();
        return sDateFormat.format(new Date(currentTime));
    }

    public static String getDateStr(long time) {
        return sDateFormat.format(new Date(time));
    }

    public static long getDateTime(String date){
        long tempTime = 0;
        try {
            tempTime = sDateFormat.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return tempTime;
    }

}
