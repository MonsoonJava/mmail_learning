package com.xfj.mmail.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * Created by asus on 2017/7/5.
 */
public class DateTimeUtil {

    public static Date strToDate(String dateTime,String dateFormat){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(dateFormat);
        DateTime time = dateTimeFormatter.parseDateTime(dateTime);
        return time.toDate();
    }

    public static Date strToDate(String dateTime){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime time = dateTimeFormatter.parseDateTime(dateTime);
        return time.toDate();
    }

    public static String dateToStr(Date date,String dateFormat){
        DateTime dateTime = new DateTime(date);
        String dateStr = dateTime.toString(dateFormat);
        return dateStr;
    }

    public static String dateToStr(Date date){
        DateTime dateTime = new DateTime(date);
        String dateStr = dateTime.toString("yyyy-MM-dd HH:mm:ss");
        return dateStr;
    }

    public static void main(String[] args) {
        System.out.println(DateTimeUtil.strToDate("2012-12-12 12:12:12"));
        System.out.println(DateTimeUtil.dateToStr(new Date()));
    }
}
