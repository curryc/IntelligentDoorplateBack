package com.scu.intelligentdoorplateback.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


public class TimeUtils {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public static String toDateString(LocalDateTime time){
        return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    public static LocalDateTime toLocalDateTime(String dateStr) {
        try {
            SimpleDateFormat sdf_us = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);

            String str = sdf.format(sdf.parse(dateStr));
            LocalDateTime localDate=LocalDateTime.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            System.out.println(localDate);
            return localDate;
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

}
