package com.hu.tookit.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * 日期操作类
 * @ClassName: DateUtil
 * @author hlz
 * @date 2017年11月28日 下午2:04:31
 *
 * Copyright (c) 2006-2017.Beijing WenHua Online Sci-Tech Development Co. Ltd
 * All rights reserved.
 */
public class DateUtil {

	private static final String DATE_FORMATE = "yyyy-MM-dd";

	private static final String DATETIME_FORMATE = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 
	 * 得到当前时间，几天前的日期 格式 yyyy-MM-dd
	 * @Title: getDateBefore
	 * @return String
	 * @param day
	 * @return
	 */
	public static String getDateStrBefore(int day) {
         return getDateStrBefore(new Date(), day); 
	}

	/**
	 * 
	 * 得到某个日期之前几天的日期，返回Date
	 * @Title: getDateBefore
	 * @return Date
	 * @param date
	 * @param day
	 * @return
	 */
	public static Date getDateBefore(Date date, int day) {
		Calendar now = Calendar.getInstance();      
        now.setTime(date);      
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.SECOND,0);
        now.set(Calendar.MINUTE,0);
        return now.getTime(); 
	}

	/**
	 * 
	 * 得到当前时间之前几天的日期，返回Date(不包括时分秒)
	 * @Title: getDateBefore
	 * @return Date
	 * @param date
	 * @param day
	 * @return
	 */
	public static Date getDateBefore(int day) {
        return getDateBefore(new Date(), day); 
	}

	/**
	 * 
	 * 得到某个时间，几天前的日期 格式 yyyy-MM-dd
	 * @Title: getDateBefore
	 * @return String
	 * @param day
	 * @return
	 */
	public static String getDateStrBefore(Date date, int day) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMATE);  
        return sdf.format(getDateBefore(date, day)); 
	}

	/**
	 * 
	 * 得到当前日期  Date (忽略时分秒)
	 * @Title: getNowDate
	 * @return Date
	 * @return
	 */
	public static Date getNowDate() {
		Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.SECOND,0);
        now.set(Calendar.MINUTE,0);
        return now.getTime(); 
	}
	
	/**
	 * 
	 * 得到当前时间  DateTime (包含时分秒)
	 * @Title: getNowDateTime
	 * @return Date
	 * @return
	 */
	public static Date getNowDateTime() {
		Calendar now = Calendar.getInstance();
        return now.getTime(); 
	}
	
	/**
	 * 
	 * 得到当前日期  格式 yyyy-MM-dd
	 * @Title: getNowDate
	 * @return String
	 * @return
	 */
	public static String getNowDateStr() {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMATE);
        return sdf.format(getNowDate()); 
	}

	/**
	 * 
	 * 得到当前时间 格式 yyyy-MM-dd HH:mm:ss
	 * @Title: getNowDateTime
	 * @return String
	 * @return
	 */
	public static String getNowDateTimeStr() {
		SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMATE);
        return sdf.format(getNowDateTime()); 
	}

	/**
	 * 
	 * 将日期对象的时间字符串 格式 yyyy-MM-dd HH:mm:ss
	 * @Title: getDateTimeStr
	 * @return String
	 * @return
	 */
	public static String getDateTimeStr(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMATE);
        return sdf.format(date.getTime()); 
	}

	/**
	 * 
	 * 日期字符串转成日期类
	 * @Title: StrToDate
	 * @return Date
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static Date strToDate(String str) throws ParseException {
	   SimpleDateFormat format = new SimpleDateFormat(DATETIME_FORMATE);
	   return format.parse(str);
	}

	/**
	 * 
	 * 得到某一年到今年的相隔时间
	 * @Title: getNumYear
	 * @return int
	 * @param year
	 * @return
	 */
	public static int getNumYear(int year) {
		int nowYear = Calendar.getInstance().get(Calendar.YEAR);
		return nowYear-year;
	}

	/**
	 * 
	 * 秒转成时间格式的时间
	 * @Title: secondsToTime
	 * @return String
	 * @param seconds
	 * @return
	 */
	public static String secondsToTime (int seconds) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (seconds <= 0)
            return "00:00:00";
        else {
            minute = seconds / 60;
            if (minute < 60) {
                second = seconds % 60;
                timeStr = "00:" + unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                minute = minute % 60;
                second = seconds - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

	private static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

	/**
	 * 
	 * 判断两个日期类型是否相等
	 * @Title: equalsTo
	 * @return boolean
	 * @param first
	 * @param second
	 * @return
	 */
	public static boolean equalsTo(Date first, Date second) {
		SimpleDateFormat f = new SimpleDateFormat(DATE_FORMATE);
		return f.format(first).equals(f.format(second));
	} 
}
