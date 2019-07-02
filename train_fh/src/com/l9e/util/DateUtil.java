package com.l9e.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间处理工具类
 * @author 肖有标
 *
 */
public class DateUtil {
	public static final String DATE_FMT3 = "yyyy-MM-dd HH:mm:ss";

	public static final String DATE_FMT1 = "yyyy-MM-dd";
	/**
	 * 把时间转换成字符串
	 * @param date  时间
	 * @param formatStr  要转换的格式
	 * @return
	 */
	public static String dateToString(Date date,String formatStr){
		SimpleDateFormat sdf=new SimpleDateFormat(formatStr);
		sdf.format(date);
		return sdf.format(date);
	}
	
	public static Date stringToDate(String dateStr,String formatStr){
		SimpleDateFormat sdf=new SimpleDateFormat(formatStr);
		Date date;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		return date;
	}
	
	//格式转换
	public static String stringToString(String dateStr,String formatStr){
		return dateToString(stringToDate(dateStr,formatStr), formatStr);
	}
	
	public static Date dateAddDays(Date date,int addDays){
		if(null==date){
			return null;
		}
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, addDays);
		return cal.getTime();
	}
	
	public static String dateAddHours(Date date,int addHour){
		if(null==date){
			return null;
		}
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR, addHour);
		return dateToString(cal.getTime(),DATE_FMT3);
	}
	
	public static String dateAddMin(Date date,int addDays){
		if(null==date){
			return null;
		}
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, addDays);
		return dateToString(cal.getTime(),DATE_FMT3);
	}
	
	public static void main(String[] args) {
//		System.out.println(dateAddMin(stringToDate("2015-01-28 23:01:29",DATE_FMT3),70));
		
		System.out.println(stringToString("2015-01-28 23:01",DATE_FMT3));
	}
}
