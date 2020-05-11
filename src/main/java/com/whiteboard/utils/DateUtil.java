package com.whiteboard.utils;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * 时间格式化
 */
public class DateUtil {

    public static final String STANDARD = "yyyy-MM-dd HH:mm:ss";

    public static Date string2Date(String strDate){
        if (strDate == null){
            return null;
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDARD);
        return dateTimeFormatter.parseDateTime(strDate).toDate();
    }

    public static Date string2Date(String strDate, String format){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(format);
        return dateTimeFormatter.parseDateTime(strDate).toDate();
    }

    public static String date2String(Date date){
        if (date == null){
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(STANDARD);
    }

    public static String date2String(Date date, String format){
        if (date == null){
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(format);
    }

    public static void main(String[] args) {
        System.out.println(string2Date("2020-05-27 21:25:53"));
    }
}
